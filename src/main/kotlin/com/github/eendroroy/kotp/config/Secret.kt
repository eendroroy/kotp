package com.github.eendroroy.kotp.config

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
    private val encodedBytes: ByteArray = Base32().encode(secretString.toByteArray())
    private val encodedString: String = String(encodedBytes).replace("=", "")

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
    fun encoded() = encodedBytes

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
