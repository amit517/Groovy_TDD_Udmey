package petros.efthymiou.groovy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import petros.efthymiou.groovy.playlist.PlaylistFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, PlaylistFragment.newInstance())
                .commit()
        }
    }
}