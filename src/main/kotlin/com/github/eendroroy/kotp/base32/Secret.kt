package com.github.eendroroy.kotp.base32

import org.apache.commons.codec.binary.Base32
import java.security.SecureRandom

/**
 * Provides functions to encode [String] in base32
 *
 * @author indrajit
 *
 * @since 1.0.x
 */
class Secret(secretString: String) {
    private val decodedString: String = secretString
    private val encodedString: String = encode(secretString)

    /**
     * Returns encoded secret string
     *
     * @return [String]
     *
     * @since 1.0.x
     */
    fun encodedString() = encodedString

    /**
     * Returns encoded secret bytearray
     *
     * @return [ByteArray]
     *
     * @since 1.0.x
     */
    fun encoded() = encodedString.toByteArray()

    /**
     * Returns decoded secret string
     *
     * @return [String]
     *
     * @since 1.0.x
     */
    fun decodedString() = decodedString

    /**
     * Returns decoded secret bytearray
     *
     * @return [ByteArray]
     *
     * @since 1.0.x
     */
    fun decoded() = decodedString.toByteArray()

    companion object {
        /**
         * Encodes [ByteArray] to [String]
         *
         * @param bytes [ByteArray] to encode
         *
         * @return [String]
         *
         * @since 1.0.x
         */
        fun encode(bytes: ByteArray): String {
            return Base32().encodeAsString(bytes).replace("=", "")
        }

        /**
         * Base32 encodes a [String]
         *
         * @param str [String] to encode
         *
         * @return [String]
         *
         * @since 1.0.x
         */
        fun encode(str: String): String {
            return encode(str.toByteArray())
        }

        /**
         * Base32 decodes [String] to [ByteArray]
         *
         * @return decoded [ByteArray]
         *
         * @since 1.0.x
         */
        fun decode(str: String): ByteArray {
            return Base32().decode(str.toByteArray())
        }

        /**
         * Decodes a base32 encoded [String]
         *
         * @return decoded [String]
         *
         * @since 1.0.x
         */
        fun decodeAsString(str: String): String {
            return String(decode(str))
        }

        /**
         * Generates a random [Secret]
         *
         * @return [Secret]
         *
         * @since 1.0.x
         */
        fun random(byteLength: Int = 20): Secret {
            return Secret(SecureRandom().generateSeed(byteLength).toString())
        }
    }
}
