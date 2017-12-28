package com.tokopedia.session.changephonenumber.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.di.SessionComponent;
import com.tokopedia.di.SessionModule;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.customview.BottomSheetInfo;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by milhamj on 20/12/17.
 */

public class ChangePhoneNumberInputFragment extends BaseDaggerFragment implements ChangePhoneNumberInputFragmentListener.View {
    public static final String PARAM_PHONE_NUMBER = "phone_number";
    public static final String PARAM_HAS_TOKOCASH = "has_tokocash";
    public static final String PARAM_WARNING_LIST = "warning_list";

    @Inject
    ChangePhoneNumberInputFragmentListener.Presenter presenter;
    private TextView oldPhoneNumber;
    private EditText newPhoneNumber;
    private TextView nextButton;
    private String phoneNumber;
    private boolean hasTokocash;
    private ArrayList<String> warningList;
    private Unbinder unbinder;
    private BottomSheetInfo bottomSheetInfo;

    public static ChangePhoneNumberInputFragment newInstance(String phoneNumber, boolean hasTokocash, ArrayList<String> warningList) {
        ChangePhoneNumberInputFragment fragment = new ChangePhoneNumberInputFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putBoolean(PARAM_HAS_TOKOCASH, hasTokocash);
        bundle.putStringArrayList(PARAM_WARNING_LIST, warningList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_change_phone_number_input, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        initView(parentView);
        setViewListener();
        initVar();

        if (warningList != null) {
            if (warningList.size() > 0) {
                setHasOptionsMenu(true);
                createBottomSheetView();
            }
        }

        presenter.attachView(this);
        return parentView;
    }

    private void initView(View view) {
        oldPhoneNumber = view.findViewById(R.id.old_phone_number_value);
        newPhoneNumber = view.findViewById(R.id.new_phone_number_value);
        nextButton = view.findViewById(R.id.next_button);

        oldPhoneNumber.setText(phoneNumber);
    }

    private void setViewListener() {
        newPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.onNewNumberTextChanged(editable);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO do something here
                Log.d("milhamj", "Halooooo");
            }
        });
    }

    private void initVar() {
        phoneNumber = getArguments().getString(PARAM_PHONE_NUMBER);
        hasTokocash = getArguments().getBoolean(PARAM_HAS_TOKOCASH, false);
        warningList = getArguments().getStringArrayList(PARAM_WARNING_LIST);
    }

    private void createBottomSheetView() {
        bottomSheetInfo = new BottomSheetInfo(getContext(), hasTokocash, warningList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_change_phone_number_input, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            bottomSheetInfo.show();
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        SessionComponent sessionComponent =
                DaggerSessionComponent.builder()
                        .appComponent(appComponent)
                        .sessionModule(new SessionModule())
                        .build();
        sessionComponent.inject(this);
    }

    @Override
    public void enableNextButton() {
        nextButton.setClickable(true);
        nextButton.setEnabled(true);
        nextButton.setBackground(MethodChecker.getDrawable(getContext(), R.drawable.green_button_rounded_unify));
        nextButton.setTextColor(MethodChecker.getColor(getContext(), R.color.white));
    }

    @Override
    public void disableNextButton() {
        nextButton.setClickable(false);
        nextButton.setEnabled(false);
        nextButton.setBackground(MethodChecker.getDrawable(getContext(), R.drawable.grey_button_rounded));
        nextButton.setTextColor(MethodChecker.getColor(getContext(), R.color.black_12));
    }
}
