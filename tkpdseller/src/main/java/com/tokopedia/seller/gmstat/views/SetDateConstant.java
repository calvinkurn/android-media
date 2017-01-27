package com.tokopedia.seller.gmstat.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.tokopedia.seller.R;

import static com.tokopedia.seller.gmstat.views.BaseGMStatActivity.IS_GOLD_MERCHANT;
import static com.tokopedia.seller.gmstat.views.GMStatHeaderViewHelper.MOVE_TO_SET_DATE;

/**
 * Created by normansyahputa on 12/7/16.
 */

public interface SetDateConstant {

    String SELECTION_TYPE = "SELECTION_TYPE";
    String SELECTION_PERIOD = "SELECTION_PERIOD";
    String CUSTOM_START_DATE = "CUSTOM_START_DATE";
    String CUSTOM_END_DATE = "CUSTOM_END_DATE";
    int PERIOD_TYPE = 0;
    int CUSTOM_TYPE = 1;


}
