package com.github.eendroroy.kotp.exception

import java.lang.RuntimeException

/**
 * @author indrajit
 *
 * @since 1.0.0
 */
class UnsupportedRadixForProvisioningUri : RuntimeException("supports only {$PROV_RADIX_VALUE} radix") {
    companion object {
        const val PROV_RADIX_VALUE = 10

        fun passOrThrow(value: Int) {
            if (PROV_RADIX_VALUE != value) throw UnsupportedRadixForProvisioningUri()
        }
    }
}
