package com.tokopedia.events.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.view.adapter.AddTicketAdapter;
import com.tokopedia.events.view.presenter.EventBookTicketPresenter;
import com.tokopedia.events.view.viewmodel.PackageViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentAddTickets extends Fragment {
    private static final String ARG_PARAM1 = "typecount";

    private int mTicketTypeCount;
    private List<PackageViewModel> mPackages;

//    EventComponent eventComponent;
//    @Inject
    EventBookTicketPresenter mPresenter;


    public FragmentAddTickets() {
        // Required empty public constructor
    }

    public static FragmentAddTickets newInstance(int typecount) {
        FragmentAddTickets fragment = new FragmentAddTickets();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, typecount);
        fragment.setArguments(args);
        return fragment;
    }

    public void setData(List<PackageViewModel> packages, EventBookTicketPresenter presenter){
        this.mPresenter = presenter;
        this.mPackages = packages;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTicketTypeCount = getArguments().getInt(ARG_PARAM1);
        }

//        executeInjector();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView scrollView = (RecyclerView) inflater.inflate(R.layout.fragment_add_tickets, container, false);
        scrollView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        scrollView.setAdapter(new AddTicketAdapter(getActivity(),mPackages,mPresenter));

        return scrollView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    private void executeInjector() {
//        if (eventComponent == null) initInjector();
//        eventComponent.inject(this);
//    }
//
//    private void initInjector() {
//        eventComponent = DaggerEventComponent.builder()
//                .appComponent(((MainApplication) getActivity().getApplication()).getAppComponent())
//                .build();
//    }

}
