package com.example.cst8334_glutentracker.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.cst8334_glutentracker.R;
import com.example.cst8334_glutentracker.database.GlutenDatabase;
import com.example.cst8334_glutentracker.entity.ItemsModel;
import com.example.cst8334_glutentracker.entity.Product;
import com.example.cst8334_glutentracker.entity.Receipt;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    AlertDialog myDialog;
    TextView txtA;
    ListView lstPopup;

    ListView lstView;
    Button btnRpt;

    private GlutenDatabase dbOpener = new GlutenDatabase(this);
    private String uId,uDate,uItem,uSub,uTax;
    private List<Product> uItems;



    int images = R.drawable.image;
//    String rId[] = {"12301","14502","30003","45601","67834","002345"};
//    String rDate[] = {"2020-9-3","2020-8-12","2020-10-8","2020-6-5","2020-4-22","2020-10-1"};
//    String rItem[] = {"Purchease detail is Oatmeal","Purchease detail is Pasta","Purchease detail is Banana","Purchease detail is Oatmeal","Purchease detail is orange","Purchease detail is apple"};
//    String rSub[] = {"23.50","105.89","78.45","23.50","105.89","78.45"};
//    String rTax[] = {"0.8","5.25","1.05","0.8","5.25","1.05"};

    // String receipts[] = {"","","","",""}


    List<ItemsModel> listItems = new ArrayList<>();

    CustomeAdapter customeAdapter;
    Toolbar reportTbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportTbar = (Toolbar) findViewById(R.id.reportToolbar);
        lstView = (ListView) findViewById(R.id.lstReceipt);
        btnRpt = (Button) findViewById(R.id.btnReport);

        List<Receipt> rec = dbOpener.selectAllReceipt();


        for (int j = 0; j < rec.size(); j++) {
            uId = String.valueOf(rec.get(j).getId());
            uDate = (rec.get(j).getDate()).substring(4, 7).trim() + "-" + (rec.get(j).getDate()).substring(8, 10).trim() + "-" + (rec.get(j).getDate()).substring(24, 28).trim() ;
            uSub = String.valueOf(rec.get(j).getTotalPrice());
            uTax = String.valueOf(rec.get(j).getTaxDeductionTotal());
            uItems = rec.get(j).getProducts();

            ItemsModel itemsModel = new ItemsModel(uId, uDate,uItems, uSub, uTax);
            listItems.add(itemsModel);
        }

        btnRpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportActivity.this, ReportMActivity.class));

            }
        });


        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<String> lstP = new ArrayList<>();



                //ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lstP);


                createNewDiaglog(position);

            }
        });



        customeAdapter = new CustomeAdapter(listItems, this);
        lstView.setAdapter(customeAdapter);


    }

    public void createNewDiaglog(int position){

        AlertDialog.Builder mybuilder = new AlertDialog.Builder(this);

        String str = "   ID: " + listItems.get(position).getrId() + "    DATE: " + listItems.get(position).getrDate() + "  TOTAL: " +  listItems.get(position).getrSub() + "    TAX(DE.): " + listItems.get(position).getrTax();



        int num = listItems.get(position).getrItem().size() + 1;


        String[] popupStr = new String[num];
        popupStr[0] = " PRODUCT  ID\t\t\t\t\t\tNAME\t\t\t\t\t\t\t\t\tQTY.";


        for (int n = 1; n < num; n++ ){

            popupStr[n] = String.valueOf(listItems.get(position).getrItem().get(n-1).getId()) + "\t\t\t" + (listItems.get(position).getrItem().get(n-1).getProductName()).substring(0,12)  + "\t\t\t\t\t\t" + listItems.get(position).getrItem().get(n-1).getQuantity();
        }


        mybuilder.setTitle("RECEIPT:" + str).setItems(popupStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        myDialog = mybuilder.create();
        myDialog.show();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        menu.findItem(R.id.reportButton).setVisible(false);

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
        switch (item.getItemId()) {
            case R.id.scannerButton:
                Intent goToScanner = new Intent(ReportActivity.this, ScanActivity.class);
                startActivity(goToScanner);
                break;
            case R.id.cartButton:
                Intent goToCart = new Intent(ReportActivity.this, CartActivity.class);
                startActivity(goToCart);
                break;
            case R.id.receiptButton:
                Intent goToReceipt = new Intent(ReportActivity.this, ReceiptActivity.class);
                startActivity(goToReceipt);
                break;
            case R.id.search_view:
                break;
        }
        return true;
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
            itemDetail.setText(""); //+ itemsModelListFiltered.get(position).getrItem());
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
                            if(itemsModel.getrId().contains(searhStr) || itemsModel.getrDate().contains(searhStr)){
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