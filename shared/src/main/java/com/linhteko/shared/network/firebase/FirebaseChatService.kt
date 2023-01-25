package com.linhteko.shared.network.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.data.vos.GroupVO
import com.linhteko.shared.network.ChatService
import com.linhteko.shared.utils.Alerts
import com.linhteko.shared.utils.FirebaseApiConstants.CHAT_CONTACTS
import com.linhteko.shared.utils.FirebaseApiConstants.CHAT_GROUPS

object FirebaseChatService : ChatService {

    private val database = Firebase.database
    private val firestore = Firebase.firestore

    override fun getConversations(
        currentUser: String,
        otherUser: String,
        onSuccess: (List<ConversationVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        database.reference.child(CHAT_CONTACTS)
            .child(currentUser)
            .child(otherUser)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val conversations =
                        snapshot.children.mapNotNull { it.getValue(ConversationVO::class.java) }
                    onSuccess(conversations)
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.message)
                }
            })
    }

    override fun sendMessage(
        currentUser: String,
        otherUser: String,
        message: ConversationVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        database.reference.child(CHAT_CONTACTS)
            .child(currentUser)
            .child(otherUser)
            .child(message.id.toString())
            .setValue(message)
            .continueWithTask {
                database.reference.child(CHAT_CONTACTS)
                    .child(otherUser)
                    .child(currentUser)
                    .child(message.id.toString())
                    .setValue(message)
            }
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { onFailure(it.message ?: Alerts.ALERT_ERR) }
    }

    override fun createGroup(groupVO: GroupVO, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        database.reference.child(CHAT_GROUPS)
            .child(groupVO.id.toString())
            .setValue(groupVO)
            .addOnSuccessListener { onSuccess.invoke() }
            .addOnFailureListener { onFailure.invoke(it.message ?: Alerts.ALERT_ERR) }
    }

    override fun getGroups(
        userId: String,
        onSuccess: (List<GroupVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        database.reference.child(CHAT_GROUPS)
            .get()
            .addOnSuccessListener {

                val items = it.children
                    .mapNotNull { item -> item.getValue(GroupVO::class.java) }
                    .filter { members -> members.members.contains(userId) }
                onSuccess(items)
            }
            .addOnFailureListener { onFailure(it.message ?: Alerts.ALERT_ERR) }
    }

    override fun getGroup(
        groupId: String,
        onSuccess: (GroupVO?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        database.reference.child(CHAT_GROUPS)
            .child(groupId)
            .get()
            .addOnSuccessListener {
                onSuccess(it.getValue(GroupVO::class.java))
            }
            .addOnFailureListener { onFailure(it.localizedMessage ?: Alerts.ALERT_ERR) }
    }

    override fun sendGroupMessage(
        groupId: String,
        conversationVO: ConversationVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        database.reference.child(CHAT_GROUPS)
            .child(groupId)
            .child("messages")
            .updateChildren(
                mapOf(
                    conversationVO.id.toString() to conversationVO
                )
            )
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onFailure(it.localizedMessage ?: Alerts.ALERT_ERR)
            }

    }

    override fun getGroupConversations(
        groupId: String,
        onSuccess: (List<ConversationVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        database.reference.child(CHAT_GROUPS)
            .child(groupId)
            .child("messages")
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val items =
                            snapshot.children.mapNotNull { it.getValue(ConversationVO::class.java) }
                        onSuccess(items)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onFailure(error.message ?: Alerts.ALERT_ERR)
                    }
                }
            )
    }

    override fun getChatContacts(
        uid: String,
        onSuccess: (List<String>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        database.reference.child(CHAT_CONTACTS)
            .child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userIds = snapshot.children.mapNotNull { it.key }
                    onSuccess(userIds)
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.message)
                }
            })
    }

    override fun getLastConversation(
        currentUser: String,
        otherUser: String,
        onSuccess: (ConversationVO?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        database.reference
            .child(CHAT_CONTACTS)
            .child(currentUser)
            .child(otherUser)
            .get()
            .addOnSuccessListener {
                val vo = it.children.reversed().take(1)
                    .mapNotNull { doc -> doc.getValue(ConversationVO::class.java) }
                if (vo.isNotEmpty())
                    onSuccess(vo[0])
            }
            .addOnFailureListener { onFailure(it.localizedMessage ?: Alerts.ALERT_ERR) }
    }
}