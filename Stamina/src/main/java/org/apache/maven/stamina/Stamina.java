package org.apache.maven.stamina;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import static org.apache.maven.stamina.files.ConfigLoader.loadConfig;

public final class Stamina extends JavaPlugin implements Listener {
        StaminaContorl staminaContorl;
    @Override
    public void onEnable() {

        // Plugin startup logic
        System.out.println("[Stamina] Starting Up...");
        staminaContorl = new StaminaContorl(this);
        BukkitTask staminaControl = staminaContorl.runTaskTimer(this,0L,10L);
        getServer().getPluginManager().registerEvents(staminaContorl,this);
        getConfig().options().copyDefaults();
        System.out.println("[Stamina] Loading Config From config.yml ...");
        staminaContorl.setAvailableGamemode(getConfig().getStringList("staminaInMode"));
        staminaContorl.bossBarTitleFormat(getConfig().getString("titleFormat"));
        staminaContorl.setSlownessStamina(getConfig().getInt("slownessStamina"));
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
