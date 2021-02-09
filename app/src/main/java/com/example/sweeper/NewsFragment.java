package com.example.sweeper;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;


public class NewsFragment extends Fragment {

    ArrayList<NewsData> newsDataArrayList = new ArrayList<NewsData>();
    ArrayList<NewsDataListViewItem> newsDataViewItems = new ArrayList<NewsDataListViewItem>();

    ListView listView;
    RSSNewsListViewAdapter adapter;

    Context context;

    Handler handler = new Handler();
    ProgressDialog progressDialog;

    SwipeRefreshLayout swipeRefreshLayout;
    SwipeDismissListViewTouchListener touchListener;
    RequestQueue queue;

    private static final String TAG = "NewsFragment";
    private static String rssUrl = "https://news.google.co.kr/news/feeds?pz=1&cf=all&ned=kr&hl=ko&topic=e&output=rss";
    String url ="https://newsapi.org/v2/top-headlines?country=kr&apiKey=a8302c39d3db490687a17b9e29a96d1c";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 프래그먼트 레이아웃 설정
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        context = container.getContext();
        queue = Volley.newRequestQueue(context);

        // 당겨서 새로고침을 위한 SwipeRefreshLayout
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);

        // Adapter 생성
        adapter = new RSSNewsListViewAdapter(context, R.layout.listview_card_item, newsDataViewItems);

        // 리스트뷰 참조 및 Adapter달기
        listView = (ListView) view.findViewById(R.id.list_view1);
        listView.setAdapter(adapter);

        // 리스트뷰의 아이템 선택 시 해당 뉴스 페이지로 연결
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                // TODO
                NewsDataListViewItem item = (NewsDataListViewItem) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getNewsData().getUrl()));

                context.startActivity(intent);
            }
        });

        // 밀어서 삭제를 위한 SwipeDismissTouchListener 설정
        touchListener =
                new SwipeDismissListViewTouchListener(listView, new SwipeDismissListViewTouchListener.DismissCallbacks() {

                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            adapter.remove(adapter.getItem(position));
                            swipeDirection();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener(touchListener.makeScrollListener());

        // 리스트 뷰의 아이템을 길게 누르면 공유하기 메뉴 표시
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                PopupMenu pMenu = new PopupMenu(context, view);
                getActivity().getMenuInflater().inflate(R.menu.news_menu, pMenu.getMenu());
                final int position = i;

                // 공유를 위한 인텐트 생성 및 실행
                pMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");

                        Intent chooser = Intent.createChooser(intent, getString(R.string.news_share_text));
                        context.startActivity(chooser);
                        return false;
                    }
                });

                pMenu.show();
                return true;
            }
        });

        // 당겨서 새로고침을 하는 OnRefreshListener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showRSS(url);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // 프래그먼트 생성시 뉴스 표시
        showRSS(url);

        return view;
    }

    // 밀어서 삭제할 때 방향을 보고 좋아요 싫어요 표시
    void swipeDirection() {
        if(touchListener.getSwipeDir() == 1) {
            //TODO Right Swipe
            Toast.makeText(context, "싫어요", Toast.LENGTH_SHORT).show();
        }
        else {
            //TODO Left Swipe
            Toast.makeText(context, "좋아요", Toast.LENGTH_SHORT).show();
        }
    }

    // 뉴스 표시
    void showRSS(String urlStr) {
        try {
            // 새로고침 중일 때 표시할 다이얼로그
            progressDialog = ProgressDialog.show(context, "RSS Refresh", "RSS 정보 업데이트 중...", true, false);

            // 뉴스를 가져오는 스레드 생성 및 실행
            RefreshThread thread = new RefreshThread(urlStr);
            thread.start();
        }
        catch (Exception e) {
            Log.e(TAG, "Error", e);
        }
    }

    class RefreshThread extends Thread {
        String urlStr;

        public RefreshThread(String str) {
            urlStr = str;
        }

        @Override
        public void run() {
            // 뉴스 데이터를 보관하는 ArrayList 초기화
            newsDataArrayList.clear();

            // 스트링을 통한 HTTP Request
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlStr,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                JSONArray arrayarticles = jsonObj.getJSONArray("articles");

                                for (int i = 0, j = arrayarticles.length(); i < j; i++) {
                                    JSONObject obj = arrayarticles.getJSONObject(i);

                                    //response -> NEWS Data class 분류
                                    NewsData newsData = new NewsData();
                                    newsData.setTitle(obj.getString("title"));
                                    newsData.setUrlToImage(obj.getString("urlToImage"));
                                    newsData.setContent(obj.getString("description"));
                                    newsData.setUrl(obj.getString("url"));

                                    // 뉴스 데이터를 ArrayList에 추가
                                    newsDataArrayList.add(newsData);

                                    Log.d(TAG, "Add News " + i);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            // 큐에 Request 추가
            queue.add(stringRequest);

            // 5초 대기 후 뉴스 리스트 뷰를 표시하는 스레드 실행
            handler.postDelayed(updateRSSRunnable, 5000);
        }
    }

    Runnable updateRSSRunnable = new Runnable() {
        public void run() {
            try {
                //리스트 뷰 아이템 ArrayList 초기화
                newsDataViewItems.clear();

                Log.d(TAG, "Get News "+ newsDataArrayList.size());

                // 뉴스 데이터 ArrayList에서 아이템을 가져와 리스트 뷰 아이템 ArrayList에 추가
                for (int i = 0; i < newsDataArrayList.size(); i++) {
                    NewsData newsItem = (NewsData) newsDataArrayList.get(i);

                    NewsDataListViewItem newsDataListViewItem = new NewsDataListViewItem();

                    newsDataListViewItem.setNewsData(newsItem);

                    newsDataViewItems.add(newsDataListViewItem);
                }
                // 어댑터 새로고침
                adapter.notifyDataSetChanged();

                // 새로고침 다이얼로그 끄기
                progressDialog.dismiss();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    };
}
