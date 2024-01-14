package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.config.TOTPConfig
import com.github.eendroroy.kotp.exception.UnsupportedDigestForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedDigitsForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedIntervalForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedRadixForProvisioningUri
import com.github.eendroroy.kotp.extensions.currentSeconds
import java.net.URLEncoder
import java.nio.charset.Charset

/**
 * Time-based One-time Password Generator
 *
 * @constructor
 *
 * @param conf OTP properties
 *
 * @since 0.1.2
 *
 * @author indrajit
 */
class TOTP(private val conf: TOTPConfig) : OTP(conf.secret, conf.digits, conf.algorithm, conf.radix) {
    private fun timeCode(second: Long): Long = second / conf.interval

    /**
     * Generates OTP at provided epoch seconds [Long]
     *
     * @param time epoch seconds
     * @return generated OTP
     *
     * @since 1.0.1
     */
    fun at(time: Long): String {
        return generateOtp(timeCode(time))
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
        (start..end).forEach { if (otp == generateOtp(it)) return it * conf.interval }
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
        UnsupportedIntervalForProvisioningUri.passOrThrow(conf.interval)
        UnsupportedDigitsForProvisioningUri.passOrThrow(conf.digits)
        UnsupportedDigestForProvisioningUri.passOrThrow(conf.algorithm)
        UnsupportedRadixForProvisioningUri.passOrThrow(conf.radix)

        val issuerStr = if (conf.issuer.isNotEmpty()) "${encode(conf.issuer)}:" else ""

        val query = listOf(
            "secret=${encode(conf.secret.encodedString())}",
            "&issuer=${encode(conf.issuer)}"
        ).joinToString("")

        return "otpauth://totp/${issuerStr}${encode(name)}?$query"
    }

    companion object {
        private val encode = { value: String ->
            URLEncoder.encode(value, Charset.defaultCharset().toString())
        }
    }
}
