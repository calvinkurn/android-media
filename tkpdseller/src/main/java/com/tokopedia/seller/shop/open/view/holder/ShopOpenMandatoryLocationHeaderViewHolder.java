package com.tokopedia.seller.shop.open.view.holder;

import android.view.View;

import com.tokopedia.core.manage.people.address.interactor.ChooseAddressRetrofitInteractor;
import com.tokopedia.seller.R;

/**
 * Created by normansyahputa on 12/20/17.
 */

public class ShopOpenMandatoryLocationHeaderViewHolder {
    ChooseAddressRetrofitInteractor networkInteractor;
    private com.tokopedia.seller.shop.open.view.holder.ShopOpenMandatoryLocationHeaderViewHolder.ViewHolderListener viewHolderListener;

    public ShopOpenMandatoryLocationHeaderViewHolder(View root, final ViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
        root.findViewById(R.id.btn_choose_from_address)
        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewHolderListener != null){
                    viewHolderListener.navigateToChooseAddressActivityRequest();
                }
            }
        });
    }

    interface ViewHolderListener{
        void navigateToChooseAddressActivityRequest();
    }
}
