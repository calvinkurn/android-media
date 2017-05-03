package com.tokopedia.seller.myproduct.view;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.seller.myproduct.ProductSocMedActivity;
import com.tokopedia.seller.myproduct.fragment.AddProductFragment;
import com.tokopedia.seller.myproduct.utils.AddProductType;
import com.tokopedia.seller.myproduct.utils.DelegateOnClick;

/**
 * Created by noiz354 on 5/13/16.
 */
public class AddProductSocMedSubmit {
    DelegateOnClick delegateOnClick;

    TextView add;
    TextView delete;

    public AddProductSocMedSubmit(View view){
        add = (TextView) view.findViewById(R.id.add_product_soc_med_submit);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        delete = (TextView) view.findViewById(R.id.add_product_soc_med_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    public DelegateOnClick getDelegateOnClick() {
        return delegateOnClick;
    }

    public void setDelegateOnClick(DelegateOnClick delegateOnClick) {
        this.delegateOnClick = delegateOnClick;
    }

    public void submit(){
        if(delegateOnClick != null && delegateOnClick instanceof AddProductFragment){
            if(((AddProductFragment)delegateOnClick).addProductType == AddProductType.EDIT){
                ((AddProductFragment) delegateOnClick).editProduct(true);
            }else {
                ((AddProductFragment) delegateOnClick).pushProduct();
            }
        }
    }

    public void delete(){
        if(delegateOnClick != null && delegateOnClick instanceof AddProductFragment){
            AddProductFragment delegateOnClick = (AddProductFragment) this.delegateOnClick;
            if(delegateOnClick.getActivity() instanceof ProductSocMedActivity) {
                delegateOnClick.removeFragment(
                        ((ProductSocMedActivity) delegateOnClick.getActivity()).getCurrentFragmentPosition()
                );
            }
            delegateOnClick.deleteProductDialog();

        }
    }

    public void turnOffButton(){
        add.setOnClickListener(null);
        delete.setOnClickListener(null);
        add.setBackgroundResource(R.color.tkpd_dark_gray);
        delete.setBackgroundResource(R.color.tkpd_dark_gray);
    }


}
