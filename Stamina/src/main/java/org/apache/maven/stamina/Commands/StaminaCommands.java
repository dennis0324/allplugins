package org.apache.maven.stamina.Commands;

import org.apache.maven.stamina.StaminaContorl;
import org.apache.maven.stamina.files.PlayerConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StaminaCommands implements CommandExecutor {
    private JavaPlugin plugin;
    private StaminaContorl staminaContorl;
    private String msg_save;
    private String msg_reload;
    public StaminaCommands(JavaPlugin plugin,StaminaContorl staminaContorl){
        this.plugin = plugin;
        this.staminaContorl = staminaContorl;
        this.msg_save = "&6[Stamina] &cData Saved!";
        this.msg_reload = "&6[Stamina] &cData Reloaded!";

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(command.getName().equalsIgnoreCase("stamina")){
                if(args[0].equalsIgnoreCase("reload")){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',msg_reload.replace("ed!","ing...")));
                    staminaContorl.resetBossBar();
                    for(Player p :plugin.getServer().getOnlinePlayers()){
                        staminaContorl.reload(p);
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',msg_reload));
                }
                else if(args[0].equalsIgnoreCase("save")){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',msg_save.replace("ed!","ing...")));
                    staminaContorl.saveServerData();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',msg_save));
                }
            }
        }
        return false;
    }
}
