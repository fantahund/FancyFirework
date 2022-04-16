package de.fanta.fancyfirework;

import com.google.common.base.Preconditions;
import de.fanta.fancyfirework.fireworks.AbstractFireWork;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigManager {

    private final FancyFirework plugin;
    private final File DATA_FOLDER;
    private final Map<NamespacedKey, YamlConfiguration> configurationMap;

    ConfigManager(FancyFirework plugin) {
        this.plugin = plugin;
        this.DATA_FOLDER = plugin.getDataFolder();
        this.configurationMap = new HashMap<>();
    }

    public void init() {
        for (AbstractFireWork fireWork : plugin.getRegistry().getValues()) {
            saveDefaultConfig(fireWork.getDefaultResource(), fireWork.getKey());
        }
    }

    public void load() {
        plugin.getRegistry().getKeys().forEach(this::loadConfig);
    }

    public YamlConfiguration getConfig(NamespacedKey key) {
        return configurationMap.get(key);
    }

    private void loadConfig(NamespacedKey key) {
        Preconditions.checkArgument(key != null, "The key cannot be null!");
        String path = "fireworks/" + key.getNamespace() + "/" + key.getKey() + ".yml";
        File file = new File(DATA_FOLDER, path);
        configurationMap.put(key, YamlConfiguration.loadConfiguration(file));
    }

    private void saveDefaultConfig(InputStream in, NamespacedKey key) {
        Preconditions.checkArgument(in != null, "The input stream cannot be null!");
        Preconditions.checkArgument(key != null, "The key cannot be null!");

        String path = "fireworks/" + key.getNamespace() + "/" + key.getKey() + ".yml";
        File outFile = new File(DATA_FOLDER, path);
        int lastIndex = path.lastIndexOf('/');
        File outDir = new File(DATA_FOLDER, path.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists()) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                plugin.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }

}
