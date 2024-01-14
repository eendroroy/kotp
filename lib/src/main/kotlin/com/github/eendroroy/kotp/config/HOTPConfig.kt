package com.github.eendroroy.kotp.config

import com.github.eendroroy.kotp.Digest
import com.github.eendroroy.kotp.exception.RadixValueOutOfRange

/**
 * HOTP Configurations
 *
 * @param secret [Secret]
 * @param digits length of the otp, default: 6
 * @param digest algorithm to use, default: [Digest.SHA1]
 * @param radix  radix/base of the OTP value, default: 10 (decimal)
 *
 * @author indrajit
 *
 * @since 1.0.1
 */
data class HOTPConfig(
    val secret: Secret,
    val digits: Int = 6,
    val digest: Digest = Digest.SHA1,
    val radix: Int = 10
) {
    init {
        RadixValueOutOfRange.passOrThrow(radix)
    }

    /**
     * @param secret plain secret string
     * @param digits length of the otp, default: 6
     * @param digest algorithm to use, default: [Digest.SHA1]
     * @param radix  radix/base of the OTP value, default: 10 (decimal)
     *
     * @author indrajit
     *
     * @since 1.0.1
     */
    constructor(secret: String, digits: Int = 6, digest: Digest = Digest.SHA1, radix: Int = 10) : this(
        Secret(secret), digits, digest, radix
    )
}
