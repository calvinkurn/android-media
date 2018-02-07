package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AcceptAdminSolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AcceptSolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AskHelpResolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.CancelResolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.EditAddressUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.FinishReturSolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResCenterDetailUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResCenterDetailV2UseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.InputAddressUseCase;
import com.tokopedia.inbox.rescenter.detailv2.view.DetailResCenterFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.
        GetResCenterDetailV2Subscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.ResolutionActionSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.TrackAwbReturProductSubscriber;
import com.tokopedia.inbox.rescenter.historyawb.domain.interactor.TrackAwbReturProductUseCase;

import javax.inject.Inject;

/**
 * Created by hangnadi on 3/9/17.
 */

public class DetailResCenterFragmentImpl implements DetailResCenterFragmentPresenter {

    private final DetailResCenterFragmentView fragmentView;
    private final GetResCenterDetailUseCase getResCenterDetailUseCase;
    private final GetResCenterDetailV2UseCase getResCenterDetailV2UseCase;
    private final TrackAwbReturProductUseCase trackAwbReturProductUseCase;
    private final CancelResolutionUseCase cancelResolutionUseCase;
    private final AskHelpResolutionUseCase askHelpResolutionUseCase;
    private final FinishReturSolutionUseCase finishReturSolutionUseCase;
    private final AcceptAdminSolutionUseCase acceptAdminSolutionUseCase;
    private final AcceptSolutionUseCase acceptSolutionUseCase;
    private final InputAddressUseCase inputAddressUseCase;
    private final EditAddressUseCase editAddressUseCase;

    @Inject
    public DetailResCenterFragmentImpl(DetailResCenterFragmentView fragmentView,
                                       GetResCenterDetailUseCase getResCenterDetailUseCase,
                                       GetResCenterDetailV2UseCase getResCenterDetailV2UseCase,
                                       TrackAwbReturProductUseCase trackAwbReturProductUseCase,
                                       CancelResolutionUseCase cancelResolutionUseCase,
                                       AskHelpResolutionUseCase askHelpResolutionUseCase,
                                       FinishReturSolutionUseCase finishReturSolutionUseCase,
                                       AcceptAdminSolutionUseCase acceptAdminSolutionUseCase,
                                       AcceptSolutionUseCase acceptSolutionUseCase,
                                       InputAddressUseCase inputAddressUseCase,
                                       EditAddressUseCase editAddressUseCase) {
        this.fragmentView = fragmentView;
        this.getResCenterDetailUseCase = getResCenterDetailUseCase;
        this.getResCenterDetailV2UseCase = getResCenterDetailV2UseCase;
        this.trackAwbReturProductUseCase = trackAwbReturProductUseCase;
        this.cancelResolutionUseCase = cancelResolutionUseCase;
        this.askHelpResolutionUseCase = askHelpResolutionUseCase;
        this.finishReturSolutionUseCase = finishReturSolutionUseCase;
        this.acceptAdminSolutionUseCase = acceptAdminSolutionUseCase;
        this.acceptSolutionUseCase = acceptSolutionUseCase;
        this.inputAddressUseCase = inputAddressUseCase;
        this.editAddressUseCase = editAddressUseCase;
    }

    @Override
    public void setOnFirstTimeLaunch() {
        fragmentView.showLoading(true);
        getResCenterDetailV2UseCase.execute(getInitResCenterDetailParam(),
                new GetResCenterDetailV2Subscriber(fragmentView));
    }

    private RequestParams getInitResCenterDetailParam() {
        String resolutionID = fragmentView.getResolutionID();
        RequestParams params = RequestParams.create();
        params.putString(GetResCenterDetailUseCase.PARAM_RESOLUTION_ID, resolutionID);
        return params;
    }

    @Override
    public void refreshPage() {
        setOnFirstTimeLaunch();
    }

    @Override
    public void finishReturProduct() {
        fragmentView.showLoadingDialog(true);
        finishReturSolutionUseCase.execute(getFinishReturSolutionParam(),
                new ResolutionActionSubscriber(fragmentView, DetailResCenterFragment.ACTION_FINISH));
    }

