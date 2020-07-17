package com.github.eendroroy.kotp

import com.github.eendroroy.kotp._base32.encodeBase32
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

/**
 * @author indrajit
 */
class HOTPTest {
    @TestFactory
    fun testGeneratedOtpAgainstSample(): Collection<DynamicTest?>? {
        val hotp = HOTP(secret = "secret".encodeBase32())
        return listOf(
            Pair(1, "533881"), Pair(2, "720111"), Pair(3, "282621"), Pair(4, "330810"),
            Pair(11, "182025"), Pair(22, "388206"), Pair(33, "526975"), Pair(44, "607928")
        ).map { pair ->
            DynamicTest.dynamicTest("testGeneratedOtpAgainstSample => at(${pair.first}): ${pair.second}") {
                val otp = hotp.at(pair.first)
                assertEquals(pair.second, otp)
                assertEquals(pair.first, hotp.verify(pair.second, pair.first))
                assertEquals(pair.first, hotp.verify(pair.second, 0, retries = pair.first))
                assertNotEquals(pair.first, hotp.verify(pair.second, 0))
            }
        }
    }

    @TestFactory
    fun testProvisioningUri(): Collection<DynamicTest?>? {
        return listOf(
            listOf("kotp", "secret1", 1, 6, "otpauth://hotp/kotp?secret=ONSWG4TFOQYQ&counter=1&digits=6"),
            listOf("kotp", "secret2", 5, 6, "otpauth://hotp/kotp?secret=ONSWG4TFOQZA&counter=5&digits=6"),
            listOf("kotp", "secret3", 5, 8, "otpauth://hotp/kotp?secret=ONSWG4TFOQZQ&counter=5&digits=8")
        ).map { params ->
            DynamicTest.dynamicTest("testProvisioningUri") {
                val uri = HOTP(secret = params[1].toString().encodeBase32(), digits = params[3] as Int)
                    .provisioningUri(params[0].toString(), params[2] as Int)
                assertEquals(params[4], uri)
            }
        }
    }
}