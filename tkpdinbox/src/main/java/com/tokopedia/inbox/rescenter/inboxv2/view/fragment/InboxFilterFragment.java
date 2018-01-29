package com.tokopedia.inbox.rescenter.inboxv2.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.InboxFilterActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.InboxFilterFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.presenter.InboxFilterFragmentPresenter;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxFilterModel;

import javax.inject.Inject;

/**
 * Created by yfsx on 29/01/18.
 */

public class InboxFilterFragment
        extends BaseDaggerFragment
        implements InboxFilterFragmentListener.View {

    private Button btnFinish;
    private EditText etDateFrom, etDateTo;
    private RecyclerView rvFilter;

    private ResoInboxFilterModel inboxFilterModel;

    @Inject
    InboxFilterFragmentPresenter presenter;

    public static Fragment getFragmentInstance(Bundle bundle) {
        Fragment fragment = new ResoInboxFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    public static Fragment getResetFragmentInstance() {
        Fragment fragment = new ResoInboxFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxFilterActivity.PARAM_FILTER_MODEL, new ResoInboxFilterModel());
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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inboxFilterModel = getArguments().getParcelable(InboxFilterActivity.PARAM_FILTER_MODEL);

        bindView();
        bindViewListener();
    }

    private void bindView() {

    }

    private void bindViewListener() {
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent output = new Intent();
                output.putExtra(InboxFilterActivity.PARAM_FILTER_MODEL, generateResult());
                getActivity().setResult(Activity.RESULT_OK, output);
                getActivity().finish();
            }
        });
    }

    private ResoInboxFilterModel generateResult() {
        ResoInboxFilterModel inboxFilterModel = new ResoInboxFilterModel();

        return inboxFilterModel;
    }
}
