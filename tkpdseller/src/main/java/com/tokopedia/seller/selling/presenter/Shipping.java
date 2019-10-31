package com.tokopedia.seller.selling.presenter;

import android.app.Dialog;
import android.content.Context;
import androidx.appcompat.view.ActionMode;
import android.widget.EditText;

import com.tokopedia.core.session.baseFragment.BaseImpl;

import java.util.List;

/**
 * Created by Toped10 on 7/28/2016.
 */
public abstract class Shipping extends BaseImpl<ShippingView> {


    public Shipping(ShippingView view) {
        super(view);
    }

    public abstract void cancelShipping(EditText remark, int pos, Dialog dialog, Context context);

    public abstract void checkValidationToSendGoogleAnalytic(boolean isVisibleToUser, Context context);

    public abstract void getShippingList(boolean isVisibleToUser);

    public abstract void onRefreshHandler();

    public abstract void requestRefNumDialog(int pos, Context context);

    public abstract void onOpenDetail(int pos, Context context);

    public abstract void onScrollView(boolean isLastVisibleItem);

    public abstract void onMultiConfirm(ActionMode actionMode, List<Integer> selecteds);

    public abstract void updateRefNumBarcode(int getBarcodePosition, String barcode);

    public abstract void doRefresh();

    public abstract void onQueryTextSubmit(String query);

    public abstract void onQueryTextChange(String newText);

    public abstract void updateListDataChecked(int position, boolean selected);

    public abstract void updateListDataChecked(boolean b);

    public abstract void moveToDetail(int position);

    public abstract void onFinishConnection();
}
