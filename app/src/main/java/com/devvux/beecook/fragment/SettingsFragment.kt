package com.devvux.beecook.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.devvux.beecook.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var isDarkModeEnabled: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        sharedPreferences = context?.getSharedPreferences("theme_preference", MODE_PRIVATE)!!
//
//        // Kiểm tra trạng thái chế độ nền tối được lưu
//        isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false)
//
//        // Thiết lập giao diện phù hợp
//        if (isDarkModeEnabled) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }

        binding.switchcompat.setOnCheckedChangeListener { buttonView, isChecked ->
            isDarkModeEnabled = !isDarkModeEnabled
            saveDarkModeState(isDarkModeEnabled)
            if (isDarkModeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }
        applyTheme()

        binding.linearFeedback.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            var uriText: String =
                "mailto: " + Uri.encode("hungmno123@gmail.com") + "?subject=" + Uri.encode("Chủ đề phản hồi ")
            val uri = Uri.parse(uriText)
            intent.data = uri
            startActivity(intent)
        }
    }

    private fun applyTheme() {
        val isDarkModeEnabled = getDarkModeState()
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }

    private fun getDarkModeState(): Boolean {
        val sharedPreferences = context?.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("darkMode", false)
    }

    private fun saveDarkModeState(isDarkMode: Boolean) {
        val sharedPreferences = context?.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        sharedPreferences!!.edit().putBoolean("darkMode", isDarkMode).apply()
    }


}