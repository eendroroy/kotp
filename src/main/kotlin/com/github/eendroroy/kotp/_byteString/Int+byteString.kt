package com.github.eendroroy.kotp._byteString

/**
 * @author indrajit
 */
fun Int.byteString(padding: Int = 8): String {
    if (this <= 0) throw IllegalArgumentException("#byteString requires a positive number")
    val result = mutableListOf<Char>()
    var numTemp = this

    while (numTemp > 0) {
        result.add(numTemp.and(0xFF).toChar())
        numTemp = numTemp.shr(8)
    }

    for (i in 1..(padding - result.size)){
        result.add(0.toChar())
    }

    result.reverse()
    return result.joinToString("")
}