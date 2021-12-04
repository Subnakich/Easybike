package ru.subnak.easybike.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import ru.subnak.easybike.R
import ru.subnak.easybike.presentation.utils.Constants


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val preferenceEditUserFragment: Preference? = findPreference(getString(R.string.user_edit_key))
        preferenceEditUserFragment?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            launchEditUser()
            true
        }

        val preferenceReadPrivacyFragment: Preference? = findPreference(getString(R.string.privacy_policy_key))
        preferenceReadPrivacyFragment?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            launchReadPrivacy()
            true
        }

        val preferenceDarkTheme: SwitchPreferenceCompat? = findPreference(getString(R.string.dark_theme_key))
        preferenceDarkTheme?.onPreferenceChangeListener = themeModeChangeListener



    }

    private fun launchEditUser() {
        findNavController().navigate(
            SettingsFragmentDirections.actionNavigationSettingsToNavigationUser(Constants.MODE_EDIT)
        )
    }

    private fun launchReadPrivacy() {
        findNavController().navigate(
            R.id.action_navigation_settings_to_navigation_privacy_policy,
        )
    }
    private val themeModeChangeListener = object : Preference.OnPreferenceChangeListener {
        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            Log.i("newValue", newValue.toString())
            when (newValue) {
                true -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                }
                false -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
            return true
        }
    }

    private fun updateTheme(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        requireActivity().recreate()
    }


}