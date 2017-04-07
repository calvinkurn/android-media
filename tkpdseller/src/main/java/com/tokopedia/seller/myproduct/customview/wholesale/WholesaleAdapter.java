package com.tokopedia.seller.myproduct.customview.wholesale;

import java.util.List;

/**
 * Created by sebastianuskh on 12/5/16.
 */

public interface WholesaleAdapter {

    void onUpdateData(int type, int position, String value, boolean isShouldCheckError) throws NumberFormatException;

    void removeWholesaleItem(int position);

    boolean checkIfErrorExist();

    List<WholesaleModel> getDatas();

    void setData(List<WholesaleModel> datas);

    void removeAllWholesaleItem();
}
