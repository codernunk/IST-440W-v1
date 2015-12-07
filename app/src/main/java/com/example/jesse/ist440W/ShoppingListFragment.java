package com.example.jesse.ist440W;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jesse.ist440W.models.App;
import com.example.jesse.ist440W.models.ShoppingList;
import com.example.jesse.ist440W.models.ShoppingListItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends ListFragment {

    private ShoppingListAdapter _adapter;

    private ArrayList<ShoppingList> _filteredShoppingLists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _filteredShoppingLists = new ArrayList<ShoppingList>();
        for (ShoppingList s : App.getInstance().getShoppingLists()){
            _filteredShoppingLists.add(s);
        }

        _adapter = new ShoppingListAdapter(this.getContext(), android.R.layout.simple_list_item_1, _filteredShoppingLists);
        setListAdapter(_adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShoppingList s = (ShoppingList) parent.getItemAtPosition(position);

                Intent i = new Intent(getActivity(), ShoppingListDetailsActivity.class);
                i.putExtra("ShoppingList", s.getShoppingListId());
                startActivity(i);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    public void filter(String searchStr){
        _filteredShoppingLists = new ArrayList<ShoppingList>();
        for (ShoppingList s : App.getInstance().getShoppingLists()){
            if (s.getTitle().toLowerCase().contains(searchStr)){
                _filteredShoppingLists.add(s);
            }
        }
        _adapter.refresh(_filteredShoppingLists);
    }

    public void resetFilter(){
        _filteredShoppingLists = new ArrayList<ShoppingList>();
        for (ShoppingList s : App.getInstance().getShoppingLists()){
            _filteredShoppingLists.add(s);
        }
        _adapter.refresh(_filteredShoppingLists);
    }

    /**
     * An inner class that helps to construct list items
     * that show recipes.
     */
    private class ShoppingListAdapter extends ArrayAdapter<ShoppingList>{

        private Context _context;

        public ShoppingListAdapter(Context context, int textViewResourceId, List<ShoppingList> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            this._context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ShoppingList rowItem = getItem(position);

            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            SimpleDateFormat sds = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.shopping_list_list_view, null);

                // Set the display's name to the shopping list's name
                TextView titleText = (TextView) convertView.findViewById(R.id.txtShoppingListTitle);
                titleText.setText(getColor(rowItem, rowItem.getTitle().toString()));

                TextView dateText = (TextView) convertView.findViewById(R.id.txtDate);
                dateText.setText(getColor(rowItem, sds.format(rowItem.getDate())));
            }else{
                // Set the display's name to the shopping list's name
                TextView titleText = (TextView) convertView.findViewById(R.id.txtShoppingListTitle);
                titleText.setText(getColor(rowItem, rowItem.getTitle().toString()));

                TextView dateText = (TextView) convertView.findViewById(R.id.txtDate);
                dateText.setText(getColor(rowItem, sds.format(rowItem.getDate())));
            }

            return convertView;
        }

        public void refresh(List<ShoppingList> shoppingLists){
            clear();
            addAll(shoppingLists);
            notifyDataSetChanged();
        }

        public Spanned getColor(ShoppingList sl, String str){
            boolean isDone = true;
            for (ShoppingListItem sli : sl.getList()){
                if (!sli.isDone())
                    isDone = false;
            }

            if (isDone){
                return Html.fromHtml("<span style='color: green'>" + str + "</span>");
            }else{
                return Html.fromHtml(str);
            }
        }

    }

}
