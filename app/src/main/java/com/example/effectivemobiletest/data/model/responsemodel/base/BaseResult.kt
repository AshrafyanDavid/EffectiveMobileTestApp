package com.example.effectivemobiletest.data.model.responsemodel.base

sealed class BaseResult<out D, out E> {

    class Success<D>(val data: D?) : BaseResult<D, Nothing>()

    class ErrorData<E>(val errorData: E? = null) : BaseResult<Nothing, E>()

    class Error(val msg: String? = null) : BaseResult<Nothing, Nothing>()

    class NetworkError(val msg: String? = null) : BaseResult<Nothing, Nothing>()
}
