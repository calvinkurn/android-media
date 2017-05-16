package com.tokopedia.seller.myproduct.presenter;

import android.content.Context;

import com.tokopedia.seller.myproduct.model.EditPriceParam;

/**
 * Created by noiz354 on 5/18/16.
 * Modify by sebast on 5/31/16 for manage product API
 */
public interface NetworkInteractor  {
    String TAG = "STUART";
    String messageTAG = "DiscoveryInteractor";
    int PRD_STATE_PENDING  = -1;

    void fetchEtalase(Context context);
    // FROM MANAGE PRODUCT
    void getProductList(Context context,
                        String sort, String keyword, String page,
                        String etalase_id, String catalog_id, String department_id,
                        String picture_status, String condition);
    void changeCategories(Context context,
                          String CtgID, final String ID, String shopID);
    void changeInsurance(Context context, String insuranceID, String ID);
    void deleteProduct(Context context, String ID);
    void editPrice(Context context, EditPriceParam param);
    void editEtalase(Context context, String productId, String etalaseID, String etalaseName, int addTo);
}
