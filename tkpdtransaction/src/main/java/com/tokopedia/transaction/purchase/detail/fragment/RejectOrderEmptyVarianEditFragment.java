package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyVarianProductEditable;

import static com.tokopedia.transaction.purchase.detail.fragment.RejectOrderEmptyVarianFragment.FRAGMENT_EDIT_EMPTY_VARIAN_REQUEST_CODE;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class RejectOrderEmptyVarianEditFragment extends TkpdFragment {

    private static final String EDITABLE_EXTRA = "EDITABLE_EXTRA";

    @Override
    protected String getScreenName() {
        return null;
    }

    public static RejectOrderEmptyVarianEditFragment createFragment(
            EmptyVarianProductEditable editable
    ) {
        RejectOrderEmptyVarianEditFragment fragment = new RejectOrderEmptyVarianEditFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EDITABLE_EXTRA, editable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        EmptyVarianProductEditable editable = getArguments().getParcelable(EDITABLE_EXTRA);
        View view = inflater.inflate(R.layout.order_reject_empty_varian, container, false);
        ViewGroup mainView = view.findViewById(R.id.main_container);
        TextView productName = view.findViewById(R.id.order_detail_product_name);
        TextView productPrice = view.findViewById(R.id.order_detail_product_price);
        EditText description = view.findViewById(R.id.description);
        Button rejectOrderConfirmButton = view.findViewById(R.id.reject_order_confirm_button);
        ImageView productImage = view.findViewById(R.id.product_image);
        mainView.setOnClickListener(null);
        productName.setText(editable.getProductName());
        productPrice.setText(editable.getProductPrice());
        ImageHandler.LoadImage(productImage, editable.getProductImage());
        description.setText(editable.getProductDescription());
        rejectOrderConfirmButton.setOnClickListener(onConfirmButtonClickedListener(
                editable,
                description)
        );
        return view;
    }

    private View.OnClickListener onConfirmButtonClickedListener(final EmptyVarianProductEditable editable,
                                                                final EditText description) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editable.setProductDescription(description.getText().toString());
                getTargetFragment().onActivityResult(FRAGMENT_EDIT_EMPTY_VARIAN_REQUEST_CODE,
                        Activity.RESULT_OK, new Intent());
            }
        };
    }
}
