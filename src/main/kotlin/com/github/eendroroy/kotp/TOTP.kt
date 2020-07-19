package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.base32.Base32String
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.Calendar
import java.util.Date

/**
 * @author indrajit
 */
open class TOTP(
    private val secret: Base32String,
    private val digits: Int = 6,
    private val digest: Digest = Digest.SHA1,
    private val interval: Int = 30,
    private val issuer: String
) : OTP(secret, digits, digest) {
    private fun timeCode(date: Date): Int = seconds(date) / interval
    private fun timeCode(second: Int): Int = second / interval

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
        (start..end).forEach { if (otp == generateOtp(it)) return it * interval }
        return null
    }

    fun provisioningUri(name: String): String {
        val issuerStr = if (issuer.isNotEmpty()) "${encode(issuer)}:" else ""
        val query = "secret=${encode(secret.raw())}" +
                "&period=${interval}" +
                "&issuer=${encode(issuer)}" +
                "&digits=${digits}" +
                "&algorithm=${encode(digest.name)}"
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