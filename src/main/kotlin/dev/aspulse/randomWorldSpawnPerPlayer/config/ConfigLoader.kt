package dev.aspulse.randomWorldSpawnPerPlayer.config

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class ConfigLoader(private val plugin: JavaPlugin) {
    
    fun loadConfiguration(worldName: String = "world"): Configuration {
        plugin.saveDefaultConfig()
        plugin.reloadConfig()
        
        val config = plugin.config
        val configMap = config.getValues(false)
        
        return try {
            Configuration.fromMap(configMap, worldName)
        } catch (e: Exception) {
            throw ConfigurationException("Failed to load configuration: ${e.message}", e)
        }
    }
    
    fun saveDefaultConfig() {
        if (!plugin.dataFolder.exists()) {
            plugin.dataFolder.mkdirs()
        }
        
        val configFile = File(plugin.dataFolder, "config.yml")
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false)
        }
    }
}