package com.tokopedia.seller.selling.presenter;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.presenter.BaseView;

/**
 * Created by Toped10 on 7/15/2016.
 */
public interface PeopleTxCenterView extends BaseView{
    int PEOPLE_CONFIRM = 2;
    int PEOPLE_VERIFICATION = 3;
    int PEOPLE_STATUS = 4;
    int PEOPLE_CANCEL = 5;
    int PEOPLE_ACCEPT = 6;
    String SHOP = "shop";

    void initView();

    void initHandlerAndAdapter();

    void setCondition1();

    void setCondition2();

    boolean getVisibleUserHint();

    String getState();

    void loadData();

    Activity getActivity();

    void onErrorRefresh(String errorMessage);

    void onSuccessRefresh();
}
