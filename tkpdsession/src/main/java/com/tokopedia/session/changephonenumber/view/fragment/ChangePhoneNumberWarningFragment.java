package com.tokopedia.session.changephonenumber.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.activity.ChangePhoneNumberInputActivity;
import com.tokopedia.session.changephonenumber.view.adapter.WarningListAdapter;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningItemViewModel;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by milhamj on 18/12/17.
 */

public class ChangePhoneNumberWarningFragment extends BaseDaggerFragment implements ChangePhoneNumberWarningFragmentListener.View {

    private RelativeLayout tokopediaBalanceLayout;
    private RelativeLayout tokocashLayout;
    private TextView tokopediaBalanceValue;
    private TextView tokocashValue;
    private RecyclerView warningRecyclerView;
    private TextView nextButton;

    private WarningViewModel viewModel;
    private Unbinder unbinder;

    //TODO use DI
//    @Inject
//    public WarningListAdapter adapter;

    public static ChangePhoneNumberWarningFragment newInstance() {
        ChangePhoneNumberWarningFragment fragment = new ChangePhoneNumberWarningFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_change_phone_number_warning, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        initView(parentView);
        setViewListener();
        initVar();
        //TODO presenter.attachView(this);
        provideDummyData();
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDataToView();
    }

    //TODO remove this function
    private void provideDummyData() {
        WarningItemViewModel item1 = new WarningItemViewModel(
                "Saldo Tokopedia tidak dapat ditarik dan digunakan selama 3 hari setelah nomor berhasil diubah.",
                "Disarankan untuk menarik Saldo Tokopedia sebelum melakukan perubahan",
                null
        );
        WarningItemViewModel item2 = new WarningItemViewModel(
                "Dana dari Tokocash sebelumnya tidak dapat dipindahkan ke Tokocash dengan nomor yang baru.*",
                "Disarankan untuk membelanjakan seluruh dana TokoCash terlebih dahulu, atau tetap login TokoCash dengan nomor lama.",
                "*Pemindahan dana hanya bisa dilakukan untuk kasus-kasus tertentu"
        );
        ArrayList<WarningItemViewModel> arrayList = new ArrayList<>();
        arrayList.add(item1);
        arrayList.add(item2);
        arrayList.add(item2);
        arrayList.add(item1);
        viewModel = new WarningViewModel("Rp 123.333,333", "Rp 123.333,333", arrayList);
    }

    private void initView(View view) {
        tokopediaBalanceLayout = view.findViewById(R.id.tokopedia_balance_layout);
        tokocashLayout = view.findViewById(R.id.tokocash_layout);
        tokopediaBalanceValue = view.findViewById(R.id.tokopedia_balance_value);
        tokocashValue = view.findViewById(R.id.tokocash_value);
        warningRecyclerView = view.findViewById(R.id.warning_rv);
        nextButton = view.findViewById(R.id.next_button);

        warningRecyclerView.setFocusable(false);
    }

    private void setViewListener() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ChangePhoneNumberInputActivity.newInstance(
                        getContext(),
                        new ArrayList<>(viewModel.getWarningList()))
                );
            }
        });
    }

    private void initVar() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //TODO presenter.detachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    private void loadDataToView() {
        if (viewModel != null) {
            if (viewModel.getTokopediaBalance() == null || viewModel.getTokopediaBalance().equalsIgnoreCase("null")) {
                tokopediaBalanceLayout.setVisibility(View.GONE);
            } else {
                tokopediaBalanceLayout.setVisibility(View.VISIBLE);
                tokopediaBalanceValue.setText(viewModel.getTokopediaBalance());
            }

            if (viewModel.getTokocash() == null || viewModel.getTokocash().equalsIgnoreCase("null")) {
                tokocashLayout.setVisibility(View.GONE);
            } else {
                tokocashLayout.setVisibility(View.VISIBLE);
                tokocashValue.setText(viewModel.getTokocash());
            }

            populateRecyclerView();
        }
    }

    private void populateRecyclerView() {
        if (viewModel != null) {
            if (viewModel.getWarningList().size() > 0) {
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                warningRecyclerView.setLayoutManager(mLayoutManager);

                WarningListAdapter adapter = new WarningListAdapter();
                adapter.addData(viewModel.getWarningList());
                warningRecyclerView.setAdapter(adapter);
            }
        }
    }
}
