package com.example.sweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class AddWordActivity extends AppCompatActivity {

    EditText editText;
    String data;
    InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 팝업 액티비티 이므로 타이틀 제거
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 레이아웃 설정
        setContentView(R.layout.activity_add_word);

        editText = (EditText)findViewById(R.id.edit_word);
        editText.requestFocus();

        // 액티비티 생성시 키보드 자동 표시
        inputMethodManager =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    // 추가 버튼 누르면 실행할 메서드
    public void mOnAdd(View view) {
        // editText로 부터 텍스트 가져오기
        data = editText.getText().toString();

        // 인텐트 생성 및 data 추가
        Intent intent = new Intent();
        intent.putExtra("word", data);
        setResult(RESULT_OK, intent);

        // 키보드 자동으로 치우기
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        finish();
    }

    // 액티비티 외부 터치시 동작 X
    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    // 취소 버튼 누를 때
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
