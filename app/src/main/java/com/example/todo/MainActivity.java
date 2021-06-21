package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.todo.data.DataBaseHandler;
import com.example.todo.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    private DataBaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        ColorDrawable colorDrawable=new ColorDrawable(Color.parseColor("#C1F57F17"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        db=new DataBaseHandler(this);
        //todo: make an bypassactivity function that will take diretly to the listactivity when there are some items in database

        byPassActivity();

        //todo: item is saved or not

        List<Item> items=db.getAllItems();
        for(Item item:items){
            Log.d("Main", "onCreate: " + item.getDateItemAdded());
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopUpDialog();
            }


        });
    }

    private void byPassActivity() {
        if(db.getItemsCount()>0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
    }


    private void createPopUpDialog() {
        builder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.popup,null);
        babyItem=view.findViewById(R.id.babyItem);
        itemQuantity=view.findViewById(R.id.itemQuantity);
        itemColor=view.findViewById(R.id.itemColor);
        itemSize=view.findViewById(R.id.itemSize);

        saveButton=view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!babyItem.getText().toString().isEmpty()
                        && !itemQuantity.getText().toString().isEmpty()
                        && ! itemColor.getText().toString().isEmpty()
                        && !itemSize.getText().toString().isEmpty()) {
                    saveItem(v);
                }
                else{
                    Snackbar.make(v,"Empty area not allowed",Snackbar.LENGTH_SHORT).show();

                }
            }
        });

        //Important steps
        //add view to dialogue builder
        builder.setView(view);
        alertDialog=builder.create();    //creating dialog object that has all widgets
        alertDialog.show();
    }
    private void saveItem(View view) {
        //todo: save each baby item

        Item item=new Item();
        String itemName=babyItem.getText().toString().trim();
        int quantity= Integer.parseInt(itemQuantity.getText().toString());
        String color=itemColor.getText().toString().trim();
        int size= Integer.parseInt(itemSize.getText().toString());

        item.setItemName(itemName);
        item.setItemQuantity(quantity);
        item.setItemColor(color);
        item.setItemSize(size);
        db.addItem(item);
        Snackbar.make(view,"Item added Successfully",Snackbar.LENGTH_SHORT).show();


        //todo: move to next screen that is detail screen

        //when we want to delay something for a bit and then do something like moving to next activity we use handler class
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //actual working happens here
                //Step 1- dismiss popup box
                alertDialog.dismiss();
                //Step 2- instantiate itent to move to next activity
                startActivity(new Intent(MainActivity.this,ListActivity.class));

            }
        },1200); // it is time in milliseconds

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //@Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}