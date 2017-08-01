package com.tokopedia.seller.utils;

import com.tokopedia.seller.goldmerchant.statistic.utils.KMNumbers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by normansyahputa on 8/1/17.
 */

public class KMNumberTest {

    @Before
    public void overrideSuffix(){
        KMNumbers.overrideSuffixes(1000L, "rb");
        KMNumbers.overrideSuffixes(1000_000L, "jt");
    }

    @Test
    public void test12000(){
        String s = KMNumbers.formatNumbers(12_500L);
        Assert.assertTrue(s.equals("12,5rb"));
    }

    @Test
    public void test120_000(){
        String s = KMNumbers.formatNumbers(120_000L);
        Assert.assertTrue(s.equals("120rb"));
    }

    @Test
    public void test1_200_000(){
        String s = KMNumbers.formatNumbers(1_200_000L);
        Assert.assertTrue(s.equals("1,2jt"));
    }
}
