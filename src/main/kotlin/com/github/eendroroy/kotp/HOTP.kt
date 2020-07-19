package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.base32.Base32String
import java.net.URLEncoder
import java.nio.charset.Charset

/**
 * @author indrajit
 */
class HOTP(
    private val secret: Base32String,
    private val digits: Int = 6,
    digest: Digest = Digest.SHA1
) : OTP(secret, digits, digest) {
    fun at(count: Int): String {
        return generateOtp(count)
    }

    fun verify(otp: String, counter: Int, retries: Int = 0): Int? {
        (counter..(counter + retries)).forEach { if (otp == at(it)) return it }
        return null
    }

    fun provisioningUri(name: String, initialCount: Int = 0): String {
        val query = "secret=${encode(secret.raw())}" +
                "&counter=${encode(initialCount.toString())}" +
                "&digits=${encode(digits.toString())}"
        return "otpauth://hotp/${encode(name)}?${query}"
    }

    companion object {
        private val encode: (String) -> String = { value ->
            URLEncoder.encode(value, Charset.defaultCharset().toString())
        }
    }
}