package com.github.eendroroy.kotp.sample

import com.github.eendroroy.kotp.Algorithm
import com.github.eendroroy.kotp.HOTP
import com.github.eendroroy.kotp.TOTP
import com.github.eendroroy.kotp.config.HOTPConfig
import com.github.eendroroy.kotp.config.TOTPConfig
import java.lang.RuntimeException
import java.util.Calendar

/**
 * @author indrajit
 */
const val secret = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

fun main() {
    hotpDemo(secret, 6, Algorithm.SHA1, 10)
    hotpDemo(secret, 8, Algorithm.SHA1, 10)
    hotpDemo(secret, 8, Algorithm.SHA256, 10)
    hotpDemo(secret, 8, Algorithm.SHA512, 10)
    hotpDemo(secret, 8, Algorithm.SHA1, 16)
    hotpDemo(secret, 8, Algorithm.SHA256, 16)
    hotpDemo(secret, 8, Algorithm.SHA512, 16)
    hotpDemo(secret, 8, Algorithm.SHA1, 36)
    hotpDemo(secret, 8, Algorithm.SHA256, 36)
    hotpDemo(secret, 8, Algorithm.SHA512, 36)
    totpDemo(secret, 6, 30, Algorithm.SHA1, 10)
    totpDemo(secret, 8, 30, Algorithm.SHA1, 10)
    totpDemo(secret, 8, 30, Algorithm.SHA256, 10)
    totpDemo(secret, 8, 30, Algorithm.SHA512, 10)
    totpDemo(secret, 8, 30, Algorithm.SHA1, 16)
    totpDemo(secret, 8, 30, Algorithm.SHA256, 16)
    totpDemo(secret, 8, 30, Algorithm.SHA512, 16)
    totpDemo(secret, 8, 30, Algorithm.SHA1, 36)
    totpDemo(secret, 8, 30, Algorithm.SHA256, 36)
    totpDemo(secret, 8, 30, Algorithm.SHA512, 36)
}

fun hotpDemo(secret: String, digits: Int, algorithm: Algorithm, radix: Int) {
    val config = HOTPConfig(secret = secret, length = digits, algorithm = algorithm, radix = radix)
    val hotp = HOTP(config)
    val counter = System.currentTimeMillis() / 1_000

    val otp = hotp.at(counter)
    val verify = hotp.verify(otp, counter)

    println()
    println()
    try {
        println(hotp.provisioningUri("KOTP_HOTP"))
    } catch (ex: RuntimeException) {
        println(ex.localizedMessage)
    }
    println("$digits <> $algorithm <> $radix")
    println("$counter  ==>  $otp  <>  ${counter == verify}")
}

fun totpDemo(secret: String, length: Int, interval: Int, algorithm: Algorithm, radix: Int) {
    val config = TOTPConfig(
        secret = secret,
        issuer = "kotp_lib",
        length = length,
        interval = interval,
        algorithm = algorithm,
        radix = radix
    )
    val totp = TOTP(config)
    val seconds = Calendar.getInstance().time.time / 1_000

    val otp = totp.at(seconds)
    val verify = totp.verify(otp, at = seconds)

    val otpNow = totp.now()
    val verifyNow = totp.verify(otpNow)

    println()
    println()
    try {
        println(totp.provisioningUri("KOTP_TOTP"))
    } catch (ex: RuntimeException) {
        println(ex.localizedMessage)
    }
    println("$length <> $algorithm <> $radix")
    println("$seconds  ==>  $otp  <>  ${verify != null}")
    println("NOW         ==>  $otpNow  <>  ${verifyNow != null}")
}
