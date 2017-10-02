package com.example.mikeev.villanue_countbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/*
Second Activity
Shows the values of a counter or a new counter that can be modified and then transfer the data back to the first activity
 */
public class AddModifyActivity extends AppCompatActivity {
    private Counter counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modify);

        //Grabs the passed data to this activity
        Intent intent = getIntent();
        counter = (Counter)intent.getSerializableExtra("My counter");


        //Shows all the current values to its respective positions
        TextView date = (TextView) findViewById(R.id.date);
        date.setText(counter.getDate());

        TextView counterName = (TextView) findViewById(R.id.counterName);
        counterName.setText(counter.getName());

        TextView currValue = (TextView) findViewById(R.id.currValue);
        currValue.setText(String.valueOf(counter.getCurrValue()));

        TextView initValue = (TextView) findViewById(R.id.initValue);
        initValue.setText(String.valueOf(counter.getInitValue()));

        TextView comment = (TextView) findViewById(R.id.comment);
        comment.setText(counter.getComment());


    }

    /*
    WHen the increment button is pressed, the current value is increased by +1
     */
    public void incCounter(View view) {

        EditText currValue = (EditText) findViewById(R.id.currValue);
        String value = currValue.getText().toString();
        counter.setCurrValue(Integer.parseInt(value) + 1);
        currValue.setText(String.valueOf(counter.getCurrValue()));
    }

    /*
    When the decrement button is pressed, the current value is decreased by 1
    The current value cannot be negative
     */
    public void decCounter(View view) {

        EditText currValue = (EditText) findViewById(R.id.currValue);
        String value = currValue.getText().toString();
        int intValue = Integer.parseInt(value);
        if(intValue < 1) {
            return;
        }
        else {
            counter.setCurrValue(intValue - 1);
            currValue.setText(String.valueOf(counter.getCurrValue()));
        }

    }

    /*
    When the reset button is pressed the current value is set to the initial value before the saved value
     */
    public void resetCounter(View view) {

        EditText currValue = (EditText) findViewById(R.id.currValue);
        counter.setCurrValue(counter.getInitValue());
        currValue.setText(String.valueOf(counter.getCurrValue()));
    }

    /*
    When the delete button is pressed, the current counter is deleted
     */
    public void delCounter(View view) {

        Intent intent = new Intent();
        intent.putExtra("Edit Counter", "");
        setResult(RESULT_OK, intent);

        finish();
    }

    /*
    When the back button is pressed, the coutner is saved to its changed values, updated in the first activity, and saved.
     */
    @Override
    public void onBackPressed() {

        EditText name = (EditText) findViewById(R.id.counterName);
        EditText currValue = (EditText) findViewById(R.id.currValue);
        EditText initValue = (EditText) findViewById(R.id.initValue);
        EditText comments = (EditText) findViewById(R.id.comment);


        String newName = name.getText().toString();
        int newCurrValue = Integer.parseInt(currValue.getText().toString());
        int newInitValue = Integer.parseInt(initValue.getText().toString());
        String newComment = comments.getText().toString();

        counter.setName(newName);
        counter.setCurrValue(newCurrValue);
        counter.setInitValue(newInitValue);
        counter.setNewDate();
        String newDate = counter.getDate();
        counter.setComment(newComment);

        if(newName.isEmpty() || newName == "\n")
        {
            newName = "No name";
        }
        if(newInitValue < 0)
        {
            counter.setCurrValue(0);
            newCurrValue = 0;
        }
        if(newCurrValue < 0)
        {
            counter.setCurrValue(0);
            newInitValue = 0;
        }
        if(newComment.isEmpty() || newComment == "/n")
        {
            newComment = "No Comments";
        }

        Intent intent = new Intent();
        intent.putExtra("Edit Counter", newName + "\n" + newCurrValue+ "\n" + newInitValue
                + "\n" + newDate + "\n" + newComment);
        setResult(RESULT_OK, intent);

        finish();
    }

}










