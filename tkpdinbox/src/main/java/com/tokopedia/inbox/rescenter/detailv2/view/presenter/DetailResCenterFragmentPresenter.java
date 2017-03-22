package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

/**
 * Created by hangnadi on 3/8/17.
 */

public interface DetailResCenterFragmentPresenter {

    void setOnFirstTimeLaunch();

    void refreshPage();

    void finishReturProduct();

    void acceptSolution();

    void cancelResolution();

    void askHelpResolution();

    void trackReturProduck(String shipmentID, String shipmentRef);

}
