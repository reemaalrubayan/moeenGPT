package com.example.moeen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<com.example.moeen.Message> messageList;
    MessageAdapter messageAdapter;
    boolean isChatAnsered=false;


    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("مُعينGPT                                                               ");
        setContentView(R.layout.activity_main);
        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v)->{

            String question = messageEditText.getText().toString().trim();
            addToChat(question, Message.SENT_BY_ME);
            messageEditText.setText("");
            callAPI(question);
            welcomeTextView.setVisibility(View.GONE);

        });

   }

        void addToChat(String message, String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message , sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });


        }

        void addResponse(String response){
        addToChat(response, Message.SENT_BY_BOT);
        }

        void callAPI(String question){
            JSONObject jsonBody = new JSONObject();
            JSONObject messages = new JSONObject();

            JSONArray messageArray = new JSONArray();

            String input2 = question + ( isChatAnsered?"":"only answer health related questions in arabic. If other types of questions are asked, respond with I don't know, act as a doctor always");

            try {
                jsonBody.put("model","gpt-3.5-turbo");
                messages.put("role","user");
                messages.put("content",input2);
                messageArray.put(messages);
                jsonBody.put("messages",messageArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
            Request request = new Request.Builder()
                    .url("https://experimental.willow.vectara.io/v1/chat/completions")
                    .header("x-api-key", "")
                    .header("customer-id", "")
                    .header("Content-Type", "")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
              //      addResponse("Failed to load message"+ e.getMessage());

                    Log.e("ERROR LOG ",e.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(MainActivity.this, "Problem connecting to our AI agent, please try again", Toast.LENGTH_LONG).show();

                        }
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.e("Success log ",response.toString());
                    String input1 = question;

                    if(response.isSuccessful()){
                        JSONObject jsonObject = null;
                        JSONObject jsonObjectContent = null;
                        try {

                            jsonObject = new JSONObject(response.body().string());
                            JSONArray jsonArray = jsonObject.getJSONArray( "choices");
                            String result = jsonArray.getJSONObject(0).getString("message");
                            Log.e("Success log ",result.toString());
                            //System.out.println(result.contains("نعم")+"88888888888888888");

                           if(result.contains("yes")) {
                               //System.out.println(result.contains("نعم")+"99999999999999");

                                //addToChat(input1, Message.SENT_BY_ME);
                               if(!isChatAnsered)
                               {
                                   callAPI(input1);
                                   isChatAnsered=true;
                               }



                               String result2 = result;



                           } else {
                              // if(!isChatAnsered)
                               // addToChat("" , Message.SENT_BY_BOT);
                        }


                            jsonObjectContent = new JSONObject(result);

                            addResponse(jsonObjectContent.getString("content"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }else{
                        addResponse("Failed to load message"+ response.body().toString());
                    }

                }
            });


        }

    }
