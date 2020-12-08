package com.example.cst8334_glutentracker.activity;

import androidx.annotation.Nullable;
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

public class MainMenuActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 0;
    public static final int RESULT_CODE_NAVIGATE_TO_SCANNER = 1;
    public static final int RESULT_CODE_NAVIGATE_TO_CART = 2;
    public static final int RESULT_CODE_NAVIGATE_TO_RECEIPT = 3;
    public static final int RESULT_CODE_NAVIGATE_TO_REPORT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView mainMenu = findViewById(R.id.main_menu);
        List<MenuItem> menu = new ArrayList<>();
        MenuAdapter adapter = new MenuAdapter(menu, this);
        mainMenu.setAdapter(adapter);

        menu.add(new MenuItem(R.drawable.barcode_icon,
                RESULT_CODE_NAVIGATE_TO_SCANNER,
                "Barcode Scanner"));
        menu.add(new MenuItem(R.drawable.cart_icon,
                RESULT_CODE_NAVIGATE_TO_CART,
                "To Cart"));
        menu.add(new MenuItem(R.drawable.receipt_icon,
                RESULT_CODE_NAVIGATE_TO_RECEIPT,
                "Receipt List"));
        menu.add(new MenuItem(R.drawable.report_icon,
                RESULT_CODE_NAVIGATE_TO_REPORT,
                "To Report Page"));
        adapter.notifyDataSetChanged();

        mainMenu.setOnItemClickListener((AdapterView<?> list, View view, int position, long id) -> {
            navigateToActivity(menu.get(position).getButtonNavigateCode());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        navigateToActivity(resultCode);
    }

    private void navigateToActivity(int buttonNavigateCode){
        switch (buttonNavigateCode){
            case RESULT_CODE_NAVIGATE_TO_SCANNER: {
                startActivityForResult(
                        new Intent(MainMenuActivity.this, ScanActivity.class),
                        REQUEST_CODE);
                break;
            }

            case RESULT_CODE_NAVIGATE_TO_CART: {
                startActivityForResult(
                        new Intent(MainMenuActivity.this, CartActivity.class),
                        REQUEST_CODE);
                break;
            }

            case RESULT_CODE_NAVIGATE_TO_RECEIPT: {
                startActivityForResult(
                        new Intent(MainMenuActivity.this, ReceiptActivity.class),
                        REQUEST_CODE);
                break;
            }

            case RESULT_CODE_NAVIGATE_TO_REPORT: {
                startActivityForResult(
                        new Intent(MainMenuActivity.this, ReportActivity.class),
                        REQUEST_CODE);
                break;
            }

            default: break;
        }
    }

    private class MenuItem {
        int iconId;
        int buttonNavigateCode;
        String buttonName;

        MenuItem(int iconId, int buttonNavigateCode, String buttonName){
            setIconId(iconId)
                    .setButtonNavigateCode(buttonNavigateCode)
                    .setButtonName(buttonName);
        }

        MenuItem setIconId(int iconId){
            this.iconId = iconId;
            return this;
        }

        int getIconId(){
            return iconId;
        }

        MenuItem setButtonNavigateCode(int buttonNavigateCode){
            this.buttonNavigateCode = buttonNavigateCode;
            return this;
        }

        int getButtonNavigateCode(){
            return buttonNavigateCode;
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
