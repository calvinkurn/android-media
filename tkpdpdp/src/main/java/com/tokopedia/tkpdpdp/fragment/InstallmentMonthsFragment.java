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
import com.tokopedia.tkpdpdp.adapter.MonthsInstallmentAdapter;

import java.util.ArrayList;

/**
 * @author by alifa on 5/17/17.
 */

public class InstallmentMonthsFragment extends Fragment {

    private static final String ARGS_MONTH_INSTALLMENT = "ARGS_MONTH_INSTALLMENT";
    private RecyclerView recyclerView;

    private ArrayList<MonthsInstallmentItem> monthsInstallmentItems;

    public InstallmentMonthsFragment() {
    }

    public static InstallmentMonthsFragment newInstance(ArrayList<MonthsInstallmentItem> listItems) {
        InstallmentMonthsFragment installmentMonthsFragment = new InstallmentMonthsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARGS_MONTH_INSTALLMENT, listItems);
        installmentMonthsFragment.setArguments(bundle);
        return installmentMonthsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            monthsInstallmentItems = getArguments().getParcelableArrayList(ARGS_MONTH_INSTALLMENT);
        } else {
            monthsInstallmentItems = new ArrayList<>();
        }
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
        MonthsInstallmentAdapter installmentAdapter = new MonthsInstallmentAdapter(getActivity());
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(installmentAdapter);
        DividerItemDecoration itemDecoration
                = new DividerItemDecoration(getActivity(), R.drawable.divider300);

        recyclerView.addItemDecoration(itemDecoration);
        installmentAdapter.setData(monthsInstallmentItems);
    }


}
