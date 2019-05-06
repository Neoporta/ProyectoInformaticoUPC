package com.acyspro.beacons.activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.acyspro.beacons.R;
import com.acyspro.beacons.activities.fragments.AdFragment;
import com.acyspro.beacons.activities.fragments.FavoriteAdFragment;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.widget.ShareButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager mViewPager;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();

        checkLoginStatus();

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

        if (title.equals("Logout Facebook")) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(getApplicationContext(), FacebookLoginActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawers(); // Cerrar drawer
        //setTitle(title); // Setear título actual

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_logout_facebook) {

            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(intent);
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(getApplicationContext(), FacebookLoginActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }*/
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        //int id = item.getItemId();
        return super.onOptionsItemSelected(item);
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

    private void checkLoginStatus()
    {
        if(AccessToken.getCurrentAccessToken()!=null) {
            Toast.makeText(getApplicationContext(),"Facebook OK.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"Sin Facebook.", Toast.LENGTH_LONG).show();
        }
    }

}
