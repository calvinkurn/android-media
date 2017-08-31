package com.tokopedia.seller.product.etalase.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.etalase.view.listener.EtalasePickerView;

/**
 * @author sebastianuskh on 4/5/17.
 */

public abstract class EtalasePickerPresenter extends BaseDaggerPresenter<EtalasePickerView>{

    public abstract void fetchFirstPageEtalaseData();

    public abstract void addNewEtalase(String newEtalaseName);

    public abstract void fetchNextPageEtalaseData(int page);
}
