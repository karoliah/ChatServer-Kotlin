import java.net.ServerSocket

class ChatServer {

    fun serve() {
        val serverSocket = ServerSocket(3001, 3)                               //Luodaan socket
        try {

            println("We have port " + serverSocket.localPort)

            while (true) {
                val s = serverSocket.accept()                                       //Kutsutaan yhteyksiä
                println("new connection " + s.inetAddress.hostAddress + " " + s.port)
                val t = Thread(ChatConnector(s.getInputStream(), s.getOutputStream(), client = s))    //Luodaan säie Chatconnectorille
                t.start()                                                                   //Käynnistetään säie, joka lähtee ajamaan run ChatConnector luokan run() metodia
                val chatConsoleThread = Thread(ChatConsole)                                 //Luodaan ChatConsolille säie
                chatConsoleThread.start()
                val topChatter = Thread(TopChatter)                                         //Luodaan TopChatterille säie
                topChatter.start()                                                          //Käynnistetään säie
            }

        } catch (e: Exception) {
            println("Got exception: ${e.message}")
        } finally {
            println("All serving done.")
        }
    }
}