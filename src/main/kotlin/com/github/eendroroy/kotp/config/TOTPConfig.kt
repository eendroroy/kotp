package com.github.eendroroy.kotp.config

import com.github.eendroroy.kotp.Digest
import com.github.eendroroy.kotp.exception.RadixValueOutOfRange

/**
 * TOTP Configurations
 *
 * @param secret   [Secret]
 * @param issuer   name of the issuer
 * @param digits   length of the otp, default: 6
 * @param interval interval in seconds to generate new OTP, default: 30
 * @param digest   algorithm to use, default: [Digest.SHA1]
 * @param radix    radix/base of the OTP value, default: 10 (decimal)
 *
 * @author indrajit
 *
 * @since 1.0.1
 */
data class TOTPConfig(
    val secret: Secret,
    val issuer: String,
    val digits: Int = 6,
    val interval: Int = 30,
    val digest: Digest = Digest.SHA1,
    val radix: Int = 10
) {
    init {
        RadixValueOutOfRange.passOrThrow(radix)
    }

    /**
     * @param secret   plain secret string
     * @param issuer   name of the issuer
     * @param digits   length of the otp, default: 6
     * @param interval interval in seconds to generate new OTP, default: 30
     * @param digest   algorithm to use, default: [Digest.SHA1]
     * @param radix    radix/base of the OTP value, default: 10 (decimal)
     *
     * @author indrajit
     *
     * @since 0.1.2
     */
    constructor(
        secret: String,
        issuer: String,
        digits: Int = 6,
        interval: Int = 30,
        digest: Digest = Digest.SHA1,
        radix: Int = 10
    ) : this(Secret(secret), issuer, digits, interval, digest, radix)
}
