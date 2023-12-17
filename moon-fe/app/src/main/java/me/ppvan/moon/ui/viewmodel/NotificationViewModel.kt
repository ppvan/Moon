package me.ppvan.moon.ui.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.ppvan.moon.di.MainNotificationCompatBuilder
import me.ppvan.moon.di.SecondNotificationCompatBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationViewModel @Inject constructor(
    @MainNotificationCompatBuilder
    private val notificationBuilder: NotificationCompat.Builder,
    @SecondNotificationCompatBuilder
    private val notificationBuilder2: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat,
    private val context: Context
) : ViewModel() {
    fun cancelSimpleNotification() {
        notificationManager.cancel(1)
    }
    fun showProgress(max: Int) {
        var progress = 0
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        viewModelScope.launch {
            while (progress != max) {
                delay(2000)
                progress += 1
                notificationManager.notify(
                    3,
                    notificationBuilder2
                        .setSound(null)
                        .setContentTitle("Uploading")
                        .setContentText("${progress}/${max}")
                        .setProgress(max, progress, false).build()
                )
            }
            notificationManager.notify(
                3,
                notificationBuilder
                    .setContentTitle("Completed!")
                    .setContentText("")
                    .setContentIntent(null)
                    .clearActions()
                    .setProgress(0, 0, false).build()
            )
        }
    }

}