package io.github.eendroroy.kotp

import io.github.eendroroy.kotp.config.Secret
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

/**
 * @author indrajit
 */
class OTPTest {
    @TestFactory
    fun testOtpGenerationUsingDigest(): Collection<DynamicTest?> {
        return Algorithm.values().map { algorithm ->
            DynamicTest.dynamicTest("testOtpGenerationUsing => ${algorithm.name}") {
                val otp = OTP(secret = Secret("secret"), algorithm = algorithm).generateOtp(123L)
                assertEquals(6, otp.length)
            }
        }
    }

    @Test
    fun testFailWithIllegalArgumentException() {
        val otp = OTP(secret = Secret("secret"))
        val exception = assertThrows<IllegalArgumentException> { otp.generateOtp(-1) }

        assertEquals("#toByteArray requires a positive number", exception.localizedMessage)
    }

    @TestFactory
    fun testGeneratedOtpLengthIsCorrect(): Collection<DynamicTest?> {
        return listOf(1, 2, 3, 4, 6, 8, 10, 12, 24).map { length ->
            DynamicTest.dynamicTest("testGeneratedOtpLengthIsCorrect => $length") {
                val otp1 = OTP(secret = Secret("secret"), length = length).generateOtp(123L)
                assertEquals(length, otp1.length)
                val otp2 = OTP(secret = Secret("secret"), length = length, radix = 16).generateOtp(123L)
                assertEquals(length, otp2.length)
                val otp3 = OTP(secret = Secret("secret"), length = length, radix = 36).generateOtp(123L)
                assertEquals(length, otp3.length)
            }
        }
    }

    @TestFactory
    fun testGeneratedOtpAgainstSample(): Collection<DynamicTest?> {
        return listOf(
            listOf<Any>(123L, "99067135", 10),
            listOf<Any>(9999L, "13747295", 10),
            listOf<Any>(123456789L, "98052198", 10),
            listOf<Any>(123L, "0BDD85FF", 16),
            listOf<Any>(9999L, "3C6C8E5F", 16),
            listOf<Any>(123456789L, "6B2C1966", 16),
            listOf<Any>(123L, "003AIP6N", 36),
            listOf<Any>(9999L, "00GRK4F3", 36),
            listOf<Any>(123456789L, "00TQIHYE", 36),
        ).map { item ->
            DynamicTest.dynamicTest(
                "testGeneratedOtpAgainstSample => generateOtp(${item[0] as Long}): ${item[1] as String}"
            ) {
                val otp = OTP(secret = Secret("secret"), length = 8, radix = item[2] as Int)
                    .generateOtp(item[0] as Long)
                assertEquals(item[1] as String, otp)
            }
        }
    }
}
