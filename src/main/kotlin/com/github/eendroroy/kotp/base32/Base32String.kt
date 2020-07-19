package com.github.eendroroy.kotp.base32

import org.apache.commons.codec.binary.Base32

/**
 * @author indrajit
 */
class Base32String private constructor(private val str: String) {
    fun decode(): ByteArray = Base32().decode(str.toByteArray())
    fun decodeAsString(): String = String(decode())
    fun raw(): String = str
    fun rawBytes(): ByteArray = str.toByteArray()

    internal class Builder(private val str: String) {
        fun build(): Base32String = Base32String(str)
    }
}