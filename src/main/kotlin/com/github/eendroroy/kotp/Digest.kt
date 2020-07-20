package com.github.eendroroy.kotp

/**
 * Supported digest algorithms
 *
 * @since 0.1.1
 * @author indrajit
 */
enum class Digest constructor(private val digestName: String) {
    /**
     * HmacMD5
     * @name MD5
     * @value HmacMD5
     */
    MD5("HmacMD5"),

    /**
     * HmacSHA1
     * @name SHA1
     * @value HmacSHA1
     */
    SHA1("HmacSHA1"),

    /**
     * HmacSHA224
     * @name SHA224
     * @value HmacSHA224
     */
    SHA224("HmacSHA224"),

    /**
     * HmacSHA256
     * @name SHA256
     * @value HmacSHA256
     */
    SHA256("HmacSHA256"),

    /**
     * HmacSHA384
     * @name SHA384
     * @value HmacSHA384
     */
    SHA384("HmacSHA384"),

    /**
     * HmacSHA512
     * @name SHA512
     * @value HmacSHA512
     */
    SHA512("HmacSHA512");

    /**
     * Digest name
     *
     * @return Digest name
     */
    override fun toString(): String {
        return this.digestName
    }
}
