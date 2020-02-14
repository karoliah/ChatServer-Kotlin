import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import kotlinx.serialization.parseList
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*


class ChatConnector(input: InputStream, output: OutputStream, client: Socket) :Runnable, ChatHistoryObserver {

    private val client: Socket = client
    val scanner = Scanner(input)
    val printer = PrintStream(output, true)
    var quit = false
    var userNameSet = false
    var userName = " "

    override fun newMessage(message: ChatMessage) {             //Overridetaan metodi newMessage, jotta se printtaa viestit muille observereille
        if(message.username != userName) {
            printer.println("${message.username} : ${message.message} : ${message.currentTime}")    //Viestin ulkoasu joka printataan konsolille/näytölle
        }
    }

    override fun run() {
        ChatHistory.registerObserver(this)              //Registeröidään uusi observeri(käyttäjä)
        printer.println("Tervetuloa chattiin!")


        do {
            while(!userNameSet) {                               //Loopataan niin kauan kunnes nimimerkki on syötetty
                userName = " "
                printer.println("Luo nimimerkki painamalla ensin enter:")
                val enter = scanner.nextLine()
                userName = scanner.nextLine()

                if (Users.addUser(userName) && userName!=" ") { //Lisätään nimimerkki käyttäjälle, jos samannimistä käyttäjää ei löydy chatista.
                    userNameSet = true
                    printer.println("Hello $userName")          //Nimimerkin lisääminen onnistui, tervehditään uutta käyttäjää nimellä
                    printer.println("Kirjoita viesti (syöttämällä help saat komennot näkyviin):")

                } else {
                    userNameSet = false
                    printer.println("Nimimerkki on jo olemassa, valitse uusi.")
                }
            }

            var input = " "
            input = scanner.nextLine()

            val pattern = "HH:mm"                               //Ajan muuttaminen oikeaan muotoon
            val simpleDateFormat = SimpleDateFormat(pattern)
            val date = simpleDateFormat.format(Date())

            val chatMessage = ChatMessage(userName, input, date.toString(), input)
            //val chatMessage: ChatMessage = Json.parse(ChatMessage.serializer(), input) //json


            if (userNameSet) {

                when (chatMessage.command) {

                    "users" -> printUserList(Users.getUserList())           //Komento jolla printataan käyttäjien lista
                    "messagehistory" -> {
                        printer.println(printMessageHistory()) }            //Komento jolla printataan viestihistoria
                    "top3" -> printer.println(TopChatter.toString())   //Komento jolla printataan topChattereiden lista
                    "quit" -> {                                             //Komento jolla poistutaan chatista
                        printer.println("$userName leave the Chat")
                        shutdown()
                    }
                    "help" -> {
                        printer.println("Komennot:")
                        printer.println("users - näyttää aktiiviset käyttäjät")
                        printer.println("messagehistory - näyttää viestihistorian")
                        printer.println("top3 - tulostaa 3 aktiivisinta chattaajaa")
                        printer.println("quit - lopettaa chatin")
                    }

                    else -> {                                               //Jos mitään komentoa ylläolevista ei käytetä, input tulkitaan viestiksi
                        ChatHistory.insert(chatMessage)                     //Lisätään viesti ChatHistoryyn
                        ChatHistory.notifyObservers(chatMessage)            //Lähetetään viesti observereille
                    }
                }
            } else println("Nimimerkkiä ei ole syötetty, syötä nimimerkki")

        } while (!quit)
    }

    fun printMessageHistory() {                                         //Funktio koko chathistorian tulostamiseen
        for(message in ChatHistory.returnMessageList())
            printer.println(message.getMessageInOneLine())
    }

    fun printUserList(users: List<String>){                             //Funktio käyttäjien tulostamiseen
        for(n in users){                                        //Iteroidaan kaikki käyttäjät ja tulostetaan omille riveille
        printer.println(n)
        }
    }

    private fun shutdown() {                                            //Funktio jota kutsutaan käyttäjän sulkiessa sovellus
        client.close()
        Users.removeUser(userName)
        TopChatter.deleteTopChatter(userName)
        quit = true
    }
}
