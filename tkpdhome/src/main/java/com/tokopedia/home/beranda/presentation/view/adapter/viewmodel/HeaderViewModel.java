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
    /*public static final int TYPE_TOKOCASH_ONLY = 1;
    public static final int TYPE_TOKOPINT_ONLY = 3;
    public static final int TYPE_EMPTY = 4;
    public static final int TYPE_TOKOCASH_WITH_TOKOPOINT = 2;*/

    public static final Creator<HeaderViewModel> CREATOR = new Creator<HeaderViewModel>() {
        @Override
        public HeaderViewModel createFromParcel(Parcel source) {
            return new HeaderViewModel(source);
        }

        @Override
        public HeaderViewModel[] newArray(int size) {
            return new HeaderViewModel[size];
        }
    };
    private HomeHeaderWalletAction homeHeaderWalletActionData;
    private TokoPointDrawerData tokoPointDrawerData;
    private CashBackData cashBackData;
    private int tokocashNetworkStatus;
    private int tokopointNetworkStatus;
    //    private int type;
    private boolean pendingTokocashChecked;

    public HeaderViewModel() {
    }

    protected HeaderViewModel(Parcel in) {
        this.homeHeaderWalletActionData = in.readParcelable(HomeHeaderWalletAction.class.getClassLoader());
        this.tokoPointDrawerData = in.readParcelable(TokoPointDrawerData.class.getClassLoader());
        this.cashBackData = in.readParcelable(CashBackData.class.getClassLoader());
//        this.type = in.readInt();
        this.pendingTokocashChecked = in.readByte() != 0;
    }

    public HomeHeaderWalletAction getHomeHeaderWalletActionData() {
        return homeHeaderWalletActionData;
    }

    public void setHomeHeaderWalletActionData(HomeHeaderWalletAction homeHeaderWalletActionData) {
       /* if (homeHeaderWalletActionData != null && tokoPointDrawerData != null
                && tokoPointDrawerData.getOffFlag() == 0)
            this.type = TYPE_TOKOCASH_WITH_TOKOPOINT;
        if (homeHeaderWalletActionData == null && tokoPointDrawerData != null
                && tokoPointDrawerData.getOffFlag() == 0)
            this.type = TYPE_TOKOPINT_ONLY;
        if (homeHeaderWalletActionData != null &&
                (tokoPointDrawerData == null || tokoPointDrawerData.getOffFlag() == 1))
            this.type = TYPE_TOKOCASH_ONLY;
        if (homeHeaderWalletActionData == null && tokoPointDrawerData == null)
            this.type = TYPE_EMPTY;*/
        this.homeHeaderWalletActionData = homeHeaderWalletActionData;
    }

    /*public int getType() {
        return type;*/
//    }

    public TokoPointDrawerData getTokoPointDrawerData() {
        return tokoPointDrawerData;
    }

    public void setTokoPointDrawerData(TokoPointDrawerData tokoPointDrawerData) {
       /* if (tokoPointDrawerData != null && tokoPointDrawerData.getOffFlag() == 0
                && homeHeaderWalletActionData != null)
            this.type = TYPE_TOKOCASH_WITH_TOKOPOINT;
        if (tokoPointDrawerData != null && tokoPointDrawerData.getOffFlag() == 0
                && homeHeaderWalletActionData == null)
            this.type = TYPE_TOKOPINT_ONLY;
        if ((tokoPointDrawerData == null || tokoPointDrawerData.getOffFlag() == 1)
                && homeHeaderWalletActionData != null)
            this.type = TYPE_TOKOCASH_ONLY;
        if (tokoPointDrawerData == null && homeHeaderWalletActionData == null)
            this.type = TYPE_EMPTY;*/
        this.tokoPointDrawerData = tokoPointDrawerData;
    }

   /* public void setType(int type) {
        this.type = type;
    }*/

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.homeHeaderWalletActionData, flags);
        dest.writeParcelable(this.tokoPointDrawerData, flags);
        dest.writeParcelable(this.cashBackData, flags);
//        dest.writeInt(this.type);
        dest.writeByte(this.pendingTokocashChecked ? (byte) 1 : (byte) 0);
    }

    public int getTokocashNetworkStatus() {
        return tokocashNetworkStatus;
    }

    public void setTokocashNetworkStatus(int tokocashNetworkStatus) {
        this.tokocashNetworkStatus = tokocashNetworkStatus;
    }

    public int getTokopointNetworkStatus() {
        return tokopointNetworkStatus;
    }

    public void setTokopointNetworkStatus(int tokopointNetworkStatus) {
        this.tokopointNetworkStatus = tokopointNetworkStatus;
    }
}
