package com.example.windows10gamer.demo3.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windows10gamer.demo3.R;
import com.example.windows10gamer.demo3.activity.AllContactActivity;
import com.example.windows10gamer.demo3.activity.PaymentActivity;
import com.example.windows10gamer.demo3.activity.PrepaidActivity;
import com.example.windows10gamer.demo3.model.ContactVO;
import com.example.windows10gamer.demo3.model.FilterHelper;

import java.util.List;

/**
 * Created by Windows 10 Gamer on 18/07/2017.
 */

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder> implements Filterable{

    private List<ContactVO> contactVOList,currentList;
    private Context mContext;
    public AllContactsAdapter(List<ContactVO> contactVOList, Context mContext){
        this.contactVOList = contactVOList;
        this.mContext = mContext;
        this.currentList = contactVOList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ContactVO contactVO = contactVOList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());
    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    @Override
    public Filter getFilter() {
        return FilterHelper.newInstance(currentList,this);
    }

    public void setContactVOList(List<ContactVO> contactVOList){
        this.contactVOList = contactVOList;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;

        public ContactViewHolder(final View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.setBackgroundColor(Color.parseColor("#b43d2d2d"));
                    Intent intent = new Intent(mContext, PrepaidActivity.class);
                    intent.putExtra("contactname", String.valueOf(contactVOList.get(getPosition()).getContactName()));
                    intent.putExtra("contactnumber", String.valueOf(contactVOList.get(getPosition()).getContactNumber()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Log.d("contact", String.valueOf(contactVOList.get(getPosition()).getContactName()));
                    mContext.startActivity(intent);
                }
            });
        }

    }

}
