package petros.efthymiou.groovy.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import petros.efthymiou.groovy.utils.BaseUnitTest
import petros.efthymiou.groovy.utils.captureValues
import petros.efthymiou.groovy.utils.getValueForTest


class PlaylistViewModelShould : BaseUnitTest() {

    //private val viewModel: PlaylistViewModel
    private val repository: PlaylistRepository = mock()
    private val playlists = mock<List<Playlist>>()
    private val expected = Result.success(playlists)
    private val exception = RuntimeException("Something went wrong")

    init {
    }

    @Test
    fun getPlaylistsFromRepository() = runBlockingTest {
        // as we are testing view model, this will be the only real object. Accept this all other obejct will be mocked
        val viewModel =
            mocSuccessfulCase()

        viewModel.playlists.getValueForTest()

        verify(repository, times(1)).getPlaylists()
    }


    @Test
    fun emitsPlaylistsFromRepository() = runBlockingTest {
        val viewModel =
            mocSuccessfulCase()

        assertEquals(expected, viewModel.playlists.getValueForTest())
    }

    @Test
    fun emitErrorWhenReceiveError() {
        val viewModel = mockErrorCase()

        assertEquals(exception, viewModel.playlists.getValueForTest()!!.exceptionOrNull())
    }

    @Test
    fun showSpinnerWhileLoading() = runBlockingTest {
        val viewModel = mocSuccessfulCase()

        viewModel.loader.captureValues {
            viewModel.playlists.getValueForTest()

            assertEquals(true, values[0])
        }
    }

    @Test
    fun closeLoaderAfterPlaylistLoad() = runBlockingTest {
        val viewModel = mocSuccessfulCase()

        viewModel.loader.captureValues {
            viewModel.playlists.getValueForTest()

            assertEquals(false, values.last())
        }
    }


    @Test
    fun closeLoaderAfterError() = runBlockingTest {
        val viewModel = mockErrorCase()

        viewModel.loader.captureValues {
            viewModel.playlists.getValueForTest()

            assertEquals(false, values.last())
        }
    }

    private fun mocSuccessfulCase(): PlaylistViewModel {
        runBlocking {
            whenever(repository.getPlaylists()).thenReturn(
                flow {
                    emit(expected)
                }
            )
        }
        return PlaylistViewModel(repository)
    }


    private fun mockErrorCase(): PlaylistViewModel {
        runBlocking {
            whenever(repository.getPlaylists()).thenReturn(
                flow { emit(Result.failure<List<Playlist>>(exception)) }
            )
        }
        return PlaylistViewModel(repository)
    }
}