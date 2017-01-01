package com.testing.hoan.at_mobile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hoan on 12/12/2016.
 */

public class read_news extends Fragment {
    private listViewAdapter adapter;
    private String TAG="read_news fragment";
    private ListView commentLv;
    private String pushID;
    private String name;
    private String pushComment;
    ArrayList<Comments> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle=getArguments();
        String transitionName="";
        String titleText="";
        String bodyText="";
        String id="";
        if(bundle!=null){
            transitionName=bundle.getString("titleTransitionName");
            titleText=bundle.getString("titleText");
            bodyText=bundle.getString("body");
            id=bundle.getString("id");
            pushID=bundle.getString("id");
            Log.i(TAG,titleText);
            Log.i(TAG,bodyText);
            Log.i(TAG,id);
        }
        list=new ArrayList<Comments>();
        getComments(id,list);
        View root=(View)inflater.inflate(R.layout.read_news,container,false);
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.pushComment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                final EditText userName=(EditText) promptsView.findViewById(R.id.editTextName);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        setPushComment(pushID,userInput.getText().toString(),userName.getText().toString(),"comment@gmail.com");
                                        name=userName.getText().toString();
                                        pushComment=userInput.getText().toString();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        root.findViewById(R.id.title_news).setTransitionName(transitionName);
        TextView titleView=(TextView) root.findViewById(R.id.title_news) ;
        titleView.setText(titleText);
        TextView newsBody=(TextView) root.findViewById(R.id.newsBody);
        newsBody.setText(bodyText);
        adapter =new listViewAdapter(getContext(),list);
        commentLv =(ListView) root.findViewById(R.id.listView);
        commentLv.setAdapter(adapter);
        return root;
    }
    public void setPushComment(String id, String newComment, String newName, String newEmail) {
        final String comment=newComment;
        final String name=newName;
        final String email=newEmail;
        Log.i(TAG,comment);
        Log.i(TAG,name);
        Log.i(TAG,newEmail);
        try {
            String tag_string_req = "req_login";
            StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.PUSH_COMMENT_URL + id + "/addComment", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object =new JSONObject(response);
                        int status=object.getInt("code");
                        if(status==200){
                            Comments comment=new Comments();
                            comment.setUserName(name);
                            comment.setComment(pushComment);
                            list.add(comment);
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(getContext(), "Failed to send out data! check your connection", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG,response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                    Toast.makeText(getContext(), "Fail to login! check your connection", Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "x-www-form-urlencoded";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("body", comment);
                    return params;
                }

            };
            ApplicationControl.getInstance(getContext()).addToRequestQueue(strReq,"push comments");
        }
        catch (Exception err){
            err.printStackTrace();
        }
    }

    public void getComments(String blogID, final ArrayList<Comments> commentList){
        String tag_string_req="req_comment";
        StringRequest strReq=new StringRequest(Request.Method.GET, AppConfig.GET_COMMENTS_URL+blogID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Get comment Response: " + response.toString());
                try{
                    JSONObject jsonObject= new JSONObject(response);
                    JSONObject data=jsonObject.getJSONObject("data");
                    JSONArray array =data.getJSONArray("comments");
                    for(int i=0;i<array.length();i++){
                        JSONObject commentObj=array.getJSONObject(i);
                        String name=commentObj.getString("name");
                        String comment =commentObj.getString("body");
                        Comments com =new Comments();
                        com.setUserName(name);
                        com.setComment(comment);
                        commentList.add(com);
                        adapter.notifyDataSetChanged();

                    }

                }catch(Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        JSONObject data=obj.getJSONObject("data");
                        JSONArray array =data.getJSONArray("comments");
                        for(int i=0;i<array.length();i++){
                            JSONObject commentObj=array.getJSONObject(i);
                            String comment =commentObj.getString("body");
                            Comments com =new Comments();
                            com.setComment(comment);
                            commentList.add(com);

                        }
                        commentLv.setAdapter(new listViewAdapter(getContext(),list));
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        }){
        };
        ApplicationControl.getInstance(getContext()).addToRequestQueue(strReq,"get comments");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"read news fragment onSaveInstance");
    }
}
