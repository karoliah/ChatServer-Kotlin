import kotlinx.serialization.Serializable

@Serializable
class ChatMessage(val username: String, val message: String, var currentTime:String, val command : String) {

    fun getMessageInOneLine(): String {                                //Luodaan funktio viestin ulkoasulle
        return "${username} : ${message} : ${currentTime}"
    }
}
