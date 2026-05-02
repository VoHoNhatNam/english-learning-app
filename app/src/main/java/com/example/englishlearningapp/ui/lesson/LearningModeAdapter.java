package com.example.englishlearningapp.ui.lesson;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.LearningMode;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class LearningModeAdapter extends RecyclerView.Adapter<LearningModeAdapter.ViewHolder> {

    private List<LearningMode> modes;
    private OnModeSelectedListener listener;

    public interface OnModeSelectedListener {
        void onModeSelected(LearningMode mode);
    }

    public LearningModeAdapter(List<LearningMode> modes, OnModeSelectedListener listener) {
        this.modes = modes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_learning_mode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LearningMode mode = modes.get(position);
        holder.tvTitle.setText(mode.getTitle());
        holder.tvDesc.setText(mode.getDescription());
        holder.ivIcon.setImageResource(mode.getIconResId());

        int iconBgColor, iconTintColor;
        switch (mode.getId()) {
            case "flashcard":
                iconBgColor = 0xFFEEF2FF;
                iconTintColor = 0xFF3B82F6;
                break;
            case "quiz":
                iconBgColor = 0xFFF5F3FF;
                iconTintColor = 0xFF8B5CF6;
                break;
            case "match":
                iconBgColor = 0xFFFFFBEB;
                iconTintColor = 0xFFF59E0B;
                break;
            case "type":
                iconBgColor = 0xFFFDF2F8;
                iconTintColor = 0xFFEC4899;
                break;
            default:
                iconBgColor = 0xFFF1F5F9;
                iconTintColor = 0xFF64748B;
                break;
        }

        holder.ivIcon.setBackgroundTintList(ColorStateList.valueOf(iconBgColor));
        holder.ivIcon.setImageTintList(ColorStateList.valueOf(iconTintColor));
        holder.tvRecommended.setVisibility(mode.isRecommended() ? View.VISIBLE : View.GONE);

        if (mode.isSelected()) {
            holder.card.setStrokeColor(Color.parseColor("#3B82F6"));
            holder.card.setStrokeWidth(dpToPx(2));
            holder.card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.ivStatus.setImageResource(R.drawable.ic_check_circle);
            holder.ivStatus.setImageTintList(ColorStateList.valueOf(Color.parseColor("#3B82F6")));
        } else {
            holder.card.setStrokeColor(Color.parseColor("#E2E8F0"));
            holder.card.setStrokeWidth(dpToPx(1.5f));
            holder.card.setCardBackgroundColor(Color.WHITE);
            holder.ivStatus.setImageResource(R.drawable.ic_next);
            holder.ivStatus.setImageTintList(ColorStateList.valueOf(Color.parseColor("#CBD5E1")));
        }

        holder.itemView.setOnClickListener(v -> {
            for (LearningMode m : modes) m.setSelected(false);
            mode.setSelected(true);
            notifyDataSetChanged();
            listener.onModeSelected(mode);
        });
    }

    private int dpToPx(float dp) {
        return (int) (dp * android.content.res.Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    public int getItemCount() {
        return modes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        ImageView ivIcon, ivStatus;
        TextView tvTitle, tvDesc, tvRecommended;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Sửa lỗi ở đây: card nằm bên trong itemView (FrameLayout)
            card = itemView.findViewById(R.id.cardMode);
            ivIcon = itemView.findViewById(R.id.ivModeIcon);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            tvTitle = itemView.findViewById(R.id.tvModeTitle);
            tvDesc = itemView.findViewById(R.id.tvModeDesc);
            tvRecommended = itemView.findViewById(R.id.tvRecommended);
        }
    }
}
