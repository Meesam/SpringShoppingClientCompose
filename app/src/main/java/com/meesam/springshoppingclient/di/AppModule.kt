package com.meesam.springshoppingclient.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.meesam.springshoppingclient.network.AuthApiService
import com.meesam.springshoppingclient.network.CategoryApi
import com.meesam.springshoppingclient.network.UserApi
import com.meesam.springshoppingclient.repository.auth.UserAuthRepository
import com.meesam.springshoppingclient.repository.auth.UserAuthRepositoryImpl
import com.meesam.springshoppingclient.repository.category.CategoryRepository
import com.meesam.springshoppingclient.repository.category.CategoryRepositoryImpl
import com.meesam.springshoppingclient.repository.user.UserRepository
import com.meesam.springshoppingclient.repository.user.UserRepositoryImpl
import com.meesam.springshoppingclient.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getRetrofitInstance(): Retrofit.Builder {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        interceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun getAuthApiServices(retrofitBuilder: Retrofit.Builder): AuthApiService {
        return retrofitBuilder.build().create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApiService: AuthApiService): UserAuthRepository {
        return UserAuthRepositoryImpl(authApiService)
    }

    @Provides
    @Singleton
    fun getCategoryApiServices(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): CategoryApi {
        return retrofitBuilder.client(okHttpClient).build().create(CategoryApi::class.java)
    }

    @Provides
    @Singleton
    fun getUserApiServices(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): UserApi {
        return retrofitBuilder.client(okHttpClient).build().create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userApi: UserApi): UserRepository {
        return UserRepositoryImpl(userApi)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryApi: CategoryApi): CategoryRepository {
        return CategoryRepositoryImpl(categoryApi)
    }


}