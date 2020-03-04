package wr4ith5.paper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.jar.JarEntry;

public class CommandExec implements CommandExecutor {
    private RepSystemController controller = new RepSystemController();

    public static boolean isNumeric(String s) {
        try{
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){

            String targetnotonline = String.format("%starget player must be online!", ChatColor.RED);

            if(label.equals("rep+")){
                if(args.length == 1){

                    Player target = Bukkit.getPlayer(args[0]);

                    if(target != null && target.isOnline()){
                        if(!(sender.getName().equals(target.getName()))){
                            Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getProvidingPlugin(RepSystem.class), () ->{
                                controller.addPlus(target, sender);
                            });
                        }else{
                            sender.sendMessage(String.format(("%sCan add +/- for oneself"), ChatColor.RED));
                        }
                    }else{
                        sender.sendMessage(targetnotonline);
                    }
                }else{
                    sender.sendMessage(String.format(("%sBad command usage: example %s/rep+ <player_nickname>"), ChatColor.RED, ChatColor.GOLD));
                }
                return false;
            }

            if(label.equals("rep++")){
                if(sender.isOp()){
                    if(args.length == 2){

                        Player target = Bukkit.getPlayer(args[0]);

                        if(target != null && target.isOnline()){
                            if(isNumeric(args[1])){

                                int repValue = Integer.parseInt(args[1]);

                                try{
                                    Statement setrep = RepSystem.connection.createStatement();

                                    setrep.executeUpdate("UPDATE rep SET rep_value = '"+ repValue +"' WHERE uuid = '" + target.getName() + "'");

                                    setrep.close();
                                }catch(SQLException e){
                                    e.printStackTrace();
                                }
                            }else{
                                sender.sendMessage(String.format("%sParameter must be a number", ChatColor.RED));
                            }
                        }else{
                            sender.sendMessage(targetnotonline);
                        }
                    }else{
                        sender.sendMessage(String.format(("%sBad command usage: example %s/rep- <player_nickname>"), ChatColor.RED, ChatColor.GOLD));
                    }
                }else{
                    sender.sendMessage(String.format("%sThis command is only for ops", ChatColor.RED));
                }
            }

            if(label.equals("rep-")){
                if(args.length == 1){

                    Player target = Bukkit.getPlayer(args[0]);

                        if(target != null && target.isOnline()){
                            if(!(sender.getName().equals(target.getName()))){
                                Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getProvidingPlugin(RepSystem.class), () ->{
                                    controller.addMinus(target, sender);
                                });
                            }else{
                                sender.sendMessage(String.format(("%sCan add +/- for oneself"), ChatColor.RED));
                            }
                        }else{
                            sender.sendMessage(targetnotonline);
                        }
                }else{
                    sender.sendMessage(String.format(("%sBad command usage: example %s/rep- <player_nickname>"), ChatColor.RED, ChatColor.GOLD));
                }
            }

            if(label.equals("rep?")){
                if(args.length == 1){

                    Player target = Bukkit.getPlayer(args[0]);

                    if(target != null && target.isOnline()){
                        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getProvidingPlugin(RepSystem.class), () ->{
                            controller.checkRepValue(target, sender);
                        });
                    }else{
                        sender.sendMessage(targetnotonline);
                    }
                }else{
                    sender.sendMessage(String.format(("%sBad command usage: example %s/rep? <player_nickname>"), ChatColor.RED, ChatColor.GOLD));
                }
            }
        }else{
            return false;
        }
        return true;
    }
}