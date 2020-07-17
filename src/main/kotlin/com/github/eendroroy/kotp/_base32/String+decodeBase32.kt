package com.github.eendroroy.kotp._base32

import org.apache.commons.codec.binary.Base32

/**
 * @author indrajit
 */
fun String.decodeBase32(): String = String(Base32().decode(this.toByteArray()))