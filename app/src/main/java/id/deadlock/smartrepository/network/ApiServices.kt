package id.deadlock.smartrepository.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiServices {
    companion object {
        const val URL = "https://api.gudang-pintar.com/"
    }

    @FormUrlEncoded
    @POST("index.php")
    fun daftar(
        @Field("fun") functions: String,

        @Field("username") username: String,
        @Field("password") password: String,
        @Field("nama") nama: String,
        @Field("email") email: String): Call<String>

    @FormUrlEncoded
    @POST("index.php")
    fun masuk(
        @Field("fun") functions: String,

        @Field("username") username: String,
        @Field("password") password: String): Call<String>

    @FormUrlEncoded
    @POST("index.php")
    fun karyaTulis(
        @Field("fun") functions: String,
        @Field("nomor_induk") nomor_induk: String): Call<String>

    @FormUrlEncoded
    @POST("index.php")
    fun detailKaryaTulis(
        @Field("fun") functions: String,
        @Field("nomor_induk") nomor_induk: String,
        @Field("id") id: String): Call<String>

    @FormUrlEncoded
    @POST("index.php")
    fun cariKaryaTulis(
        @Field("fun") functions: String,
        @Field("nomor_induk") nomor_induk: String,
        @Field("keyword") keyword: String,
        @Field("filter") filter: String,
        @Field("sort") sort: String,
        @Field("limit") limit: Int): Call<String>

    @FormUrlEncoded
    @POST("index.php")
    fun addReads(
        @Field("fun") functions: String,
        @Field("username") nomor_induk: String,
        @Field("id") id_keryaTulis: String): Call<String>

    @FormUrlEncoded
    @POST("index.php")
    fun addFavorite(
        @Field("fun") functions: String,
        @Field("username") nomor_induk: String,
        @Field("id") id_keryaTulis: String): Call<String>

    @FormUrlEncoded
    @POST("index.php")
    fun listKaryaTulis(
        @Field("fun") functions: String,
        @Field("nomor_induk") nomor_induk: String,
        @Field("kategori") kategori: String,
        @Field("limit") limit: Int): Call<String>

    @FormUrlEncoded
    @POST("index.php")
    fun InformasiKaryaTulisAkun(
        @Field("fun") functions: String,
        @Field("nomor_induk") nomor_induk: String): Call<String>

    @FormUrlEncoded
    @POST("index.php")
    fun listKaryaTulisAkun(
        @Field("fun") functions: String,
        @Field("nomor_induk") nomor_induk: String,
        @Field("limit") limit: Int): Call<String>

    @FormUrlEncoded
    @POST("index.php")
    fun listAkun(
        @Field("fun") functions: String): Call<String>

    @FormUrlEncoded
    @POST("index.php")
    fun verifikasiAkun(
        @Field("fun") functions: String,
        @Field("username") username: String): Call<String>

}