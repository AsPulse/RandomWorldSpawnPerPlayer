package dev.aspulse.randomWorldSpawnPerPlayer.config

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class ConfigLoader(private val plugin: JavaPlugin) {
    
    fun loadConfiguration(worldName: String = "world"): Configuration {
        plugin.saveDefaultConfig()
        plugin.reloadConfig()
        
        val config = plugin.config
        val configMap = mutableMapOf<String, Any>()
        
        // Get top-level values
        config.get("radius")?.let { configMap["radius"] = it }
        config.get("max-retries")?.let { configMap["max-retries"] = it }
        
        // Get center section as a map
        val centerSection = config.getConfigurationSection("center")
        if (centerSection != null) {
            configMap["center"] = centerSection.getValues(false)
        }
        
        // Get spawnable-condition section as a map
        val spawnableSection = config.getConfigurationSection("spawnable-condition")
        if (spawnableSection != null) {
            configMap["spawnable-condition"] = spawnableSection.getValues(false)
        }
        
        // Get worldspawn-condition section as a map
        val worldspawnSection = config.getConfigurationSection("worldspawn-condition")
        if (worldspawnSection != null) {
            configMap["worldspawn-condition"] = worldspawnSection.getValues(false)
        }
        
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