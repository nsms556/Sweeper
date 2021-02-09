package com.example.sweeper;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

// 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의
public class RSSNewsListViewAdapter extends ArrayAdapter {
    int resourceId;                     //resource ID 저장

    RSSNewsListViewAdapter(Context context, int resource, ArrayList<NewsDataListViewItem> list) {
        super(context, resource, list);
        Fresco.initialize(context);
        this.resourceId = resource;     //resource ID 복사
    }

    // 새롭게 만든 Layout을 위한 View를 생성하는 코드
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // 생성자로부터 저장된 resourceId(listview_btn_item)에 해당하는 Layout을 inflate하여 convertView 참조 획득.
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId, parent, false);
        }

        // 레이아웃의 아이템 객체화
        final SimpleDraweeView imageView = (SimpleDraweeView) convertView.findViewById(R.id.ImageView_title);
        final TextView titleView = (TextView) convertView.findViewById(R.id.TextView_title);
        final TextView contentView = (TextView) convertView.findViewById(R.id.TextView_content);
        final NewsDataListViewItem listViewItem = (NewsDataListViewItem) getItem(position);

        // 리스트 뷰 아이템의 뉴스 데이터 불러오기
        Uri uri = Uri.parse(listViewItem.getNewsData().getUrlToImage());

        // 리스트 뷰 아이템 레이아웃 설정
        titleView.setText(listViewItem.getNewsData().getTitle());
        imageView.setImageURI(uri);
        contentView.setText(listViewItem.getNewsData().getContent());

        return convertView;
    }
}
