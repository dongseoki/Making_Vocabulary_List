package com.example.wordlike;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WordsListItem extends AppCompatActivity {
    private RecyclerAdapter adapter;
    private TextView Extraview;
    String json;
    String urll = "http://ec2-13-209-19-111.ap-northeast-2.compute.amazonaws.com/";
    String selectWord = "select_Word.php"; // var 1~2 등록번호, title
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list_item);
        Extraview.findViewById(R.id.extraView);
String getTitle = getIntent().getStringExtra("Title");
        String getmembershipNumber = getIntent().getStringExtra("membershipNumber");

        PhpConn task = new PhpConn();
        try {
            json = task.execute(urll + selectWord +"?var1="+getTitle+"&var2="+getmembershipNumber).get();
            getResult(json);
        }
        catch(Exception e){
            System.out.println(e);
        }

    }
    private void getResult(String json) { // type == 0 그냥 쿼리문 // 1: list 반환

        try {
            JSONObject jsonObject = new JSONObject(json);
            String wordarrayDB = jsonObject.getString("WordlistDB");
            JSONArray jsonArray = new JSONArray(wordarrayDB);
            for (int i = 0; i < 5; i++) {
                JSONObject subJsonObject = jsonArray.getJSONObject(i);
                String spel = subJsonObject.getString("wordSpelling");
                String mean = subJsonObject.getString("wordMeaning");
                String sent = subJsonObject.getString("wordSentence");
                Extraview.setText(spel+'\n');
                Extraview.setText(mean+'\n');
                Extraview.setText(sent+"\n\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class PhpConn extends AsyncTask<String,String,String>{
        String output;
        @Override
        protected String doInBackground(String... params) {
            output = "";
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
                        br.close();
                        conn.disconnect();
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
