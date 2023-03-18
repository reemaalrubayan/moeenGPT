package com.example.moeen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class SecondPage extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_second_page);

            RadioButton haveDises=findViewById(R.id.radioButton6);
            RadioButton noDises=findViewById(R.id.radioButton7);

            RadioButton haveAllergy=findViewById(R.id.radioButton3);
            RadioButton noAllergy=findViewById(R.id.radioButton4);

            TextView disessMessage=findViewById(R.id.textView13);
            TextView allergyMessage=findViewById(R.id.textView);
            Button signup = findViewById(R.id.button);
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SecondPage.this, MainActivity.class);
                    startActivity(intent);
                }
            });



            haveDises.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    disessMessage.setText("*الرجاء مراجعة طبيب مختص");
                }
            });

            noDises.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    disessMessage.setText("");
                }
            });

            haveAllergy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allergyMessage.setText("*الرجاء مراجعة طبيب مختص");
                }
            });


            noAllergy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allergyMessage.setText("");
                }
            });

        }
    }