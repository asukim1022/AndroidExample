package com.example.administrator.savejsonlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;

import java.util.ArrayList;

/**
 *@MainActivity
 *@brief listView생성, adapter생성, ArrayList제어, 데이터 저장/불러오기
 *@date 2016.02.18
 *@details
 */
public class MainActivity extends Activity {

    //intent전송 타입, onActivityResult에서 결과 구분하는 값
    //CALLER_REQUEST : 메모 처음 입력시
    private final int CALLER_REQUEST = 1;

    //REVISION_LIST : 기존 메모 수정시
    private final int REVISION_LIST = 2;

    //selectedList : 선택된 리스트 아이템
    int selectedList;

    //메모 저장하는 배열
    ArrayList<ListData> listDataArrayList;

    //메모 리스트를 출력하는 listView
    ListView listView;

    //listView 관리하는 adapter
    ListAdapter listAdapter;

    //메모 데이터 관리
    ListData listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //json에서 저장한 listDataArrayList 배열 불러오기
        try {
            listDataArrayList = ListLab.getListDatas(getApplicationContext()).getListDataArrayList();
            Toast.makeText(getApplicationContext(), listDataArrayList.size(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }

        listView = (ListView) findViewById(R.id.list);

        /*
        adapter 생성후 layout이랑 배열 연결
        adapter : adapter 설정, MyListViewAdapter에 layout.item와 myList 연결
        */
        listAdapter = new ListAdapter(getApplicationContext(), R.layout.list_item, listDataArrayList);
        listAdapter.setMainContext(this);
        listView.setAdapter(listAdapter);

        //listView item long 클릭시 onMenuDelete() 실행
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //클릭한 해당 position 삭제하기
                onMenuDelete(position);
                return true;
            }
        });

        //setOnItemClickListener : 리스트 아이템 클릭시 발생하는 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //클릭시 SubActivity로 intent이동
                Intent intent = new Intent(MainActivity.this, SubActivity.class);

                /*
                intent로 데이터 전송하여 이동
                SubActivity에서 받을 users key값으로 현재 myList의 position 데이터 전송
                */
                intent.putExtra("users", listDataArrayList.get(position));

                /*
                selectedList : selectedList변수에 현재 클릭된 position값 저장
                onActivityResult : onActivityResult시에 변경사항 수정시 사용
                */
                selectedList = position;

                /*
                startActivityForResult
                Activity의 결과를 받으려면 호출할 때 startActivity() 대신 startActivityForResult() 메소드를 사용
                인수를 하나 추가(REVISION_LIST).
                이 인수(REVISION_LIST)는 0보다 크거나 같은 integer 값으로 추후 onActivityResult() 메소드에도 동일한 값이 전달되며
                이를 통해 하나의 onActivityResult() 메소드에서 (만약 있다면) 여러 개의 startActivityForResult()를 구분.
                REVISION_LIST : 기존 리스트 아이템 클릭시에
                */
                startActivityForResult(intent, REVISION_LIST);
            }
        });

        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        //floatingActionButton : 메모 추가하기 버튼, SubActivity로 이동
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);

                /*
                startActivityForResult
                Activity의 결과를 받으려면 호출할 때 startActivity() 대신 startActivityForResult() 메소드를 사용
                인수를 하나 추가(CALLER_REQUEST).
                이 인수(CALLER_REQUEST)는 0보다 크거나 같은 integer 값으로 추후 onActivityResult() 메소드에도 동일한 값이 전달되며
                이를 통해 하나의 onActivityResult() 메소드에서 (만약 있다면) 여러 개의 startActivityForResult()를 구분.
                CALLER_REQUEST : 새로 리스트 추가 접근시에
                */
                startActivityForResult(intent, CALLER_REQUEST);
            }
        });
    }


    /** @brief onActivityResult
     *  @date 2016-02-18
     *  @param requestCode : startActivityForResult()의 두번째 인수 값
     *         resultCode : 호출된 Activity에서 설정한 성공(RESULT_OK)/실패(RESULT_CANCEL) 값
     *         intent : 넘겨온 intent 값
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        //requestCode : requestCode가 CALLER_REQUEST일 경우, 새로운 데이터를 입력하는 경우
        if (requestCode == CALLER_REQUEST) {

            //resultCode : RESULT_OK 결과 요청이 성공적일 경우
            if (resultCode == RESULT_OK) {

                //getListDatas("KEY") 값으로 해당 값 변수에 저장하기
                String name = intent.getExtras().get("name").toString();

                //KEY값으로 받아서 저장된 변수 listData에 저장
                listData = new ListData(name);

                //myList에 listData에 저장
                listDataArrayList.add(listData);

                //adapter에 리스트 갱신
                listAdapter.notifyDataSetChanged();
            }
        } else {
            //requestCode : requestCode가 REVISION_LIST일 경우, 기존의 데이터를 수정하는 경우

            //resultCode : RESULT_OK 결과 요청이 성공적일 경우
            if (resultCode == RESULT_OK) {

                //getListDatas("KEY") 값으로 해당 값 변수에 저장하기
                String name = intent.getExtras().get("name").toString();

                /*
                기존의 데이터를 수정하기 위해서
                selectedList : 사용자가 선택한 아이템의 position
                selectedList의 position의 데이터를 수정해야함 = myList.getListDatas(selectedList);
                */
                listData = listDataArrayList.get(selectedList);

                //해당 배열의 위치에 수정한 데이터를 set해준다.
                listData.setmName(name);

                //adapter에 리스트 갱신
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionDelete:
                listDataArrayList.clear();
                listAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /** @brief onMenuDelete
     *  @date 2020-03-16
     *  @param position : 삭제할 listView의 position
     *  @detail 메모전체삭제 다이얼로그
     */
    private void onMenuDelete(final int position) {
        AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setMessage("delete");
        ad.setButton(AlertDialog.BUTTON_POSITIVE, "yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                //listDataArrayList에서 삭제할 position선택
                listDataArrayList.remove(position);

                //리스트 갱신
                listAdapter.notifyDataSetChanged();
            }
        });
        ad.setButton(AlertDialog.BUTTON_NEGATIVE, "no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        ad.show();
    }

    @Override
    public void onPause() {
        super.onPause();

        //onStop(), onDestroy()가 호출될 때까지 기다린다면 데이터를 저장할 기회를 놓칠 수 있으므로 onPause()에서 호출한다.
        ListLab.getListDatas(getApplicationContext()).saveListData();
    }
}