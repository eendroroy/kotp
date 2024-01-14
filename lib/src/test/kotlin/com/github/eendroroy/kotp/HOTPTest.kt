package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.config.HOTPConfig
import com.github.eendroroy.kotp.config.Secret
import com.github.eendroroy.kotp.exception.UnsupportedDigestForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedDigitsForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedRadixForProvisioningUri
import com.github.eendroroy.kotp.extensions.currentSeconds
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

/**
 * @author indrajit
 */
class HOTPTest {
    @Test
    fun testConfig() {
        val config1 = HOTPConfig(Secret("secret"))
        val config2 = HOTPConfig("secret")

        assertEquals(config1.secret.decodedString(), config2.secret.decodedString())
        assertEquals(config1.secret.encodedString(), config2.secret.encodedString())
    }

    @Test
    fun testOtpAtZeroCounter() {
        val hotp = HOTP(HOTPConfig("SECRET"))
        val hotpAt = hotp.at(0)

        assertNotNull(hotpAt)
        assertNotNull(hotp.verify(hotpAt, counter = 0))
        assertNull(hotp.verify(hotpAt, counter = 1))
    }

    @TestFactory
    fun testOtpGeneration(): Collection<DynamicTest?> {
        return listOf(
            listOf<Any>("SECRET", Algorithm.SHA1, 6, 30, 10),
            listOf<Any>("SECRET", Algorithm.SHA256, 8, 30, 10),
            listOf<Any>("SECRET", Algorithm.SHA512, 12, 60, 16),
            listOf<Any>("SECRET", Algorithm.SHA512, 6, 60, 36),
        ).map { (secret, digest, digits, interval, radix) ->
            DynamicTest.dynamicTest(
                "testOtpGeneration: " +
                    "[Digest: ${digest as Algorithm}], " +
                    "[Digits: ${digits as Int}], " +
                    "[Interval: ${interval as Int}], " +
                    "[radix: ${radix as Int}]"
            ) {
                val hotp = HOTP(HOTPConfig(secret as String, digits, digest, radix))
                val counter = currentSeconds()
                val hotpAt = hotp.at(counter)

                assertNotNull(hotpAt)
                assertNotNull(hotp.verify(hotpAt, counter = counter))
                assertNotNull(hotp.verify(hotpAt, counter = counter - 5, retries = 5))
                assertNull(hotp.verify(hotpAt, counter = counter + 1))
            }
        }
    }

    @Test
    fun testPassProvisioningUri() {
        val uri = HOTP(HOTPConfig("secret", digits = 6, algorithm = Algorithm.SHA1, radix = 10)).provisioningUri("kotp")
        assertEquals("otpauth://hotp/kotp?secret=ONSWG4TFOQ&counter=0", uri)
    }

    @Test
    fun testFailWithUnsupportedDigitsForProvisioningUri() {
        val hotp = HOTP(HOTPConfig("secret", digits = 8))

        val exception = Assertions.assertThrows(UnsupportedDigitsForProvisioningUri::class.java) {
            hotp.provisioningUri("kotp")
        }

        Assertions.assertTrue(exception.message == "supports only {${UnsupportedDigitsForProvisioningUri.PROV_DIGIT_VALUE}} digits")
    }

    @Test
    fun testFailWithUnsupportedDigestForProvisioningUri() {
        val hotp = HOTP(HOTPConfig("secret", algorithm = Algorithm.SHA512))

        val exception = Assertions.assertThrows(UnsupportedDigestForProvisioningUri::class.java) {
            hotp.provisioningUri("kotp")
        }

        Assertions.assertTrue(exception.message == "supports only {${UnsupportedDigestForProvisioningUri.PROV_DIGEST_VALUEDigest}}")
    }

    @Test
    fun testFailWithUnsupportedRadixForProvisioningUri() {
        val hotp = HOTP(HOTPConfig("secret", radix = 16))

        val exception = Assertions.assertThrows(UnsupportedRadixForProvisioningUri::class.java) {
            hotp.provisioningUri("kotp")
        }

        Assertions.assertTrue(exception.message == "supports only {${UnsupportedRadixForProvisioningUri.PROV_RADIX_VALUE}} radix")
    }

    @TestFactory
    fun testGeneratedOtpAgainstRFCSample(): Collection<DynamicTest?> {
        val hotp = HOTP(HOTPConfig("12345678901234567890"))
        return listOf(
            Pair(0L, "755224"),
            Pair(1L, "287082"),
            Pair(2L, "359152"),
            Pair(3L, "969429"),
            Pair(4L, "338314"),
            Pair(5L, "254676"),
            Pair(6L, "287922"),
            Pair(7L, "162583"),
            Pair(8L, "399871"),
            Pair(9L, "520489")
        ).map { pair ->
            DynamicTest.dynamicTest("testGeneratedOtpAgainstSample => at(${pair.first}): ${pair.second}") {
                val otp = hotp.at(pair.first)
                assertEquals(pair.second, otp)
                assertEquals(pair.first, hotp.verify(pair.second, pair.first))
                assertEquals(pair.first, hotp.verify(pair.second, 0, retries = pair.first))
            }
        }
    }
}
