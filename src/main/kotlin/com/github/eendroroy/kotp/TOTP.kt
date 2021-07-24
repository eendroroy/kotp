package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.config.TOTPConfig
import com.github.eendroroy.kotp.exception.UnsupportedDigestForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedDigitsForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedIntervalForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedRadixForProvisioningUri
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.Calendar
import java.util.Date

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
class TOTP(private val conf: TOTPConfig) : OTP(conf.secret, conf.digits, conf.digest, conf.radix) {
    private fun timeCode(date: Date): Long = seconds(date) / conf.interval
    private fun timeCode(second: Long): Long = second / conf.interval

    /**
     * Generates OTP at provided [Date]
     *
     * @param time time
     * @return generated OTP
     *
     * @since 0.1.1
     */
    fun at(time: Date): String {
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
        return generateOtp(timeCode(Calendar.getInstance().time))
    }

    /**
     * Verifies the OTP against the current time OTP and adjacent intervals using [driftAhead] and [driftBehind].
     *
     * Excludes OTPs from [after] and earlier. Returns time value of matching OTP code for use in subsequent call.
     *
     * @param otp         OTP to verify
     * @param driftAhead  seconds to look ahead
     * @param driftBehind seconds to look back
     * @param after       prevent token reuse, last login timestamp
     * @param at          time at which to verify OTP. default: current time
     *
     * @return the last successful timestamp interval
     *
     * @since 0.1.3
     */
    fun verify(
        otp: String,
        driftAhead: Long = 0L,
        driftBehind: Long = 0L,
        after: Date? = null,
        at: Date = Calendar.getInstance().time
    ): Long? {
        val now = seconds(at)
        var start = timeCode(now - driftBehind)
        after?.let { seconds(it).run { if (start < this) start = this } }
        val end = timeCode(now + driftAhead)
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
        UnsupportedDigestForProvisioningUri.passOrThrow(conf.digest)
        UnsupportedRadixForProvisioningUri.passOrThrow(conf.radix)

        val issuerStr = if (conf.issuer.isNotEmpty()) "${encode(conf.issuer)}:" else ""

        val query = listOf(
            "secret=${encode(conf.secret.encodedString())}",
            "&issuer=${encode(conf.issuer)}"
        ).joinToString("")

        return "otpauth://totp/${issuerStr}${encode(name)}?$query"
    }

    companion object {
        private val seconds = { date: Date ->
            (Calendar.getInstance().apply { this.time = date }.timeInMillis / 1_000)
        }

        private val encode = { value: String ->
            URLEncoder.encode(value, Charset.defaultCharset().toString())
        }
    }
}
