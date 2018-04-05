package com.tokopedia.topads.keyword.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.topads.keyword.constant.KeywordTypeDef.KEYWORD_TYPE_BROAD;
import static com.tokopedia.topads.keyword.constant.KeywordTypeDef.KEYWORD_TYPE_EXACT;
import static com.tokopedia.topads.keyword.constant.KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_BROAD;
import static com.tokopedia.topads.keyword.constant.KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_EXACT;
import static com.tokopedia.topads.keyword.constant.KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_PHRASE;
import static com.tokopedia.topads.keyword.constant.KeywordTypeDef.KEYWORD_TYPE_PHRASE;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({KEYWORD_TYPE_BROAD, KEYWORD_TYPE_PHRASE, KEYWORD_TYPE_EXACT,
        KEYWORD_TYPE_NEGATIVE_BROAD, KEYWORD_TYPE_NEGATIVE_PHRASE, KEYWORD_TYPE_NEGATIVE_EXACT})
public @interface KeywordTypeDef {
    int KEYWORD_TYPE_BROAD           = 1;
    int KEYWORD_TYPE_PHRASE          = 11;
    int KEYWORD_TYPE_EXACT           = 21;
    int KEYWORD_TYPE_NEGATIVE_BROAD  = 2;
    int KEYWORD_TYPE_NEGATIVE_PHRASE = 12;
    int KEYWORD_TYPE_NEGATIVE_EXACT  = 22;

    String KEYWORD = "keyword";
    String START_DATE = "start_date";
    String END_DATE = "end_date";
    String PAGE = "page";
    String IS_POSITIVE = "is_positive";
    String GROUP_ID = "group";
    String KEYWORD_STATUS = "status";
}