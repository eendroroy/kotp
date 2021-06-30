package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.base32.Base32
import com.github.eendroroy.kotp.config.HOTPConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

/**
 * @author indrajit
 */
class HOTPTest {
    @TestFactory
    fun testGeneratedOtpAgainstSample(): Collection<DynamicTest?> {
        val hotp = HOTP(HOTPConfig("secret"))
        return listOf(
            Pair(1L, "533881"), Pair(2L, "720111"), Pair(3L, "282621"), Pair(4L, "330810"),
            Pair(11L, "182025"), Pair(22L, "388206"), Pair(33L, "526975"), Pair(44L, "607928")
        ).map { pair ->
            DynamicTest.dynamicTest("testGeneratedOtpAgainstSample => at(${pair.first}): ${pair.second}") {
                val otp = hotp.at(pair.first)
                assertEquals(pair.second, otp)
                assertEquals(pair.first, hotp.verify(pair.second, pair.first))
                assertEquals(pair.first, hotp.verify(pair.second, 0, retries = pair.first))
                assertNotEquals(pair.first, hotp.verify(pair.second, 0L))
            }
        }
    }

    @TestFactory
    fun testProvisioningUri(): Collection<DynamicTest?> {
        return listOf(
            listOf("kotp", "secret1", 1L, 6, "otpauth://hotp/kotp?secret=ONSWG4TFOQYQ&counter=1&digits=6"),
            listOf("kotp", "secret2", 5L, 6, "otpauth://hotp/kotp?secret=ONSWG4TFOQZA&counter=5&digits=6"),
            listOf("kotp", "secret3", 5L, 8, "otpauth://hotp/kotp?secret=ONSWG4TFOQZQ&counter=5&digits=8")
        ).map { params ->
            DynamicTest.dynamicTest("testProvisioningUri") {
                val uri = HOTP(HOTPConfig(params[1].toString(), digits = params[3] as Int))
                    .provisioningUri(params[0].toString(), params[2] as Long)
                assertEquals(params[4], uri)
            }
        }
    }

    @TestFactory
    fun testBackwardCompatibility(): Collection<DynamicTest?> {
        @Suppress("DEPRECATION")
        val hotpOld = HOTP(secret = Base32.encode("secret"))
        val hotpNew = HOTP(HOTPConfig("secret"))

        return listOf(1L, 2L, 3L, 4L, 1_111_111_111L, 1_234_567_890L, 2_000_000_000L, 2_111_333_222L).map {
            DynamicTest.dynamicTest("testBackwardCompatibility => counter: $it") {
                assertEquals(hotpNew.at(it), hotpOld.at(it))
                assertEquals(hotpNew.provisioningUri("kotp"), hotpOld.provisioningUri("kotp"))
            }
        }
    }
}
