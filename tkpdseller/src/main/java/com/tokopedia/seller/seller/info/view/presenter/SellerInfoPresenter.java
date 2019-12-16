package com.tokopedia.seller.seller.info.view.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.seller.seller.info.data.model.DataList;
import com.tokopedia.seller.seller.info.data.model.NotificationUpdateActionResponse;
import com.tokopedia.seller.seller.info.data.model.ResponseSellerInfoModel;
import com.tokopedia.seller.seller.info.domain.interactor.MarkReadNotificationUseCase;
import com.tokopedia.seller.seller.info.domain.interactor.SellerCenterUseCase;
import com.tokopedia.seller.seller.info.view.SellerInfoView;
import com.tokopedia.seller.seller.info.view.model.SellerInfoModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 12/5/17.
 */

public class SellerInfoPresenter extends BaseDaggerPresenter<SellerInfoView> {
    private SellerCenterUseCase sellerCenterUseCase;
    private MarkReadNotificationUseCase markReadNotificationUseCase;

    @Inject
    public SellerInfoPresenter(
            SellerCenterUseCase sellerCenterUseCase,
            MarkReadNotificationUseCase markReadNotificationUseCase
    ) {
        this.sellerCenterUseCase = sellerCenterUseCase;
        this.markReadNotificationUseCase = markReadNotificationUseCase;
    }

    public void getSellerInfoList(int page, String lastNotifId) {
        sellerCenterUseCase.execute(SellerCenterUseCase.Companion.createRequestParams(page, lastNotifId),
                new Subscriber<ResponseSellerInfoModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonUtils.dumper(e);
                        if (isViewAttached()) {
                            getView().onLoadSearchError(e);
                        }
                    }

                    @Override
                    public void onNext(ResponseSellerInfoModel response) {
                        if (isViewAttached()) {
                            List<SellerInfoModel> result = conv(response);

                            getView().onSearchLoaded(result, result.size(), response.getNotifData().getPaging().getHasNext());
                        }
                    }
                });
    }

    private List<SellerInfoModel> conv(ResponseSellerInfoModel response) {
        List<SellerInfoModel> res = new ArrayList<>();
        for (DataList list : response.getNotifData().getList()) {
            res.add(conv(list));
        }
        return res;
    }

    private SellerInfoModel conv(DataList list) {
        SellerInfoModel sellerInfoModel = new SellerInfoModel();
        sellerInfoModel.setInfoId(list.getNotifId());
        sellerInfoModel.setContent(list.getContent());
        sellerInfoModel.setCreateTimeUnix(list.getCreateTimeUnix());
        sellerInfoModel.setTitle(list.getTitle());
        sellerInfoModel.setInfoThumbnailUrl(list.getDataNotification().getInfoThumbnailUrl());
        sellerInfoModel.setExternalLink(list.getDataNotification().getDesktopLink());
        sellerInfoModel.setRead(list.getReadStatusInfo());
        sellerInfoModel.setStatus(list.getStatus());
        sellerInfoModel.setNotifId(list.getNotifId());

        SellerInfoModel.Section section = new SellerInfoModel.Section();
        section.setIconUrl(list.getSectionIcon());
        section.setName(list.getSectionName());
        section.setSectionId(list.getSectionId());

        sellerInfoModel.setSection(section);
        return sellerInfoModel;
    }

    public void markReadNotification(String infoId) {
        markReadNotificationUseCase.execute(
                MarkReadNotificationUseCase.createRequestParams(String.valueOf(infoId)),
                new Subscriber<NotificationUpdateActionResponse>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onNext(NotificationUpdateActionResponse notificationUpdateActionResponse) {}
                });
    }
}
