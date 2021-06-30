package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.base32.Base32
import com.github.eendroroy.kotp.config.HOTPConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
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

    @TestFactory
    fun testProvisioningUri(): Collection<DynamicTest?> {
        return listOf(
            listOf<Any>("kotp", "secret1", 1L, 6, "otpauth://hotp/kotp?secret=ONSWG4TFOQYQ&counter=1&digits=6"),
            listOf<Any>("kotp", "secret2", 5L, 6, "otpauth://hotp/kotp?secret=ONSWG4TFOQZA&counter=5&digits=6"),
            listOf<Any>("kotp", "secret3", 5L, 8, "otpauth://hotp/kotp?secret=ONSWG4TFOQZQ&counter=5&digits=8")
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
