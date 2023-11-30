package com.xinhui.quizapp.data.model

data class StudentGroup(
    val id:String? = null,
    val name:String,
    val createdBy: String,
){
    fun toHash():HashMap<String,Any>{
        return hashMapOf(
            "name" to name,
            "createdBy" to createdBy,
        )
    }

    companion object{
        fun fromHash(hash:Map<String,Any>):StudentGroup{
//            Log.d("debugging", "fromHash: ${hash["quizzes"] as ArrayList<*>?}")
            return StudentGroup(
                id = hash["id"].toString(),
                name = hash["name"].toString(),
                createdBy = hash["createdBy"].toString(),
            )
        }
    }
}
