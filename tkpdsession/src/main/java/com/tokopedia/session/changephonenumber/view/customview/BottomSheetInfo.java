package com.tokopedia.session.changephonenumber.view.customview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.adapter.WarningListAdapter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by milhamj on 21/12/17.
 */

public class BottomSheetInfo extends BottomSheetDialog {
    @Inject
    WarningListAdapter adapter;
    private Context context;
    private List<String> viewModelList;
    private RecyclerView warningRecyclerView;
    private ImageView closeIcon;

    public BottomSheetInfo(@NonNull Context context, @NonNull List<String> viewModelList) {
        super(context);
        if (viewModelList.size() < 1)
            throw new IllegalStateException("You have to provide the list of warnings!");
        this.context = context;
        this.viewModelList = viewModelList;
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        View bottomSheetView = ((Activity) context).getLayoutInflater().inflate(R.layout
                .bottom_sheet_info, null);
        setContentView(bottomSheetView);

        warningRecyclerView = bottomSheetView.findViewById(R.id.warning_rv);
        closeIcon = bottomSheetView.findViewById(R.id.close_icon);

        warningRecyclerView.setFocusable(false);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetInfo.this.dismiss();
            }
        });
        populateRecyclerView();
    }

    private void populateRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager
                .VERTICAL, false);
        warningRecyclerView.setLayoutManager(mLayoutManager);

        WarningListAdapter adapter = new WarningListAdapter();
        adapter.addData(viewModelList);
        warningRecyclerView.setAdapter(adapter);
    }
}
