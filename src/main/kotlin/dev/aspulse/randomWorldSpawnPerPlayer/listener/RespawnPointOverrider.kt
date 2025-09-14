package dev.aspulse.randomWorldSpawnPerPlayer.listener

import dev.aspulse.randomWorldSpawnPerPlayer.RandomWorldSpawnPerPlayer
import dev.aspulse.randomWorldSpawnPerPlayer.spawn.SpawnLocationGenerator
import dev.aspulse.randomWorldSpawnPerPlayer.spawn.WorldSpawnManager
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import org.spigotmc.event.player.PlayerSpawnLocationEvent

class RespawnPointOverrider(
    private val plugin: RandomWorldSpawnPerPlayer,
    private val worldSpawnManager: WorldSpawnManager
) : Listener {
    
    private val spawnLocationGenerator = SpawnLocationGenerator(plugin.pluginConfig, plugin)
    
    private fun getDefaultSupplier(player: Player): () -> Location {
        return spawnLocationGenerator.getDefaultSupplier(player)
    }
    
    private fun centerLocation(location: Location): Location {
        return Location(
            location.world,
            location.blockX + 0.5,
            location.y,
            location.blockZ + 0.5,
            location.yaw,
            location.pitch
        )
    }

    
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        if (event.isBedSpawn) return
        if (event.isAnchorSpawn) return

        val player = event.player
        val worldSpawn = worldSpawnManager.getOrDefault(player, getDefaultSupplier(player))
        
        // Check if the world spawn location satisfies spawnable conditions
        if (plugin.pluginConfig.spawnableCondition.checkSatisfied(worldSpawn)) {
            plugin.logger.info("Respawn location for ${player.name} was ok and set to: $worldSpawn")
            event.respawnLocation = centerLocation(worldSpawn)
            return
        }
        
        // Try to find a suitable location nearby using beamSearch
        val searchResult = spawnLocationGenerator.beamSearch(plugin.pluginConfig.spawnableCondition, worldSpawn)
        if (searchResult.isPresent) {
            val adjustedLocation = searchResult.get()
            plugin.logger.info("Respawn location for ${player.name} adjusted to: $adjustedLocation (original didn't satisfy spawnable conditions)")
            event.respawnLocation = centerLocation(adjustedLocation)
            return
        }
        
        // Fallback: use the original world spawn even if it doesn't satisfy conditions
        plugin.logger.warning("Could not find suitable respawn location for ${player.name} near $worldSpawn")
        plugin.logger.warning("Using original location despite not satisfying spawnable conditions")
        event.respawnLocation = centerLocation(worldSpawn)
    }

    
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerSpawnLocation(event: PlayerSpawnLocationEvent) {
        if (event.player.hasPlayedBefore()) return
        
        val player = event.player
        val spawnLocation = worldSpawnManager.getOrDefault(player, getDefaultSupplier(player))
        
        plugin.logger.info("First join spawn for ${player.name} set to: $spawnLocation")
        event.spawnLocation = centerLocation(spawnLocation)
    }

}
