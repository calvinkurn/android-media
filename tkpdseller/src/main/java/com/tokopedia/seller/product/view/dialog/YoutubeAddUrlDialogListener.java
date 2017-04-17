package com.tokopedia.seller.product.view.dialog;

import android.content.Context;

/**
 * @author normansyahputa on 4/13/17.
 *
 * dont remove "extends AddEtalaseDialogListener"
 * please look at {@link AddEtalaseDialog#onAttach(Context)}
 * that code require {@link AddEtalaseDialogListener} to be implemented
 *
 */
public interface YoutubeAddUrlDialogListener extends AddEtalaseDialogListener {
    void addYoutubeUrl(String youtubeUrl);
}
