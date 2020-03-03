package wr4ith5.paper;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class CommandExec implements CommandExecutor {

    public boolean isNumeric(String strNum){

        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){

            String targetIsNotOnline = "§ctarget player must be online!";

            if(label.equals("rep+")){
                if(args.length == 1){
                    Player target = Bukkit.getPlayer(args[0]);

                    if(target != null && target.isOnline()){
                        RepSystemController.AddPlus(target, sender);
                    }else{
                        sender.sendMessage(targetIsNotOnline);
                    }
                }else{
                    sender.sendMessage("§cBad command usage: example §6/rep+ <player_nickname>");
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
                                    AddDataToDatabase.fromAdmin(target, repValue);
                                }catch(SQLException e){
                                    e.printStackTrace();
                                }
                            }else{
                                sender.sendMessage("§cParameter must be a number");
                            }
                        }else{
                            sender.sendMessage(targetIsNotOnline);
                        }
                    }else{
                        sender.sendMessage("§cBad command usage: example §6/rep++ <player_nickname>");
                    }
                }else{
                    sender.sendMessage("§cThis command is only for ops");
                }
            }

            if(label.equals("rep-")){
                if(args.length == 1){
                    Player target = Bukkit.getPlayer(args[0]);

                    if(target != null && target.isOnline()){
                        RepSystemController.AddMinus(target, sender);
                    }else{
                        sender.sendMessage(targetIsNotOnline);
                    }
                }else{
                    sender.sendMessage("§cBad command usage: example §6/rep- <player_nickname>");
                }
            }

            if(label.equals("rep?")){
                if(args.length == 1){
                    Player target = Bukkit.getPlayer(args[0]);

                    if(target != null && target.isOnline()){
                        RepSystemController.CheckRepValue(target, sender);
                    }else{
                        sender.sendMessage(targetIsNotOnline);
                    }
                }else{
                    sender.sendMessage("§cBad command usage: example /rep? <player_nickname>");
                }
            }
        }
        return false;
    }
}