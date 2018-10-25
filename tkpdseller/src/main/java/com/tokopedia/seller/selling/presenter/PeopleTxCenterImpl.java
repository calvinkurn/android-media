package com.tokopedia.seller.selling.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.drawer2.data.factory.NotificationSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.NotificationMapper;
import com.tokopedia.core.drawer2.data.mapper.TopChatNotificationMapper;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.data.repository.NotificationRepositoryImpl;
import com.tokopedia.core.drawer2.data.source.TopChatNotificationSource;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.util.GlobalConfig;

import rx.Subscriber;

/**
 * Created by Toped10 on 7/15/2016.
 */
public class PeopleTxCenterImpl extends PeopleTxCenter {

    private Bundle bundle;
    private NotificationUseCase notificationUseCase;

    public PeopleTxCenterImpl(PeopleTxCenterView view) {
        super(view);

        LocalCacheHandler cacheHandler =
                new LocalCacheHandler(view.getActivity(), DrawerHelper.DRAWER_CACHE);

        NotificationService notificationService = new NotificationService();
        NotificationSourceFactory notificationSourceFactory =
                new NotificationSourceFactory(
                        view.getActivity(),
                        notificationService,
                        new NotificationMapper(),
                        cacheHandler
                );

        ChatService chatService = new ChatService();
        TopChatNotificationMapper topChatNotificationMapper = new TopChatNotificationMapper();

        TopChatNotificationSource topChatNotificationSource = new TopChatNotificationSource(
                chatService, topChatNotificationMapper, cacheHandler
        );

        NotificationRepository notificationRepository =
                new NotificationRepositoryImpl(notificationSourceFactory, topChatNotificationSource);
        this.notificationUseCase =
                new NotificationUseCase(
                        new JobExecutor(),
                        new UIThread(),
                        notificationRepository);
    }

    @Override
    public String getMessageTAG() {
        return null;
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return null;
    }

    @Override
    public void initData(@NonNull Context context) {
        view.loadData();
        view.initView();
    }

    @Override
    public void fetchArguments(Bundle argument) {
        this.bundle = argument;
    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {

    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {

    }

    @Override
    public void initDataInstance(Context context) {
        view.initHandlerAndAdapter();
        checkCondition();
        checkValidationToSendGoogleAnalytic(view.getVisibleUserHint(), context);
    }

    private void checkCondition() {
        if (bundle.getString("type").equals("people")) {
            view.setCondition1();
        } else {
            view.setCondition2();
        }
    }

    @Override
    public void setLocalyticFlow(Activity context) {
        try {
            String screenName = context.getString(R.string.transaction_buy_page);

            switch (view.getState()) {
                case "people":
                    screenName = context.getString(R.string.transaction_buy_page);
                    break;
                case "shop":
                    screenName = context.getString(R.string.transaction_sell_page);
                    break;
            }

            sendToGTM(screenName, context);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
    }

    private void sendToGTM(String screenName, Context context) {

    }

    @Override
    public void checkValidationToSendGoogleAnalytic(boolean isVisibleToUser, Context context) {
        if (isVisibleToUser && context != null) {
        }
    }

    @Override
    public void refreshData() {
        notificationUseCase.execute(
                notificationUseCase.getRequestParam(
                        GlobalConfig.isSellerApp()), onGetNotification(view.getActivity()));
    }

    @Override
    public void onDestroyView() {
        notificationUseCase.unsubscribe();
    }

    private Subscriber<NotificationModel> onGetNotification(final Context context) {
        return new Subscriber<NotificationModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.onErrorRefresh(ErrorHandler.getErrorMessage(e));
            }

            @Override
            public void onNext(NotificationModel notificationModel) {
                view.onSuccessRefresh();
            }
        };
    }
}
