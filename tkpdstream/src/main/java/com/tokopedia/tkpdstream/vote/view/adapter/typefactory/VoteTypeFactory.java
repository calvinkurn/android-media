package com.tokopedia.tkpdstream.vote.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

/**
 * @author by StevenFredian on 21/02/18.
 */

public interface VoteTypeFactory{

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(VoteViewModel voteViewModel);

}
