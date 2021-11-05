package io.github.collins993.budgettracker2.ui.profile

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.registration.LoginActivity
import io.github.collins993.budgettracker2.utils.Resource
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory

class ProfileSettingsFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var viewModel: MyViewModel


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(requireActivity(), viewModelProviderFactory).get(MyViewModel::class.java)

        viewModel.retrieveUserInfoFromFireStore()



        val resetPassword = findPreference<Preference>("reset_password_key")
        val editTxtUsername = findPreference<Preference>("edit_username_key")
        val aboutApp = findPreference<Preference>("about_app_key")
        val privacyPolicy = findPreference<Preference>("privacy_policy_key")
        val logOut = findPreference<Preference>("logout_key")


        viewModel.getUserStatus.observe(this, {
            Log.i("User", "${it?.username}")
            editTxtUsername?.summary = it?.emailAddress
            editTxtUsername?.title = it?.username

        })



        resetPassword?.setOnPreferenceClickListener {

            findNavController().navigate(R.id.action_profileSettingsFragment_to_resetPasswordFragment)
            true
        }
        aboutApp?.setOnPreferenceClickListener {

            findNavController().navigate(R.id.action_profileSettingsFragment_to_aboutAppFragment)
            true
        }
        privacyPolicy?.setOnPreferenceClickListener {
            //Open web page
            true
        }
        logOut?.setOnPreferenceClickListener {
            viewModel.signOut()
            true
        }

        viewModel.signOutStatus.observe(this, Observer { response ->
            when(response){
                is Resource.Success ->{
                    if(response.data.equals("Signout Successful",ignoreCase = true)){
                        //Start Activity
                        startActivity(Intent(requireActivity(), LoginActivity::class.java))
                        requireActivity().finish()

                    }
                }
                is Resource.Error -> {
                    val failedMessage =  response.message ?: "Unknown Error"
                    Toast.makeText(requireContext(),"Signout  failed $failedMessage", Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {

                }
            }
        })
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {

        if (key == "edit_username_key"){
            val newUsername = sharedPreferences.getString(key, "")
            Log.i("New Name", newUsername.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(this)
    }

}