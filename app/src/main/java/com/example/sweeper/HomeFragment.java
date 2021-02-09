package com.example.sweeper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment{

    Context context;

    ListView listView;
    ArrayAdapter adapter;

    static ArrayList<String> wordList = new ArrayList<String>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 레이아웃 설정
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = container.getContext();

        // 내 설정 메뉴용 툴바 설정
        setHasOptionsMenu(true);

        // 리스트뷰에 어댑터 설정
        listView = root.findViewById(R.id.word_list);
        adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, wordList);
        listView.setAdapter(adapter);

        // 리스트뷰 아이템 길게 누를 시 삭제하기 팝업 메뉴
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                PopupMenu pMenu = new PopupMenu(context, view);
                getActivity().getMenuInflater().inflate(R.menu.listview_con_menu, pMenu.getMenu());
                final int position = i;

                // 삭제하기 클릭 시 해당 단어 삭제
                pMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        adapter.remove(adapter.getItem(position));
                        adapter.notifyDataSetChanged();
                        return false;
                    }
                });

                pMenu.show();
                return false;
            }
        });

        return root;
    }

    // 툴바 레이아웃 설정
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}