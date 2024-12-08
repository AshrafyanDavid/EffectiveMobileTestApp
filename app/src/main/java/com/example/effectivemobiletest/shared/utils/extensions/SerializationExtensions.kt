package com.example.effectivemobiletest.shared.utils.extensions

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = false
}

fun getJsonConverter() = json

fun <T> encodeListToJson(list: List<T>, serializer: KSerializer<T>): String {
    return getJsonConverter().encodeToString(ListSerializer(serializer), list)
}

inline fun <reified T> List<List<T>>.encodeToJson(serializer: KSerializer<T>): String {
    return getJsonConverter().encodeToString(ListSerializer(ListSerializer(serializer)), this)
}

fun <T> decodeJsonToList(json: String, serializer: KSerializer<T>): MutableList<T> {
    return getJsonConverter().decodeFromString(ListSerializer(serializer), json).toMutableList()
}

inline fun <reified T> decodeJsonToListOfList(json: String, serializer: KSerializer<T>): List<List<T>> {
    return getJsonConverter().decodeFromString(ListSerializer(ListSerializer(serializer)), json)
}

fun <T> encodeObjectToJson(item: T, serializer: KSerializer<T>): String {
    return getJsonConverter().encodeToString(serializer, item)
}

fun <T> decodeJsonToObject(json: String, serializer: KSerializer<T>): T {
    return getJsonConverter().decodeFromString(serializer, json)
}

inline fun <reified T, reified K> Map<K, List<T>>.encodeToJson(kSerializer: KSerializer<K>, tSerializer: KSerializer<T>): String {
    return getJsonConverter().encodeToString(MapSerializer(kSerializer, ListSerializer(tSerializer)), this)
}

fun <K, T> decodeJsonToMap(json: String, kSerializer: KSerializer<K>, tSerializer: KSerializer<T>): MutableMap<K, List<T>>? {
    return getJsonConverter().decodeFromString(MapSerializer(kSerializer, ListSerializer(tSerializer)), json).toMutableMap()
}