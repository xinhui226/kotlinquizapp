package com.xinhui.quizapp.data.model

data class User (
    val id:String? = null,
    val name: String,
    val email: String,
    val group:List<String>
){
    fun toHash():HashMap<String,Any>{
        return hashMapOf(
            "name" to name,
            "email" to email,
            "group" to group
        )
    }

    companion object{
        fun fromHash(hash:Map<String,Any>):User{
            return User(
                id = hash["id"].toString(),
                name = hash["name"].toString(),
                email = hash["email"].toString(),
                group = (hash["group"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
            )
        }
    }
}