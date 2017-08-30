package com.djavid.bitcoinrate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BaseRecyclerAdapter;
import com.djavid.bitcoinrate.model.realm.TickerItem;
import com.djavid.bitcoinrate.view.fragment.TickerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TickerRecyclerAdapter extends BaseRecyclerAdapter<TickerItem, TickerRecyclerAdapter.ViewHolder> {

    private final TickerFragment.OnFragmentInteractionListener mListener;

    public TickerRecyclerAdapter(Context context, TickerFragment.OnFragmentInteractionListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void bindSingleItem(ViewHolder viewHolder, TickerItem item) {
        viewHolder.ticket_text.setText(Double.toString(item.getPrice()));

        viewHolder.view.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onFragmentInteraction(item);
            }
        });
    }

    @Override
    protected ViewHolder createVH(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        @BindView(R.id.ticket_text)
        TextView ticket_text;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
