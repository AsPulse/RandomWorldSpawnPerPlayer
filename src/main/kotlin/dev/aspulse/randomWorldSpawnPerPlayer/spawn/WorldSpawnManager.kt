package dev.aspulse.randomWorldSpawnPerPlayer.spawn

import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class WorldSpawnManager(private val plugin: JavaPlugin) {
    
    private val spawnCache = ConcurrentHashMap<UUID, Location>()
    private val worldSpawnsDir: File
    
    init {
        worldSpawnsDir = File(plugin.dataFolder, "WorldSpawns")
        if (!worldSpawnsDir.exists()) {
            worldSpawnsDir.mkdirs()
        }
    }
    
    fun getOrDefault(player: Player, defaultSupplier: () -> Location): Location {
        val uuid = player.uniqueId
        
        // Check cache first
        spawnCache[uuid]?.let { return it }
        
        // Try to load from file
        val spawnFile = File(worldSpawnsDir, "$uuid.yml")
        if (spawnFile.exists()) {
            try {
                val config = YamlConfiguration.loadConfiguration(spawnFile)
                val world = plugin.server.getWorld(config.getString("world") ?: player.world.name)
                if (world != null) {
                    val location = Location(
                        world,
                        config.getDouble("x"),
                        config.getDouble("y"),
                        config.getDouble("z")
                    )
                    spawnCache[uuid] = location
                    return location
                }
            } catch (e: Exception) {
                plugin.logger.warning("Failed to load spawn for player ${player.name}: ${e.message}")
            }
        }
        
        // Generate new spawn using supplier
        val newSpawn = defaultSupplier()
        saveSpawn(uuid, newSpawn)
        spawnCache[uuid] = newSpawn
        return newSpawn
    }
    
    private fun saveSpawn(uuid: UUID, location: Location) {
        val spawnFile = File(worldSpawnsDir, "$uuid.yml")
        val config = YamlConfiguration()
        
        config.set("world", location.world?.name)
        config.set("x", location.x)
        config.set("y", location.y)
        config.set("z", location.z)
        
        try {
            config.save(spawnFile)
        } catch (e: Exception) {
            plugin.logger.severe("Failed to save spawn for UUID $uuid: ${e.message}")
        }
    }
}