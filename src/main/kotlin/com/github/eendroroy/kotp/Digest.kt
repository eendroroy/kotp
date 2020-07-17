package com.github.eendroroy.kotp

/**
 * @author indrajit
 */
enum class Digest constructor(private val digestName: String) {
    MD5("HmacMD5"),
    SHA1("HmacSHA1"),
    SHA224("HmacSHA224"),
    SHA256("HmacSHA256"),
    SHA384("HmacSHA384"),
    SHA512("HmacSHA512");

    override fun toString(): String {
        return this.digestName
    }
}