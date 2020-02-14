import java.util.HashSet

object Users {
    val users : HashSet<String> = hashSetOf()

    fun addUser(nickname: String = ""): Boolean {
        if(users.contains(nickname)) {                          //Tarkistetaan, että käyttäjää ei ole olemassa ja lisätään listalle.
            println("Asetus ei onnistunut")
            return false
        } else {
            users.add(nickname)
            println("Nimimerkki asetettu")
            return true
        }
    }

    fun getUserList(): List<String> {
        return users.toList()
    }

    fun removeUser(nickname: String) {
        users.remove(nickname)
        println("Käyttäjä $nickname on poistunut chatista")
    }

    override fun toString():String {
        var listOfUsers = "Lista käyttäjistä: "

        for(i in users) {
            listOfUsers += "\r$i"
        }
        return listOfUsers
    }

}