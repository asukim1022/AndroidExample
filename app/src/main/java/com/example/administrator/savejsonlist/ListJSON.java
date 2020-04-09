package com.example.administrator.savejsonlist;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016-02-18.
 */
public class ListJSON {
    private Context mContext;
    private String mFilename;
    private static final String TAG = "ListLab";

    public ListJSON(Context context, String filname) {
        mContext = context;
        mFilename = filname;
    }


    /** @brief loadListData()
     *  @detail 파일 시스템으로부터 데이터를 읽기 위해 Context 메서드인 openFileInput()을 사용한다.
     *          그리고 읽은 JSONObject 문장를 파싱항 JSONArray로 저장한 후 다시 ListData 객체 타입으로 ArrayList에 저장하고 반환
     */
    public ArrayList<ListData> loadListData() throws IOException, JSONException {
        ArrayList<ListData> alarm = new ArrayList<ListData>();
        BufferedReader reader = null;
        try {

            /*
            InputStream : 프로그램이 데이터를 입력받을 때, 바이트 단위로 데이터를 읽음, 외부로부터 읽어 들이는 기능
            openFileInput : 입출력 스트림을 사용, openFileInput()을 사용하여 안드로이드 내부 저장장치 에 파일을 생성하여 쓰고 읽기
            /파일을 열고 데이터를 익어서 StringBuilder 객체로 만듬
            */
            InputStream in = mContext.openFileInput(mFilename);

            //BufferedReader : 입력된 데이터가 바로 전달되지 않고 중간에 버퍼링이 된 후에 전달
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            //JSONTokener 객체를 사용해서 JSON 객체를 파싱한다.
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                alarm.add(new ListData(array.getJSONObject(i)));
                Log.e("JSON", "" + array);
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "file not found");
            //이 예외는 무시한다. 이 앱의 최초 사용 시 데이터가 없어서 발생하지만 무시해도 된다.
        } finally {
            if (reader != null) {

                //finally 블록은 예외가 있던 말던 항상 실행된다.
                //처리 도중에 예외가 생기더라도 파일 핸들을 정상적으로 해제시키기 위함.
                reader.close();
            }
        }
        return alarm;
    }


    /** @brief saveData(ArrayList<ListData> arrayList)
     *  @detail saveData(ArrayList<ListData> arrayList)에서는 JSONArray 객체를 생성한다.
     *          그 다음에 ArrayList에 저장된 각 ListData 객체에 대해 toJSON() 메서드를 호출하여 JSON 포맷으로 변환 후 JSONArray에 추가
     *          ListData 객체들을 직렬화한 후 성공했음을 나타내는 boolean값을 반환한다.
     *          파일에 저장이 잘 되었는지 확인할 수 있는 로그 메서드 추가
     */
    public void saveData(ArrayList<ListData> arrayList) throws JSONException, IOException {
        Log.d(TAG, "alarm saved to file / " + arrayList.size());

        //JSON 객체가 저장되는 배열을 생성한다.
        JSONArray array = new JSONArray();
        for (ListData data : arrayList) {
            array.put(data.toJSON());
        }
        Log.e("JSON", "" + array);
        Writer writer = null;
        try {

            //OutputStream : 바이트 기반 출력 스트림의 최상위 클래스, 외부로 데이터를 전송
            //mContext.openFileOutput : 파일을 열고 데이터를 쓰기 위해 사용, 이때 메서드는 파일 이름과 모드를 인자로 받는다.
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);

            //파일을 연 후에는 표준 자바 I/O 클래스들인 Writer, OutputStream, OutputStreamWriter를 사용하여 데이터를 사용
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
            Log.d(TAG, "file output");
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}