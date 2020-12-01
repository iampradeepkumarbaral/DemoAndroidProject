package com.demo.application.home.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.application.R;
import com.demo.application.databinding.QuizDocUploadLayoutBinding;
import com.demo.application.home.data.model.KYCDocumentDatamodel;

import java.util.ArrayList;


public class KYCViewDocAdapter extends RecyclerView.Adapter<KYCViewDocAdapter.ViewHolder> {

    ArrayList<KYCDocumentDatamodel> mValues;
    Context mContext;
    QuizDocUploadLayoutBinding binding;

    public KYCViewDocAdapter(Context context, ArrayList<KYCDocumentDatamodel> values) {
        mValues = values;
        mContext = context;
    }

    public void updateList(ArrayList<KYCDocumentDatamodel> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        QuizDocUploadLayoutBinding binding;

        public ViewHolder(QuizDocUploadLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(KYCDocumentDatamodel kycDocumentDatamodel, int position) {
            binding.docImageLayout.setVisibility(View.GONE);
            binding.imageCloudDone.setVisibility(View.VISIBLE);
            binding.uploadImageLayout.setVisibility(View.GONE);
            binding.tvIdHead.setText(kycDocumentDatamodel.getDocumentName());
            //ImageUtil.loadNormalImage(binding.docImage, kycDocumentDatamodel.getDocumentUrl());
        }

    }

    @Override
    public KYCViewDocAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.quiz_doc_upload_layout, viewGroup, false);
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
