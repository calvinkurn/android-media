package com.tokopedia.inbox.rescenter.inbox.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.tokopedia.core2.R;
import com.tokopedia.inbox.rescenter.inbox.presenter.InboxResCenterPresenter;
import com.tokopedia.inbox.rescenter.utils.LocalCacheManager;

/**
 * Created on 4/9/16.
 */
public class BottomSheetFilterDialog {

    private final Spinner filterRead, filterStatus, filterTime;
    private final Button submit, reset;
    private final Activity activity;
    private final BottomSheetDialog dialog;
    private final LocalCacheManager.Filter cacheManager;

    private ArrayAdapter<CharSequence> adapterStatus, adapterRead, adapterTime;

    @SuppressLint("InflateParams")
    public BottomSheetFilterDialog(Activity activity, String tabName, String tabType) {
        this.activity = activity;
        this.dialog = new BottomSheetDialog(activity);
        this.dialog.setContentView(R.layout.layout_filter_inbox_rescenter);
        filterRead = (Spinner) dialog.findViewById(R.id.filter_status_read);
        filterStatus = (Spinner) dialog.findViewById(R.id.filter_status_reso);
        filterTime = (Spinner) dialog.findViewById(R.id.filter_time);
        submit = (Button) dialog.findViewById(R.id.submit);
        reset = (Button) dialog.findViewById(R.id.action_reset);
        cacheManager = LocalCacheManager.Filter.Builder(tabType, tabName).getCache();
        initAdapter();
        setAdapter();
    }

    public static BottomSheetFilterDialog Builder(Activity activity, String tabType, String tabName) {
        return new BottomSheetFilterDialog(activity, tabName, tabType);
    }

    public void initAdapter() {
        adapterRead =  ArrayAdapter.createFromResource(activity, R.array.rescenter_read, R.layout.dialog_item);
        adapterStatus = ArrayAdapter.createFromResource(activity, R.array.rescenter_status, R.layout.dialog_item);
        adapterTime =  ArrayAdapter.createFromResource(activity, R.array.rescenter_time, R.layout.dialog_item);

        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterRead.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void setAdapter() {
        filterRead.setAdapter(adapterRead);
        filterStatus.setAdapter(adapterStatus);
        filterTime.setAdapter(adapterTime);
    }

    public void setListener(final InboxResCenterPresenter presenter) {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cacheManager.setFilterRead((filterRead.getSelectedItemPosition() == 2 ? 3 : filterRead.getSelectedItemPosition()))
                            .setFilterReadText((String) filterRead.getSelectedItem())
                            .setFilterStatus(filterStatus.getSelectedItemPosition())
                            .setFilterStatusText((String) filterStatus.getSelectedItem())
                            .setFilterTime(filterTime.getSelectedItemPosition())
                            .setFilterTimeText((String) filterTime.getSelectedItem())
                            .save();
                    dismissDialog();
                    presenter.setActionOnFilterClick(activity);
                } catch (Exception e) {
                    dismissDialog();
                    e.printStackTrace();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterRead.setSelection(0, true);
                filterStatus.setSelection(0, true);
                filterTime.setSelection(0, true);
            }
        });
    }

    public void setView() {
        filterRead.setSelection(cacheManager.getFilterRead() == 3 ? 2 : cacheManager.getFilterRead(), true);
        filterStatus.setSelection(cacheManager.getFilterStatus(), true);
        filterTime.setSelection(cacheManager.getFilterTime(), true);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog dialog = (BottomSheetDialog) dialogInterface;
                FrameLayout frameLayout = (FrameLayout) dialog.findViewById(R.id.design_bottom_sheet);
                if (frameLayout != null) {
                    BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(frameLayout);
                    behavior.setHideable(false);
                }
            }
        });
    }

    public void show() {
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public void setOnDestroy() {
        cacheManager.clear();
    }
}
