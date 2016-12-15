package com.tokopedia.inbox.rescenter.shipping.presenter;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by hangnadi on 12/13/16.
 */
public interface InputShippingFragmentPresenter {

    void onSaveState(Bundle state);

    void onRestoreState(Bundle savedState);

    void onFirstTimeLaunched();

    void onScanBarcodeClick();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onDestroy();

    void onConfirrmButtonClick();
}
