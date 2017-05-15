package com.tokopedia.seller.product.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.view.model.AddUrlVideoModel;

import java.util.List;

/**
 * @author normansyahputa on 4/17/17.
 */
public interface YoutubeAddVideoView extends CustomerView {
    int REQUEST_CODE_GET_VIDEO = 1;
    String KEY_VIDEOS_LINK = "KEY_VIDEOS_LINK";

    void addAddUrlVideModel(AddUrlVideoModel addUrlVideoModel);

    void addYoutubeUrl(String youtubeUrl);

    void showMessageError(String message);

    void showMessageErrorRaw(String message);

    void addAddUrlVideModels(List<AddUrlVideoModel> convert);

    void showLoading();

    void hideLoading();
}
