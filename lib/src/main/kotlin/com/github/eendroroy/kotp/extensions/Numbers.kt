package com.github.eendroroy.kotp.extensions

/**
 * @author indrajit
 */
internal fun Long.toByteArray(padding: Int = 8): ByteArray {
    if (this < 0) throw IllegalArgumentException("#toByteArray requires a positive number")

    var numTemp = this
    val bytes = mutableListOf<Byte>().apply {
        while (numTemp > 0) {
            add(numTemp.and(0xFF).toByte())
            numTemp = numTemp.shr(8)
        }
    }

    (1..(padding - bytes.size)).forEach { _ ->
        bytes.add(0.toByte())
    }

    return bytes.reversed().toByteArray()
}
