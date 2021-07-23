package com.github.eendroroy.kotp.exception

import java.lang.RuntimeException

/**
 * @author indrajit
 */
class UnsupportedBaseValue : RuntimeException("supports only base value $PROV_BASE_VALUE") {
    companion object {
        const val PROV_BASE_VALUE = 10

        fun passOrThrow(value: Int) {
            if (PROV_BASE_VALUE != value) throw UnsupportedBaseValue()
        }
    }
}
