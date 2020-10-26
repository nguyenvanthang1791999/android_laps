package com.example.myapplication;

import android.app.TabActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LunchList_Lab8 extends TabActivity {




    Cursor curRestaurant = null;
    RestaurantAdapter adapter = null;

    RestaurantHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lunchlist_lab7_layout);
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(onSave);



        helper = new RestaurantHelper(this);

        ListView list = (ListView) findViewById(R.id.restaurants);


        list.setOnItemClickListener(onListClick);



        curRestaurant = helper.getAll();
        startManagingCursor(curRestaurant);
        adapter = new RestaurantAdapter(curRestaurant);
        list.setAdapter(adapter);





        TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
        spec.setContent(R.id.restaurants);
        spec.setIndicator(null,getResources().getDrawable(R.drawable.icon_list));
        getTabHost().addTab(spec);
        spec = getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.details);
        spec.setIndicator(null,
                getResources().getDrawable(R.drawable.icon_details));
        getTabHost().addTab(spec);
        getTabHost().setCurrentTab(0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        helper.close();
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        public void onClick(View v) {
            Restaurant r = new Restaurant();

            EditText name = (EditText)findViewById(R.id.name);
            EditText address = (EditText)findViewById(R.id.addr);
            r.setName(name.getText().toString());
            r.setAddress(address.getText().toString());
            RadioGroup type = (RadioGroup)findViewById(R.id.type);
            switch (type.getCheckedRadioButtonId())
            {
                case R.id.take_out:
                    r.setType("Take out");
                    break;
                case R.id.sit_down:
                    r.setType("Sit down");
                    break;
                case R.id.delivery:
                    r.setType("Delivery");
                    break;
            }
            String result = " ";
            result += r.getName() +" "+ r.getAddress() + " " + r.getType();
            Toast.makeText( LunchList_Lab8.this, result , Toast.LENGTH_SHORT).show();

            helper.insert(r.getName(), r.getAddress(), r.getType());

            curRestaurant.requery();
        }

    };







    class RestaurantAdapter extends CursorAdapter
    {
        public RestaurantAdapter(Cursor c)
        {
            super(LunchList_Lab8.this, c);
        }
        public RestaurantAdapter(Context context, Cursor c) {
            super(context, c);

        }
        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            View row = view;
            ((TextView)row.findViewById(R.id.title)).
                    setText(helper.getName(cursor));
            ((TextView)row.findViewById(R.id.address)).
                    setText(helper.getAddress(cursor));
            ImageView icon = (ImageView)row.findViewById(R.id.icon);
            String type = helper.getType(cursor);
            if (type.equals("Take out"))
                icon.setImageResource(R.drawable.icon_t);
            else if (type.equals("Sit down"))
                icon.setImageResource(R.drawable.icon_s);
            else
                icon.setImageResource(R.drawable.icon_d);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row, parent, false);
            return row;
        }
    }


    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            curRestaurant.moveToPosition(position);
            EditText name;
            EditText address;
            RadioGroup types;

            name = (EditText)findViewById(R.id.name);
            address = (EditText)findViewById(R.id.addr);
            types = (RadioGroup)findViewById(R.id.type);

            name.setText(helper.getName(curRestaurant));
            address.setText(helper.getAddress(curRestaurant));
            if (helper.getType(curRestaurant).equals("Sit down"))
                types.check(R.id.sit_down);
            else if (helper.getType(curRestaurant).equals("Take out"))
                types.check(R.id.take_out);
            else
                types.check(R.id.delivery);

            getTabHost().setCurrentTab(1);
        }
    };

}
