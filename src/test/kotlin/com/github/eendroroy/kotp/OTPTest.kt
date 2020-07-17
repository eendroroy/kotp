package com.github.eendroroy.kotp

import com.github.eendroroy.kotp._base32.encodeBase32
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
                val otp = OTP(secret = "secret".encodeBase32(), digits = 6, digest = digest).generateOtp(123)
                assertEquals(6, otp.length)
            }
        }
    }

    @TestFactory
    fun testGeneratedOtpLengthIsCorrect(): Collection<DynamicTest?>? {
        return listOf(1, 2, 3, 4, 6, 8, 10, 12, 24).map { len ->
            DynamicTest.dynamicTest("testGeneratedOtpLengthIsCorrect => $len") {
                val otp1 = OTP(secret = "secret".encodeBase32(), digits = len).generateOtp(123)
                assertEquals(len, otp1.length)
            }
        }
    }

    @TestFactory
    fun testGeneratedOtpAgainstSample(): Collection<DynamicTest?>? {
        return listOf(
            Pair(1, "533881"), Pair(2, "720111"), Pair(3, "282621"), Pair(4, "330810"),
            Pair(11, "182025"), Pair(22, "388206"), Pair(33, "526975"), Pair(44, "607928"),
            Pair(123, "067135"), Pair(132, "841825"), Pair(213, "068586"), Pair(231, "101814"),
            Pair(312, "203944"), Pair(321, "827117")
        ).map { pair ->
            DynamicTest.dynamicTest("testGeneratedOtpAgainstSample => generateOtp(${pair.first}): ${pair.second}") {
                val otp = OTP(secret = "secret".encodeBase32()).generateOtp(pair.first)
                assertEquals(pair.second, otp)
            }
        }
    }
}