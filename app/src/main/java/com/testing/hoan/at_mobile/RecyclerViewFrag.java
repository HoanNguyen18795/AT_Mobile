package com.testing.hoan.at_mobile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoan on 11/21/2016.
 */
public class RecyclerViewFrag extends Fragment implements RecyclerView.OnScrollChangeListener{
    private static final String TAG="RecyclerView Fragment";
    private RequestQueue requestQueue;
    private List<Events> eventsList;
    private ProgressBar mProgressBar;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayout;
    private RecyclerView mRecyclerView;
    private int counter=1;
    private String mDataset[]={"asdas","awdasd","wdfdddd","this is a test","wohoooooo","yeahhhhhhhhh","hope this work","please","oh it works"};


    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if(isLastItem(mRecyclerView)){
            getData();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get data first here
        requestQueue= Volley.newRequestQueue(getActivity());
        getData();
        Log.i(TAG,"fragment oncreate");
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.recyclerview_fragment,container,false);
        rootview.setTag("RecyclerViewFragment on create view inflated rootview");
        mProgressBar =(ProgressBar) rootview.findViewById(R.id.progressbar);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
        // initialize recyclerview
        mRecyclerView=(RecyclerView) rootview.findViewById(R.id.recyclerview);
        //setting up the recyclerview
        mRecyclerView.setHasFixedSize(true);
        // initialize a layout reference
        mLayout=new LinearLayoutManager(getActivity());
        // this layout will define how  the recyclerview items are displayed
        mRecyclerView.setLayoutManager(mLayout);
        // initialize custom adapter
        eventsList=new ArrayList<Events>();
        mRecyclerViewAdapter=new RecyclerViewAdapter(eventsList,getActivity());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        return rootview;
    }

    private JsonArrayRequest getDataFromServer(int count){
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
                Log.i(TAG,"Failed to get response");
                // error

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
                event.setTitle(json.getString("title"));
            }catch(Exception ex){
                ex.printStackTrace();
                Log.i(TAG,"could not parse json");
            }
            eventsList.add(event);
        }
        // notifying the adapter that data has been changed
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

}
