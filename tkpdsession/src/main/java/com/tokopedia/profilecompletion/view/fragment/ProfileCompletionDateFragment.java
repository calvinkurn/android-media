package com.tokopedia.profilecompletion.view.fragment;

import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.profilecompletion.view.util.Events;
import com.tokopedia.profilecompletion.view.util.Properties;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract;
import com.tokopedia.session.R;

import java.text.DateFormatSymbols;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func3;


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
    private Observable<String> dateObservable;
    private Observable<String> yearObservable;
    private Observable<Integer> monthObservable;

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
        setUpFields();
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
//        skip = this.view.getSkipButton();
        progress = this.view.getView().findViewById(R.id.progress);
        proceed.setEnabled(false);
        this.view.canProceed(false);

        Drawable drawable = MethodChecker.getDrawable(getActivity(), R.drawable.chevron_thin_down);
        drawable.setColorFilter(MethodChecker.getColor(getActivity(), R.color.warm_grey), PorterDuff.Mode.SRC_IN);
        double size = drawable.getIntrinsicWidth()*0.3;
        drawable.setBounds(0, 0, (int)(size), (int)(size));

        month.setCompoundDrawables(null, null, drawable, null);
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

        dateObservable = Events.text(date);
        yearObservable = Events.text(year);
        monthObservable = Events.select(month);

        Observable<Boolean> dateMapper = dateObservable.map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String text) {
                return !text.trim().equals("");
            }
        });


        Observable<Boolean> yearMapper = yearObservable.map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String text) {
                return !text.trim().equals("");
            }
        });

        Observable<Boolean> monthMapper = monthObservable.map(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                position = integer;
                return integer!=0;
            }
        });

        Observable<Boolean> allField = Observable.combineLatest(dateMapper, yearMapper, monthMapper, new Func3<Boolean, Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean date, Boolean year, Boolean month) {
                return date && month && year;
            }
        }).map(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean) {
                return aBoolean;
            }
        });

        allField.subscribe(Properties.enabledFrom(proceed));
        allField.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                view.canProceed(aBoolean);
            }
        });
    }


    private void setUpFields() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
