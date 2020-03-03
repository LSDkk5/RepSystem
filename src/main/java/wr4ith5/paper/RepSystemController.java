package wr4ith5.paper;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class RepSystemController{

    private  static String limitReached = "§cLimit reached, wait 12hours";

    public static void AddPlus(Player targetPlayer, CommandSender sender){
        try{
            Statement targetPlayerStatement = RepSystem.connection.createStatement();
            Statement senderStatement = RepSystem.connection.createStatement();

            ResultSet targetPlayerResult = targetPlayerStatement.executeQuery("SELECT * FROM rep WHERE uuid = '"+targetPlayer.getUniqueId() + "'");
            ResultSet senderResult = senderStatement.executeQuery("SELECT when_can_add FROM rep WHERE uuid = '" + ((Player) sender).getUniqueId() + "'");

            boolean PlayerExist = targetPlayerResult.getInt(1) == 1;
            int currentRepValue = targetPlayerResult.getInt(3);

            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime whenCanAdd = LocalDateTime.parse(senderResult.getString("when_can_add"));

            if(currentDateTime.isAfter(whenCanAdd)){
                if(PlayerExist){

                    Statement addPlusStatement = RepSystem.connection.createStatement();

                    Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(RepSystem.class), () ->{
                        try{
                            addPlusStatement.executeUpdate("UPDATE rep SET rep_value = '" + (currentRepValue+1) + "' WHERE uuid = '" + (targetPlayer.getUniqueId()) + "'");
                            addPlusStatement.executeUpdate("UPDATE rep SET when_can_add = '" + (LocalDateTime.now().plusHours(12)) + "' WHERE uuid = '" + ((Player) sender).getUniqueId() + "'");
                            Bukkit.broadcastMessage("§7[§a+§7] " + sender.getName() + " §6> §7"+targetPlayer.getName());;

                            addPlusStatement.close();
                        }catch(SQLException e){
                            RepSystem.log.warning("[RepS] Error occurred while processing your request");
                            RepSystem.log.warning(e.toString());
                        }
                    }, 30);
                }
            }else{
                sender.sendMessage(limitReached);
            }

            targetPlayerStatement.close();
            senderStatement.close();
        }catch(SQLException e){
            RepSystem.log.warning("[RepS] Error occurred while processing your request");
            RepSystem.log.warning(e.toString());
            return;
        }

    }

    public static void AddMinus(Player targetPlayer, CommandSender sender){
        try{
            Statement targetPlayerStatement = RepSystem.connection.createStatement();
            Statement senderStatement = RepSystem.connection.createStatement();

            ResultSet targetPlayerResult = targetPlayerStatement.executeQuery("SELECT * FROM rep WHERE uuid = '"+targetPlayer.getUniqueId() + "'");
            ResultSet senderResult = senderStatement.executeQuery("SELECT when_can_add FROM rep WHERE uuid = '" + ((Player) sender).getUniqueId() + "'");

            boolean PlayerExist = targetPlayerResult.getInt(1) == 1;
            int currentRepValue = targetPlayerResult.getInt(3);

            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime whenCanAdd = LocalDateTime.parse(senderResult.getString("when_can_add"));


            if(currentDateTime.isAfter(whenCanAdd)){
                if(PlayerExist){

                    Statement addMinusStatement = RepSystem.connection.createStatement();

                    Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(RepSystem.class), () ->{
                        try{

                            addMinusStatement.executeUpdate("UPDATE rep SET rep_value = '" + (currentRepValue-1) + "' WHERE uuid = '" + (targetPlayer.getUniqueId()) + "'");
                            addMinusStatement.executeUpdate("UPDATE rep SET when_can_add = '" + (LocalDateTime.now().plusHours(12)) + "' WHERE uuid = '" + ((Player) sender).getUniqueId() + "'");
                            Bukkit.broadcastMessage("§7[§c-§7] " + sender.getName() + " §6> §7 "+targetPlayer.getName());

                            addMinusStatement.close();

                        }catch(SQLException e){
                            RepSystem.log.warning("[RepS] Error occurred while processing your request");
                            RepSystem.log.warning(e.toString());
                        }
                    }, 30);
                }
            }else{
                sender.sendMessage(limitReached);
            }

            targetPlayerStatement.close();
            senderStatement.close();
        }catch(SQLException e){
            RepSystem.log.warning("[RepS] Error occurred while processing your request");
            RepSystem.log.warning(e.toString());
            return;
        }
    }

    public static void CheckRepValue(Player targetPlayer, CommandSender sender){

        Statement checkRepStatement;

        try{
            checkRepStatement = RepSystem.connection.createStatement();

            ResultSet result = checkRepStatement.executeQuery("SELECT * FROM rep WHERE uuid = '" + targetPlayer.getUniqueId() + "'");

            boolean PlayerExist = result.getInt(1) == 1;
            int currentRepValue = result.getInt("rep_value");

            if(PlayerExist){
                if(currentRepValue<0){
                    sender.sendMessage("§7[§c" + currentRepValue +"§7] " + targetPlayer.getName());
                }else{
                    sender.sendMessage("§7[§a" + currentRepValue +"§7] " + targetPlayer.getName());
                }
            }

            checkRepStatement.close();

        }catch(SQLException e){
            RepSystem.log.warning("[RepS] Error occurred while processing your request");
            RepSystem.log.warning(e.toString());
        }
    }
}