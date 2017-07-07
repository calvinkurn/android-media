package com.tokopedia.profilecompletion.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.BasePresenterFragment;
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
    private View skip;
    private int position;

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
        presenter = view.getPresenter();
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
        date = (TextInputEditText) view.findViewById(R.id.date);
        month = (AutoCompleteTextView) view.findViewById(R.id.month);
        year = (TextInputEditText) view.findViewById(R.id.year);
        actvContainer = view.findViewById(R.id.autoCompleteTextViewContainer);
        proceed = this.view.getView().findViewById(R.id.proceed);
        skip = this.view.getView().findViewById(R.id.skip);
    }

    @Override
    protected void setViewListener() {
        actvContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month.showDropDown();
                KeyboardHandler.DropKeyboard(context, getView());
            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month.showDropDown();
                KeyboardHandler.DropKeyboard(context, getView());
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

    @Override
    protected void initialVar() {
        String[] monthsIndo = new DateFormatSymbols(Locale.getDefault()).getMonths();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), R.layout.select_dialog_item_material, monthsIndo);
        month.setAdapter(adapter);
    }

    @Override
    protected void setActionVar() {

    }
}
