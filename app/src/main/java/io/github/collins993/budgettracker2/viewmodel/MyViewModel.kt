package io.github.collins993.budgettracker2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import io.github.collins993.budgettracker2.models.Income
import io.github.collins993.budgettracker2.models.TotalExpenseAmount
import io.github.collins993.budgettracker2.models.TotalIncomeAmount
import io.github.collins993.budgettracker2.models.User
import io.github.collins993.budgettracker2.utils.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var user: User
    private var auth: FirebaseAuth? = null
    private var incomeCollectionReference: CollectionReference? = null
    private var expenseCollectionReference: CollectionReference? = null
    private var userCollectionReference: CollectionReference? = null
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
    val resetPasswordStatus: LiveData<Resource<String>> = _resetPasswordStatus

    //
    private val _addUserStatus = MutableLiveData<Resource<String>>()
    val addUserStatus: LiveData<Resource<String>> = _addUserStatus

    //
    private val _addIncomeStatus = MutableLiveData<Resource<String>>()
    val addIncomeStatus: LiveData<Resource<String>> = _addIncomeStatus

    //
    private val _getIncomeItem = MutableLiveData<List<Income>>()
    val getIncomeItem: LiveData<List<Income>> = _getIncomeItem

    //
    private val _addExpenseStatus = MutableLiveData<Resource<String>>()
    val addExpenseStatus: LiveData<Resource<String>> = _addExpenseStatus

    //
    private val _getExpenseItem = MutableLiveData<List<Income>>()
    val getExpenseItem: LiveData<List<Income>> = _getExpenseItem

    //
    private val _getUserStatus = MutableLiveData<User?>()
    val getUserStatus: LiveData<User?> = _getUserStatus

    //
    private val _totalBudgetStatus = MutableLiveData<Resource<String>>()
    val totalBudgetStatus: LiveData<Resource<String>> = _totalBudgetStatus

    //
    private val _totalExpenseStatus = MutableLiveData<Resource<String>>()
    val totalExpenseStatus: LiveData<Resource<String>> = _totalExpenseStatus

    //
    private val _remainingBudgetStatus = MutableLiveData<Resource<String>>()
    val remainingBudgetStatus: LiveData<Resource<String>> = _remainingBudgetStatus

    //
    private val _totalIncomeAmountStatus = MutableLiveData<Resource<TotalIncomeAmount?>>()
    val totalIncomeAmountStatus: LiveData<Resource<TotalIncomeAmount?>> = _totalIncomeAmountStatus

    //
    private val _totalExpenseAmountStatus = MutableLiveData<Resource<TotalExpenseAmount?>>()
    val totalExpenseAmountStatus: LiveData<Resource<TotalExpenseAmount?>> = _totalExpenseAmountStatus

    init {
        auth = FirebaseAuth.getInstance()
        incomeCollectionReference = auth!!.currentUser?.let {
            Firebase.firestore.collection("incomes")
        }
        expenseCollectionReference = auth!!.currentUser?.let {
            Firebase.firestore.collection("expense")
        }
        userCollectionReference = auth!!.currentUser?.let {
            Firebase.firestore.collection("users")
        }
    }

    /**
     * Firebase Registration
     * ***/
    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            var errorCode = -1
            _registrationStatus.postValue(Resource.Loading())
            try {
                auth?.let { authentication ->
                    authentication.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task: Task<AuthResult> ->
                            if (!task.isSuccessful) {
                                println("Registration Failed with ${task.exception}")
                                _registrationStatus.postValue(Resource.Success("Registration Failed with ${task.exception}"))
                            } else {
                                _registrationStatus.postValue(Resource.Success("UserCreated"))

                            }
                        }

                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    _registrationStatus.postValue(
                        Resource.Error(
                            "Failed with Error Code ${errorCode} ",
                            e.toString()
                        )
                    )
                } else {
                    _registrationStatus.postValue(
                        Resource.Error(
                            "Failed with Exception ${e.message} ",
                            e.toString()
                        )
                    )
                }


            }
        }
    }

    fun signIn(email: String, password: String) {

        viewModelScope.launch {
            var errorCode = -1
            _signInStatus.postValue(Resource.Loading())
            try {
                auth?.let { login ->
                    login.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task: Task<AuthResult> ->

                            if (!task.isSuccessful) {
                                println("Login Failed with ${task.exception}")
                                _signInStatus.postValue(Resource.Success("Login Failed with ${task.exception}"))
                            } else {
                                _signInStatus.postValue(Resource.Success("Login Successful"))

                            }
                        }

                }

            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    _signInStatus.postValue(
                        Resource.Error(
                            "Failed with Error Code ${errorCode} ",
                            e.toString()
                        )
                    )
                } else {
                    _signInStatus.postValue(
                        Resource.Error(
                            "Failed with Exception ${e.message} ",
                            e.toString()
                        )
                    )
                }


            }
        }
    }

    fun signOut() {

        viewModelScope.launch {
            var errorCode = -1
            _signOutStatus.postValue(Resource.Loading())
            try {
                auth?.let { authentation ->
                    authentation.signOut()
                    _signOutStatus.postValue(Resource.Success("Signout Successful"))

                }

            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    _signOutStatus.postValue(
                        Resource.Error(
                            "Failed with Error Code ${errorCode} ",
                            e.toString()
                        )
                    )
                } else {
                    _signOutStatus.postValue(
                        Resource.Error(
                            "Failed with Exception ${e.message} ",
                            e.toString()
                        )
                    )
                }


            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            var errorCode = -1
            _resetPasswordStatus.postValue(Resource.Loading())
            try {
                auth?.let { resetPassword ->
                    resetPassword.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->

                            if (!task.isSuccessful) {
                                //println("Login Failed with ${task.exception}")
                                _resetPasswordStatus.postValue(Resource.Success("Reset Failed with ${task.exception}"))
                            } else {
                                _resetPasswordStatus.postValue(Resource.Success("Reset Successful"))

                            }
                        }

                }

            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    _resetPasswordStatus.postValue(
                        Resource.Error(
                            "Failed with Error Code ${errorCode} ",
                            e.toString()
                        )
                    )
                } else {
                    _resetPasswordStatus.postValue(
                        Resource.Error(
                            "Failed with Exception ${e.message} ",
                            e.toString()
                        )
                    )
                }


            }
        }
    }

    /****
     *
     * Firebase Firestore
     */

    fun addUserInfoToFireStore(user: User) {
        viewModelScope.launch {
            var errorCode = -1

            try {

                firebaseUserId = auth?.currentUser?.uid.toString()
                Firebase.firestore.collection("users").document(firebaseUserId)
                    .set(user, SetOptions.merge()).addOnSuccessListener {
                        Log.d("TAG", "DocumentSnapshot successfully written!")
                    }


            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    _addExpenseStatus.postValue(
                        Resource.Error(
                            "Failed with Error Code ${errorCode} ",
                            e.toString()
                        )
                    )
                } else {
                    _addExpenseStatus.postValue(
                        Resource.Error(
                            "Failed with Exception ${e.message} ",
                            e.toString()
                        )
                    )
                }
            }
        }
    }

    fun retrieveUserInfoFromFireStore() {
        viewModelScope.launch {
            var errorCode = -1
            try {

                firebaseUserId = auth?.currentUser?.uid.toString()
                userCollectionReference?.document(firebaseUserId)
                    ?.addSnapshotListener { value, error ->
                        if (value != null){
                            val user = value.toObject<User>()
                            if (user != null){
                                _getUserStatus.postValue(user)
                            }
                        }
                    }


            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    //_getIncomeItem.postValue(Resource.Error(e.toString()))
                } else {
                    //_getIncomeItem.postValue(Resource.Error(e.toString()))
                }
            }
        }
    }

    fun addIncomeItemToFireStore(income: Income) {
        viewModelScope.launch {
            var errorCode = -1

            try {
                auth?.currentUser?.let {
                    //firebaseUserId = auth?.currentUser?.uid.toString()
                    incomeCollectionReference?.add(income)
                        ?.addOnSuccessListener { documentReference ->
                            Log.d(
                                "ViewModel",
                                "DocumentSnapshot written with ID: ${documentReference.id}"
                            )
                            _addIncomeStatus.postValue(Resource.Success("Success"))
                        }
                        ?.addOnFailureListener { e ->
                            Log.w("ViewModel", "Error adding document", e)
                            _addIncomeStatus.postValue(Resource.Error("Failed"))
                        }


                }

            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    _addIncomeStatus.postValue(
                        Resource.Error(
                            "Failed with Error Code ${errorCode} ",
                            e.toString()
                        )
                    )
                } else {
                    _addIncomeStatus.postValue(
                        Resource.Error(
                            "Failed with Exception ${e.message} ",
                            e.toString()
                        )
                    )
                }
            }
        }
    }

    fun retrieveIncomeItemFromFireStore() {
        viewModelScope.launch {
            var errorCode = -1
            var totalAmount = 0
            try {
                firebaseUserId = auth?.currentUser?.uid.toString()
                incomeCollectionReference?.whereEqualTo("uid", firebaseUserId)
                    ?.get()
                    ?.addOnSuccessListener { documents ->
                        //_getIncomeItem.postValue(Resource.Success(documents))
                        val incomeList = documents.toObjects(Income::class.java)
                        for (income in incomeList) {
                            Log.d("ViewModel", "Income $income")
                            val currentAmount = income.amount!!.toInt()

                            totalAmount += currentAmount
                        }
                        totalAmount
                        _totalBudgetStatus.postValue(Resource.Success(totalAmount.toString()))
                        _getIncomeItem.postValue(incomeList)
                    }
                    ?.addOnFailureListener { exception ->
                        Log.w("ViewModel", "Error getting documents: ", exception)
                    }

            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    //_getIncomeItem.postValue(Resource.Error(e.toString()))
                } else {
                    //_getIncomeItem.postValue(Resource.Error(e.toString()))
                }
            }
        }
    }

    fun addExpenseItemToFireStore(income: Income) {
        viewModelScope.launch {
            var errorCode = -1

            try {
                auth?.currentUser?.let {
                    //firebaseUserId = auth?.currentUser?.uid.toString()
                    expenseCollectionReference?.add(income)
                        ?.addOnSuccessListener { documentReference ->
                            Log.d(
                                "ViewModel",
                                "DocumentSnapshot written with ID: ${documentReference.id}"
                            )
                            _addExpenseStatus.postValue(Resource.Success("Success"))
                        }
                        ?.addOnFailureListener { e ->
                            Log.w("ViewModel", "Error adding document", e)
                            _addExpenseStatus.postValue(Resource.Error("Failed"))
                        }


                }

            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    _addExpenseStatus.postValue(
                        Resource.Error(
                            "Failed with Error Code ${errorCode} ",
                            e.toString()
                        )
                    )
                } else {
                    _addExpenseStatus.postValue(
                        Resource.Error(
                            "Failed with Exception ${e.message} ",
                            e.toString()
                        )
                    )
                }
            }
        }
    }

    fun retrieveExpenseItemFromFireStore() {
        viewModelScope.launch {
            var errorCode = -1
            var totalAmount = 0
            try {
                firebaseUserId = auth?.currentUser?.uid.toString()

                expenseCollectionReference?.whereEqualTo("uid", firebaseUserId)
                    ?.addSnapshotListener { snapshot, error ->
                        if (snapshot != null) {
                            val snap = snapshot.toObjects<Income>()
                            for (sna in snap){
                                sna.amount?.toInt()
                                val currentAmount = sna.amount!!.toInt()

                                totalAmount += currentAmount
                            }
                            _totalExpenseStatus.postValue(Resource.Success(totalAmount.toString()))

                            Log.d("ViewModel", "Total Amount ${totalAmount}")

                            _getExpenseItem.postValue(snap)
                        } else {
                            Log.w("ViewModel", "Error getting documents: ", error)
                        }
                    }

//                expenseCollectionReference?.whereEqualTo("uid", firebaseUserId)
//                    ?.get()
//                    ?.addOnSuccessListener { documents ->
//                        //_getIncomeItem.postValue(Resource.Success(documents))
//                        val expenseList = documents.toObjects(Income::class.java)
//                        for (expense in expenseList) {
//                            Log.d("ViewModel", "Income ${expense.amount.toString()}")
//                            val currentAmount = expense.amount!!.toInt()
//
//                            totalAmount += currentAmount
//
//                        }
//                        totalAmount
//
//                        _totalExpenseStatus.postValue(Resource.Success(totalAmount.toString()))
//
//                        Log.d("ViewModel", "Total Amount ${totalAmount}")
//
//                        _getExpenseItem.postValue(expenseList)
//                    }
//                    ?.addOnFailureListener { exception ->
//                        Log.w("ViewModel", "Error getting documents: ", exception)
//                    }

            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    //_getIncomeItem.postValue(Resource.Error(e.toString()))
                } else {
                    //_getIncomeItem.postValue(Resource.Error(e.toString()))
                }
            }
        }
    }

    fun retrieveRemainingBudgetAmount() {
        viewModelScope.launch {
            var errorCode = -1
            var totalAmount = 0
            var totalExpenseAmount = 0
            var remainingBudgetAmount = 0

            try {
                firebaseUserId = auth?.currentUser?.uid.toString()
                incomeCollectionReference?.whereEqualTo("uid", firebaseUserId)
                    ?.get()
                    ?.addOnSuccessListener { documents ->
                        //_getIncomeItem.postValue(Resource.Success(documents))
                        val incomeList = documents.toObjects(Income::class.java)
                        for (income in incomeList) {
                            Log.d("ViewModel", "Income $income")
                            val currentAmount = income.amount!!.toInt()
                            totalAmount += currentAmount
                        }
                        //
                        firebaseUserId = auth?.currentUser?.uid.toString()
                        expenseCollectionReference?.whereEqualTo("uid", firebaseUserId)
                            ?.get()
                            ?.addOnSuccessListener { documents2 ->
                                //_getIncomeItem.postValue(Resource.Success(documents))
                                val expenseList = documents2.toObjects(Income::class.java)
                                for (expense in expenseList) {
                                    Log.d("ViewModel", "Income ${expense.amount.toString()}")
                                    val currentAmount = expense.amount!!.toInt()

                                    totalExpenseAmount += currentAmount

                                }
                                remainingBudgetAmount = totalAmount - totalExpenseAmount
                                Log.d("ViewModel", "Remaining Amount $remainingBudgetAmount")
                                _remainingBudgetStatus.postValue(Resource.Success(remainingBudgetAmount.toString()))


                                Log.d("ViewModel", "Total Amount ${totalExpenseAmount}")

                            }
                    }
                    ?.addOnFailureListener { exception ->
                        Log.w("ViewModel", "Error getting documents: ", exception)
                    }



            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    //_getIncomeItem.postValue(Resource.Error(e.toString()))
                } else {
                    //_getIncomeItem.postValue(Resource.Error(e.toString()))
                }
            }
        }
    }

    fun retrieveTotalIncomeAmount(){
        viewModelScope.launch {
            var errorCode = -1
            var totalAllowanceAmount = 0
            var totalSalaryAmount = 0
            var totalPettyCashAmount = 0
            var totalBonusAmount = 0
            var totalOtherAmount = 0

            _totalIncomeAmountStatus.postValue(Resource.Loading())

            try {
                firebaseUserId = auth?.currentUser?.uid.toString()
                incomeCollectionReference?.whereEqualTo("uid", firebaseUserId)
                    ?.get()
                    ?.addOnSuccessListener { documents ->
                        val incomeList = documents.toObjects(Income::class.java)
                        for (income in  incomeList){
                            val incomeCategory = income.category
                            Log.i("ViewModel", income.category.toString())
                            when (incomeCategory) {
                                "Allowance" -> {
                                    val currentTotalAllowance = income.amount!!.toInt()
                                    totalAllowanceAmount += currentTotalAllowance

                                }
                                "Salary" ->{
                                    val currentTotalSalary = income.amount!!.toInt()
                                    totalSalaryAmount += currentTotalSalary
                                }
                                "Petty cash" ->{
                                    val currentPettyCashSalary = income.amount!!.toInt()
                                    totalPettyCashAmount += currentPettyCashSalary
                                }
                                "Bonus" ->{
                                    val currentBonusSalary = income.amount!!.toInt()
                                    totalBonusAmount += currentBonusSalary
                                }
                                else -> {
                                    val currentOtherSalary = income.amount!!.toInt()
                                    totalOtherAmount += currentOtherSalary
                                }
                            }
                            val totalIncomeAmount = TotalIncomeAmount(
                                totalAllowanceAmount,
                                totalSalaryAmount,
                                totalPettyCashAmount,
                                totalBonusAmount,
                                totalOtherAmount,
                            )

                            _totalIncomeAmountStatus.postValue(Resource.Success(totalIncomeAmount))
                        }



                    }
            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    _totalIncomeAmountStatus.postValue(Resource.Error(e.toString()))
                } else {
                    _totalIncomeAmountStatus.postValue(Resource.Error(e.toString()))
                }
            }

        }
    }

    fun retrieveTotalExpenseAmount(){
        viewModelScope.launch {
            var errorCode = -1
            var totalFoodAmount = 0
            var totalSocialLifeAmount = 0
            var totalTransportationAmount = 0
            var totalHouseholdAmount = 0
            var totalHealthAmount = 0
            var totalGiftAmount = 0
            var totalEducationAmount = 0
            var totalOtherAmount = 0

            _totalExpenseAmountStatus.postValue(Resource.Loading())

            try {
                firebaseUserId = auth?.currentUser?.uid.toString()
                expenseCollectionReference?.whereEqualTo("uid", firebaseUserId)
                    ?.get()
                    ?.addOnSuccessListener { documents ->
                        val expenseList = documents.toObjects(Income::class.java)
                        for (expense in  expenseList){
                            val expenseCategory = expense.category
                            Log.i("ViewModel", expense.category.toString())
                            when (expenseCategory) {
                                "Food" -> {
                                    val currentTotalFood = expense.amount!!.toInt()
                                    totalFoodAmount += currentTotalFood

                                }
                                "Social Life" ->{
                                    val currentTotalSocialLife = expense.amount!!.toInt()
                                    totalSocialLifeAmount += currentTotalSocialLife
                                }
                                "Transportation" ->{
                                    val currentTotalTransport = expense.amount!!.toInt()
                                    totalTransportationAmount += currentTotalTransport
                                }
                                "Household" ->{
                                    val currentHouseholdAmount = expense.amount!!.toInt()
                                    totalHouseholdAmount += currentHouseholdAmount
                                }
                                "Health" ->{
                                    val currentHealthAmount = expense.amount!!.toInt()
                                    totalHealthAmount += currentHealthAmount
                                }
                                "Gift" ->{
                                    val currentGiftAmount = expense.amount!!.toInt()
                                    totalGiftAmount += currentGiftAmount
                                }
                                "Education" ->{
                                    val currentEducationAmount = expense.amount!!.toInt()
                                    totalEducationAmount += currentEducationAmount
                                }
                                else -> {

                                    val currentOtherAmount = expense.amount!!.toInt()
                                    totalOtherAmount += currentOtherAmount
                                }
                            }
                            val totalExpenseAmount = TotalExpenseAmount(
                                totalFoodAmount,
                                totalSocialLifeAmount,
                                totalTransportationAmount,
                                totalHouseholdAmount,
                                totalHealthAmount,
                                totalGiftAmount,
                                totalEducationAmount,
                                totalOtherAmount
                            )

                            _totalExpenseAmountStatus.postValue(Resource.Success(totalExpenseAmount))
                        }



                    }
            } catch (e: Exception) {
                e.printStackTrace()
                if (errorCode != -1) {
                    _totalExpenseAmountStatus.postValue(Resource.Error(e.toString()))
                } else {
                    _totalExpenseAmountStatus.postValue(Resource.Error(e.toString()))
                }
            }

        }
    }

}