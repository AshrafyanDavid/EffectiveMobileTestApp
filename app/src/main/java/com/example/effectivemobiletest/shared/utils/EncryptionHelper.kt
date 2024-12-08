package com.example.effectivemobiletest.shared.utils

import android.annotation.SuppressLint
import android.provider.Settings
import android.util.Base64
import com.example.effectivemobiletest.App
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.PBEParameterSpec

object EncryptionHelper {

    private val SECRET = charArrayOf('E', 'f', 'f', 'e', 'c', 't', 'i', 'v', 'e')

    @SuppressLint("HardwareIds")
    private val deviceID = Settings.Secure.getString(App.applicationContext().contentResolver, Settings.Secure.ANDROID_ID)

    fun encrypt(value: String?): String? {
        return try {
            val bytes = value?.toByteArray(StandardCharsets.UTF_8) ?: ByteArray(0)
            val keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
            val key = keyFactory.generateSecret(PBEKeySpec(SECRET))
            val pbeCipher = Cipher.getInstance("PBEWithMD5AndDES")
            pbeCipher.init(
                Cipher.ENCRYPT_MODE, key, PBEParameterSpec(
                    deviceID.toByteArray(StandardCharsets.UTF_8), 20
                )
            )
            String(Base64.encode(pbeCipher.doFinal(bytes), Base64.NO_WRAP), StandardCharsets.UTF_8)
        } catch (e: Exception) {
            null
        }
    }

    fun decrypt(value: String?): String? {
        return if (value == "") "" else try {
            val bytes = if (value != null) Base64.decode(value, Base64.DEFAULT) else ByteArray(0)
            val keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
            val key = keyFactory.generateSecret(PBEKeySpec(SECRET))
            val pbeCipher = Cipher.getInstance("PBEWithMD5AndDES")
            pbeCipher.init(
                Cipher.DECRYPT_MODE, key, PBEParameterSpec(
                    deviceID.toByteArray(StandardCharsets.UTF_8), 20
                )
            )
            String(pbeCipher.doFinal(bytes), StandardCharsets.UTF_8)
        } catch (e: java.lang.Exception) {
            null
        }
    }
}