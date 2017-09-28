package com.example.viet.readfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> locations = new ArrayList<>();
    String num;
    EditText submitted;
    Button button;

    TextView txtView;
    TextView txtView2;
    TextView txtView3;
    TextView txtView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submitted = (EditText) findViewById(R.id.num);

        txtView = (TextView) findViewById(R.id.txt);
        txtView2 = (TextView) findViewById(R.id.txt2);
        txtView3 = (TextView) findViewById(R.id.txt3);
        txtView4 = (TextView) findViewById(R.id.txt4);

        convertStreamToString(getResources().openRawResource(R.raw.au_locations));

        submitted.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (Option() <= 100) {
                        String original = locations.get(Option());
                        String[] separated = original.split(",");
                        txtView.setText(separated[0]);
                        txtView2.setText(separated[1]);
                        txtView3.setText(separated[2]);
                        txtView4.setText(separated[3]);
                    } else {
                        txtView.setText("No Data");
                        txtView2.setText("");
                        txtView3.setText("");
                        txtView4.setText("");
                    }
                    return true;
                }
                return false;
            }
        });


    }

    public int Option() {
        int selected = 999;
        num = submitted.getText().toString();
        if (!num.matches("")) {
            selected = Integer.valueOf(num);
        }
        return selected;
    }

    public void convertStreamToString(InputStream is) {
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = reader.readLine()) != null) {
                locations.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
