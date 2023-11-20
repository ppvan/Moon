package me.ppvan.moon.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.ppvan.moon.services.MoonMediaService
import me.ppvan.moon.services.PermissionsManager
import me.ppvan.moon.ui.Nav.graphs.AlbumGraph
import me.ppvan.moon.ui.Nav.graphs.ArtistGraph
import me.ppvan.moon.ui.theme.MoonTheme
import me.ppvan.moon.ui.view.DownloadView
import me.ppvan.moon.ui.view.HomeView
import me.ppvan.moon.ui.view.SettingView
import me.ppvan.moon.ui.view.TagEditView
import me.ppvan.moon.ui.view.nowplaying.NowPlayingQueue
import me.ppvan.moon.ui.view.nowplaying.NowPlayingView
import me.ppvan.moon.ui.viewmodel.AlbumViewModel
import me.ppvan.moon.ui.viewmodel.ArtistViewModel
import me.ppvan.moon.ui.viewmodel.TagEditViewModel
import me.ppvan.moon.ui.viewmodel.TrackViewModel
import me.ppvan.moon.ui.viewmodel.YTViewModel
import me.ppvan.moon.utils.DownloadViewModel
import me.ppvan.moon.utils.FadeTransition
import me.ppvan.moon.utils.ScaleTransition
import me.ppvan.moon.utils.SlideTransition
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var permissionsManager: PermissionsManager

    @Inject
    lateinit var ytViewModel: YTViewModel

    @Inject
    lateinit var trackViewModel: TrackViewModel

    @Inject
    lateinit var albumViewModel: AlbumViewModel

    @Inject
    lateinit var artistViewModel: ArtistViewModel

    @Inject
    lateinit var downloadViewModel: DownloadViewModel

    @Inject
    lateinit var tagEditViewModel: TagEditViewModel

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MoonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

//                    SearchView(YTViewModel)
                    MoonApp(
                        activity = this,
                        ytViewModel = ytViewModel,
                        trackViewModel = trackViewModel,
                        albumViewModel = albumViewModel,
                        artistViewModel = artistViewModel,
                        downloadViewModel = downloadViewModel,
                        tagEditViewModel = tagEditViewModel
                    )

                }

            }
        }

        startPlayerService()
//        askManageMediaPermission()
        lifecycleScope.launch {
            tagEditViewModel.permissionEvents
                .filter { it != null }
                .onEach {
               startIntentSender(it!!, null, 0,0, 0)
            }.collect()
        }
        permissionsManager.handle(this)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun askManageMediaPermission() {
        if (!MediaStore.canManageMedia(this)) {
            launchMediaManagementIntent { }
        }
    }

    private fun startPlayerService() {
        lifecycleScope.launch(Dispatchers.Main) {
            val serviceIntent = Intent(this@MainActivity, MoonMediaService::class.java)
            startService(serviceIntent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun launchMediaManagementIntent(callback: () -> Unit) {
        val intent = Intent(Settings.ACTION_REQUEST_MANAGE_MEDIA).apply {
            data = Uri.parse("package:$packageName")
        }
        startActivity(intent)
    }

}

data class ViewContext(
    val navigator: NavHostController,
    val activity: Activity,
    val ytViewModel: YTViewModel,
    val trackViewModel: TrackViewModel,
    val albumViewModel: AlbumViewModel,
    val artistViewModel: ArtistViewModel,
    val downloadViewModel: DownloadViewModel,
    val tagEditViewModel: TagEditViewModel,
)

@Composable
fun MoonApp(
    activity: Activity,
    ytViewModel: YTViewModel,
    trackViewModel: TrackViewModel,
    albumViewModel: AlbumViewModel,
    artistViewModel: ArtistViewModel,
    downloadViewModel: DownloadViewModel,
    tagEditViewModel: TagEditViewModel,
    navController: NavHostController = rememberNavController()
) {

    val context = ViewContext(
        navigator = navController,
        activity = activity,
        ytViewModel = ytViewModel,
        trackViewModel = trackViewModel,
        albumViewModel = albumViewModel,
        artistViewModel = artistViewModel,
        downloadViewModel = downloadViewModel,
        tagEditViewModel = tagEditViewModel
    )

    NavHost(navController = navController, startDestination = Routes.Home.name) {
        AlbumGraph(context)
        composable(
            Routes.Home.name,
            enterTransition = { FadeTransition.enterTransition() }
        ) {
            HomeView(context = context)
        }
        composable(
            Routes.NowPlaying.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
            exitTransition = { FadeTransition.exitTransition() },
            popEnterTransition = { FadeTransition.enterTransition() },
            popExitTransition = { SlideTransition.slideDown.exitTransition() },
        ) {
            NowPlayingView(context = context)
        }

        composable(
            Routes.NowPlayingQueue.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
            exitTransition = { FadeTransition.exitTransition() },
            popEnterTransition = { FadeTransition.enterTransition() },
            popExitTransition = { SlideTransition.slideDown.exitTransition() },
        ) {
            NowPlayingQueue(context = context)
        }
        ArtistGraph(context)


        composable(
            Routes.Settings.name,
            enterTransition = { ScaleTransition.scaleDown.enterTransition() },
            exitTransition = { ScaleTransition.scaleUp.exitTransition() },
        ) {
            SettingView(context)
        }

        composable(
            Routes.Download.name,
            enterTransition = { ScaleTransition.scaleDown.enterTransition() },
            exitTransition = { ScaleTransition.scaleUp.exitTransition() },
        ) {
            DownloadView(context)
        }

        composable(
            "${Routes.TagEdit.name}/{mediaId}",
            enterTransition = { ScaleTransition.scaleDown.enterTransition() },
            exitTransition = { ScaleTransition.scaleUp.exitTransition() },
        ) {
            it.arguments?.let { it1 -> TagEditView(context, it1.getString("mediaId", "")) }
        }
    }
}


enum class Routes() {
    Home, NowPlaying, NowPlayingQueue, Album, Artist, Playlist, Settings, Download, TagEdit
}
