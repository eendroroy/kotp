package io.github.eendroroy.kotp

/**
 * Supported digest algorithms
 *
 * @since 0.1.1
 * @author indrajit
 */
@Deprecated(
    message = "\"Digest\" is deprecated, use \"Algorithm\" instead",
    replaceWith = ReplaceWith("Algorithm", "io.github.eendroroy.kotp.Algorithm"),
    level = DeprecationLevel.WARNING
)
enum class Digest constructor(private val digestName: String) {
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
        return this.digestName
    }
}
