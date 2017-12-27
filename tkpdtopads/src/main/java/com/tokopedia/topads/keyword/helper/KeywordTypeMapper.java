package com.tokopedia.topads.keyword.helper;

import android.content.Context;

import com.tokopedia.topads.R;
import com.tokopedia.topads.keyword.constant.KeywordTypeDef;

/**
 * @author Hendry on 5/26/2017.
 */

public class KeywordTypeMapper {
    public static final int PHRASE_SPINNER_POS = 0;
    public static final int EXACT_SPINNER_POS = 1;

    public static String mapToKeywordName(Context context, @KeywordTypeDef int keywordType) {
        switch (keywordType) {
            case KeywordTypeDef.KEYWORD_TYPE_PHRASE:
                return context.getString(R.string.top_ads_keyword_phrase_match);
            case KeywordTypeDef.KEYWORD_TYPE_EXACT:
                return context.getString(R.string.top_ads_keyword_exact_match);
            case KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_PHRASE:
                return context.getString(R.string.top_ads_keyword_phrase_match);
            case KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_EXACT:
                return context.getString(R.string.top_ads_keyword_exact_match);
            default:
                return "";
        }
    }

    @KeywordTypeDef
    public static int mapToDef(boolean isPositive, int spinnerPosition) {
        switch (spinnerPosition) {
            case PHRASE_SPINNER_POS:
                return isPositive ?
                        KeywordTypeDef.KEYWORD_TYPE_PHRASE :
                        KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_PHRASE;
            case EXACT_SPINNER_POS:
                return isPositive ?
                        KeywordTypeDef.KEYWORD_TYPE_EXACT :
                        KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_EXACT;
        }
        return KeywordTypeDef.KEYWORD_TYPE_EXACT;
    }

    public static boolean isKeywordPositive(@KeywordTypeDef int keywordType) {
        switch (keywordType) {
            case KeywordTypeDef.KEYWORD_TYPE_BROAD:
            case KeywordTypeDef.KEYWORD_TYPE_PHRASE:
            case KeywordTypeDef.KEYWORD_TYPE_EXACT:
                return true;
            case KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_BROAD:
            case KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_PHRASE:
            case KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_EXACT:
                return false;
        }
        return true;
    }
}