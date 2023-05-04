package com.freelek.apps.freelekdz.adapter.lists;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.freelek.apps.freelekdz.AppController;
import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.animation.ImageLoaderAnimation;
import com.freelek.apps.freelekdz.classes.Store;
import com.freelek.apps.freelekdz.utils.Utils;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;


public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.mViewHolder> {


    private LayoutInflater infalter;
    private List<Store> data;
    private Context context;
    private ClickListener clickListener;
    private boolean isHorizontalList = false;
    private float width = 0, height = 0;

    public StoreListAdapter(Context context, List<Store> data, boolean isHorizontalList) {
        this.data = data;
        this.infalter = LayoutInflater.from(context);
        this.context = context;
        this.isHorizontalList = isHorizontalList;
    }

    public StoreListAdapter(Context context, List<Store> data, boolean isHorizontalList, float width, float height) {
        this.data = data;
        this.infalter = LayoutInflater.from(context);
        this.context = context;
        this.isHorizontalList = isHorizontalList;
        this.width = width;
        this.height = height;
    }


    @Override
    public StoreListAdapter.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = null;
        if (isHorizontalList) rootView = infalter.inflate(R.layout.v2_item_store, parent, false);
        else rootView = infalter.inflate(R.layout.fragment_custom_item_store, parent, false);


        mViewHolder holder = new mViewHolder(rootView);

        return holder;
    }


    @SuppressLint("StringFormatInvalid")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(StoreListAdapter.mViewHolder holder, int position) {


        //resize image frame
        if (height > 0 && width > 0) {
            //set set the dp dimension
            int dp1 = Utils.dip2pix(context, 1);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) width, (int) height);
            params.setMargins((5 * dp1), (5 * dp1), (5 * dp1), (5 * dp1));
            // holder.frameImage.setLayoutParams(params);
        }

        if (this.data.get(position).getImages() != null && this.data.get(position).getImages().getUrl200_200() != null) {

            Glide.with(context)
                    .load(this.data.get(position).getImages().getUrl200_200())
                    .dontTransform()
                    .placeholder(ImageLoaderAnimation.glideLoader(context))
                    .into(holder.image);

        } else {

            Glide.with(context)
                    .load(R.drawable.def_logo)
                    .centerCrop().placeholder(R.drawable.def_logo)
                    .into(holder.image);

        }


        if (data.get(position).getDistance() > 0 && this.data.get(position).getLatitude() != 0 && this.data.get(position).getLongitude() != 0) {

            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
            String distance_unit = sh.getString("distance_unit", "km");

            if (distance_unit.equals("km")) {
                if (Utils.isNearMAXDistanceKM(this.data.get(position).getDistance())) {
                    holder.distance.setText(
                            Utils.prepareDistanceKm(this.data.get(position).getDistance())
                                    + " " +
                                    Utils.getDistanceByKm(this.data.get(position).getDistance())
                    );

                    holder.distance.setVisibility(View.VISIBLE);
                } else if (!Utils.isNearMAXDistanceKM(this.data.get(position).getDistance())) {
                    holder.distance.setText(String.format(context.getString(R.string.distance_100), distance_unit));
                    holder.distance.setVisibility(View.VISIBLE);
                } else {
                    holder.distance.setVisibility(View.GONE);
                }

            } else {
                if (Utils.isNearMAXDistanceKM(this.data.get(position).getDistance())) {
                    holder.distance.setText(
                            Utils.prepareDistanceMiles(this.data.get(position).getDistance())
                                    + " " +
                                    Utils.getDistanceMiles(this.data.get(position).getDistance())
                    );
                    holder.distance.setVisibility(View.VISIBLE);
                } else if (!Utils.isNearMAXDistanceMiles(this.data.get(position).getDistance())) {
                    holder.distance.setText(String.format(context.getString(R.string.distance_100), distance_unit));
                    holder.distance.setVisibility(View.VISIBLE);
                } else {
                    holder.distance.setVisibility(View.GONE);
                }
            }


            holder.distance.setText(holder.distance.getText().toString().toLowerCase());

        } else {
            holder.distance.setVisibility(View.GONE);
        }


        float rated = (float) data.get(position).getVotes();
        DecimalFormat decim = new DecimalFormat("#.##");

        holder.rate.setText(decim.format(rated) + " (" + data.get(position).getNbr_votes() + ")");


        holder.name.setText(data.get(position).getName());


        Drawable locationDrawable = new IconicsDrawable(context)
                .icon(CommunityMaterial.Icon2.cmd_map_marker)
                .color(ResourcesCompat.getColor(context.getResources(), R.color.colorGrayDefault, null))
                .sizeDp(12);

        holder.address.setCompoundDrawablePadding(10);

        if (AppController.isRTL()) {
            holder.address.setCompoundDrawables(null, null, locationDrawable, null);
        } else {
            holder.address.setCompoundDrawables(locationDrawable, null, null, null);
        }
        holder.address.setText(data.get(position).getAddress());


        if (data.get(position).getLastOffer().equals("")) {
            holder.offer.setVisibility(View.GONE);
        } else {
            holder.offer.setVisibility(View.VISIBLE);
            holder.offer.setText(data.get(position).getLastOffer());
        }


        if (data.get(position).getFeatured() == 0) {
            holder.featured.setVisibility(View.GONE);
        } else {
            holder.featured.setVisibility(View.VISIBLE);
        }

        if (data.get(position).getCategory_name() != null && !data.get(position).getCategory_name().equals("")) {
            holder.store_tag_category.setText((data.get(position).getCategory_name()));
            try {
                if (data.get(position).getCategory_color() != null && !data.get(position).getCategory_color().equals("null")) {
                    holder.store_tag_category.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(data.get(position).getCategory_color())));
                }
            } catch (Exception e) {
                Log.e("colorParser", e.getMessage());
            }
        }

    }


    public void removeAll() {
        int size = this.data.size();

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.data.remove(0);
            }

            if (size > 0)
                this.notifyItemRangeRemoved(0, size);


        }

    }

    public void clear() {

        data = new ArrayList<Store>();
        notifyDataSetChanged();

    }

    public Store getItem(int position) {

        try {
            return data.get(position);
        } catch (Exception e) {
            return null;
        }

    }

    public void addAllItems(RealmList<Store> list) {

        data.addAll(list);
        notifyDataSetChanged();

    }


    public void addItem(Store item) {

        data.add(item);
        notifyDataSetChanged();
        //notifyItemInserted(index);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setClickListener(ClickListener clicklistener) {

        this.clickListener = clicklistener;

    }


    public interface ClickListener {
        void itemClicked(View view, int position);

    }

    public class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ImageView image;
        public TextView name;
        public TextView address;
        public TextView distance;
        public TextView rate;
        public RatingBar ratingBar;
        public TextView offer;
        public ImageView featured;
        public TextView store_tag_category;


        public mViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            rate = itemView.findViewById(R.id.rate);
            distance = itemView.findViewById(R.id.distance);
            ratingBar = itemView.findViewById(R.id.ratingBar2);
            offer = itemView.findViewById(R.id.offer);
            featured = itemView.findViewById(R.id.featured);
            store_tag_category = itemView.findViewById(R.id.store_tag_category);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {


            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }

            //delete(getPosition());


        }
    }


}
