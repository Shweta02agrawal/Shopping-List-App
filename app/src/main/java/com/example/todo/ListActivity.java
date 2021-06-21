package com.example.todo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.example.todo.data.DataBaseHandler;
import com.example.todo.model.Item;
import com.example.todo.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private static final String TAG = "ListActivity";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    private Button saveButton;
    List<Item> itemList;
    DataBaseHandler dataBaseHandler;
    private FloatingActionButton floatingActionButton;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView=findViewById(R.id.recyclerView);



        ActionBar actionBar=getSupportActionBar();
        ColorDrawable colorDrawable=new ColorDrawable(Color.parseColor("#C1F57F17"));
        actionBar.setBackgroundDrawable(colorDrawable);




        dataBaseHandler=new DataBaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList=new ArrayList<>();

        //get item from database and it in itemlist
        itemList=dataBaseHandler.getAllItems();
        Log.d("item", "onCreate: " + itemList);
        for(Item item:itemList){
            Log.d(TAG, "onCreate: " + item.getItemName());
        }
        recyclerViewAdapter=new RecyclerViewAdapter(this,itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        //to notify adapter about the changes in the view
        recyclerViewAdapter.notifyDataSetChanged();


        floatingActionButton=findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopUpDialog();
            }
        });
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
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();

    }

    private void saveItem(View v) {
        Item item=new Item();
        item.setItemName(babyItem.getText().toString().trim());
        item.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString().trim()));
        item.setItemColor(itemColor.getText().toString().trim());
        item.setItemSize(Integer.parseInt(itemSize.getText().toString().trim()));
        dataBaseHandler.addItem(item);
        Snackbar.make(v,"Item added Successfully", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable(){
            public void run(){
                alertDialog.dismiss();
                startActivity(new Intent(ListActivity.this,ListActivity.class));
                finish();
            }
        },1200);

    }
}