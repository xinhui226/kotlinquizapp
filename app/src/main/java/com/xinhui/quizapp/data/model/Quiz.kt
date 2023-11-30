package com.xinhui.quizapp.data.model

data class Quiz(
    val id:String? = null,
    val name: String,
    val date: String,
    val titles:List<String>,
    val options: List<String>,
    val answers: List<String>,
    val seconds: List<Long>,
    val groups: List<String>,
    val createdBy: String? = null,
    val isPublished :Boolean
){
    fun toHash():HashMap<String,Any>{
        return hashMapOf(
            "name" to name,
            "date" to date,
            "titles" to titles,
            "options" to options,
            "answers" to answers,
            "seconds" to seconds,
            "groups" to groups,
            "createdBy" to createdBy.toString(),
            "isPublished" to isPublished
        )
    }

    fun toQuestions():List<Question>{
        val questions = mutableListOf<Question>()
        titles.mapIndexed { i, s ->
            questions.add(
                Question(
                    titles = s,
                    options = listOf(options[i*4],options[i*4+1],options[i*4+2],options[i*4+3]),
                    answers = answers[i],
                    seconds = seconds[i])
            )
        }
        return questions
    }

    companion object{
        fun fromHash(hash:Map<String,Any>):Quiz{
            return Quiz(
                id = hash["id"].toString(),
                name = hash["name"].toString(),
                date = hash["date"].toString(),
                titles = (hash["titles"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                options = (hash["options"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                answers = (hash["answers"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                seconds = (hash["seconds"] as ArrayList<*>?)?.map {
                    it.toString().toLong()
                }?.toList() ?: emptyList(),
                groups = (hash["groups"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                createdBy = hash["createdBy"].toString(),
                isPublished = hash["isPublished"].toString().toBoolean()
            )
        }

    }
}
