package com.tokopedia.inbox.rescenter.product.view.presenter;

/**
 * Created by hangnadi on 3/23/17.
 */

public interface ListProductFragmentPresenter {
    void onFirstTimeLaunch();

    void refreshPage();

    void setOnDestroyView();
}
