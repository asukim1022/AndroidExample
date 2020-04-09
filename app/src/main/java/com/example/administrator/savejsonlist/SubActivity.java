package com.example.administrator.savejsonlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 *@SubActivity
 *@brief 내용 추가/수정, MainActivity에 데이터 전송
 *@date 2016.02.18
 */
public class SubActivity extends Activity {

    EditText editText;
    Button okBtn, cancelBtn;
    ListData listData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        editText = (EditText) findViewById(R.id.editText);
        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        //MainActivity의 ArrayList에 저장한 intnent 데이터 불러오기, 기존의 데이터 불러오기
        Intent intent = new Intent(getIntent());

        //key값을 통해서 데이터 가져오기(getIntent)
        listData = (ListData) getIntent().getSerializableExtra("users");

        //listData가 null이 아닐때만(null 예외처리 꼭 해줘야함)
        if (listData != null) {

            //이전에 저장한 데이터 editText에 입력
            editText.setText(listData.getmName());
        }

        //okBtn : 클릭시 saveClickListener()함수 실행, 데이터 저장
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClickListener();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /** @brief saveClickListener
     *  @date 2016-02-18
     *  @detail intent를 통해서 데이터 MainActivity에 보내기(putExtra)
     */
    public void saveClickListener() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", editText.getText());
        setResult(RESULT_OK, intent);
        finish();
    }
}