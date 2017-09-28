package com.example.viet.readfile;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    Cities mCity;

    TextView txtView;
    TextView txtView2;
    TextView txtView3;
    TextView txtView4;

    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submitted = (EditText) findViewById(R.id.num);

        txtView = (TextView) findViewById(R.id.txt);
        txtView2 = (TextView) findViewById(R.id.txt2);
        txtView3 = (TextView) findViewById(R.id.txt3);
        txtView4 = (TextView) findViewById(R.id.txt4);

        mAdapter = new Adapter();
        ListView listNote = (ListView) findViewById(R.id.listView);
        listNote.setAdapter(mAdapter);

        convertStreamToString(getResources().openRawResource(R.raw.au_locations));

        for (int i = 0; i < locations.size(); i++) {
            String original = locations.get(i);
            String[] separated = original.split(",");
            Cities city = new Cities();
            String input = String.valueOf(i) + ". " + separated[0];
            city.setCity(input);
            createNewNote(city);
        }

        submitted.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    DataPopulate(Option());
                    return true;
                }
                return false;
            }
        });

        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int whichItem, long id) {
                //txtView.setText(String.valueOf(whichItem));
                DataPopulate(whichItem);
                submitted.setText("");
            }
        });
    }

    public void DataPopulate(int num) {
        if (num <= 100) {
            String original = locations.get(num);
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
    }

    public void createNewNote(Cities n) {
        mAdapter.addNote(n);
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

    public class Adapter extends BaseAdapter {
        List<Cities> cityList = new ArrayList<>();

        @Override
        public int getCount() {
            return cityList.size();
        }

        @Override
        public Cities getItem(int whichItem) {
            return cityList.get(whichItem);
        }

        @Override
        public long getItemId(int whichItem) {
            return whichItem;
        }

        @Override
        public View getView(int whichItem, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater intflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = intflater.inflate(R.layout.listitem, viewGroup, false);
            }

            TextView txtCity = view.findViewById(R.id.txtCity);
            Cities temp = cityList.get(whichItem);
            txtCity.setText(temp.getCity());
            return view;
        }

        public void addNote(Cities n) {
            cityList.add(n);
            notifyDataSetChanged();
        }
    }

}
