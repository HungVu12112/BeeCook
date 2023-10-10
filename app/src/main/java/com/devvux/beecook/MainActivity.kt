package com.devvux.beecook

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.devvux.beecook.databinding.ActivityMainBinding
import com.devvux.beecook.db.MealDatabase
import com.devvux.beecook.viewModel.HomeViewModel
import com.devvux.beecook.viewModel.HomeViewModelFactory

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
     val viewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelFactory =HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this,homeViewModelFactory)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = Navigation.findNavController(this,R.id.hots_fragment)
        NavigationUI.setupWithNavController(binding.navigation,navController)
        Handler(Looper.getMainLooper()).postDelayed({
            createNotification()
        },6000)
    }
    @SuppressLint("MissingPermission")
    private fun createNotification() {
        val channelId = "channelId"
        val notificationId = 1
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            Intent.FILL_IN_ACTION or PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.queenbee)
            .setContentTitle("BeeCook")
            .setSound(null)
            .setContentText("Chúc bạn một ngày mới chàn đầy năng lượng")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tạo kênh thông báo (chỉ cần làm một lần)
            val channel = NotificationChannel(channelId, "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, builder.build())
    }
}