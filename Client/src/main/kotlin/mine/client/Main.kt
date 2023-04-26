package mine.client

import kotlinx.coroutines.runBlocking
import mine.io.cryptography.symmetric.AES
import java.util.Base64

fun main() = runBlocking {
    //Client("localhost", 5004).start().join()

    val aes = AES()


    val key = aes.secretKey.encoded.toString(Charsets.UTF_8)
    println("key: $key")

    while (true) {
        val plainText = readlnOrNull()

        val encrypted = AES.encrypt(plainText!!.toByteArray(Charsets.UTF_8), aes.secretKey)
        println("encrypted: ${encrypted.toString(Charsets.UTF_8)}")

        val decrypted = AES.decrypt(encrypted, aes.secretKey, aes.iv)
        println("decrypted: ${decrypted.toString(Charsets.UTF_8)}")


        //    val encoder = Base64.getEncoder()
        //    val decoder = Base64.getDecoder()

//        val encrypted = aes.encrypt(decoder.decode(plainText))
//        println("encrypted: ${encoder.encodeToString(encrypted)}")
//
//        val decrypted = aes.decrypt(encrypted!!)
//        println("decrypted: ${encoder.encodeToString(decrypted)}")
    }
}


