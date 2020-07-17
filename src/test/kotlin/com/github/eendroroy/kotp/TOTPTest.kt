package com.github.eendroroy.kotp

import com.github.eendroroy.kotp._base32.encodeBase32
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.Calendar

/**
 * @author indrajit
 */
class TOTPTest {
    @TestFactory
    fun testGeneratedOtpAgainstSample(): Collection<DynamicTest?>? {
        val totp = TOTP(secret = "secret".encodeBase32(), issuer = "kotp_lib")
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
        ).map { item ->
            DynamicTest.dynamicTest("testGeneratedOtpAgainstSample => at(${item[0]}): ${item[1]} [interval: ${item[2]}]") {
                val time = Calendar.getInstance().apply { timeInMillis = item[0] as Int * 1_000L }.time
                val otp = totp.at(time)
                assertEquals(item[1], otp)
                assertTrue((item[0] as Int) + (item[2] as Int) > totp.verify(otp, at = time) ?: 0)
                assertTrue((item[0] as Int) - (item[2] as Int) < totp.verify(otp, at = time) ?: 0)
            }
        }
    }

    @TestFactory
    fun testProvisioningUri(): Collection<DynamicTest?>? {
        return listOf(
//          listOf("0-name", "1-secret", "2-issuer", 3-interval, 4-digits, "5-uri")
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
                    secret = params[1].toString().encodeBase32(),
                    issuer = params[2] as String,
                    interval = params[3] as Int,
                    digits = params[4] as Int
                ).provisioningUri(params[0].toString())
                assertEquals(params[5], uri)
            }
        }
    }
}