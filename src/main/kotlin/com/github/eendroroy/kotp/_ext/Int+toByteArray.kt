package com.github.eendroroy.kotp._ext

import java.util.LinkedList

/**
 * @author indrajit
 */
internal fun Int.toByteArray(padding: Int = 8): ByteArray {
    if (this < 0) throw IllegalArgumentException("#toByteArray requires a positive number")
    var numTemp = this
    return LinkedList<Byte>().apply {
        while (numTemp > 0) {
            add(numTemp.and(0xFF).toByte())
            numTemp = numTemp.shr(8)
        }
    }.apply {
        for (i in 1..(padding - size)) add(0.toByte())
    }.apply {
        reverse()
    }.toByteArray()
}
