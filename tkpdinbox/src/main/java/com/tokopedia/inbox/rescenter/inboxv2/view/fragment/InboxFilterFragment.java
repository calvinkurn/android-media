package com.tokopedia.inbox.rescenter.inboxv2.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.InboxFilterActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.InboxFilterAdapter;
import com.tokopedia.inbox.rescenter.inboxv2.view.utils.DatePickerDialog;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxFilterModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yfsx on 29/01/18.
 */

public class InboxFilterFragment
        extends BaseDaggerFragment {

    public static final String FORMAT_DATE = "dd/MM/yyyy";
    public static final String FORMAT_DATE_API = "dd/MM/yyyy hh:mm:ss";

    private Button btnFinish;
    private EditText etDateFrom, etDateTo;
    private RecyclerView rvFilter;
    private ImageView icCloseFrom, icCloseTo;

    private ResoInboxFilterModel inboxFilterModel;
    private InboxFilterAdapter adapter;


    public static Fragment getFragmentInstance(Bundle bundle) {
        Fragment fragment = new InboxFilterFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    public static Fragment getResetFragmentInstance(ResoInboxFilterModel filterModel) {
        Fragment fragment = new InboxFilterFragment();
        Bundle bundle = new Bundle();
        ResoInboxFilterModel inboxFilterModel = new ResoInboxFilterModel();
        inboxFilterModel.setFilterViewModelList(filterModel.getFilterViewModelList());
        bundle.putParcelable(InboxFilterActivity.PARAM_FILTER_MODEL, inboxFilterModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_reso_filter, container,false);
        btnFinish = (Button) view.findViewById(R.id.btn_finish);
        etDateFrom = (EditText) view.findViewById(R.id.et_date_from);
        etDateTo = (EditText) view.findViewById(R.id.et_date_to);
        rvFilter = (RecyclerView) view.findViewById(R.id.rv_filter);
        icCloseFrom = (ImageView) view.findViewById(R.id.ic_close_from);
        icCloseTo = (ImageView) view.findViewById(R.id.ic_close_to);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            inboxFilterModel = getArguments().getParcelable(InboxFilterActivity.PARAM_FILTER_MODEL);
        } else {
            inboxFilterModel = new ResoInboxFilterModel();
        }
        bindView();
        bindViewListener();
    }

    private void bindView() {
        adapter = new InboxFilterAdapter(inboxFilterModel);
        rvFilter.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFilter.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        etDateTo.setFocusable(false);
        etDateFrom.setFocusable(false);
        updateView();
    }

    private void bindViewListener() {
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent output = new Intent();
                output.putExtra(InboxFilterActivity.PARAM_FILTER_MODEL, generateResult());
                getActivity().setResult(Activity.RESULT_OK, output);
                getActivity().finish();
                getBottomBackSheetActivityTransition();
            }
        });

        icCloseFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inboxFilterModel.setDateFrom(null);
                inboxFilterModel.setDateFromString("");
                updateView();
            }
        });

        icCloseTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inboxFilterModel.setDateTo(null);
                inboxFilterModel.setDateToString("");
                updateView();
            }
        });

        etDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(inboxFilterModel.getDateFrom() != null ?
                        inboxFilterModel.getDateFrom() :
                        new Date());
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), calendar,
                        new DatePickerDialog.OnDateSetListener(inboxFilterModel.getDateFrom(), FORMAT_DATE) {
                            @Override
                            public void onDateUpdated(Date date) {
                                super.onDateUpdated(date);
                                inboxFilterModel.setDateFrom(date);
                                updateView();
                            }
                });
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                if (inboxFilterModel.getDateTo() != null) {
                    dialog.getDatePicker().setMaxDate(inboxFilterModel.getDateTo().getTime());
                }
                dialog.show();
            }
        });

        etDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(inboxFilterModel.getDateTo() != null ?
                        inboxFilterModel.getDateTo() :
                        new Date());

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), calendar,
                        new DatePickerDialog.OnDateSetListener(inboxFilterModel.getDateTo(), FORMAT_DATE) {
                            @Override
                            public void onDateUpdated(Date date) {
                                super.onDateUpdated(date);
                                inboxFilterModel.setDateTo(date);
                                updateView();
                            }
                        });
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                if (inboxFilterModel.getDateFrom() != null) {
                    dialog.getDatePicker().setMinDate(inboxFilterModel.getDateFrom().getTime());
                }
                dialog.show();
            }
        });
    }


    private void updateView() {
        enableButton(btnFinish);
        if (inboxFilterModel.getDateFrom() != null) {
            icCloseFrom.setVisibility(View.VISIBLE);
            etDateFrom.setText(convertDateToString(inboxFilterModel.getDateFrom()));
            if (inboxFilterModel.getDateTo() == null)
                disableButton(btnFinish);
        } else {
            icCloseFrom.setVisibility(View.GONE);
            etDateFrom.setText("");
        }

        if (inboxFilterModel.getDateTo() != null) {
            icCloseTo.setVisibility(View.VISIBLE);
            etDateTo.setText(convertDateToString(inboxFilterModel.getDateTo()));
            if (inboxFilterModel.getDateFrom() == null)
                disableButton(btnFinish);
        } else {
            icCloseTo.setVisibility(View.GONE);
            etDateTo.setText("");
        }
    }

    private ResoInboxFilterModel generateResult() {
        ResoInboxFilterModel inboxFilterModel = adapter.getInboxFilterModel();
        if (this.inboxFilterModel.getDateTo() != null) {
            inboxFilterModel.setDateTo(this.inboxFilterModel.getDateTo());
            inboxFilterModel.setDateToString(convertDateToString(this.inboxFilterModel.getDateTo(), FORMAT_DATE_API));
        }
        if (this.inboxFilterModel.getDateFrom() != null) {
            inboxFilterModel.setDateFrom(this.inboxFilterModel.getDateFrom());
            inboxFilterModel.setDateFromString(convertDateToString(this.inboxFilterModel.getDateFrom(), FORMAT_DATE_API));
        }
        return inboxFilterModel;
    }

    private String convertDateToString(Date date) {
        return convertDateToString(date, FORMAT_DATE);
    }

    private String convertDateToString(Date date, String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        String dateString = format.format(date);
        return dateString;
    }

    public void getBottomBackSheetActivityTransition() {
        getActivity().overridePendingTransition(R.anim.push_down, R.anim.pull_up);
    }

    private void disableButton(Button button) {
        button.setEnabled(false);
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_38));
        button.setBackground(MethodChecker.getDrawable(getActivity(),
                R.drawable.bg_button_disable));
    }

    private void enableButton(Button button) {
        button.setEnabled(true);
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        button.setBackground(MethodChecker.getDrawable(getActivity(),
                R.drawable.bg_button_enable));
    }
}
