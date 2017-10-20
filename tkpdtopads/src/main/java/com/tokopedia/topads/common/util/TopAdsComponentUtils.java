package com.tokopedia.topads.common.util;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.topads.TopAdsModuleRouter;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;

/**
 * Created by normansyahputa on 10/20/17.
 */

public class TopAdsComponentUtils {

    public static TopAdsComponent getTopAdsComponent(BaseDaggerFragment fragment){
        if(fragment.getActivity() != null){
            if(fragment.getActivity().getApplication() instanceof TopAdsModuleRouter){
                return ((TopAdsModuleRouter)fragment.getActivity().getApplication()).getTopAdsComponent();
            }else{
                return null;
            }
        }else {
            return null;
        }
    }
}
