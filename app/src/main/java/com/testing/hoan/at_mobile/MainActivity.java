package com.testing.hoan.at_mobile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Volley Request Queue
    private RequestQueue requestQueue;

    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(savedInstanceState==null){
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            RecyclerViewFrag recyclerFragment=new RecyclerViewFrag();
            transaction.replace(R.id.centerFragment,recyclerFragment);
            transaction.commit();
        }
        requestQueue = Volley.newRequestQueue(this);

        session=new SessionManager(getApplicationContext());
        if(!session.isLoggedin()){
            // initialize activity without logging in
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            // go back to the parent View
        } else if (id == R.id.nav_Category) {
            Fragment CateGoryFragment=new CateGoryFragment();
            showFragment(CateGoryFragment);

        } else if (id == R.id.nav_YourOrder) {
            Fragment OrderFragment=new OrderFragment();
            showFragment(OrderFragment);

        } else if (id == R.id.nav_Settings) {

        } else if (id == R.id.nav_PromotionCode) {

        } else if (id == R.id.nav_Logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void showFragment(Fragment fragment){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.animator.exit,R.animator.enter,R.animator.return_transition,R.animator.reenter);
        transaction.replace(R.id.centerFragment,fragment).addToBackStack("").commit();
    }

    public static class OrderFragment extends Fragment{
        private TextView mTextLabel;
        private TextView mTextTotal;
        private Button mRmvButton;
        private Button mPurchaseButton;
        public OrderFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.your_order, container, false);
             mTextLabel=(TextView) rootView.findViewById(R.id.label);
             mTextTotal=(TextView) rootView.findViewById(R.id.totalOrder);
             mRmvButton=(Button) rootView.findViewById(R.id.remove);
             mPurchaseButton=(Button) rootView.findViewById(R.id.purchase);
            Log.i("order","onCreateView");
            return rootView;
        }
    }
    public static class CateGoryFragment extends Fragment{
        private TextView apple;
        private TextView samsung;
        private TextView blackberry;
        private TextView accessories;
        private TextView used;
        public CateGoryFragment(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.category, container, false);
            apple=(TextView) rootView.findViewById(R.id.Apple);
            samsung=(TextView) rootView.findViewById(R.id.Samsung);
            blackberry=(TextView) rootView.findViewById(R.id.BlackBerry);
            accessories=(TextView) rootView.findViewById(R.id.Accessories);
            used=(TextView) rootView.findViewById(R.id.Used);
            Log.i("category","onCreateView");
            return rootView;
        }
    }
}
