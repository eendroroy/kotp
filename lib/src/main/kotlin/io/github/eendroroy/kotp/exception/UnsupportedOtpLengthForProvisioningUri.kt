package io.github.eendroroy.kotp.exception

/**
 * @author indrajit
 *
 * @since 1.0.2
 */
class UnsupportedOtpLengthForProvisioningUri : RuntimeException("supports only {$PROV_LENGTH_VALUE} digits") {
    companion object {
        const val PROV_LENGTH_VALUE = 6

        fun passOrThrow(value: Int) {
            if (PROV_LENGTH_VALUE != value) throw UnsupportedOtpLengthForProvisioningUri()
        }
    }
}
