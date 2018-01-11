package com.tokopedia.session.changephonenumber.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.activity
        .ChangePhoneNumberEmailVerificationActivity;

import java.util.ArrayList;

/**
 * Created by milhamj on 11/01/18.
 */

public class ChangePhoneNumberEmailFragment extends TkpdBaseV4Fragment {
    public static final String PARAM_PHONE_NUMBER = "phone_number";
    public static final String PARAM_WARNING_LIST = "warning_list";
    public static final String PARAM_EMAIL = "email";

    private TextView sendButton;

    private String phoneNumber;
    private ArrayList<String> warningList;
    private String email;

    public static ChangePhoneNumberEmailFragment newInstance(String phoneNumber, String email,
                                                             ArrayList<String> warningList) {
        ChangePhoneNumberEmailFragment fragment = new ChangePhoneNumberEmailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putStringArrayList(PARAM_WARNING_LIST, warningList);
        bundle.putString(PARAM_EMAIL, email);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_change_phone_number_email,
                container, false);
        initVar();
        initView(parentView);
        setViewListener();

        return parentView;
    }

    private void initVar() {
        phoneNumber = getArguments().getString(PARAM_PHONE_NUMBER);
        warningList = getArguments().getStringArrayList(PARAM_WARNING_LIST);
        email = getArguments().getString(PARAM_EMAIL);
    }

    private void initView(View view) {
        TextView verificationInstruction = view.findViewById(R.id.verification_instruction);
        sendButton = view.findViewById(R.id.send_button);

        String verificationText = String.format("%s <b>%s</b>",
                getString(R.string.we_will_send_email_to),
                email
        );
        verificationInstruction.setText(MethodChecker.fromHtml(verificationText));
    }

    private void setViewListener() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ChangePhoneNumberEmailVerificationActivity.newInstance(getContext(),
                        phoneNumber,
                        email,
                        warningList);
                intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
