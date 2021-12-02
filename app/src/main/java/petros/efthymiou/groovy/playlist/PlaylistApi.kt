package petros.efthymiou.groovy.playlist

interface PlaylistApi {
    suspend fun fetchAllPlaylists() : List<Playlist> {
        TODO()
    }
}
