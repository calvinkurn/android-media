package com.tokopedia.seller.shop.open.view.holder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.seller.R;

/**
 * Created by normansyahputa on 12/20/17.
 */

public class LocationHeaderViewHolder {
    private ViewHolderListener viewHolderListener;

    public LocationHeaderViewHolder(View root, final ViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
        root.findViewById(R.id.btn_choose_from_address_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewHolderListener != null){
                    viewHolderListener.navigateToChooseAddressActivityRequest();
                }
            }
        });
    }

    public interface ViewHolderListener{
        void navigateToChooseAddressActivityRequest();
    }
}
