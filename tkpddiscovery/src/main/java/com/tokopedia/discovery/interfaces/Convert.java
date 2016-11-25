package com.tokopedia.discovery.interfaces;

import java.util.List;

/**
 * Created by noiz354 on 6/28/16.
 */
public interface Convert<E,F> {
    F from(E data);
    List<F> fromList(E... datas);
    F[] toArrays(E... datas);

    abstract class DefaultConvert<E,F> implements Convert<E,F>{
        public static String TAG = "DefaultConvert";
        public static String MESSAGETAG = TAG +" -> ";

        @Override
        public F from(E data) {
            return null;
        }

        @Override
        public List<F> fromList(E... datas) {
            return null;
        }

        @Override
        public F[] toArrays(E... datas) {
            return null;
        }
    }
}
