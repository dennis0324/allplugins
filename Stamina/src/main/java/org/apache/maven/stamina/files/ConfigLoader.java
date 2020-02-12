package org.apache.maven.stamina.files;

import org.apache.maven.stamina.Stamina;

import java.util.HashMap;

public class ConfigLoader {
    public static HashMap<String,Double> loadConfig(Stamina plugin){
        HashMap<String,Double> storeConfig = new HashMap<>();
        storeConfig.put("updateStamina",plugin.getConfig().getDouble("updateStamina"));
        storeConfig.put("startStamina",plugin.getConfig().getDouble("startStamina"));
        storeConfig.put("maxStamina",plugin.getConfig().getDouble("maxStamina"));
        storeConfig.put("staminaDecreaseAmount",plugin.getConfig().getDouble("staminaDecreaseAmount"));
        storeConfig.put("staminaIncreaseAmount",plugin.getConfig().getDouble("staminaIncreaseAmount"));
        storeConfig.put("maxStaminaDecreaseAmount",plugin.getConfig().getDouble("maxStaminaDecreaseAmount"));
        storeConfig.put("titleFormat",plugin.getConfig().getDouble("titleFormat"));
        return storeConfig;
    }
}
