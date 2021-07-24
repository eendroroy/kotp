package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.base32.Base32String
import com.github.eendroroy.kotp.config.HOTPConfig
import com.github.eendroroy.kotp.exception.UnsupportedRadixForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedDigestForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedDigitsForProvisioningUri
import java.net.URLEncoder
import java.nio.charset.Charset

/**
 * HMAC-based One-time Password Generator
 *
 * @constructor
 *
 * @param conf OTP properties
 *
 * @since 0.1.2
 *
 * @author indrajit
 */
class HOTP constructor(private val conf: HOTPConfig) : OTP(conf.secret, conf.digits, conf.digest, conf.radix) {

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
     * Returns provisioning URI
     * This can then be encoded in a QR Code and used to provision the Google Authenticator app
     *
     * @param name         name of the account
     * @param initialCount starting counter value, default: 0
     *
     * @return provisioning uri
     *
     * @since 1.0.0
     */
    fun provisioningUri(name: String, initialCount: Long = 0L): String {
        UnsupportedDigitsForProvisioningUri.passOrThrow(conf.digits)
        UnsupportedDigestForProvisioningUri.passOrThrow(conf.digest)
        UnsupportedRadixForProvisioningUri.passOrThrow(conf.radix)

        val query = listOf(
            "secret=${encode(conf.secret.raw())}",
            "&counter=${encode(initialCount.toString())}",
        ).joinToString("")

        return "otpauth://hotp/${encode(name)}?$query"
    }

    companion object {
        private val encode: (String) -> String = { value ->
            URLEncoder.encode(value, Charset.defaultCharset().toString())
        }
    }
}
