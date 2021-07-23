package com.github.eendroroy.kotp.exception

import java.lang.RuntimeException

/**
 * @author indrajit
 */
class RadixValueOutOfRange : RuntimeException("radix was not in valid range {$RADIX_VALUE_RANGE}") {
    companion object {
        val RADIX_VALUE_RANGE = 10..36

        fun passOrThrow(value: Int) {
            if (!RADIX_VALUE_RANGE.contains(value)) throw RadixValueOutOfRange()
        }
    }
}
