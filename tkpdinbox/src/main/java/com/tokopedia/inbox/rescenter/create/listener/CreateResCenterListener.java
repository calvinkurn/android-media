package com.tokopedia.inbox.rescenter.create.listener;

import android.app.Fragment;

import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;

/**
 * Created on 6/15/16.
 */
public interface CreateResCenterListener {

    void inflateFragment(Fragment fragment, String TAG);

    void addSolutionFragmentStacked(ActionParameterPassData passData);

    void addProductDetailTroubleFragmentStacked(ActionParameterPassData passData);

    void startCreateResCenterService(ActionParameterPassData passData);
}
