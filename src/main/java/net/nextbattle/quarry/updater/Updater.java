package net.nextbattle.quarry.updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Updater {

    private static final String DBOUrl = "http://dev.bukkit.org/server-mods/"; // Slugs will be appended to this to get to the project's RSS feed
    private static final int BYTE_SIZE = 1024; // Used for downloading files
    // Strings for reading RSS
    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String ITEM = "item";
    private final Plugin plugin;
    private final UpdateType type;
    private String versionTitle;
    private String versionLink;
    private long totalSize; // Holds the total size of the file
    //private double downloadedSize; TODO: Holds the number of bytes downloaded
    private int sizeLine; // Used for detecting file size
    private int multiplier; // Used for determining when to broadcast download updates
    private final boolean announce; // Whether to announce file downloads
    private URL url; // Connecting to RSS
    private final File file; // The plugin's file
    private final Thread thread; // Updater thread
    private final String[] noUpdateTag = {"-DEV", "-PRE", "-SNAPSHOT"}; // If the version number contains one of these, don't update.
    private final String updateFolder = YamlConfiguration.loadConfiguration(new File("bukkit.yml")).getString("settings.update-folder"); // The folder that downloads will be placed in
    private Updater.UpdateResult result = Updater.UpdateResult.SUCCESS; // Used for determining the outcome of the update process

    /**
     * Initialize the updater
     *
     * @param plugin   The plugin that is checking for an update.
     * @param slug     The dev.bukkit.org slug of the project
     *                 (http://dev.bukkit.org/server-mods/SLUG_IS_HERE)
     * @param file     The file that the plugin is running from, get this by doing
     *                 this.getFile() from within your main class.
     * @param type     Specify the type of update this will be. See
     *                 {@link UpdateType}
     * @param announce True if the program should announce the progress of new
     *                 updates in console
     */
    public Updater(Plugin plugin, String slug, File file, UpdateType type, boolean announce) {
        this.plugin = plugin;
        this.type = type;
        this.announce = announce;
        this.file = file;
        try {
            // Obtain the results of the project's file feed
            url = new URL(DBOUrl + slug + "/files.rss");
        } catch (MalformedURLException ex) {
            // Invalid slug
            plugin.getLogger().warning("The author of this plugin (" + plugin.getDescription().getAuthors().get(0) + ") has misconfigured their Auto Update system");
            plugin.getLogger().warning("The project slug given ('" + slug + "') is invalid. Please nag the author about this.");
            result = Updater.UpdateResult.FAIL_BADSLUG; // Bad slug! Bad!
        }
        thread = new Thread(new UpdateRunnable());
        thread.start();
    }

    public static void updateResultCustom(Updater updater) {
        Updater.UpdateResult result = updater.getResult();
        switch (result) {
            case SUCCESS:
                Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "NeXTQuarry has updated to the new version: " + updater.getLatestVersionString() + ".");
                Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "Please restart the server, or use the /reload command, to apply the changes.");
                break;
            case FAIL_DOWNLOAD:
                Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "There was an update available for NeXTQuarry, but the download failed. Error Code: 001");
                Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "Please download and install the new version of NeXTQuarry manually.");
                break;
            case FAIL_DBO:
                Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "There was an update available for NeXTQuarry, but the download failed. Error Code: 002");
                Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "Please download and install the new version of NeXTQuarry manually.");
                break;
            case FAIL_NOVERSION:
                Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "There was an update available for NeXTQuarry, but the download failed. Error Code: 003");
                Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "Please download and install the new version of NeXTQuarry manually.");
                break;
            case FAIL_BADSLUG:
                Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "There was an update available for NeXTQuarry, but the download failed. Error Code: 004");
                Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "Please download and install the new version of NeXTQuarry manually.");
                break;
            case UPDATE_AVAILABLE:
                Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "A new update for NeXTQuarry is available!");
                Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "Please download and install the new version of NeXTQuarry to update.");
        }
    }

    /**
     * Get the result of the update process.
     */
    public Updater.UpdateResult getResult() {
        waitForThread();
        return result;
    }

    /**
     * Get the total bytes of the file (can only be used after running a version
     * check or a normal run).
     */
    public long getFileSize() {
        waitForThread();
        return totalSize;
    }

    /**
     * Get the version string latest file avaliable online.
     */
    public String getLatestVersionString() {
        waitForThread();
        return versionTitle;
    }

    /**
     * As the result of Updater output depends on the thread's completion, it is
     * necessary to wait for the thread to finish before alloowing anyone to
     * check the result.
     */
    public void waitForThread() {
        if (thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Save an update from dev.bukkit.org into the server's update folder.
     */
    private void saveFile(File folder, String file, String u) {
        if (!folder.exists()) {
            folder.mkdir();
        }
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            // Download the file
            URL url = new URL(u);
            int fileLength = url.openConnection().getContentLength();
            in = new BufferedInputStream(url.openStream());
            fout = new FileOutputStream(folder.getAbsolutePath() + "/" + file);

            byte[] data = new byte[BYTE_SIZE];
            int count;
            if (announce) {
                plugin.getLogger().info("About to download a new update: " + versionTitle);
            }
            long downloaded = 0;
            while ((count = in.read(data, 0, BYTE_SIZE)) != -1) {
                downloaded += count;
                fout.write(data, 0, count);
                int percent = (int) (downloaded * 100 / fileLength);
                if (announce & (percent % 10 == 0)) {
                    plugin.getLogger().info("Downloading update: " + percent + "% of " + fileLength + " bytes.");
                }
            }
            //Just a quick check to make sure we didn't leave any files from last time...
            for (File xFile : new File("plugins/" + updateFolder).listFiles()) {
                if (xFile.getName().endsWith(".zip")) {
                    xFile.delete();
                }
            }
            // Check to see if it's a zip file, if it is, unzip it.
            File dFile = new File(folder.getAbsolutePath() + "/" + file);
            if (dFile.getName().endsWith(".zip")) {
                // Unzip
                unzip(dFile.getCanonicalPath());
            }
            if (announce) {
                plugin.getLogger().info("Finished updating.");
            }
        } catch (Exception ex) {
            plugin.getLogger().warning("The auto-updater tried to download a new update, but was unsuccessful.");
            result = Updater.UpdateResult.FAIL_DOWNLOAD;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (fout != null) {
                    fout.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Part of Zip-File-Extractor, modified by H31IX for use with Bukkit
     */
    private void unzip(String file) {
        try {
            File fSourceZip = new File(file);
            String zipPath = file.substring(0, file.length() - 4);
            ZipFile zipFile = new ZipFile(fSourceZip);
            Enumeration<? extends ZipEntry> e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                File destinationFilePath = new File(zipPath, entry.getName());
                destinationFilePath.getParentFile().mkdirs();
                if (entry.isDirectory()) {
                    continue;
                } else {
                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                    int b;
                    byte[] buffer = new byte[BYTE_SIZE];
                    FileOutputStream fos = new FileOutputStream(destinationFilePath);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, BYTE_SIZE);
                    while ((b = bis.read(buffer, 0, BYTE_SIZE)) != -1) {
                        bos.write(buffer, 0, b);
                    }
                    bos.flush();
                    bos.close();
                    bis.close();
                    String name = destinationFilePath.getName();
                    if (name.endsWith(".jar") && pluginFile(name)) {
                        destinationFilePath.renameTo(new File("plugins/" + updateFolder + "/" + name));
                    }
                }
                entry = null;
                destinationFilePath = null;
            }
            e = null;
            zipFile.close();
            zipFile = null;
            // Move any plugin data folders that were included to the right place, Bukkit won't do this for us.
            for (File dFile : new File(zipPath).listFiles()) {
                if (dFile.isDirectory()) {
                    if (pluginFile(dFile.getName())) {
                        File oFile = new File("plugins/" + dFile.getName()); // Get current dir
                        File[] contents = oFile.listFiles(); // List of existing files in the current dir
                        for (File cFile : dFile.listFiles()) // Loop through all the files in the new dir
                        {
                            boolean found = false;
                            for (File xFile : contents) // Loop through contents to see if it exists
                            {
                                if (xFile.getName().equals(cFile.getName())) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                // Move the new file into the current dir
                                cFile.renameTo(new File(oFile.getCanonicalFile() + "/" + cFile.getName()));
                            } else {
                                // This file already exists, so we don't need it anymore.
                                cFile.delete();
                            }
                        }
                    }
                }
                dFile.delete();
            }
            new File(zipPath).delete();
            fSourceZip.delete();
        } catch (IOException ex) {
            ex.printStackTrace();
            plugin.getLogger().warning("The auto-updater tried to unzip a new update file, but was unsuccessful.");
            result = Updater.UpdateResult.FAIL_DOWNLOAD;
        }
        new File(file).delete();
    }

    /**
     * Check if the name of a jar is one of the plugins currently installed,
     * used for extracting the correct files out of a zip.
     */
    public boolean pluginFile(String name) {
        for (File file : new File("plugins").listFiles()) {
            if (file.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtain the direct download file url from the file's page.
     */
    private String getFile(String link) {
        String download = null;
        try {
            // Open a connection to the page
            URL url = new URL(link);
            URLConnection urlConn = url.openConnection();
            InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
            BufferedReader buff = new BufferedReader(inStream);

            int counter = 0;
            String line;
            while ((line = buff.readLine()) != null) {
                counter++;
                // Search for the download link
                if (line.contains("<li class=\"user-action user-action-download\">")) {
                    // Get the raw link
                    download = line.split("<a href=\"")[1].split("\">Download</a>")[0];
                } // Search for size
                else if (line.contains("<dt>Size</dt>")) {
                    sizeLine = counter + 1;
                } else if (counter == sizeLine) {
                    String size = line.replaceAll("<dd>", "").replaceAll("</dd>", "");
                    multiplier = size.contains("MiB") ? 1048576 : 1024;
                    size = size.replace(" KiB", "").replace(" MiB", "");
                    totalSize = (long) (Double.parseDouble(size) * multiplier);
                }
            }
            urlConn = null;
            inStream = null;
            buff.close();
            buff = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            plugin.getLogger().warning("The auto-updater tried to contact dev.bukkit.org, but was unsuccessful.");
            result = Updater.UpdateResult.FAIL_DBO;
            return null;
        }
        return download;
    }

    /**
     * Check to see if the program should continue by evaluation whether the
     * plugin is already updated, or shouldn't be updated
     */
    private boolean versionCheck(String title) {
        if (type != UpdateType.NO_VERSION_CHECK) {
            String version = plugin.getDescription().getVersion();
            if (title.split(" v").length == 2) {
                String remoteVersion = title.split(" v")[1].split(" ")[0]; // Get the newest file's version number
                int remVer = -1, curVer = 0;
                try {
                    remVer = calVer(remoteVersion);
                    curVer = calVer(version);
                } catch (NumberFormatException nfe) {
                    remVer = -1;
                }
                if (hasTag(version) || version.equalsIgnoreCase(remoteVersion) || curVer >= remVer) {
                    // We already have the latest version, or this build is tagged for no-update
                    result = Updater.UpdateResult.NO_UPDATE;
                    return false;
                }
            } else {
                // The file's name did not contain the string 'vVersion'
                plugin.getLogger().warning("The author of this plugin (" + plugin.getDescription().getAuthors().get(0) + ") has misconfigured their Auto Update system");
                plugin.getLogger().warning("Files uploaded to BukkitDev should contain the version number, seperated from the name by a 'v', such as PluginName v1.0");
                plugin.getLogger().warning("Please notify the author of this error.");
                result = Updater.UpdateResult.FAIL_NOVERSION;
                return false;
            }
        }
        return true;
    }

    /**
     * Used to calculate the version string as an Integer
     */
    private Integer calVer(String s) throws NumberFormatException {
        if (s.contains(".")) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                Character c = s.charAt(i);
                if (Character.isLetterOrDigit(c)) {
                    sb.append(c);
                }
            }
            return Integer.parseInt(sb.toString());
        }
        return Integer.parseInt(s);
    }

    /**
     * Evaluate whether the version number is marked showing that it should not
     * be updated by this program
     */
    private boolean hasTag(String version) {
        for (String string : noUpdateTag) {
            if (version.contains(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Part of RSS Reader by Vogella, modified by H31IX for use with Bukkit
     */
    private boolean readFeed() {
        try {
            // Set header values intial to the empty string
            String title = "";
            String link = "";
            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = read();
            if (in != null) {
                XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
                // Read the XML document
                while (eventReader.hasNext()) {
                    XMLEvent event = eventReader.nextEvent();
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart().equals(TITLE)) {
                            event = eventReader.nextEvent();
                            title = event.asCharacters().getData();
                            continue;
                        }
                        if (event.asStartElement().getName().getLocalPart().equals(LINK)) {
                            event = eventReader.nextEvent();
                            link = event.asCharacters().getData();
                            continue;
                        }
                    } else if (event.isEndElement()) {
                        if (event.asEndElement().getName().getLocalPart().equals(ITEM)) {
                            // Store the title and link of the first entry we get - the first file on the list is all we need
                            versionTitle = title;
                            versionLink = link;
                            // All done, we don't need to know about older files.
                            break;
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (XMLStreamException e) {
            plugin.getLogger().warning("Could not reach dev.bukkit.org for update checking. Is it offline?");
            return false;
        }
    }

    /**
     * Open the RSS feed
     */
    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException e) {
            plugin.getLogger().warning("Could not reach BukkitDev file stream for update checking. Is dev.bukkit.org offline?");
            return null;
        }
    }

    /**
     * Gives the dev the result of the update process. Can be obtained by called
     * getResult().
     */
    public enum UpdateResult {

        /**
         * The updater found an update, and has readied it to be loaded the next
         * time the server restarts/reloads.
         */
        SUCCESS,
        /**
         * The updater did not find an update, and nothing was downloaded.
         */
        NO_UPDATE,
        /**
         * The updater found an update, but was unable to download it.
         */
        FAIL_DOWNLOAD,
        /**
         * For some reason, the updater was unable to contact dev.bukkit.org to
         * download the file.
         */
        FAIL_DBO,
        /**
         * When running the version check, the file on DBO did not contain the a
         * version in the format 'vVersion' such as 'v1.0'.
         */
        FAIL_NOVERSION,
        /**
         * The slug provided by the plugin running the updater was invalid and
         * doesn't exist on DBO.
         */
        FAIL_BADSLUG,
        /**
         * The updater found an update, but because of the UpdateType being set
         * to NO_DOWNLOAD, it wasn't downloaded.
         */
        UPDATE_AVAILABLE
    }

    /**
     * Allows the dev to specify the type of update that will be run.
     */
    public enum UpdateType {

        /**
         * Run a version check, and then if the file is out of date, download
         * the newest version.
         */
        DEFAULT,
        /**
         * Don't run a version check, just find the latest update and download
         * it.
         */
        NO_VERSION_CHECK,
        /**
         * Get information about the version and the download size, but don't
         * actually download anything.
         */
        NO_DOWNLOAD
    }

    private class UpdateRunnable implements Runnable {

        public void run() {
            if (url != null) {
                // Obtain the results of the project's file feed
                if (readFeed()) {
                    if (versionCheck(versionTitle)) {
                        String fileLink = getFile(versionLink);
                        if (fileLink != null && type != UpdateType.NO_DOWNLOAD) {
                            String name = file.getName();
                            // If it's a zip file, it shouldn't be downloaded as the plugin's name
                            if (fileLink.endsWith(".zip")) {
                                String[] split = fileLink.split("/");
                                name = split[split.length - 1];
                            }
                            saveFile(new File("plugins/" + updateFolder), name, fileLink);
                        } else {
                            result = UpdateResult.UPDATE_AVAILABLE;
                        }
                    }
                }
            }
        }
    }
}
