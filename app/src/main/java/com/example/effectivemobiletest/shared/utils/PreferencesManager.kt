package com.example.effectivemobiletest.shared.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.effectivemobiletest.App
import com.example.effectivemobiletest.shared.utils.extensions.decodeJsonToObject
import com.example.effectivemobiletest.shared.utils.extensions.encodeObjectToJson
import kotlinx.serialization.KSerializer

object PreferencesManager {

    private const val PREF_NAME = "SHARED_PREF"
    const val FROM_COUNTRY_TEXT = "FROM_COUNTRY_TEXT"

    private val instance: SharedPreferences by lazy {
        App.applicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    private val editor: SharedPreferences.Editor by lazy { instance.edit() }

    private fun setString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    private fun setStringNullable(key: String, value: String?) {
        editor.putString(key, value).apply()
    }

    private fun setInt(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    private fun setFloat(key: String, value: Float) {
        editor.putFloat(key, value).apply()
    }

    private fun setLong(key: String, value: Long) {
        editor.putLong(key, value).apply()
    }

    private fun setBoolean(key: String, value: Boolean?) {
        editor.putBoolean(key, value!!).apply()
    }

    private fun getString(key: String, defaultValue: String = ""): String {
        return instance.getString(key, defaultValue)!!
    }

    private fun getInt(key: String, defaultValue: Int = -1): Int {
        return instance.getInt(key, defaultValue)
    }

    private fun getFloat(key: String, defaultValue: Float = -1F): Float {
        return instance.getFloat(key, defaultValue)
    }

    private fun getLong(key: String, defaultValue: Long = 0L): Long {
        return instance.getLong(key, defaultValue)
    }

    private fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return instance.getBoolean(key, defaultValue)
    }

    private fun <T> setObject(key: String, c: T, serializer: KSerializer<T>) {
        editor.putString(key, encodeObjectToJson(c, serializer)).apply()
    }

    private fun <T> getObject(key: String, serializer: KSerializer<T>): T? {
        val value = instance.getString(key, null)
        return if (value != null) decodeJsonToObject(value, serializer) else null
    }

    private fun storeAndEncodeStringInPreference(key: String, value: String?) {
        editor.putString(key, EncryptionHelper.encrypt(value)).apply()
    }

    private fun loadAndDecodeStringInPreference(key: String, defaultValue: String = ""): String? {
        val value = instance.getString(key, defaultValue)
        return if (value != null) EncryptionHelper.decrypt(value) else ""
    }

    private fun remove(key: String) {
        editor.remove(key).apply()
    }

    fun clear(): Boolean {
        return editor.clear().commit()
    }

//    /** Last Flexible InAppUpdate shown timestamp */
//    var lastFlexibleInAppUpdateShownTimeInMillis: Long
//        get() = getLong(LAST_FLEXIBLE_IN_APP_UPDATE_SHOWN_TIME_IN_MILLIS, -1L)
//        set(value) = setLong(LAST_FLEXIBLE_IN_APP_UPDATE_SHOWN_TIME_IN_MILLIS, value)

    var fromCountryText: String
        get() = getString(FROM_COUNTRY_TEXT, "")
        set(value) = setString(FROM_COUNTRY_TEXT, value)
}