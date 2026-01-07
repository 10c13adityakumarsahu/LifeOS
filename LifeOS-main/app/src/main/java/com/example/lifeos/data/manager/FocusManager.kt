package com.example.lifeos.data.manager

import java.util.concurrent.ConcurrentHashMap

/**
 * Manages temporary state during Focus Mode sessions, 
 * such as tracking incoming call counts for emergency override logic.
 */
object FocusManager {
    private val callCounts = ConcurrentHashMap<String, Int>()
    
    /**
     * Increments the call count for a given number and returns the new count.
     */
    fun incrementAndGetCount(number: String): Int {
        val normalized = number.replace("[^0-9]".toRegex(), "")
        if (normalized.isEmpty()) return 0
        
        val count = (callCounts[normalized] ?: 0) + 1
        callCounts[normalized] = count
        return count
    }
    
    /**
     * Resets all call counts. Should be called when Focus Mode starts or ends.
     */
    fun resetCounts() {
        callCounts.clear()
    }
}
