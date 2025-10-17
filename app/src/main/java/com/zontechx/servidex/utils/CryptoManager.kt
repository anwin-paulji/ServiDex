package com.zontechx.servidex.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object CryptoManager {

    private const val SECRET_KEY = "MySuperSecretKey1!" // Must be 16 chars
    private const val ALGORITHM = "AES"

    private fun getSecretKeySpec(): SecretKeySpec {
        return SecretKeySpec(SECRET_KEY.toByteArray(Charsets.UTF_8), ALGORITHM)
    }

    fun encrypt(input: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec())
        val encryptedBytes = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(encrypted: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec())
        val decodedBytes = Base64.decode(encrypted, Base64.DEFAULT)
        val original = cipher.doFinal(decodedBytes)
        return String(original, Charsets.UTF_8)
    }
}
