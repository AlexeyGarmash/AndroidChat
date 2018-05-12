package com.example.tabloproject.blablachat;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.OpenChannel;

import java.util.List;

/**
 * Created by Tablo Project on 09.04.2018.
 */

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {

    private Context context;
    public static final String EXTRA_CHANNEL = "CHANNEL";

    public static class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener, View.OnLongClickListener{


        ItemClickListener itemClickListener;

        // each data item is just a string in this case
        private TextView mTextViewName;
        private TextView mTextViewType;
        private ImageView mImageChanel;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextViewName = (TextView) itemView.findViewById(R.id.chanel_name);
            mTextViewType = (TextView)itemView.findViewById(R.id.chanel_type);
            mImageChanel = (ImageView)itemView.findViewById(R.id.chanel_image);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
            return false;
        }
    }



    private List<BaseChannel> chanels;



    public GroupsAdapter(List<BaseChannel> chanels, Context context){
        this.chanels = chanels;
        this.context = context;
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chanels_list_part, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTextViewName.setText(chanels.get(position).getName());
        if(chanels.get(position).isOpenChannel())
            holder.mTextViewType.setText("Open channel");
        else
            holder.mTextViewType.setText("Group channel");
        ImageUtils.displayRoundImageFromUrl(context, chanels.get(position).getCoverUrl(), holder.mImageChanel);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {
                if(isLongClick){
                    Toast.makeText(context, "Long: " + chanels.get(pos).getName(), Toast.LENGTH_SHORT).show();

                }
                else
                {


                    String channelUrl = chanels.get(pos).getUrl();
                    Toast.makeText(context, "Once click: " + channelUrl, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MessageListActivity.class);
                    intent.putExtra(EXTRA_CHANNEL, channelUrl);
                    context.startActivity(intent);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return chanels.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
