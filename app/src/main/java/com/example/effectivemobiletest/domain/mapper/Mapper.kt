package com.example.effectivemobiletest.domain.mapper

interface Mapper<in T, out K> {
    fun map(model: T?): K?
}