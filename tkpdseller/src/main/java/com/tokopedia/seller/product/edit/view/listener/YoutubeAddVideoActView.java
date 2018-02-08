package com.tokopedia.seller.product.edit.view.listener;

import com.tokopedia.seller.product.edit.view.fragment.YoutubeAddVideoFragment;

import java.util.List;

/**
 * Created by normansyahputa on 4/17/17.
 */

public interface YoutubeAddVideoActView {
    YoutubeAddVideoFragment youtubeAddVideoFragment();

    void openAddYoutubeDialog();

    List<String> videoIds();

    void addVideoIds(String videoId);

    void removeVideoIds(int index);

    void removeVideoId(String videoIds);
}
