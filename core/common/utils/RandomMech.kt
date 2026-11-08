import kotlin.system.exitProcess

/**
 * Implements the Xoshiro128++ 1.0 PRNG algorithm.
 *
 * This is a fast, high-quality PRNG for 32-bit output.
 * It is not cryptographically secure and should not be used for such.
 *
 * Original algorithm by David Blackman and Sebastiano Vigna.
 *
 * @param seed The 64-bit seed to initialize the generator.
 */
class RandomMech(seed: Long) {

    // The 128-bit state of the generator, held as four 32-bit integers.
    private val state = IntArray(4)

    init {
        // Initialize the state using a SplitMix64 generator
        // This ensures a good starting state even from a simple seed.
        // Replaced hex literals with their signed decimal equivalents to fix compiler "value out of range" errors.
        var z = seed + 0x9E3779B97F4A7C15L //  -7046029254386353131L
        for (i in 0..3) {
            z = (z xor (z ushr 30)) * 0xBF58476D1CE4E5B9L // -4641369832810843175L
            z = (z xor (z ushr 27)) * 0x94D049BB133111EBL // -7733878170889821717L
            state[i] = (z xor (z ushr 31)).toInt()
        }
    }

    /**
     * Helper function for 32-bit left rotation.
     */
    private fun rotl(x: Int, k: Int): Int {
        return (x shl k) or (x ushr (32 - k))
    }

    /**
     * Generates and returns the next 32-bit pseudo-random integer.
     */
    fun nextInt(): Int {
        val result = rotl(state[0] + state[3], 7) + state[0]
        val t = state[1] shl 9

        state[2] = state[2] xor state[0]
        state[3] = state[3] xor state[1]
        state[1] = state[1] xor state[2]
        state[0] = state[0] xor state[3]
        state[2] = state[2] xor t
        state[3] = rotl(state[3], 11)

        return result
    }

    // --- Utility Functions ---

    /**
     * Generates a random integer between x (inclusive) and y (inclusive).
     *
     * @param x The lower bound of the range.
     * @param y The upper bound of the range.
     * @return A random integer within [x, y].
     */
    fun numberBetweenXandY(x: Int, y: Int): Int {
        if (x > y) {
            println("Error: Lower bound x ($x) cannot be greater than upper bound y ($y).")
            return x // Or throw an exception
        }
        // We use toLong() to prevent overflow if (y - x) is near Int.MAX_VALUE
        val range = y.toLong() - x.toLong() + 1
        
        // Get the next random 32-bit value and treat it as unsigned (0 to 2^32 - 1)
        val randomUnsigned = nextInt().toLong() and 0xFFFFFFFFL
        
        // Map the value to the desired range
        return (x + (randomUnsigned % range)).toInt()
    }

    /**
     * Checks for success based on a given percentage chance.
     *
     * @param percent The success chance (1-100). Defaults to 50.
     * @return `true` if the check succeeds, `false` otherwise.
     */
    fun checkPercentageSuccess(percent: Int = 50): Boolean {
        val p = when {
            percent <= 0 -> 0
            percent >= 100 -> 100
            else -> percent
        }
        
        if (p == 0) return false
        if (p == 100) return true
        
        // Get a random number between 1 and 100
        val roll = numberBetweenXandY(1, 100)
        
        // Success if the roll is less than or equal to the percentage
        return roll <= p
    }

    /**
     * Simulates rolling `n` dice, each with `d` faces, and returns the sum.
     *
     * @param n The number of dice to roll. Defaults to 1.
     * @param d The number of faces on each die. Defaults to 6.
     * @return The total sum of all dice rolls.
     */
    fun diceRoll(n: Int = 1, d: Int = 6): Int {
        val numDice = if (n < 1) 1 else n
        val numFaces = if (d < 2) 2 else d // A die must have at least 2 faces

        var total = 0
        repeat(numDice) {
            total += numberBetweenXandY(1, numFaces)
        }
        return total
    }

    /**
     * Shuffles a mutable list in-place using the Fisher-Yates algorithm
     * and this generator's 'numberBetweenXandY' function.
     *
     * @param list The list to be shuffled.
     */
    fun <T> shuffleList(list: MutableList<T>) {
        // Loop from the last index down to 1
        for (i in list.lastIndex downTo 1) {
            // Pick a random index j from 0 to i (inclusive)
            val j = numberBetweenXandY(0, i)

            // Swap element at i with element at j
            val temp = list[i]
            list[i] = list[j]
            list[j] = temp
        }
    }
}