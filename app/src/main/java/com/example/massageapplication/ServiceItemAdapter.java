package com.example.massageapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.ViewHolder> implements Filterable {
    private ArrayList<ServiceItem> mServiceItemsData;
    private ArrayList<ServiceItem> mServiceItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;
    public ServiceItemAdapter(Context context, ArrayList<ServiceItem> itemsData) {
        this.mServiceItemsData = itemsData;
        this.mServiceItemsDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ServiceItemAdapter.ViewHolder viewHolder, int i) {
        ServiceItem currentItem = mServiceItemsData.get(i);

        viewHolder.bindTo(currentItem);
    }

    @Override
    public int getItemCount() {
        return mServiceItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return serviceFilter;
    }
    private Filter serviceFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ServiceItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() ==0 ){
                results.count = mServiceItemsDataAll.size();
                results.values = mServiceItemsDataAll;
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(ServiceItem item : mServiceItemsDataAll){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }

                return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        mServiceItemsData = (ArrayList) filterResults.values;
        notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mPriceText;
        private ImageView mItemImage;
        private RatingBar mRatingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.itemTitle);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mPriceText = itemView.findViewById(R.id.price);
            mItemImage = itemView.findViewById(R.id.itemImage);
            mRatingBar = itemView.findViewById(R.id.ratingBar);

            itemView.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Log.d("Activity","The Add to cart button has been pressed");
                }
            });
        }

        public void bindTo(ServiceItem currentItem) {
            mTitleText.setText(currentItem.getName());
            mInfoText.setText(currentItem.getInfo());
            mPriceText.setText(currentItem.getPrice());
            mRatingBar.setRating(currentItem.getRating());

            Glide.with(mContext).load(currentItem.getImageResource()).into(mItemImage);
        }
    };
}


