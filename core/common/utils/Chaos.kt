package com.yourcompany.yourgame.utils

import kotlin.random.Random // Used only for the initial seed

/**
 * A global singleton object.
 * This provides a single, shared instance of RandomMech for the entire app.
 */
object Chaos {

    /**
     * The single, shared instance of our random number generator.
     *
     * It is initialized 'by lazy', meaning the code inside the { ... }
     * block is executed exactly ONCE, the very first time 'mech'
     * is accessed by any part of your app.
     */
    val mech: RandomMech by lazy {
        // 1. Create a high-quality initial seed.
        // We combine time, nanoseconds, and another random long
        // to ensure it's extremely unique even if the app restarts quickly.
        val seed = System.currentTimeMillis() xor System.nanoTime() xor Random.nextLong()

        // 2. Initialize the RandomMech class.
        // This line runs only one time for the entire app lifecycle.
        RandomMech(seed)
    }
}