package com.apkvisionewsapp.apkvisionnews;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    RecyclerView post_list;

    List<Post> posts;
    PostsAdapter adapter;

    private SearchView searh_view;



    LinearLayoutManager linearLayoutManager;



    String url="https://apkvision.com/wp-json/wp/v2/news?_embed3&page=";


    private boolean isLoading;
    private boolean islastpage;

    private int page=1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        posts = new ArrayList<>();




        searh_view=findViewById(R.id.searh_view);

        searh_view.findFocus();

        searh_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterlist(newText);
                return false;
            }
        });


        drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_view);
        toggle.setDrawerIndicatorEnabled(true);//enable hamburger sign
        drawer.addDrawerListener(toggle);
        toggle.syncState();





        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.telegram:

                        Intent intent=new Intent(Intent
                                .ACTION_VIEW, Uri.parse("https://t.me/apkvision"));

                        startActivity(intent);
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.youtube:

                        Intent intent1=new Intent(Intent
                                .ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCG61ewrtOJ9pVtCqUAv1WMQ"));

                        startActivity(intent1);
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.instra:

                        Intent intent2=new Intent(Intent
                                .ACTION_VIEW, Uri.parse("https://www.instagram.com/apkvision/"));

                        startActivity(intent2);
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.privacy:

                        startActivity(new Intent(MainActivity.this,Privacy.class));

                        drawer.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.share:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                "Check "+getString(R.string.app_name)+"♥ awesome! app \n" + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                        sendIntent.setType("text/plain");
                        drawer.closeDrawer(GravityCompat.START);
                        startActivity(sendIntent);

                        break;


                }

                return true;
            }
        });


        post_list=findViewById(R.id.post_list_);
        /// extractPosts(getResources().getString(R.string.url));

         linearLayoutManager=new LinearLayoutManager(this);

        //GridLayoutManager manager = new GridLayoutManager(this, 2);
        post_list.setLayoutManager(linearLayoutManager);


        adapter = new PostsAdapter(posts);
        post_list.setAdapter(adapter);



        fethitem();




        /*
            // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray=new JSONArray(response);

                            for (int i=0; i<jsonArray.length();i++){

                                Post p=new Post();

                                JSONObject jsonObject= (JSONObject) jsonArray.get(i);

                                JSONObject titleObject = jsonObject.getJSONObject("title");
                                p.setTitle(titleObject.getString("rendered"));



                                // extract the content
                                JSONObject contentObject = jsonObject.getJSONObject("content");
                                p.setContent(contentObject.getString("rendered"));

                                //extract the excerpt
                                JSONObject excerptObject = jsonObject.getJSONObject("excerpt");
                                p.setExcerpt(excerptObject.getString("rendered"));


                                // extract feature image
                                JSONObject imageObject = jsonObject.getJSONObject("yoast_head_json");
                                p.setFeature_image(imageObject.getString("og_image"));

                                JSONObject img = jsonObject.getJSONObject("yoast_head_json");

                                JSONArray ca = img.getJSONArray("og_image");

                                for (int j = 0; j < ca.length(); j++) {

                                    JSONObject showimg = ca.getJSONObject(j);
                                    String iurl = showimg.getString("url");

                                    p.setFeature_image(iurl);

                                }

                                posts.add(p);

                                adapter.notifyDataSetChanged();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

// Add the request to the RequestQueue.

       */


        post_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy>0) {

                    int visibleitem = linearLayoutManager.getChildCount();

                    int totalitem = linearLayoutManager.getItemCount();

                    int firstvisibleitem = linearLayoutManager.findFirstVisibleItemPosition();


                    if (!isLoading) {

                        if ((visibleitem + firstvisibleitem >= totalitem) && firstvisibleitem > 0) {


                            if(page!=6) {


                                page++;


                                fethitem();

                                //Toast.makeText(MainActivity.this, "page num" + page, Toast.LENGTH_SHORT).show();
                            }


                        }
                    }


                }


            }
        });


    }

    private void fethitem() {


        isLoading=true;

         String geturl=url+page;
         // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, geturl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray=new JSONArray(response);

                            for (int i=0; i<jsonArray.length();i++){

                                Post p=new Post();

                                JSONObject jsonObject= (JSONObject) jsonArray.get(i);

                                JSONObject titleObject = jsonObject.getJSONObject("title");
                                p.setTitle(titleObject.getString("rendered"));

                                // extract the content
                                JSONObject contentObject = jsonObject.getJSONObject("content");
                                p.setContent(contentObject.getString("rendered"));

                                //extract the excerpt
                                JSONObject excerptObject = jsonObject.getJSONObject("excerpt");
                                p.setExcerpt(excerptObject.getString("rendered"));

                                // extract feature image
                                JSONObject imageObject = jsonObject.getJSONObject("yoast_head_json");
                                p.setFeature_image(imageObject.getString("og_image"));

                                JSONObject img = jsonObject.getJSONObject("yoast_head_json");

                                JSONArray ca = img.getJSONArray("og_image");

                                for (int j = 0; j < ca.length(); j++) {

                                    JSONObject showimg = ca.getJSONObject(j);
                                    String iurl = showimg.getString("url");

                                    p.setFeature_image(iurl);

                                }

                                posts.add(p);

                                adapter.notifyDataSetChanged();

                                    isLoading=false;


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

// Add the request to the RequestQueue.





    }

    private void filterlist(String newText) {

        List<Post>filterlist=new ArrayList<>();

        for (Post item:posts){


            if (item.getTitle().toLowerCase().contains(newText.toLowerCase())){

                filterlist.add(item);
            }

        }
        if (filterlist.isEmpty()){


        }else {

            adapter.setfilterlist(filterlist);
        }

    }



}