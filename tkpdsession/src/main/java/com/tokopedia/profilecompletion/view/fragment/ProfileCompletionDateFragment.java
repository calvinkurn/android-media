package com.tokopedia.profilecompletion.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.profilecompletion.view.listener.ProfileListener;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionPresenter;
import com.tokopedia.session.R;

import java.text.DateFormatSymbols;
import java.util.Locale;


/**
 * Created by stevenfredian on 7/3/17.
 */

public class ProfileCompletionDateFragment extends BasePresenterFragment<ProfileCompletionPresenter> {


    public static final String TAG = "date";
    private AutoCompleteTextView month;
    private View actvContainer;
    private TextInputEditText year;
    private TextInputEditText date;
    private ProfileCompletionFragment view;
    private View proceed;

    public static ProfileCompletionDateFragment createInstance(ProfileCompletionFragment view) {
        ProfileCompletionDateFragment fragment = new ProfileCompletionDateFragment();
        fragment.view = view;
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile_completion_dob;
    }

    @Override
    protected void initView(View view) {
        month = (AutoCompleteTextView) view.findViewById(R.id.month);
        actvContainer = view.findViewById(R.id.autoCompleteTextViewContainer);
        proceed = this.view.getView().findViewById(R.id.proceed);
    }

    @Override
    protected void setViewListener() {
        actvContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month.showDropDown();
            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month.showDropDown();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] fruits = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Pear"};
            }
        });
    }

    @Override
    protected void initialVar() {
        String[] fruits = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Pear"};
        String[] monthsIndo = new DateFormatSymbols(Locale.getDefault()).getMonths();
        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), R.layout.select_dialog_item_material, monthsIndo);
        //Getting the instance of AutoCompleteTextView

//        actv.setThreshold(1);//will start working from first character
        month.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
    }

    @Override
    protected void setActionVar() {

    }
}
