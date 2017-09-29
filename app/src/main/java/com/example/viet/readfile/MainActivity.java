package com.example.viet.readfile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> locations = new ArrayList<>();
    String num;
    ClearableEditText submitted;
    TextView txtView;
    TextView txtView2;
    TextView txtView3;
    TextView txtView4;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submitted = (ClearableEditText) findViewById(R.id.num);
        txtView = (TextView) findViewById(R.id.txt);
        txtView2 = (TextView) findViewById(R.id.txt2);
        txtView3 = (TextView) findViewById(R.id.txt3);
        txtView4 = (TextView) findViewById(R.id.txt4);

        File f = getFileStreamPath("added_locations.txt");
        if (f.length() == 0) {
            convertStreamToString(getResources().openRawResource(R.raw.au_locations));
        }

        loadLocations(f);

        mAdapter = new Adapter();
        ListView listNote = (ListView) findViewById(R.id.listView);
        listNote.setAdapter(mAdapter);

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
                    if (Option() < locations.size()) {
                        DataPopulate(Option());
                        return true;
                    } else {
                        noData();
                    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            DialogNewLocation dialog = new DialogNewLocation();
            dialog.show(getFragmentManager(), "");
            return true;
        } else {
            return false;
        }
    }

    public void DataPopulate(int num) {
        String original = locations.get(num);
        String[] separated = original.split(",");
        txtView.setText(separated[0]);
        txtView2.setText(separated[1]);
        txtView3.setText(separated[2]);
        txtView4.setText(separated[3]);
    }

    public void noData() {
        txtView.setText("No Data");
        txtView2.setText("");
        txtView3.setText("");
        txtView4.setText("");
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
                file_append(line+"\n");
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

    public void loadLocations(File file) {
        String strLine;
        try {
            FileInputStream fis = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((strLine = br.readLine()) != null) {
                locations.add(strLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void file_append(String location) {
        FileOutputStream outputStream;
        String filename = "added_locations.txt";
        try {
            outputStream = openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(location.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
        */
        Intent intent = new Intent(getIntent());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
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
