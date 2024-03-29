package io.github.eendroroy.kotp.exception

/**
 * @author indrajit
 *
 * @since 1.0.0
 */
class UnsupportedIntervalForProvisioningUri : RuntimeException("supports only {$PROV_INTERVAL_VALUE} second interval") {
    companion object {
        const val PROV_INTERVAL_VALUE = 30

        fun passOrThrow(value: Int) {
            if (PROV_INTERVAL_VALUE != value) throw UnsupportedIntervalForProvisioningUri()
        }
    }
}
