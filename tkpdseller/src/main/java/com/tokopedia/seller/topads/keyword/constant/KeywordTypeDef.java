package com.tokopedia.seller.topads.keyword.constant;

import android.support.annotation.IntDef;

import static com.tokopedia.seller.topads.keyword.constant.KeywordTypeDef.KEYWORD_TYPE_BROAD;
import static com.tokopedia.seller.topads.keyword.constant.KeywordTypeDef.KEYWORD_TYPE_EXACT;
import static com.tokopedia.seller.topads.keyword.constant.KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_BROAD;
import static com.tokopedia.seller.topads.keyword.constant.KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_EXACT;
import static com.tokopedia.seller.topads.keyword.constant.KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_PHRASE;
import static com.tokopedia.seller.topads.keyword.constant.KeywordTypeDef.KEYWORD_TYPE_PHRASE;

/**
 * @author normansyahputa on 4/25/17.
 */
@IntDef({KEYWORD_TYPE_BROAD, KEYWORD_TYPE_PHRASE, KEYWORD_TYPE_EXACT,
        KEYWORD_TYPE_NEGATIVE_BROAD, KEYWORD_TYPE_NEGATIVE_PHRASE, KEYWORD_TYPE_NEGATIVE_EXACT})
public @interface KeywordTypeDef {
    int KEYWORD_TYPE_BROAD           = 1;
    int KEYWORD_TYPE_PHRASE          = 11;
    int KEYWORD_TYPE_EXACT           = 21;
    int KEYWORD_TYPE_NEGATIVE_BROAD  = 2;
    int KEYWORD_TYPE_NEGATIVE_PHRASE = 12;
    int KEYWORD_TYPE_NEGATIVE_EXACT  = 22;
}