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
            String find = prefs.getString("signature", "");

            if (list_city.equals("Петрозаводск")) {
                list_city="petrozavodsk";
            }
            if (list_city.equals("Санкт-Петербург")) {
                list_city="sankt-peterburg";

            }
            if (list_city.equals("Москва")) {
                list_city="moskva";

            }
            if (list_city.equals("Краснодар")) {
                list_city="krasnodar";
            }

           Log.d("list_city", "list_city: " + list_city);
            switch (list_sites) {
                case "Avito":
                    try {
                        String url = "https://";
                        String domen = ".ru/";
                        String pref = "?q=";
                        String arg = "&page=";
                        for (int j = 0; j < 5; j++) {//кол-во страниц парсинга J
                            Log.d("find", "find: " + (url) +list_sites+domen+list_city+pref+find+arg + j);
                            Document doc = Jsoup.connect((url) +list_sites+domen+list_city+pref+find+arg + j).get();//+ добавлять фильтры поиска
                            Log.d("createurl", "URLpage" + j);

                            Elements data = doc.select("div.iva-item-content-m2FiN");

                        int size = data.size();
                        Log.d("doc", "doc: "+doc);
                        Log.d("data", "data: "+data);
                        Log.d("size", ""+size);
                        for (int i = 0; i < size; i++) {
                            String imgUrl = data.select("img.photo-slider-image-1fpZZ")
                                    .eq(i)
                                    .attr("src");

                            String title = data.select("h3")
                                    .eq(i)
                                    .text();

                            String detailUrl = data.select("div.iva-item-titleStep-2bjuh")
                                    .select("a")
                                    .eq(i)
                                    .attr("href");

                            String price = data.select("span.price-price-32bra")
                                    .eq(i)
                                    .text();
                            parseItems.add(new ParseItem(imgUrl, title, price, detailUrl));

                            Log.d("items", "img: " + imgUrl + " . title: " + title + " price: " + price +" detlAvito"+detailUrl);
                        }
                        }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    break;
                case "Youla":
                    try {
                        String url = "https://";
                        String domen = ".ru/";
                        String pref = "?q=";
                        String arg = "&page=";
                        for (int j = 0; j < 7; j++) {//кол-во страниц парсинга J
                            Log.d("find", "find: " + (url) +list_sites+domen+list_city+pref+find+arg + j);
                            Document doc = Jsoup.connect((url) +list_sites+domen+list_city+pref+find+arg + j).get();//+ добавлять фильтры поиска
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
                case "Ebay":
                        try {
                            //https://www.ebay.com/sch/i.html?_nkw=толстовка&_sacat=0&_pgn=3
                            String url = "https://";
                            String domen = ".com/";
                            String pref = "sch/i.html?_nkw=";
                            String arg = "&_sacat=0&_pgn=";
                            for (int j = 0; j < 5; j++) {//кол-во страниц парсинга J
                                Log.d("find", "find: " + (url) + list_sites + domen + pref + find + arg + j);
                                Document doc = Jsoup.connect((url) + list_sites + domen + pref + find + arg + j).get();//+ добавлять фильтры поиска
                                Log.d("createurl", "URLpage" + j);

                                Elements data = doc.select("li.s-item");

                                int size = data.size();
                                Log.d("doc", "doc: " + doc);
                                Log.d("data", "data: " + data);
                                Log.d("size", "" + size);
                                for (int i = 0; i < size; i++) {
                                    String imgUrl = data.select("img.s-item__image-img")
                                            .eq(i)
                                            .attr("src");

                                    String title = data.select("h3")
                                            .eq(i)
                                            .text();

                                    String detailUrl = data.select("div.s-item__image")
                                            .select("a")
                                            .eq(i)
                                            .attr("href");

                                    String price = data.select("span.s-item__price")
                                            .eq(i)
                                            .text();
                                    parseItems.add(new ParseItem(imgUrl, title, price, detailUrl));
                                    Log.d("items", "img: " + imgUrl + " . title: " + title + " price: " + price +" detalUrlEbay " + detailUrl);
                                }
                            }

                        }catch (IOException e) {
                         e.printStackTrace();
                         }
                        break;


                }


            return null;
        }
    }

}