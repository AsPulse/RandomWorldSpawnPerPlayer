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
    
    private val spawnLocationGenerator = SpawnLocationGenerator(plugin.pluginConfig)
    
    private fun getDefaultSupplier(player: Player): () -> Location {
        return spawnLocationGenerator.getDefaultSupplier(player)
    }

    
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        if (event.isBedSpawn) return
        if (event.isAnchorSpawn) return

        val player = event.player
        val respawnLocation = worldSpawnManager.getOrDefault(player, getDefaultSupplier(player))

        plugin.logger.info("Respawn location for ${player.name} was forced to: $respawnLocation")
        event.respawnLocation = respawnLocation
    }

    
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerSpawnLocation(event: PlayerSpawnLocationEvent) {
        if (event.player.hasPlayedBefore()) return
        
        val player = event.player
        val spawnLocation = worldSpawnManager.getOrDefault(player, getDefaultSupplier(player))
        
        plugin.logger.info("First join spawn for ${player.name} set to: $spawnLocation")
        event.spawnLocation = spawnLocation
    }

}
