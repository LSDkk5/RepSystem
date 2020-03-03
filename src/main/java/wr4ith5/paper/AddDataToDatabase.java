package wr4ith5.paper;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class AddDataToDatabase{
    public static void fromAddPlus(Player targetPlayer, CommandSender sender, int currentRepValue) throws SQLException {

        Statement addPlusStatement = RepSystem.connection.createStatement();

        addPlusStatement.executeUpdate("UPDATE rep SET rep_value = '" + (currentRepValue+1) + "' WHERE uuid = '" + (targetPlayer.getUniqueId()) + "'");
        addPlusStatement.executeUpdate("UPDATE rep SET when_can_add = '" + (LocalDateTime.now().plusMinutes(10)) + "' WHERE uuid = '" + ((Player) sender).getUniqueId() + "'");
        Bukkit.broadcastMessage("§7[§a+§7] " + sender.getName() + " §6> §7"+targetPlayer.getName());;

        addPlusStatement.close();
    }

    public static void fromAddMinus(Player targetPlayer, CommandSender sender, int currentRepValue) throws SQLException {

        Statement addMinusStatement = RepSystem.connection.createStatement();

        addMinusStatement.executeUpdate("UPDATE rep SET rep_value = '" + (currentRepValue-1) + "' WHERE uuid = '" + (targetPlayer.getUniqueId()) + "'");
        addMinusStatement.executeUpdate("UPDATE rep SET when_can_add = '" + (LocalDateTime.now().plusMinutes(10)) + "' WHERE uuid = '" + ((Player) sender).getUniqueId() + "'");
        Bukkit.broadcastMessage("§7[§c-§7] " + sender.getName() + " §6> §7 "+targetPlayer.getName());

        addMinusStatement.close();
    }

    public static void fromAdmin(Player targetPlayer, int repValue) throws SQLException {

        Statement setRep = RepSystem.connection.createStatement();

        setRep.executeUpdate("UPDATE rep SET rep_value = '"+ repValue +"' WHERE uuid = '" + targetPlayer.getUniqueId() +"'");

        setRep.close();
    }
}