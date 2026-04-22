package com.example.englishlearningapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView rvNotifications;
    private NotificationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saved) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        
        rvNotifications = view.findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<Object> items = createCategorizedItems();
        adapter = new NotificationAdapter(items);
        rvNotifications.setAdapter(adapter);
        
        setupClickListeners(view);
        
        return view;
    }

    private void setupClickListeners(View view) {
        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        });

        view.findViewById(R.id.btnMenu).setOnClickListener(v -> 
            Toast.makeText(getContext(), "Menu tùy chọn", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.btnFilterAll).setOnClickListener(v -> 
            Toast.makeText(getContext(), "Lọc tất cả", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.btnFilterUnread).setOnClickListener(v -> 
            Toast.makeText(getContext(), "Lọc chưa đọc", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.btnFilterLearning).setOnClickListener(v -> 
            Toast.makeText(getContext(), "Lọc học tập", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.tvUndo).setOnClickListener(v -> 
            Toast.makeText(getContext(), "Đã hoàn tác", Toast.LENGTH_SHORT).show());
    }

    private List<Object> createCategorizedItems() {
        List<Object> items = new ArrayList<>();
        
        // Nhóm HÔM NAY
        items.add("HÔM NAY");
        items.add(new Notification("1", "Bài học mới", "Chương \"Cấu trúc câu nâng cao\" đã sẵn sàng cho bạn khám phá.", "2H AGO", Notification.Type.LESSON, false));
        items.add(new Notification("2", "Thành tích mới", "Chúc mừng! Bạn đã đạt được huy hiệu \"Weekly Warrior\" vì học tập liên tục 7 ngày.", "5H AGO", Notification.Type.ACHIEVEMENT, false));
        
        // Nhóm HÔM QUA
        items.add("HÔM QUA");
        items.add(new Notification("3", "Nhắc nhở học tập", "Đã đến giờ ôn tập từ vựng hôm nay rồi. Đừng bỏ lỡ nhé!", "1 ngày trước", Notification.Type.SYSTEM, true));
        
        // Nhóm TUẦN TRƯỚC
        items.add("TUẦN TRƯỚC");
        items.add(new Notification("4", "Bài học mới", "Khám phá các thành ngữ phổ biến trong giao tiếp công việc.", "5 ngày trước", Notification.Type.LESSON, true));
        
        return items;
    }
}
