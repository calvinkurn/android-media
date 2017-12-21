package com.tokopedia.session.changephonenumber.view.customview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.adapter.WarningListAdapter;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningItemViewModel;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import java.util.List;

/**
 * Created by milhamj on 21/12/17.
 */

public class BottomSheetInfo extends BottomSheetDialog {
    private Context context;
    private List<WarningItemViewModel> viewModelList;
    private RecyclerView warningRecyclerView;

    public BottomSheetInfo(@NonNull Context context, @NonNull List<WarningItemViewModel> viewModelList) {
        super(context);
        if (viewModelList.size()<1)
            throw new IllegalStateException("You have to provide the list of warnings!");
        this.context = context;
        this.viewModelList = viewModelList;
        init();
    }

    private void init() {
        View bottomSheetView = ((Activity) context).getLayoutInflater().inflate(R.layout.bottom_sheet_info, null);
        setContentView(bottomSheetView);

        warningRecyclerView = bottomSheetView.findViewById(R.id.warning_rv);

        populateRecyclerView();
    }

    private void populateRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        warningRecyclerView.setLayoutManager(mLayoutManager);

        WarningListAdapter adapter = new WarningListAdapter(viewModelList);
        warningRecyclerView.setAdapter(adapter);
    }
}
