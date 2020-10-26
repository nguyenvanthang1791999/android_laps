package vanthang.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.service.autofill.LuhnChecksumValidator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost;
import android.app.TabActivity;
import android.widget.AdapterView;

import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.List;

public class lunchlist extends TabActivity {

    private List<Restaurant> restaurantlist=new ArrayList<Restaurant>();
    //private ArrayAdapter<Restaurant> adapter =null;
    RestaurantAdapter adapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(onSave);
        ListView list=(ListView)findViewById(R.id.restaurants);
        list.setOnItemClickListener(onListClick);
       // adapter=new ArrayAdapter<Restaurant>(this,android.R.layout.simple_list_item_1,restaurantlist);
        adapter=new RestaurantAdapter();
        list.setAdapter(adapter);
        TabHost.TabSpec spec  = getTabHost().newTabSpec("tag1");
        spec.setContent(R.id.restaurants);
        spec.setIndicator("List",getResources().getDrawable(R.drawable.list));
        getTabHost().addTab(spec);
        spec = getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.details);
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
        getTabHost().addTab(spec);
        getTabHost().setCurrentTab(0);
    }
    private View.OnClickListener onSave = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Restaurant r=new Restaurant();
            EditText name = (EditText) findViewById(R.id.name);
            EditText address = (EditText)findViewById(R.id.addr);
            r.setName(name.getText().toString());
            r.setAddress(address.getText().toString());
            RadioGroup type = (RadioGroup)findViewById(R.id.type);
            switch (type.getCheckedRadioButtonId())
            {
                case R.id.take_out:
                    r.setType("Take out");
                    Toast.makeText(lunchlist.this, name.getText().toString()+""+address.getText().toString()+""+"Take out", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.sit_down:
                    r.setType("Sit down");
                    Toast.makeText(lunchlist.this, name.getText().toString()+""+address.getText().toString()+""+"Sit down", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.delivery:
                    r.setType("Delivery");
                    Toast.makeText(lunchlist.this, name.getText().toString()+""+address.getText().toString()+""+"Delivery", Toast.LENGTH_SHORT).show();
                    break;
            }
            restaurantlist.add(r);
        }
    };
    class RestaurantAdapter extends ArrayAdapter<Restaurant> {
        public RestaurantAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public RestaurantAdapter() {
            super(lunchlist.this, android.R.layout.simple_list_item_1, restaurantlist);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.row, null);
            }
            Restaurant r = restaurantlist.get(position);
            ((TextView) row.findViewById(R.id.title)).setText(r.getName());
            ((TextView) row.findViewById(R.id.address)).setText(r.getAddress());
            ImageView icon = (ImageView) row.findViewById(R.id.icon);
            String type = r.getType();
            if (type.equals("Take out"))
                icon.setImageResource(R.drawable.type_t);
            else if (type.equals("Sit down"))
                icon.setImageResource(R.drawable.type_s);
            else icon.setImageResource(R.drawable.type_d);
            return row;
        }
    }
    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener()  {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Restaurant r = restaurantlist.get(position);
            EditText name;
            EditText address;
            RadioGroup types;
            name = (EditText)findViewById(R.id.name);
            address = (EditText)findViewById(R.id.addr);
            types = (RadioGroup)findViewById(R.id.type);
            name.setText(r.getName());
            address.setText(r.getAddress());
            if (r.getType().equals("Sit down"))
                    types.check(R.id.sit_down);
            else if (r.getType().equals("Take out"))
                types.check(R.id.take_out);
            else
                types.check(R.id.delivery);
            getTabHost().setCurrentTab(1);
        }
    };
}
