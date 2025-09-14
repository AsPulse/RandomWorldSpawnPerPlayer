package dev.aspulse.randomWorldSpawnPerPlayer.spawn

import dev.aspulse.randomWorldSpawnPerPlayer.config.Configuration
import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class SpawnLocationGenerator(private val config: Configuration) {
    
    fun getDefaultSupplier(player: Player): () -> Location {
        return {
            generateRandomSpawnLocation(player)
        }
    }
    
    private fun generateRandomSpawnLocation(player: Player): Location {
        // For now, return a random location within the configured radius
        // This will be expanded later to check spawn conditions
        val world = config.center.world ?: player.world
        val centerX = config.center.x
        val centerZ = config.center.z
        
        val angle = Random.nextDouble() * 2 * Math.PI
        val distance = Random.nextDouble() * config.radius
        
        val x = centerX + distance * cos(angle)
        val z = centerZ + distance * sin(angle)
        val y = world.getHighestBlockYAt(x.toInt(), z.toInt()) + 1.0
        
        return Location(world, x, y, z)
    }
}