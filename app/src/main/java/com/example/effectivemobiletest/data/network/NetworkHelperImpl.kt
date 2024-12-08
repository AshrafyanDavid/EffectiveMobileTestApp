package com.example.effectivemobiletest.data.network

import com.example.effectivemobiletest.App
import com.example.effectivemobiletest.R
import com.example.effectivemobiletest.data.model.responsemodel.base.BaseResult
import com.example.effectivemobiletest.domain.mapper.Mapper
import com.example.effectivemobiletest.presentation.utils.extensions.logE
import com.example.effectivemobiletest.shared.utils.NetworkUtils
import com.example.effectivemobiletest.shared.utils.extensions.decodeJsonToObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.serialization.KSerializer
import retrofit2.Response
import java.net.HttpURLConnection

@Suppress("UNCHECKED_CAST")
class NetworkHelperImpl() : NetworkHelper {

    private val defaultInternalErrorMessage: String by lazy {
        App.applicationContext().getString(R.string.default_internal_error_message)
    }
    private val defaultNetworkErrorMessage: String by lazy {
        App.applicationContext().getString(R.string.default_network_error_message)
    }

    override fun <TResponse, TResultData, TErrorData> proceed(
        mapper: Mapper<TResponse, TResultData>?,
        serializer: KSerializer<TErrorData>?,
        doingApiCall: suspend () -> Response<TResponse>
    ): Flow<BaseResult<TResultData, TErrorData>> {
        return channelFlow {
            if (!NetworkUtils.isNetworkAvailable(App.applicationContext())) {
                send(BaseResult.NetworkError(defaultNetworkErrorMessage))
            } else {
                try {
                    val response = doingApiCall()
                    val body = response.body()
                    val errorBody = response.errorBody()

                    if (response.isSuccessful) {
                        when {
                            body != null -> {
                                val result: TResultData? = mapper?.map(body) ?: body as TResultData?
                                send(BaseResult.Success(result))
                            }

                            response.code() == HttpURLConnection.HTTP_NO_CONTENT -> {
                                send(BaseResult.Success(null))
                            }

                            else -> throw IllegalArgumentException("Response body cannot be empty")
                        }
                    } else
                        if (errorBody != null) {
                            when (response.code()) {
                                HttpURLConnection.HTTP_NOT_MODIFIED -> {
                                    val result: TResultData? = mapper?.map(body) ?: body as TResultData?
                                    send(BaseResult.Success(result))
                                }

                                HttpURLConnection.HTTP_NOT_FOUND -> {
                                    send(BaseResult.Error(defaultInternalErrorMessage))
                                }

                                HttpURLConnection.HTTP_BAD_REQUEST -> {
                                    val parsedJson: TErrorData? = try {
                                        decodeJsonToObject(errorBody.string(), serializer!!)
                                    } catch (e: Exception) {
                                        null
                                    }
                                    send(BaseResult.ErrorData(parsedJson))
                                }

                                HttpURLConnection.HTTP_UNAUTHORIZED, HttpURLConnection.HTTP_FORBIDDEN -> {
                                    send(BaseResult.Error())
                                }

                                else -> send(BaseResult.Error(defaultInternalErrorMessage))
                            }
                        } else {
                            send(BaseResult.Error(defaultInternalErrorMessage))
                        }
                } catch (e: Exception) {
                    trySend(BaseResult.Error(e.message))
                    logE(e.message)
                }
            }
        }
    }

    override fun <TResponse, TResultData, TErrorData> proceedWithoutWrapper(
        mapper: Mapper<TResponse, TResultData>?,
        serializer: KSerializer<TErrorData>?,
        doingApiCall: suspend () -> Response<TResponse>
    ): Flow<BaseResult<TResultData, TErrorData>> {
        return channelFlow {
            if (!NetworkUtils.isNetworkAvailable(App.applicationContext())) {
                send(BaseResult.NetworkError(defaultNetworkErrorMessage))
            } else {
                try {
                    val response = doingApiCall()
                    val body = response.body()
                    val errorBody = response.errorBody()

                    if (response.isSuccessful) {
                        when {
                            body != null -> {
                                val result: TResultData? = mapper?.map(body) ?: body as TResultData?
                                send(BaseResult.Success(result))
                            }

                            response.code() == HttpURLConnection.HTTP_NO_CONTENT -> send(BaseResult.Success(null))

                            else -> throw IllegalArgumentException("Response body cannot be empty")
                        }
                    } else
                        if (errorBody != null) {
                            when (response.code()) {
                                HttpURLConnection.HTTP_NOT_MODIFIED -> {
                                    val result: TResultData? = mapper?.map(body) ?: body as TResultData?
                                    send(BaseResult.Success(result))
                                }

                                HttpURLConnection.HTTP_NOT_FOUND -> {
                                    send(BaseResult.Error(defaultInternalErrorMessage))
                                }

                                HttpURLConnection.HTTP_BAD_REQUEST -> {
                                    val parsedJson: TErrorData? = try {
                                        decodeJsonToObject(errorBody.string(), serializer!!)
                                    } catch (e: Exception) {
                                        null
                                    }
                                    send(BaseResult.ErrorData(parsedJson))
                                }

                                HttpURLConnection.HTTP_UNAUTHORIZED, HttpURLConnection.HTTP_FORBIDDEN -> {
                                    send(BaseResult.Error())
                                }
                            }
                        } else {
                            send(BaseResult.Error(defaultInternalErrorMessage))
                        }
                } catch (e: Exception) {
                    trySend(BaseResult.Error(e.message ?: defaultNetworkErrorMessage))
                }
            }
        }
    }
}
