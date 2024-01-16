package io.github.eendroroy.kotp

/**
 * Supported digest algorithms
 *
 * @since 1.0.2
 * @author indrajit
 */
enum class Algorithm(private val algorithmName: String) {
    /**
     * HmacSHA1
     * @name SHA1
     * @value HmacSHA1
     */
    SHA1("HmacSHA1"),

    /**
     * HmacSHA256
     * @name SHA256
     * @value HmacSHA256
     */
    SHA256("HmacSHA256"),

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
        return this.algorithmName
    }
}
