package com.example.cst8334_glutentracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cst8334_glutentracker.R;

import java.util.ArrayList;
import java.util.List;


//version feng

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView mainMenu = findViewById(R.id.main_menu);
        List<MenuItem> menu = new ArrayList<>();
        MenuAdapter adapter = new MenuAdapter(menu, this);
        mainMenu.setAdapter(adapter);

        menu.add(new MenuItem(R.drawable.barcode_icon, "Barcode Scanner"));
        menu.add(new MenuItem(R.drawable.cart_icon, "To Cart"));
        menu.add(new MenuItem(R.drawable.receipt_icon, "Receipt List"));
        menu.add(new MenuItem(R.drawable.report_icon, "To Report Page"));
        adapter.notifyDataSetChanged();

        mainMenu.setOnItemClickListener((AdapterView<?> list, View view, int position, long id) -> {
            switch (menu.get(position).getButtonName()){
                case("Barcode Scanner"): {
                    startActivity(new Intent(MainActivity.this, ScanActivity.class));
                    break;
                }

                case("To Cart"): {
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                    break;
                }

                case("Receipt List"): {
                    startActivity(new Intent(MainActivity.this, ReceiptActivity.class));
                    break;
                }

                case("To Report Page"): {
                    startActivity(new Intent(MainActivity.this, ReportActivity.class));
                    break;
                }

                default: break;
            }
        });

        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    private class MenuItem {
        int iconId;
        String buttonName;

        MenuItem(int iconId, String buttonName){
            setIconId(iconId).setButtonName(buttonName);
        }

        MenuItem setIconId(int iconId){
            this.iconId = iconId;
            return this;
        }

        int getIconId(){
            return iconId;
        }

        MenuItem setButtonName(String buttonName){
            this.buttonName = buttonName;
            return this;
        }

        String getButtonName(){
            return buttonName;
        }

    }

    private class MenuAdapter extends ArrayAdapter<MenuItem> {

        MenuAdapter(List<MenuItem> menu, Context context){
            super(context, R.layout.menu_item, menu);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.menu_item, parent, false);

            ImageView icon = convertView.findViewById(R.id.icon);
            TextView buttonName = convertView.findViewById(R.id.button_name);

            icon.setImageDrawable(getDrawable(getItem(position).getIconId()));
            buttonName.setText(getItem(position).getButtonName());

            return convertView;
        }
    }

}
