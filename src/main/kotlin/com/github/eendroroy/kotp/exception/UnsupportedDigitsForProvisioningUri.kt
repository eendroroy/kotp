package com.github.eendroroy.kotp.exception

/**
 * @author indrajit
 */
class UnsupportedDigitsForProvisioningUri : RuntimeException("supports only {$PROV_DIGIT_VALUE} digits") {
    companion object {
        const val PROV_DIGIT_VALUE = 6

        fun passOrThrow(value: Int) {
            if (PROV_DIGIT_VALUE != value) throw UnsupportedDigitsForProvisioningUri()
        }
    }
}
