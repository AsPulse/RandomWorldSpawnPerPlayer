package dev.aspulse.randomWorldSpawnPerPlayer

import dev.aspulse.randomWorldSpawnPerPlayer.config.ConfigLoader
import dev.aspulse.randomWorldSpawnPerPlayer.config.Configuration
import org.bukkit.plugin.java.JavaPlugin

class RandomWorldSpawnPerPlayer : JavaPlugin() {
    
    lateinit var pluginConfig: Configuration
        private set
    
    private lateinit var configLoader: ConfigLoader

    override fun onEnable() {
        configLoader = ConfigLoader(this)
        
        try {
            reloadPluginConfig()
            logger.info("RandomWorldSpawnPerPlayer has been enabled.")
            logger.info("Center: ${pluginConfig.center}")
            logger.info("Radius: ${pluginConfig.radius}")
        } catch (e: Exception) {
            logger.severe("Failed to load configuration: ${e.message}")
            logger.severe("Disabling plugin...")
            server.pluginManager.disablePlugin(this)
        }
    }

    override fun onDisable() {
        logger.info("RandomWorldSpawnPerPlayer has been disabled.")
    }
    
    fun reloadPluginConfig() {
        pluginConfig = configLoader.loadConfiguration()
    }
}
