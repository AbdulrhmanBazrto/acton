package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.billingclient.api.SkuDetails;
import com.gnusl.wow.Delegates.ReChargeDelegate;
import com.gnusl.wow.R;

import java.util.List;

public class RechargeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SkuDetails> skuDetails;
    ReChargeDelegate reChargeDelegate;

    public RechargeRecyclerViewAdapter(Context context, List<SkuDetails> skuDetails, ReChargeDelegate reChargeDelegate) {
        this.context = context;
        this.skuDetails = skuDetails;
        this.reChargeDelegate = reChargeDelegate;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.rechrage_view_holder, parent, false);
        return new RechargeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((RechargeItemViewHolder) holder).bind(skuDetails.get(position));

    }

    @Override
    public int getItemCount() {
        return skuDetails.size();
    }

    public class RechargeItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvCoins, tvPrice;

        public RechargeItemViewHolder(View itemView) {
            super(itemView);
            tvCoins = itemView.findViewById(R.id.tv_coins);
            tvPrice = itemView.findViewById(R.id.tv_price);

        }

        public void bind(SkuDetails skuDetails) {

            tvPrice.setText(skuDetails.getPrice());
            tvCoins.setText(skuDetails.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reChargeDelegate != null)
                        reChargeDelegate.onClick(skuDetails);
                }
            });

        }

    }

    // region setters and getters


    // endregion
}
