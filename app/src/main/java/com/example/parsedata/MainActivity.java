package com.example.parsedata;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParseAdapter adapter;
    private ArrayList<ParseItem> parseItems = new ArrayList<>();
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;
    private ImageButton settings_button1;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();//Full Screen Activity in Android

        settings_button1 = (ImageButton) findViewById(R.id.settings_button);//imageButton)))
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        refreshLayout = findViewById(R.id.swipeLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParseAdapter(parseItems, this);
        recyclerView.setAdapter(adapter);


        loadItems();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });


        settings_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });

    }

    private void openActivity() {
        Intent openActivity = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(openActivity);
    }

    private void loadItems() {
        Content content = new Content();
        content.execute();
    }


    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
            parseItems.clear();
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String list_sites = prefs.getString("list_sites", "");
            String list_city = prefs.getString("list_city", "");
            String signature = prefs.getString("signature", "");

            new CityChange(list_city);

            switch (list_sites) {
                case "Avito":
                    try {
                        String arg = "&page=";
                        String url = "https://youla.ru/petrozavodsk?q=красная%20куртка";
                        //https://youla.ru/petrozavodsk?q=красная%20куртка&page=1
                        for (int j = 0; j < 7; j++) {//кол-во страниц парсинга J
                            Document doc = Jsoup.connect((url) + arg + j).get();//+ добавлять фильтры поиска
                            Log.d("createurl", "URLpage" + j);
                            Elements data = doc.select("li.product_item");
                            int size = data.size();
                            Log.d("doc", "doc: " + doc);
                            Log.d("data", "data: " + data);
                            Log.d("size", "" + size);
                            for (int i = 0; i < size; i++) {
                                String imgUrl = data.select("div.product_item__image")
                                        .select("image")
                                        .eq(i)
                                        .attr("xlink:href");

                                String title = data.select("div.product_item__title")
                                        .select("div")
                                        .eq(i)
                                        .text();

                                String detailUrl = data.select("a")
                                        .eq(i)
                                        .attr("href");

                                String price = data.select("div.product_item__description")
                                        .eq(i)
                                        .text()
                                        .replace("руб.", "");//убирает руб.
                                parseItems.add(new ParseItem(imgUrl, title, price, detailUrl));
                                Log.d("items", "img: " + imgUrl + " . title: " + title + " price: " + price + " detal url: " + detailUrl);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case "Youla":
                    System.out.println("Today is sunny !");
                    break;
                case "Ebay":
                    System.out.println("Today is rainy!");
                    break;



            }


            return null;
        }
    }

}