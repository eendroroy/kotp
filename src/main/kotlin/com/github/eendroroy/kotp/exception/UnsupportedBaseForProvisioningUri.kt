package com.github.eendroroy.kotp.exception

import java.lang.RuntimeException

/**
 * @author indrajit
 */
class UnsupportedBaseForProvisioningUri : RuntimeException("supports only {$PROV_BASE_VALUE} base") {
    companion object {
        const val PROV_BASE_VALUE = 10

        fun passOrThrow(value: Int) {
            if (PROV_BASE_VALUE != value) throw UnsupportedBaseForProvisioningUri()
        }
    }
}
