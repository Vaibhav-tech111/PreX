package com.codez.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

// डेटा क्लास हमारे चैट संदेशों को रखने के लिए
data class Message(val text: String, val isFromUser: Boolean)

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var queryEditText: EditText
    private lateinit var sendButton: ImageButton

    private val messages = mutableListOf<Message>()
    private lateinit var messageAdapter: MessageAdapter
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI घटकों को इनिशियलाइज़ करें
        recyclerView = findViewById(R.id.recyclerView)
        queryEditText = findViewById(R.id.queryEditText)
        sendButton = findViewById(R.id.sendButton)

        // RecyclerView सेट करें
        messageAdapter = MessageAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter

        // आरंभिक स्वागत संदेश जोड़ें
        addMessage("Welcome to PreX! Your AI-powered testbook. How can I help you learn today?", false)

        // सेंड बटन के लिए क्लिक लिसनर
        sendButton.setOnClickListener {
            val query = queryEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                // यूजर का प्रश्न दिखाएं और सर्च करें
                addMessage(query, true)
                performSearch(query)
                queryEditText.text.clear()
            }
        }
    }

    private fun addMessage(text: String, isFromUser: Boolean) {
        runOnUiThread {
            messages.add(Message(text, isFromUser))
            messageAdapter.notifyItemInserted(messages.size - 1)
            recyclerView.scrollToPosition(messages.size - 1)
        }
    }

    private fun performSearch(query: String) {
        // एक लोडिंग संदेश दिखाएं
        addMessage("Thinking...", false)

        // DuckDuckGo API के लिए URL बनाएं
        val url = "https://api.duckduckgo.com/?q=$query&format=json&pretty=1"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // त्रुटि होने पर संदेश दिखाएं
                updateLastMessage("Sorry, I couldn't find an answer. Please check your internet connection.")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    try {
                        val json = JSONObject(it)
                        val answerText = json.optString("AbstractText", "No direct answer found. Try rephrasing your question.")
                        val sourceUrl = json.optString("AbstractURL", "")
                        
                        var formattedAnswer = if (answerText.isNotBlank()) answerText else "I couldn't find a direct summary. Here are some related topics that might help."
                        
                        // यदि स्रोत उपलब्ध है तो उसे जोड़ें
                        if (sourceUrl.isNotBlank()) {
                            formattedAnswer += "\n\nSource: $sourceUrl"
                        }

                        // अंतिम "Thinking..." संदेश को वास्तविक उत्तर से अपडेट करें
                        updateLastMessage(formattedAnswer)

                    } catch (e: Exception) {
                        updateLastMessage("Failed to parse the answer. The topic might be too complex.")
                    }
                }
            }
        })
    }
    
    // अंतिम संदेश को अपडेट करने के लिए हेल्पर फ़ंक्शन
    private fun updateLastMessage(newText: String) {
         runOnUiThread {
            if (messages.isNotEmpty()) {
                messages[messages.size - 1] = Message(newText, false) // अंतिम संदेश को बदलें
                messageAdapter.notifyItemChanged(messages.size - 1)
            }
        }
    }
}


// RecyclerView के लिए एडाप्टर
class MessageAdapter(private val messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_BOT = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isFromUser) VIEW_TYPE_USER else VIEW_TYPE_BOT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_USER) R.layout.item_message_user else R.layout.item_message_bot
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.textView.text = messages[position].text
    }

    override fun getItemCount() = messages.size

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.messageTextView)
    }
}