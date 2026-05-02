package com.example.englishlearningapp.ui.lesson;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.CustomLesson;
import com.example.englishlearningapp.data.model.LessonBlock;
import com.example.englishlearningapp.ui.chat.ChatFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LessonDetailFragment extends Fragment {

    private CustomLesson lesson;
    private String lessonId;
    private String lessonTitle;
    private String lessonDescription;
    private String lessonContent;

    private View chatContainer;
    private Guideline guideline;
    private FloatingActionButton fabAiAction;
    private boolean isChatOpen = false;

    private RecyclerView rvLessonContent;
    private LessonContentAdapter contentAdapter;

    public static LessonDetailFragment newInstance(CustomLesson lesson) {
        LessonDetailFragment fragment = new LessonDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("lesson", lesson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lesson = (CustomLesson) getArguments().getSerializable("lesson");
            lessonId = getArguments().getString("lessonId");
            lessonTitle = getArguments().getString("lessonName");
            lessonDescription = getArguments().getString("lessonDescription");
            lessonContent = getArguments().getString("lessonContent");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_detail, container, false);

        initViews(view);
        setupContentRecyclerView(view);

        return view;
    }

    private void initViews(View view) {
        chatContainer = view.findViewById(R.id.chatContainer);
        guideline = view.findViewById(R.id.guideline);
        View btnCloseChat = view.findViewById(R.id.btnCloseChat);

        if (btnCloseChat != null) {
            btnCloseChat.setOnClickListener(v -> closeSplitChat());
        }

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        }

        TextView tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        TextView tvTitle = view.findViewById(R.id.tvLessonTitle);
        fabAiAction = view.findViewById(R.id.fabAiAction);
        View btnGoPremium = view.findViewById(R.id.btnGoPremium);

        String fullTitle = "Lesson";
        if (lesson != null) {
            fullTitle = lesson.getTitle();
            tvTitle.setText(lesson.getTitle());
        } else if (lessonTitle != null) {
            fullTitle = lessonTitle;
            tvTitle.setText(lessonTitle);
        }

        String toolbarTitle = "Bài";
        if (fullTitle != null) {
            if (fullTitle.contains(":")) {
                String partBeforeColon = fullTitle.split(":")[0].trim();
                if (partBeforeColon.toLowerCase().contains("bài")) {
                    toolbarTitle = partBeforeColon;
                } else {
                    toolbarTitle = findBàiPattern(fullTitle);
                }
            } else {
                toolbarTitle = findBàiPattern(fullTitle);
            }
        }

        if (toolbarTitle.equals("Bài") && lessonId != null) {
            try {
                int idNum = Integer.parseInt(lessonId);
                int lessonNum = idNum % 100;
                toolbarTitle = "Bài " + lessonNum;
            } catch (NumberFormatException ignored) {}
        }

        if (tvToolbarTitle != null) {
            tvToolbarTitle.setText(toolbarTitle);
        }

        if (btnGoPremium != null) {
            btnGoPremium.setOnClickListener(v -> {});
        }

        if (fabAiAction != null) {
            fabAiAction.setOnClickListener(v -> showAiHelperBottomSheet(tvTitle.getText().toString()));
        }
    }

    private void setupContentRecyclerView(View view) {
        rvLessonContent = view.findViewById(R.id.rvLessonContent);
        rvLessonContent.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<LessonBlock> blocks;
        if (lessonContent != null && !lessonContent.isEmpty()) {
            blocks = parseContentToBlocks(lessonContent);
        } else {
            blocks = new ArrayList<>();
            blocks.add(new LessonBlock("text", lessonDescription != null ? lessonDescription : "Nội dung đang được cập nhật..."));
        }
        
        contentAdapter = new LessonContentAdapter(blocks);
        rvLessonContent.setAdapter(contentAdapter);
    }

    private List<LessonBlock> parseContentToBlocks(String content) {
        List<LessonBlock> blocks = new ArrayList<>();
        String[] lines = content.split("\n");
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            // Header: "1. Mục tiêu", "2. Cách dùng", ...
            if (Pattern.matches("^\\d+\\..*", line)) {
                blocks.add(new LessonBlock("header", line));
            }
            // Section Title: "a. Động từ...", "b. Động từ..."
            else if (Pattern.matches("^[a-z]\\..*", line)) {
                blocks.add(new LessonBlock("section_title", line));
            }
            // Note/Example: "👉 Ví dụ:", "👉 Lưu ý:"
            else if (line.startsWith("👉")) {
                blocks.add(new LessonBlock("note", line));
            }
            // Error Correction: ❌ và ✅
            else if (line.startsWith("❌")) {
                String wrong = line;
                String correct = "";
                if (i + 1 < lines.length && lines[i+1].trim().startsWith("✅")) {
                    correct = lines[++i].trim();
                }
                blocks.add(new LessonBlock("error_correction", "", wrong.replace("❌", "").trim(), correct.replace("✅", "").trim()));
            }
            // Bullet points
            else if (line.startsWith("-") || line.startsWith("•")) {
                blocks.add(new LessonBlock("bullet", line.substring(1).trim()));
            }
            // Default text
            else {
                blocks.add(new LessonBlock("text", line));
            }
        }
        return blocks;
    }

    private String findBàiPattern(String text) {
        Pattern pattern = Pattern.compile("(?i)Bài\\s*\\d+");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "Bài";
    }

    private void showAiHelperBottomSheet(String lessonTitle) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_ai_helper, null);
        bottomSheetDialog.setContentView(view);

        view.findViewById(R.id.btnCreateNote).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showQuickNoteDialog();
        });

        view.findViewById(R.id.btnAiQa).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            openSplitChat("Chào bạn! Mình là trợ lý AI. Bạn có thắc mắc gì về bài học '" + lessonTitle + "' không?");
        });

        bottomSheetDialog.show();
    }

    private void openSplitChat(String initialMessage) {
        if (chatContainer == null || guideline == null) return;
        isChatOpen = true;
        chatContainer.setVisibility(View.VISIBLE);
        if (fabAiAction != null) fabAiAction.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
        params.guidePercent = 0.45f;
        guideline.setLayoutParams(params);
        Bundle bundle = new Bundle();
        bundle.putString("initial_topic", initialMessage);
        bundle.putBoolean("is_split_screen", true);
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction().replace(R.id.innerChatContainer, chatFragment).commit();
    }

    private void closeSplitChat() {
        if (chatContainer == null || guideline == null) return;
        isChatOpen = false;
        chatContainer.setVisibility(View.GONE);
        if (fabAiAction != null) fabAiAction.setVisibility(View.VISIBLE);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
        params.guidePercent = 1.0f;
        guideline.setLayoutParams(params);
    }

    private void showQuickNoteDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_quick_note);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.gravity = Gravity.BOTTOM | Gravity.END;
            params.y = 50; params.x = 20;
            dialog.getWindow().setAttributes(params);
        }
        dialog.findViewById(R.id.btnCloseNote).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
