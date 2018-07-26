package com.tokopedia.seller.product.edit.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.activity.ProductAddDescriptionInfoActivity;

/**
 * Created by nathan on 3/6/18.
 */

public class ProductAddDescriptionPickerFragment extends TkpdBaseV4Fragment {

    public static final String PRODUCT_DESCRIPTION = "PRODUCT_DESCRIPTION";

    public static ProductAddDescriptionPickerFragment newInstance(String description) {
        Bundle args = new Bundle();
        ProductAddDescriptionPickerFragment fragment = new ProductAddDescriptionPickerFragment();
        args.putString(PRODUCT_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    private EditText descriptionEditText;
    private View tipsView;

    private String description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        description = bundle.getString(PRODUCT_DESCRIPTION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_description, container, false);
        TextView tvTitleTips = view.findViewById(R.id.tv_title_tips);
        tvTitleTips.setText(MethodChecker.fromHtml(getString(R.string.product_description_tips_title)));

        descriptionEditText = view.findViewById(R.id.edit_text_description);
        tipsView = view.findViewById(R.id.linear_layout_tips);
        tipsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProductAddDescriptionInfoActivity.class));
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!TextUtils.isEmpty(description)) {
            descriptionEditText.setText(MethodChecker.fromHtmlPreserveLineBreak(description));
        }
    }

    public String getDescriptionText() {
        return descriptionEditText.getText().toString();
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
