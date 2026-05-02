package com.example.englishlearningapp.ui.chat;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ChatMessage.TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user, parent, false);
            return new UserViewHolder(view);
        } else if (viewType == ChatMessage.TYPE_AI) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_ai, parent, false);
            return new AiViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_typing, parent, false);
            return new TypingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (holder instanceof UserViewHolder) {
            UserViewHolder userHolder = (UserViewHolder) holder;
            userHolder.tvMessage.setText(message.getText());
            userHolder.tvTime.setText("SENT • 10:02 AM");
        } else if (holder instanceof AiViewHolder) {
            AiViewHolder aiHolder = (AiViewHolder) holder;
            aiHolder.tvMessage.setText(message.getText());

            if (message.getLabel() != null) {
                aiHolder.tvLabel.setVisibility(View.VISIBLE);
                aiHolder.tvLabel.setText(message.getLabel());
            } else {
                aiHolder.tvLabel.setVisibility(View.GONE);
            }

            if (message.isHasGrammarFix()) {
                aiHolder.layoutActionButtons.setVisibility(View.VISIBLE);
                aiHolder.layoutCorrection.setVisibility(View.VISIBLE);
                aiHolder.tvCorrection.setText(message.getCorrection());
            } else {
                aiHolder.layoutActionButtons.setVisibility(View.GONE);
                aiHolder.layoutCorrection.setVisibility(View.GONE);
            }
        } else if (holder instanceof TypingViewHolder) {
            ((TypingViewHolder) holder).startAnimation();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof TypingViewHolder) {
            ((TypingViewHolder) holder).stopAnimation();
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;
        UserViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    static class AiViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvLabel, tvCorrection;
        View layoutActionButtons, layoutCorrection;
        AiViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            tvCorrection = itemView.findViewById(R.id.tvCorrection);
            layoutActionButtons = itemView.findViewById(R.id.layoutActionButtons);
            layoutCorrection = itemView.findViewById(R.id.layoutCorrection);
        }
    }

    static class TypingViewHolder extends RecyclerView.ViewHolder {
        View dot1, dot2, dot3;
        AnimatorSet animatorSet;

        TypingViewHolder(View itemView) {
            super(itemView);
            dot1 = itemView.findViewById(R.id.dot1);
            dot2 = itemView.findViewById(R.id.dot2);
            dot3 = itemView.findViewById(R.id.dot3);
        }

        void startAnimation() {
            if (animatorSet != null && animatorSet.isRunning()) {
                return;
            }

            animatorSet = new AnimatorSet();
            
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(dot1, "translationY", 0, -10, 0);
            anim1.setDuration(600);
            anim1.setRepeatCount(ObjectAnimator.INFINITE);

            ObjectAnimator anim2 = ObjectAnimator.ofFloat(dot2, "translationY", 0, -10, 0);
            anim2.setDuration(600);
            anim2.setStartDelay(200);
            anim2.setRepeatCount(ObjectAnimator.INFINITE);

            ObjectAnimator anim3 = ObjectAnimator.ofFloat(dot3, "translationY", 0, -10, 0);
            anim3.setDuration(600);
            anim3.setStartDelay(400);
            anim3.setRepeatCount(ObjectAnimator.INFINITE);

            animatorSet.playTogether(anim1, anim2, anim3);
            animatorSet.start();
        }

        void stopAnimation() {
            if (animatorSet != null) {
                animatorSet.cancel();
                dot1.setTranslationY(0);
                dot2.setTranslationY(0);
                dot3.setTranslationY(0);
            }
        }
    }
}
