package com.demo.application.home.presentation.view.viewholder;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.common.CommonCallBackListner;
import com.demo.application.core.common.Status;
import com.demo.application.core.util.uiwidget.RPTextView;
import com.demo.application.databinding.FriendsBoardItemLayoutBinding;
import com.demo.application.home.data.model.FriendsLeaderBoardModel;
import com.demo.application.util.AppUtil;
import com.demo.application.util.ImageUtil;
import com.demo.application.util.TextUtil;

import java.util.List;

public class LeaderBoardItemViewHolder extends RecyclerView.ViewHolder {
    FriendsBoardItemLayoutBinding layoutBinding;
    Animation startAnimation;
    Animation endanimation;
    public int runColor;

    public LeaderBoardItemViewHolder(@NonNull FriendsBoardItemLayoutBinding layoutBinding) {
        super(layoutBinding.getRoot());
        this.layoutBinding = layoutBinding;
    }


    public void bindUser(FriendsLeaderBoardModel model, List<FriendsLeaderBoardModel> list, int position, CommonCallBackListner commonCallBackListner) {
        if (list.size() - 1 == position) {
            layoutBinding.viewLine.setVisibility(View.GONE);
        } else {
            layoutBinding.viewLine.setVisibility(View.VISIBLE);
        }

        if (model.isChallengedYou()) {
            if (!TextUtil.isEmpty(model.getSentText()) && model.getSentText().equalsIgnoreCase(DemoApp.getAppContext().getString(R.string.accept))) {
                layoutBinding.sentTxt.setText("Accepted");
            }
            layoutBinding.challengeText.setVisibility(View.VISIBLE);
            layoutBinding.parentLayout.setBackgroundColor(DemoApp.getAppContext().getResources().getColor(R.color.yellowdark));
            changeViewColor(layoutBinding.parentLayout);
            layoutBinding.inviteText.setText(DemoApp.getAppContext().getResources().getString(R.string.accept));
        } else {
            if (startAnimation != null) {
                startAnimation.cancel();
            }

            if (endanimation != null) {
                endanimation.cancel();
            }
            layoutBinding.challengeText.setVisibility(View.GONE);
            layoutBinding.inviteText.setText(DemoApp.getAppContext().getResources().getString(R.string.challenge));
            layoutBinding.parentLayout.setBackgroundColor(DemoApp.getAppContext().getResources().getColor(R.color.white));
        }

        if (!TextUtil.isEmpty(model.getStudentName())) {
            layoutBinding.nameText.setText(model.getStudentName());
        } else if (!TextUtil.isEmpty(model.getMobileNo())) {
            layoutBinding.nameText.setText(model.getMobileNo());
        } else {
            layoutBinding.nameText.setVisibility(View.GONE);
        }
        if (model.isProgress()) {
            layoutBinding.inviteButtonLayout.setVisibility(View.GONE);
            layoutBinding.progressBar.setVisibility(View.VISIBLE);
            layoutBinding.sentLayout.setVisibility(View.GONE);
        } else {
            if (model.isSent()) {
                layoutBinding.inviteButtonLayout.setVisibility(View.GONE);
                layoutBinding.progressBar.setVisibility(View.GONE);
                layoutBinding.sentLayout.setVisibility(View.VISIBLE);
            } else {
                layoutBinding.inviteButtonLayout.setVisibility(View.VISIBLE);
                layoutBinding.progressBar.setVisibility(View.GONE);
            }
        }

        if (!TextUtil.isEmpty(model.getStudentScore())) {
            getSpannableString(layoutBinding.scoreText, model.getStudentScore());
        } else {
            layoutBinding.scoreText.setVisibility(View.GONE);
        }
        ImageUtil.loadCircleImage(layoutBinding.profileImage, (String) model.getImagePath());
        layoutBinding.inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutBinding.inviteText.getText().toString().equalsIgnoreCase(DemoApp.getAppContext().getResources().getString(R.string.challenge))) {
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.SEND_INVITE_CLICK, model));
                    }
                } else {
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.ACCEPT_INVITE_CLICK, model));
                    }
                }
            }
        });

    }


    public void getSpannableString(RPTextView textview, String score) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableStringBuilder span1 = new SpannableStringBuilder("Score : ");
        ForegroundColorSpan color1 = new ForegroundColorSpan(ContextCompat.getColor(DemoApp.getAppContext(), R.color.light_grey));
        span1.setSpan(color1, 0, span1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.append(span1);

        SpannableStringBuilder span2 = new SpannableStringBuilder("" + score);
        ForegroundColorSpan color2 = new ForegroundColorSpan(DemoApp.getAppContext().getResources().getColor(R.color.color_red));
        span2.setSpan(color2, 0, span2.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        span2.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, span2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(span2);

        textview.setText(builder, TextView.BufferType.SPANNABLE);
    }


    private void changeViewColor(View view) {
        // Load initial and final colors.
        final int initialColor = DemoApp.getAppContext().getResources().getColor(R.color.orange);
        final int finalColor = DemoApp.getAppContext().getResources().getColor(R.color.white);

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();
                int blended = blendColors(initialColor, finalColor, position);

                // Apply blended color to the view.
                view.setBackgroundColor(blended);
            }
        });
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(500).start();
    }

    private int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }
}
