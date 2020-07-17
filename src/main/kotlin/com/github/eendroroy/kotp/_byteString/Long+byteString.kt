package com.github.eendroroy.kotp._byteString

/**
 * @author indrajit
 */
fun Long.byteString() {
    if (this <= 0) throw IllegalArgumentException("#byteString requires a positive number")
    val result = mutableListOf<String>()
    var numTemp = this

    while (numTemp > 0) {
        result.add(numTemp.and(0xFF).toString())
        numTemp = numTemp.shr(8)
    }

    result.reverse()
    val resultStr = result.joinToString("")
    println(resultStr)
}