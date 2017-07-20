package com.example.windows10gamer.demo3.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.windows10gamer.demo3.R;
import com.example.windows10gamer.demo3.model.ContactVO;
import com.example.windows10gamer.demo3.model.GetBrandProduct;

import org.json.JSONArray;

import java.util.concurrent.ExecutionException;

public class PrepaidActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    int minteger = 1;
    JSONArray brandTopup = null,brandCard = null;

    public String contactname="",contactnumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepaid);

        contactname = getIntent().getStringExtra("contactname");
        contactnumber = getIntent().getStringExtra("contactnumber");

        GetBrandTopup();
        GetBrandCard();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(),getApplicationContext()));

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

        ActionToolbar();


    }

    private void GetBrandCard() {
        GetBrandProduct myTask3 = new GetBrandProduct();
        myTask3.execute("MobileCard");
        try {
            brandCard = myTask3.get();
            Log.d("brandCard ", String.valueOf(brandCard));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void GetBrandTopup() {
        GetBrandProduct myTask2 = new GetBrandProduct();
        myTask2.execute("Topup");
        try {
            brandTopup = myTask2.get();
            Log.d("brand TOpup ", String.valueOf(brandTopup));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public void increaseInteger(View view) {
        minteger = minteger + 1;
        display(minteger);

    }public void decreaseInteger(View view) {
        if (minteger>1){
            minteger = minteger - 1;
        }
        display(minteger);
    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(R.id.integer_number);
        displayInteger.setText("" + number);
    }

    private void ActionToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class CustomAdapter extends FragmentPagerAdapter {
        private String fragments [] = {"TRẢ TRƯỚC","MUA MÃ THẺ","TRẢ SAU"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 1:
                    return new Fragment1();
                case 0:
                    return new Fragment2();
                case 2:
                    return new Fragment3();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }
}
