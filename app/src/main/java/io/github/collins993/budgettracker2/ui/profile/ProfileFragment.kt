package io.github.collins993.budgettracker2.ui.profile

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.databinding.FragmentProfileBinding
import io.github.collins993.budgettracker2.models.User
import io.github.collins993.budgettracker2.registration.LoginActivity
import io.github.collins993.budgettracker2.utils.Resource
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory

class ProfileFragment : Fragment() {

    private lateinit var viewModel: MyViewModel
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(requireActivity(), viewModelProviderFactory).get(MyViewModel::class.java)



        binding.resetPasswordCardview.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_resetPasswordFragment)
        }
        binding.aboutAppCardview.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_aboutAppFragment)
        }

        binding.logOutCardview.setOnClickListener {
            viewModel.signOut()
        }

        viewModel.retrieveUserFromFireBaseDatabase()
        viewModel.getUserDetails.observe(viewLifecycleOwner, { snapShot ->
            when(snapShot){
                is Resource.Success ->{
                    snapShot.data?.let { dataSnapshot ->
                        val value = dataSnapshot.getValue(User::class.java)
                        binding.userName.text = value?.username
                        binding.userEmail.text = value?.emailAddress
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        })


        viewModel.signOutStatus.observe(viewLifecycleOwner, Observer { response ->
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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}