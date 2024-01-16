package io.github.eendroroy.kotp

import io.github.eendroroy.kotp.config.HOTPConfig
import io.github.eendroroy.kotp.exception.UnsupportedAlgorithmForProvisioningUri
import io.github.eendroroy.kotp.exception.UnsupportedOtpLengthForProvisioningUri
import io.github.eendroroy.kotp.exception.UnsupportedRadixForProvisioningUri
import java.net.URLEncoder
import java.nio.charset.Charset

/**
 * HMAC-based One-time Password Generator
 *
 * @constructor
 *
 * @param configuration OTP properties
 *
 * @since 0.1.2
 *
 * @author indrajit
 */
class HOTP(
    private val configuration: HOTPConfig
) : OTP(
    configuration.secret,
    configuration.length,
    configuration.algorithm,
    configuration.radix
) {

    /**
     * Generates OTP at provided counter
     *
     * @param counter count of OTP
     * @return generated OTP
     *
     * @since 0.1.3
     */
    fun at(counter: Long): String {
        return generateOtp(counter)
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
        UnsupportedOtpLengthForProvisioningUri.passOrThrow(configuration.length)
        UnsupportedAlgorithmForProvisioningUri.passOrThrow(configuration.algorithm)
        UnsupportedRadixForProvisioningUri.passOrThrow(configuration.radix)

        val query = listOf(
            "secret=${encode(configuration.secret.encodedString())}",
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
