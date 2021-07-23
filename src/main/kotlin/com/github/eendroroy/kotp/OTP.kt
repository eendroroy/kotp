package com.github.eendroroy.kotp

import com.github.eendroroy.kotp._ext.toByteArray
import com.github.eendroroy.kotp.base32.Base32String
import com.github.eendroroy.kotp.exception.InvalidBaseValue
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.pow

/**
 * @param secret [Base32String] secret string encoded by [com.github.eendroroy.kotp.base32.Base32]
 * @param digits [Int] length of the otp, default: 6
 * @param digest [Digest] algorithm to use, default: [Digest.SHA1]
 * @param base   [Int] base of the OTP value, default: 10 (decimal)
 *
 * @author indrajit
 */
open class OTP(
    private val secret: Base32String,
    private val digits: Int = 6,
    private val digest: Digest = Digest.SHA1,
    private val base: Int = 10
) {
    init {
        InvalidBaseValue.passOrThrow(base)
    }

    /**
     * Generates OTP from input
     * Deprecated
     *
     * @param input either a counter or unix timestamp
     *
     * @return generated OTP
     *
     * @since 0.1.1
     */
    @Deprecated(
        message = "Deprecated since version: 0.1.3",
        replaceWith = ReplaceWith("generateOtp(input.toLong())")
    )
    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun generateOtp(input: Int): String {
        return generateOtp(input.toLong())
    }

    /**
     * Generates OTP from input
     *
     * @param input either a counter or unix timestamp
     *
     * @return generated OTP
     *
     * @since 0.1.3
     */
    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun generateOtp(input: Long): String {
        val hMac = Mac.getInstance(digest.toString()).let {
            it.init(SecretKeySpec(secret.decode(), digest.toString()))
            it.doFinal(input.toByteArray())
        }

        val offset = hMac.last().toInt().and(0xf)

        val code = listOf(
            hMac[offset].toInt().and(0x7f).shl(24),
            hMac[offset + 1].toInt().and(0xff).shl(16),
            hMac[offset + 2].toInt().and(0xff).shl(8),
            hMac[offset + 3].toInt().and(0xff)
        ).reduce(Int::or) % base.toDouble().pow(digits.toDouble()).toInt()

        return String.format("%1$" + digits + "s", code.toString(base)).uppercase().replace(' ', '0')
    }
}
