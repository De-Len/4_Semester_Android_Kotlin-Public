package com.example.individualproject_4semester.data.remote.response.chatbotsresponse

data class ChatBotsResult(
    val output: List<ChatBotsOutputMessage>
)

data class ChatBotsOutputMessage(
    val content: List<ChatBotsOutputText>
)

data class ChatBotsOutputText(
    val text: String
)