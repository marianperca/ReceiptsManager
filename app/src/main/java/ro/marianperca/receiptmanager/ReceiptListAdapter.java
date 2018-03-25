package ro.marianperca.receiptmanager;


import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ro.marianperca.receiptmanager.database.model.Receipt;

public class ReceiptListAdapter extends RecyclerView.Adapter<ReceiptListAdapter.ViewHolder> {
    private SimpleDateFormat formatDate = new SimpleDateFormat("EEEE, MMM d", Locale.ENGLISH);
    private List<Receipt> mDataset = new ArrayList<>();
    private ListActionListener mListener;

    @Override
    public ReceiptListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receipt_list_item, parent, false);

        return new ViewHolder(v);
    }

    void swap(List<Receipt> entries) {
        mDataset.clear();
        mDataset.addAll(entries);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mValue.setText(mDataset.get(position).getValue() + "");
        holder.mDate.setText(formatDate.format(mDataset.get(position).getDate()));
        holder.mStore.setText(mDataset.get(position).getStore());
        holder.mOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), holder.mOptions);
                popup.inflate(R.menu.options_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.option_delete:
                                mListener.deleteReceipt(mDataset.get(position));
                                mDataset.remove(position);
                                notifyDataSetChanged();
                                break;
                        }

                        return false;
                    }
                });

                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setListener(ListActionListener mListener) {
        this.mListener = mListener;
    }

    public interface ListActionListener {
        void deleteReceipt(Receipt r);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mValue;
        TextView mDate;
        TextView mStore;
        ImageView mOptions;

        ViewHolder(CardView v) {
            super(v);

            mValue = v.findViewById(R.id.value);
            mDate = v.findViewById(R.id.date);
            mStore = v.findViewById(R.id.store);
            mOptions = v.findViewById(R.id.menu_more);
        }
    }
}