package com.tokopedia.seller.gmstat.views;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.library.LoaderImageView;
import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.gmstat.models.GetTransactionGraph;
import com.tokopedia.seller.gmstat.presenters.GMFragmentPresenterImpl;
import com.tokopedia.seller.gmstat.presenters.GMFragmentView;
import com.tokopedia.seller.gmstat.presenters.GMStat;
import com.tokopedia.seller.gmstat.utils.GMNetworkErrorHelper;
import com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils;
import com.tokopedia.seller.gmstat.utils.GridDividerItemDecoration;
import com.tokopedia.seller.gmstat.utils.GrossGraphChartConfig;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.gmstat.views.adapter.GMStatWidgetAdapter;
import com.tokopedia.seller.gmstat.views.helper.BuyerDataLoading;
import com.tokopedia.seller.gmstat.views.helper.MarketInsightLoading;
import com.tokopedia.seller.gmstat.views.helper.PopularProductLoading;
import com.tokopedia.seller.gmstat.views.helper.TransactionDataLoading;
import com.tokopedia.seller.gmstat.views.models.BaseGMModel;
import com.tokopedia.seller.gmstat.views.models.ConvRate;
import com.tokopedia.seller.gmstat.views.models.GrossIncome;
import com.tokopedia.seller.gmstat.views.models.LoadingGMModel;
import com.tokopedia.seller.gmstat.views.models.LoadingGMTwoModel;
import com.tokopedia.seller.gmstat.views.models.ProdSeen;
import com.tokopedia.seller.gmstat.views.models.ProdSold;
import com.tokopedia.seller.gmstat.views.models.SuccessfulTransaction;
import com.tokopedia.seller.lib.williamchart.renderer.StringFormatRenderer;
import com.tokopedia.seller.lib.williamchart.renderer.XRenderer;
import com.tokopedia.seller.lib.williamchart.tooltip.Tooltip;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NavigableMap;
import java.util.TreeMap;

import static com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils.getDateRaw;
import static com.tokopedia.seller.gmstat.views.BaseGMStatActivity.IS_GOLD_MERCHANT;
import static com.tokopedia.seller.gmstat.views.BaseGMStatActivity.SHOP_ID;
import static com.tokopedia.seller.gmstat.views.DataTransactionViewHelper.dpToPx;
import static com.tokopedia.seller.gmstat.views.GMStatHeaderViewHelper.getDates;

/**
 * A placeholder fragment containing a simple view.
 * created by norman 02/01/2017
 */
public class GMStatActivityFragment extends BasePresenterFragment implements GMFragmentView {

    public static final double NoDataAvailable = -2147483600;
    public static final String TAG = "GMStatActivityFragment";
    private static final Locale locale = new Locale("in", "ID");
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    private String tryAgainText;
    private String unknownExceptionDescription;
    private String messageExceptionDescription;
    private String defaultExceptionDescription;
    private String[] monthNamesAbrev;
    private LineChartView grossIncomeGraph2;
    private RecyclerView gmStatRecyclerView;
    private GMStatWidgetAdapter gmStatWidgetAdapter;
    private LoaderImageView grossIncomeGraph2Loading;
    private View popularProduct;
    private View transactionData;
    private View marketInsight;
    private View marketInsightReal;
    private HorizontalScrollView grossIncomeGraphContainer;
    private Drawable oval2Copy6;
    private GridLayoutManager gridLayoutManager;
    private MarketInsightViewHelper marketInsightViewHelper;
    private PopularProductLoading popularProductLoading;
    private TransactionDataLoading transactionDataLoading;
    private BuyerDataLoading buyerDataLoading;
    private MarketInsightLoading marketInsightLoading;
    private PopularProductViewHelper popularProductViewHelper;
    private View rootView;
    private GMFragmentPresenterImpl gmFragmentPresenter;
    private GMStat gmstat;
    private DataTransactionViewHelper dataTransactionViewHelper;
    private BuyerDataViewHelper buyerDataViewHelper;
    private GMStatHeaderViewHelper gmstatHeaderViewHelper;
    private GrossGraphChartConfig grossGraphChartConfig;
    private GMNetworkErrorHelper gmNetworkErrorHelper;
    private long shopId;
    private boolean isGoldMerchant;

