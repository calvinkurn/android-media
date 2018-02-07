package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.kol;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentProductViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentViewModel;

/**
 * @author by nisie on 10/31/17.
 */

public interface KolTypeFactory {

    int type(KolCommentViewModel viewModel);

    int type(KolCommentHeaderViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
