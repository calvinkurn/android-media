package com.tokopedia.seller.shop.setting.view.adapter;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictItemViewModel;

/**
 * Created by sebastianuskh on 3/22/17.
 */

class ShopSettingLocationDistrictViewHolder {
    private final TextView districtStringTextView;

    public ShopSettingLocationDistrictViewHolder(View convertView) {
        districtStringTextView = (TextView) convertView.findViewById(R.id.textivew_district_string);
    }

    public void bindData(
            RecommendationDistrictItemViewModel viewModel, String stringTyped
    ) {

        Spannable spannedWord =
                getSpannedWord(viewModel.getDistrictString(),
                        stringTyped);
        districtStringTextView.setText(spannedWord);
    }

    private Spannable getSpannedWord(String districtString, String stringTyped) {
        Spannable wordToSpan = new SpannableString(MethodChecker.fromHtml(districtString));

        String[] words = stringTyped.split("\\W+");
        for(String word : words){
            int start = districtString.toLowerCase().indexOf(word.toLowerCase());
            if (start != -1) {
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                wordToSpan.setSpan(boldSpan, start, start + word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return wordToSpan;
    }
}
