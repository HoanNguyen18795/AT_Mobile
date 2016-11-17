package com.testing.hoan.at_mobile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerView.OnScrollChangeListener {
    //Volley Request Queue
    private RequestQueue requestQueue;

    private SessionManager session;
    // progress bar to display while the list is being loaded
    private ProgressBar mProgress;
    //events list
    private List<Events> eventList;
    // Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    // counter to send the page number
    private int counter=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // initializing Views
        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // initializing list of events
        eventList=new ArrayList<Events>();
        requestQueue = Volley.newRequestQueue(this);
        //initializing the custom adapter
        adapter=new RecyclerViewAdapter(eventList,this);
        //adding the adapter to recyclerView
        recyclerView.setAdapter(adapter);
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
            showCateGory();
        } else if (id == R.id.nav_YourOrder) {
            showOrder();
        } else if (id == R.id.nav_Settings) {

        } else if (id == R.id.nav_PromotionCode) {

        } else if (id == R.id.nav_Logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
    if(isLastItem(recyclerView)){
    getData();
     }

    }
    private JsonArrayRequest getDataFromServer(int count){
        final ProgressBar mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminate(true);

        //volley request
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(AppConfig.EVENTS_URL + String.valueOf(count), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                // parsing data from API
                parseData(jsonArray);
                mProgressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgressBar.setVisibility(View.GONE);
                // error
                Toast.makeText(MainActivity.this, "No More Items Available", Toast.LENGTH_SHORT).show();
            }
        });
        return jsonArrayRequest;
    }
    private void getData(){
        // adding the request to the request que
        //ApplicationControl.getInstance(this).addToRequestQueue(getDataFromServer(counter));
        requestQueue.add(getDataFromServer(counter));
        counter++;
    }
    private void parseData(JSONArray array){
        for(int i=0;i<array.length();i++){
            Events event= new Events();
            JSONObject json=null;
            try{
                json=array.getJSONObject(i);
                event.setImageUrl(json.getString("image"));

            }catch(Exception ex){
                ex.printStackTrace();
            }
            eventList.add(event);
        }
        // notifying the adapter that data has been changed
        adapter.notifyDataSetChanged();
    }
    // check that the recycler view has reached the last item
    public boolean isLastItem(RecyclerView mrecyclerView){
        if(mrecyclerView.getAdapter().getItemCount()!=0){
            int lastVisibleItemPosition=((LinearLayoutManager)mrecyclerView.getLayoutManager()).findLastVisibleItemPosition();
            if(lastVisibleItemPosition !=RecyclerView.NO_POSITION&& lastVisibleItemPosition==mrecyclerView.getAdapter().getItemCount()-1){
                return true;
            }
        }
        return false;
    }
    public void showOrder(){
        Fragment order=new OrderFragment();
        FragmentManager fm=getFragmentManager();
        //fm.beginTransaction().replace(R.id.main,order).addToBackStack("order fragment").commit();
        Log.i("order","replaced order");
    }
    public void showCateGory(){
        Fragment order=new CateGoryFragment();
        FragmentManager fm=getFragmentManager();
        //fm.beginTransaction().replace(R.id.main,order).addToBackStack("category fragment").commit();
        Log.i("category","replaced category");
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
