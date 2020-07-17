package com.github.eendroroy.kotp._base32

import org.apache.commons.codec.binary.Base32
import java.security.SecureRandom

/**
 * @author indrajit
 */
fun Base32.random(byteLength: Int = 20): String = SecureRandom().generateSeed(byteLength).encodeBase32()