package com.example.massageapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AppointmentsActivity extends AppCompatActivity {
    private static final String LOG_TAG = AppointmentsActivity.class.getName();
    private FirebaseUser user;

    private RecyclerView myRecycleView;
    private ArrayList<ServiceItem> mItemList;
    private ServiceItemAdapter mAdapter;
    private int gridNumber = 1;
    //Menu:
    private boolean viewRow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointments);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user.");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user.");
            finish();
        }

        myRecycleView = findViewById(R.id.recyclerView);
        myRecycleView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        mAdapter = new ServiceItemAdapter(this, mItemList);

        myRecycleView.setAdapter(mAdapter);

        initializeData();

    }

    //Date:
    /*private void openDialog(){
        DatePickerDialog dialog = new DatePickerDialog(this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                textDate.setText(String.valueOf(year)+"."+String.valueOf(month+1)+"."+ String.valueOf(day));
            }
        }, 2024, 01, 01);
        dialog.show();
    }*/


    @SuppressLint("NotifyDataSetChanged")
    private void initializeData() {
        String[] itemsList = getResources().getStringArray(R.array.service_item_names);
        String[] itemsInfo = getResources().getStringArray(R.array.service_item_description);
        String[] itemsPrice = getResources().getStringArray(R.array.service_item_price);
        String[] webpages = getResources().getStringArray(R.array.webpages);

        TypedArray itemsImageResource = getResources().obtainTypedArray(R.array.service_item_images);
        TypedArray itemsRate = getResources().obtainTypedArray(R.array.service_item_rates);

        mItemList.clear();

        for (int i = 0; i < itemsList.length; i++) {
            mItemList.add(new ServiceItem(itemsList[i], itemsInfo[i], itemsPrice[i], itemsRate.getFloat(i, 0), itemsImageResource.getResourceId(i, 0), webpages[i]));
            if (myRecycleView.callOnClick()) {
                openWebpage(webpages[i]);
            }
        }
        itemsImageResource.recycle();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.service_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                Log.d(LOG_TAG, "Log out!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.setting_button:
                Log.d(LOG_TAG, "Settings button!");
                return true;
            case R.id.cart:
                Log.d(LOG_TAG, "it's a cart");
                return true;
            case R.id.view_selector:
                Log.d(LOG_TAG, "view changed");
                if (viewRow) {
                    changeSpanCount(item, R.drawable.view_change, 1);
                } else {
                    changeSpanCount(item, R.drawable.view_change, 1);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void changeSpanCount(MenuItem item, int viewChange, int i) {
        viewRow = !viewRow;
        item.setIcon(viewChange);
        GridLayoutManager layoutManager = (GridLayoutManager) myRecycleView.getLayoutManager();
        layoutManager.setSpanCount(i);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        return super.onPrepareOptionsMenu(menu);
    }

    public void openWebpage(String URL) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        startActivity(browserIntent);
    }


    public void cancel(View view) {
        finish();
    }
}