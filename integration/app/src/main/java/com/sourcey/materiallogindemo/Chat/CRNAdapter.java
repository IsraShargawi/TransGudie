package com.sourcey.materiallogindemo.Chat;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourcey.materiallogindemo.R;

import java.util.List;

public class CRNAdapter extends RecyclerView.Adapter<CRNAdapter.MyViewHolder> {
    public String ROOM_NAME ="";
    private List<CRNModel> list;
    private final OnItemClickListener listener;
    public CRNAdapter(List<CRNModel> newsList, OnItemClickListener listener) {
        this.list = newsList;
        this.listener = listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_room, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AppCompatButton title;
        public MyViewHolder(View view) {
            super(view);
            title = (AppCompatButton)view.findViewById(R.id.txtName);
        }
        public void bind(final CRNModel item, final OnItemClickListener listener) {
            title.setText(item.getTitle());
            itemView.findViewById(R.id.txtName).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });*/
        }
    }
    //Interface to handle click event
    public interface OnItemClickListener {
        void onItemClick(CRNModel item);
    }
}