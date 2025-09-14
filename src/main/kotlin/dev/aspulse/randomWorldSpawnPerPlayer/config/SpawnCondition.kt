package dev.aspulse.randomWorldSpawnPerPlayer.config

import java.util.Optional

data class SpawnCondition(
    val nAboveAirBlocks: Optional<Int> = Optional.empty(),
    val nBelowOpaqueBlocks: Optional<Int> = Optional.empty(),
    val yUpperLimit: Optional<Int> = Optional.empty(),
    val yLowerLimit: Optional<Int> = Optional.empty()
) {
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