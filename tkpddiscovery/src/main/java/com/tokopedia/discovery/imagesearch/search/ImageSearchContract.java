package com.tokopedia.discovery.imagesearch.search;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by sachinbansal on 4/12/18.
 */

public class ImageSearchContract {


    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }
}