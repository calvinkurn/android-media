package com.tokopedia.seller.topads.dashboard.view.listener;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.List;

/**
 * Created by normansyahputa on 2/13/17.
 */

public interface AddProductListInterface extends ActivityItemSelection {

    int RESULT_CODE = 9912;

    ImageHandler imageHandler();

    void hideBottom();

    void showBottom();

    void removeSelection(TopAdsProductViewModel data);

    void addSelection(TopAdsProductViewModel data);

    int sizeSelection();

    List<TopAdsProductViewModel> selections();

    void disableNextButton();

    void enableNextButton();

    boolean isHideEtalase();

    boolean isExistingGroup();

    boolean isSelectionViewShown();

    void showNextButton();

    void dismissNextButton();
}
