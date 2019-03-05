package com.nivashini.numbersround.datatransaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nivashini.numbersround.R;
import com.nivashini.numbersround.utils.AppUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*** Created by Nivashinim on 16-04-2018.*/

public class RoundingStepsListAdapter extends RecyclerView.Adapter<RoundingStepsListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MStepDetails> notifList;
    private AppUtils appUtils = new AppUtils();

    private View.OnClickListener onClickListener = new MyOnClickListener();

    public RoundingStepsListAdapter(Context context, ArrayList<MStepDetails> notifList) {
        this.context = context;
        this.notifList = notifList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_rounding_steps_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new RoundingStepsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvFirstNo.setText(notifList.get(position).getFirstNo());
        holder.tvSecondNo.setText(notifList.get(position).getSecondNo());
        holder.tvOperator.setText(notifList.get(position).getOperator());
        holder.tvResult.setText(notifList.get(position).getResult());
        holder.tvStepCount.setText("Step " + String.valueOf(position+1));

    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFirstNo, tvSecondNo, tvOperator, tvResult, tvStepCount;

        ViewHolder(View itemView) {
            super(itemView);
            tvFirstNo = itemView.findViewById(R.id.tvFirstNo);
            tvSecondNo = itemView.findViewById(R.id.tvSecondNo);
            tvOperator = itemView.findViewById(R.id.tvOperator);
            tvResult = itemView.findViewById(R.id.tvResult);
            tvStepCount = itemView.findViewById(R.id.tvStepCount);
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            RecyclerView recyclerView = new RecyclerView(context);
//            int itemPos = recyclerView.getChildLayoutPosition(view);
//            appUtils.goToFragment(context, R.id.main_frag_container, new ViewIdeaDetailFrag(), true);
            appUtils.showLongToast(context, "This feature will be available later");
        }
    }
}
