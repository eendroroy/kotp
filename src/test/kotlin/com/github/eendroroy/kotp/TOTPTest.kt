package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.base32.Base32
import com.github.eendroroy.kotp.config.TOTPConfig
import com.github.eendroroy.kotp.exception.UnsupportedDigestForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedDigestForProvisioningUri.Companion.PROV_DIGEST_VALUE
import com.github.eendroroy.kotp.exception.UnsupportedDigitsForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedDigitsForProvisioningUri.Companion.PROV_DIGIT_VALUE
import com.github.eendroroy.kotp.exception.UnsupportedIntervalForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedIntervalForProvisioningUri.Companion.PROV_INTERVAL_VALUE
import com.github.eendroroy.kotp.exception.UnsupportedRadixForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedRadixForProvisioningUri.Companion.PROV_RADIX_VALUE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import java.util.Calendar

/**
 * @author indrajit
 */
class TOTPTest {
    @TestFactory
    fun testGeneratedOtpAgainstSample(): Collection<DynamicTest?> {
        return listOf(
            listOf<Any>(59L, "94287082", Digest.SHA1, "12345678901234567890"),
            listOf<Any>(59L, "46119246", Digest.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(
                59L,
                "90693936",
                Digest.SHA512,
                "1234567890123456789012345678901234567890123456789012345678901234"
            ),
            listOf<Any>(1111111109L, "07081804", Digest.SHA1, "12345678901234567890"),
            listOf<Any>(1111111109L, "68084774", Digest.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(
                1111111109L,
                "25091201",
                Digest.SHA512,
                "1234567890123456789012345678901234567890123456789012345678901234"
            ),
            listOf<Any>(1111111111L, "14050471", Digest.SHA1, "12345678901234567890"),
            listOf<Any>(1111111111L, "67062674", Digest.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(
                1111111111L,
                "99943326",
                Digest.SHA512,
                "1234567890123456789012345678901234567890123456789012345678901234"
            ),
            listOf<Any>(1234567890L, "89005924", Digest.SHA1, "12345678901234567890"),
            listOf<Any>(1234567890L, "91819424", Digest.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(
                1234567890L,
                "93441116",
                Digest.SHA512,
                "1234567890123456789012345678901234567890123456789012345678901234"
            ),
            listOf<Any>(2000000000L, "69279037", Digest.SHA1, "12345678901234567890"),
            listOf<Any>(2000000000L, "90698825", Digest.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(
                2000000000L,
                "38618901",
                Digest.SHA512,
                "1234567890123456789012345678901234567890123456789012345678901234"
            ),
            listOf<Any>(20000000000L, "65353130", Digest.SHA1, "12345678901234567890"),
            listOf<Any>(20000000000L, "77737706", Digest.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(
                20000000000L,
                "47863826",
                Digest.SHA512,
                "1234567890123456789012345678901234567890123456789012345678901234"
            ),
        ).map { (seconds, otpStr, digest, seed) ->
            DynamicTest.dynamicTest(
                "testGeneratedOtpAgainstSample => at(${seconds as Long}): $otpStr [Digest: ${digest as Digest}]"
            ) {
                val interval = 30
                val totp = TOTP(TOTPConfig(seed as String, "kotp_lib", 8, interval, digest))
                val time = Calendar.getInstance().apply { timeInMillis = seconds * 1_000L }.time
                val timeNext = Calendar.getInstance().apply { timeInMillis = (seconds + interval) * 1_000L }.time
                val otp = totp.at(time)
                val otpNext = totp.at(timeNext)

                assertEquals(otp, otpStr)
                assertNotEquals(otpNext, otpStr)

                assertEquals(
                    seconds / interval,
                    totp.verify(otp, at = time) as Long / interval
                )
                assertNotEquals(
                    seconds / interval,
                    totp.verify(otpNext, at = timeNext) as Long / interval
                )
            }
        }
    }

    @Test
    fun testPassProvisioningUri() {
        val uri = TOTP(TOTPConfig("secret", "kotp_lib", digits = 6, interval = 30)).provisioningUri("kotp")
        assertEquals("otpauth://totp/kotp_lib:kotp?secret=ONSWG4TFOQ&issuer=kotp_lib", uri)
    }

    @Test
    fun testFailWithUnsupportedIntervalForProvisioningUri() {
        val totp = TOTP(TOTPConfig("secret", "kotp_lib", interval = 60))

        val exception = assertThrows(UnsupportedIntervalForProvisioningUri::class.java) {
            totp.provisioningUri("kotp")
        }

        assertTrue(exception.message == "supports only {$PROV_INTERVAL_VALUE} second interval")
    }

    @Test
    fun testFailWithUnsupportedDigitsForProvisioningUri() {
        val totp = TOTP(TOTPConfig("secret", "kotp_lib", digits = 8))

        val exception = assertThrows(UnsupportedDigitsForProvisioningUri::class.java) {
            totp.provisioningUri("kotp")
        }

        assertTrue(exception.message == "supports only {${PROV_DIGIT_VALUE}} digits")
    }

    @Test
    fun testFailWithUnsupportedDigestForProvisioningUri() {
        val totp = TOTP(TOTPConfig("secret", "kotp_lib", digest = Digest.SHA512))

        val exception = assertThrows(UnsupportedDigestForProvisioningUri::class.java) {
            totp.provisioningUri("kotp")
        }

        assertTrue(exception.message == "supports only {$PROV_DIGEST_VALUE}")
    }

    @Test
    fun testFailWithUnsupportedRadixForProvisioningUri() {
        val totp = TOTP(TOTPConfig("secret", "kotp_lib", radix = 16))

        val exception = assertThrows(UnsupportedRadixForProvisioningUri::class.java) {
            totp.provisioningUri("kotp")
        }

        assertTrue(exception.message == "supports only {${PROV_RADIX_VALUE}} radix")
    }

    @TestFactory
    fun testBackwardCompatibility(): Collection<DynamicTest?> {
        @Suppress("DEPRECATION")
        val totpOld = TOTP(Base32.encode("secret"), issuer = "kotp-lib")
        val totpNew = TOTP(TOTPConfig(Base32.encode("secret"), "kotp-lib"))

        return listOf(1, 2, 3, 4, 1_111_111_111, 1_234_567_890, 2_000_000_000, 2_111_333_222).map {
            DynamicTest.dynamicTest("testBackwardCompatibility => time: $it") {
                val time = Calendar.getInstance().apply { timeInMillis = it * 1_000L }.time
                assertEquals(totpNew.at(time), totpOld.at(time))
                assertEquals(totpNew.provisioningUri("kotp"), totpOld.provisioningUri("kotp"))
            }
        }
    }
}
