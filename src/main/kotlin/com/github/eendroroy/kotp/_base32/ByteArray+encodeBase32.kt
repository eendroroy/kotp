package com.github.eendroroy.kotp._base32

import org.apache.commons.codec.binary.Base32

/**
 * @author indrajit
 */
fun ByteArray.encodeBase32(): String = Base32().encodeAsString(this)