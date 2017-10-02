package com.example.mikeev.villanue_countbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;


/*
First Activity
Shows the summary list of the entire CountBook where a user can add or modify a counter to the list.
Credit: Functions: loadInFile() and saveInFile() taken from lonelytwitter
 */

public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "file.sav";

    private ArrayList<Counter> counters = new ArrayList<Counter>();
    private ListView CL;
    private ArrayAdapter<Counter> adapter;

    Counter editCounter;
    int editPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CL = (ListView) findViewById(R.id.counterList);

        adapter = new ArrayAdapter<Counter>(this, android.R.layout.simple_list_item_1, counters);
        CL.setAdapter(adapter);

        //"Grabs" data and trasfer it to second activity to be modified or updated.
        CL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(MainActivity.this, AddModifyActivity.class);
                //Get the value of the item you clicked
                Counter counter = counters.get(position);
                editCounter = counter;
                editPosition = position;
                intent.putExtra("My counter", counter);
                startActivityForResult(intent, 1);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();
        adapter = new ArrayAdapter<Counter>(this, android.R.layout.simple_list_item_1, counters);
        CL.setAdapter(adapter);
    }

    /*
    * Adds a default counter when the "Add" Button is pressed, which is then modified by the user
     */

    public void addCounter(View view) {
        Intent intent = new Intent(this, AddModifyActivity.class);
        counters.add(new Counter());
        adapter.notifyDataSetChanged();
        CL = (ListView) findViewById(R.id.counterList);
        Counter counter = counters.get(counters.size() - 1);
        editPosition = (counters.size() - 1);
        editCounter = counter;
        intent.putExtra("My counter", counter);
        startActivityForResult(intent, 1);

    }

    /*
    "Grabs" data values from the AddModify ACtivity class to be used to update the list view in this activity
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {

                String eData = data.getStringExtra("Edit Counter");
                System.out.println(eData);
                if(eData.isEmpty()) {
                    counters.remove(editPosition);
                    CL = (ListView) findViewById(R.id.counterList);

                    adapter = new ArrayAdapter<Counter>(this, android.R.layout.simple_list_item_1, counters);
                    CL.setAdapter(adapter);
                    saveInFile();

                }
                else {
                    String lines[] = eData.split("\\r?\\n");
                    editCounter.setName(lines[0]);
                    editCounter.setCurrValue(Integer.parseInt(lines[1]));
                    editCounter.setInitValue(Integer.parseInt(lines[2]));
                    editCounter.setDate(lines[3]);
                    editCounter.setComment(lines[4]);

                    counters.set(editPosition, editCounter);
                    CL = (ListView) findViewById(R.id.counterList);

                    adapter = new ArrayAdapter<Counter>(this, android.R.layout.simple_list_item_1, counters);
                    CL.setAdapter(adapter);
                    saveInFile();
                }

            }
        }
    }

    //added dependency at gradle build (Gson)
    /*
    At start, loads the saved file (if any) and inputs the data into the listview.
     */

    private void loadFromFile() {
        try{
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Counter>>() {}.getType();
            counters = gson.fromJson(in,listType);
        }
        catch(FileNotFoundException e) {
            counters = new ArrayList<Counter>();
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Saves new data (if any) to a file as data.
     */
    private void saveInFile(){
        try{
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(counters, writer);
            writer.flush();

            fos.close();
        }
        catch(FileNotFoundException e) {
            throw new RuntimeException();
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

}