    private RequestParams getFinishReturSolutionParam() {
        RequestParams params = RequestParams.create();
        params.putString(CancelResolutionUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        return params;
    }

    @Override
    public void acceptSolution() {
        acceptSolutionUseCase.execute(getAcceptSolutionParam(),
                new ResolutionActionSubscriber(fragmentView, DetailResCenterFragment.ACTION_ACCEPT));
    }

    private RequestParams getAcceptSolutionParam() {
        RequestParams params = RequestParams.create();
        params.putString(AcceptSolutionUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        return params;
    }

    @Override
    public void acceptAdminSolution() {
        acceptAdminSolutionUseCase.execute(getAcceptAdminSolutionParam(),
                new ResolutionActionSubscriber(fragmentView, DetailResCenterFragment.ACTION_ACCEPT));
    }

    private RequestParams getAcceptAdminSolutionParam() {
        RequestParams params = RequestParams.create();
        params.putString(AcceptAdminSolutionUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        return params;
    }

    @Override
    public void cancelResolution() {
        fragmentView.showLoadingDialog(true);
        cancelResolutionUseCase.execute(getCancelResolutionParam(),
                new ResolutionActionSubscriber(fragmentView, DetailResCenterFragment.ACTION_CANCEL));
    }

    private RequestParams getCancelResolutionParam() {
        RequestParams params = RequestParams.create();
        params.putString(CancelResolutionUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        return params;
    }

    @Override
    public void askHelpResolution() {
        fragmentView.showLoadingDialog(true);
        askHelpResolutionUseCase.execute(getAskHelpResolutionParam(),
                new ResolutionActionSubscriber(fragmentView, DetailResCenterFragment.ACTION_HELP));
    }

    private RequestParams getAskHelpResolutionParam() {
        RequestParams params = RequestParams.create();
        params.putString(AskHelpResolutionUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        return params;
    }

    @Override
    public void trackReturProduck(String shipmentID, String shipmentRef) {
        fragmentView.showLoadingDialog(true);
        trackAwbReturProductUseCase.execute(getTrackingAwbProductParam(shipmentID, shipmentRef),
                new TrackAwbReturProductSubscriber(fragmentView));
    }

    private RequestParams getTrackingAwbProductParam(String shipmentID, String shipmentRef) {
        RequestParams params = RequestParams.create();
        params.putString(TrackAwbReturProductUseCase.PARAM_SHIPMENT_ID, shipmentID);
        params.putString(TrackAwbReturProductUseCase.PARAM_SHIPPING_REFENCE, shipmentRef);
        return params;
    }

    @Override
    public void inputAddressAcceptSolution(String addressId) {
        fragmentView.showLoadingDialog(true);
        inputAddressUseCase.execute(getInputAddressParam(addressId, InputAddressUseCase.DEFAULT_BY_PASS),
                new ResolutionActionSubscriber(fragmentView, DetailResCenterFragment.ACTION_INPUT_ADDRESS));
    }

    @Override
    public void inputAddressAcceptAdminSolution(String addressId) {
        fragmentView.showLoadingDialog(true);
        inputAddressUseCase.execute(getInputAddressParam(addressId, InputAddressUseCase.ADMIN_BY_PASS),
                new ResolutionActionSubscriber(fragmentView, DetailResCenterFragment.ACTION_INPUT_ADDRESS));
    }

    private RequestParams getInputAddressParam(String addressId, int paramByPass) {
        RequestParams params = RequestParams.create();
        params.putString(InputAddressUseCase.PARAM_ADDRESS_ID, addressId);
        params.putInt(InputAddressUseCase.PARAM_BYPASS, paramByPass);
        params.putString(InputAddressUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        params.putInt(InputAddressUseCase.PARAM_NEW_ADDRESS, 1);
        return params;
    }

    @Override
    public void inputAddressMigrateVersion(String addressId) {
        fragmentView.showLoadingDialog(true);
        inputAddressUseCase.execute(getInputAddressMigrateVersionParam(addressId),
                new ResolutionActionSubscriber(fragmentView, DetailResCenterFragment.ACTION_INPUT_ADDRESS));
    }

    private RequestParams getInputAddressMigrateVersionParam(String addressId) {
        RequestParams params = RequestParams.create();
        params.putString(InputAddressUseCase.PARAM_ADDRESS_ID, addressId);
        params.putString(InputAddressUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        params.putInt(InputAddressUseCase.PARAM_NEW_ADDRESS, 1);
        return params;
    }

    @Override
    public void actionEditAddress(String addressId, String oldAddressId, String conversationId) {
        fragmentView.showLoadingDialog(true);
        editAddressUseCase.execute(getEditAddressParam(addressId, oldAddressId, conversationId),
                new ResolutionActionSubscriber(fragmentView, DetailResCenterFragment.ACTION_EDIT_ADDRESS));
    }

    private RequestParams getEditAddressParam(String addressId, String oldAddressId, String conversationId) {
        RequestParams params = RequestParams.create();
        params.putString(EditAddressUseCase.PARAM_CONVERSATION_ID, conversationId);
        params.putString(EditAddressUseCase.PARAM_ADDRESS_ID, addressId);
        params.putString(EditAddressUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        params.putString(EditAddressUseCase.PARAM_OLD_DATA, oldAddressId + "-" + conversationId);
        return params;
    }

    @Override
    public void setOnDestroyView() {
        unsubscribeObservable();
    }

    private void unsubscribeObservable() {
        getResCenterDetailUseCase.unsubscribe();
        getResCenterDetailV2UseCase.unsubscribe();
        trackAwbReturProductUseCase.unsubscribe();
        cancelResolutionUseCase.unsubscribe();
        askHelpResolutionUseCase.unsubscribe();
        finishReturSolutionUseCase.unsubscribe();
        acceptAdminSolutionUseCase.unsubscribe();
        acceptSolutionUseCase.unsubscribe();
        inputAddressUseCase.unsubscribe();
        editAddressUseCase.unsubscribe();

    }

}
