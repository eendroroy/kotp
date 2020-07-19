package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.base32.Base32String
import com.github.eendroroy.kotp.config.TOTPConfig
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.Calendar
import java.util.Date

/**
 * @author indrajit
 */
class TOTP(private val config: TOTPConfig) : OTP(config.secret, config.digits, config.digest) {

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

    private fun timeCode(date: Date): Int = seconds(date) / config.interval
    private fun timeCode(second: Int): Int = second / config.interval

    fun at(time: Date): String {
        return generateOtp(timeCode(time))
    }

    fun now(): String {
        return generateOtp(timeCode(Calendar.getInstance().time))
    }

    fun verify(
        otp: String,
        driftAhead: Int = 0,
        driftBehind: Int = 0,
        after: Date? = null,
        at: Date = Calendar.getInstance().time
    ): Int? {
        val now = seconds(at)
        var start = timeCode(now - driftBehind)
        after?.let { seconds(it).run { if (start < this) start = this } }
        val end = timeCode(now + driftAhead)
        (start..end).forEach { if (otp == generateOtp(it)) return it * config.interval }
        return null
    }

    fun provisioningUri(name: String): String {
        val issuerStr = if (config.issuer.isNotEmpty()) "${encode(config.issuer)}:" else ""
        val query = "secret=${encode(config.secret.raw())}" +
                "&period=${config.interval}" +
                "&issuer=${encode(config.issuer)}" +
                "&digits=${config.digits}" +
                "&algorithm=${encode(config.digest.name)}"
        return "otpauth://totp/${issuerStr}${encode(name)}?${query}"
    }

    companion object {
        private val seconds = { date: Date ->
            (Calendar.getInstance().apply { this.time = date }.timeInMillis / 1_000).toInt()
        }

        private val encode = { value: String ->
            URLEncoder.encode(value, Charset.defaultCharset().toString())
        }
    }
}