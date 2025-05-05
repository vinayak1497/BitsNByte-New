// Replace your existing imports and class declaration
package com.example.signuploginrealtime;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.speech.tts.TextToSpeech;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.TextPart;

import java.util.ArrayList;
import java.util.List;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class AiChatBotActivity extends AppCompatActivity implements ChatAdapter.OnItemClickListener {

    private EditText etPrompt;
    private ImageButton btnSend;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages = new ArrayList<>();

    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final String API_KEY = "YOUR_API_KEY_HERE"; // Since, posting this on github, i had removed the api, you can paste it here your api key and it would run perfectly!

    private TextToSpeech tts;
    private boolean isSpeaking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chatbot);

        etPrompt = findViewById(R.id.etPrompt);
        btnSend = findViewById(R.id.btnSend);
        recyclerView = findViewById(R.id.recyclerView);

        chatAdapter = new ChatAdapter(chatMessages, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        btnSend.setOnClickListener(v -> {
            String userInput = etPrompt.getText().toString().trim();
            if (!userInput.isEmpty()) {
                if (isFoodRelated(userInput)) {
                    addMessage(userInput, true);
                    generateText(userInput);
                } else {
                    addMessage("❌ I can only answer food-related questions. Try asking about recipes, nutrition, or Indian dishes!", false);
                }
                etPrompt.setText("");
            }
        });

        tts = new TextToSpeech(this, status -> {
            if(status != TextToSpeech.SUCCESS){
                Log.e("TTS", "Initialization failed!");
            }
        });
    }

    private void speakOut(String text) {
        String processedText = text.replaceAll("[\\p{Punct}]", "");
        tts.speak(processedText, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("AI Response", text);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onListenClick(int position) {
        ChatMessage message = chatMessages.get(position);
        if (isSpeaking) {
            tts.stop();
            isSpeaking = false;
        } else {
            speakOut(message.getMessage());
            isSpeaking = true;
        }
    }

    @Override
    public void onCopyClick(int position) {
        ChatMessage message = chatMessages.get(position);
        copyToClipboard(message.getMessage());
        Toast.makeText(AiChatBotActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    private void generateText(String prompt) {
        GenerativeModel model = new GenerativeModel(MODEL_NAME, API_KEY);

        String systemPrompt = "You are a friendly food assistant. Your job is to answer only food-related questions, like recipes, cooking tips, nutrition facts, Indian dishes, street food, and healthy eating. Use short, simple sentences and explain in human, easy-to-understand Hindi-English (Hinglish) if needed. If the question is not food-related, reply with 'Sorry, I can only help with food topics.'";

        String finalPrompt = systemPrompt + "\nUser: " + prompt;

        model.generateContent(finalPrompt, new Continuation<GenerateContentResponse>() {
            @Override
            public void resumeWith(Object result) {
                if (result instanceof GenerateContentResponse) {
                    Content content = ((GenerateContentResponse) result).getCandidates().get(0).getContent();
                    if (content != null) {
                        String responseText = ((TextPart) content.getParts().get(0)).getText();

                        if (isFoodResponse(responseText) && !isHallucinated(responseText)) {
                            runOnUiThread(() -> addMessage(responseText, false));
                        } else {
                            runOnUiThread(() -> addMessage("⚠️ I'm not sure about this. Please ask something related to food.", false));
                        }
                    }
                } else if (result instanceof Throwable) {
                    Log.e("GeminiAPI", "Error generating response", (Throwable) result);
                }
            }

            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }
        });
    }

    private void addMessage(String message, boolean isUser) {
        chatMessages.add(new ChatMessage(message, isUser));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        recyclerView.scrollToPosition(chatMessages.size() - 1);

        if (isSpeaking) {
            tts.stop();
            isSpeaking = false;
        }
    }

    // ✅ Detects if user's question is about food
    private boolean isFoodRelated(String query) {
        String[] foodKeywords = {
                "food", "recipe", "nutrition", "calories", "protein", "carbs", "fat", "healthy", "diet", "cooking",
                "veg", "non veg", "paneer", "chicken", "masala", "rice", "dal", "biryani", "roti", "sabzi",
                "breakfast", "lunch", "dinner", "street food", "junk food", "milk", "vitamins", "snacks", "dessert",
                "Indian food", "fruits", "vegetables", "chai", "maggi", "bread", "oil", "sugar", "salt", "ingredients",
                "Vada Pav", "Frankie", "Samosa", "Masala Dosa"
        };

        for (String keyword : foodKeywords) {
            if (query.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // ✅ Verifies if AI responded with food-related content
    private boolean isFoodResponse(String response) {
        String[] foodKeywords = {
                "food", "recipe", "cooking", "calories", "diet", "nutrition", "dish", "taste", "ingredient", "snack", "meal", "protein", "carbohydrates", "healthy", "fat"
        };

        for (String keyword : foodKeywords) {
            if (response.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean isHallucinated(String response) {
        String[] hallucinationIndicators = {
                "I believe", "It might be", "Possibly", "I'm not sure", "Maybe", "I think", "It could be", "Most people say", "Some say"
        };

        for (String phrase : hallucinationIndicators) {
            if (response.toLowerCase().contains(phrase)) {
                return true;
            }
        }
        return false;
    }
}
