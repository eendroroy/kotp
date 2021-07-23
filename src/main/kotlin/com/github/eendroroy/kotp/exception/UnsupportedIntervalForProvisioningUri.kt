package com.github.eendroroy.kotp.exception

import java.lang.RuntimeException

/**
 * @author indrajit
 */
class UnsupportedIntervalForProvisioningUri : RuntimeException("supports only {$PROV_INTERVAL_VALUE} digits") {
    companion object {
        const val PROV_INTERVAL_VALUE = 30

        fun passOrThrow(value: Int) {
            if (PROV_INTERVAL_VALUE != value) throw UnsupportedIntervalForProvisioningUri()
        }
    }
}
