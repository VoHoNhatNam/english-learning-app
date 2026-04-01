package com.example.englishlearningapp.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;
import com.google.android.material.button.MaterialButton;

public class VipFragment extends Fragment {

    private ImageView btnBack;
    private MaterialButton btnUpgrade;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Nạp layout fragment_vip (Cái có nút Back mà bạn vừa viết)
        View view = inflater.inflate(R.layout.fragment_vip, container, false);

        // 2. Ánh xạ các view từ XML
        btnBack = view.findViewById(R.id.btnBackVip);
        btnUpgrade = view.findViewById(R.id.btnUpgradeVip);

        // 3. Xử lý sự kiện nút Quay lại
        btnBack.setOnClickListener(v -> {
            // Kiểm tra xem FragmentManager có tồn tại không và thực hiện quay lại
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

        // 4. Xử lý sự kiện nút Nâng cấp
        btnUpgrade.setOnClickListener(v -> {
            // Thông báo giả lập thanh toán
            Toast.makeText(getContext(),
                    "Chức năng thanh toán đang được bảo trì!",
                    Toast.LENGTH_LONG).show();
        });

        return view;
    }
}