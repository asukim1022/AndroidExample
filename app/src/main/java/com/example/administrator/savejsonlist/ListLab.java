package com.example.administrator.savejsonlist;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/** @brief ListLab()
 *  @detail ListData 객체들을 저장하는 집중화된 데이터 저장소
 *          컨트롤러 클래스들과 ListLab이 상호 동작하여 모델 계층의 데이터를 액세스할 것이다
 *          싱글톤 클래스
 *          ListLab.java에서 private 생성자와 getListDatas(context) 메서드를 갖는 싱글톤 클래스를 구현한다.
 */
public class ListLab {
    private static final String TAG = "ListLab";
    private static final String FILENAME = "alarm.json";
    private ArrayList<ListData> listDataArrayList;
    private ListJSON mSerializer;

    //static의 변수의 접두사로 s를 사용
    private static ListLab sListLab;

    /*
    액티비티를 시작하고, 프로젝트 리소스를 액세스하며, 우리 애플리케이션의 private 스토리지를 찾는 등의 일을 한다.
    context 매개변수는 직접 생성자에 전달하는 것이 아니고 getListDatas(context)에 전달한다.
    */
    private Context mAppContext;

    private ListLab(Context appContext) {
        mAppContext = appContext;

        //ListLab의 생성자에서는 이 데이터들을 ArrayList에 로딩
        //만약 데이터가 하나도 없으면 새로운 ArralList를 생성한다.
        mSerializer = new ListJSON(mAppContext, FILENAME);
        try {
            listDataArrayList = mSerializer.loadListData();
            Log.d(TAG, "" + String.valueOf(listDataArrayList.size()));
            Log.e(TAG, "" + listDataArrayList);
        } catch (Exception e) {

            //ListData 객체를 저장하는 빈 ArrayList 객체를 생성
            listDataArrayList = new ArrayList<ListData>();
            Log.d(TAG, "new load");
        }
    }


    /** @brief getListDatas(Context c)
     *  @detail 생성된 ArrayList 객체를 반환하는 getListData() 메서드
     */
    public static ListLab getListDatas(Context c) {
        if (sListLab == null) sListLab = new ListLab(c.getApplicationContext());
        return sListLab;
    }


    /** @brief getListData(UUID id)
     *  @detail 주어진 id를 갖는 ListData 객체를 반환하는 getListData(UUID)
     *          UUID : 128비트의 고유한 값으로서, 여기서는 ListData 객체들을 고유하게 식별하기 위해 사용한 것
     */
    public ListData getListData(UUID id) {
        for (ListData c : listDataArrayList) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }

    public ArrayList<ListData> getListDataArrayList() {
        return listDataArrayList;
    }


    public boolean saveListData() {
        try {
            mSerializer.saveData(listDataArrayList);
            Log.d(TAG, "alarm saved to file / " + listDataArrayList.size());
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving alarm: " + e);
            return false;
        }
    }
}