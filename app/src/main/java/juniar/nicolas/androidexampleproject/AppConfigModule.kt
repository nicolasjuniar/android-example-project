package juniar.nicolas.androidexampleproject

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import juniar.nicolas.androidskeletoncore.di.BaseUrl
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class AppConfigModule {

    @Provides
    @BaseUrl
    fun provideBaseUrl() = "https://catfact.ninja/"

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

}