package uz.ultimatedevs.hrmdemo1.domain

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import uz.ultimatedevs.hrmdemo1.data.ResultData
import uz.ultimatedevs.hrmdemo1.data.User
import uz.ultimatedevs.hrmdemo1.data.WorkHour

class Repository {

    private val db = Firebase.firestore

    fun getUsers() = flow<ResultData<List<User>>> {
        val users = mutableListOf<User>()
        db.collection("users").get().await().forEach {
            users.add(
                User(
                    it.id,
                    it.getString("name") ?: "",
                    it.getString("login") ?: "",
                    it.getString("password") ?: "",
                    it.getString("profession") ?: "",
                )
            )
        }
        Log.d("TTT", users.toString())
        emit(ResultData.Success(users))
    }.catch {
        Log.d("TTT", it.message.toString())
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    fun addUser(user: User) = flow<ResultData<User>> {
        val hashMap = hashMapOf(
            "name" to user.name,
            "login" to user.login,
            "password" to user.password,
            "profession" to user.profession
        )

        db.collection("users").document().set(hashMap).await()
        emit(ResultData.Success(user))
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    fun updateUser(user: User) = flow<ResultData<User>> {
        val hashMap = hashMapOf(
            "name" to user.name,
            "login" to user.login,
            "password" to user.password,
            "profession" to user.profession
        )
        Log.d("TTT", user.toString())
        Log.d("TTT", user.name)
        db.collection("users").document(user.id).set(hashMap).await()
        emit(ResultData.Success(user))
    }.catch {
        Log.d("TTT", it.message.toString())
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    fun deleteUser(userID: String) = flow<ResultData<Unit>> {
        db.collection("users").document(userID).delete().await()
        emit(ResultData.Success(Unit))
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    fun getUserWorkHistory(userID: String) = flow<ResultData<List<WorkHour>>> {

        val workHours = mutableListOf<WorkHour>()

        db.collection("users").document(userID).collection("work_history").get().await().forEach {
            workHours.add(
                WorkHour(
                    it.id,
                    it.getString("date") ?: "",
                    it.getString("start_hour") ?: "",
                    it.getString("end_hour") ?: "",
                )
            )
        }
        emit(ResultData.Success(workHours))
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

}