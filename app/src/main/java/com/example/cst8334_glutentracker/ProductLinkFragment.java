package com.example.cst8334_glutentracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.entity.Product;

import org.w3c.dom.Text;

import java.util.ArrayList;

/* <- Added this
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductLinkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductLinkFragment extends Fragment {
    ArrayList<Product> listOfProducts = new ArrayList<Product>();
    FragmentAdapter fragmentAdapter = new FragmentAdapter();
    Bundle dataFromActivity;

    /* <- added this
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductLinkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductLinkFragment.
     */
 /* <- added this   // TODO: Rename and change types and number of parameters
    public static ProductLinkFragment newInstance(String param1, String param2) {
        ProductLinkFragment fragment = new ProductLinkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    } */
    public ProductLinkFragment(){
            // required default constructor
         }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      /*  // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_link, container, false); */
        View result = inflater.inflate(R.layout.fragment_product_link, container, false);
        //Bundle dataFromActivity = getArguments();
        dataFromActivity = getArguments();
        ListView itemsList = result.findViewById(R.id.fragmentListView);
        listOfProducts.add(new Product(3, "Chip", "A bag of chips", "test", 1.00, false));
        itemsList.setAdapter(fragmentAdapter);
        fragmentAdapter.notifyDataSetChanged();
        return result;
    }

    class FragmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listOfProducts.size();
        }

        @Override
        public Product getItem(int position) {
            return listOfProducts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return listOfProducts.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Product product = (Product) getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            //View newView = inflater.inflate(R.layout.activity_product_list, parent, false);
            View newView = inflater.inflate(R.layout.activity_fragment_populate_listview, parent, false);
   //         TextView textTest = newView.findViewById(R.id.textTest);
   //         TextView testPrice = newView.findViewById(R.id.testPrice);
            int passedIndex = dataFromActivity.getInt("Index");
            //testPrice.setText(product.getPrice() + "");
   //         testPrice.setText(CartActivity.getProductsArrayList().get(passedIndex).getPrice() + "");
            //Product passedProduct = (Product) dataFromActivity.getSerializable("Product");

            Button testButton = newView.findViewById(R.id.linkCommit);
            testButton.setOnClickListener((v) -> {
                //passedProduct.setPrice(product.getPrice());
                CartActivity.getProductsArrayList().get(passedIndex).setDisplayedPrice(product.getPrice());
            });


            /*TextView productName = newView.findViewById(R.id.productName);
            productName.setText(product.getProductName());
            TextView price = newView.findViewById(R.id.price);
            price.setText(product.getDisplayedPrice() + "");
            EditText quantity = newView.findViewById(R.id.quantity);
            //quantity.setText("1");
            quantity.setText(Integer.toString(product.getQuantity()));

            Button plusButton = newView.findViewById(R.id.plusButton);
            plusButton.setOnClickListener((v) ->{

                int convertedToInt = Integer.parseInt(quantity.getText().toString());
                product.setDisplayedPrice(product.getPrice() * (convertedToInt + 1));
                product.setQuantity(convertedToInt + 1);
                fragmentAdapter.notifyDataSetChanged();
            });

            Button minusButton = newView.findViewById(R.id.minusButton);
            minusButton.setOnClickListener((v) ->{
                if(product.getQuantity() > 1) {
                    int convertedToInt = Integer.parseInt(quantity.getText().toString());
                    product.setDisplayedPrice(product.getPrice() * (convertedToInt - 1));
                    product.setQuantity(convertedToInt - 1);
                    fragmentAdapter.notifyDataSetChanged();
                }
            });

            Button removeButton = newView.findViewById(R.id.removeFromCart);
            removeButton.setOnClickListener((v) -> {
                listOfProducts.remove(position);
                //Toast.makeText(this, "Should be working", Toast.LENGTH_SHORT).show();
                fragmentAdapter.notifyDataSetChanged();
            });

            Button linkButton = newView.findViewById(R.id.linkButton);
            linkButton.setOnClickListener((v) -> {
                // Finish the rest of this
            }); */

            return newView;
            //return null;
        }
    }
}