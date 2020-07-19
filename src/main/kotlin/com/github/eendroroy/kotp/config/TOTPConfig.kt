package com.github.eendroroy.kotp.config

import com.github.eendroroy.kotp.Digest
import com.github.eendroroy.kotp.base32.Base32
import com.github.eendroroy.kotp.base32.Base32String

/**
 * @author indrajit
 */
data class TOTPConfig(
    val secret: Base32String,
    val issuer: String,
    val digits: Int = 6,
    val interval: Int = 30,
    val digest: Digest = Digest.SHA1
) {
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
