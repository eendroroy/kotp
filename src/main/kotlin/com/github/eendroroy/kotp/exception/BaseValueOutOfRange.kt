package com.github.eendroroy.kotp.exception

import java.lang.RuntimeException

/**
 * @author indrajit
 */
class BaseValueOutOfRange : RuntimeException("base was not in valid range {$BASE_VALUE_RANGE}") {
    companion object {
        val BASE_VALUE_RANGE = 10..36

        fun passOrThrow(value: Int) {
            if (!BASE_VALUE_RANGE.contains(value)) throw BaseValueOutOfRange()
        }
    }
}
