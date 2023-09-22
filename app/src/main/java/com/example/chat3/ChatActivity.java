package com.example.chat3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chat3.model.ChatDTO;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private String CHAT_NAME;
    private String USER_NAME;

    private ListView chat_view;
    private EditText chat_edit;
    private Button chat_send;

    RadioGroup radioGroup;

    String text;
    String translatedText;

    String target;
    String ApiUrl = "https://openapi.naver.com/v1/papago/n2mt";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        radioGroup = findViewById(R.id.radioGroup);
        // 위젯 ID 참조
        chat_view = (ListView) findViewById(R.id.chat_view);
        chat_edit = (EditText) findViewById(R.id.chat_edit);
        chat_send = (Button) findViewById(R.id.chat_sent);

        // 로그인 화면에서 받아온 채팅방 이름, 유저 이름 저장
        Intent intent = getIntent();
        CHAT_NAME = intent.getStringExtra("chatName");
        USER_NAME = intent.getStringExtra("userName");

        // 채팅 방 입장
        openChat(CHAT_NAME);

        // 메시지 전송 버튼에 대한 클릭 리스너 지정
        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat_edit.getText().toString().equals(""))
                    return;

                // 1. 어떤 언어로 번역할지에 대한 정보를 가져온다.
                // 눌려진 라디오 버튼 정보를 가져온다.
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                if (radioButtonId == R.id.btnEng) {
                    target = "en";
                } else if (radioButtonId == R.id.btnCn) {
                    target = "zh-CN";
                } else if (radioButtonId == R.id.btnJp) {
                    target = "ja";
                }else {
                    Snackbar.make(chat_send, "번역할 언어를 꼭 선택하세요", Snackbar.LENGTH_SHORT);
                    return; // 이상한 경우니까 밑의 코드로 바로 안내려가게 써준다
                }
                // 2. 에디트텍스트에, 유저가 작성한 글을 가져온다
                text = chat_edit.getText().toString().trim();
                // 3. 파파고 API를 호출한다. 그 결과를 화면에 보여준다.

                String source = "ko";


                RequestQueue queue = Volley.newRequestQueue(ChatActivity.this);
                JSONObject body = new JSONObject();
                try {
                    body.put("source", source);
                    body.put("target", target);
                    body.put("text", text);
                } catch (JSONException e) {
                    return;
                }
                StringRequest request = new StringRequest(Request.Method.POST, ApiUrl, response -> {
                    try {
                        JSONObject result = new JSONObject(response);
                        translatedText = result.getJSONObject("message").getJSONObject("result").getString("translatedText");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {


                }) {
                    // 헤더부분
                    // Volley에서 헤더에 데이터를 셋팅하고 싶으면, 요렇게 한다
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>(); // 해쉬맵은 해쉬테이블과 비슷하다
                        params.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");  // 키와 밸류
                        params.put("X-Naver-Client-Id", Config.NAVER_API_KEY); // 자신의 API 키 입력
                        params.put("X-Naver-Client-Secret", Config.NAVER_SECRET_KEY); // 자신의 API 시크릿 키 입력
                        return params;

                    }

                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("source", source); // 원본 언어
                        params.put("target", target); // 목적 언어
                        params.put("text", text); // 번역할 텍스트
                        return params;
                    }
                };
                queue.add(request);

                ChatDTO chat = new ChatDTO(USER_NAME, translatedText); //ChatDTO를 이용하여 데이터를 묶는다.
                databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat); // 데이터 푸쉬
                chat_edit.setText(""); //입력창 초기화

            }

        });
    }

    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.add(chatDTO.getUserName() + " : " + chatDTO.getMessage());
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.remove(chatDTO.getUserName() + " : " + chatDTO.getMessage());
    }

    private void openChat(String chatName) {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_view.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, adapter);
                Log.e("LOG", "s:"+s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}