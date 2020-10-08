package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.base32.Base32String
import com.github.eendroroy.kotp.config.TOTPConfig
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.Calendar
import java.util.Date

/**
 * Time-based One-time Password Generator
 *
 * @constructor
 *
 * @param config OTP properties
 *
 * @since 0.1.2
 *
 * @author indrajit
 */
class TOTP(private val config: TOTPConfig) : OTP(config.secret, config.digits, config.digest) {
    /**
     * Time-based One-time Password Generator
     * Deprecated
     *
     * @param secret   secret string encoded by [com.github.eendroroy.kotp.base32.Base32]
     * @param digits   length of the otp, default: 6
     * @param digest   algorithm to use, default: [Digest.SHA1]
     * @param interval interval in seconds to generate new OTP, default: 30
     * @param issuer   name of the issuer
     *
     * @since 0.1.1
     */
    @Deprecated(
        message = "Since version: 0.1.2",
        replaceWith = ReplaceWith(
            "TOTP(TOTPConfig(secret, issuer, digits = digits, interval = interval, digest = digest))"
        )
    )
    constructor(
        secret: Base32String,
        digits: Int = 6,
        digest: Digest = Digest.SHA1,
        interval: Int = 30,
        issuer: String
    ) : this(TOTPConfig(secret, issuer, digits, interval, digest))

    private fun timeCode(date: Date): Long = seconds(date) / config.interval
    private fun timeCode(second: Long): Long = second / config.interval

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
        (start..end).forEach { if (otp == generateOtp(it)) return it * config.interval }
        return null
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
     * @since 0.1.1
     */
    @Deprecated(
        message = "Deprecated since version: 0.1.3",
        replaceWith = ReplaceWith("verify(otp, driftAhead.toLong(), driftBehind.toLong(), after, at)")
    )
    fun verify(
        otp: String,
        driftAhead: Int,
        driftBehind: Int,
        after: Date? = null,
        at: Date = Calendar.getInstance().time
    ): Int? {
        return verify(otp, driftAhead.toLong(), driftBehind.toLong(), after, at)?.toInt()
    }

    /**
     * Returns provisioning URI
     * This can then be encoded in a QR Code and used to provision the Google Authenticator app
     *
     * @param name name of the account
     *
     * @return provisioning uri
     *
     * @since 0.1.1
     */
    fun provisioningUri(name: String): String {
        val issuerStr = if (config.issuer.isNotEmpty()) "${encode(config.issuer)}:" else ""
        val query = "secret=${encode(config.secret.raw())}" +
                "&period=${config.interval}" +
                "&issuer=${encode(config.issuer)}" +
                "&digits=${config.digits}" +
                "&algorithm=${encode(config.digest.name)}"
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
