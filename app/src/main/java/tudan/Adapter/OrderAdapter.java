package tudan.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wjx.homemaker.R;

import java.text.NumberFormat;

import tudan.Item.GoodsItem;


/**
 * Created by fengliang
 * on 2017/7/6.
 */


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Activity activity;
    private SparseArray<GoodsItem> dataList;
    private NumberFormat nf;
    private LayoutInflater mInflater;

    public OrderAdapter(Activity activity, SparseArray<GoodsItem> dataList) {
        this.activity = activity;
        this.dataList = dataList;
        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        mInflater = LayoutInflater.from(activity);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_order_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GoodsItem item = dataList.valueAt(position);
        holder.bindData(item);
    }


    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private GoodsItem item;
        private TextView tvCount, tvCost,tvName,tvAccount;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCost = (TextView) itemView.findViewById(R.id.tvCost);
            tvCount = (TextView) itemView.findViewById(R.id.count);
            tvAccount= (TextView) itemView.findViewById(R.id.account);

//            tvMinus.setOnClickListener(this);
//            tvAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.tvAdd:
//                    activity.add(item, true);
//                    break;
//                case R.id.tvMinus:
//                    activity.remove(item, true);
//                    break;
//                default:
//                    break;
            }
        }

        public void bindData(GoodsItem item) {
            this.item = item;
            tvName.setText(item.name);
            tvCost.setText(nf.format(item.price));
            tvCount.setText(String.valueOf(item.count));
            tvAccount.setText(nf.format(item.count * item.price));
        }
    }
}