package com.github.eendroroy.kotp

import com.github.eendroroy.kotp._ext.toByteArray
import com.github.eendroroy.kotp.base32.Base32String
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.pow

/**
 * @param secret [Base32String] secret string encoded by [com.github.eendroroy.kotp.base32.Base32]
 * @param digits [Int] length of the otp, default: 6
 * @param digest [Digest] algorithm to use, default: [Digest.SHA1]
 *
 * @author indrajit
 */
open class OTP(
    private val secret: Base32String,
    private val digits: Int = 6,
    private val digest: Digest = Digest.SHA1
) {
    /**
     * Generates OTP from input
     *
     * @param input either a counter or unix timestamp
     *
     * @return generated OTP
     *
     * @since 0.1.1
     */
    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun generateOtp(input: Int): String {
        val hMac = Mac.getInstance(digest.toString()).let {
            it.init(SecretKeySpec(secret.decode(), digest.toString()))
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
