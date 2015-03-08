package com.example.sajal.intentexample;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


public class Alpha extends ActionBarActivity {

    String radioButtonChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alpha);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alpha, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        Intent i = new Intent(this,Beta.class);
        EditText userName = (EditText) findViewById(R.id.enterNameText);
        EditText contactNum = (EditText) findViewById(R.id.editNumberText);
        String userNameString = userName.getText().toString();
        String contactNumString = contactNum.getText().toString();
        i.putExtra("userName",userNameString);
        i.putExtra("contactNum",contactNumString);
        i.putExtra("radioButton",radioButtonChecked);
        startActivity(i);


    }

    public void onRadioButtonClicked(View view) {
        RadioButton radioButton = (RadioButton) view;
        boolean checked = radioButton.isChecked();
        Context context = getApplicationContext();
        switch(view.getId()){
            case R.id.metroRadioButton:
                if(checked) {
                    radioButtonChecked = radioButton.getText().toString();
                    Toast toast = Toast.makeText(context,radioButtonChecked,Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.taxiRadioButton:
                if(checked) {
                    radioButtonChecked = radioButton.getText().toString();
                    Toast toast = Toast.makeText(context,radioButtonChecked,Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.busRadioButton:
                if(checked) {
                    radioButtonChecked = radioButton.getText().toString();
                    Toast toast = Toast.makeText(context,radioButtonChecked,Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }

    }
}
