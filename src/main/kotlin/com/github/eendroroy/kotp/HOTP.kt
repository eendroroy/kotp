package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.base32.Base32String
import com.github.eendroroy.kotp.config.HOTPConfig
import java.net.URLEncoder
import java.nio.charset.Charset

/**
 * @author indrajit
 */
class HOTP(private val config: HOTPConfig) : OTP(config.secret, config.digits, config.digest) {

    @Deprecated(
        message = "Since version: 0.1.2",
        replaceWith = ReplaceWith("HOTP(HOTPConfig(secret, digits = digits, digest = digest))")
    )
    constructor(secret: Base32String, digits: Int = 6, digest: Digest = Digest.SHA1) : this(
        HOTPConfig(secret, digits, digest)
    )

    fun at(count: Int): String {
        return generateOtp(count)
    }

    fun verify(otp: String, counter: Int, retries: Int = 0): Int? {
        (counter..(counter + retries)).forEach { if (otp == at(it)) return it }
        return null
    }

    fun provisioningUri(name: String, initialCount: Int = 0): String {
        val query = "secret=${encode(config.secret.raw())}" +
                "&counter=${encode(initialCount.toString())}" +
                "&digits=${encode(config.digits.toString())}"
        return "otpauth://hotp/${encode(name)}?${query}"
    }

    companion object {
        private val encode: (String) -> String = { value ->
            URLEncoder.encode(value, Charset.defaultCharset().toString())
        }
    }
}