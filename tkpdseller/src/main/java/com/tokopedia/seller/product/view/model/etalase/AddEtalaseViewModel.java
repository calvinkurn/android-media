package com.tokopedia.seller.product.view.model.etalase;


import com.tokopedia.seller.R;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class AddEtalaseViewModel implements EtalaseViewModel {

    public static final int LAYOUT = R.layout.etalase_add_new_layout;

    @Override
    public int getType() {
        return LAYOUT;
    }
}
