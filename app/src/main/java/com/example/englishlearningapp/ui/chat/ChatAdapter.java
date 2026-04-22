package com.example.englishlearningapp.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.ChatMessage;

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
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_ai, parent, false);
            return new AiViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (holder instanceof UserViewHolder) {
            UserViewHolder userHolder = (UserViewHolder) holder;
            userHolder.tvMessage.setText(message.getText());
            // In a real app, you'd format the timestamp
            userHolder.tvTime.setText("SENT • 10:02 AM");
        } else {
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
}