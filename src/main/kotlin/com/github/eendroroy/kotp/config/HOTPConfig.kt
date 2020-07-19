package com.github.eendroroy.kotp.config

import com.github.eendroroy.kotp.Digest
import com.github.eendroroy.kotp.base32.Base32
import com.github.eendroroy.kotp.base32.Base32String

/**
 * @author indrajit
 */
data class HOTPConfig(
    val secret: Base32String,
    val digits: Int = 6,
    val digest: Digest = Digest.SHA1
) {
    constructor(secret: String, digits: Int = 6, digest: Digest = Digest.SHA1) : this(
        Base32.encode(secret), digits, digest
    )
}
