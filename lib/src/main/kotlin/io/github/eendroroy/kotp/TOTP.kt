package io.github.eendroroy.kotp

import io.github.eendroroy.kotp.config.TOTPConfig
import io.github.eendroroy.kotp.exception.UnsupportedAlgorithmForProvisioningUri
import io.github.eendroroy.kotp.exception.UnsupportedOtpLengthForProvisioningUri
import io.github.eendroroy.kotp.exception.UnsupportedIntervalForProvisioningUri
import io.github.eendroroy.kotp.exception.UnsupportedRadixForProvisioningUri
import io.github.eendroroy.kotp.extensions.currentSeconds
import java.net.URLEncoder
import java.nio.charset.Charset

/**
 * Time-based One-time Password Generator
 *
 * @constructor
 *
 * @param configuration OTP properties
 *
 * @since 0.1.2
 *
 * @author indrajit
 */
class TOTP(
    private val configuration: TOTPConfig
) : OTP(
    configuration.secret,
    configuration.length,
    configuration.algorithm,
    configuration.radix
) {
    private fun timeCode(second: Long): Long = second / configuration.interval

    /**
     * Generates OTP at provided epoch seconds [Long]
     *
     * @param epochSeconds epoch seconds
     * @return generated OTP
     *
     * @since 1.0.1
     */
    fun at(epochSeconds: Long): String {
        return generateOtp(timeCode(epochSeconds))
    }

    /**
     * Generates OTP at now (current time)
     *
     * @return generated OTP
     *
     * @since 0.1.1
     */
    fun now(): String {
        return at(currentSeconds())
    }

    /**
     * Verifies the OTP against the current time OTP and adjacent intervals using [driftAhead] and [driftBehind].
     *
     * Excludes OTPs from [after] and earlier. Returns time value of matching OTP code for use in subsequent call.
     *
     * @param otp         OTP to verify
     * @param at          epoch seconds at which to verify OTP. default: current seconds
     * @param after       prevent token reuse, last login timestamp
     * @param driftAhead  seconds to look ahead
     * @param driftBehind seconds to look back
     *
     * @return the last successful timestamp interval
     *
     * @since 1.0.1
     */
    @JvmOverloads
    fun verify(
        otp: String,
        at: Long = currentSeconds(),
        after: Long? = null,
        driftAhead: Long = 0L,
        driftBehind: Long = 0L,
    ): Long? {
        var start = timeCode(at - driftBehind)
        after?.let { timeCode(it) }?.run { if (start < this) start = this }
        val end = timeCode(at + driftAhead)
        (start..end).forEach { if (otp == generateOtp(it)) return it * configuration.interval }
        return null
    }

    /**
     * Returns provisioning URI
     * This can then be encoded in a QR Code and used to provision the Google Authenticator app
     *
     * @param name name of the account
     *
     * @return provisioning uri
     *
     * @since 1.0.0
     */
    fun provisioningUri(name: String): String {
        UnsupportedIntervalForProvisioningUri.passOrThrow(configuration.interval)
        UnsupportedOtpLengthForProvisioningUri.passOrThrow(configuration.length)
        UnsupportedAlgorithmForProvisioningUri.passOrThrow(configuration.algorithm)
        UnsupportedRadixForProvisioningUri.passOrThrow(configuration.radix)

        val issuerStr = if (configuration.issuer.isNotEmpty()) "${encode(configuration.issuer)}:" else ""

        val query = listOf(
            "secret=${encode(configuration.secret.encodedString())}",
            "&issuer=${encode(configuration.issuer)}"
        ).joinToString("")

        return "otpauth://totp/${issuerStr}${encode(name)}?$query"
    }

    companion object {
        private val encode = { value: String ->
            URLEncoder.encode(value, Charset.defaultCharset().toString())
        }
    }
}
