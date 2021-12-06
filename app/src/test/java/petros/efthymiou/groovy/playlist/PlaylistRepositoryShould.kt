package petros.efthymiou.groovy.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import petros.efthymiou.groovy.utils.BaseUnitTest

class PlaylistRepositoryShould : BaseUnitTest() {

    private val service: PlaylistService = mock()
    private val  mapper : PlaylistMapper = mock()
    private val playlists = mock<List<Playlist>>()
    private val playlistRaw = mock<List<PlaylistRaw>>()
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun getMappedPlaylistsFromService() = runBlockingTest {

        val repository = mocSuccessfulCase()

        repository.getPlaylists()

        verify(service, times(1)).fetchPlaylists()
    }

    @Test
    fun emitPlaylistsFromService() = runBlockingTest {

        val repository = mocSuccessfulCase()

        assertEquals(playlists, repository.getPlaylists().first().getOrNull())
    }

    @Test
    fun propagateErrors() = runBlockingTest {
        val repository = mockFailureCase()

        assertEquals(exception,repository.getPlaylists().first().exceptionOrNull())
    }

    @Test
    fun delegateBusinessLogicToMapper() = runBlockingTest{
        val repository = mocSuccessfulCase()

        repository.getPlaylists().first()

        verify(mapper,times(1)).invoke(playlistRaw)
    }

    private suspend fun mockFailureCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.failure<List<PlaylistRaw>>(exception))
            }
        )

        return PlaylistRepository(service,mapper)
    }

    private suspend fun mocSuccessfulCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.success(playlistRaw))
            }
        )

        whenever(mapper.invoke(playlistRaw)).thenReturn(playlists)

        return PlaylistRepository(service,mapper)
    }
}