package com.github.eendroroy.kotp.base32

import org.apache.commons.codec.binary.Base32
import java.security.SecureRandom

/**
 * Provides functions to encode [String] in base32
 *
 * @author indrajit
 *
 * @since 0.1.2
 */
class Base32 {
    companion object {
        /**
         * Encodes [ByteArray] to [Base32String]
         *
         * @param bytes [ByteArray] to encode
         *
         * @return [Base32String]
         *
         * @since 0.1.2
         */
        fun encode(bytes: ByteArray): Base32String {
            return Base32String.Builder(Base32().encodeAsString(bytes).replace("=", "")).build()
        }

        /**
         * Encodes [String] to [Base32String]
         *
         * @param str [String] to encode
         *
         * @return [Base32String]
         *
         * @since 0.1.2
         */
        fun encode(str: String): Base32String {
            return encode(str.toByteArray())
        }

        /**
         * Generates a random [Base32String]
         *
         * @return [Base32String]
         *
         * @since 0.1.2
         */
        fun random(byteLength: Int = 20): Base32String {
            return encode(SecureRandom().generateSeed(byteLength))
        }
    }
}
