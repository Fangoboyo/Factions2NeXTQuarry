package net.nextbattle.quarry.entities;

import net.nextbattle.quarry.main.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CustomItems {

    public ItemStack quarry_tier1;
    public ItemStack quarry_tier2;
    public ItemStack quarry_tier3;
    public ItemStack speed_upgrade;
    public ItemStack wrench_tool;
    public ItemStack fuel_tool;
    public ItemStack fuel_finder_upgrade;
    public ItemStack chest_miner;
    public ItemStack fuel_efficiency_upgrade;
    public ItemStack smelter_upgrade;
    public ItemStack liquid_miner;

    public CustomItems() {
        //Work Vars
        ItemStack is;
        ItemMeta meta;
        ArrayList<String> lorelist;

        //Fuel Efficiency Upgrade
        is = new ItemStack(MainClass.config.fuel_efficiency_upgrade, 1);
        meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + "Fuel Efficiency Upgrade");
        lorelist = new ArrayList<String>();
        lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD + "Makes the quarry last longer per unit of fuel");
        lorelist.add(ChatColor.RESET + "" + ChatColor.RED + "Max 3 per quarry");
        meta.setLore(lorelist);
        is.setItemMeta(meta);
        fuel_efficiency_upgrade = is;
        
        //Smelter Upgrade
        is = new ItemStack(MainClass.config.smelter_upgrade, 1);
        meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + "Smelter Upgrade");
        lorelist = new ArrayList<String>();
        lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD + "Gives the quarry the ability to smelt ores");
        lorelist.add(ChatColor.RESET + "" + ChatColor.RED + "Max 1 per quarry");
        meta.setLore(lorelist);
        is.setItemMeta(meta);
        smelter_upgrade = is;
        
        //Liquid Miner
        is = new ItemStack(MainClass.config.liquid_miner, 1);
        meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + "Liquid Miner");
        lorelist = new ArrayList<String>();
        lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD + "Allows buckets to be filled with found liquids");
        lorelist.add(ChatColor.RESET + "" + ChatColor.RED + "Max 1 per quarry");
        meta.setLore(lorelist);
        is.setItemMeta(meta);
        liquid_miner = is;
        
        //Tier 1 Quarry
        is = new ItemStack(Material.IRON_BLOCK, 1);
        meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GRAY + "Quarry [Tier 1]");
        lorelist = new ArrayList<String>();
        lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD + "Mines a 16x16 area");
        meta.setLore(lorelist);
        is.setItemMeta(meta);
        quarry_tier1 = is;

        //Tier 2 Quarry
        is = new ItemStack(Material.GOLD_BLOCK, 1);
        meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + "Quarry [Tier 2]");
        lorelist = new ArrayList<String>();
        lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD + "Mines a 32x32 area");
        meta.setLore(lorelist);
        is.setItemMeta(meta);
        quarry_tier2 = is;

        //Tier 3 Quarry
        is = new ItemStack(Material.OBSIDIAN, 1);
        meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.AQUA + "Quarry [Tier 3]");
        lorelist = new ArrayList<String>();
        lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD + "Mines a 48x48 area");
        meta.setLore(lorelist);
        is.setItemMeta(meta);
        quarry_tier3 = is;

        //Speed Upgrade
        is = new ItemStack(MainClass.config.speed_upgrade, 1);
        meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + "Quarry Speed Upgrade");
        lorelist = new ArrayList<String>();
        lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD + "Makes the quarry a bit faster");
        lorelist.add(ChatColor.RESET + "" + ChatColor.RED + "Max 3 per quarry");
        meta.setLore(lorelist);
        is.setItemMeta(meta);
        speed_upgrade = is;

        //Fuel Finder Upgrade
        is = new ItemStack(MainClass.config.fuel_finder_upgrade, 1);
        meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + "Quarry Fuel Finder");
        lorelist = new ArrayList<String>();
        lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD + "Automatically transfers coal found to the fuel bay");
        lorelist.add(ChatColor.RESET + "" + ChatColor.RED + "Max 1 per quarry");
        meta.setLore(lorelist);
        is.setItemMeta(meta);
        fuel_finder_upgrade = is;

        //Wrench Tool
        is = new ItemStack(MainClass.config.wrench_tool, 1);
        meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.YELLOW + "Quarry Wrench");
        lorelist = new ArrayList<String>();
        lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD + "Right click a quarry with this to apply upgrades!");
        meta.setLore(lorelist);
        is.setItemMeta(meta);
        wrench_tool = is;

        //Fuel Tool
        is = new ItemStack(MainClass.config.fuel_tool, 1);
        meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.YELLOW + "Quarry Fuel Injector");
        lorelist = new ArrayList<String>();
        lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD + "Right click a quarry with this to inject fuel!");
        meta.setLore(lorelist);
        is.setItemMeta(meta);
        fuel_tool = is;

        //Chest Miner
        is = new ItemStack(MainClass.config.chest_miner, 1);
        meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + "Chest Mining Upgrade");
        lorelist = new ArrayList<String>();
        lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD + "Allows you to mine the contents of chests.");
        lorelist.add(ChatColor.RESET + "" + ChatColor.RED + "Max 1 per quarry");
        meta.setLore(lorelist);
        is.setItemMeta(meta);
        chest_miner = is;
        
    }

    public void addRecipes() {
        //Work Variable
        ShapedRecipe recipe;

        //Fuel Efficiency Upgrade
        recipe = new ShapedRecipe(fuel_efficiency_upgrade);
        recipe.shape("ABA", "BCB", "ABA");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.REDSTONE);
        recipe.setIngredient('C', Material.DIAMOND);
        Bukkit.getServer().addRecipe(recipe);
        
        //Liquid Miner
        recipe = new ShapedRecipe(liquid_miner);
        recipe.shape("A A", "A A", " B ");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.BUCKET);
        Bukkit.getServer().addRecipe(recipe);
        
        //Tier 1 Quarry
        recipe = new ShapedRecipe(quarry_tier1);
        recipe.shape("ABA", "ACA", "ADA");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.REDSTONE);
        recipe.setIngredient('C', Material.HOPPER);
        recipe.setIngredient('D', Material.DIAMOND_PICKAXE);
        Bukkit.getServer().addRecipe(recipe);

        //Tier 2 Quarry
        recipe = new ShapedRecipe(quarry_tier2);
        recipe.shape("ABA", "ACA", "ADA");
        recipe.setIngredient('A', Material.GOLD_INGOT);
        recipe.setIngredient('B', Material.REDSTONE);
        recipe.setIngredient('C', Material.HOPPER);
        recipe.setIngredient('D', Material.DIAMOND_PICKAXE);
        Bukkit.getServer().addRecipe(recipe);

        //Tier 3 Quarry
        recipe = new ShapedRecipe(quarry_tier3);
        recipe.shape("ABA", "ACA", "ADA");
        recipe.setIngredient('A', Material.OBSIDIAN);
        recipe.setIngredient('B', Material.REDSTONE_BLOCK);
        recipe.setIngredient('C', Material.HOPPER);
        recipe.setIngredient('D', Material.DIAMOND_PICKAXE);
        Bukkit.getServer().addRecipe(recipe);

        //Speed Upgrade
        recipe = new ShapedRecipe(speed_upgrade);
        recipe.shape("ADA", "BCB", "ADA");
        recipe.setIngredient('A', Material.GOLD_INGOT);
        recipe.setIngredient('B', Material.GLASS);
        recipe.setIngredient('C', Material.REDSTONE);
        recipe.setIngredient('D', Material.STRING);
        Bukkit.getServer().addRecipe(recipe);

        //Fuel Finder Upgrade
        recipe = new ShapedRecipe(fuel_finder_upgrade);
        recipe.shape("ABA", "BCB", "ABA");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.STICK);
        recipe.setIngredient('C', Material.COAL);
        Bukkit.getServer().addRecipe(recipe);

        //Wrench Tool
        recipe = new ShapedRecipe(wrench_tool);
        recipe.shape("B B", " A ", " A ");
        recipe.setIngredient('A', Material.GOLD_INGOT);
        recipe.setIngredient('B', Material.IRON_INGOT);
        Bukkit.getServer().addRecipe(recipe);

        //Fuel Tool
        recipe = new ShapedRecipe(fuel_tool);
        recipe.shape("ABA", "A A", " C ");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.BUCKET);
        recipe.setIngredient('C', Material.HOPPER);
        Bukkit.getServer().addRecipe(recipe);

        //Chest Miner upgrade
        recipe = new ShapedRecipe(chest_miner);
        recipe.shape(" A ", " B ", " C ");
        recipe.setIngredient('A', Material.REDSTONE);
        recipe.setIngredient('B', Material.GOLD_HOE);
        recipe.setIngredient('C', Material.CHEST);
        Bukkit.getServer().addRecipe(recipe);
        
        //Smelter upgrade
        recipe = new ShapedRecipe(smelter_upgrade);
        recipe.shape("ABA", "CDA", "ABA");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.HOPPER);
        recipe.setIngredient('C', Material.REDSTONE);
        recipe.setIngredient('D', Material.FURNACE);
        Bukkit.getServer().addRecipe(recipe);

    }
    
    public static boolean customItemsMatch(ItemStack first, ItemStack second)
    {
        if (first.getItemMeta().equals(second.getItemMeta())) {
            return true;
        }
        return false;
    }
}
