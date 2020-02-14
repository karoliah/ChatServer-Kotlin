import java.time.LocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse

object ChatHistory: ChatHistoryObservable {
    val observers = HashSet<ChatHistoryObserver>()
    val messages : MutableList<ChatMessage> = mutableListOf()
    val messagesString : MutableList<String> = mutableListOf()

    fun insert(message: ChatMessage) {                      //Funktio jolla isätään viesti ChatHistory-listaan (messages)
        messages.add(message)
    }

    override fun registerObserver(observer: ChatHistoryObserver) {
        observers.add(observer)
    }

    override fun deregisterObserver(observer: ChatHistoryObserver) {
        observers.remove(observer)
    }

    override fun notifyObservers(message: ChatMessage) {     //Iteroidaan kaikki observerit läpi ja lähetetään viestit"
        for (observer in observers){
            observer.newMessage(message)
        }
    }

    fun returnMessageList(): List<ChatMessage> {                //Funktio jolla palautetaan viestilista
        return messages.toList()
    }

    override fun toString(): String {
        var allMessages = ""

        for(n in messages) {
            allMessages += "/r $n"
        }
        return allMessages
    }

}