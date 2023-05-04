package com.freelek.apps.freelekdz.booking.controllers.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.animation.Animation;
import com.freelek.apps.freelekdz.appconfig.Constances;
import com.freelek.apps.freelekdz.booking.modals.Reservation;
import com.freelek.apps.freelekdz.booking.views.activities.BookingCheckoutActivity;
import com.freelek.apps.freelekdz.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookingListExpandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Reservation> data = new ArrayList<>();

    private final Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public BookingListExpandAdapter(Context context, List<Reservation> data) {
        this.data = data;
        ctx = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item_expand, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("StringFormatInvalid")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;


            final Reservation mReservation = data.get(position);
            view.orderID.setText(
                    String.format(ctx.getString(R.string.booking_id), mReservation.getId())
            );

            //set status with color
            String[] arrayStatus = mReservation.getStatus().split(";");
            if (arrayStatus.length > 0) {
                view.status.setText(arrayStatus[0].substring(0, 1).toUpperCase() + arrayStatus[0].substring(1));
                if (arrayStatus[1] != null && !arrayStatus[0].equals("null")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(arrayStatus[1])));
                    }
                }
            }

            //set status with color
            if (mReservation.getPayment_status() != null) {
                String[] arrayPayStatus = mReservation.getPayment_status().split(";");
                if (arrayPayStatus.length > 0) {


                    if (arrayPayStatus[0] != null && arrayPayStatus[0].equalsIgnoreCase("cod_paid"))
                        arrayPayStatus[0] = ctx.getString(R.string.paid_cash);
                    else if (arrayPayStatus[0] != null && arrayPayStatus[0].equalsIgnoreCase("cod"))
                        arrayPayStatus[0] = ctx.getString(R.string.payment_spot);


                    view.order_payment_status.setText(arrayPayStatus[0].substring(0, 1).toUpperCase() + arrayPayStatus[0].substring(1));

                    if (arrayPayStatus.length == 2 && arrayPayStatus[1] != null && !arrayPayStatus[0].equals("null")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            view.order_payment_status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(arrayPayStatus[1])));
                        }else{
                            view.order_payment_status.setVisibility(View.GONE);
                        }
                    }else{
                        view.order_payment_status.setVisibility(View.GONE);
                    }

                    //showup a pay now button when the order is not paid yet
                    if (arrayPayStatus[0].equals("unpaid")) {
                        view.btn_pay_now.setVisibility(View.VISIBLE);

                        //view.btn_order_detail.setVisibility(View.GONE);

                        view.btn_pay_now.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Send The Message Receiver ID & Name
                                Intent intent = new Intent(v.getContext(), BookingCheckoutActivity.class);
                                intent.putExtra("fragmentToOpen", "fragment_payment");
                                intent.putExtra("booking_id", mReservation.getId());
                                if (mReservation.getCart() != null && !mReservation.getCart().equals("null")) {
                                    try {
                                        JSONObject cartArray = new JSONObject(mReservation.getCart());
                                        if (cartArray.length() > 0) {
                                            intent.putExtra("module_id", mReservation.getId_store());
                                            intent.putExtra("module", Constances.ModulesConfig.BOOKING_MODULE);
                                            intent.putExtra("cart", mReservation.getCart());
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                v.getContext().startActivity(intent);
                            }
                        });
                    } else {
                        view.btn_pay_now.setVisibility(View.GONE);
                        view.btn_order_detail.setVisibility(View.VISIBLE);
                    }
                }
            }


            //setup product items in recyclerview
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ctx, 1);
            ItemsAdapter mProductAdapter = new ItemsAdapter(ctx, mReservation.getItems());
            view.list_items.setHasFixedSize(true);
            view.list_items.setLayoutManager(mLayoutManager);
            view.list_items.setItemAnimator(new DefaultItemAnimator());
            view.list_items.setAdapter(mProductAdapter);

            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.get(position).expanded = toggleLayoutExpand(!mReservation.expanded, v, view.lyt_expand);
                }
            });

            view.itemBookingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.get(position).expanded = toggleLayoutExpand(!mReservation.expanded, view.bt_expand, view.lyt_expand);
                }
            });


            // void recycling view
            if (mReservation.expanded) {
                view.lyt_expand.setVisibility(View.VISIBLE);
            } else {
                view.lyt_expand.setVisibility(View.GONE);
            }

            Utils.toggleArrow(mReservation.expanded, view.bt_expand, false);


        }
    }


    public Reservation getItem(int position) {

        try {
            return data.get(position);
        } catch (Exception e) {
            return null;
        }

    }

    public void addItem(Reservation item) {

        int index = (data.size());
        data.add(item);
        notifyItemInserted(index);

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

    private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
        Utils.toggleArrow(show, view);
        if (show) {
            Animation.expand(lyt_expand);
        } else {
            Animation.collapse(lyt_expand);
        }
        return show;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onOrderDetailClick(int position);

    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        View itemBookingLayout;
        TextView orderID;
        TextView status;
        TextView order_payment_status;
        ImageButton bt_expand;
        View lyt_expand;
        View lyt_parent;
        RecyclerView list_items;
        AppCompatButton btn_order_detail;
        AppCompatButton btn_pay_now;


        public OriginalViewHolder(View v) {
            super(v);
            itemBookingLayout = v.findViewById(R.id.itemBookingLayout);
            orderID = v.findViewById(R.id.booking_id);
            status = v.findViewById(R.id.order_status);
            order_payment_status = v.findViewById(R.id.order_payment_status);
            bt_expand = v.findViewById(R.id.bt_expand);
            lyt_expand = v.findViewById(R.id.lyt_expand);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            list_items = v.findViewById(R.id.list_items);

            btn_order_detail = v.findViewById(R.id.btn_order_detail);
            btn_pay_now = v.findViewById(R.id.btn_pay_now);


            //click listener
            itemBookingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });

            btn_order_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onOrderDetailClick(getAdapterPosition());
                    }
                }
            });


        }
    }


}