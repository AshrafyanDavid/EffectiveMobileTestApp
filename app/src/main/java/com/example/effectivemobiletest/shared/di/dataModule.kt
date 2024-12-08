@file:Suppress("RemoveExplicitTypeArguments")

package com.example.effectivemobiletest.shared.di

import com.example.effectivemobiletest.BuildConfig
import com.example.effectivemobiletest.data.network.NetworkHelper
import com.example.effectivemobiletest.data.network.NetworkHelperImpl
import com.example.effectivemobiletest.data.network.api.ContentApi
import com.example.effectivemobiletest.data.network.api.GoogleApi
import com.example.effectivemobiletest.data.repository.ContentRepositoryImpl
import com.example.effectivemobiletest.data.repository.GoogleRepositoryImpl
import com.example.effectivemobiletest.data.repository.IContentRepository
import com.example.effectivemobiletest.data.repository.IGoogleRepository
import com.example.effectivemobiletest.shared.utils.constants.Constants
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {

    single {
        Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
    }

    single<OkHttpClient> {
        val builder = OkHttpClient.Builder()
            .connectTimeout(Constants.NETWORK_CALL_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(Constants.NETWORK_CALL_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(Constants.NETWORK_CALL_TIMEOUT, TimeUnit.MILLISECONDS)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        } else {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
        }

        builder.build()
    }

    single<Retrofit>(named("UserContentRetrofit")) {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl(BuildConfig.baseUrlUserContent)
            .addConverterFactory(get<Json>().asConverterFactory(CONTENT_TYPE.toMediaType()))
            .build()
    }

    single<Retrofit>(named("GoogleRetrofit")) {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl(BuildConfig.baseUrlGoogle)
            .addConverterFactory(get<Json>().asConverterFactory(CONTENT_TYPE.toMediaType()))
            .build()
    }

    /** Network Helper */
    single<NetworkHelper> { NetworkHelperImpl() }

    /** Api */
    single<ContentApi> { get<Retrofit>(named("UserContentRetrofit")).create(ContentApi::class.java) }
    single<GoogleApi> { get<Retrofit>(named("GoogleRetrofit")).create(GoogleApi::class.java) }

    /** Repository */
    single<IContentRepository> {
        ContentRepositoryImpl(get<NetworkHelper>(), get<ContentApi>())
    }
    single<IGoogleRepository> {
        GoogleRepositoryImpl(get<NetworkHelper>(), get<GoogleApi>())
    }
}

private const val CONTENT_TYPE = "application/json"