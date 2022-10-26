package net.nextbattle.quarry.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.nextbattle.quarry.main.MainClass;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {

    public ArrayList<Material> cantbreak;
    public ArrayList<String> world_whitelist;
    public boolean privatequarries;
    public boolean autoupdate;
    public boolean updatenotify;
    public boolean continue_when_unloaded;
    public boolean continue_when_offline;
    public boolean dev_join_message;
    public boolean send_usage_data;
    public boolean world_whitelist_enabled;
    public boolean draw_all_beams;
    public Material speed_upgrade = Material.WATCH;
    public Material wrench_tool = Material.BLAZE_ROD;
    public Material fuel_tool = Material.BUCKET;
    public Material fuel_finder_upgrade = Material.TRAP_DOOR;
    public Material chest_miner = Material.GOLD_AXE;
    public Material fuel_efficiency_upgrade = Material.ENDER_PORTAL_FRAME;
    public Material smelter_upgrade = Material.FURNACE;
    public Material liquid_miner = Material.HOPPER;
    public int globalmaxquarries = 256;
    public int maxquarriestier1 = 8;
    public int maxquarriestier2 = 8;
    public int maxquarriestier3 = 8;
    public int user_max_quarries = 24;
    public int save_interval = 60;

    public static void loadConfig() {
        FileConfiguration fc = new YamlConfiguration().loadConfiguration(new File(MainClass.plugin.getDataFolder(), "config.yml"));
        MainClass.config = new Configuration(false);
        List<?> list = fc.getList("ignored-blocks");
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                try {
                    MainClass.config.addCantBreak(Material.getMaterial(Integer.parseInt(list.get(i).toString())));
                } catch (Exception e) {
                }
            }
        }
        list = fc.getList("world-whitelist");
        ArrayList<String> worlds = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                try {
                    worlds.add(list.get(i).toString());
                } catch (Exception e) {
                }
            }
            MainClass.config.world_whitelist = worlds;
        }
        MainClass.config.speed_upgrade = Material.getMaterial(fc.getInt("speed_upgrade"));
        MainClass.config.wrench_tool = Material.getMaterial(fc.getInt("wrench_tool"));
        MainClass.config.fuel_tool = Material.getMaterial(fc.getInt("fuel_tool"));
        MainClass.config.fuel_finder_upgrade = Material.getMaterial(fc.getInt("fuel_finder_upgrade"));
        MainClass.config.fuel_efficiency_upgrade = Material.getMaterial(fc.getInt("fuel_efficiency_upgrade"));
        MainClass.config.chest_miner = Material.getMaterial(fc.getInt("chest_miner"));
        MainClass.config.smelter_upgrade = Material.getMaterial(fc.getInt("smelter_upgrade"));
        MainClass.config.liquid_miner = Material.getMaterial(fc.getInt("liquid_miner"));
        MainClass.config.privatequarries = fc.getBoolean("private-quarries");
        MainClass.config.autoupdate = fc.getBoolean("auto-update");
        MainClass.config.updatenotify = fc.getBoolean("update-notify");
        MainClass.config.continue_when_unloaded = fc.getBoolean("continue-when-unloaded");
        MainClass.config.continue_when_offline = fc.getBoolean("continue-when-offline");
        MainClass.config.dev_join_message = fc.getBoolean("dev-join-message");
        MainClass.config.send_usage_data = fc.getBoolean("send-usage-data");
        MainClass.config.globalmaxquarries = fc.getInt("global-max-quarries");
        MainClass.config.maxquarriestier1 = fc.getInt("user-max-tier-1-quarries");
        MainClass.config.maxquarriestier2 = fc.getInt("user-max-tier-2-quarries");
        MainClass.config.maxquarriestier3 = fc.getInt("user-max-tier-3-quarries");
        MainClass.config.world_whitelist_enabled = fc.getBoolean("world-whitelist-enabled");
        MainClass.config.draw_all_beams = fc.getBoolean("draw-all-beams");
        MainClass.config.user_max_quarries = fc.getInt("user-max-quarries");
        MainClass.config.save_interval = fc.getInt("save-interval");
        if (MainClass.config.save_interval == 0) { MainClass.config.save_interval = 60; }
    }

    public Configuration() {
        cantbreak = new ArrayList<>();
        cantbreak.add(Material.BEDROCK);
        cantbreak.add(Material.OBSIDIAN);
        cantbreak.add(Material.STATIONARY_WATER);
        cantbreak.add(Material.STATIONARY_LAVA);
        cantbreak.add(Material.WATER);
        cantbreak.add(Material.LAVA);
        privatequarries = true;
        autoupdate = false;
        updatenotify = true;
        continue_when_unloaded = false;
        continue_when_offline = false;
        dev_join_message = true;
        send_usage_data = true;
    }

    public Configuration(boolean default_cantbreak) {
        cantbreak = new ArrayList<>();
        if (default_cantbreak) {
            cantbreak.add(Material.BEDROCK);
            cantbreak.add(Material.OBSIDIAN);
            cantbreak.add(Material.STATIONARY_WATER);
            cantbreak.add(Material.STATIONARY_LAVA);
            cantbreak.add(Material.WATER);
            cantbreak.add(Material.LAVA);
        }
    }

    public void addCantBreak(Material mat) {
        if (!cantbreak.contains(mat) && mat != null) {
            cantbreak.add(mat);
        }
    }
}
