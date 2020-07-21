package com.github.eendroroy.kotp.config

import com.github.eendroroy.kotp.Digest
import com.github.eendroroy.kotp.base32.Base32
import com.github.eendroroy.kotp.base32.Base32String

/**
 * TOTP Configurations
 *
 * @param secret   secret string encoded by [com.github.eendroroy.kotp.base32.Base32]
 * @param issuer   name of the issuer
 * @param digits   length of the otp, default: 6
 * @param interval interval in seconds to generate new OTP, default: 30
 * @param digest   algorithm to use, default: [Digest.SHA1]
 *
 * @author indrajit
 *
 * @since 0.1.2
 */
data class TOTPConfig(
    val secret: Base32String,
    val issuer: String,
    val digits: Int = 6,
    val interval: Int = 30,
    val digest: Digest = Digest.SHA1
) {
    /**
     * @param secret   plain secret string
     * @param issuer   name of the issuer
     * @param digits   length of the otp, default: 6
     * @param interval interval in seconds to generate new OTP, default: 30
     * @param digest   algorithm to use, default: [Digest.SHA1]
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
        digest: Digest = Digest.SHA1
    ) : this(
        Base32.encode(secret), issuer, digits, interval, digest
    )
}