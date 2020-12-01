package com.demo.application.home.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.application.R;
import com.demo.application.databinding.QuizwonItemLayoutBinding;
import com.demo.application.home.data.model.QuizResModel;

import java.util.ArrayList;
import java.util.List;


public class QuizWonAdapter extends RecyclerView.Adapter<QuizWonAdapter.ViewHolder> {

    List<QuizResModel> mValues;
    Context mContext;
    QuizwonItemLayoutBinding binding;
    int count;

    public QuizWonAdapter(Context context, List<QuizResModel> values, int count) {
        mValues = values;
        mContext = context;
        this.count = count;
    }

    public void updateList(ArrayList<QuizResModel> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        QuizwonItemLayoutBinding binding;

        public ViewHolder(QuizwonItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(QuizResModel resModel, int position) {
            binding.youWon.setText(mContext.getString(R.string.you_win)+" "+mContext.getString(R.string.rs)+"50");
            if (resModel.isWonStatus()) {
                binding.strip.setBackgroundColor(mContext.getResources().getColor(R.color.blue_color));
                binding.circle.setBackground(mContext.getResources().getDrawable(R.drawable.blue_circle));
                binding.youWon.setTextColor(mContext.getResources().getColor(R.color.blue_color));
            } else {
                binding.strip.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
                binding.circle.setBackground(mContext.getResources().getDrawable(R.drawable.grey_circle));
            }

        }

    }

    @Override
    public QuizWonAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.quizwon_item_layout, viewGroup, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position), position);


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}
