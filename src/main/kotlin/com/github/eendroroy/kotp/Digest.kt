package com.github.eendroroy.kotp

/**
 * @author indrajit
 */
enum class Digest constructor(private val digestName: String) {
    DESMAC("DESMAC"),
    DESMAC_CFB8("DESMAC/CFB8"),
    DESedeMAC("DESedeMAC"),
    DESedeMAC_CFB8("DESedeMAC/CFB8"),
    DESedeMAC64("DESedeMAC64"),
    DESwithISO9797("DESwithISO9797"),
    HmacMD5("HmacMD5"),
    HmacSHA1("HmacSHA1"),
    HmacSHA224("HmacSHA224"),
    HmacSHA256("HmacSHA256"),
    HmacSHA384("HmacSHA384"),
    HmacSHA512("HmacSHA512"),
    ISO9797ALG3MAC("ISO9797ALG3MAC"),
    PBEwithHmacSHA("PBEwithHmacSHA"),
    PBEwithHmacSHA1("PBEwithHmacSHA1"),
    PBEwithHmacSHA224("PBEwithHmacSHA224"),
    PBEwithHmacSHA256("PBEwithHmacSHA256"),
    PBEwithHmacSHA384("PBEwithHmacSHA384"),
    PBEwithHmacSHA512("PBEwithHmacSHA512");

    override fun toString(): String = this.digestName
}