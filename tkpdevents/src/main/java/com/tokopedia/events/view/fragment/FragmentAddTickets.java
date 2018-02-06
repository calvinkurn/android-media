package com.tokopedia.events.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.events.R;
import com.tokopedia.events.view.adapter.AddTicketAdapter;
import com.tokopedia.events.view.presenter.EventBookTicketPresenter;
import com.tokopedia.events.view.viewmodel.PackageViewModel;

import java.util.List;

public class FragmentAddTickets extends Fragment {
    private static final String ARG_PARAM1 = "typecount";

    private int mTicketTypeCount;
    private List<PackageViewModel> mPackages;

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

    public void setData(List<PackageViewModel> packages, EventBookTicketPresenter presenter) {
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
        scrollView.setAdapter(new AddTicketAdapter(getActivity(), mPackages, mPresenter));
        scrollView.setHasFixedSize(true);
        scrollView.setNestedScrollingEnabled(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recycler_view_divider));
        scrollView.addItemDecoration(dividerItemDecoration);

        return scrollView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
