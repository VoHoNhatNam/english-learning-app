package com.example.englishlearningapp.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
<<<<<<< HEAD
import android.widget.RadioButton;
=======
>>>>>>> 556415d64adb3e497a21be13cd4f6b0536fd6cc0
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;
<<<<<<< HEAD
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class VipFragment extends Fragment {

    private ImageView btnBack;
=======
import com.google.android.material.button.MaterialButton;

public class VipFragment extends Fragment {

    private ImageView btnBack;
    private MaterialButton btnUpgrade;
>>>>>>> 556415d64adb3e497a21be13cd4f6b0536fd6cc0

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
<<<<<<< HEAD
        View view = inflater.inflate(R.layout.fragment_vip, container, false);

        btnBack = view.findViewById(R.id.btnBackVip);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
            });
        }

        // Tìm tất cả các nút "Chọn" trong layout
        setupSelectionButtons(view);

        return view;
    }

    private void setupSelectionButtons(View rootView) {
        List<MaterialButton> buttons = findButtons(rootView);
        for (MaterialButton btn : buttons) {
            if (btn.getText().toString().equalsIgnoreCase("Chọn")) {
                btn.setOnClickListener(v -> showPaymentMethodBottomSheet());
            }
        }
    }

    private List<MaterialButton> findButtons(View v) {
        List<MaterialButton> result = new ArrayList<>();
        if (v instanceof MaterialButton) {
            result.add((MaterialButton) v);
        } else if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                result.addAll(findButtons(vg.getChildAt(i)));
            }
        }
        return result;
    }

    private void showPaymentMethodBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_payment_method, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        RadioButton rbMoMo = bottomSheetView.findViewById(R.id.rbMoMo);
        RadioButton rbZaloPay = bottomSheetView.findViewById(R.id.rbZaloPay);
        RadioButton rbVietQR = bottomSheetView.findViewById(R.id.rbVietQR);

        View.OnClickListener paymentClickListener = v -> {
            rbMoMo.setChecked(v.getId() == R.id.rbMoMo || v.getId() == R.id.cardMoMo);
            rbZaloPay.setChecked(v.getId() == R.id.rbZaloPay || v.getId() == R.id.cardZaloPay);
            rbVietQR.setChecked(v.getId() == R.id.rbVietQR || v.getId() == R.id.cardVietQR);
        };

        // Gán sự kiện cho RadioButtons
        rbMoMo.setOnClickListener(paymentClickListener);
        rbZaloPay.setOnClickListener(paymentClickListener);
        rbVietQR.setOnClickListener(paymentClickListener);

        // Gán sự kiện cho các vùng chứa (Cards) để dễ bấm hơn
        View cardMoMo = bottomSheetView.findViewById(R.id.cardMoMo);
        View cardZaloPay = bottomSheetView.findViewById(R.id.cardZaloPay);
        View cardVietQR = bottomSheetView.findViewById(R.id.cardVietQR);

        if (cardMoMo != null) cardMoMo.setOnClickListener(paymentClickListener);
        if (cardZaloPay != null) cardZaloPay.setOnClickListener(paymentClickListener);
        if (cardVietQR != null) cardVietQR.setOnClickListener(paymentClickListener);

        MaterialButton btnConfirmPayment = bottomSheetView.findViewById(R.id.btnConfirmPayment);
        btnConfirmPayment.setOnClickListener(v -> {
            String method = "";
            if (rbMoMo.isChecked()) method = "Ví MoMo";
            else if (rbZaloPay.isChecked()) method = "ZaloPay";
            else if (rbVietQR.isChecked()) method = "VietQR";

            Toast.makeText(getContext(), "Đã chọn thanh toán qua: " + method, Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();

=======
        // 1. Nạp layout fragment_vip (Cái có nút Back mà bạn vừa viết)
        View view = inflater.inflate(R.layout.fragment_vip, container, false);

        // 2. Ánh xạ các view từ XML
        btnBack = view.findViewById(R.id.btnBackVip);
        btnUpgrade = view.findViewById(R.id.btnUpgradeVip);

        // 3. Xử lý sự kiện nút Quay lại
        btnBack.setOnClickListener(v -> {
            // Kiểm tra xem FragmentManager có tồn tại không và thực hiện quay lại
>>>>>>> 556415d64adb3e497a21be13cd4f6b0536fd6cc0
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

<<<<<<< HEAD
        bottomSheetDialog.show();
    }
}
=======
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
>>>>>>> 556415d64adb3e497a21be13cd4f6b0536fd6cc0
