package dev.aspulse.randomWorldSpawnPerPlayer.config

import org.bukkit.Location
import org.bukkit.Material
import java.util.Optional

data class SpawnCondition(
    val nAboveAirBlocks: Optional<Int> = Optional.empty(),
    val nBelowOpaqueBlocks: Optional<Int> = Optional.empty(),
    val yUpperLimit: Optional<Int> = Optional.empty(),
    val yLowerLimit: Optional<Int> = Optional.empty()
) {
    
    fun checkSatisfied(location: Location): Boolean {
        val world = location.world ?: return false
        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ

        if (yUpperLimit.isPresent && y > yUpperLimit.get()) {
            return false
        }
        
        if (yLowerLimit.isPresent && y < yLowerLimit.get()) {
            return false
        }

        if (nAboveAirBlocks.isPresent) {
            val required = nAboveAirBlocks.get()
            for (i in 0..<required) {
                val block = world.getBlockAt(x, y + i, z)
                if (!block.type.isAir) {
                    return false
                }
            }
        }

        if (nBelowOpaqueBlocks.isPresent) {
            val required = nBelowOpaqueBlocks.get()
            for (i in 1..required) {
                val block = world.getBlockAt(x, y - i, z)
                if (!block.type.isOccluding) {
                    return false
                }
            }
        }
        
        return true
    }
    companion object {
        fun fromMap(map: Map<String, Any>?): SpawnCondition {
            if (map == null) return SpawnCondition()
            
            return SpawnCondition(
                nAboveAirBlocks = Optional.ofNullable(map["n-above-air-blocks"] as? Int),
                nBelowOpaqueBlocks = Optional.ofNullable(map["n-below-opaque-blocks"] as? Int),
                yUpperLimit = Optional.ofNullable(map["y-upper-limit"] as? Int),
                yLowerLimit = Optional.ofNullable(map["y-lower-limit"] as? Int)
            )
        }
    }
}