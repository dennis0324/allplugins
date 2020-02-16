package org.apache.maven.stamina.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.lang.model.type.IntersectionType;
import java.awt.geom.CubicCurve2D;
import java.util.*;

import static java.lang.Float.NaN;

public class SetValue implements CommandExecutor {
    private JavaPlugin plugin;
    private List<String> firstArgs;
    private List<String> secondArgs;
    private List<String> thridArgs;
    private List<String> fourthArgs;
    private String Usage_user;
    private String Usage_help;
    private HashMap<UUID, HashMap<String,Double>> playerlist = new HashMap<>();

    public SetValue(JavaPlugin plugin, List<String> configList,HashMap<UUID, HashMap<String,Double>> playerlist){
        this.plugin = plugin;
        this.firstArgs = new ArrayList<>(Arrays.asList("user","help"));
        this.thridArgs = new ArrayList<>(Arrays.asList("set","add","remove"));
        this.fourthArgs = configList;
        this.playerlist = playerlist;
        this.Usage_user = "&cUsage: &6/setvalue &l&9<&r&6User&r&l&9>&r &l&9<&r&6Player&r&l&9>&r &l&9<&r&6Actionfactor&r&l&9>&r &l&9<&r&6ConfigData&r&l&9>&r &l&9<&r&6Value&r&l&9>&r";
        this.Usage_help = "&cUsage: &6/setvalue &l&9<&r&6help&r&l&9>&r";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("SetValue")){
            if(sender instanceof Player){
                Player player = (Player) sender;
                String firstOutput = testCommand(player,"Factor",args,0,firstArgs,Usage_user);
                if(firstOutput.equalsIgnoreCase("user")){
                    String secondOutput = testCommand(player,"PlayerName",args,1,null,Usage_user);
                    if(secondOutput != null){
                        String thirdOutput= testCommand(player,"ActionFactor",args,2,thridArgs,Usage_user);
                        if(thirdOutput != null){
                            String fourthOutput= testCommand(player,"ConfigData",args,3,fourthArgs,Usage_user);
                            if(fourthOutput != null){
                                Integer fifthOutput = testCommmand(player, "Value",args,4,Usage_user);
                                if(fifthOutput != null){
                                    if(thirdOutput.equalsIgnoreCase("set")){
                                        System.out.println("executed!");
                                        Player target = Bukkit.getPlayer(secondOutput);
                                        HashMap<String, Double> temp= playerlist.get(target.getUniqueId());
                                        temp.put("Info."+fourthOutput,(double)fifthOutput);
                                    }
                                    else if(thirdOutput.equalsIgnoreCase("add")){
                                        System.out.println("executed! add");
                                        Player target = Bukkit.getPlayer(secondOutput);
                                        HashMap<String, Double> temp= playerlist.get(target.getUniqueId());
                                        temp.put("Info."+fourthOutput,(double)fifthOutput + temp.get("Info."+fourthOutput));
                                    }
                                    else if(thirdOutput.equalsIgnoreCase("remove")){
                                        System.out.println("executed!");
                                        Player target = Bukkit.getPlayer(secondOutput);
                                        HashMap<String, Double> temp= playerlist.get(target.getUniqueId());
                                        double temp2 = temp.get("Info."+fourthOutput) - (double)fifthOutput;
                                        System.out.println(temp2);
                                        if(temp2 < 0){
                                            temp2 = 0;
                                        }
                                        temp.put("Info."+fourthOutput,temp2);
                                    }
                                }

                            }
                        }
                    }
                }
                else{
                    String secondOutput = testCommand(player,"Factor",args,1,fourthArgs,Usage_help);

                    if(secondOutput != null){

                        String temp = plugin.getConfig().getString("commands."+secondOutput+".description");
                        String helpMessage = "&c" +secondOutput + " : &r" + temp;
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&c&l[Command Help]&r"));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',helpMessage));
                    }
                }

            }
        }
        return false;
    }

    public String testCommand(Player player,String argName, String[] inputCommands, int index, List<String> compareCommands,String errorMessage){
        String errMessage = "&cInvalid Value For &9&l<" + argName + ">";
        if(compareCommands != null){
            String var3 = inputCommands[index];

            String[] var2 = compareCommands.toArray(new String[0]);
            if(!StringUtils.isNumeric(var3)){
                for(String element : var2){
                    if(element.equalsIgnoreCase(var3)){
                        // command feedback
                        return element;
                    }
                }
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',errMessage));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',errorMessage));
        }
        else{
            for(Player p :plugin.getServer().getOnlinePlayers() ){
                if(p.getName().equals(inputCommands[index])){
                    return p.getName();
                }
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',errMessage));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cCan't find Player!"));
        }
        return null;
    }

    public Integer testCommmand(Player player,String argName, String[] inputCommands, int index, String errorMessage){
        String var3 = inputCommands[index];
        String errMessage = "&cInvalid Value For &9&l<" + argName + ">";
        if(StringUtils.isNumeric(var3)){
            return Integer.parseInt(var3);
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',errMessage));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',errorMessage));
        return null;
    }

}
