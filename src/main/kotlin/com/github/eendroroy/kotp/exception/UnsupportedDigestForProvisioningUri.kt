package com.github.eendroroy.kotp.exception

import com.github.eendroroy.kotp.Digest
import java.lang.RuntimeException

/**
 * @author indrajit
 */
class UnsupportedDigestForProvisioningUri : RuntimeException("supports only {$PROV_DIGEST_VALUE}") {
    companion object {
        val PROV_DIGEST_VALUE = Digest.SHA1

        fun passOrThrow(value: Digest) {
            if (PROV_DIGEST_VALUE != value) throw UnsupportedDigestForProvisioningUri()
        }
    }
}
