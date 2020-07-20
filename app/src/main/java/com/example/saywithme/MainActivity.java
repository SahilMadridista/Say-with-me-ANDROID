package com.example.saywithme;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

   ImageView Speak,Listen;
   TextView Words;
   private static final int REQUEST_CODE = 100;
   private TextToSpeech textToSpeech;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      Speak = findViewById(R.id.say);
      Listen = findViewById(R.id.listen);

      Words = findViewById(R.id.words);

      Speak.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Say();
         }
      });

      Listen.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Hear();
         }
      });

      textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
         @Override
         public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
               int result = textToSpeech.setLanguage(Locale.getDefault());
               if (result == TextToSpeech.LANG_MISSING_DATA
                       || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                  Log.e("TTS", "Language not supported");
               }
            } else {
               Log.e("TTS", "Initialization failed");
            }
         }
      });

   }

   private void Say() {

      Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

      try {
         startActivityForResult(intent, REQUEST_CODE);
      } catch (ActivityNotFoundException a) {
         Toast.makeText(getApplicationContext(),"Not found",Toast.LENGTH_SHORT).show();
      }

   }

   private void Hear() {

      String text = Words.getText().toString().trim();
      textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);

   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      switch (requestCode) {
         case REQUEST_CODE: {
            if (resultCode == RESULT_OK && null != data) {
               ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
               assert result != null;
               Words.setText(result.get(0));
            }
            break;
         }

      }

      Listen.setVisibility(View.VISIBLE);

   }

   @Override
   protected void onDestroy() {
      if (textToSpeech != null) {
         textToSpeech.stop();
         textToSpeech.shutdown();
      }
      super.onDestroy();
   }

}