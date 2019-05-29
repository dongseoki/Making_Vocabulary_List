package com.example.wordlike;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WordListsDownload extends AppCompatActivity {

    private RecyclerAdapter adapter;
    String urll = "http://ec2-13-209-19-111.ap-northeast-2.compute.amazonaws.com/select_WordsList.php";
    TextView searchEdit;
    private static String TAG = "phptest";
    String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_lists_download_center);
        searchEdit = (TextView) findViewById(R.id.searchEdit);
        init();

        PhpTest task = new PhpTest();
        task.execute(urll);

    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getResult(String json) {

        String TAG_JSON = "Wordlists";
        String TAG_ID = "membershipNumber";
        String TAG_TITLE = "Title";
        try {
            JSONObject jsonObject = new JSONObject(json);
            String wordarrayDB = jsonObject.getString("WordlistDB");
            JSONArray jsonArray = new JSONArray(wordarrayDB);
            for (int i=0; i < jsonArray.length(); i++) {
                JSONObject subJsonObject = jsonArray.getJSONObject(i);
                String num = subJsonObject.getString("membershipNumber");
                String title = subJsonObject.getString("Title");

                Data data = new Data();
                data.setTitle(title);
                data.setAuth(num);
                adapter.addItem(data);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class PhpTest extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            String output = "";
            try {
                //연결 url 설정
                URL url = new URL(params[0]);

                //커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                //연결되었으면
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    //연결된 코드가 리턴되면
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        int i = 0 ;
                        for(;;){
                            //웹상에 보이는 텍스트를 라인단위로 읽어 저장
                            String line = br.readLine();
                            if(line == null)
                                break;
                            i++;
                            output += line;
                        }
                        getResult(output);
                        br.close();
                    }
                    conn.disconnect();
                }else{
                    System.out.println("실패ㅡㅡ");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return output;
        }
    }
}



