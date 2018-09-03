package com.example.aliothman.devtask;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  onClicklong , SwipeRefreshLayout.OnRefreshListener {
    private List<Model_Repo> repos = new ArrayList<>();
    private RecyclerView recyclerView;
    private RepoAdapter repoAdapter;
    private  DBManager dbManager ;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        dbManager = new DBManager(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                // Fetching data from server
                   prepareRepoData();
            }
        });
    }

    private void prepareRepoData() {
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        AndroidNetworking.get("https://api.github.com/users/square/repos")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        // Stopping swipe refresh
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.e("data" , response);
                        JSONObject item = new JSONObject() ;
                        int id = 0;
                        String repo_name , description , owner_name, url_repo , url_owner , fork = "" ;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length()==0){
                                AppUtils.showInfoToast(MainActivity.this ,getString(R.string.no_data));
                            }
                            if (jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    item = jsonArray.getJSONObject(i);
                                    id = item.getInt("id");
                                    repo_name = item.getString("name");
                                    description = item.getString("description");
                                    url_repo  = item.getString("html_url");
                                    fork  = item.getString("fork");
                                    JSONObject innerItem = item.getJSONObject("owner");
                                    owner_name = innerItem.getString("login");
                                    url_owner  = innerItem.getString("html_url");
                                    repos.add(new Model_Repo(id, repo_name, description ,owner_name ,url_repo , url_owner , fork));
                                   ContentValues values= new ContentValues();
                                    values.put("Colownername" ,owner_name);
                                    values.put("Coldescreption" , description);
                                    values.put("ColReponame" ,repo_name);
                                    values.put("Colownerhtml" ,url_owner);
                                    values.put("Colrepohtml" ,url_repo);
                                    values.put("Colfork" ,fork);
                                    long num =  dbManager.insert(values);
                                    if(num != 0){
                                        Log.e("insertSuccess", "true");
                                    }
                                }
                            }
                            initiation();
                        }catch (Exception e){
                            AppUtils.showInfoToast(MainActivity.this,getString(R.string.error));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // Stopping swipe refresh
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void initiation() {
        repoAdapter = new RepoAdapter(repos ,getBaseContext() , this);
        repoAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(repoAdapter);

    }

    public void showCustomDialog(boolean status , final String Url_repo , final String url_owner ) {
        final Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.custom_dialog);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        Button button = myDialog.findViewById(R.id.owner_html);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = url_owner;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                myDialog.dismiss();
            }
        });
        Button button1  = myDialog.findViewById(R.id.repo_html);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Url_repo;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                myDialog.dismiss();
            }
        });
        if (status) {
            myDialog.show();
        }else {
            myDialog.dismiss();
        }
    }

    @Override
    public void data(String data , String url_owner , String url_repo) {
        if(data == "openDialog"){
            showCustomDialog(true , url_owner , url_repo);
        }
    }

    @Override
    public void onRefresh() {
        // Fetching data from server
        dbManager.ClearAll();
        // Remember to CLEAR OUT old items before appending in the new ones
       if(!repos.isEmpty()) {
            repos.clear();
        }
        prepareRepoData();
    }
}
