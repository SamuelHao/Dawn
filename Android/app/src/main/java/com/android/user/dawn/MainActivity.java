package com.android.scowluga.dawn;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    // For general UI
    private TabAdapter tabAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for UI
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabAdapter = new TabAdapter(getSupportFragmentManager());

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(getLightDrawable(R.drawable.ic_alarm, false));
        tabLayout.getTabAt(1).setIcon(getLightDrawable(R.drawable.ic_manual, true));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                updateDrawables(viewPager, tabLayout);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                updateDrawables(viewPager, tabLayout);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                updateDrawables(viewPager, tabLayout);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_github) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://git.uwaterloo.ca/se101-f18/se101-f18-group-dalu-h9hao"));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public class TabAdapter extends FragmentPagerAdapter {

        String[] fragments = {"Alarm", "Manual"};
        Fragment alarm, manual;

        public TabAdapter(FragmentManager fm) { super(fm); };

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (alarm == null)
                        return AlarmFragment.newInstance();
                    return alarm;
                case 1:
                    if (manual == null)
                        return ManualFragment.newInstance();
                    return manual;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        public CharSequence getPageTitle(int position) { return fragments[position]; };
    }

    private void updateDrawables(ViewPager viewPager, TabLayout tabLayout) {
        // In a tabLayout, the deselected tab's text and icon should be a different shade.
        // The text is automatically done, however the icon isn't. Here is the code for that aspect.
        boolean isFirst = viewPager.getCurrentItem() == 0;
        tabLayout.getTabAt(0).setIcon(getLightDrawable(R.drawable.ic_alarm, !isFirst));
        tabLayout.getTabAt(1).setIcon(getLightDrawable(R.drawable.ic_manual, isFirst));
    }

    private Drawable getLightDrawable(int id, boolean tinted) {
        int color = tinted ? R.color.lightColorAccent : R.color.white;
        Drawable temp = DrawableCompat.wrap(getResources().getDrawable(id));
        DrawableCompat.setTint(temp, getResources().getColor(color));
        return temp;
    }
}
