package wr4ith5.paper;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.Objects;
import java.util.logging.Logger;


public final class RepSystem extends JavaPlugin{

    public static Connection connection = null;
    public static Statement createTableStatement = null;

    public static Logger log = Bukkit.getLogger();


    @Override
    public void onEnable(){
        File repSystemDir = new File("plugins/RepSystem");

        if(!repSystemDir.exists()){
            repSystemDir.mkdir();
            log.info("[RepS] Plugin directory successful created");
        }

        try{
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/RepSystem/reps.db");
            createTableStatement = connection.createStatement();


            log.info("[RepS] Connection to database established");

            createTableStatement.executeUpdate("CREATE TABLE IF NOT EXISTS rep(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "uuid VARCHAR(40) NOT NULL," +
                    "rep_value VARCHAR(10) NOT NULL DEFAULT 0," +
                    "when_can_add VARCHAR(50) DEFAULT 0" +
                    ");");
            createTableStatement.close();
        }catch(SQLException e){
            log.warning("[RepS] Connection to database not be established");
            log.warning(e.toString());
        }

        Objects.requireNonNull(this.getCommand("rep+")).setExecutor(new CommandExec());
        Objects.requireNonNull(this.getCommand("rep++")).setExecutor(new CommandExec());
        Objects.requireNonNull(this.getCommand("rep-")).setExecutor(new CommandExec());
        Objects.requireNonNull(this.getCommand("rep?")).setExecutor(new CommandExec());
        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable(){
        try{
            connection.close();
            log.warning("[RepS] Connection to database closed");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
