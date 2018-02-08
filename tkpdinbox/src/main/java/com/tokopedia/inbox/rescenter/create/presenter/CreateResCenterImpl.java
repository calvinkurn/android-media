package com.tokopedia.inbox.rescenter.create.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.inbox.rescenter.create.fragment.ChooseTroubleFragment;
import com.tokopedia.inbox.rescenter.create.listener.CreateResCenterListener;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.CreateResolutionCenterFragment;

/**
 * Created on 6/16/16.
 */
public class CreateResCenterImpl implements CreateResCenterPresenter {

    private final CreateResCenterListener listener;

    public CreateResCenterImpl(CreateResCenterListener listener) {
        this.listener = listener;
    }

    @Override
    public void initFragment(@NonNull Context context, Uri uriData, Bundle bundleData) {
        listener.inflateFragment(CreateResolutionCenterFragment.newInstance(generatePassData(bundleData, uriData)),
                CreateResolutionCenterFragment.class.getSimpleName());
    }

    @Override
    public void initRecomplaintFragment(@NonNull Context context, String orderId, String resolutionId) {
        listener.inflateFragment(CreateResolutionCenterFragment.newRecomplaintInstance(orderId, resolutionId),
                CreateResolutionCenterFragment.class.getSimpleName());
    }

    private ActionParameterPassData generatePassData(Bundle bundleData, Uri uriData) {
        ActionParameterPassData passData = new ActionParameterPassData();
        if (bundleData != null) {
            passData.setOrderID(bundleData.getString(InboxRouter.EXTRA_ORDER_ID));
            passData.setFlagReceived(bundleData.getInt(InboxRouter.EXTRA_STATE_FLAG_RECEIVED));
            if (bundleData.getInt(InboxRouter.EXTRA_STATE_FLAG_RECEIVED) == 0) {
                passData.setTroubleID(bundleData.getInt(InboxRouter.EXTRA_TROUBLE_ID));
                passData.setSolutionID(bundleData.getInt(InboxRouter.EXTRA_SOLUTION_ID));
            }
        }
        return passData;
    }
}
