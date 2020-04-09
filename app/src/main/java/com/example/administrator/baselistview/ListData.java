package com.example.administrator.baselistview;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Administrator on 2016-02-18.
 */
public class ListData implements Serializable {
    private static final String JSON_ID = "id";
    private static final String JSON_NAME = "name";

    private UUID mId;
    private String mName;

    public ListData() {
        mId = UUID.randomUUID();
    }


    /** @brief ListData(String neme)
     *  @detail listDataArrayList에서 저장할 데이터 구조
     */
    ListData(String name) {
        mId = UUID.randomUUID();
        mName = name;
    }


    /** @brief ListData(JSONObject json)
     *  @detail 앱이 시작할때 파일 시스템으로부터 데이터를 ㄹ딩
     */
    public ListData(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mName = json.getString(JSON_NAME);
    }


    /** @brief JSONObject toJSON()
     *  @detail JSONObject 클래스의 메서드들을 사용해서 ListData의 데이터를 JSON 포맷으로 파일에 쓸 수 있게 변환
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_NAME, mName);
        return json;
    }

    public UUID getId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}


