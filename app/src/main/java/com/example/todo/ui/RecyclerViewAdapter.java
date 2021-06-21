package com.example.todo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.ListActivity;
import com.example.todo.R;
import com.example.todo.data.DataBaseHandler;
import com.example.todo.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.sql.DatabaseMetaData;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private  List<Item> itemList;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private LayoutInflater inflater;



    public RecyclerViewAdapter(Context context,List<Item>itemList) {
        this.context=context;
        this.itemList=itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //create a new view which defines the ui of list item
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row,viewGroup,false);


        return new ViewHolder(view,context);
    }
    //replacing the content of the view by the data of layout
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {
    //todo: get the element from dataset at position "position" and replace content of the view by that element
        Item item=itemList.get(position);   //object item
        viewHolder.itemName.setText(MessageFormat.format("Item: {0}", item.getItemName()));
        viewHolder.item_Quantity.setText(MessageFormat.format("Quantity: {0}", Integer.toString(item.getItemQuantity())));
        viewHolder.itemColor.setText(MessageFormat.format("Color: {0}", item.getItemColor()));
        viewHolder.itemSize.setText(MessageFormat.format("Size: {0}", Integer.toString(item.getItemSize())));
        viewHolder.dateAdded.setText(MessageFormat.format("Date: {0}", item.getDateItemAdded()));

     }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemName;
        public TextView item_Quantity;
        public TextView itemColor;
        public TextView itemSize;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            itemName= itemView.findViewById(R.id.item_name);
            item_Quantity=itemView.findViewById(R.id.item_quantity);
            itemColor=itemView.findViewById(R.id.item_color);
            itemSize=itemView.findViewById(R.id.item_size);
            dateAdded=itemView.findViewById(R.id.item_date);

            editButton=itemView.findViewById(R.id.edit_button);
            deleteButton=itemView.findViewById(R.id.delete_button);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
               // to get position od=f element
            int position=getAdapterPosition();
            Item item=itemList.get(position);
            switch(v.getId()){
                case R.id.edit_button:
                    //edit item
//                    position=getAdapterPosition();
//                    Item item=itemList.get(position);
                    editText(item);


                    break;
                case R.id.delete_button:
//                    position=getAdapterPosition();
//                    Item item=itemList.get(position);
                    //delete item
                    //todo: to permannently delete an item from recycler view we have to pass an object to the function and this object should contain
                    //todo: the all the items of current position
                    deleteItem(item.getId());
                    break;

            }
        }



        private void deleteItem(int id) {
            builder=new AlertDialog.Builder(context);
            inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.confirmation_popup,null);

            Button yesButton=view.findViewById(R.id.yes_button);
            Button noButton=view.findViewById(R.id.no_button);
            builder.setView(view);
            alertDialog=builder.create();
            alertDialog.show();


            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataBaseHandler dataBaseHandler=new DataBaseHandler(context);
                    dataBaseHandler.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemChanged(getAdapterPosition());

                    alertDialog.dismiss();
                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });


        }

        private void editText(Item newitem) {
            //todo: populate the popup with current item object
//            Item item=itemList.get(getAdapterPosition());



            builder=new AlertDialog.Builder(context);
            inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.popup,null);

            Button saveButton;
            EditText babyItem;
            EditText itemQuantity;
            EditText itemColor;
            EditText itemSize;
            TextView title_text;

            babyItem=view.findViewById(R.id.babyItem);
            itemQuantity=view.findViewById(R.id.itemQuantity);
            itemColor=view.findViewById(R.id.itemColor);
            itemSize=view.findViewById(R.id.itemSize);
            title_text=view.findViewById(R.id.title);
            saveButton=view.findViewById(R.id.save_button);
            title_text.setText("Edit Text");
            saveButton.setText("Update");

            babyItem.setText(newitem.getItemName());
            itemQuantity.setText(String.valueOf(newitem.getItemQuantity()));
            itemColor.setText(newitem.getItemColor());
            itemSize.setText(String.valueOf(newitem.getItemSize()));
            builder.setView(view);
            alertDialog=builder.create();
            alertDialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataBaseHandler dataBaseHandler=new DataBaseHandler(context);

                    newitem.setItemName(babyItem.getText().toString().trim());
                    newitem.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));
                    newitem.setItemColor(itemColor.getText().toString());
                    newitem.setItemSize(Integer.parseInt(itemSize.getText().toString()));

                    if(!babyItem.getText().toString().isEmpty() && !itemQuantity.getText().toString().isEmpty() && !itemColor.getText().toString().isEmpty() && !itemSize.getText().toString().isEmpty()){
                        dataBaseHandler.updateItem(newitem);

                        //todo: after updation we have to inform recycleviewadpater about the changes and we have to paas new item object to update it in place of old one
                        notifyItemChanged(getAdapterPosition(),newitem);
                    }
                    else{
                        Snackbar.make(view,"Field Is Empty",Snackbar.LENGTH_SHORT).show();
                    }
                    alertDialog.dismiss();
                }
            });
        }

        }


}



