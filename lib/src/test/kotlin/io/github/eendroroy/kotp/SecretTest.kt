package io.github.eendroroy.kotp

import io.github.eendroroy.kotp.config.Secret
import org.apache.commons.codec.binary.Base32
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * @author indrajit
 */
class SecretTest {
    @Test
    fun testSecretProperlyEncodes() {
        val rawStr = "secret"

        val secret = Secret(rawStr)

        assertEquals(secret.decoded()::class, ByteArray::class)
        assertEquals(secret.decodedString()::class, String::class)
        assertEquals(secret.encoded()::class, ByteArray::class)
        assertEquals(secret.encodedString()::class, String::class)

        assertTrue(secret.decoded().contentEquals(rawStr.toByteArray()))
        assertEquals(secret.decodedString(), rawStr)
        assertTrue(secret.encoded().contentEquals(Base32().encode(rawStr.toByteArray())))
        assertEquals(secret.encodedString(), Base32().encodeAsString(rawStr.toByteArray()).replace("=", ""))
    }

    @Test
    fun testRandomSecret() {
        val secret = Secret.random()

        assertEquals(secret.decoded()::class, ByteArray::class)
        assertEquals(secret.decodedString()::class, String::class)
        assertEquals(secret.encoded()::class, ByteArray::class)
        assertEquals(secret.encodedString()::class, String::class)
    }
}
