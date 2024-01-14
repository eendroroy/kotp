package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.config.Secret
import com.github.eendroroy.kotp.exception.RadixValueOutOfRange
import com.github.eendroroy.kotp.extensions.toByteArray
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.pow

/**
 * @param secret    [Secret] secret string
 * @param length    [Int] length of the otp, default: 6
 * @param algorithm [Algorithm] algorithm to use, default: [Algorithm.SHA1]
 * @param radix     [Int] radix/base of the OTP value, default: 10 (decimal)
 *
 * @author indrajit
 */
open class OTP(
    private val secret: Secret,
    private val length: Int = 6,
    private val algorithm: Algorithm = Algorithm.SHA1,
    private val radix: Int = 10
) {
    init {
        RadixValueOutOfRange.passOrThrow(radix)
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
        val hMac = Mac.getInstance(algorithm.toString()).let {
            it.init(SecretKeySpec(secret.decoded(), algorithm.toString()))
            it.doFinal(input.toByteArray())
        }

        val offset = hMac.last().toInt().and(0xf)

        val code = listOf(
            hMac[offset].toInt().and(0x7f).shl(24),
            hMac[offset + 1].toInt().and(0xff).shl(16),
            hMac[offset + 2].toInt().and(0xff).shl(8),
            hMac[offset + 3].toInt().and(0xff)
        ).reduce(Int::or) % radix.toDouble().pow(length.toDouble()).toInt()

        return String.format("%1$" + length + "s", code.toString(radix)).uppercase().replace(' ', '0')
    }
}
