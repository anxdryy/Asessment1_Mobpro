package com.andryadis0105.asessment1_mobpro.network

import com.andryadis0105.asessment1_mobpro.model.Kamus
import com.andryadis0105.asessment1_mobpro.model.OpStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://apimobpromobil-production.up.railway.app/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface KamusApiService {
    @GET("kamus")
    suspend fun getKamus(
        @Header("Authorization") authorization: String
    ): List<Kamus>

    @Multipart
    @POST("kamus")
    suspend fun postKamus(
        @Header("Authorization") authorization: String,
        @Part("bahasaIndonesia") bahasaIndonesia: RequestBody,
        @Part("bahasaInggris") bahasaInggris: RequestBody,
        @Part gambar: MultipartBody.Part
    ): OpStatus

    @FormUrlEncoded
    @POST("kamus")
    suspend fun postKamusWithoutImage(
        @Header("Authorization") authorization: String,
        @Field("bahasaIndonesia") bahasaIndonesia: String,
        @Field("bahasaInggris") bahasaInggris: String
    ): OpStatus

    @Multipart
    @POST("kamus/{id}")
    suspend fun updateKamus(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Part("bahasaIndonesia") bahasaIndonesia: RequestBody,
        @Part("bahasaInggris") bahasaInggris: RequestBody,
        @Part gambar: MultipartBody.Part
    ): OpStatus

    @FormUrlEncoded
    @POST("kamus/{id}")
    suspend fun updateKamusWithoutImage(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Field("bahasaIndonesia") bahasaIndonesia: String,
        @Field("bahasaInggris") bahasaInggris: String
    ): OpStatus

    @DELETE("kamus/{id}")
    suspend fun deleteKamus(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): OpStatus
}

object KamusApi {
    val service: KamusApiService by lazy {
        retrofit.create(KamusApiService::class.java)
    }

    fun getKamusImageUrl(imagePath: String?): String {
        return if (imagePath != null) {
            "https://apimobpromobil-production.up.railway.app/storage/$imagePath"
        } else {
            ""
        }
    }
}