package com.example.effectivemobiletest.data.network

import com.example.effectivemobiletest.data.model.responsemodel.base.BaseResult
import com.example.effectivemobiletest.domain.mapper.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer
import retrofit2.Response

interface NetworkHelper {

    @DslMarker
    annotation class ApiRequest

    @ApiRequest
    fun <TResponse, TResultData, TErrorData> proceed(
        mapper: Mapper<TResponse, TResultData>? = null,
        serializer: KSerializer<TErrorData>? = null,
        doingApiCall: suspend () -> Response<TResponse>
    ): Flow<BaseResult<TResultData, TErrorData>>

    @ApiRequest
    fun <TResponse, TResultData, TErrorData> proceedWithoutWrapper(
        mapper: Mapper<TResponse, TResultData>? = null,
        serializer: KSerializer<TErrorData>? = null,
        doingApiCall: suspend () -> Response<TResponse>
    ): Flow<BaseResult<TResultData, TErrorData>>
}
