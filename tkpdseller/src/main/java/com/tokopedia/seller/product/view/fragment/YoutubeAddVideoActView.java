package com.tokopedia.seller.product.view.fragment;

import java.util.List;

/**
 * Created by normansyahputa on 4/17/17.
 */

public interface YoutubeAddVideoActView {
    YoutubeAddVideoFragment youtubeAddVideoFragment();

    void openAddYoutubeDialog();

    List<String> videoIds();
}
