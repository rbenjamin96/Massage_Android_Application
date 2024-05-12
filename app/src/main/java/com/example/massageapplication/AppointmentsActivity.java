package com.example.massageapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
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
        if(user != null){
            Log.d(LOG_TAG, "Authenticated user.");
        } else{
            Log.d(LOG_TAG, "Unauthenticated user.");
            finish();
        }

        myRecycleView = findViewById(R.id.recyclerView);
        myRecycleView.setLayoutManager(new GridLayoutManager(this,gridNumber));
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

        TypedArray itemsImageResource = getResources().obtainTypedArray(R.array.service_item_images);
        TypedArray itemsRate = getResources().obtainTypedArray(R.array.service_item_rates);

        mItemList.clear();

        for(int i = 0; i < itemsList.length;i++){
            mItemList.add(new ServiceItem(itemsList[i],itemsInfo[i], itemsPrice[i],itemsRate.getFloat(i,0),itemsImageResource.getResourceId(i,0)));
        }
        itemsImageResource.recycle();
        mAdapter.notifyDataSetChanged();
    }


}