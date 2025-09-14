package dev.aspulse.randomWorldSpawnPerPlayer.config

import org.bukkit.Bukkit
import org.bukkit.Location

data class Configuration(
    val center: Location,
    val radius: Int,
    val maxRetries: Int,
    val spawnableCondition: SpawnCondition,
    val worldspawnCondition: SpawnCondition
) {
    companion object {
        fun fromMap(map: Map<String, Any>, worldName: String = "world"): Configuration {
            val centerMap = map["center"] as? Map<String, Any> ?: mapOf()
            val world = Bukkit.getWorld(worldName) ?: Bukkit.getWorlds().firstOrNull()
            
            val center = Location(
                world,
                (centerMap["x"] as? Number)?.toDouble() ?: 0.0,
                0.0,
                (centerMap["z"] as? Number)?.toDouble() ?: 0.0
            )
            
            return Configuration(
                center = center,
                radius = (map["radius"] as? Int) ?: 1000,
                maxRetries = (map["max-retries"] as? Int) ?: 100,
                spawnableCondition = SpawnCondition.fromMap(map["spawnable-condition"] as? Map<String, Any>),
                worldspawnCondition = SpawnCondition.fromMap(map["worldspawn-condition"] as? Map<String, Any>)
            )
        }
        
        fun getDefault(worldName: String = "world"): Configuration {
            val world = Bukkit.getWorld(worldName) ?: Bukkit.getWorlds().firstOrNull()
            
            return Configuration(
                center = Location(world, 0.0, 0.0, 0.0),
                radius = 1000,
                maxRetries = 100,
                spawnableCondition = SpawnCondition(
                    nAboveAirBlocks = java.util.Optional.of(2),
                    nBelowOpaqueBlocks = java.util.Optional.of(1)
                ),
                worldspawnCondition = SpawnCondition(
                    nAboveAirBlocks = java.util.Optional.of(10),
                    nBelowOpaqueBlocks = java.util.Optional.of(5),
                    yUpperLimit = java.util.Optional.of(100),
                    yLowerLimit = java.util.Optional.of(45)
                )
            )
        }
    }
}