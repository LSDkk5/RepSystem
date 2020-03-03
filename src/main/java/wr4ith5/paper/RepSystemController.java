package wr4ith5.paper;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                    AddDataToDatabase.fromAddPlus(targetPlayer, sender, currentRepValue);
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
                    AddDataToDatabase.fromAddMinus(targetPlayer, sender, currentRepValue);
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
                    Bukkit.broadcastMessage("§7[§c" + currentRepValue +"§7] " + targetPlayer.getName());
                }else{
                    Bukkit.broadcastMessage("§7[§a" + currentRepValue +"§7] " + targetPlayer.getName());
                }
            }

            checkRepStatement.close();

        }catch(SQLException e){
            return;
        }
    }
}