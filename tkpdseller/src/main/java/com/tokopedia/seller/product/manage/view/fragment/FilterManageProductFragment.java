package com.tokopedia.seller.product.manage.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelView;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class FilterManageProductFragment extends TkpdBaseV4Fragment {

    private LabelView etalase;
    private LabelView category;
    private LabelView status;
    private LabelView condition;
    private LabelView catalog;
    private LabelView productPicture;
    private Button buttonSubmit;

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_activation_resent, container);
        setHasOptionsMenu(true);

        etalase = (LabelView) view.findViewById(R.id.etalase);
        category = (LabelView) view.findViewById(R.id.category);
        status = (LabelView) view.findViewById(R.id.status);
        condition = (LabelView) view.findViewById(R.id.condition);
        catalog = (LabelView) view.findViewById(R.id.catalog);
        productPicture = (LabelView) view.findViewById(R.id.product_picture);
        buttonSubmit = (Button) view.findViewById(R.id.button_submit);

        etalase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        productPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter_manage_product, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.menu_reset){

        }
        return super.onOptionsItemSelected(item);
    }
}
