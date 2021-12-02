package petros.efthymiou.groovy.playlist

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(FragmentComponent::class)
class PlaylistModule {

    @Provides
    fun playlistAPI(retrofit: Retrofit) = retrofit.create(PlaylistApi::class.java)


    @Provides
    fun retrofit() = Retrofit.Builder()
        .baseUrl("http://192.168.21.233:3000/") //must need to check our local ip. It's configured with Mockoon
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}