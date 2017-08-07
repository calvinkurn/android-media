package com.tokopedia.seller.goldmerchant.statistic.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.util.Pair;
import com.tokopedia.seller.common.williamchart.view.LineChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 7/11/17.
 */

public final class GMStatisticUtil {
    private static final String TAG = "GMStatisticUtil";

    public static List<Integer> subList(List<Integer> datas, int size) {
        if (datas != null && !datas.isEmpty()) {
            if (datas.size() <= size) {
                return datas;
            } else {
                return datas.subList(0, size);
            }
        }
        return null;
    }

    public static List<Integer> indexToDisplay(float[] values) {
        final List<Integer> indexToDisplay = new ArrayList<>();
        int divide = values.length / 10;
        for (int j = 1; j <= divide - 1; j++) {
            indexToDisplay.add((j * 10) - 1);
        }
        return indexToDisplay;
    }

    /**
     * limitation of william chart ( for big width it cannot draw, effectively for size of 15 )
     * https://github.com/diogobernardino/WilliamChart/issues/152
     * <p>
     * set only 8 values in  Window width rest are on sroll or dynamically change the width of linechart
     * is  window width/8 * total values returns you the total width of linechart with scrolling and set it in
     * layout Params of linechart .
     *
     * @param numChart
     */
    public static void resizeChart(int numChart, LineChartView chartView) {
        int maxNumberChart = 7;
        int scrolledWidth = 680;
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = chartView.getLayoutParams().height;
        double newSizeRatio = ((double) numChart) / maxNumberChart;
        if (newSizeRatio > 1 && width < scrolledWidth) {
            width = (int) CommonUtils.DptoPx(chartView.getContext(), scrolledWidth);
        }
        if (chartView.getParent() instanceof LinearLayout) {
            chartView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        } else if (chartView.getParent() instanceof FrameLayout) {
            chartView.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        }
    }

    private static List<Pair<Integer, String>> joinDateAndGraph(List<Integer> dateGraph, List<Integer> graph) {
        List<Pair<Integer, String>> pairs = new ArrayList<>();
        if (dateGraph == null || graph == null || dateGraph.isEmpty() || graph.isEmpty())
            return null;

        int lowerSize;
        if (dateGraph.size() > graph.size()) {
            lowerSize = graph.size();
        } else {
            lowerSize = dateGraph.size();
        }

        for (int i = 0; i < lowerSize; i++) {
            Integer date = dateGraph.get(i);
            Integer gross = graph.get(i);

            pairs.add(new Pair<>(gross, GoldMerchantDateUtils.getDate(date)));
        }

        return pairs;
    }

    private static Pair<String[], float[]> joinDateAndGraph2(List<Integer> dateGraph, List<Integer> graph, String[] monthNamesAbrev) {
        List<Pair<Integer, String>> pairs = joinDateAndGraph(dateGraph, graph);
        if (pairs == null)
            return null;

        String[] labels = new String[pairs.size()];
        float[] values = new float[pairs.size()];
        int i = 0;
        for (Pair<Integer, String> integerStringPair : pairs) {
            labels[i] = GoldMerchantDateUtils.getDateRaw(integerStringPair.getModel2(), monthNamesAbrev);
            values[i] = integerStringPair.getModel1();
            i++; // increment the index here
        }

        return new Pair<>(labels, values);
    }

    public static BaseWilliamChartModel joinDateAndGraph3(List<Integer> dateGraph, List<Integer> graph, String[] monthNamesAbrev) {
        Pair<String[], float[]> pair = joinDateAndGraph2(dateGraph, graph, monthNamesAbrev);
        return (pair != null) ? new BaseWilliamChartModel(pair.getModel1(), pair.getModel2()) : null;
    }

    public static List<Integer> sumTwoGraph(List<Integer> firstGraph, List<Integer> secondGraph) {
        if (firstGraph == null || secondGraph == null || firstGraph.size() != secondGraph.size()) {
            return null;
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < secondGraph.size(); i++) {
            Integer product = firstGraph.get(i);
            Integer shop = secondGraph.get(i);

            result.add(product + shop);
        }
        return result;
    }

    public static int findSelection(String[] values, String selection) {
        int searchIndex = -1;

        int count = 0;
        for (String value : values) {
            if (value.equals(selection)) {
                return searchIndex = count;
            }
            count++;
        }
        return searchIndex;
    }
}
