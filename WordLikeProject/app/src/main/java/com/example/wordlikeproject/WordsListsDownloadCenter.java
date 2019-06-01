package com.example.wordlikeproject;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WordsListsDownloadCenter extends AppCompatActivity {

    private RecyclerAdapter3 adapter;
    String urll = "http://ec2-13-209-19-111.ap-northeast-2.compute.amazonaws.com/";
    String selectWordList = "select_WordsList.php"; // 제목또는 작성자 이름
    String selectWord = "select_Word.php"; // var 1~2 등록번호, title
    String deleteWordList = "delete_WordsList.php"; // var 1+2 작성자이름, 제목
    String insertWordList = "insert_WordsList.php";//  var 1~2까지 넣고, 각각 멤버 번호, 제목
    String insertWord = "select_Word.php"; // var 1~4까지 넣고 , 각각 list번호, 스펠링, 뜻, 문장

    String json = "";
    SearchView searchButton;
    RecyclerView recyclerView;

    private LinearLayout mLayout;
    int selected = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_words_lists_download_center);
        searchButton = findViewById(R.id.searchButton);
        mLayout = (LinearLayout) findViewById(R.id.OnlienWordListItem);
        init();

        PhpConn task = new PhpConn();
        try {
            json = task.execute(urll + selectWordList).get();
            getResult(json, 1); // type 1 : list 받아오기
        }
        catch(Exception e){
            System.out.println(e);
        }

        adapter.notifyItemRangeInserted(0, adapter.getItemCount());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getApplicationContext(), adapter.getItemTitle(position)+"이 선택됨.", Toast.LENGTH_SHORT).show();
                        selected = position;
                        mLayout.setBackgroundColor(0xF0D0DBAF);


                    }
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getApplicationContext(), adapter.getItemTitle(position)+"이 선택됨.", Toast.LENGTH_SHORT).show();
                        selected = position;
                    }
                }));

        //SearchView의 검색 이벤트
        searchButton.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                onSearchButtonClicekd();
                searchButton.setQuery("",false);
                searchButton.clearFocus();
                return true;
            }

            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
//                TextView text = (TextView)findViewById(R.id.txtsearch);
//                text.setText("검색식 : "+newText);

                return true;
            }
        });

    }
    public void onDownButtonClicked(View view) {
        if(selected == -1){
            Toast.makeText(getApplicationContext(), "아무것도 선택되지 않음", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), adapter.getItemTitle(selected)+"가 다운됩니다.", Toast.LENGTH_SHORT).show();
            selected = -1;
        }
    }
    public void onDeleteButtonClicked(View view)
    {

        if(selected == -1){
            Toast.makeText(getApplicationContext(), "아무것도 선택되지 않음", Toast.LENGTH_SHORT).show();
        }
        else {
            System.out.println(urll+deleteWordList+"?var1=" + adapter.getItemTitle(selected) + "&var2=" + adapter.getItemAuth(selected));
            adapter.deleteItem(selected);

            PhpConn task = new PhpConn();
            task.execute(urll+selectWordList+"?var1=" + searchButton.getQuery());


            adapter.notifyItemRemoved(selected);
            selected = -1;
        }
    }
    public void onSearchButtonClicekd()
    {
        if(selected == -1){
            Toast.makeText(getApplicationContext(), "아무것도 선택되지 않음", Toast.LENGTH_SHORT).show();
        }
        else {
            int tmp = adapter.getItemCount();
            adapter.deleteAllItem();
            adapter.notifyItemRangeRemoved(0, tmp);
            PhpConn task = new PhpConn();
            try {
                json = task.execute(urll + selectWordList + "?var1=" + searchButton.getQuery()).get();
                getResult(json, 1); // 1 type, 리스트
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void onPeepButtonClicked(View view){
//        PhpConn task = new PhpConn();
//        try {
//            json = task.execute(urll + selectWord + "?var1=" + adapter.getItemTitle(selected) + "&var2=" + adapter.getItemAuth(selected)).get();
//            getResult(json, 2); // 2 type, 단어
//        } catch (Exception e) {
//            System.out.println(e);
//        }
        Intent intent=new Intent(WordsListsDownloadCenter.this, PreviewPopUpWord.class);
        intent.putExtra("Title", adapter.getItemTitle(selected));
        intent.putExtra("membershipNumber", adapter.getItemAuth(selected));

        startActivity(intent);
    }



    private void init() {
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter3();
        recyclerView.setAdapter(adapter);
    }

    private void getResult(String json, int type) { // type == 0 그냥 쿼리문 // 1: list 반환,    type == 2, word 반환

        try {
            JSONObject jsonObject = new JSONObject(json);
            String wordarrayDB = jsonObject.getString("WordlistDB");
            JSONArray jsonArray = new JSONArray(wordarrayDB);

            if(type == 1) {

                for (int i = 1; i < jsonArray.length(); i++) {
                    JSONObject subJsonObject = jsonArray.getJSONObject(i);
                    String num = subJsonObject.getString("membershipNumber");
                    String title = subJsonObject.getString("Title");

                    Data data = new Data();
                    data.setTitle(title);
                    data.setAuth(num);
                    adapter.addItem(data);
                }

            }
            else if (type == 2){
                for (int i = 0; i < 5; i++) {
                    JSONObject subJsonObject = jsonArray.getJSONObject(i);
                    String spel = subJsonObject.getString("wordSpelling");
                    String mean = subJsonObject.getString("wordMeaning");

                    Toast.makeText(getApplicationContext(), spel + ": " + mean, Toast.LENGTH_SHORT).show();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class PhpConn extends AsyncTask<String,String,String> {
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



