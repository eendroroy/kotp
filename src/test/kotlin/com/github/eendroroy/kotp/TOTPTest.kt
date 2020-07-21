package com.github.eendroroy.kotp

import com.github.eendroroy.kotp.base32.Base32
import com.github.eendroroy.kotp.config.TOTPConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.Calendar

/**
 * @author indrajit
 */
class TOTPTest {
    @TestFactory
    fun testGeneratedOtpAgainstSample(): Collection<DynamicTest?>? {
        val totp = TOTP(TOTPConfig(Base32.encode("secret"), "kotp_lib"))
        return listOf(
            listOf(0, "814628", 30),
            listOf(1_111_111_111, "001123", 30),
            listOf(1_234_567_890, "442583", 30),
            listOf(2_000_000_000, "974517", 30),
            listOf(2_111_333_222, "255203", 30),
            listOf(0, "814628", 60),
            listOf(1_111_111_111, "001123", 60),
            listOf(1_234_567_890, "442583", 60),
            listOf(2_000_000_000, "974517", 60),
            listOf(2_111_333_222, "255203", 60),
            listOf(0, "814628", 120),
            listOf(1_111_111_111, "001123", 120),
            listOf(1_234_567_890, "442583", 120),
            listOf(2_000_000_000, "974517", 120),
            listOf(2_111_333_222, "255203", 120)
        ).map { (seconds, otpStr, interval) ->
            DynamicTest.dynamicTest(
                "testGeneratedOtpAgainstSample => at(${seconds as Int}): $otpStr [interval: ${interval as Int}]"
            ) {
                val time = Calendar.getInstance().apply { timeInMillis = seconds * 1_000L }.time
                val timeNext = Calendar.getInstance().apply { timeInMillis = (seconds + interval) * 1_000L }.time
                val otp = totp.at(time)
                val otpNext = totp.at(timeNext)

                assertEquals(otp, otpStr)
                assertNotEquals(otpNext, otpStr)

                assertEquals(seconds / interval, totp.verify(otp, at = time) as Int / interval)
                assertNotEquals(seconds / interval, totp.verify(otpNext, at = timeNext) as Int / interval)
            }
        }
    }

    @TestFactory
    fun testProvisioningUri(): Collection<DynamicTest?>? {
        return listOf(
            listOf(
                "kotp",
                "secret",
                "kotp_lib",
                30,
                6,
                "otpauth://totp/kotp_lib:kotp?secret=ONSWG4TFOQ&period=30&issuer=kotp_lib&digits=6&algorithm=SHA1"
            ),
            listOf(
                "kotp",
                "secret",
                "kotp_lib",
                60,
                6,
                "otpauth://totp/kotp_lib:kotp?secret=ONSWG4TFOQ&period=60&issuer=kotp_lib&digits=6&algorithm=SHA1"
            ),
            listOf(
                "kotp",
                "secret",
                "kotp_lib",
                120,
                8,
                "otpauth://totp/kotp_lib:kotp?secret=ONSWG4TFOQ&period=120&issuer=kotp_lib&digits=8&algorithm=SHA1"
            )
        ).map { params ->
            DynamicTest.dynamicTest("testProvisioningUri") {
                val uri = TOTP(
                    TOTPConfig(
                        Base32.encode(params[1].toString()),
                        params[2] as String,
                        digits = params[4] as Int,
                        interval = params[3] as Int
                    )
                ).provisioningUri(params[0].toString())
                assertEquals(params[5], uri)
            }
        }
    }

    @TestFactory
    fun testBackwardCompatibility(): Collection<DynamicTest?>? {
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
