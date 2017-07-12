package com.tokopedia.profilecompletion.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract;
import com.tokopedia.session.R;

import java.text.DateFormatSymbols;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by stevenfredian on 7/3/17.
 */

public class ProfileCompletionDateFragment extends BaseDaggerFragment {


    public static final String TAG = "date";
    private AutoCompleteTextView month;
    private View actvContainer;
    private TextInputEditText year;
    private TextInputEditText date;
    private ProfileCompletionFragment view;
    private View proceed;
    private View skip;
    private View progress;
    private int position;
    private Unbinder unbinder;
    private ProfileCompletionContract.Presenter presenter;

    public static ProfileCompletionDateFragment createInstance(ProfileCompletionFragment view) {
        ProfileCompletionDateFragment fragment = new ProfileCompletionDateFragment();
        fragment.view = view;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_profile_completion_dob, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        initView(parentView);
        setViewListener();
        initialVar();
        return parentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected void initView(View view) {
        date = (TextInputEditText) view.findViewById(R.id.date);
        month = (AutoCompleteTextView) view.findViewById(R.id.month);
        year = (TextInputEditText) view.findViewById(R.id.year);
        actvContainer = view.findViewById(R.id.autoCompleteTextViewContainer);
        proceed = this.view.getView().findViewById(R.id.proceed);
        skip = this.view.getView().findViewById(R.id.skip);
        progress = this.view.getView().findViewById(R.id.progress);
    }

    protected void setViewListener() {
        actvContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month.showDropDown();
                KeyboardHandler.DropKeyboard(getActivity(), getView());
            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month.showDropDown();
                KeyboardHandler.DropKeyboard(getActivity(), getView());
            }
        });

        month.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long rowId) {
                position = -1;
                position = pos+1;
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.editUserInfo(date.getText().toString(), position, year.getText().toString());
            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.skipView(TAG);
            }
        });
    }

    protected void initialVar() {
        String[] monthsIndo = new DateFormatSymbols(Locale.getDefault()).getMonths();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), R.layout.select_dialog_item_material, monthsIndo);
        month.setAdapter(adapter);
        presenter = view.getPresenter();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
