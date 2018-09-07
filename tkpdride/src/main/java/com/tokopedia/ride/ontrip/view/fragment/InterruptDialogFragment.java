package com.tokopedia.ride.ontrip.view.fragment;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.activity.RideWebViewActivity;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterruptDialogFragment extends DialogFragment {
    private static final String TAG = "IntprConfirmationDialog";

    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_VALUE = "EXTRA_VALUE";
    public static final String EXTRA_KEY = "EXTRA_KEY";
    private boolean isProgramaticallyDismissed = false;

    private TextView labelDescriptionTextView;
    private Button cancelButton;
    private Button acceptButton;

    private String url;
    private String key;
    private String value;

    public InterruptDialogFragment() {
        // Required empty public constructor
    }

    public static InterruptDialogFragment newInstance(String key, String value, String url) {
        InterruptDialogFragment fragment = new InterruptDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_KEY, key);
        bundle.putString(EXTRA_VALUE, value);
        bundle.putString(EXTRA_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);
        return inflater.inflate(R.layout.fragment_interrupt_dialog, container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString(EXTRA_URL);
        key = getArguments().getString(EXTRA_KEY);
        value = getArguments().getString(EXTRA_VALUE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        labelDescriptionTextView = (TextView) view.findViewById(R.id.tv_label_description);
        cancelButton = (Button) view.findViewById(R.id.btn_cancel);
        acceptButton = (Button) view.findViewById(R.id.btn_ok);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProgramaticallyDismissed = true;
                Intent intent = getActivity().getIntent();
                intent.putExtra(EXTRA_KEY, key);
                intent.putExtra(EXTRA_VALUE, value);
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        intent
                );
                dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProgramaticallyDismissed = true;
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_CANCELED,
                        null);
                dismiss();
            }
        });

        labelDescriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RideWebViewActivity.getCallingIntent(getActivity(), url);
                startActivity(intent);
            }
        });

        String full = "By clicking Booking Ride,\nI agree with terms and conditions.";
        String keyword = "terms and conditions";
        int startIndex = indexOfSearchQuery(keyword, full);
        if (startIndex == -1) {
            labelDescriptionTextView.setText(full);
        } else {
            SpannableString highlightedTitle = new SpannableString(full);
            highlightedTitle.setSpan(new TextAppearanceSpan(getActivity(), R.style.searchTextPrimaryHiglight),
                    0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            highlightedTitle.setSpan(new TextAppearanceSpan(getActivity(), R.style.searchTextPrimaryHiglight),
                    startIndex + keyword.length(),
                    full.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            labelDescriptionTextView.setText(highlightedTitle);
        }
    }

    private int indexOfSearchQuery(String keyword, String full) {
        if (!TextUtils.isEmpty(keyword)) {
            return full.toLowerCase(Locale.getDefault()).indexOf(keyword.toLowerCase(Locale.getDefault()));
        }
        return -1;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    // To dismiss the fragment when the back-button is pressed.
                    dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    @Override
    public void dismiss() {
        if (isProgramaticallyDismissed) {
            super.dismiss();
        } else {
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_CANCELED,
                    null);

            super.dismiss();
        }
    }
}
