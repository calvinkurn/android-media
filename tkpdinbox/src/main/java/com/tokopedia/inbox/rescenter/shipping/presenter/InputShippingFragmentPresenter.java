package com.tokopedia.inbox.rescenter.shipping.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;

/**
 * Created by hangnadi on 12/13/16.
 */
public interface InputShippingFragmentPresenter {

    void onSaveState(Bundle state);

    void onRestoreState(Bundle savedState);

    void onFirstTimeLaunched();

    void onScanBarcodeClick(Context context);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onDestroy();

    void removeAttachment();

    void onConfirrmButtonClick();

    void onShippingRefChanged(Editable editable);

    void onShippingSpinnerChanged(int position);

    void onListAttachmentChanged(int itemCount);
}
