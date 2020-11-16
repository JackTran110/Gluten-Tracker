package com.example.cst8334_glutentracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.ItemsModel;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    ListView lstView;
    Button btnRpt;

    GlutenDbHelper glt = new GlutenDbHelper(this);



//    receipts.receiptID
//    receipts.receiptFile
//    receipts.totalTaxDeduction
//    receipts.date
//
//    products.productID
//    products.productName
//    products.productDescription
//    products.price
//    products.isGlutenFree



    int images = R.drawable.image;
    String rId[] = {"12301","14502","30003","45601","67834","002345"};
    String rDate[] = {"2020-9-3","2020-8-12","2020-10-8","2020-6-5","2020-4-22","2020-10-1"};
    String rItem[] = {"Purchease detail is Oatmeal","Purchease detail is Pasta","Purchease detail is Banana","Purchease detail is Oatmeal","Purchease detail is orange","Purchease detail is apple"};
    String rSub[] = {"23.50","105.89","78.45","23.50","105.89","78.45"};
    String rTax[] = {"0.8","5.25","1.05","0.8","5.25","1.05"};

   // String receipts[] = {"","","","",""}


    List<ItemsModel> listItems = new ArrayList<>();

    CustomeAdapter customeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //glt.insertIntoReceiptsTable()

        lstView = (ListView) findViewById(R.id.lstReceipt);
        btnRpt = (Button) findViewById(R.id.btnReport);

        btnRpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportActivity.this,ReportMActivity.class));

            }
        });



        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position+1;
                Intent intent= new Intent(ReportActivity.this,DigitalReceipt.class);
                intent.putExtra("index",position);
                startActivity(intent);
            }
        });



        for(int i = 0; i < rId.length; i++){

            ItemsModel itemsModel = new ItemsModel(rId[i],rDate[i],rItem[i],rSub[i],rTax[i]);

            listItems.add(itemsModel);


        }

        customeAdapter = new CustomeAdapter(listItems, this);
        lstView.setAdapter(customeAdapter);




    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem menuItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                customeAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getGroupId();
        if(id == R.id.search_view){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class CustomeAdapter extends BaseAdapter implements Filterable {

        private List<ItemsModel> itemsModelList;
        private List<ItemsModel> itemsModelListFiltered;
        private Context context;

        public CustomeAdapter(List<ItemsModel> itemsModelList, Context context) {
            this.itemsModelList = itemsModelList;
            this.itemsModelListFiltered = itemsModelList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return itemsModelListFiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.row_items,null);

            ImageView imageView = view.findViewById(R.id.itemView);
            TextView itemId = view.findViewById(R.id.itemId);
            TextView itemDate = view.findViewById(R.id.itemDate);
            TextView itemDetail = view.findViewById(R.id.itemDetail);
            TextView itemSub = view.findViewById(R.id.itemSub);
            TextView itemTax = view.findViewById(R.id.itemTax);

           // imageView.setImageResource(itemsModelsFiltered.get(position).getImage());
            itemId.setText("Receipt No.:" + itemsModelListFiltered.get(position).getrId());
            itemDate.setText("Date:" + itemsModelListFiltered.get(position).getrDate());
            itemDetail.setText("Detail:" + itemsModelListFiltered.get(position).getrItem());
            itemSub.setText("Total:" + itemsModelListFiltered.get(position).getrSub());
            itemTax.setText("Tax Deduction:" + itemsModelListFiltered.get(position).getrTax());

            return view;
        }

        @Override
        public Filter getFilter() {

            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults = new FilterResults();

                    if(constraint == null || constraint.length() == 0){

                        filterResults.count = itemsModelList.size();
                        filterResults.values = itemsModelList;
                    }else{

                        String searhStr = constraint.toString().toLowerCase();
                        List<ItemsModel> resultData = new ArrayList<>();

                        for(ItemsModel itemsModel:itemsModelList){
                            if(itemsModel.getrId().contains(searhStr) || itemsModel.getrDate().contains(searhStr) || itemsModel.getrItem().contains(searhStr)){
                                resultData.add(itemsModel);
                            }

                            filterResults.count = resultData.size();
                            filterResults.values = resultData;

                        }



                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    itemsModelListFiltered = (List<ItemsModel>) results.values;

                    notifyDataSetChanged();

                }
            };
            return filter;
        }
    }
}