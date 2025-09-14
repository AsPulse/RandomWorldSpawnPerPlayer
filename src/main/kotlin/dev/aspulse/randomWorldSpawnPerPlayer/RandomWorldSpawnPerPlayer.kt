package dev.aspulse.randomWorldSpawnPerPlayer

import dev.aspulse.randomWorldSpawnPerPlayer.config.ConfigLoader
import dev.aspulse.randomWorldSpawnPerPlayer.config.Configuration
import dev.aspulse.randomWorldSpawnPerPlayer.listener.RespawnPointOverrider
import dev.aspulse.randomWorldSpawnPerPlayer.spawn.SpawnLocationGenerator
import dev.aspulse.randomWorldSpawnPerPlayer.spawn.WorldSpawnManager
import org.bukkit.plugin.java.JavaPlugin

class RandomWorldSpawnPerPlayer : JavaPlugin() {
    
    lateinit var pluginConfig: Configuration
        private set
    
    lateinit var worldSpawnManager: WorldSpawnManager
        private set
    
    private lateinit var configLoader: ConfigLoader

    override fun onEnable() {
        configLoader = ConfigLoader(this)
        
        try {
            reloadPluginConfig()
            worldSpawnManager = WorldSpawnManager(this)
            
            // Register event listeners
            val respawnPointOverrider = RespawnPointOverrider(this, worldSpawnManager)
            server.pluginManager.registerEvents(respawnPointOverrider, this)
            
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
