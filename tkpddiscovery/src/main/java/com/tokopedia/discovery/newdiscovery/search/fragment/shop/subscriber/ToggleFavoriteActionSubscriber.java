package com.tokopedia.discovery.newdiscovery.search.fragment.shop.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.domain.model.ActionResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.listener.FavoriteActionListener;

import rx.Subscriber;

/**
 * Created by henrypriyono on 10/23/17.
 */

public class ToggleFavoriteActionSubscriber extends Subscriber<Boolean> {
    private final FavoriteActionListener viewListener;
    private int adapterPosition;
    private boolean targetFavoritedStatus;

    public ToggleFavoriteActionSubscriber(FavoriteActionListener viewListener, int adapterPosition,
                                          boolean targetFavoritedStatus) {
        this.viewListener = viewListener;
        this.adapterPosition = adapterPosition;
        this.targetFavoritedStatus = targetFavoritedStatus;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorToggleFavorite(
                ErrorHandler.getErrorMessage(e), adapterPosition);
    }

    @Override
    public void onNext(Boolean isSuccess) {
        if (isSuccess)
            viewListener.onSuccessToggleFavorite(adapterPosition, targetFavoritedStatus);
        else
            viewListener.onErrorToggleFavorite(
                    viewListener.getString(R.string.default_request_error_unknown),
                    adapterPosition);
    }
}
