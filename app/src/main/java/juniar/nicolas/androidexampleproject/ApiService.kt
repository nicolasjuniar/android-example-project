package juniar.nicolas.androidexampleproject

import retrofit2.http.GET

interface ApiService {
    @GET("fact")
    suspend fun getRandomCatFact(): CatFactResponse
}