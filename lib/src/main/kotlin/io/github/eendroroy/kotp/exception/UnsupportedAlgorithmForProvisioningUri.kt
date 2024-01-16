package io.github.eendroroy.kotp.exception

import io.github.eendroroy.kotp.Algorithm
import java.lang.RuntimeException

/**
 * @author indrajit
 *
 * @since 1.0.2
 */
class UnsupportedAlgorithmForProvisioningUri : RuntimeException("supports only {$PROV_ALGORITHM_VALUE}") {
    companion object {
        val PROV_ALGORITHM_VALUE = Algorithm.SHA1

        fun passOrThrow(value: Algorithm) {
            if (PROV_ALGORITHM_VALUE != value) throw UnsupportedAlgorithmForProvisioningUri()
        }
    }
}
