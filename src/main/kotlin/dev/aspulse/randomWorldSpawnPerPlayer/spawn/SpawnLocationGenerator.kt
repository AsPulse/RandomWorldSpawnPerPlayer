package dev.aspulse.randomWorldSpawnPerPlayer.spawn

import com.google.common.math.IntMath.pow
import dev.aspulse.randomWorldSpawnPerPlayer.config.Configuration
import dev.aspulse.randomWorldSpawnPerPlayer.config.SpawnCondition
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Math.pow
import java.util.Optional
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

class SpawnLocationGenerator(private val config: Configuration, private val plugin: JavaPlugin) {
    
    fun getDefaultSupplier(player: Player): () -> Location {
        return {
            generateRandomSpawnLocation(player)
        }
    }
    
    private fun generateRandomSpawnLocation(player: Player): Location {
        for (attempt in 1..config.maxRetries) {
            val candidate = generateCandidate(player)
            val searchResult = beamSearch(config.worldspawnCondition, candidate)
            if (searchResult.isPresent) {
                plugin.logger.info("Worldspawn was generated for ${player.name} after $attempt try(s)")
                return searchResult.get()
            }
        }

        // Fallback: couldn't find suitable location after max attempts
        val fallback = generateCandidate(player)
        plugin.logger.warning("Could not find suitable spawn location for ${player.name} after ${config.maxRetries} attempts.")
        plugin.logger.warning("Using fallback location: $fallback (may not satisfy worldspawn conditions)")
        return fallback
    }
    
    private fun generateCandidate(player: Player): Location {
        val world = config.center.world ?: player.world
        val centerX = config.center.x
        val centerZ = config.center.z
        
        val angle = Random.nextDouble() * 2 * Math.PI
        val distance = sqrt(Random.nextDouble() * pow(config.radius, 2))
        
        val x = centerX + distance * cos(angle)
        val z = centerZ + distance * sin(angle)
        val y = world.getHighestBlockYAt(x.toInt(), z.toInt()) + 1.0
        
        return Location(world, x, y, z)
    }
    
    fun beamSearch(spawnCondition: SpawnCondition, location: Location): Optional<Location> {
        val world = location.world ?: return Optional.empty()
        val baseX = location.blockX
        val baseZ = location.blockZ
        
        // Create a list of offsets sorted by distance from center
        val offsets = mutableListOf<Pair<Int, Int>>()
        for (dx in -10..10) {
            for (dz in -10..10) {
                offsets.add(dx to dz)
            }
        }
        
        // Sort by distance from center (closer points first)
        offsets.sortBy { (dx, dz) -> dx * dx + dz * dz }
        
        // Check each position in order of distance
        for ((dx, dz) in offsets) {
            val x = baseX + dx
            val z = baseZ + dz
            val y = world.getHighestBlockYAt(x, z) + 1
            
            val candidate = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
            if (spawnCondition.checkSatisfied(candidate)) {
                val distance = kotlin.math.sqrt((dx * dx + dz * dz).toDouble())
                plugin.logger.info("Found suitable location at distance ${"%.1f".format(distance)} blocks from original")
                return Optional.of(candidate)
            }
        }
        
        return Optional.empty()
    }
}