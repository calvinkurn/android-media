package com.tokopedia.seller.product.view.fragment;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.view.model.AddUrlVideoModel;

/**
 * @author normansyahputa on 4/17/17.
 */
public interface YoutubeAddVideoView extends CustomerView {
    void addAddUrlVideModel(AddUrlVideoModel addUrlVideoModel);

    void addYoutubeUrl(String youtubeUrl);

    void showMessageError(String message);
}
