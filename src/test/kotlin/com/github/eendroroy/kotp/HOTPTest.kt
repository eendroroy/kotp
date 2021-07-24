package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.config.HOTPConfig
import com.github.eendroroy.kotp.exception.UnsupportedDigestForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedDigitsForProvisioningUri
import com.github.eendroroy.kotp.exception.UnsupportedRadixForProvisioningUri
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

/**
 * @author indrajit
 */
class HOTPTest {
    @TestFactory
    fun testGeneratedOtpAgainstSample(): Collection<DynamicTest?> {
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

    @Test
    fun testPassProvisioningUri() {
        val uri = HOTP(HOTPConfig("secret", digits = 6, digest = Digest.SHA1, radix = 10)).provisioningUri("kotp")
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
        val hotp = HOTP(HOTPConfig("secret", digest = Digest.SHA512))

        val exception = Assertions.assertThrows(UnsupportedDigestForProvisioningUri::class.java) {
            hotp.provisioningUri("kotp")
        }

        Assertions.assertTrue(exception.message == "supports only {${UnsupportedDigestForProvisioningUri.PROV_DIGEST_VALUE}}")
    }

    @Test
    fun testFailWithUnsupportedRadixForProvisioningUri() {
        val hotp = HOTP(HOTPConfig("secret", radix = 16))

        val exception = Assertions.assertThrows(UnsupportedRadixForProvisioningUri::class.java) {
            hotp.provisioningUri("kotp")
        }

        Assertions.assertTrue(exception.message == "supports only {${UnsupportedRadixForProvisioningUri.PROV_RADIX_VALUE}} radix")
    }
}
