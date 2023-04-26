package mine.io.cryptography.symmetric

import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class AES {

    val secretKey: SecretKey
    val iv: ByteArray

    init {

        val keygen = KeyGenerator.getInstance("AES")
            .apply { init(256) }


        secretKey = keygen.generateKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")

        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        iv = cipher.iv

    }

    companion object {
        fun encrypt(data: ByteArray, key: SecretKey): ByteArray {

            val encryptor = Cipher.getInstance("AES/CBC/PKCS5PADDING")
                .apply { init(Cipher.ENCRYPT_MODE, key) }

            return encryptor.doFinal(data)
        }

        fun decrypt(data: ByteArray, key: SecretKey, iv: ByteArray): ByteArray {

            val ivSpec = IvParameterSpec(iv)
            val decryptor = Cipher.getInstance("AES/CBC/PKCS5PADDING")
                .apply { init(Cipher.DECRYPT_MODE, key, ivSpec) }

            return decryptor.doFinal(data)
        }
    }


}