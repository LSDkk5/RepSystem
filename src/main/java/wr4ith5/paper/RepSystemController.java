package wr4ith5.paper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class RepSystemController{

    private String limitreached = "§cLimit reached, wait 12hours";

    public void addPlus(Player targetPlayer, CommandSender sender){
        try{
            Statement targetplayer = RepSystem.connection.createStatement();
            Statement getsenderdata = RepSystem.connection.createStatement();

            ResultSet targetPlayerResult = targetplayer.executeQuery("SELECT * FROM rep WHERE uuid = '" + targetPlayer.getName() + "'");
            ResultSet senderResult = getsenderdata.executeQuery("SELECT when_can_add FROM rep WHERE uuid = '" + sender.getName() + "'");

            boolean PlayerExist = targetPlayerResult.getInt(1) == 1;
            int currentRepValue = targetPlayerResult.getInt(3);

            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime whenCanAdd = LocalDateTime.parse(senderResult.getString("when_can_add"));

            if(currentDateTime.isAfter(whenCanAdd)){

                if(PlayerExist){
                    Statement addplus = RepSystem.connection.createStatement();

                    Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(RepSystem.class), () ->{
                        try{
                            addplus.executeUpdate("UPDATE rep SET rep_value = '" + (currentRepValue+1) + "' WHERE uuid = '" + (targetPlayer.getName()) + "'");
                            addplus.executeUpdate("UPDATE rep SET when_can_add = '" + (LocalDateTime.now().plusHours(12)) + "' WHERE uuid = '" + (sender.getName()) + "'");
                            Bukkit.broadcastMessage(String.format("%s[%s+%s]" + sender.getName() + " %s>%s " + targetPlayer.getName(), ChatColor.GRAY, ChatColor.GREEN, ChatColor.GRAY, ChatColor.GOLD , ChatColor.GRAY));

                            addplus.close();
                        }catch(SQLException e){
                            RepSystem.log.warning("[RepS] Error occurred while processing your request");
                            RepSystem.log.warning(e.toString());
                        }
                    }, 30);
                }
            }else{
                sender.sendMessage(limitreached);
            }

            targetplayer.close();
            getsenderdata.close();
        }catch(SQLException e){
            RepSystem.log.warning("[RepS] Error occurred while processing your request");
            RepSystem.log.warning(e.toString());
            return;
        }

    }

    public void addMinus(Player targetPlayer, CommandSender sender){
        try{
            Statement gettargetplayer = RepSystem.connection.createStatement();
            Statement getsenderdata = RepSystem.connection.createStatement();

            ResultSet playerresult = gettargetplayer.executeQuery("SELECT * FROM rep WHERE uuid = '" + (targetPlayer.getName()) + "'");
            ResultSet senderresult = getsenderdata.executeQuery("SELECT when_can_add FROM rep WHERE uuid = '" + (sender.getName()) + "'");

            boolean PlayerExist = playerresult.getInt(1) == 1;
            int currentRepValue = playerresult.getInt(3);

            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime whenCanAdd = LocalDateTime.parse(senderresult.getString("when_can_add"));


            if(currentDateTime.isAfter(whenCanAdd)){
                if(PlayerExist){

                    Statement addminus = RepSystem.connection.createStatement();

                    Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(RepSystem.class), () ->{
                        try{

                            addminus.executeUpdate("UPDATE rep SET rep_value = '" + (currentRepValue-1) + "' WHERE uuid = '" + (targetPlayer.getName()) + "'");
                            addminus.executeUpdate("UPDATE rep SET when_can_add = '" + (LocalDateTime.now().plusHours(12)) + "' WHERE uuid = '" + (sender.getName()) + "'");
                            Bukkit.broadcastMessage(String.format("%s[%s-%s]" + sender.getName() + " %s>%s " + targetPlayer.getName(), ChatColor.GRAY, ChatColor.RED, ChatColor.GRAY, ChatColor.GOLD , ChatColor.GRAY));

                            addminus.close();

                        }catch(SQLException e){
                            RepSystem.log.warning("[RepS] Error occurred while processing your request");
                            RepSystem.log.warning(e.toString());
                        }
                    }, 30);
                }
            }else{
                sender.sendMessage(limitreached);
            }

            gettargetplayer.close();
            getsenderdata.close();
        }catch(SQLException e){
            RepSystem.log.warning("[RepS] Error occurred while processing your request");
            RepSystem.log.warning(e.toString());
            return;
        }
    }

    public void checkRepValue(Player targetPlayer, CommandSender sender){

        Statement checkrep;

        try{
            checkrep = RepSystem.connection.createStatement();

            ResultSet result = checkrep.executeQuery("SELECT * FROM rep WHERE uuid = '" + targetPlayer.getName() + "'");

            boolean PlayerExist = result.getInt(1) == 1;
            int repvalue = result.getInt("rep_value");

            if(PlayerExist){
                if(repvalue<0){
                    sender.sendMessage(String.format("%s[%s" + repvalue + "%s] " + targetPlayer.getName(), ChatColor.GRAY, ChatColor.RED, ChatColor.GRAY));
                }else{
                    sender.sendMessage(String.format("%s[%s" + repvalue + "%s] " + targetPlayer.getName(), ChatColor.GRAY, ChatColor.GREEN, ChatColor.GRAY));
                }
            }

            checkrep.close();

        }catch(SQLException e){
            RepSystem.log.warning("[RepS] Error occurred while processing your request");
            RepSystem.log.warning(e.toString());
        }
    }
}