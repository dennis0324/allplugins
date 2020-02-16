package org.apache.maven.stamina;

import org.apache.maven.stamina.Commands.SetValue;
import org.apache.maven.stamina.TabComplete.SetValueTab;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static org.apache.maven.stamina.files.ConfigLoader.loadConfig;

public final class Stamina extends JavaPlugin implements Listener {
        StaminaContorl staminaContorl;
    private HashMap<UUID, HashMap<String,Double>> playerlist = new HashMap<>();

    @Override
    public void onEnable() {

        // Plugin startup logic
        System.out.println("[Stamina] Starting Up...");
        staminaContorl = new StaminaContorl(this,playerlist);
        BukkitTask staminaControl = staminaContorl.runTaskTimer(this,0L,10L);
        getServer().getPluginManager().registerEvents(staminaContorl,this);
        Set<String> configList= getConfig().getConfigurationSection("commands").getKeys(false);
        List<String> list = new ArrayList<>();
        System.out.println(configList);
        for(String e : configList){
            if(e.toLowerCase().contains("stamina") && !e.equalsIgnoreCase("staminaInMode")){
                list.add(e);
            }
        }
        getCommand("setvalue").setTabCompleter(new SetValueTab(this,list));
        getCommand("setvalue").setExecutor(new SetValue(this,list,playerlist));
//        getCommand("SetValue").setTabCompleter(new SetValueTab(this,getConfig().));
        getConfig().options().copyDefaults();
        System.out.println("[Stamina] Loading Config From config.yml ...");
        staminaContorl.setAvailableGamemode(getConfig().getStringList("staminaInMode"));
        staminaContorl.bossBarTitleFormat(getConfig().getString("titleFormat"));
        staminaContorl.setSlownessStamina(getConfig().getInt("commands.slownessStamina.value"));
        System.out.println("[Stamina] Setting Up Config Files ...");

        System.out.println("[Stamina] Setting Up Config Files ...");
        staminaContorl.loadConfig(loadConfig(this));
        System.out.println("[Stamina] Getting All Data From Online Player...");
        System.out.println("[Stamina] Online Player " + getServer().getOnlinePlayers().size() + " Found...");
        for(Player p : getServer().getOnlinePlayers()){
            staminaContorl.reload(p);
        }
        System.out.println("[Stamina] Getting Data Complete");
        saveDefaultConfig();
        System.out.println("[Stamina] Setting Complete");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("[Stamina] Shutting Down ...");
        staminaContorl.resetBossBar();
        staminaContorl.saveServerData();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        return false;
    }

//    private void displayBanner(Sender sender) {
//        sender.sendMessage(Message.colorize("&b   ___   ___   ___  &3 __    "));
//        sender.sendMessage(Message.colorize("&b  |___    |   |___| &3|__)   " + "&2LuckPerms &bv" + getBootstrap().getVersion()));
//        sender.sendMessage(Message.colorize("&b   ___|   |   |   | &3|      " + "&8Running on " + getBootstrap().getType().getFriendlyName() + " - " + getBootstrap().getServerBrand()));
//        sender.sendMessage("");
//    }
}
