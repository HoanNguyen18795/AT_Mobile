package com.testing.hoan.at_mobile;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoan on 11/21/2016.
 */
public class RecyclerViewFrag extends Fragment implements RecyclerView.OnScrollChangeListener{
    private RecyclerViewListener mRecyclerViewListener;
    private static final String TAG="RecyclerView Fragment";
    private RequestQueue requestQueue;
    private ArrayList<Events> eventsList;
    private ProgressBar mProgressBar;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayout;
    private RecyclerView mRecyclerView;
    private int counter=1;
    private int code=-1;

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //if(isLastItem(mRecyclerView)){
          //  getData();
        //}
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            getData();
        }
        getData();
        Log.i(TAG,"recycler fragment onActivityCreated");
    }

    public interface RecyclerViewListener{
        public void onItemSelected(int position,ArrayList<Events> events);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            Activity activity;
            if(context instanceof Activity){
                activity=(Activity) context;
                mRecyclerViewListener=(RecyclerViewListener) activity;
            }
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement RecyclerViewListener");
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
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mRecyclerViewListener.onItemSelected( position,eventsList);
            }
        }));
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
    private JsonObjectRequest getDataFromServer() {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, AppConfig.EVENTS_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            parseData(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mProgressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        error.printStackTrace();
                        mProgressBar.setVisibility(View.GONE);
                        Log.i(TAG,"Failed to get response");
                    }
                });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return jsObjRequest;
    }
    private void getData(){
        // adding the request to the request que
        //ApplicationControl.getInstance(this).addToRequestQueue(getDataFromServer(counter));
        requestQueue.add(getDataFromServer());
        //counter++;
    }
    private void parseData(JSONObject obj) throws JSONException {
        JSONArray array=obj.getJSONArray("data");
        Log.i(TAG,"data array size: "+array.length());
        for(int i=0;i<array.length();i++){
            Events event= new Events();
            JSONObject json=null;
            try{
                json=array.getJSONObject(i);
                event.setId(json.getString("_id"));
                event.setBody(json.getString("body"));
                try {
                    Log.i(TAG, json.getString("body"));
                }catch(JSONException e){
                    e.printStackTrace();
                }
               // if(json.getString("image")!=null) {
                   // event.setImageUrl(json.getString("image"));
               // }
               // else{
                    event.setImageUrl("http://cucgach.mobi/wp-content/uploads/2016/04/icon-android-150x150.png");
              //  }
                if(json.getString("title")!=null) {
                    event.setTitle(json.getString("title"));
                }
                else{
                    event.setTitle("Untitled");
                }
            }catch(Exception ex){
                ex.printStackTrace();
                Log.i(TAG,"could not parse json");
            }
            eventsList.add(event);
        }
        // notifying the adapter that data has been changed
        mRecyclerViewAdapter.notifyDataSetChanged();
    }
    public ArrayList getEventList(){
        return eventsList;
    }

}
