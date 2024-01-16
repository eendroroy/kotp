package io.github.eendroroy.kotp

import io.github.eendroroy.kotp.config.Secret
import io.github.eendroroy.kotp.config.TOTPConfig
import io.github.eendroroy.kotp.exception.*
import io.github.eendroroy.kotp.exception.RadixValueOutOfRange.Companion.RADIX_VALUE_RANGE
import io.github.eendroroy.kotp.exception.UnsupportedAlgorithmForProvisioningUri.Companion.PROV_ALGORITHM_VALUE
import io.github.eendroroy.kotp.exception.UnsupportedOtpLengthForProvisioningUri.Companion.PROV_LENGTH_VALUE
import io.github.eendroroy.kotp.exception.UnsupportedIntervalForProvisioningUri.Companion.PROV_INTERVAL_VALUE
import io.github.eendroroy.kotp.exception.UnsupportedRadixForProvisioningUri.Companion.PROV_RADIX_VALUE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import java.util.*

/**
 * @author indrajit
 */
class TOTPTest {
    @Test
    fun testConfig() {
        val config1 = TOTPConfig(Secret("secret"), "")
        val config2 = TOTPConfig("secret", "")

        assertEquals(config1.secret.decodedString(), config2.secret.decodedString())
        assertEquals(config1.secret.encodedString(), config2.secret.encodedString())
    }

    @TestFactory
    fun testOtpGeneration(): Collection<DynamicTest?> {
        return listOf(
            listOf<Any>("SECRET", Algorithm.SHA1, 6, 30, 10),
            listOf<Any>("SECRET", Algorithm.SHA256, 8, 30, 10),
            listOf<Any>("SECRET", Algorithm.SHA512, 12, 60, 16),
            listOf<Any>("SECRET", Algorithm.SHA512, 6, 60, 36),
        ).map { (secret, algorithm, digits, interval, radix) ->
            DynamicTest.dynamicTest(
                "testOtpGeneration: " +
                    "[Algorithm: ${algorithm as Algorithm}], " +
                    "[Digits: ${digits as Int}], " +
                    "[Interval: ${interval as Int}], " +
                    "[radix: ${radix as Int}]"
            ) {
                val totp = TOTP(TOTPConfig(secret as String, "KOTP", digits, interval, algorithm, radix))
                val totpNow = totp.now()
                assertNotNull(totpNow)
                assertNotNull(totp.verify(totpNow, driftBehind = 5))

                val at = Calendar.getInstance().time.time / 1_000
                val totpAt = totp.at(at)
                assertNotNull(totpAt)
                assertNotNull(totp.verify(totpAt))
                assertNotNull(totp.verify(totpAt, after = at + 1, driftBehind = 1))
                assertNull(totp.verify(totpAt, after = at + interval + 10))
            }
        }
    }

    @Test
    fun testPassProvisioningUri() {
        val uri1 = TOTP(TOTPConfig("secret", "kotp_lib")).provisioningUri("kotp")
        assertEquals("otpauth://totp/kotp_lib:kotp?secret=ONSWG4TFOQ&issuer=kotp_lib", uri1)
        val uri2 = TOTP(TOTPConfig("secret", "")).provisioningUri("kotp")
        assertEquals("otpauth://totp/kotp?secret=ONSWG4TFOQ&issuer=", uri2)
    }

    @Test
    fun testFailWithRadixValueOutOfRange() {
        val exception = assertThrows(RadixValueOutOfRange::class.java) {
            TOTP(TOTPConfig("secret", "kotp_lib", interval = 60, radix = 2))
        }

        assertTrue(exception.message == "radix was not in valid range {$RADIX_VALUE_RANGE}")
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
        val totp = TOTP(TOTPConfig("secret", "kotp_lib", length = 8))

        val exception = assertThrows(UnsupportedOtpLengthForProvisioningUri::class.java) {
            totp.provisioningUri("kotp")
        }

        assertTrue(exception.message == "supports only {$PROV_LENGTH_VALUE} digits")
    }

