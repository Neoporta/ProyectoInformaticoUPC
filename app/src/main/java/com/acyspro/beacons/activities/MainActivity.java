package com.acyspro.beacons.activities;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;

import android.view.MenuItem;

import com.acyspro.beacons.R;
import com.acyspro.beacons.activities.fragments.AdFragment;
import com.acyspro.beacons.activities.fragments.FavoriteAdFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        // Setear adaptador al viewpager.
        mViewPager = findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2);
        setupViewPager(mViewPager);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Marcar item presionado
                        menuItem.setChecked(true);
                        // Crear nuevo fragmento
                        String title = menuItem.getTitle().toString();
                        selectItem(title);
                        return true;
                    }
                }
        );
    }

    private void selectItem(String title) {
        // Enviar título como arguemento del fragmento
        Bundle args = new Bundle();

        if (title.equals("Fotos")) {
            //Intent intent2 = new Intent(getApplicationContext(), PhotosActivity.class);
            //intent2.putExtra("nroEmpleado", nroEmpleado);
            //intent2.putExtra("fechaHoy", fechaHoy);
            //startActivity(intent2);
        }

        drawerLayout.closeDrawers(); // Cerrar drawer
        //setTitle(title); // Setear título actual

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupViewPager(ViewPager viewPager) {

        Bundle bundle = new Bundle();
        //bundle.putString("nroEmpleado", nroEmpleado);
        //bundle.putString("fechaHoy", fechaHoy);

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        AdFragment ad = new AdFragment();
        //nf.setArguments(bundle);

        FavoriteAdFragment fad = new FavoriteAdFragment();
        //np.setArguments(bundle);

        //nf.setArguments(bundle);
        adapter.addFragment(ad, "Anuncios");
        adapter.addFragment(fad, "Favoritos");
        viewPager.setAdapter(adapter);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
