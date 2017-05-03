package com.tokopedia.seller.myproduct.view;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.myproduct.fragment.AddProductFragment;
import com.tokopedia.seller.myproduct.utils.DelegateOnClick;



/**
 * Created by noiz354 on 5/13/16.
 */
public class AddProductSubmit {
    DelegateOnClick delegateOnClick;

    TextView submit;
    TextView submitAndAdd;

    public AddProductSubmit(View view){
        submit = (TextView) view.findViewById(R.id.add_product_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndPushProduct();
            }
        });
        submitAndAdd = (TextView) view.findViewById(R.id.add_product_submit_and_push);
        submitAndAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

    public void saveAndPushProduct(){
        if(delegateOnClick != null && delegateOnClick instanceof AddProductFragment){
            ((AddProductFragment)delegateOnClick).pushProduct();
        }
    }

    public void saveProduct(){
        if(delegateOnClick != null && delegateOnClick instanceof AddProductFragment){
            ((AddProductFragment)delegateOnClick).pushAndCreateNewProduct();
        }
    }

    public DelegateOnClick getDelegateOnClick() {
        return delegateOnClick;
    }

    public void setDelegateOnClick(DelegateOnClick delegateOnClick) {
        this.delegateOnClick = delegateOnClick;
    }
}
