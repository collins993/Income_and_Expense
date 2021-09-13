package io.github.collins993.budgettracker2.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.github.collins993.budgettracker2.MyApplication
import io.github.collins993.budgettracker2.models.Income
import io.github.collins993.budgettracker2.models.User
import io.github.collins993.budgettracker2.utils.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MyViewModel(application: Application): AndroidViewModel(application) {

    private var  auth: FirebaseAuth? = null
    private var  userCollectionReference: CollectionReference? = null
    private var refUsers: DatabaseReference? = null
    private var firebaseUserId: String = ""
    //
    private val _registrationStatus = MutableLiveData<Resource<String>>()
    val registrationStatus: LiveData<Resource<String>> = _registrationStatus
    //
    private val _signInStatus = MutableLiveData<Resource<String>>()
    val signInStatus: LiveData<Resource<String>> = _signInStatus
    //
    private val _signOutStatus = MutableLiveData<Resource<String>>()
    val signOutStatus: LiveData<Resource<String>> = _signOutStatus
    //
    private val _resetPasswordStatus = MutableLiveData<Resource<String>>()
    val  resetPasswordStatus: LiveData<Resource<String>> = _resetPasswordStatus
    //
    private val _addUserStatus = MutableLiveData<Resource<String>>()
    val  addUserStatus: LiveData<Resource<String>> = _addUserStatus
    //
    private val _getUserDetails = MutableLiveData<Resource<DataSnapshot?>>()
    val getUserDetails: LiveData<Resource<DataSnapshot?>> = _getUserDetails
    //
    private val _addIncomeStatus = MutableLiveData<Resource<String>>()
    val  addIncomeStatus: LiveData<Resource<String>> = _addIncomeStatus
    //
    private val _getIncomeItem = MutableLiveData<Resource<QuerySnapshot?>>()
    val getIncomeItem: LiveData<Resource<QuerySnapshot?>> = _getIncomeItem
    init {
        auth = FirebaseAuth.getInstance()
        userCollectionReference = Firebase.firestore.collection("incomes")
        refUsers = Firebase.database.reference
    }

    /**
     * Firebase Registration
     * ***/
    fun signUp(email:String, password:String){
        viewModelScope.launch{
            var  errorCode = -1
            _registrationStatus.postValue(Resource.Loading())
            try{
                auth?.let { authentication ->
                    authentication.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener {task: Task<AuthResult> ->
                            if(!task.isSuccessful){
                                println("Registration Failed with ${task.exception}")
                                _registrationStatus.postValue(Resource.Success("Registration Failed with ${task.exception}"))
                            }else{
                                _registrationStatus.postValue(Resource.Success("UserCreated"))

                            }
                        }

                }
            }catch (e:Exception){
                e.printStackTrace()
                if(errorCode != -1){
                    _registrationStatus.postValue(Resource.Error("Failed with Error Code ${errorCode} ", e.toString()))
                }else{
                    _registrationStatus.postValue(Resource.Error("Failed with Exception ${e.message} ", e.toString()))
                }


            }
        }
    }

    fun signIn(email:String, password:String){

        viewModelScope.launch{
            var  errorCode = -1
            _signInStatus.postValue(Resource.Loading())
            try{
                auth?.let{ login->
                    login.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener {task: Task<AuthResult> ->

                            if(!task.isSuccessful){
                                println("Login Failed with ${task.exception}")
                                _signInStatus.postValue(Resource.Success("Login Failed with ${task.exception}"))
                            }else{
                                _signInStatus.postValue(Resource.Success("Login Successful"))

                            }
                        }

                }

            }catch (e:Exception){
                e.printStackTrace()
                if(errorCode != -1){
                    _signInStatus.postValue(Resource.Error("Failed with Error Code ${errorCode} ", e.toString()))
                }else{
                    _signInStatus.postValue(Resource.Error("Failed with Exception ${e.message} ", e.toString()))
                }


            }
        }
    }

    fun signOut(){

        viewModelScope.launch{
            var  errorCode = -1
            _signOutStatus.postValue(Resource.Loading())
            try{
                auth?.let {authentation ->
                    authentation.signOut()
                    _signOutStatus.postValue(Resource.Success("Signout Successful"))

                }

            }catch (e:Exception){
                e.printStackTrace()
                if(errorCode != -1){
                    _signOutStatus.postValue(Resource.Error("Failed with Error Code ${errorCode} ", e.toString()))
                }else{
                    _signOutStatus.postValue(Resource.Error("Failed with Exception ${e.message} ", e.toString()))
                }


            }
        }
    }

    fun resetPassword(email: String){
        viewModelScope.launch {
            var  errorCode = -1
            _resetPasswordStatus.postValue(Resource.Loading())
            try{
                auth?.let{ resetPassword->
                    resetPassword.sendPasswordResetEmail(email)
                        .addOnCompleteListener {task ->

                            if(!task.isSuccessful){
                                //println("Login Failed with ${task.exception}")
                                _resetPasswordStatus.postValue(Resource.Success("Reset Failed with ${task.exception}"))
                            }else{
                                _resetPasswordStatus.postValue(Resource.Success("Reset Successful"))

                            }
                        }

                }

            }catch (e:Exception){
                e.printStackTrace()
                if(errorCode != -1){
                    _resetPasswordStatus.postValue(Resource.Error("Failed with Error Code ${errorCode} ", e.toString()))
                }else{
                    _resetPasswordStatus.postValue(Resource.Error("Failed with Exception ${e.message} ", e.toString()))
                }


            }
        }
    }

    fun storeUserInFireBaseDatabase(user: User){

        viewModelScope.launch {
            var errorCode = -1
            _addUserStatus.postValue(Resource.Loading())

            try {
                firebaseUserId = auth?.currentUser?.uid.toString()
                refUsers?.child("users")?.child(firebaseUserId)?.setValue(user)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful){

                            _addUserStatus.postValue(Resource.Success("Successfully added user"))
                        }
                        else{
                                _addUserStatus.postValue(Resource.Error("Failed in adding user"))
                        }

                    }



            } catch (e: Exception){
                e.printStackTrace()
                if(errorCode != -1){
                    _addUserStatus.postValue(Resource.Error("Failed with Error Code ${errorCode} ", e.toString()))
                }else{
                    _addUserStatus.postValue(Resource.Error("Failed with Exception ${e.message} ", e.toString()))
                }
            }
        }
    }

    fun retrieveUserFromFireBaseDatabase(){
        viewModelScope.launch {
            var errorCode = -1
            try {

                firebaseUserId = auth?.currentUser?.uid.toString()
                refUsers?.child("users")?.child(firebaseUserId)
                    ?.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        _getUserDetails.postValue(Resource.Success(snapshot))
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

            }catch (e:Exception){
                e.printStackTrace()
                if(errorCode != -1){
                    _getUserDetails.postValue(Resource.Error(e.toString()))
                }else{
                    _getUserDetails.postValue(Resource.Error(e.toString()))
                }
            }
        }
    }

    fun addIncomeItemToFireStore(income: Income){
        viewModelScope.launch {
            var errorCode = -1

            try {
                auth?.currentUser?.let {
                    userCollectionReference?.add(income)?.await()
                    _addIncomeStatus.postValue(Resource.Success("Income Item Saved"))
                }

            }catch (e:Exception){
                e.printStackTrace()
                if(errorCode != -1){
                    _addIncomeStatus.postValue(Resource.Error("Failed with Error Code ${errorCode} ", e.toString()))
                }else{
                    _addIncomeStatus.postValue(Resource.Error("Failed with Exception ${e.message} ", e.toString()))
                }
            }
        }
    }

    fun retrieveIncomeItemFromFireStore(){
        viewModelScope.launch {
            var errorCode = -1
            try {

                auth?.currentUser?.let {
                    var querySnapshot = userCollectionReference?.get()?.await()

                    _getIncomeItem.postValue(Resource.Success(querySnapshot))
                }

//                for (document in querySnapshot?.documents!!){
//                    var userDoc = document.toObject(User::class.java)
//                   userDoc = user
//                }


            }catch (e:Exception){
                e.printStackTrace()
                if(errorCode != -1){
                    _getIncomeItem.postValue(Resource.Error(e.toString()))
                }else{
                    _getIncomeItem.postValue(Resource.Error(e.toString()))
                }
            }
        }
    }

}