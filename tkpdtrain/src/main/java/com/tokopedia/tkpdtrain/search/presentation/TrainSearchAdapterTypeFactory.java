package com.tokopedia.tkpdtrain.search.presentation;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by nabillasabbaha on 3/14/18.
 */

public class TrainSearchAdapterTypeFactory extends BaseAdapterTypeFactory
        implements AdapterTypeFactory, ErrorNetworkViewHolder.OnRetryListener {

    private OnTrainSearchListener listener;

    public TrainSearchAdapterTypeFactory(OnTrainSearchListener listener) {
        this.listener = listener;
    }

    public int type(EmptyResultViewModel viewModel) {
        return EmptyResultViewHolder.LAYOUT;
    }

    public int type(ErrorNetworkModel viewModel) {
        return TrainErrorNetworkViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == TrainSearchViewHolder.LAYOUT) {
            return new TrainSearchViewHolder(parent, listener);
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            return new EmptyResultViewHolder(parent);
        } else if (type == TrainErrorNetworkViewHolder.LAYOUT) {
            return new TrainErrorNetworkViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public void onRetryClicked() {

    }

    public int type(TrainScheduleViewModel viewModel) {
        return TrainSearchViewHolder.LAYOUT;
    }

    public interface OnTrainSearchListener {

    }
}
