package com.tokopedia.seller.topads.listener;

import com.tokopedia.seller.topads.view.models.TopAdsProductViewModel;
import com.tkpd.library.utils.image.ImageHandler;
import java.util.List;

/**
 * Created by normansyahputa on 2/13/17.
 */

public interface AddProductListInterface extends ActivityItemSelection {

    String EXTRA_SELECTIONS = "EXTRA_SELECTIONS";

    ImageHandler imageHandler();

    void hideBottomBecauseEmpty();

    void removeSelection(TopAdsProductViewModel data);

    void addSelection(TopAdsProductViewModel data);

    int sizeSelection();

    List<TopAdsProductViewModel> selections();
}
