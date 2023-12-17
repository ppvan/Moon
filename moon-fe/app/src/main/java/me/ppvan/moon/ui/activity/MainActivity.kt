package me.ppvan.moon.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anggrayudi.storage.media.MediaStoreCompat
import com.anggrayudi.storage.media.MediaType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.services.MoonMediaService
import me.ppvan.moon.services.PermissionsManager
import me.ppvan.moon.ui.nav.graphs.AlbumGraph
import me.ppvan.moon.ui.nav.graphs.ArtistGraph
import me.ppvan.moon.ui.nav.graphs.PlaylistGraph
import me.ppvan.moon.ui.theme.MoonTheme
import me.ppvan.moon.ui.view.DownloadView
import me.ppvan.moon.ui.view.HomeView
import me.ppvan.moon.ui.view.LoginScreen
import me.ppvan.moon.ui.view.MoonPages
import me.ppvan.moon.ui.view.RegisterScreen
import me.ppvan.moon.ui.view.SettingView
import me.ppvan.moon.ui.view.TagEditView
import me.ppvan.moon.ui.view.UploadView
import me.ppvan.moon.ui.view.home.AlbumScreen
import me.ppvan.moon.ui.view.home.ArtistScreen
import me.ppvan.moon.ui.view.home.SongScreen
import me.ppvan.moon.ui.view.nowplaying.NowPlayingQueue
import me.ppvan.moon.ui.view.nowplaying.NowPlayingView
import me.ppvan.moon.ui.viewmodel.AlbumViewModel
import me.ppvan.moon.ui.viewmodel.ArtistViewModel
import me.ppvan.moon.ui.viewmodel.NotificationViewModel
import me.ppvan.moon.ui.viewmodel.PlaylistViewModel
import me.ppvan.moon.ui.viewmodel.ProfileViewModel
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

    @Inject
    lateinit var playlistViewModel: PlaylistViewModel

    @Inject
    lateinit var profileViewModel: ProfileViewModel

    @Inject
    lateinit var notificationViewModel: NotificationViewModel


    /*
        This code force to be here, to update mp3 tag
    */
    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                tagEditViewModel.writeTagToFile(this)
            } else {
                Toast.makeText(this, "Request write permission failed", Toast.LENGTH_SHORT)
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)


//        storageHelper.requestStorageAccess()

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
                        tagEditViewModel = tagEditViewModel,
                        playlistViewModel = playlistViewModel,
                        profileViewModel = profileViewModel,
                        notificationViewModel = notificationViewModel,
                    )

                }

            }
        }

        startPlayerService()
        permissionsManager.handle(this)

        lifecycleScope.launch {
            tagEditViewModel.pendingTrackWriteRequest
                .filter { it != Track.default() }
                .onEach { track ->

                    val mediaFile = MediaStoreCompat.fromMediaId(this@MainActivity, MediaType.AUDIO, track.id)
                    val intent = MediaStore.createWriteRequest(contentResolver, listOf(mediaFile?.uri))
                    val request = IntentSenderRequest.Builder(intent.intentSender).build()
                    intentLauncher.launch(request)

                }.collect()
        }
    }

    private fun startPlayerService() {
        lifecycleScope.launch(Dispatchers.Main) {
            val serviceIntent = Intent(this@MainActivity, MoonMediaService::class.java)
            startService(serviceIntent)
        }
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
    val playlistViewModel: PlaylistViewModel,
    val profileViewModel: ProfileViewModel,
    val selectedTab: MutableState<MoonPages>,
    val notificationViewModel: NotificationViewModel
) { fun updateSelectedTab(newTab: MoonPages) {
        selectedTab.value = newTab
    }
}

@Composable
fun MoonApp(
    activity: Activity,
    ytViewModel: YTViewModel,
    trackViewModel: TrackViewModel,
    albumViewModel: AlbumViewModel,
    artistViewModel: ArtistViewModel,
    downloadViewModel: DownloadViewModel,
    tagEditViewModel: TagEditViewModel,
    playlistViewModel: PlaylistViewModel,
    profileViewModel: ProfileViewModel,
    navController: NavHostController = rememberNavController(),
    selectedTab: MutableState<MoonPages> = rememberSaveable { mutableStateOf(MoonPages.Library) },
    notificationViewModel: NotificationViewModel
) {

    val context = ViewContext(
        navigator = navController,
        activity = activity,
        ytViewModel = ytViewModel,
        trackViewModel = trackViewModel,
        albumViewModel = albumViewModel,
        artistViewModel = artistViewModel,
        downloadViewModel = downloadViewModel,
        tagEditViewModel = tagEditViewModel,
        playlistViewModel = playlistViewModel,
        profileViewModel =  profileViewModel,
        selectedTab = selectedTab,
        notificationViewModel = notificationViewModel
    )

    NavHost(navController = navController, startDestination = Routes.Login.name) {
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
            Routes.Song.name,
            enterTransition = { ScaleTransition.scaleDown.enterTransition() },
            exitTransition = { ScaleTransition.scaleUp.exitTransition() },
        ) {
            SongScreen(context)
        }
        composable(
            "${Routes.TagEdit.name}/{mediaId}",
            enterTransition = { ScaleTransition.scaleDown.enterTransition() },
            exitTransition = { ScaleTransition.scaleUp.exitTransition() },
        ) {
            it.arguments?.let { it1 -> TagEditView(context, it1.getString("mediaId", "")) }
        }

        PlaylistGraph(context)

        composable(
            Routes.Register.name,
            enterTransition = { ScaleTransition.scaleDown.enterTransition() },
            exitTransition = { ScaleTransition.scaleUp.exitTransition() },
        ) {
            RegisterScreen(context)
        }

        composable(
            Routes.Login.name,
            enterTransition = { ScaleTransition.scaleDown.enterTransition() },
            exitTransition = { ScaleTransition.scaleUp.exitTransition() },
        ) {
            LoginScreen(context)
        }
        composable(
            Routes.Album.name,
            enterTransition = { ScaleTransition.scaleDown.enterTransition() },
            exitTransition = { ScaleTransition.scaleUp.exitTransition() },
        ) {
            AlbumScreen(context = context)
        }
        composable(
            Routes.Artist.name,
            enterTransition = { ScaleTransition.scaleDown.enterTransition() },
            exitTransition = { ScaleTransition.scaleUp.exitTransition() },
        ) {
            ArtistScreen(context = context)
        }
        composable(
            Routes.Upload.name,
            enterTransition = { ScaleTransition.scaleDown.enterTransition() },
            exitTransition = { ScaleTransition.scaleUp.exitTransition() },
        ) {
            UploadView(context = context)
        }
    }
}


enum class Routes() {
    Home, NowPlaying, NowPlayingQueue, Album, Artist, Playlist, Settings, Download, TagEdit, Register, Login, Profile, Song, Upload
}


