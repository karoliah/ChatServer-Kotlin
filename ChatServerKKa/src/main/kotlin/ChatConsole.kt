object ChatConsole :Runnable,ChatHistoryObserver {

    override fun run() {
        ChatHistory.registerObserver(this)
    }

    override fun newMessage(message: ChatMessage) {             //Printataan viesti konsolin ruudulle
        println(message)
    }
}