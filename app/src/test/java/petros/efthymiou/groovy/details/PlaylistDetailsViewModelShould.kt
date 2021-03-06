package petros.efthymiou.groovy.details

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test
import petros.efthymiou.groovy.utils.BaseUnitTest
import petros.efthymiou.groovy.utils.captureValues
import petros.efthymiou.groovy.utils.getValueForTest

class PlaylistDetailsViewModelShould : BaseUnitTest() {
    private val playlistDetailsService = mock<PlaylistDetailsService>()
    lateinit var viewModel: PlaylistDetailsViewModel
    private val id = "1"
    private val playlistDetails = mock<PlaylistDetails>()
    private val expected = Result.success(playlistDetails)
    private val exception = RuntimeException("Something went wrong")
    private val error = Result.failure<PlaylistDetails>(exception)

    init {

    }

    @Test
    fun getPlaylistDetailsFromService() = runBlockingTest {
        mockSuccessfulCase()

        viewModel.getPlaylistDetails(id)

        viewModel.playlistDetails.getValueForTest()

        verify(playlistDetailsService, times(1)).fetchPlaylistDetails(id)
    }

    @Test
    fun emitPlaylistDetailsFromService() = runBlockingTest {
        mockSuccessfulCase()

        viewModel.getPlaylistDetails(id)

        Assert.assertEquals(expected, viewModel.playlistDetails.getValueForTest())
    }

    @Test
    fun emitErrorWhenServiceFailed() {
        mockErrorCase()

        Assert.assertEquals(error, viewModel.playlistDetails.getValueForTest())
    }

    @Test
    fun showSpinnerWhileLoading() = runBlockingTest {
        mockSuccessfulCase()

        viewModel.loader.captureValues {
            viewModel.getPlaylistDetails(id)

            viewModel.playlistDetails.getValueForTest()

            Assert.assertEquals(true, values[0])
        }
    }

    @Test
    fun closeLoaderAfterPlaylistDetailsLoad() = runBlockingTest{
        mockSuccessfulCase()
        viewModel.loader.captureValues {
            viewModel.getPlaylistDetails(id)

            viewModel.playlistDetails.getValueForTest()

            Assert.assertEquals(false, values.last())
        }
    }

    private suspend fun mockSuccessfulCase() {
        whenever(playlistDetailsService.fetchPlaylistDetails(id)).thenReturn(
            flow { emit(expected) }
        )

        viewModel = PlaylistDetailsViewModel(playlistDetailsService)
    }

    private fun mockErrorCase(): Unit {
        runBlocking {
            whenever(playlistDetailsService.fetchPlaylistDetails(id)).thenReturn(
                flow { emit(error) }
            )
        }

        viewModel = PlaylistDetailsViewModel(playlistDetailsService)

        viewModel.getPlaylistDetails(id)
    }

}