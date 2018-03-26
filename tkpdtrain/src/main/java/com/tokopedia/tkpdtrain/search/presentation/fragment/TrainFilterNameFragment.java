package com.tokopedia.tkpdtrain.search.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.search.presentation.adapter.TrainFilterAdapter;
import com.tokopedia.tkpdtrain.search.presentation.contract.FilterSearchActionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 3/23/18.
 */

public class TrainFilterNameFragment extends BaseDaggerFragment {

    public static final String TAG = TrainFilterNameFragment.class.getSimpleName();
    private static final String NAME_LIST = "name_list";

    private VerticalRecyclerView recyclerView;
    private TrainFilterAdapter adapter;
    private FilterSearchActionView listener;

    public static TrainFilterNameFragment newInstance(List<String> nameTrainList) {
        TrainFilterNameFragment fragment = new TrainFilterNameFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(NAME_LIST, (ArrayList) nameTrainList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_filter_item_search, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listener.setTitleToolbar("Kereta");
        adapter = new TrainFilterAdapter();
        adapter.addList(getArguments().getStringArrayList(NAME_LIST));
        adapter.setListener(new TrainFilterAdapter.ActionListener() {
            @Override
            public void onCheckChanged(String itemSelected) {
                Toast.makeText(getActivity(), itemSelected, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void onAttachActivity(Context context) {
        listener = (FilterSearchActionView) context;
    }
}
