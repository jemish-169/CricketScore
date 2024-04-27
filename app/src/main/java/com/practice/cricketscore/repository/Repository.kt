package com.practice.cricketscore.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.practice.cricketscore.models.Match
import com.practice.cricketscore.utils.Constants

class Repository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val matchesRef = database.getReference("matches")
    val matchLiveData = MutableLiveData<Match?>()

    var match = Constants.INIT_MATCH
    var editMatch = Match()


    fun getMatches(callback: DataCallback) {
        val matchList = ArrayList<Match>()
        matchesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { matchSnapshot ->
                    val match = matchSnapshot.getValue(Match::class.java)
                    match?.let {
                        matchList.add(it)
                    }
                }
                callback.onDataLoaded(matchList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onError(error.message)
            }
        })
    }

    fun updateMatch(match: Match) {
        val matchKey = match.id
        Log.d("TAG", "match - key\t$match")
        matchKey.let { it ->
            match.id = matchKey
            matchesRef.child(it).setValue(match)
                .addOnSuccessListener {
                    Log.d("TAG", "it\t$it")
                }
                .addOnFailureListener {
                    Log.d("TAG", "msg\t$it")
                }
        }

    }

    fun getOneMatch() {
        matchesRef.child(match.id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val matchF = snapshot.getValue(Match::class.java)
                if (matchF == null) {
                    Log.d("TAG", "No match found!")
                    match = Match()
                } else
                    matchLiveData.value = matchF
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "some Error in finding match!")
            }
        })

    }

    interface DataCallback {
        fun onDataLoaded(dataList: ArrayList<Match>)
        fun onError(errorMessage: String?)
    }

}