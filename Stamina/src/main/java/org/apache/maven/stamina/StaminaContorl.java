package org.apache.maven.stamina;

import org.apache.maven.stamina.files.PlayerConfig;
import org.apache.maven.stamina.files.SimpleConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class StaminaContorl extends BukkitRunnable implements Listener {
    private HashMap<UUID, HashMap<String,Double>> playerlist = new HashMap<>();
    private HashMap<UUID,Boolean> isRunning = new HashMap<>();
    private HashMap<UUID, BossBar> StaminaBars = new HashMap<>();
    private HashMap<String,Double> configs = new HashMap<>();
    private Stamina plugin;
    List<String> gamemodeList;
    private SimpleConfigManager manager;
    private int slownessStamina;

    public String bossBarTitle;


    public void bossBarTitleFormat(String string){ bossBarTitle = string; }

    public void setAvailableGamemode(List<String> list){ this.gamemodeList = list; }

    public void loadConfig(HashMap<String,Double> list){ configs = list; }

    public void setSlownessStamina(int value){this.slownessStamina = value; }

    //contorl possible gamemode
    public StaminaContorl(Stamina plugin,HashMap<UUID, HashMap<String,Double>> playerlist) {
        this.playerlist = playerlist;
        this.plugin = plugin;
    }

    @Override
    public void run(){
        Set<UUID> players = isRunning.keySet();
        for(Player p : plugin.getServer().getOnlinePlayers()){
            HashMap<String, Double> var1;
            var1 = playerlist.get(p.getUniqueId());
            if(p.getFoodLevel() != 19){
                p.setFoodLevel(19);
            }
            if(var1.get("Info.stamina") >= slownessStamina){
                p.removePotionEffect(PotionEffectType.SLOW);
            }
            if(isRunning.get(p.getUniqueId())){

                if(var1.get("Info.stamina") > 0){
                    var1.put("Info.stamina",var1.get("Info.stamina") - var1.get("Info.staminaDecreaseAmount"));
                    var1.put("Info.startStamina",var1.get("Info.startStamina") - var1.get("Info.maxStaminaDecreaseAmount"));
                    if(var1.get("Info.stamina") < 0){
                        var1.put("Info.stamina",0.0);
                        p.setFoodLevel(3);
                    }
                    if(var1.get("Info.stamina") < slownessStamina){
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,999999,0,true));
                    }
                }
            }
            else{
                if(var1.get("Info.stamina") <var1.get("Info.startStamina")){
                    var1.put("Info.stamina",var1.get("Info.stamina") + var1.get("Info.staminaIncreaseAmount"));
                }
                if(var1.get("Info.stamina") >var1.get("Info.startStamina")){
                    var1.put("Info.stamina",var1.get("Info.startStamina"));
                }
            }
            BossBar bar = StaminaBars.get(p.getUniqueId());
            bar.setVisible(false);
            String temp = bossBarTitle;
            Integer var2 = (int)Math.round(var1.get("Info.stamina"));
            Integer var3 = (int)Math.round(var1.get("Info.startStamina"));
            temp = temp.replace("{stamina}",var2.toString());
            temp = temp.replace("{startStamina}",var3.toString());
            bar.setTitle(ChatColor.translateAlternateColorCodes('&',temp));
            for(String str : gamemodeList){
                if(p.getGameMode().toString().equals(str)){
                    bar.setVisible(true);
                }
            }
            double var4 = var1.get("Info.stamina") / var1.get("Info.maxStamina");
            if(Math.abs(var4) > 1){
                var4 = 1;
            }
            bar.setProgress(var4);
        }
    }
    //스태미나, 스태미나 상한선, 스태미나 다는 속도, 스태미나 차는 속도, 스태미나 상한선 다는 속도,
    @EventHandler
    public void onServerJoin(PlayerJoinEvent e){
        //서버에 사람이 들어왔을 경우에 작동하는 메소드
        final Player p = e.getPlayer();
        BossBar bar = Bukkit.createBossBar("Stamina", BarColor.YELLOW, BarStyle.SOLID);
        bar.addPlayer(p);
        bar.setVisible(false);
        StaminaBars.put(p.getUniqueId(),bar);
        HashMap<String,Double> temp = new HashMap<>();
        PlayerConfig playerConfig = new PlayerConfig(plugin);
        playerConfig.createUUID(configs,e.getPlayer());
        isRunning.put(e.getPlayer().getUniqueId(),false);
        Set<String> testing = playerConfig.get().getConfigurationSection("Info").getKeys(false);
        for(String string : testing){
            temp.put("Info."+string,playerConfig.get().getDouble("Info."+string));
        }

        System.out.println(temp);
        playerlist.put(e.getPlayer().getUniqueId(),temp);
    }

    @EventHandler
    public void onServerQuit(PlayerQuitEvent e){
        isRunning.remove(e.getPlayer().getUniqueId());
        StaminaBars.remove(e.getPlayer().getUniqueId());
        PlayerConfig.updateConfig(playerlist.get(e.getPlayer().getUniqueId()),e.getPlayer());
        playerlist.remove(e.getPlayer().getUniqueId());

    }

    @EventHandler
    public void onRunning(PlayerToggleSprintEvent e){
        if(e.isSprinting()){
            for(String str : gamemodeList){
                if(e.getPlayer().getGameMode().toString().equals(str)){
                    isRunning.put(e.getPlayer().getUniqueId(),true);
                }
            }
        }
        else{
            isRunning.put(e.getPlayer().getUniqueId(),false);
        }
    }

    @EventHandler
    public void onEating(PlayerItemConsumeEvent e){ System.out.println(e.getItem().getItemMeta().toString()); }

    public void resetBossBar() {
        StaminaBars.entrySet().forEach((entry) -> {
            entry.getValue().removeAll();
        });
    }

    public void reload(Player p){
        BossBar bar = Bukkit.createBossBar("Stamina", BarColor.YELLOW, BarStyle.SOLID);
        bar.addPlayer(p);
        bar.setVisible(false);
        StaminaBars.put(p.getUniqueId(),bar);
        HashMap<String,Double> temp = new HashMap<>();
        PlayerConfig playerConfig = new PlayerConfig(plugin);
        playerConfig.createUUID(configs,p);
        isRunning.put(p.getUniqueId(),false);
        Set<String> testing = playerConfig.get().getConfigurationSection("Info").getKeys(false);
        for(String string : testing){
            temp.put("Info."+string,playerConfig.get().getDouble("Info."+string));
        }
        playerlist.put(p.getUniqueId(),temp);
    }

    public void saveServerData(){
        for(Player player : plugin.getServer().getOnlinePlayers()){
            PlayerConfig.updateConfig(playerlist.get(player.getPlayer().getUniqueId()),player.getPlayer());
        }
    }
}
