package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.base32.Base32String
import com.github.eendroroy.kotp.config.HOTPConfig
import java.net.URLEncoder
import java.nio.charset.Charset

/**
 * HMAC-based One-time Password Generator
 *
 * @constructor
 *
 * @param config OTP properties
 *
 * @since 0.1.2
 *
 * @author indrajit
 */
class HOTP constructor(private val config: HOTPConfig) : OTP(config.secret, config.digits, config.digest) {

    /**
     * HMAC-based One-time Password Generator
     * Deprecated
     *
     * @param secret secret string encoded by [com.github.eendroroy.kotp.base32.Base32]
     * @param digits length of the otp, default: 6
     * @param digest algorithm to use, default: [Digest.SHA1]
     *
     * @since 0.1.1
     */
    @Deprecated(
        message = "Deprecated since version: 0.1.2",
        replaceWith = ReplaceWith("HOTP(HOTPConfig(secret, digits = digits, digest = digest))")
    )
    constructor(secret: Base32String, digits: Int = 6, digest: Digest = Digest.SHA1) : this(
        HOTPConfig(secret, digits, digest)
    )

    /**
     * Generates OTP at provided counter
     *
     * @param count count of OTP
     * @return generated OTP
     *
     * @since 0.1.3
     */
    fun at(count: Long): String {
        return generateOtp(count)
    }

    /**
     * Generates OTP at provided counter
     *
     * @param count count of OTP
     * @return generated OTP
     *
     * @since 0.1.1
     */
    @Deprecated(
        message = "Deprecated since version: 0.1.3",
        replaceWith = ReplaceWith("at(count.toLong())")
    )
    fun at(count: Int): String {
        return at(count.toLong())
    }

    /**
     * Verifies OTP
     *
     * @param otp     OTP
     * @param counter counter of the OTP
     * @param retries number of retry
     *
     * @return first matched counter
     *
     * @since 0.1.3
     */
    fun verify(otp: String, counter: Long, retries: Long = 0): Long? {
        (counter..(counter + retries)).forEach { if (otp == at(it)) return it }
        return null
    }

    /**
     * Verifies OTP
     *
     * @param otp     OTP
     * @param counter counter of the OTP
     * @param retries number of retry
     *
     * @return first matched counter
     *
     * @since 0.1.1
     */
    @Deprecated(
        message = "Deprecated since version: 0.1.3",
        replaceWith = ReplaceWith("verify(otp, counter.toLong(), retries.toLong())")
    )
    fun verify(otp: String, counter: Int, retries: Int = 0): Int? {
        return verify(otp, counter.toLong(), retries.toLong())?.toInt()
    }

    /**
     * Returns provisioning URI
     * This can then be encoded in a QR Code and used to provision the Google Authenticator app
     *
     * @param name         name of the account
     * @param initialCount starting counter value, default: 0
     *
     * @return provisioning uri
     *
     * @since 0.1.3
     */
    fun provisioningUri(name: String, initialCount: Long = 0L): String {
        val query = "secret=${encode(config.secret.raw())}" +
                "&counter=${encode(initialCount.toString())}" +
                "&digits=${encode(config.digits.toString())}"
        return "otpauth://hotp/${encode(name)}?$query"
    }

    /**
     * Returns provisioning URI
     * This can then be encoded in a QR Code and used to provision the Google Authenticator app
     *
     * @param name         name of the account
     * @param initialCount starting counter value, default: 0
     *
     * @return provisioning uri
     *
     * @since 0.1.1
     */
    @Deprecated(
        message = "Deprecated since version: 0.1.3",
        replaceWith = ReplaceWith("provisioningUri(name, initialCount.toLong())")
    )
    fun provisioningUri(name: String, initialCount: Int): String {
        return provisioningUri(name, initialCount.toLong())
    }

    companion object {
        private val encode: (String) -> String = { value ->
            URLEncoder.encode(value, Charset.defaultCharset().toString())
        }
    }
}
