package com.xinhui.quizapp.data.model

data class Score(
    val quizScore: Int = 0,
    val correct: Int = 0,
    val wrong: Int = 0,
    val skip: Int = 0,
    val userId: String,
    val userName: String,
    val quizId: String,
    val userGroups: List<String>
){
    fun toHash():HashMap<String,Any>{
        return hashMapOf(
            "quizScore" to quizScore,
            "correct" to correct,
            "wrong" to wrong,
            "skip" to skip,
            "userId" to userId,
            "userName" to userName,
            "quizId" to quizId,
            "userGroups" to userGroups,
        )
    }

    companion object{
        fun fromHash(hash:Map<String,Any>):Score{
            return Score(
                quizScore = hash["quizScore"].toString().toInt(),
                correct = hash["correct"].toString().toInt(),
                wrong = hash["wrong"].toString().toInt(),
                skip = hash["skip"].toString().toInt(),
                userId = hash["userId"].toString(),
                userName = hash["userName"].toString(),
                quizId = hash["quizId"].toString(),
                userGroups = (hash["userGroups"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
            )
        }
    }
}