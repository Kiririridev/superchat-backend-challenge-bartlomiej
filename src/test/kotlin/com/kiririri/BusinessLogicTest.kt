package com.kiririri

import com.kiririri.superchat.controller.dto.ExternalMessage
import com.kiririri.superchat.core.ChatUser
import com.kiririri.superchat.core.Contact
import com.kiririri.superchat.core.Message
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.`when`
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.isEqualTo

@QuarkusTest
class BusinessLogicTest {

    @Nested
    inner class Users {
        @Test
        fun `should create user`() {
            val username = "someUsername"
            val externalUsername = "someExternalUsername"

            val createdUser = createUser(username, externalUsername)

            val responseString = listUsers()

            expectThat(responseString).contains("\"id\": ${createdUser.id!!}")
        }
    }

    @Nested
    inner class Contacts {

        @Test
        fun `should create two users and add contact`() {
            val userA = createUser("user1", "user1")
            val userB = createUser("user2", "user2")

            val contact = createContact(contactOwnerId = userA.id!!, contactTargetId = userB.id!!)

            val responseString = listContacts(userA.id!!)

            expectThat(responseString).contains("\"id\": ${contact.id!!}")
        }
    }

    @Nested
    inner class SendMessage {

        @Test
        fun `should send message using contact id`() {
            val userA = createUser("user1", "user1")
            val userB = createUser("user2", "user2")

            val contact = createContact(userA.id!!, userB.id!!)
            val message = sendMessageToContact(contact.id!!, "messageContent")

            val responseString = listMessages(userA.id!!)

            expectThat(responseString).contains("\"id\": ${message.id!!}")
        }
    }

    @Nested
    inner class ReceiveExternalMessage {

        @Test
        fun `should receive external message`() {
            val user = createUser("userName", "externalUsername")
            val senderExternalName = "externalSender"

            val externalMessage = sendExternalMessage("externalSender", user.externalUsername!!)

            val userListResponse = listUsers()
            expectThat(userListResponse).contains("\"externalUsername\": \"${senderExternalName}\"")

            val messageListResponse = listMessages(user.id!!)
            expectThat(messageListResponse).contains("\"id\": ${externalMessage.id!!}")
        }
    }

    @Nested
    inner class Placeholders {

        @Test
        fun `should return message with placeholders resolved`() {
            val userA = createUser("john", "johnExternal")
            val userB = createUser("marcus", "marcusExternal")
            val sentMessage = "Hello [\$user=${userB.username!!}]. The current price of bitcoin is [\$btc]"
            val expectedMessage =
                "Hello ${userB}. The current price of bitcoin is 28486.43 EUR"

            val contact = createContact(userA.id!!, userB.id!!)
            val message = sendMessageToContact(contact.id!!, sentMessage)

            val messagesResponseString = listMessages(userA.id!!)

            expectThat(message.messageContent!!.plainMessageContent).isEqualTo(sentMessage)
            expectThat(messagesResponseString).contains(expectedMessage)
        }
    }

    private fun sendExternalMessage(senderExternalName: String, receiverExternalName: String): Message {

        val externalMessage = ExternalMessage(senderExternalName, receiverExternalName, "externalMessageContent")
        val response = given()
            .body(externalMessage)
            .`when`()
            .header("content-type", "application/json")
            .post("external/receiveMessage")

        expectThat(response.statusCode).isEqualTo(200)
        return response.body.`as`(Message::class.java)
    }

    private fun createContact(contactOwnerId: Long, contactTargetId: Long): Contact {
        val requestBody = mapOf(
            "contactOwnerId" to contactOwnerId,
            "contactTargetId" to contactTargetId,
            "contactName" to "someContactName",
            "contactEmail" to "someContactEmail"
        )
        val response = given()
            .`when`()
            .body(requestBody)
            .header("Content-Type", "application/json")
            .post("/contact/add")

        expectThat(response.statusCode).isEqualTo(200)
        return response.body.`as`(Contact::class.java)
    }

    private fun createUser(username: String, externalUsername: String): ChatUser {
        val response = `when`()
            .post("/user/add?username=$username&externalUsername=$externalUsername")

        expectThat(response.statusCode).isEqualTo(200)
        return response.body.`as`(ChatUser::class.java)
    }

    private fun listUsers(): String {
        val response = `when`()
            .get("user/all")

        expectThat(response.statusCode).isEqualTo(200)
        return response.body.asPrettyString()
    }

    private fun listMessages(userId: Long): String {
        val response = `when`()
            .get("message/listByUser?userId=$userId")

        expectThat(response.statusCode).isEqualTo(200)
        return response.body.asPrettyString()
    }

    private fun sendMessageToContact(contactId: Long, messageContent: String): Message {
        val requestBody = mapOf(
            "contactId" to contactId,
            "messageContent" to messageContent
        )

        val response = given()
            .`when`()
            .body(requestBody)
            .header("content-type", "application/json")
            .put("message/send")

        expectThat(response.statusCode).isEqualTo(200)
        return response.body.`as`(Message::class.java)
    }

    private fun listContacts(contactOwnerId: Long): String {
        val response = `when`()
            .get("/contact/listByOwner?contactOwnerId=$contactOwnerId")

        expectThat(response.statusCode).isEqualTo(200)
        return response.body.asPrettyString()
    }
}