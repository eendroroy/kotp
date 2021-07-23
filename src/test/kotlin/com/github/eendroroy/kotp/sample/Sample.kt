package com.github.eendroroy.kotp.sample

import com.github.eendroroy.kotp.Digest
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
    hotpDemo(secret, 6, Digest.SHA1, 10)
    hotpDemo(secret, 8, Digest.SHA1, 10)
    hotpDemo(secret, 8, Digest.SHA256, 10)
    hotpDemo(secret, 8, Digest.SHA512, 10)
    hotpDemo(secret, 8, Digest.SHA1, 16)
    hotpDemo(secret, 8, Digest.SHA256, 16)
    hotpDemo(secret, 8, Digest.SHA512, 16)
    hotpDemo(secret, 8, Digest.SHA1, 36)
    hotpDemo(secret, 8, Digest.SHA256, 36)
    hotpDemo(secret, 8, Digest.SHA512, 36)
    totpDemo(secret, 6, 30, Digest.SHA1, 10)
    totpDemo(secret, 8, 30, Digest.SHA1, 10)
    totpDemo(secret, 8, 30, Digest.SHA256, 10)
    totpDemo(secret, 8, 30, Digest.SHA512, 10)
    totpDemo(secret, 8, 30, Digest.SHA1, 16)
    totpDemo(secret, 8, 30, Digest.SHA256, 16)
    totpDemo(secret, 8, 30, Digest.SHA512, 16)
    totpDemo(secret, 8, 30, Digest.SHA1, 36)
    totpDemo(secret, 8, 30, Digest.SHA256, 36)
    totpDemo(secret, 8, 30, Digest.SHA512, 36)
}

fun hotpDemo(secret: String, digits: Int, digest: Digest, base: Int) {
    val config = HOTPConfig(secret = secret, digits = digits, digest = digest, base = base)
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
    println("$digits <> $digest <> $base")
    println("$counter  ==>  $otp  <>  ${counter == verify}")
}

fun totpDemo(secret: String, digits: Int, interval: Int, digest: Digest, base: Int) {
    val config = TOTPConfig(
        secret = secret,
        issuer = "kotp_lib",
        digits = digits,
        interval = interval,
        digest = digest,
        base = base
    )
    val totp = TOTP(config)
    val date = Calendar.getInstance().time
    val seconds = date.time / 1_000

    val otp = totp.at(date)
    val verify = totp.verify(otp, at = date)

    println()
    println()
    try {
        println(totp.provisioningUri("KOTP_TOTP"))
    } catch (ex: RuntimeException) {
        println(ex.localizedMessage)
    }
    println("$digits <> $digest <> $base")
    println("$date  ==>  $otp  <>  ${seconds - (seconds % interval) == verify}")
    println("NOW                           ==>  ${totp.now()}")
}