package org.apache.maven.stamina.TabComplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StaminaCommandsTab implements TabCompleter {
    private List<String> factor;
    private JavaPlugin plugin;
    public StaminaCommandsTab(JavaPlugin plugin){
        this.factor = new ArrayList<>(Arrays.asList("reload","save"));
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(command.getName().equalsIgnoreCase("stamina")){
                if(args.length == 1){
                    if(!args[0].equalsIgnoreCase("")){
                        for(String element : factor){
                            if(element.toLowerCase().startsWith(args[0].toLowerCase())){
                                list.add(element);
                            }
                        }
                    }
                    else{
                        list = factor;
                    }
                }
            }
            return list;
        }
        return null;
    }
}
