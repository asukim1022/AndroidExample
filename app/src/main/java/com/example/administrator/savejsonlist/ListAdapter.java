package com.example.administrator.savejsonlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *@ListAdapter
 *@brief MainActivity에있는 listView을 관리, listView에 ListData에서 저장한 데이터 출력하기
 *@date 2016.02.18
 */
public class ListAdapter extends ArrayAdapter<ListData> {

    /*
    adapter는 adapter인터페이스를 구현하는 클래스의 인스턴스이다.
    여기서는 ArrayAdapter<ListData>의 인스턴스를 사용할 것이다.
    ListData 타입의 객체를 저장하는 배열에 저장된 데이터를 처리
    ArrayAdapter<ListData>는 BaseAdapter 클래스로부터 상속받는다.
    화면에 보여줄 뷰 객체를 listView가 필요로 할 때는 자신의 어댑터와 소통한다.
    */
    ArrayList<ListData> listDatas;
    private Context mContext;

    /*
    context : 두번째 인자인 viewId의 사용에 필요한 context 객체이다.
    viewId : 뷰 객체를 생성하기 위해 ArrayAdapter가 사용할 레이아웃을 나타낸다.
    listDataArrayList : 객체들이 저장된 데이터
    */
    public ListAdapter(Context context, int viewId, ArrayList<ListData> listDataArrayList) {
        super(context, viewId, listDataArrayList);
        this.listDatas = listDataArrayList;
    }

    public void setListDatas(ArrayList<ListData> listDatas) {
        this.listDatas = listDatas;
    }

    public void setMainContext(Context context) {
        mContext = context;
    }

    /** @brief getCount
     *  @detail listView는 어댑터의 getCount() 메서드를 호출하여 배열에 저장된 객체가 몇개가 있는지 요청한다.
     */
    @Override
    public int getCount() {
        return listDatas.size();
    }

    @Override
    public ListData getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    /** @brief getView
     *  @param  position : listView가 찾을 리스트 항목의 위치
     *  @dtails getView의 구현 코드 내부에서 어댑터는 배열의 올바른 항목에 대한 뷰객체를 생성하고 그 뷰 객체를 listView에 반환한다.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //listView의 item을 출력할 layout 설정
            convertView = li.inflate(R.layout.list_item, null);
        }

        //출력할 ListData의 position값 설정
        ListData data = listDatas.get(position);

        //null 예외 처리
        if (null != data) {
            TextView nameText = (TextView) convertView.findViewById(R.id.nameText);

            //nameText에 data의 getmName값 출력하기
            nameText.setText(data.getmName());
        }
        return convertView;
    }
}