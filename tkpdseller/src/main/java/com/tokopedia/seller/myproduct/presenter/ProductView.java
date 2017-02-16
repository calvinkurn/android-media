package com.tokopedia.seller.myproduct.presenter;

import com.tokopedia.seller.myproduct.model.SimpleTextModel;
import com.tokopedia.seller.myproduct.ProductActivity;

import java.util.List;

/**
 * Created by noiz354 on 5/13/16.
 * presenter of {@link ProductActivity}
 */
public interface ProductView {
    String TWITTER_DIALOG_V_4 = "TwitterDialogV4";
//    String TAG = "MNORMANSYAH";
    String PLEASE_DISABLE_DON_T_KEEP_ACTIVITIES = "please disable don't keep activities";

    void showPopup(int type, String title, List<SimpleTextModel> simpleTextModels);
    void WarningDialog();
    void showTwitterDialog();
}
