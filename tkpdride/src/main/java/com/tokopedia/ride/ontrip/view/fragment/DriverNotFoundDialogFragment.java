package com.tokopedia.ride.ontrip.view.fragment;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author alvarisi
 */


public class DriverNotFoundDialogFragment extends DialogFragment {
    public static final int BOOK_AGAIN_RESULT_CODE = 1011;
    @BindView(R2.id.btn_book_again)
    Button bookAgainButton;

    public DriverNotFoundDialogFragment() {
    }

    public static DriverNotFoundDialogFragment newInstance() {
        DriverNotFoundDialogFragment fragment = new DriverNotFoundDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_not_found, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R2.id.btn_book_again)
    public void actionBtnTryAgainClicked() {
        Intent intent = getActivity().getIntent();
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                BOOK_AGAIN_RESULT_CODE,
                intent
        );
        dismiss();
    }
}
