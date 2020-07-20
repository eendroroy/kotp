package com.github.eendroroy.kotp.config

import com.github.eendroroy.kotp.Digest
import com.github.eendroroy.kotp.base32.Base32
import com.github.eendroroy.kotp.base32.Base32String

/**
 * HOTP Configurations
 *
 * @param secret secret string encoded by [com.github.eendroroy.kotp.base32.Base32]
 * @param digits length of the otp, default: 6
 * @param digest algorithm to use, default: [Digest.SHA1]
 *
 * @author indrajit
 *
 * @since 0.1.2
 */
data class HOTPConfig(
    val secret: Base32String,
    val digits: Int = 6,
    val digest: Digest = Digest.SHA1
) {
    /**
     * @param secret plain secret string
     * @param digits length of the otp, default: 6
     * @param digest algorithm to use, default: [Digest.SHA1]
     *
     * @author indrajit
     *
     * @since 0.1.2
     */
    constructor(secret: String, digits: Int = 6, digest: Digest = Digest.SHA1) : this(
        Base32.encode(secret), digits, digest
    )
}
