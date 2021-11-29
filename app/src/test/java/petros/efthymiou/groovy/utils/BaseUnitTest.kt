package petros.efthymiou.groovy.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

open class BaseUnitTest {
    @get:Rule
    val coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()
}