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
class TOTPTestV0001003 {
    @TestFactory
    fun testGeneratedOtpAgainstSample(): Collection<DynamicTest?>? {
        val totp = TOTP(TOTPConfig(Base32.encode("secret"), "kotp_lib"))
        return listOf(
            listOf(0L, "814628", 30),
            listOf(1_111_111_111L, "001123", 30),
            listOf(1_234_567_890L, "442583", 30),
            listOf(2_000_000_000L, "974517", 30),
            listOf(2_111_333_222L, "255203", 30),
            listOf(0L, "814628", 60),
            listOf(1_111_111_111L, "001123", 60),
            listOf(1_234_567_890L, "442583", 60),
            listOf(2_000_000_000L, "974517", 60),
            listOf(2_111_333_222L, "255203", 60),
            listOf(0L, "814628", 120),
            listOf(1_111_111_111L, "001123", 120),
            listOf(1_234_567_890L, "442583", 120),
            listOf(2_000_000_000L, "974517", 120),
            listOf(2_111_333_222L, "255203", 120)
        ).map { (seconds, otpStr, interval) ->
            DynamicTest.dynamicTest(
                "testGeneratedOtpAgainstSample => at(${seconds as Long}): $otpStr [interval: ${interval as Int}]"
            ) {
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
}
