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
    fun testGeneratedOtpAgainstSample(): Collection<DynamicTest?> {
        return listOf(
            listOf<Any>(59L, "94287082", Digest.SHA1, "12345678901234567890"),
            listOf<Any>(59L, "46119246", Digest.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(59L, "90693936", Digest.SHA512, "1234567890123456789012345678901234567890123456789012345678901234"),
            listOf<Any>(1111111109L, "07081804", Digest.SHA1, "12345678901234567890"),
            listOf<Any>(1111111109L, "68084774", Digest.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(1111111109L, "25091201", Digest.SHA512, "1234567890123456789012345678901234567890123456789012345678901234"),
            listOf<Any>(1111111111L, "14050471", Digest.SHA1, "12345678901234567890"),
            listOf<Any>(1111111111L, "67062674", Digest.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(1111111111L, "99943326", Digest.SHA512, "1234567890123456789012345678901234567890123456789012345678901234"),
            listOf<Any>(1234567890L, "89005924", Digest.SHA1, "12345678901234567890"),
            listOf<Any>(1234567890L, "91819424", Digest.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(1234567890L, "93441116", Digest.SHA512, "1234567890123456789012345678901234567890123456789012345678901234"),
            listOf<Any>(2000000000L, "69279037", Digest.SHA1, "12345678901234567890"),
            listOf<Any>(2000000000L, "90698825", Digest.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(2000000000L, "38618901", Digest.SHA512, "1234567890123456789012345678901234567890123456789012345678901234"),
            listOf<Any>(20000000000L, "65353130", Digest.SHA1, "12345678901234567890"),
            listOf<Any>(20000000000L, "77737706", Digest.SHA256, "12345678901234567890123456789012"),
            listOf<Any>(20000000000L, "47863826", Digest.SHA512, "1234567890123456789012345678901234567890123456789012345678901234"),
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

    @TestFactory
    fun testProvisioningUri(): Collection<DynamicTest?> {
        return listOf(
            listOf<Any>(
                "kotp",
                "secret",
                "kotp_lib",
                30,
                6,
                "otpauth://totp/kotp_lib:kotp?secret=ONSWG4TFOQ&period=30&issuer=kotp_lib&digits=6&algorithm=SHA1"
            ),
            listOf<Any>(
                "kotp",
                "secret",
                "kotp_lib",
                60,
                6,
                "otpauth://totp/kotp_lib:kotp?secret=ONSWG4TFOQ&period=60&issuer=kotp_lib&digits=6&algorithm=SHA1"
            ),
            listOf<Any>(
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
