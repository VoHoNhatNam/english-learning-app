package com.example.englishlearningapp.ui.lesson;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
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

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.CustomLesson;
import com.example.englishlearningapp.ui.chat.ChatFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        return view;
    }

    private void initViews(View view) {
        // Split Screen Views
        chatContainer = view.findViewById(R.id.chatContainer);
        guideline = view.findViewById(R.id.guideline);
        View btnCloseChat = view.findViewById(R.id.btnCloseChat);

        if (btnCloseChat != null) {
            btnCloseChat.setOnClickListener(v -> closeSplitChat());
        }

        // Toolbar Navigation
        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        }

        // Binding Views
        TextView tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        TextView tvTitle = view.findViewById(R.id.tvLessonTitle);
        TextView tvContext = view.findViewById(R.id.tvLessonContext);
        fabAiAction = view.findViewById(R.id.fabAiAction);
        View btnGoPremium = view.findViewById(R.id.btnGoPremium);

        // Populate Data
        String fullTitle = "Lesson";
        if (lesson != null) {
            fullTitle = lesson.getTitle();
            tvTitle.setText(lesson.getTitle());
            if (lesson.getDescription() != null && !lesson.getDescription().isEmpty()) {
                tvContext.setText(lesson.getDescription());
            }
        } else {
            if (lessonTitle != null) {
                fullTitle = lessonTitle;
                tvTitle.setText(lessonTitle);
            }
            if (lessonContent != null) {
                displayFormattedContent(tvContext, lessonContent);
                hidePlaceholderContent(view);
            } else if (lessonDescription != null) {
                tvContext.setText(lessonDescription);
            }
        }

        // Extract strictly "Bài X" for Toolbar
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

        // Fallback to Lesson ID
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

        // Interaction
        if (btnGoPremium != null) {
            btnGoPremium.setOnClickListener(v -> {
                // Handle premium upgrade
            });
        }

        if (fabAiAction != null) {
            fabAiAction.setOnClickListener(v -> showAiHelperBottomSheet(tvTitle.getText().toString()));
        }
    }

    private void displayFormattedContent(TextView textView, String content) {
        // Simple modern formatting for the lesson content
        String formatted = content
            .replace("1. Mục tiêu", "<b>1. Mục tiêu</b>")
            .replace("2. Cách dùng", "<br><br><b>2. Cách dùng</b>")
            .replace("3. Cấu trúc", "<br><br><b>3. Cấu trúc</b>")
            .replace("a. Động từ “TO BE”", "<br><i>a. Động từ “TO BE”</i>")
            .replace("b. Động từ thường", "<br><br><i>b. Động từ thường</i>")
            .replace("4. Lưu ý quan trọng", "<br><br><b>4. Lưu ý quan trọng</b>")
            .replace("5. Lỗi sai phổ biến", "<br><br><b>5. Lỗi sai phổ biến</b>")
            .replace("👉 Ví dụ:", "<br><br>👉 <u>Ví dụ:</u>")
            .replace("❌", "<br>❌")
            .replace("✅", "<br>✅")
            .replace("→", " → ");

        textView.setText(Html.fromHtml(formatted, Html.FROM_HTML_MODE_COMPACT));
        textView.setLineSpacing(0f, 1.4f); // Increase line spacing for better readability
    }

    private void hidePlaceholderContent(View view) {
        ViewGroup parent = (ViewGroup) view.findViewById(R.id.tvLessonContext).getParent();
        if (parent != null) {
            boolean foundContext = false;
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (child.getId() == R.id.tvLessonContext) {
                    foundContext = true;
                    continue;
                }
                if (foundContext) {
                    child.setVisibility(View.GONE);
                }
            }
        }
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

        view.findViewById(R.id.btnTakeQuiz).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            // Handle take quiz
        });

        view.findViewById(R.id.btnUpgrade).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            // Handle upgrade
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

        getChildFragmentManager().beginTransaction()
                .replace(R.id.innerChatContainer, chatFragment)
                .commit();
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
            params.y = 50;
            params.x = 20;
            dialog.getWindow().setAttributes(params);
        }

        dialog.findViewById(R.id.btnCloseNote).setOnClickListener(v -> dialog.dismiss());
        
        dialog.findViewById(R.id.btnSaveDraft).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đã lưu bản nháp ghi chú", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.btnSendNote).setOnClickListener(v -> {
            Toast.makeText(getContext(), "AI đang xử lý ghi chú của bạn...", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private String findBàiPattern(String text) {
        Pattern pattern = Pattern.compile("(?i)Bài\\s*\\d+");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "Bài";
    }
}
