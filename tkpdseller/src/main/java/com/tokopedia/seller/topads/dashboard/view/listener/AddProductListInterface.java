package com.tokopedia.seller.topads.dashboard.view.listener;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.List;

/**
 * Created by normansyahputa on 2/13/17.
 */

public interface AddProductListInterface extends ActivityItemSelection {

    int RESULT_CODE = 9912;

    void hideBottom();

    void showBottom();

    void removeSelection(TopAdsProductViewModel data);

    void addSelection(TopAdsProductViewModel data);

    int sizeSelection();

    List<TopAdsProductViewModel> selections();

    boolean isHideEtalase();

    boolean isExistingGroup();

    boolean isSelectionViewShown();

    void setSubmitButtonEnabled(boolean enabled);

    void setSubmitButtonVisibility(int visibility);
}