package wr4ith5.paper;

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
            player.setPlayerListName("§2"+player.getName());
        }

        if(currentRepValue >= 50 && currentRepValue <= 94){
            player.setPlayerListName("§a"+player.getName());
        }

        if(currentRepValue >= 15 && currentRepValue <= 49 ){
            player.setPlayerListName("§9"+player.getName());
        }

        if(currentRepValue >= 0 && currentRepValue <= 15 ){
            player.setPlayerListName("§7"+player.getName());
        }

        if(currentRepValue < 0 && currentRepValue >= -34 ){
            player.setPlayerListName("§c"+player.getName());
        }

        if(currentRepValue <= -35 && currentRepValue >= -94 ){
            player.setPlayerListName("§5"+player.getName());
        }

        if(currentRepValue <= -95 ){
            player.setPlayerListName("§4"+player.getName());
        }
    }

    @EventHandler
    public static void onPlayerBedEnter(PlayerBedEnterEvent event){
        Player player = event.getPlayer();

        try{
            Statement insertPlayersStatement = RepSystem.connection.createStatement();
            Statement getPlayerDataStatement = RepSystem.connection.createStatement();


            ResultSet result = insertPlayersStatement.executeQuery("SELECT COUNT(*) FROM rep WHERE uuid = '" + player.getUniqueId() + "'");

            boolean PlayerNotExist = result.getInt(1) == 0;

            if(!PlayerNotExist) {
                ResultSet result1 = getPlayerDataStatement.executeQuery("SELECT rep_value FROM rep WHERE uuid = '" + player.getUniqueId() + "'");
                setPlayerColors(event.getPlayer(), result1.getInt(1));
            }

            insertPlayersStatement.close();
            getPlayerDataStatement.close();

        }catch(SQLException e){
            RepSystem.log.warning("[RepS] Error occurred while processing your request");
            RepSystem.log.warning(e.toString());
            return;
        }
    }

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();

        try{
            Statement insertPlayersStatement = RepSystem.connection.createStatement();
            Statement getPlayerDataStatement = RepSystem.connection.createStatement();


            ResultSet result = insertPlayersStatement.executeQuery("SELECT COUNT(*) FROM rep WHERE uuid = '" + player.getUniqueId() + "'");

            boolean PlayerNotExist = result.getInt(1) == 0;
            LocalDateTime date = LocalDateTime.now();

            if(PlayerNotExist){
                insertPlayersStatement.executeUpdate("INSERT INTO rep(uuid, when_can_add) VALUES('" + player.getUniqueId() + "', '" + date +"');");
            }else{
                ResultSet result1 = getPlayerDataStatement.executeQuery("SELECT rep_value FROM rep WHERE uuid = '" + player.getUniqueId() + "'");
                setPlayerColors(event.getPlayer(), result1.getInt(1));
            }

            insertPlayersStatement.close();
            getPlayerDataStatement.close();

        }catch(SQLException e){
            RepSystem.log.warning("[RepS] Error occurred while processing your request");
            RepSystem.log.warning(e.toString());
            return;
        }
    }
}
