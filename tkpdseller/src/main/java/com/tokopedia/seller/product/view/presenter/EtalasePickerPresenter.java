package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.view.fragment.EtalasePickerView;

/**
 * @author sebastianuskh on 4/5/17.
 */

public abstract class EtalasePickerPresenter extends BaseDaggerPresenter<EtalasePickerView>{

    public abstract void fetchEtalaseData(String shopId);
}
