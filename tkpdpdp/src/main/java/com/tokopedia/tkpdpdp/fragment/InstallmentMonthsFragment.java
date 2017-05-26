package com.tokopedia.tkpdpdp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.product.model.etalase.MonthsInstallmentItem;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.R2;
import com.tokopedia.tkpdpdp.adapter.MonthsInstallmentAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alifa on 5/17/17.
 */

public class InstallmentMonthsFragment extends Fragment {

    private RecyclerView recyclerView;

    private final ArrayList<MonthsInstallmentItem> monthsInstallmentItems;

    private MonthsInstallmentAdapter monthsInstallmentAdapter;

    public InstallmentMonthsFragment(ArrayList<MonthsInstallmentItem> monthsInstallmentItems) {
        this.monthsInstallmentItems = monthsInstallmentItems;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_months_installment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setupRecyclerView();
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.courier_list);
    }

    private void setupRecyclerView() {
        monthsInstallmentAdapter = new MonthsInstallmentAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(monthsInstallmentAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider300));
        monthsInstallmentAdapter.setData(monthsInstallmentItems);
    }


}
