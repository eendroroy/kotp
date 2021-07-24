package com.github.eendroroy.kotp.exception

import java.lang.RuntimeException

/**
 * @author indrajit
 *
 * @since 1.0.0
 */
class RadixValueOutOfRange : RuntimeException("radix was not in valid range {$RADIX_VALUE_RANGE}") {
    companion object {
        val RADIX_VALUE_RANGE = 10..36

        fun passOrThrow(value: Int) {
            if (!RADIX_VALUE_RANGE.contains(value)) throw RadixValueOutOfRange()
        }
    }
}
