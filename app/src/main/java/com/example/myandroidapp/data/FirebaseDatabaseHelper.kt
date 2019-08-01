package com.example.myandroidapp.data

import android.util.Log
import com.example.myandroidapp.utils.Constants.TODO_PATH
import com.example.myandroidapp.utils.Logg
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseDatabaseHelper {

    @set:Inject
    public var db: FirebaseFirestore? = null

    var TAG = "FDHelper"


    fun insertTodo(todo: Todo) {
        Logg.e(TAG, "insert todo : " )
        // Add a new document with a generated ID
        db?.collection(TODO_PATH)?.document(todo._Id.toString())
            ?.set(todo)
            ?.addOnSuccessListener { documentReference ->
                Log.e(TAG, "DocumentSnapshot added with ID: ${documentReference}")
            }
            ?.addOnFailureListener { e ->
                Log.e(TAG, "Error adding document", e)
            }
    }

    fun getTodos() {
        db?.collection(TODO_PATH)
            ?.get()
            ?.addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            ?.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}