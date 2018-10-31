package com.tokopedia.tkpdpdp.courier

import android.os.Bundle
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import java.util.ArrayList

class CourierFragment : BaseListFragment<CourierViewData, CourierTypeFactoryImpl>() {

    private lateinit var list: List<CourierViewData>

    companion object {

        private const val ARGS_LIST: String = "ARGS_LIST"

        fun newInstance(list: ArrayList<CourierViewData>): Fragment {
            val courierFragment = CourierFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARGS_LIST, list)
            courierFragment.arguments = bundle
            return courierFragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = arguments!!.getParcelableArrayList(ARGS_LIST)
    }

    override fun loadData(page: Int) {
        renderList(list, false)
    }

    override fun getAdapterTypeFactory(): CourierTypeFactoryImpl {
        return CourierTypeFactoryImpl()
    }

    override fun onItemClicked(courierViewData: CourierViewData) {

    }

    override fun initInjector() {

    }

    override fun getScreenName(): String? {
        return null
    }

}
