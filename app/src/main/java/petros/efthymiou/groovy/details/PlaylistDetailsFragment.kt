package petros.efthymiou.groovy.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_playlist_detail.*
import kotlinx.android.synthetic.main.playlist_item.playlist_name
import petros.efthymiou.groovy.R
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistDetailsFragment : Fragment() {

    lateinit var viewModel: PlaylistDetailsViewModel

    @Inject
    lateinit var viewModelFactory: PlaylistDetailsViewModelFactory

    private val args: PlaylistDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_playlist_detail, container, false)
        val id = args.playlistId
        setupViewModel()
        viewModel.getPlaylistDetails(id)
        observePlaylistDetails()
        observeLoader()

        return view
    }

    private fun observeLoader() {
        viewModel.loader.observe(this as LifecycleOwner, { loading ->
            when (loading) {
                true -> details_loader.visibility = View.VISIBLE
                false -> details_loader.visibility = View.GONE
            }
        })
    }

    private fun observePlaylistDetails() {
        viewModel.playlistDetails.observe(this as LifecycleOwner) { playlistsDetails ->
            if (playlistsDetails.getOrNull() != null) {
                setupUI(playlistsDetails)
            } else {
                Snackbar.make(playlistes_details_root, R.string.generic_error, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(PlaylistDetailsViewModel::class.java)
    }

    private fun setupUI(playlistsDetails: Result<PlaylistDetails>) {
        playlist_name.text = playlistsDetails.getOrNull()!!.name
        playlist_details.text = playlistsDetails.getOrNull()!!.details
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlaylistDetailsFragment()
    }
}