package com.demo.application.home.presentation.view.viewholder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.application.databinding.MonthWiseItemLayoutBinding;
import com.demo.application.home.data.model.MonthlyScholarShipModel;

public class MonthlyWiseViewHolder extends RecyclerView.ViewHolder {
    MonthWiseItemLayoutBinding binding;


    public MonthlyWiseViewHolder(@NonNull MonthWiseItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bindUser(MonthlyScholarShipModel model, int position) {

    }
}
