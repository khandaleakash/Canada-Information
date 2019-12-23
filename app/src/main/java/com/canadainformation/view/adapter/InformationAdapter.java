package com.canadainformation.view.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.canadainformation.R;
import com.canadainformation.model.Information;
import com.canadainformation.view.information.InfoItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created By Akash
 * on 17,Dec,2019 : 2:58 PM
 */
public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder> {

    private List<InfoItem> infoItemList;

    public InformationAdapter(List<InfoItem> itemList) {
        this.infoItemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Information information = infoItemList.get(position).getmInformation();

        if (!TextUtils.isEmpty(information.getImageHref())) {

            Picasso.get().load(information.getImageHref()).error(R.drawable.ic_launcher_foreground).into(holder.ivImage, new Callback() {
                @Override
                public void onSuccess() {
                    holder.ivImage.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    holder.ivImage.setVisibility(View.GONE);
                }
            });

        } else {
            holder.ivImage.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(information.getTitle())) {
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(information.getTitle());
        } else {
            holder.tvTitle.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(information.getDescription())) {
            holder.tvDescription.setVisibility(View.VISIBLE);
            holder.tvDescription.setText(information.getDescription());
        } else {
            holder.tvDescription.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return infoItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDescription;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            ivImage = itemView.findViewById(R.id.iv_image);
        }
    }


    public void setInfoItemList(List<InfoItem> itemList) {
        this.infoItemList = itemList;
        notifyDataSetChanged();
    }

}
