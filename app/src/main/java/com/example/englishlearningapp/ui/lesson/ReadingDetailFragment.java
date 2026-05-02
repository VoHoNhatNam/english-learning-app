package com.example.englishlearningapp.ui.lesson;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.MainActivity;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.ReadingArticle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReadingDetailFragment extends Fragment {

    private ReadingArticle article;
    private TextView tvAppBarTitle, tvTitle, tvAuthorName, tvAuthorInfo, tvContentPart1, tvContentPart2, tvContentPart3, tvAiInsights;
    private TextView tvTabEn, tvTabVi;
    private ImageView ivAuthor, ivMainImage;
    private ImageButton btnBack;
    private FloatingActionButton fabAiHelper;
    private boolean isEnglish = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading_detail, container, false);

        if (getArguments() != null) {
            article = (ReadingArticle) getArguments().getSerializable("article");
        }

        initViews(view);
        setupUI();
        
        // Ẩn Bottom Navigation khi vào màn hình này
        setBottomNavigationVisibility(View.GONE);

        return view;
    }

    private void setBottomNavigationVisibility(int visibility) {
        if (getActivity() instanceof MainActivity) {
            BottomNavigationView navView = getActivity().findViewById(R.id.bottomNavigationView);
            if (navView != null) {
                navView.setVisibility(visibility);
            }
            View navContainer = getActivity().findViewById(R.id.bottomNavContainer);
            if (navContainer != null) {
                navContainer.setVisibility(visibility);
            }
            
            View fragmentContainer = getActivity().findViewById(R.id.fragment_container);
            if (fragmentContainer != null) {
                if (visibility == View.GONE) {
                    fragmentContainer.setPadding(0, 0, 0, 0);
                } else {
                    int paddingBottom = (int) (80 * getResources().getDisplayMetrics().density);
                    fragmentContainer.setPadding(0, 0, 0, paddingBottom);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Hiện lại Bottom Navigation khi thoát màn hình này
        setBottomNavigationVisibility(View.VISIBLE);
    }

    private void initViews(View view) {
        tvAppBarTitle = view.findViewById(R.id.tvAppBarTitle);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvAuthorName = view.findViewById(R.id.tvAuthorName);
        tvAuthorInfo = view.findViewById(R.id.tvAuthorInfo);
        tvContentPart1 = view.findViewById(R.id.tvContentPart1);
        tvContentPart2 = view.findViewById(R.id.tvContentPart2);
        tvContentPart3 = view.findViewById(R.id.tvContentPart3);
        tvAiInsights = view.findViewById(R.id.tvAiInsights);
        ivAuthor = view.findViewById(R.id.ivAuthor);
        ivMainImage = view.findViewById(R.id.ivMainImage);
        btnBack = view.findViewById(R.id.btnBack);
        fabAiHelper = view.findViewById(R.id.fabAiHelper);

        // Language Tabs
        tvTabEn = view.findViewById(R.id.btnToggleEn);
        tvTabVi = view.findViewById(R.id.btnToggleVi);

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        if (tvTabEn != null) tvTabEn.setOnClickListener(v -> switchLanguage(true));
        if (tvTabVi != null) tvTabVi.setOnClickListener(v -> switchLanguage(false));
        
        if (fabAiHelper != null) {
            fabAiHelper.setOnClickListener(v -> showAiHelperBottomSheet());
        }
    }

    private void showAiHelperBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_ai_helper, null);
        
        bottomSheetView.findViewById(R.id.btnAiQa).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Hỏi đáp với AI", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });
        
        bottomSheetView.findViewById(R.id.btnCreateNote).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Tạo ghi chú", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });
        
        bottomSheetView.findViewById(R.id.btnTakeQuiz).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Làm kiểm tra", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void switchLanguage(boolean english) {
        if (isEnglish == english) return;
        isEnglish = english;
        updateLanguageUI();
        displayContent();
    }

    private void updateLanguageUI() {
        if (tvTabEn == null || tvTabVi == null) return;

        if (isEnglish) {
            tvTabEn.setBackgroundResource(R.drawable.bg_button);
            if (tvTabEn.getBackground() != null) {
                tvTabEn.getBackground().setTint(Color.WHITE);
            }
            tvTabEn.setTextColor(Color.parseColor("#1E293B"));

            tvTabVi.setBackground(null);
            tvTabVi.setTextColor(Color.parseColor("#94A3B8"));
        } else {
            tvTabVi.setBackgroundResource(R.drawable.bg_button);
            if (tvTabVi.getBackground() != null) {
                tvTabVi.getBackground().setTint(Color.WHITE);
            }
            tvTabVi.setTextColor(Color.parseColor("#1E293B"));

            tvTabEn.setBackground(null);
            tvTabEn.setTextColor(Color.parseColor("#94A3B8"));
        }
    }

    private void setupUI() {
        if (article == null) return;

        // Tách tiêu đề thành "Bài X" cho AppBar và full title cho nội dung
        String fullTitle = article.getTitle();
        if (fullTitle.contains(":")) {
            tvAppBarTitle.setText(fullTitle.split(":")[0].trim());
        } else {
            tvAppBarTitle.setText(fullTitle);
        }

        tvTitle.setText(fullTitle);
        tvAuthorName.setText(article.getAuthor());
        tvAuthorInfo.setText(article.getAuthorRole() + " • " + article.getReadTime());
        tvAiInsights.setText(article.getAiInsights());

        displayContent();
    }

    private void displayContent() {
        if (article == null) return;

        String fullText = isEnglish ? article.getContent() : article.getContentVi();
        
        // Chia nội dung thành các đoạn để gán vào các TextView khác nhau
        String[] paragraphs = fullText.split("\\n\\n");
        
        if (paragraphs.length >= 1) {
            setParagraphText(tvContentPart1, paragraphs[0]);
        }

        if (paragraphs.length >= 2) {
            setParagraphText(tvContentPart2, paragraphs[1]);
        } else {
            tvContentPart2.setVisibility(View.GONE);
            if (ivMainImage != null && ivMainImage.getParent() instanceof View) {
                 ((View)ivMainImage.getParent()).setVisibility(View.GONE); // Hide card main image if no content part 2
            }
        }

        if (paragraphs.length >= 3) {
            StringBuilder remaining = new StringBuilder();
            for (int i = 2; i < paragraphs.length; i++) {
                remaining.append(paragraphs[i]).append("\n\n");
            }
            setParagraphText(tvContentPart3, remaining.toString().trim());
        } else {
            tvContentPart3.setVisibility(View.GONE);
        }
    }

    private void setParagraphText(TextView textView, String text) {
        textView.setVisibility(View.VISIBLE);
        if (isEnglish) {
            textView.setText(createSpannable(text, getTargetWords(text)));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            textView.setText(text);
            textView.setMovementMethod(null);
        }
    }

    private String[] getTargetWords(String text) {
        return new String[]{"Greetings", "essential", "introducing", "describe", "identities", "Alphabet", "fundamental", "vowel", "consonant", "literacy"};
    }

    private SpannableString createSpannable(String fullText, String... targetWords) {
        SpannableString ss = new SpannableString(fullText);
        for (String target : targetWords) {
            String lowerFull = fullText.toLowerCase();
            String lowerTarget = target.toLowerCase();
            int start = lowerFull.indexOf(lowerTarget);
            while (start != -1) {
                int end = start + target.length();
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Toast.makeText(getContext(), "Tra từ: " + target, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.parseColor("#2563EB"));
                        ds.setUnderlineText(true);
                    }
                };
                ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                start = lowerFull.indexOf(lowerTarget, end);
            }
        }
        return ss;
    }
}
