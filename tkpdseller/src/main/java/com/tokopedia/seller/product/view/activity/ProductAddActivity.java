package com.tokopedia.seller.product.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by nathan on 4/3/17.
 */

public class ProductAddActivity extends TActivity {

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_product_add);
//        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
//                .add(R.id.container, ProductAddFragment.createInstance(), ProductAddFragment.class.getSimpleName())
//                .commit();

        findViewById(R.id.button_ask_etalase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductAddActivity.this, CategoryPickerActivity.class);
                startActivityForResult(intent, 1000);
            }
        });

        result = (TextView) findViewById(R.id.etalase_result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                List<CategoryViewModel> category =
                        Parcels.unwrap(
                                data.getParcelableExtra(
                                        CategoryPickerActivity.CATEGORY_RESULT_LEVEL
                                )
                        );
                String test = "";
                for (CategoryViewModel viewModel : category){
                    test += viewModel.getName();
                }
                result.setText(test);
            }
        }
    }
}
