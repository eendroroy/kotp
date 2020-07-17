package com.github.eendroroy.kotp

import com.github.eendroroy.kotp._base32.decodeBase32
import com.github.eendroroy.kotp._byteString.toByteArray
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.pow

/**
 * @author indrajit
 */
class Otp(
    private val secret: String,
    private val digits: Int = 6,
    private val digest: Digest = Digest.SHA1
) {
    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun generateOtp(input: Int): String {
        val hMac = Mac.getInstance(digest.toString()).let {
            it.init(SecretKeySpec(secret.decodeBase32().toByteArray(), digest.toString()))
            return@let it.doFinal(input.toByteArray())
        }
        val offset = hMac.last().toInt().and(0xf)
        val code = listOf(
            hMac[offset].toInt().and(0x7f).shl(24),
            hMac[offset + 1].toInt().and(0xff).shl(16),
            hMac[offset + 2].toInt().and(0xff).shl(8),
            hMac[offset + 3].toInt().and(0xff)
        ).reduce(Int::or) % 10.0.pow(digits.toDouble()).toInt()
        return String.format("%1$" + digits + "s", code.toString()).replace(' ', '0')
    }
}