package com.tokopedia.seller.topads.data.source.local;

import com.tokopedia.seller.topads.data.source.TopAdsEtalaseDataSource;
import com.tokopedia.seller.topads.domain.model.data.Etalase;

import java.util.List;

import rx.Observable;
/**
 * Created by hendry on 2/20/17.
 */
public class TopAdsEtalaseCacheDataSource implements TopAdsEtalaseDataSource {
    public TopAdsEtalaseCacheDataSource() {

    }

    public Observable<List<Etalase>> getEtalaseList(String shopId) {
//        List<DataEtalaseDB> dataEtalaseDBList = new Select().from(DataEtalaseDB.class).queryList();
//
//        return Observable.from(dataEtalaseDBList)
//                .map(new Func1<DataEtalaseDB, Etalase>() {
//                    @Override
//                    public Etalase call(DataEtalaseDB dataEtalaseDB) {
//                        return new Etalase(dataEtalaseDB);
//                    }
//                })
//                .toList();
        return null;
    }

    public static void saveEtalaseListToCache(List<Etalase> etalaseList) {
//        final DatabaseWrapper database = FlowManager.getDatabase(TkpdSellerDatabase.NAME).getWritableDatabase();
//        database.beginTransaction();
//        try {
//            DataEtalaseDB dataEtalaseDB = new DataEtalaseDB();
//            for (int i = 0, sizei = etalaseList.size(); i < sizei; i++) {
//                Etalase etalase = etalaseList.get(i);
//                dataEtalaseDB.etalaseId = etalase.getEtalaseId();
//                dataEtalaseDB.etalaseName = etalase.getEtalaseName();
//                dataEtalaseDB.etalaseNumProduct = etalase.getEtalaseNumProduct();
//                dataEtalaseDB.etalaseTotalProduct = etalase.getEtalaseTotalProduct();
//                dataEtalaseDB.save();
//            }
//            database.setTransactionSuccessful();
//        } finally {
//            database.endTransaction();
//        }
    }

    public static boolean isCacheExpired(){
//        LocalCacheHandler lch = new LocalCacheHandler(
//                MainApplication.getAppContext(), TkpdCache.NAMECACHE);
//        if ( lch.isExpired() ){
//            return true;
//        }
//        return false;
        return true;
    }

    public static void setExpired(int seconds){
//        LocalCacheHandler lch = new LocalCacheHandler(
//                MainApplication.getAppContext(), TkpdCache.NAMECACHE);
//        lch.setExpire(seconds);
    }

    public static void clearPreference(){
//        LocalCacheHandler.clearCache(
//                MainApplication.getAppContext(),
//                TkpdCache.NAMECACHE
//        );
    }



}
