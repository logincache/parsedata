package com.example.parsedata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();//Full Screen Activity in Android

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
    }
            private void loadItems() {
                Content content = new Content();
                content.execute();
            }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_item, menu);
//
//        MenuItem searchViewItem = menu.findItem(R.id.action_search);
//        // Get the search view and set the searchable configuration
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) searchViewItem.getActionView();
//        searchView.setQueryHint("Search...");
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false); //Do not iconfy the widget; expand it by default
//
//        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                newText = newText.toLowerCase();
//                ArrayList<ParseItem> newList = new ArrayList<>();
//                for (ParseItem parseItem : parseItems) {
//                    String title = parseItem.getTitle().toLowerCase();
//
//                    // you can specify as many conditions as you like
//                    if (title.contains(newText)) {
//                        newList.add(parseItem);
//                    }
//                }
//                // create method in adapter
//                adapter.setFilter(newList);
//
//                return true;
//            }
//        };
//
//        searchView.setOnQueryTextListener(queryTextListener);
//
//        return true;
//
//    }

    private class Content extends AsyncTask<Void,Void,Void>{

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

            try {
                String arg = "&page=";
                String url = "https://youla.ru/petrozavodsk?q=красная%20куртка";
                            //https://youla.ru/petrozavodsk?q=красная%20куртка&page=1
                for(int j = 0; j < 3;j++) {//кол-во страниц парсинга J
                    Document doc = Jsoup.connect((url)+ arg + j).get();//+ добавлять фильтры поиска
                    Log.d("createurl", "URLpage"+ j);
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

//            try {
//            String url = "https://m.avito.ru/petrozavodsk/tovary_dlya_kompyutera/monitory-ASgBAgICAUTGB4Bo?context=H4sIAAAAAAAA_wFCAL3_YToxOntzOjU6Inhfc2d0IjtzOjQwOiI4YjY0YjZmNDlmNmQyMGM3ZTVkMjNiY2FkZmVlMmYxYWQ5Y2RkMGUzIjt9snV-4kIAAAA";//avito
//            Document doc = Jsoup.connect(url).get();
//
//            Elements data = doc.select("div.iva-item-content-m2FiN");
//
//            int size = data.size();
//            Log.d("doc", "doc: "+doc);
//            Log.d("data", "data: "+data);
//            Log.d("size", ""+size);
//            for (int i = 0; i < size; i++) {
//                String imgUrl = data.select("img.photo-slider-image-1fpZZ")
//                        .eq(i)
//                        .attr("src");
//
//                String title = data.select("h3")
//                        .eq(i)
//                        .text();
//
//                String detailUrl = data.select("a")
//                        .eq(i)
//                        .attr("href");
//
//                String price = data.select("span.price-price-32bra")
//                        .eq(i)
//                        .text();
//                parseItems.add(new ParseItem(imgUrl, title, price, detailUrl));
//                Log.d("items", "img: " + imgUrl + " . title: " + title + " price: " + price);
//            }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                String url = "https://www.ebay.com/sch/i.html?_from=R40&_trksid=p2380057.m570.l1313&_nkw=монитор&_sacat=0";//ebay
//                Document doc = Jsoup.connect(url).get();
//
//                Elements data = doc.select("li.s-item");
//
//                int size = data.size();
//                Log.d("doc", "doc: "+doc);
//                Log.d("data", "data: "+data);
//                Log.d("size", ""+size);
//                for (int i = 0; i < size; i++) {
//                    String imgUrl = data.select("img.s-item__image-img")
//                            .eq(i)
//                            .attr("src");
//
//                    String title = data.select("h3")
//                            .eq(i)
//                            .text();
//
//                    String detailUrl = data.select("a")
//                            .eq(i)
//                            .attr("href");
//
//                    String price = data.select("span.s-item__price")
//                            .eq(i)
//                            .text();
//                    parseItems.add(new ParseItem(imgUrl, title, price, detailUrl));
//                    Log.d("items", "img: " + imgUrl + " . title: " + title + " price: " + price);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////
////
           return null;
        }
    }
}