    public GMStatActivityFragment() {
    }

    public void onClickHeaderGMStat() {
        if (gmstatHeaderViewHelper != null) {
            gmstatHeaderViewHelper.onClick(this, false);
        }
    }

    void initViews(View rootView) {
        tryAgainText = getString(R.string.try_again);
        unknownExceptionDescription = getString(R.string.unknown_exception_description);
        messageExceptionDescription = getString(R.string.message_exception_description);
        defaultExceptionDescription = getString(R.string.default_exception_description);

        monthNamesAbrev = rootView.getResources().getStringArray(R.array.month_names_abrev);
        grossIncomeGraph2 = (LineChartView) rootView.findViewById(R.id.gross_income_graph2);
        gmStatRecyclerView = (RecyclerView) rootView.findViewById(R.id.gmstat_recyclerview);
        grossIncomeGraph2Loading = (LoaderImageView) rootView.findViewById(R.id.gross_income_graph2_loading);
        popularProduct = rootView.findViewById(R.id.popular_product);
        transactionData = rootView.findViewById(R.id.transaction_data);
        marketInsight = rootView.findViewById(R.id.buyer_data);
        marketInsightReal = rootView.findViewById(R.id.market_insight);
        grossIncomeGraphContainer = (HorizontalScrollView) rootView.findViewById(R.id.gross_income_graph_container);
        oval2Copy6 = ResourcesCompat.getDrawable(getResources(), R.drawable.oval_2_copy_6, null);

        rootView.findViewById(R.id.header_gmstat).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickHeaderGMStat();
                    }
                }
        );

        // analytic below : https://phab.tokopedia.com/T18496
        final ScrollView contentGMStat = (ScrollView) rootView.findViewById(R.id.content_gmstat);
        contentGMStat.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = contentGMStat.getChildAt(contentGMStat.getChildCount()-1);
                int diff = (view.getBottom()+contentGMStat.getPaddingBottom()
                        -(contentGMStat.getHeight()+contentGMStat.getScrollY()));

                // if diff is zero, then the bottom has been reached
                if (diff == 0) {
                    onScrollGMStatTracking();
                }
            }
        });
    }

    private List<NExcel> joinDateAndGrossGraph(List<Integer> dateGraph, List<Integer> grossGraph) {
        List<NExcel> nExcels = new ArrayList<>();
        if (dateGraph == null || grossGraph == null || dateGraph.isEmpty() || grossGraph.isEmpty())
            return null;

        int lowerSize;
        if (dateGraph.size() > grossGraph.size()) {
            lowerSize = grossGraph.size();
        } else {
            lowerSize = dateGraph.size();
        }

        for (int i = 0; i < lowerSize; i++) {
            Integer date = dateGraph.get(i);
            Integer gross = grossGraph.get(i);

            nExcels.add(new NExcel(gross, GoldMerchantDateUtils.getDate(date)));
        }

        return nExcels;
    }

    protected void initAdapter() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gmStatRecyclerView.setLayoutManager(gridLayoutManager);
        GridDividerItemDecoration gridDividerItemDecoration = new GridDividerItemDecoration(getActivity());
        gmStatRecyclerView.addItemDecoration(gridDividerItemDecoration);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (gmStatWidgetAdapter.getItemViewType(position)) {
                    case SuccessfulTransaction.TYPE:
                    case ProdSeen.TYPE:
                    case ProdSold.TYPE:
                    case ConvRate.TYPE:
                    case LoadingGMModel.TYPE:
                        return 1;
                    case LoadingGMTwoModel.TYPE:
                    default:
                        return 2;
                }
            }
        });
        gmStatRecyclerView.setAdapter(gmStatWidgetAdapter);
    }

    protected void initAdapter(final GMStatWidgetAdapter gmStatWidgetAdapter) {
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gmStatRecyclerView.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (gmStatWidgetAdapter.getItemViewType(position)) {
                    case SuccessfulTransaction.TYPE:
                    case ProdSeen.TYPE:
                    case ProdSold.TYPE:
                    case ConvRate.TYPE:
                    case LoadingGMModel.TYPE:
                        return 1;
                    case LoadingGMTwoModel.TYPE:
                    default:
                        return 2;
                }
            }
        });
        gmStatRecyclerView.setAdapter(gmStatWidgetAdapter);
    }

    @Override
    public void resetToLoading() {
        resetEmptyAdapter();
        gmstatHeaderViewHelper.resetToLoading();
        initChartLoading();
        initPopularLoading();
        initTransactionDataLoading();
        initMarketInsightLoading();
        initMarketInsightLoading2();
    }

    @Override
    public void bindHeader(long sDate, long eDate, int lastSelectionPeriod, int selectionType) {
        gmstatHeaderViewHelper.bindDate(sDate, eDate, lastSelectionPeriod, selectionType);
    }

    @Override
    public void onLoadGMStatTracking() {
        UnifyTracking.eventLoadGMStat();
    }

    @Override
    public void onScrollGMStatTracking() {
        UnifyTracking.eventScrollGMStat();
    }

    /**
     * reset 4 box to loading state
     */
    private void resetEmptyAdapter() {
        gmStatWidgetAdapter.clear();

        List<BaseGMModel> loadingBases = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            loadingBases.add(new LoadingGMModel());

        loadingBases.add(new LoadingGMTwoModel());
        gmStatWidgetAdapter.addAll(loadingBases);
        gmStatWidgetAdapter.notifyDataSetChanged();
    }

    protected void initEmptyAdapter() {
        List<BaseGMModel> loadingBases = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            loadingBases.add(new LoadingGMModel());

        loadingBases.add(new LoadingGMTwoModel());

        gmStatWidgetAdapter = new GMStatWidgetAdapter(loadingBases, gmstat);
        initAdapter();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity != null && activity instanceof GMStat) {
            this.gmstat = (GMStat) activity;

            // get shop id from activity.
            try {
                if(gmstat.getShopId() != null)
                    shopId = Long.parseLong(gmstat.getShopId());
            } catch (NumberFormatException nfe) {
                throw new RuntimeException(nfe.getMessage() + "\n [need valid shop id]");
            }

            isGoldMerchant = gmstat.isGoldMerchant();

            gmFragmentPresenter = new GMFragmentPresenterImpl(this, gmstat, shopId);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchSaveInstanceSate(savedInstanceState);
    }

    private void fetchSaveInstanceSate(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            shopId= savedInstanceState.getLong(SHOP_ID);
            isGoldMerchant = savedInstanceState.getBoolean(IS_GOLD_MERCHANT);
            gmFragmentPresenter = new GMFragmentPresenterImpl(this, gmstat, shopId);
            gmFragmentPresenter.restoreState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gmFragmentPresenter.setFirstTime(false);
        rootView = inflater.inflate(R.layout.fragment_gmstat, container, false);
        initViews(rootView);
        initNumberFormatter();
        initEmptyAdapter();
        initChartLoading();
        popularProductViewHelper = new PopularProductViewHelper(rootView);
        dataTransactionViewHelper = new DataTransactionViewHelper(rootView, gmstat.getImageHandler(), gmstat.isGoldMerchant());
        buyerDataViewHelper = new BuyerDataViewHelper(rootView);
        gmstatHeaderViewHelper = new GMStatHeaderViewHelper(rootView, gmstat.isGoldMerchant());
        marketInsightViewHelper = new MarketInsightViewHelper(rootView, gmstat.isGoldMerchant());
        popularProductLoading = new PopularProductLoading(rootView);
        transactionDataLoading = new TransactionDataLoading(rootView);
        buyerDataLoading = new BuyerDataLoading(rootView);
        marketInsightLoading = new MarketInsightLoading(rootView);
        initPopularLoading();
        initTransactionDataLoading();
        initMarketInsightLoading();
        initMarketInsightLoading2();
        gmFragmentPresenter.initInstance();
        grossGraphChartConfig = new GrossGraphChartConfig(
                gmFragmentPresenter.getmLabels(), gmFragmentPresenter.getmValues());
        return rootView;
    }

    private void initNumberFormatter() {
        KMNumbers.overrideSuffixes(1000L, "Rb");
        KMNumbers.overrideSuffixes(1000000L, "jt");
    }

    private void initMarketInsightLoading2() {
        marketInsightLoading.displayLoading();
        marketInsightReal.setVisibility(View.GONE);
    }

    private void initMarketInsightLoading() {
        buyerDataLoading.displayLoading();
        marketInsight.setVisibility(View.GONE);
    }

    private void initTransactionDataLoading() {
        transactionDataLoading.displayLoading();
        transactionData.setVisibility(View.GONE);
    }

    private void initPopularLoading() {
        popularProductLoading.displayLoading();
        popularProduct.setVisibility(View.GONE);
    }

    private void initChartLoading() {
        grossIncomeGraphContainer.setVisibility(View.GONE);
        grossIncomeGraph2Loading.setVisibility(View.VISIBLE);
        grossIncomeGraph2Loading.resetLoader();
    }

    private void displayChart() {
        grossIncomeGraphContainer.setVisibility(View.VISIBLE);
        grossIncomeGraph2Loading.setVisibility(View.GONE);
    }

    /**
     * limitation of william chart ( for big width it cannot draw, effectively for size of 15 )
     * https://github.com/diogobernardino/WilliamChart/issues/152
     *
     * @param numChart
     */
    private void resizeChart(int numChart) {
        Log.d(TAG, "resizeChart " + numChart);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) dpToPx(getActivity(), 360); //displaymetrics.widthPixels;
        /*
            set only 8 values in  Window width rest are on sroll or dynamically change the width of linechart
            is  window width/8 * total values returns you the total width of linechart with scrolling and set it in
            layout Params of linechart .
        */
        double newSizeRatio = ((double) numChart) / 7;
        if (newSizeRatio > 1) {
            grossIncomeGraph2.setLayoutParams(new LinearLayout.LayoutParams((int) dpToPx(getActivity(), 680), grossIncomeGraph2.getLayoutParams().height));//(int) (newSizeRatio * width / 2)
        } else {
            grossIncomeGraph2.setLayoutParams(new LinearLayout.LayoutParams(width, grossIncomeGraph2.getLayoutParams().height));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        gmNetworkErrorHelper = new GMNetworkErrorHelper(null, rootView);
        gmFragmentPresenter.onResume();
    }

    public void displayDefaultValue() {
        gmFragmentPresenter.displayDefaultValue(getActivity().getAssets());
    }

    @Override
    public void fetchData() {
        gmFragmentPresenter.fetchData();
    }

    @Override
    public void fetchData(long sDate, long eDate, int lastSelectionPeriod, int selectionType) {
        gmFragmentPresenter.fetchData(sDate, eDate, lastSelectionPeriod, selectionType);
    }

    @Override
    public void onPause() {
        super.onPause();
        gmNetworkErrorHelper.onPause();
        gmFragmentPresenter.onPause();
        if (grossIncomeGraph2 != null)
            grossIncomeGraph2.dismissAllTooltips();
    }

    @Override
    public void onSuccessGetShopCategory(GetShopCategory getShopCategory) {
        marketInsightReal.setVisibility(View.VISIBLE);
        marketInsightLoading.hideLoading();
        marketInsightViewHelper.bindData(getShopCategory);
    }

    @Override
    public void onSuccessTransactionGraph(GetTransactionGraph getTransactionGraph, long sDate, long eDate, int lastSelectionPeriod, int selectionType) {
        GrossIncome grossIncome = new GrossIncome(getTransactionGraph.getGrossRevenue());
        List<BaseGMModel> baseGMModels = new ArrayList<>();
        baseGMModels.add(grossIncome);

        List<Integer> dateGraph = getTransactionGraph.getDateGraph();
        //[START] use date from network
        List<String> dates = getDates(dateGraph, GMStatActivityFragment.this.monthNamesAbrev);
        if (dates != null) {
            grossIncome.textDescription = dates.get(0) + " - " + dates.get(1);
        }
        //[END] use date from network
        //[START] override sDate and eDate with local selection.
        if(sDate != -1 || eDate != -1) {
            String sDateWithYear = GoldMerchantDateUtils.getDateWithYear(
                    GoldMerchantDateUtils.getDateFormatForInput(sDate), monthNamesAbrev);
            String eDateWithYear = GoldMerchantDateUtils.getDateWithYear(
                    GoldMerchantDateUtils.getDateFormatForInput(eDate), monthNamesAbrev);
            grossIncome.textDescription = sDateWithYear + " - " + eDateWithYear;

            dateGraph = GoldMerchantDateUtils.generateDateRanges(sDate,eDate);
        }
        //[END] override sDate and eDate with local selection.
        List<Integer> grossGraph = getTransactionGraph.getGrossGraph();
        List<NExcel> nExcels = joinDateAndGrossGraph(dateGraph, grossGraph);

        if (nExcels != null) {
            //[]START] try used willam chart
            displayChart();
            resizeChart(nExcels.size());
            int i = 0;
            String[] mLabels = new String[nExcels.size()];
            final float[] mValues = new float[nExcels.size()];
            for (NExcel nExcel : nExcels) {
                mLabels[i] = getDateRaw(nExcel.getXmsg(), monthNamesAbrev);
                mValues[i] = nExcel.getUpper();
                i++;
            }

            final List<Integer> indexToDisplay = new ArrayList<>();
            int divide = mValues.length / 10;
            for (int j = 1; j <= divide - 1; j++) {
                indexToDisplay.add((j * 10) - 1);
            }

            @LayoutRes int layoutTooltip = R.layout.gm_stat_tooltip_lollipop;
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion < android.os.Build.VERSION_CODES.LOLLIPOP) {
                layoutTooltip = R.layout.gm_stat_tooltip;
            }

            grossGraphChartConfig
                    .setmLabels(mLabels)
                    .setmValues(mValues, new XRenderer.XRendererListener() {
                        @Override
                        public boolean filterX(@IntRange(from = 0L) int i) {
                            if (i == 0 || mValues.length - 1 == i)
                                return true;

                            if (mValues.length <= 15) {
                                return true;
                            }

                            return indexToDisplay.contains(i);

                        }
                    })
                    .setDotDrawable(oval2Copy6)
                    .setTooltip(new Tooltip(getActivity(),
                            layoutTooltip,
                            R.id.gm_stat_tooltip_textview,
                            new StringFormatRenderer() {
                                @Override
                                public String formatString(String s) {
                                    return KMNumbers.formatNumbers(Float.valueOf(s));
                                }
                            }))
                    .buildChart(grossGraphChartConfig.buildLineChart(grossIncomeGraph2));
            //[END] try used willam chart
        }


        gmStatWidgetAdapter.addAll(baseGMModels);
        gmStatWidgetAdapter.notifyDataSetChanged();

        dataTransactionViewHelper.bindData(getTransactionGraph);
        transactionDataLoading.hideLoading();
        transactionData.setVisibility(View.VISIBLE);

        if (sDate == -1 && eDate == -1) {
            gmstatHeaderViewHelper.bindData(dateGraph, lastSelectionPeriod);
            gmFragmentPresenter.setsDate(gmstatHeaderViewHelper.getsDate());
            gmFragmentPresenter.seteDate(gmstatHeaderViewHelper.geteDate());
        } else {
            gmstatHeaderViewHelper.bindDate(sDate, eDate, lastSelectionPeriod, selectionType);
            gmstatHeaderViewHelper.stopLoading();
        }
    }

    @Override
    public void onSuccessProductnGraph(GetProductGraph getProductGraph, boolean isFirstTime) {
        List<BaseGMModel> baseGMModels = new ArrayList<>();
        SuccessfulTransaction successfulTransaction
                = new SuccessfulTransaction(getProductGraph.getSuccessTrans());
        successfulTransaction.percentage = getProductGraph.getDiffTrans() * 100;

        ProdSeen prodSeen = new ProdSeen(getProductGraph.getProductView());
        prodSeen.percentage = getProductGraph.getDiffView() * 100;

        ProdSold prodSold = new ProdSold(getProductGraph.getProductSold());
        prodSold.percentage = getProductGraph.getDiffSold() * 100;

        ConvRate convRate = new ConvRate(getProductGraph.getConversionRate() * 100);
        convRate.percentage = getProductGraph.getDiffConv() * 100;

        baseGMModels.add(successfulTransaction);
        baseGMModels.add(prodSeen);
        baseGMModels.add(prodSold);
        baseGMModels.add(convRate);
        gmStatWidgetAdapter.clear();
        gmStatWidgetAdapter.addAll(baseGMModels);

        if (!isFirstTime) {
            initAdapter(gmStatWidgetAdapter);
            gmFragmentPresenter.setFirstTime(true);
        }
    }

    @Override
    public void onSuccessPopularProduct(GetPopularProduct getPopularProduct) {
        popularProductViewHelper.bindData(getPopularProduct, gmstat.getImageHandler());
        popularProductLoading.hideLoading();
        popularProduct.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessBuyerData(GetBuyerData getBuyerData) {
        buyerDataViewHelper.bindData(getBuyerData);
        marketInsight.setVisibility(View.VISIBLE);
        buyerDataLoading.hideLoading();
    }

    @Override
    public void onSuccessGetKeyword(List<GetKeyword> getKeywords) {
        marketInsightViewHelper.bindData(getKeywords);
        marketInsightReal.setVisibility(View.VISIBLE);
        marketInsightLoading.hideLoading();
    }

    @Override
    public void onSuccessGetCategory(List<HadesV1Model> hadesV1Models) {
        marketInsightViewHelper.bindDataCategory(hadesV1Models);
    }

    @Override
    public void onComplete() {
        // tracking below : https://phab.tokopedia.com/T18496
        onLoadGMStatTracking();
    }

    @Override
    public void onError(Throwable e) {
        displayDefaultValue();

        final StringBuilder textMessage = new StringBuilder("");
        if (e instanceof UnknownHostException) {
            textMessage.append(unknownExceptionDescription);
        } else if (e instanceof MessageErrorException) {
            textMessage.append(messageExceptionDescription);
        } else {
            textMessage.append(defaultExceptionDescription);
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null && rootView != null) {
                    gmNetworkErrorHelper.showSnackbar(textMessage.toString(), tryAgainText
                            , new OnActionClickListener() {
                                @Override
                                public void onClick(@SuppressWarnings("UnusedParameters") View view) {
                                    gmFragmentPresenter.setFetchData(true);
                                    gmFragmentPresenter.fetchData();
                                }
                            });
                }
            }
        }, 100);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SHOP_ID, shopId);
        outState.putBoolean(IS_GOLD_MERCHANT, isGoldMerchant);
        gmFragmentPresenter.saveState(outState);
    }

    @Override
    public void onFailure() {

    }

    //[START] unused methods
    @Override
    protected void setViewListener() {
    }

    @Override
    protected void initialVar() {
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
    }

    @Override
    public void onSaveState(Bundle state) {
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected void initialListener(Activity activity) {
    }

    @Override
    protected void setupArguments(Bundle arguments) {
    }

    @Override
    protected int getFragmentLayout() {
        return 0;
    }

    @Override
    protected void initView(View view) {
    }
    //[END] unused methods


}
