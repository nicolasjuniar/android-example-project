package juniar.nicolas.androidexampleproject

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("fact")
    fun getRandomCatFact(): Call<CatFactResponse>
}