    @Test
    fun testFailWithUnsupportedAlgorithmForProvisioningUri() {
        val totp = TOTP(TOTPConfig("secret", "kotp_lib", algorithm = Algorithm.SHA512))

        val exception = assertThrows(UnsupportedAlgorithmForProvisioningUri::class.java) {
            totp.provisioningUri("kotp")
        }

        assertTrue(exception.message == "supports only {${PROV_ALGORITHM_VALUE}}")
    }

    @Test
    fun testFailWithUnsupportedRadixForProvisioningUri() {
        val totp = TOTP(TOTPConfig("secret", "kotp_lib", radix = 16))

        val exception = assertThrows(UnsupportedRadixForProvisioningUri::class.java) {
            totp.provisioningUri("kotp")
        }

        assertTrue(exception.message == "supports only {$PROV_RADIX_VALUE} radix")
    }

    @TestFactory
    fun testGeneratedOtpAgainstRFCSample(): Collection<DynamicTest?> {
        return listOf(
            listOf<Any>(59L, "94287082", Algorithm.SHA1, "12345678901234567890"),
            listOf<Any>(59L, "46119246", Algorithm.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(
                59L,
                "90693936",
                Algorithm.SHA512,
                "1234567890123456789012345678901234567890123456789012345678901234"
            ),
            listOf<Any>(1111111109L, "07081804", Algorithm.SHA1, "12345678901234567890"),
            listOf<Any>(1111111109L, "68084774", Algorithm.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(
                1111111109L,
                "25091201",
                Algorithm.SHA512,
                "1234567890123456789012345678901234567890123456789012345678901234"
            ),
            listOf<Any>(1111111111L, "14050471", Algorithm.SHA1, "12345678901234567890"),
            listOf<Any>(1111111111L, "67062674", Algorithm.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(
                1111111111L,
                "99943326",
                Algorithm.SHA512,
                "1234567890123456789012345678901234567890123456789012345678901234"
            ),
            listOf<Any>(1234567890L, "89005924", Algorithm.SHA1, "12345678901234567890"),
            listOf<Any>(1234567890L, "91819424", Algorithm.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(
                1234567890L,
                "93441116",
                Algorithm.SHA512,
                "1234567890123456789012345678901234567890123456789012345678901234"
            ),
            listOf<Any>(2000000000L, "69279037", Algorithm.SHA1, "12345678901234567890"),
            listOf<Any>(2000000000L, "90698825", Algorithm.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(
                2000000000L,
                "38618901",
                Algorithm.SHA512,
                "1234567890123456789012345678901234567890123456789012345678901234"
            ),
            listOf<Any>(20000000000L, "65353130", Algorithm.SHA1, "12345678901234567890"),
            listOf<Any>(20000000000L, "77737706", Algorithm.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(
                20000000000L,
                "47863826",
                Algorithm.SHA512,
                "1234567890123456789012345678901234567890123456789012345678901234"
            ),
        ).map { (seconds, otpStr, algorithm, seed) ->
            DynamicTest.dynamicTest(
                "testGeneratedOtpAgainstSample => at(${seconds as Long}): $otpStr [Algorithm: ${algorithm as Algorithm}]"
            ) {
                val interval = 30
                val totp = TOTP(TOTPConfig(seed as String, "kotp_lib", 8, interval, algorithm))
                val timeNext = seconds + interval
                val otp = totp.at(seconds)
                val otpNext = totp.at(timeNext)

                assertEquals(otp, otpStr)
                assertNotEquals(otpNext, otpStr)

                assertEquals(seconds / interval, totp.verify(otp, at = seconds) as Long / interval)
                assertNotEquals(seconds / interval, totp.verify(otpNext, at = timeNext) as Long / interval)
                assertNull(totp.verify(otpNext, at = seconds + 1_000))
            }
        }
    }
}
