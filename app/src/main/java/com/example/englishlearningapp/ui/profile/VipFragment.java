package com.example.englishlearningapp.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.utils.UserStateManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class VipFragment extends Fragment {

    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vip, container, false);

        toolbar = view.findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> {
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
            if (btn.getText().toString().equalsIgnoreCase("Chọn") || 
                btn.getText().toString().equalsIgnoreCase("Nâng cấp ngay")) {
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

            // LOGIC GLOBAL STATE: Cập nhật level mới
            UserStateManager.getInstance().updateLevel("VIP");

            Toast.makeText(getContext(), "Đã chọn thanh toán qua: " + method + ". Nâng cấp VIP thành công!", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();

            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

        bottomSheetDialog.show();
    }
}
