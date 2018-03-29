package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

/**
 * @author anggaprasetiyo on 11/12/17.
 */

public class HeaderViewModel implements Parcelable, Visitable<HomeTypeFactory> {

    public static final Creator<HeaderViewModel> CREATOR = new Creator<HeaderViewModel>() {
        @Override
        public HeaderViewModel createFromParcel(Parcel in) {
            return new HeaderViewModel(in);
        }

        @Override
        public HeaderViewModel[] newArray(int size) {
            return new HeaderViewModel[size];
        }
    };
    private HomeHeaderWalletAction homeHeaderWalletActionData;
    private TokoPointDrawerData tokoPointDrawerData;
    private CashBackData cashBackData;
    private boolean pendingTokocashChecked;
    private boolean isWalletError;
    private boolean isTokoPointError;

    public HeaderViewModel() {
    }

    protected HeaderViewModel(Parcel in) {
        homeHeaderWalletActionData = in.readParcelable(HomeHeaderWalletAction.class.getClassLoader());
        tokoPointDrawerData = in.readParcelable(TokoPointDrawerData.class.getClassLoader());
        cashBackData = in.readParcelable(CashBackData.class.getClassLoader());
        pendingTokocashChecked = in.readByte() != 0;
        isWalletError = in.readByte() != 0;
        isTokoPointError = in.readByte() != 0;
    }

    public HomeHeaderWalletAction getHomeHeaderWalletActionData() {
        return homeHeaderWalletActionData;
    }

    public void setHomeHeaderWalletActionData(HomeHeaderWalletAction homeHeaderWalletActionData) {
        this.homeHeaderWalletActionData = homeHeaderWalletActionData;
    }

    public TokoPointDrawerData getTokoPointDrawerData() {
        return tokoPointDrawerData;
    }

    public void setTokoPointDrawerData(TokoPointDrawerData tokoPointDrawerData) {
        this.tokoPointDrawerData = tokoPointDrawerData;
    }

    public CashBackData getCashBackData() {
        return cashBackData;
    }

    public void setCashBackData(CashBackData cashBackData) {
        this.cashBackData = cashBackData;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public boolean isPendingTokocashChecked() {
        return pendingTokocashChecked;
    }

    public void setPendingTokocashChecked(boolean pendingTokocashChecked) {
        this.pendingTokocashChecked = pendingTokocashChecked;
    }


    public void setWalletDataSuccess() {
        this.isWalletError = false;
    }

    public void setWalletDataError() {
        this.isWalletError = true;
    }

    public boolean isWalletDataError() {
        return isWalletError;
    }

    public void setTokoPointDataSuccess() {
        this.isTokoPointError = false;
    }

    public void setTokoPointDataError() {
        this.isTokoPointError = true;
    }

    public boolean isTokoPointDataError() {
        return isTokoPointError;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(homeHeaderWalletActionData, i);
        parcel.writeParcelable(tokoPointDrawerData, i);
        parcel.writeParcelable(cashBackData, i);
        parcel.writeByte((byte) (pendingTokocashChecked ? 1 : 0));
        parcel.writeByte((byte) (isWalletError ? 1 : 0));
        parcel.writeByte((byte) (isTokoPointError ? 1 : 0));
    }
}
