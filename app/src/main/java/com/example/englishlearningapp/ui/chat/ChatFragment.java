package com.example.englishlearningapp.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.BuildConfig;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.ChatMessage;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.BlockThreshold;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.GenerationConfig;
import com.google.ai.client.generativeai.type.HarmCategory;
import com.google.ai.client.generativeai.type.RequestOptions;
import com.google.ai.client.generativeai.type.SafetySetting;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";
    private static final String API_KEY = BuildConfig.GEMINI_API_KEY;

    private RecyclerView rvChat;
    private EditText edtMessage;
    private View btnSend;
    private ChatAdapter adapter;
    private List<ChatMessage> messageList;
    private View scrollSuggestions;

    private GenerativeModelFutures model;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews(view);
        setupToolbar(view);
        initModel("gemini-2.5-flash"); // luôn luôn cố định gemini-2.5-flash cho bất cứ trường hợp nào.Không được phép can thiệp chỉnh sửa tại model gemini-2.5-flash.

        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList);
        rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChat.setAdapter(adapter);

        loadInitialGreeting();

        btnSend.setOnClickListener(v -> {
            String text = edtMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                edtMessage.setText("");
            }
        });

        return view;
    }

    private void initViews(View view) {
        rvChat = view.findViewById(R.id.rvChat);
        edtMessage = view.findViewById(R.id.edtMessage);
        btnSend = view.findViewById(R.id.btnSend);
        scrollSuggestions = view.findViewById(R.id.scrollSuggestions);

        // Giảm lag: Xử lý nhẹ nhàng việc hiển thị suggestion
        edtMessage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && scrollSuggestions != null) {
                scrollSuggestions.setVisibility(View.GONE);
            }
        });
    }

    private void loadInitialGreeting() {
        FirebaseUser user = mAuth.getCurrentUser();
        String name = (user != null && user.getDisplayName() != null) ? user.getDisplayName() : "bạn";
        Random random = new Random();

        // Danh sách câu chào mặc định ngẫu nhiên
        String[] generalGreetings = {
                "Chào " + name + "! Hôm nay bạn muốn luyện tập gì nào?",
                "Chào " + name + "! Rất vui được gặp lại bạn. Hôm nay chúng ta bắt đầu bằng chủ đề gì đây?",
                "Hi " + name + ", sẵn sàng để nâng cao trình độ tiếng Anh cùng tôi chưa?",
                "Chào " + name + "! Hôm nay bạn cảm thấy thế nào? Muốn trò chuyện về chủ đề gì không?",
                "Chào mừng " + name + " trở lại! Chúc bạn có một buổi học thật hiệu quả.",
                "Chào buổi sáng " + name + "! Bạn đã sẵn sàng học thêm vài cấu trúc câu mới chưa?",
                "Hello " + name + "! Tôi có một vài ý tưởng hay cho bài luyện tập hôm nay, bạn muốn nghe thử không?"
        };

        String defaultGreeting = generalGreetings[random.nextInt(generalGreetings.length)];

        if (user != null) {
            db.collection("users").document(user.getUid()).get().addOnSuccessListener(doc -> {
                String finalGreeting = defaultGreeting;
                if (doc.exists()) {
                    String level = doc.getString("englishLevel");
                    if (level != null) {
                        // Danh sách câu chào ngẫu nhiên dựa trên trình độ
                        String[] levelGreetings = {
                                "Chào " + name + "! Tôi thấy bạn đang ở trình độ " + level + ". Chúng ta tiếp tục luyện tập nhé?",
                                "Chào " + name + "! Với trình độ " + level + " hiện tại, tôi tin rằng bạn sẽ hoàn thành tốt bài học hôm nay.",
                                "Chào mừng " + name + "! Tiến độ ở trình độ " + level + " của bạn rất ấn tượng. Bắt đầu thôi!",
                                "Hi " + name + ", tôi nhận thấy bạn đang học rất chăm chỉ ở mức " + level + ". Hôm nay bạn muốn thảo luận chủ đề gì?",
                                "Chào " + name + "! Dựa trên trình độ " + level + ", tôi đã chuẩn bị một vài bài tập thử thách cho bạn đây.",
                                "Tuyệt vời " + name + "! Bạn đang làm rất tốt ở mức " + level + ". Tiếp tục duy trì phong độ này nhé!"
                        };
                        finalGreeting = levelGreetings[random.nextInt(levelGreetings.length)];
                    }
                }
                addAiMessage(finalGreeting, "CONVERSATION STARTER");
            }).addOnFailureListener(e -> addAiMessage(defaultGreeting, "CONVERSATION STARTER"));
        } else {
            addAiMessage(defaultGreeting, "CONVERSATION STARTER");
        }
    }

    private void addAiMessage(String text, String label) {
        ChatMessage msg = new ChatMessage(text, ChatMessage.TYPE_AI);
        if (label != null) msg.setLabel(label);
        messageList.add(msg);
        adapter.notifyItemInserted(messageList.size() - 1);
        rvChat.scrollToPosition(messageList.size() - 1);
    }

    private void setupToolbar(View view) {
        view.findViewById(R.id.btnMenu).setOnClickListener(v -> {
            // Logic for menu
        });
        view.findViewById(R.id.btnHistory).setOnClickListener(v -> {
            // Logic for history
        });
    }

    private void initModel(String modelName) {
        GenerationConfig.Builder configBuilder = new GenerationConfig.Builder();
        configBuilder.temperature = 0.7f;
        GenerationConfig config = configBuilder.build();

        Content systemInstruction = new Content.Builder()
                .addText("Bạn là một trợ lý học tiếng Anh cá nhân hóa. Trả lời ngắn gọn, thân thiện.")
                .build();

        GenerativeModel gm = new GenerativeModel(
                modelName,
                API_KEY,
                config,
                Collections.singletonList(new SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE)),
                new RequestOptions(),
                null,
                null,
                systemInstruction
        );

        model = GenerativeModelFutures.from(gm);
    }

    private void sendMessage(String text) {
        messageList.add(new ChatMessage(text, ChatMessage.TYPE_USER));
        adapter.notifyItemInserted(messageList.size() - 1);
        rvChat.scrollToPosition(messageList.size() - 1);
        
        if (scrollSuggestions != null) scrollSuggestions.setVisibility(View.GONE);
        callGemini(text);
    }

    private void callGemini(String text) {
        Content prompt = new Content.Builder().addText(text).build();
        ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> addAiMessage(resultText, null));
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Log.e(TAG, "Gemini Error", t);
            }
        }, executor);
    }
}