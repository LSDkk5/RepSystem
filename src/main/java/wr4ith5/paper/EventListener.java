package wr4ith5.paper;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class EventListener implements Listener {

    private static void setPlayerColors(Player player, int currentRepValue){
        if(currentRepValue >= 95){
            player.setPlayerListName(String.format("%s" + player.getName(), ChatColor.DARK_GREEN));
        }

        if(currentRepValue >= 50 && currentRepValue <= 94){
            player.setPlayerListName(String.format("%s" + player.getName(), ChatColor.GREEN));
        }

        if(currentRepValue >= 15 && currentRepValue <= 49 ){
            player.setPlayerListName(String.format("%s" + player.getName(), ChatColor.BLUE));
        }

        if(currentRepValue >= 0 && currentRepValue <= 15 ){
            player.setPlayerListName(String.format("%s" + player.getName(), ChatColor.GRAY));
        }

        if(currentRepValue < 0 && currentRepValue >= -34 ){
            player.setPlayerListName(String.format("%s" + player.getName(), ChatColor.RED));
        }

        if(currentRepValue <= -35 && currentRepValue >= -94 ){
            player.setPlayerListName(String.format("%s" + player.getName(), ChatColor.DARK_PURPLE));
        }

        if(currentRepValue <= -95 ){
            player.setPlayerListName(String.format("%s" + player.getName(), ChatColor.DARK_RED));;
        }
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event){
        Player player = event.getPlayer();

        try{
            Statement playerd = RepSystem.connection.createStatement();
            Statement playerdata = RepSystem.connection.createStatement();


            ResultSet result = playerd.executeQuery("SELECT COUNT(*) FROM rep WHERE uuid = '" + player.getName() + "'");

            boolean PlayerNotExist = result.getInt(1) == 0;

            if(!PlayerNotExist) {
                ResultSet result1 = playerdata.executeQuery("SELECT rep_value FROM rep WHERE uuid = '" + player.getName() + "'");
                setPlayerColors(event.getPlayer(), result1.getInt(1));
            }

            playerd.close();
            playerdata.close();

        }catch(SQLException e){
            RepSystem.log.warning("[RepS] Error occurred while processing your request");
            RepSystem.log.warning(e.toString());
            return;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();

        try{
            Statement insertplayers = RepSystem.connection.createStatement();
            Statement playerdata = RepSystem.connection.createStatement();


            ResultSet result = insertplayers.executeQuery("SELECT COUNT(*) FROM rep WHERE uuid = '" + player.getName() + "'");

            boolean PlayerNotExist = result.getInt(1) == 0;
            LocalDateTime date = LocalDateTime.now();

            if(PlayerNotExist){
                insertplayers.executeUpdate("INSERT INTO rep(uuid, when_can_add) VALUES('" + player.getName() + "', '" + date +"');");
            }else{
                ResultSet result1 = playerdata.executeQuery("SELECT rep_value FROM rep WHERE uuid = '" + player.getName() + "'");
                setPlayerColors(event.getPlayer(), result1.getInt(1));
            }

            insertplayers.close();
            playerdata.close();

        }catch(SQLException e){
            RepSystem.log.warning("[RepS] Error occurred while processing your request");
            RepSystem.log.warning(e.toString());
            return;
        }
    }
}
