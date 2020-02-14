object TopChatter:Runnable,ChatHistoryObserver {
    val topChatter = mutableMapOf<String, Int>()


    override fun run() {
        ChatHistory.registerObserver(this)
    }


    override fun newMessage(message: ChatMessage) {
        val user = message.username

        if(topChatter.containsKey(user)) {
            var value = topChatter.getValue(user)          //Lisätään topChatter mappiin value kun tietty käyttäjä syöttää viestin
            value += 1
            topChatter.set(user, value)
        }
        else {
            topChatter.put(user,1)                            //Jos käyttäjää ei löydy mapista, lisätään käyttäjä mappiin
        }

    }

    fun deleteTopChatter(nickname: String) {
        topChatter.remove(nickname)
    }

    override fun toString(): String {                       //Tulostetaan top 3 chattaajat
        var topChatters = "Top 3 chattaajat: "

        var sorted = topChatter.toList().sortedByDescending { (_ , value) -> value }            //Järjestetään mappi viestimäärän perusteella
        var i = 1
        for (chatter in sorted) {            //Iteroidaan mapin läpi ja lopetetaan kun i pääsee neljään. Tällöin 3 parasta chattaajaa tulostetaan.
            topChatters += "$i. $chatter "
            i++
            if(i==4) {
                break;
            }
        }
        return topChatters
    }
}