package org.apache.maven.stamina.TabComplete;

import org.apache.maven.stamina.files.SimpleConfig;
import org.apache.maven.stamina.files.SimpleConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SetValueTab implements TabCompleter {
    private SimpleConfigManager manager;
    private SimpleConfig config;
    private JavaPlugin plugin;
    private List<String> firstArgs;
    private List<String> secondArgs;
    private List<String> thridArgs;
    private List<String> fourthArgs;

    public SetValueTab(JavaPlugin plugin, List<String> configList){
        this.plugin = plugin;
        this.firstArgs = new ArrayList<>(Arrays.asList("user","help"));
        this.thridArgs = new ArrayList<>(Arrays.asList("set","add","remove"));
        this.fourthArgs = configList;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(command.getName().equalsIgnoreCase("SetValue")){
                if(args.length == 1){
                    if(!args[0].equalsIgnoreCase("")){
                        for(String e : firstArgs){
                            if(e.toLowerCase().startsWith(args[0].toLowerCase())){
                                list.add(e);
                            }
                        }
                    }
                    else{
                        list = firstArgs;
                    }
                }
                else if(args.length == 2 && args[0].equalsIgnoreCase("user")){
                    if(!args[1].equalsIgnoreCase("")){
                        for(Player player : plugin.getServer().getOnlinePlayers()){
                            if(player.getName().toLowerCase().startsWith(args[1]))
                            list.add(player.getName());
                        }
                    }
                    else{
                        list = secondArgs;
                    }
                }
                else if(args.length == 2 && args[0].equalsIgnoreCase("help")){
                    if(!args[1].equalsIgnoreCase("")){
                        for(String e : fourthArgs){
                            if(e.toLowerCase().startsWith(args[1].toLowerCase())){
                                list.add(e);
                            }
                        }
                    }
                    else{
                        list = fourthArgs;
                    }
                }
                else if(args.length == 3 && args[0].equalsIgnoreCase("user")){
                    if(!args[2].equalsIgnoreCase("")){
                        for(String e : thridArgs){
                            if(e.toLowerCase().startsWith(args[2].toLowerCase())){
                                list.add(e);
                            }
                        }
                    }
                    else{
                        list = thridArgs;
                    }
                }
                else if(args.length ==4 && args[0].equalsIgnoreCase("user")){
                    if(!args[3].equalsIgnoreCase("")){
                        for(String e : fourthArgs){
                            if(e.toLowerCase().startsWith(args[3].toLowerCase())){
                                list.add(e);
                            }
                        }
                    }
                    else{
                        list = fourthArgs;
                    }
                }
            }
            return list;
        }
        return null;
    }

    public List<String> organizeList(Set<String> list){
        List<String> output = new ArrayList<>();
        for(String e : list){
            String temp = e.toLowerCase();
            if(temp.contains("stamina")){
                output.add(temp);
            }
        }
        return output;
    }

}
