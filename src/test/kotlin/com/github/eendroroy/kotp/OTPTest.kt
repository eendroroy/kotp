package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.base32.Base32
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

/**
 * @author indrajit
 */
class OTPTest {
    @TestFactory
    fun testOtpGenerationUsingDigest(): Collection<DynamicTest?>? {
        return Digest.values().map { digest ->
            DynamicTest.dynamicTest("testOtpGenerationUsing => ${digest.name}") {
                val otp = OTP(secret = Base32.encode("secret"), digits = 6, digest = digest).generateOtp(123L)
                assertEquals(6, otp.length)
            }
        }
    }

    @TestFactory
    fun testGeneratedOtpLengthIsCorrect(): Collection<DynamicTest?>? {
        return listOf(1, 2, 3, 4, 6, 8, 10, 12, 24).map { len ->
            DynamicTest.dynamicTest("testGeneratedOtpLengthIsCorrect => $len") {
                val otp1 = OTP(secret = Base32.encode("secret"), digits = len).generateOtp(123L)
                assertEquals(len, otp1.length)
            }
        }
    }

    @TestFactory
    fun testGeneratedOtpAgainstSample(): Collection<DynamicTest?>? {
        return listOf(
            Pair(1L, "533881"), Pair(2L, "720111"), Pair(3L, "282621"), Pair(4L, "330810"),
            Pair(11L, "182025"), Pair(22L, "388206"), Pair(33L, "526975"), Pair(44L, "607928"),
            Pair(123L, "067135"), Pair(132L, "841825"), Pair(213L, "068586"), Pair(231L, "101814"),
            Pair(312L, "203944"), Pair(321L, "827117")
        ).map { pair ->
            DynamicTest.dynamicTest("testGeneratedOtpAgainstSample => generateOtp(${pair.first}): ${pair.second}") {
                val otp = OTP(secret = Base32.encode("secret")).generateOtp(pair.first)
                assertEquals(pair.second, otp)
            }
        }
    }
}
