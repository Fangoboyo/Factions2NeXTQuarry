package net.nextbattle.quarry.entities;

import net.nextbattle.quarry.main.MainClass;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cmnd.getName().equalsIgnoreCase("nextquarry")) {
            if (args.length == 1 && args[0].equals("give")) {
                cs.sendMessage(ChatColor.GOLD + "Items to choose from: tier1, tier2, tier3, wrench, speedupgrade, fuelinjector, fuelfinder, fuelupgrade, smelter, liquidminer & chestminer.");
                return true;
            }
            if (args.length == 2 && args[0].equals("give")) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(ChatColor.GOLD + "You can't execute this command from the console.");
                    return false;
                }
                
                Player p = (Player) cs;

                if (!p.hasPermission("nextquarry.admin")) {
                    cs.sendMessage(ChatColor.GOLD + "You are not allowed to execute this command with the give parameter!");
                    return true;
                }
                if (args[1].equals("tier1")) {
                    p.getInventory().addItem(MainClass.citems.quarry_tier1);
                } else if (args[1].equals("tier2")) {
                    p.getInventory().addItem(MainClass.citems.quarry_tier2);
                } else if (args[1].equals("tier3")) {
                    p.getInventory().addItem(MainClass.citems.quarry_tier3);
                } else if (args[1].equals("wrench")) {
                    p.getInventory().addItem(MainClass.citems.wrench_tool);
                } else if (args[1].equals("speedupgrade")) {
                    p.getInventory().addItem(MainClass.citems.speed_upgrade);
                } else if (args[1].equals("fuelinjector")) {
                    p.getInventory().addItem(MainClass.citems.fuel_tool);
                } else if (args[1].equals("fuelfinder")) {
                    p.getInventory().addItem(MainClass.citems.fuel_finder_upgrade);
                } else if (args[1].equals("chestminer")) {
                    p.getInventory().addItem(MainClass.citems.chest_miner);
                }else if (args[1].equals("smelter")) {
                    p.getInventory().addItem(MainClass.citems.smelter_upgrade);
                } else if (args[1].equals("fuelupgrade")) {
                    p.getInventory().addItem(MainClass.citems.fuel_efficiency_upgrade);
                }  else if (args[1].equals("liquidminer")) {
                    p.getInventory().addItem(MainClass.citems.liquid_miner);
                } else {
                    p.sendMessage(ChatColor.GOLD + "This item does not exist!");
                    p.sendMessage(ChatColor.GOLD + "Items to choose from: tier1, tier2, tier3, wrench, speedupgrade, fuelinjector, fuelfinder, fuelupgrade, smelter, liquidminer & chestminer.");
                }
                return true;
            }

            cs.sendMessage(ChatColor.GOLD + "----[NeXTQuarry Plugin Info]----");
            cs.sendMessage(ChatColor.GOLD + "NeXTQuarry v1.5.1 - Coded by BeMacized");
            cs.sendMessage(ChatColor.GOLD + "Website: http://www.nextbattle.net/");
            if (cs instanceof Player) {
                Player p = (Player) cs;
                cs.sendMessage(ChatColor.GOLD + "----[NeXTQuarry Permissions]----");
                if (p.hasPermission("nextquarry.user.tier1")) {
                    cs.sendMessage(ChatColor.GREEN + "You are allowed to place tier 1 quarries.");
                } else {
                    cs.sendMessage(ChatColor.RED + "You are NOT allowed to place tier 1 quarries.");
                }
                if (p.hasPermission("nextquarry.user.tier2")) {
                    cs.sendMessage(ChatColor.GREEN + "You are allowed to place tier 2 quarries.");
                } else {
                    cs.sendMessage(ChatColor.RED + "You are NOT allowed to place tier 2 quarries.");
                }
                if (p.hasPermission("nextquarry.user.tier3")) {
                    cs.sendMessage(ChatColor.GREEN + "You are allowed to place tier 3 quarries.");
                } else {
                    cs.sendMessage(ChatColor.RED + "You are NOT allowed to place tier 3 quarries.");
                }
                if (MainClass.config.privatequarries) {
                    if (p.hasPermission("nextquarry.admin")) {
                        cs.sendMessage(ChatColor.GREEN + "You are allowed to break all quarries.");
                        cs.sendMessage(ChatColor.GREEN + "You are allowed to edit all quarries.");
                    } else if (p.hasPermission("nextquarry.user.remove")) {
                        cs.sendMessage(ChatColor.GOLD + "You are allowed to break YOUR quarries only.");
                        if (p.hasPermission("nextquarry.user.edit")) {
                            cs.sendMessage(ChatColor.GOLD + "You are allowed to edit YOUR quarries only.");
                        } else {
                            cs.sendMessage(ChatColor.RED + "You are NOT allowed to edit quarries.");
                        }
                    } else {
                        cs.sendMessage(ChatColor.RED + "You are NOT allowed to break quarries.");
                        if (p.hasPermission("nextquarry.user.edit")) {
                            cs.sendMessage(ChatColor.GOLD + "You are allowed to edit YOUR quarries only.");
                        } else {
                            cs.sendMessage(ChatColor.RED + "You are NOT allowed to edit quarries.");
                        }
                    }

                } else {
                    if (p.hasPermission("nextquarry.admin")) {
                        cs.sendMessage(ChatColor.GREEN + "You are allowed to edit all quarries.");
                    }
                    if (p.hasPermission("nextquarry.admin") || p.hasPermission("nextquarry.user.remove")) {
                        cs.sendMessage(ChatColor.GREEN + "You are allowed to break all quarries.");
                    } else {
                        cs.sendMessage(ChatColor.RED + "You are NOT allowed to break quarries.");
                    }
                    if (p.hasPermission("nextquarry.user.edit")) {
                        cs.sendMessage(ChatColor.GREEN + "You are allowed to edit all quarries.");
                    } else {
                        cs.sendMessage(ChatColor.RED + "You are NOT allowed to edit quarries.");
                    }
                }
            }
            return true;
        }

        return true;
    }
}
