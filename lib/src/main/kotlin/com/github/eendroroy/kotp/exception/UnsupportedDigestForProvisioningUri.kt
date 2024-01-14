package com.github.eendroroy.kotp.exception

import com.github.eendroroy.kotp.Algorithm
import java.lang.RuntimeException

/**
 * @author indrajit
 *
 * @since 1.0.0
 */
class UnsupportedDigestForProvisioningUri : RuntimeException("supports only {$PROV_Algorithm_VALUE}") {
    companion object {
        val PROV_Algorithm_VALUE = Algorithm.SHA1

        fun passOrThrow(value: Algorithm) {
            if (PROV_Algorithm_VALUE != value) throw UnsupportedDigestForProvisioningUri()
        }
    }
}
