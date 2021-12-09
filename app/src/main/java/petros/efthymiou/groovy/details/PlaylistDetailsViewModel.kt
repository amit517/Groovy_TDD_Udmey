package petros.efthymiou.groovy.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel (private val playlistDetailsService: PlaylistDetailsService) : ViewModel(){
    val playlistDetails: MutableLiveData<Result<PlaylistDetails>> = MutableLiveData()

    fun getPlaylistDetails(id: String) {
        viewModelScope.launch{
            playlistDetailsService.fetchPlaylistDetails(id).collect {
                playlistDetails.postValue(it)
            }
        }
    }
}
