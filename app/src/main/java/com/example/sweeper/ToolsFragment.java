package com.example.sweeper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ToolsFragment extends Fragment {

    ArrayList<String> listMenu = new ArrayList<String>();
    Context context;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 프래그먼트 레이아웃 설정
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        ListView listView = (ListView) root.findViewById(R.id.tool_list_view);
        context = container.getContext();

        // 기타 메뉴 화면에 아이템 추가
        listMenu.add("문의사항\n분산 모바일 정보 시스템@울산대학교");
        listMenu.add(getString(R.string.tools_list2));

        // 리스트 뷰 어댑터 생성 및 설정
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, listMenu);
        listView.setAdapter(adapter);

        // 리스트 뷰의 아이템 클릭 시 해당 기능으로 연결하는 OnItemClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //TODO OnItemClick Listener
                if(pos == 1) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://news.google.com"));
                    context.startActivity(intent);
                }
            }
        });

        return root;
    }
}