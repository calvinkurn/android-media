package com.tokopedia.seller.purchase.detail.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.purchase.detail.activity.CustomScannerBarcodeActivity;

import org.jetbrains.annotations.NotNull;

/**
 * Created by kris on 1/9/18. Tokopedia
 */

public class ChangeAwbFragment extends TkpdFragment {

    private static final String ORDER_ID_ARGUMENT = "ORDER_ID_ARGUMENT";
    private static final String REF_NUMBER_ARGUMENT = "REF_NUMBER_ARGUMENT";

    private ChangeAwbListener listener;

    private EditText refNumberField;

    private PermissionCheckerHelper permissionCheckerHelper;

    @Override
    protected String getScreenName() {
        return null;
    }

    public static ChangeAwbFragment createFragment(String orderId, String refNumber) {
        ChangeAwbFragment fragment = new ChangeAwbFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_ARGUMENT, orderId);
        bundle.putString(REF_NUMBER_ARGUMENT, refNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ChangeAwbListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ChangeAwbListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_awb_fragment, container, false);
        TextInputLayout textInputLayout = view.findViewById(R.id.notes_text_input_layout);
        refNumberField = view.findViewById(R.id.cancel_order_notes_field);
        ImageView scanBarcodeButton = view.findViewById(R.id.scan_barcode_button);
        Button changeRefConfirmButton = view.findViewById(R.id.cancel_order_confirm_button);
        refNumberField.setText(getArguments().getString(REF_NUMBER_ARGUMENT));
        changeRefConfirmButton
                .setOnClickListener(onChangeAwbButtonClicked(refNumberField, textInputLayout));
        scanBarcodeButton.setOnClickListener(onBarcodeScannerClickedListener());
        permissionCheckerHelper = new PermissionCheckerHelper();
        return view;
    }

    private View.OnClickListener onChangeAwbButtonClicked(
            final EditText refNumberField,
            final TextInputLayout RefNumberInputLayout) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (refNumberField.getText().toString().isEmpty()) {
                    RefNumberInputLayout.setError(
                            getActivity()
                                    .getString(com.tokopedia.core2.R.string.error_note_empty)
                    );
                } else {
                    if (getArguments() != null) {
                        listener.changeAwb(getArguments().getString(ORDER_ID_ARGUMENT),
                                refNumberField.getText().toString());
                    }
                }
            }
        };
    }

    public interface ChangeAwbListener {
        void changeAwb(String orderId, String refNumber);
    }

    private View.OnClickListener onBarcodeScannerClickedListener() {
        return view -> {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                permissionCheckerHelper.checkPermissions(getActivity(), getPermissions(), new PermissionCheckerHelper.PermissionCheckListener() {
                    @Override
                    public void onPermissionDenied(@NotNull String permissionText) {
                        permissionCheckerHelper.onPermissionDenied(getActivity(), permissionText);
                    }

                    @Override
                    public void onNeverAskAgain(@NotNull String permissionText) {
                        permissionCheckerHelper.onNeverAskAgain(getActivity(), permissionText);
                    }

                    @Override
                    public void onPermissionGranted() {
                        onScanBarcode();
                    }
                }, "");
            } else {
                onScanBarcode();
            }
        };
    }

    private void onScanBarcode() {
        CommonUtils.requestBarcodeScanner(this, CustomScannerBarcodeActivity.class);
    }

    private String[] getPermissions() {
        return new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refNumberField.setText(CommonUtils.getBarcode(requestCode, resultCode, data));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
    }

}
