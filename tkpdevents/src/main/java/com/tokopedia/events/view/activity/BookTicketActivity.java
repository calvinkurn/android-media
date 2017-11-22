package com.tokopedia.events.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tokopedia.events.R;

import com.tokopedia.events.view.fragment.FragmentAddTickets;

public class BookTicketActivity extends AppCompatActivity implements FragmentAddTickets.OnFragmentInteractionListener {

    ViewPager bookTicketViewPager;
    //RecyclerView datesRecycler;
    //DatesRecylerAdaper adapter;
    TabLayout tabLayout;
    ImageView address;
    TextView tvAddress;
    TextView buttonText;
    View button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.book_ticket_layout);
        bookTicketViewPager = findViewById(R.id.viewpager_book_ticket);
        AddTicketFragmentAdapter fragmentAdapter = new AddTicketFragmentAdapter(getSupportFragmentManager());
        bookTicketViewPager.setAdapter(fragmentAdapter);
        bookTicketViewPager.addOnPageChangeListener(new PageChangeListener());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button = findViewById(R.id.pay_tickets);
        buttonText = (button.findViewById(R.id.button_textview));


        CollapsingToolbarLayout cTL = findViewById(R.id.collasing_toolbar);
        cTL.setTitle("The 90s Festival");
        //cTL.setExpandedTitleMarginStart(40);
        cTL.setExpandedTitleColor(Color.parseColor("#00ffffff"));
        cTL.setCollapsedTitleTextColor(Color.parseColor("#ff000000"));
        //datesRecycler = findViewById(R.id.dates_recycler);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(bookTicketViewPager);
        tabLayout.setSelectedTabIndicatorHeight(0);
        address = findViewById(R.id.event_address).findViewById(R.id.image_holder_small);
        tvAddress = findViewById(R.id.event_address).findViewById(R.id.textview_holder_small);
        address.setImageResource(R.drawable.ic_skyline);
        address.setColorFilter(getResources().getColor(R.color.white));
        tvAddress.setText("Jalan Benyamin Suaeb No. 1");
        tvAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        tvAddress.setTextColor(getResources().getColor(R.color.white));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                View view = tab.getCustomView();
//                View view2 = view.findViewById(R.id.calender_view);
//                view.setBackgroundResource(R.drawable.ic_calendar_vector_green);
                View tV = tab.getCustomView();
                //tV.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.calendar_green,0,0);
                tV.setBackgroundResource(R.drawable.rounded_rectangle_green_solid);
                bookTicketViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                View view = tab.getCustomView();
//                View view2 = view.findViewById(R.id.calender_view);
//                view.setBackgroundResource(R.drawable.ic_calendar_vector);
                View tV = tab.getCustomView();
                tV.setBackgroundResource(R.drawable.rounded_rectangle_grey_solid);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //datesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //adapter = new DatesRecylerAdaper();
        //adapter.setSelected(0);
        //datesRecycler.setAdapter(adapter);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.calendar_item);
        }

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onFragmentInteraction(int price,int units) {
        if(units>0){
            buttonText.setText("Pay "+"Rp "+price*units);
            button.setVisibility(View.VISIBLE);
        } else
            button.setVisibility(View.GONE);

    }

    public class AddTicketFragmentAdapter extends FragmentStatePagerAdapter {

        public AddTicketFragmentAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentAddTickets.newInstance(10, 4);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


    public class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //adapter.setSelected(position);
            //tabLayout.getTabAt(position).select();

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void onResume() {
//        tabLayout.getTabAt(0).getCustomView().setSelected(true);
//        tabLayout.getTabAt(0).select();

        bookTicketViewPager.setCurrentItem(1);
        bookTicketViewPager.setCurrentItem(0);
        super.onResume();
    }
}
