package org.apache.maven.stamina.files;

import org.apache.maven.stamina.Stamina;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class PlayerConfig extends SimpleConfigManager{
    private static File file;
    private static FileConfiguration customFile;
    private static HashMap<String,Double> configs;
    public SimpleConfigManager manager;
    public SimpleConfig config;

    public PlayerConfig(JavaPlugin plugin){
        super(plugin);
    }

    public void createUUID(HashMap<String,Double> hashMap, Player player){
        configs = hashMap;
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("Stamina").getDataFolder()+"/userdata/",player.getUniqueId() + ".yml");
        manager = new SimpleConfigManager(super.plugin);
        this.config = manager.getNewConfig("/userdata/" + player.getUniqueId()+".yml");
        if(!this.config.getString("UUID").equals(player.getUniqueId().toString())){
            System.out.println("Player" + player.getPlayer().getName() +" don't have file");
            this.config.set("UUID",player.getUniqueId().toString());
            this.config.set("Info.stamina",configs.get("startStamina"));
            this.config.set("Info.startStamina",configs.get("startStamina"));
            this.config.set("Info.staminaDecreaseAmount",configs.get("staminaDecreaseAmount"));
            this.config.set("Info.staminaIncreaseAmount",configs.get("staminaIncreaseAmount"));
            this.config.set("Info.maxStamina",configs.get("maxStamina"));
            this.config.set("Info.maxStaminaDecreaseAmount",configs.get("maxStaminaDecreaseAmount"));
            this.config.saveConfig();
        }
    }

    public FileConfiguration get(){
        return config.getConfigPath();
    }


    public static void updateConfig(HashMap<String,Double> list,Player player){
        HashMap<String,Double> configt = list;
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("Stamina").getDataFolder()+"/userdata",player.getUniqueId() + ".yml");

        if(!file.exists()){
            try{
                file.createNewFile();
            }
            catch(IOException e){
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
        customFile.set("Info.stamina",list.get("Info.startStamina"));
        customFile.set("Info.startStamina",list.get("Info.startStamina"));
        customFile.set("Info.staminaDecreaseAmount",list.get("Info.staminaDecreaseAmount"));
        customFile.set("Info.staminaIncreaseAmount",list.get("Info.staminaIncreaseAmount"));
        customFile.set("Info.maxStamina",list.get("Info.maxStamina"));
        customFile.set("Info.maxStaminaDecreaseAmount",list.get("Info.maxStaminaDecreaseAmount"));
        String testing1 = customFile.saveToString();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Bukkit.getServer().getPluginManager().getPlugin("Stamina").getDataFolder()+"/userdata/"+player.getPlayer().getUniqueId()+".yml"));
            writer.write(testing1);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //리로드 설정 다시 해야됨
    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }

}
