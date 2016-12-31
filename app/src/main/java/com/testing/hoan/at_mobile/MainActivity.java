package com.testing.hoan.at_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.transition.Transition;
import android.transition.TransitionInflater;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,RecyclerViewFrag.RecyclerViewListener {
    private static final String TAG="MainActivity";
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
            TabFragment tf=new TabFragment();
            transaction.replace(R.id.centerFragment,tf);
            transaction.commit();
        }
        requestQueue = Volley.newRequestQueue(this);

        session=new SessionManager(getApplicationContext());
        if(!session.isLoggedin()){
            // initialize activity without logging in
        }

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


        } else if (id == R.id.nav_YourOrder) {


        } else if (id == R.id.nav_Settings) {

        } else if (id == R.id.nav_PromotionCode) {

        } else if (id == R.id.nav_Logout) {
            session.setLogin(false);
            Intent logout=new Intent(MainActivity.this,LoginActivity.class);
            finish();
            startActivity(logout);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(int position,ArrayList<Events> eventsArrayList) {
        String transitionName="";
        String imageTransitionName="";
        RecyclerViewFrag fragment1=new RecyclerViewFrag();
        read_news fragment2=new read_news();
        //transisions
        Transition changeTransform = TransitionInflater.from(this).
                inflateTransition(android.R.transition.move);
        Transition explodeTransform = TransitionInflater.from(this).
                inflateTransition(android.R.transition.explode);
        // fragment1-recyclerViewFrag
        fragment1.setSharedElementReturnTransition(changeTransform);
        fragment1.setExitTransition(explodeTransform);
        //fragment2-read_news
        fragment2.setSharedElementEnterTransition(changeTransform);
        fragment2.setEnterTransition(explodeTransform);
        //Shared Views
        NetworkImageView imageView=(NetworkImageView) findViewById(R.id.imageItem);
        TextView title=(TextView) findViewById(R.id.testTv);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                transitionName = title.getTransitionName();
                imageTransitionName = imageView.getTransitionName();

        }

        Bundle bundle=new Bundle();
        bundle.putString("titleTransitionName",transitionName);
        bundle.putString("imageTransisionName",imageTransitionName);
        bundle.putString("titleText",title.getText().toString());

        Events e=(Events)eventsArrayList.get(position);
        Log.i(TAG,"current body text of "+position +"item"+" is "+e.getBody());
        bundle.putString("body",e.getBody());
        bundle.putString("id",e.getId());
        Log.i(TAG,"current id of "+position +"item"+" is "+e.getId());
        Log.i("onClickListener",title.getText().toString());
        fragment2.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                .replace(R.id.centerFragment, fragment2)
                .addToBackStack("transaction")
                .addSharedElement(imageView,imageTransitionName)
                .addSharedElement(title, transitionName);
        // Apply the transaction
        ft.commit();
        Log.i("onClickListener",""+position);
        Log.i("onclicklistener","title: "+transitionName);
        Log.i("onclicklistener","image: "+imageTransitionName);
    }
    public void pushComment(final String comment, final int blogID ){
        String tag_string_req="req_comment";
        StringRequest strReq=new StringRequest(Request.Method.GET, AppConfig.GET_COMMENTS_URL+blogID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Get comment Response: " + response.toString());
                try{


                }catch(Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Toast.makeText(MainActivity.this, "Fail to get json! check your connection", Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("blogID", String.valueOf(blogID));
                params.put("comment",comment);

                return params;
            }

        };
    }


}
