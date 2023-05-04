package com.freelek.apps.freelekdz.booking.controllers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.booking.modals.Item;
import com.freelek.apps.freelekdz.classes.Currency;
import com.freelek.apps.freelekdz.classes.Setting;
import com.freelek.apps.freelekdz.controllers.SettingsController;
import com.freelek.apps.freelekdz.parser.api_parser.OfferCurrencyParser;
import com.freelek.apps.freelekdz.utils.OfferUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> items = new ArrayList<>();
    private final Context ctx;
    private ClickListener mClickListener;

    public ItemsAdapter(Context context, RealmList<Item> items) {
        this.items = items;
        ctx = context;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setClickListener(final ClickListener mItemClickListener) {
        this.mClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_service, parent, false);
        vh = new ItemViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder view = (ItemViewHolder) holder;


            final Item reservation = items.get(position);
            view.title_product.setText(reservation.getName());

            String service = reservation.getService();

            String[] arrayServices = service.split(",");
            if (arrayServices.length > 0) {
                String customString = "";
                for (String serv : arrayServices) {
                    customString = customString + " - " + serv + " \n ";
                }
                view.desc_product.setText(customString);
                view.desc_product.setVisibility(View.VISIBLE);
            }


            if (reservation.getAmount() > 0) {

                //get currrency from appConfig API
                String defaultLocalCurrency = null;
                Setting defaultAppSetting = SettingsController.findSettingFiled("CURRENCY_OBJECT");
                if (defaultAppSetting != null && !defaultAppSetting.getValue().equals("")) {
                    defaultLocalCurrency = defaultAppSetting.getValue();

                    OfferCurrencyParser mProductCurrencyParser = null;
                    try {
                        mProductCurrencyParser = new OfferCurrencyParser(new JSONObject(
                                defaultLocalCurrency
                        ));

                        mProductCurrencyParser.getCurrency();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }


                //calculate  the amount based on qty
                double amountOrder = reservation.getAmount() * (reservation.getQty() > 0 ? reservation.getQty() : 1);
                view.price_product.setText(OfferUtils.parseCurrencyFormat(
                        (float) amountOrder,
                        OfferUtils.defaultCurrency()));



            } else {
                view.price_product.setVisibility(View.GONE);
            }


            if (reservation.getImage() != null && !reservation.getImage().equals("")) {
                Glide.with(ctx)
                        .load(reservation.getImage())
                        .centerCrop().placeholder(R.drawable.calendar)
                        .into(view.image_product);
            } else {
                Glide.with(ctx).load(R.drawable.calendar)
                        .centerCrop().into(view.image_product);
            }

        }
    }

    public void removeAll() {

        int size = this.items.size();

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.items.remove(0);
            }

            if (size > 0)
                this.notifyItemRangeRemoved(0, size);
        }
    }

    public float getTotalPrice() {
        float total_price = 0;
        if (items != null && items.size() > 0) {
            for (Item service : items) {
                total_price = (float) (total_price + (service.getAmount() * service.getQty()));
            }
        }
        return total_price;
    }

    public Currency getCurrency() {

        return null;
    }

    public void addItem(Item item) {

        int index = (items.size());
        items.add(item);
        notifyItemInserted(index);
    }

    public void addAll(final List<Item> productList) {
        int size = productList.size();

        items.clear();
        if (size > 0) {
            //remove all items before adding new items
            for (int i = 0; i < size; i++) {
                items.add(productList.get(i));
            }

            notifyDataSetChanged();
        }


    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }


    public interface ClickListener {
        void onItemClick(View view, int pos);

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout product_detail_layout;
        ImageView image_product;
        TextView title_product;
        TextView desc_product;
        TextView price_product;


        public ItemViewHolder(View v) {
            super(v);

            product_detail_layout = v.findViewById(R.id.product_detail_layout);
            image_product = v.findViewById(R.id.image_product);
            title_product = v.findViewById(R.id.title_product);
            desc_product = v.findViewById(R.id.desc_product);
            price_product = v.findViewById(R.id.price_product);

        }

    }


}