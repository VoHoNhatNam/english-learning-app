package com.example.englishlearningapp.ui.home;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.Notification;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Object> items;

    public NotificationAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
            return new NotificationViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).tvHeader.setText((String) items.get(position));
        } else if (holder instanceof NotificationViewHolder) {
            Notification notification = (Notification) items.get(position);
            NotificationViewHolder itemHolder = (NotificationViewHolder) holder;

            // Xử lý hiển thị tin nhắn đã thu hồi
            if (notification.isDeleted()) {
                itemHolder.tvTitle.setText(notification.getTitle());
                itemHolder.tvTitle.setAlpha(0.5f);
                itemHolder.tvContent.setText("Thông báo này đã được thu hồi");
                itemHolder.tvContent.setTypeface(null, android.graphics.Typeface.ITALIC);
                itemHolder.tvContent.setAlpha(0.5f);
                itemHolder.llAction.setVisibility(View.GONE);
                itemHolder.viewUnreadDot.setVisibility(View.GONE);
            } else {
                itemHolder.tvTitle.setText(notification.getTitle());
                itemHolder.tvTitle.setAlpha(1.0f);
                itemHolder.tvContent.setText(notification.getContent());
                itemHolder.tvContent.setTypeface(null, android.graphics.Typeface.NORMAL);
                itemHolder.tvContent.setAlpha(1.0f);
                itemHolder.tvTime.setText(notification.getTime());

                // 1. Phân cấp Typography & Màu sắc (Đã đọc/Chưa đọc)
                if (notification.isRead()) {
                    itemHolder.tvTitle.setTextColor(Color.parseColor("#9CA3AF"));
                    itemHolder.tvContent.setTextColor(Color.parseColor("#9CA3AF"));
                    itemHolder.viewUnreadDot.setVisibility(View.GONE);
                    itemHolder.cardView.setCardElevation(0.5f * itemHolder.itemView.getContext().getResources().getDisplayMetrics().density);
                } else {
                    itemHolder.tvTitle.setTextColor(Color.parseColor("#111827"));
                    itemHolder.tvContent.setTextColor(Color.parseColor("#4B5563"));
                    itemHolder.viewUnreadDot.setVisibility(View.VISIBLE);
                    itemHolder.cardView.setCardElevation(4 * itemHolder.itemView.getContext().getResources().getDisplayMetrics().density);
                }

                // 2. Soft Background Icons (Pastel Colors) & Action Button Visibility
                switch (notification.getType()) {
                    case LESSON:
                        itemHolder.ivIcon.setImageResource(R.drawable.ic_book);
                        itemHolder.viewIconBg.setBackgroundColor(Color.parseColor("#E1F5FE")); // Blue Light
                        itemHolder.ivIcon.setColorFilter(Color.parseColor("#0288D1"));
                        itemHolder.llAction.setVisibility(View.VISIBLE);
                        break;
                    case ACHIEVEMENT:
                        itemHolder.ivIcon.setImageResource(R.drawable.ic_trophy);
                        itemHolder.viewIconBg.setBackgroundColor(Color.parseColor("#FFF9C4")); // Yellow Light
                        itemHolder.ivIcon.setColorFilter(Color.parseColor("#FBC02D"));
                        itemHolder.llAction.setVisibility(View.GONE);
                        break;
                    case SYSTEM:
                        itemHolder.ivIcon.setImageResource(R.drawable.ic_notification);
                        itemHolder.viewIconBg.setBackgroundColor(Color.parseColor("#F3E5F5")); // Purple Light
                        itemHolder.ivIcon.setColorFilter(Color.parseColor("#7B1FA2"));
                        itemHolder.llAction.setVisibility(View.GONE);
                        break;
                    default:
                        itemHolder.ivIcon.setImageResource(R.drawable.ic_notification);
                        itemHolder.viewIconBg.setBackgroundColor(Color.parseColor("#F5F5F5"));
                        itemHolder.ivIcon.setColorFilter(Color.parseColor("#757575"));
                        itemHolder.llAction.setVisibility(View.GONE);
                        break;
                }
            }

            // 3. Sự kiện Click: Đánh dấu đã đọc
            itemHolder.itemView.setOnClickListener(v -> {
                int curPos = holder.getAdapterPosition();
                if (curPos != RecyclerView.NO_POSITION && !notification.isDeleted()) {
                    notification.setRead(true);
                    notifyItemChanged(curPos);
                }
            });

            // 4. SỰ KIỆN NHẤN GIỮ (LONG PRESS)
            itemHolder.itemView.setOnLongClickListener(v -> {
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.getMenu().add(0, 1, 0, "Thu hồi (Unsend)");
                popup.getMenu().add(0, 2, 1, "Xóa ở phía tôi (Delete for me)");
                
                popup.setOnMenuItemClickListener(item -> {
                    int curPos = holder.getAdapterPosition();
                    if (curPos == RecyclerView.NO_POSITION) return true;

                    if (item.getItemId() == 1) {
                        notification.setDeleted(true);
                        notifyItemChanged(curPos);
                    } else if (item.getItemId() == 2) {
                        showConfirmDeleteDialog(v, curPos);
                    }
                    return true;
                });
                popup.show();
                return true;
            });

            itemHolder.llAction.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "Bắt đầu bài học: " + notification.getTitle(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void showConfirmDeleteDialog(View view, int position) {
        if (position == RecyclerView.NO_POSITION) return;

        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_confirm_delete, null);
        AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                .setView(dialogView)
                .create();

        // Làm nền dialog trong suốt để bo góc của MaterialCardView hiển thị đẹp
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialogView.findViewById(R.id.btnConfirmDelete).setOnClickListener(v -> {
            performDelete(view, position);
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.btnCancelDelete).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void performDelete(View view, int position) {
        if (position == RecyclerView.NO_POSITION || position >= items.size()) return;

        Object removedItem = items.get(position);
        items.remove(position);
        
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());

        Snackbar.make(view, "Đã xóa thông báo", Snackbar.LENGTH_LONG)
                .setAction("HOÀN TÁC", v -> {
                    items.add(position, removedItem);
                    notifyItemInserted(position);
                    if (view.getParent() instanceof RecyclerView) {
                        ((RecyclerView) view.getParent()).scrollToPosition(position);
                    }
                })
                .setActionTextColor(Color.parseColor("#4ADE80"))
                .show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;
        HeaderViewHolder(View view) {
            super(view);
            tvHeader = view.findViewById(R.id.tvHeader);
        }
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        View viewIconBg;
        TextView tvTitle, tvTime, tvContent;
        View llAction;
        View viewUnreadDot;
        com.google.android.material.card.MaterialCardView cardView;

        NotificationViewHolder(View view) {
            super(view);
            ivIcon = view.findViewById(R.id.ivNotifIcon);
            viewIconBg = view.findViewById(R.id.viewIconBg);
            tvTitle = view.findViewById(R.id.tvNotifTitle);
            tvTime = view.findViewById(R.id.tvNotifTime);
            tvContent = view.findViewById(R.id.tvNotifContent);
            llAction = view.findViewById(R.id.llAction);
            viewUnreadDot = view.findViewById(R.id.viewUnreadDot);
            cardView = view.findViewById(R.id.cardNotification);
        }
    }
}
