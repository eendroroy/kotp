package com.github.eendroroy.kotp.base32

import org.apache.commons.codec.binary.Base32
import java.security.SecureRandom

/**
 * @author indrajit
 */
class Base32 {
    companion object {
        fun encode(bytes: ByteArray): Base32String {
            return Base32String.Builder(Base32().encodeAsString(bytes).replace("=", "")).build()
        }

        fun encode(str: String): Base32String {
            return encode(str.toByteArray())
        }

        fun random(byteLength: Int = 20): Base32String {
            return encode(SecureRandom().generateSeed(byteLength))
        }
    }
}

