package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

/**
 * Created by hangnadi on 3/8/17.
 */

public interface DetailResCenterFragmentPresenter {

    void setOnFirstTimeLaunch();

    void refreshPage();

    void finishReturProduct();

    void acceptSolution();

    void acceptAdminSolution();

    void cancelResolution();

    void askHelpResolution();

    void trackReturProduck(String shipmentID, String shipmentRef);

    void inputAddressAcceptSolution(String addressId);

    void inputAddressMigrateVersion(String addressId);

    void actionEditAddress(String addressId, String oldAddressId, String conversationId);

    void inputAddressAcceptAdminSolution(String addressId);

    void setOnDestroyView();
}
