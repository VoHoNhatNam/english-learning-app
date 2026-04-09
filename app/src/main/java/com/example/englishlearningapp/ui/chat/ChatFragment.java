package com.example.englishlearningapp.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.ChatMessage;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.BlockThreshold;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.GenerationConfig;
import com.google.ai.client.generativeai.type.HarmCategory;
import com.google.ai.client.generativeai.type.SafetySetting;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";
    // Tạm ẩn API_KEY để bảo mật. Hãy dán lại key thật của bạn vào đây ở dưới local nhé!
    private static final String API_KEY = "AIzaSyCUrNPJWv5Zl7qdN7G-LhEREiC1-EPxFHs";

    private RecyclerView rvChat;
    private EditText edtMessage;
    private ChatAdapter adapter;
    private List<ChatMessage> messageList;

    private GenerativeModelFutures model;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        rvChat = view.findViewById(R.id.rvChat);
        edtMessage = view.findViewById(R.id.edtMessage);
        ImageButton btnSend = view.findViewById(R.id.btnSend);

        initModel("gemini-2.5-flash");

        messageList = new ArrayList<>();
        messageList.add(new ChatMessage("Xin chào! Tôi là trợ lý AI của bạn. Bạn cần giúp gì không?", ChatMessage.TYPE_AI));

        adapter = new ChatAdapter(messageList);
        rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChat.setAdapter(adapter);

        btnSend.setOnClickListener(v -> {
            String text = edtMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                edtMessage.setText("");
            }
        });

        return view;
    }

    private void initModel(String modelName) {
        GenerationConfig.Builder configBuilder = new GenerationConfig.Builder();
        configBuilder.temperature = 0.7f;
        configBuilder.topK = 40;
        configBuilder.topP = 0.95f;
        GenerationConfig config = configBuilder.build();

        GenerativeModel gm = new GenerativeModel(
                modelName,
                API_KEY,
                config,
                Collections.singletonList(new SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE))
        );

        model = GenerativeModelFutures.from(gm);
    }
    private void sendMessage(String text) {
        messageList.add(new ChatMessage(text, ChatMessage.TYPE_USER));
        adapter.notifyItemInserted(messageList.size() - 1);
        rvChat.scrollToPosition(messageList.size() - 1);

        callGemini(text);
    }

    private void callGemini(String text) {
        Content prompt = new Content.Builder()
                .addText(text)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        messageList.add(new ChatMessage(resultText, ChatMessage.TYPE_AI));
                        adapter.notifyItemInserted(messageList.size() - 1);
                        rvChat.scrollToPosition(messageList.size() - 1);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Log.e(TAG, "Gemini API Error: " + t.getMessage(), t);
                handleError(t);
            }
        }, executor);
    }

    private void handleError(Throwable t) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            String errorMsg = t.getMessage() != null ? t.getMessage() : "Unknown error";
            String displayError;

            if (errorMsg.contains("404")) {
                displayError = "Lỗi 404: Model không tồn tại hoặc không được hỗ trợ ở vùng của bạn. Đã thử dùng 'gemini-1.5-flash'.";
            } else if (errorMsg.contains("403")) {
                displayError = "Lỗi 403: Không có quyền truy cập. Kiểm tra lại API Key hoặc vùng hỗ trợ (có thể cần dùng VPN).";
            } else {
                displayError = "Lỗi kết nối: " + errorMsg;
            }

            messageList.add(new ChatMessage(displayError, ChatMessage.TYPE_AI));
            adapter.notifyItemInserted(messageList.size() - 1);
            rvChat.scrollToPosition(messageList.size() - 1);
        });
    }
}