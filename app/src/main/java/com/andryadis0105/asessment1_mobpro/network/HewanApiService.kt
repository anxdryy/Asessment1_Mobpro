package com.andryadis0105.asessment1_mobpro.network

import com.andryadis0105.asessment1_mobpro.model.Hewan
import com.andryadis0105.asessment1_mobpro.model.OpStatus
import com.andryadis0105.asessment1_mobpro.model.DogApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://dog.ceo/api/"
private const val OLD_BASE_URL = "https://gh.d3ifcool.org/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

private val oldRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(OLD_BASE_URL)
    .build()

interface HewanApiService {
    @GET("breeds/list/all")
    suspend fun getDogBreeds(): DogApiResponse

    @GET("breed/hound/images")
    suspend fun getDogImages(): DogApiResponse
}

interface OldHewanApiService {
    @GET("hewan.php")
    suspend fun getHewan(
        @Header("Authorization") userId: String
    ): List<Hewan>

    @Multipart
    @POST("hewan.php")
    suspend fun postHewan(
        @Header("Authorization") userId: String,
        @Part("nama") nama: RequestBody,
        @Part("namaLatin") namaLatin: RequestBody,
        @Part image: MultipartBody.Part,
    ): OpStatus

    @DELETE("hewan.php")
    suspend fun deleteHewan(
        @Header("Authorization") userId: String,
        @Query("id") id: String
    ): OpStatus
}

object HewanApi {
    val service: HewanApiService by lazy {
        retrofit.create(HewanApiService::class.java)
    }

    val oldService: OldHewanApiService by lazy {
        oldRetrofit.create(OldHewanApiService::class.java)
    }

    fun getHewanUrl(imageId: String): String {
        // Jika imageId adalah URL lengkap dari Dog API, return as is
        return if (imageId.startsWith("http")) {
            imageId
        } else {
            "${OLD_BASE_URL}image.php?id=$imageId"
        }
    }

    suspend fun getHewan(userId: String): List<Hewan> {
        return try {
            val response = service.getDogImages()
            if (response.status == "success") {
                response.message.take(2).mapIndexed { index, imageUrl ->
                    Hewan(
                        id = "dog_${index}",
                        nama = "Dog ${index + 1}",
                        namaLatin = "Canis lupus familiaris",
                        imageId = imageUrl,
                        mine = "0" // Semua dari API publik, bukan milik user
                    )
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Function untuk ambil data user dari API lama
    suspend fun getUserHewan(userId: String): List<Hewan> {
        return try {
            val userHewan = oldService.getHewan(userId)
            // Pastikan semua data user memiliki mine = "1"
            userHewan.map { hewan ->
                hewan.copy(mine = "1")
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }