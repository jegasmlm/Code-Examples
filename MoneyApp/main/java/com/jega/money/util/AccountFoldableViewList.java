package com.jega.money.util;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jega.money.R;

import java.util.ArrayList;

/**
 * Created by jegasmlm on 4/6/2015.
 */
public class AccountFoldableViewList extends LinearLayout {
    private String headerTittle;
    private ViewGroup headerContainer;
    private ViewGroup content;
    private boolean selected = false;
    private ArrayList<View> accountsToHide = new ArrayList<>();
    private ArrayList<Float> accountsToHideInitialWeight = new ArrayList<>();
    private float amount;

    public AccountFoldableViewList(Context context, String tittle, float amount) {
        super(context);
        this.headerTittle = tittle;
        this.amount = amount;
        setLayoutAttrs();
        loadSubComponents(context);
        setAttrs();
    }

    private void setLayoutAttrs() {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
        setOrientation(VERTICAL);
        setId(R.id.foldableContainer);
    }

    private void loadSubComponents(Context context) {
        LayoutInflater.from(context).inflate(R.layout.foldable_container, this);
        headerContainer = (ViewGroup) findViewById(R.id.headerContainer);
        content = (ViewGroup) findViewById(R.id.content);
    }

    private void setAttrs() {
        ((TextView) findViewById(R.id.tittle)).setText(this.headerTittle);
        ((TextView) findViewById(R.id.balance)).setText(String.valueOf(this.amount));
        headerContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = !selected;
                if (selected) {
                    unFold();
                } else {
                    Fold();
                }
            }
        });
    }

    private void Fold() {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator headerAnimation = Animations.animateWeight(headerContainer, 0.2f, 1);
        animatorSet.play(headerAnimation).with(Animations.animateWeight(content, 1, 0));
        for(int i = 0; i < accountsToHide.size(); i++)
            animatorSet.play(headerAnimation).with(Animations.animateWeight(accountsToHide.get(i), 0, accountsToHideInitialWeight.get(i)));
        animatorSet.start();
    }

    private void unFold() {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator headerAnimation = Animations.animateWeight(headerContainer, 2, 0.2f);
        animatorSet.play(headerAnimation).with(Animations.animateWeight(content, 0, 1));
        for(int i = 0; i < accountsToHide.size(); i++) {
            accountsToHideInitialWeight.set(i, ((LayoutParams) accountsToHide.get(i).getLayoutParams()).weight);
            animatorSet.play(headerAnimation).with(Animations.animateWeight(accountsToHide.get(i), accountsToHideInitialWeight.get(i), 0));
        }
        animatorSet.start();
    }

    @Override
    public void addView(View view){
        content.addView(view);
        content.findViewById(R.id.noAccountMessage).setVisibility(GONE);
    }

    public void setAccountsToHide(View... accountsToHide) {
        for(View accountToHide : accountsToHide) {
            this.accountsToHide.add(accountToHide);
            accountsToHideInitialWeight.add(((LayoutParams) accountToHide.getLayoutParams()).weight);
        }
    }

    public View getHeader() {
        return headerContainer;
    }

    public void setSelector(int res) {
        headerContainer.findViewById(R.id.header).setBackgroundResource(res);
    }
}
