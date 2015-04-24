package com.jega.kairometer.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jega.kairometer.R;


public class Qualifier extends View {
	
	private int numLevels;
	private int level;
	private int imageW;
	private int imageH;
	
	private float innerPadding;
	
	private Drawable deactImage;
	private Drawable actImage;

	public Qualifier(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Qualifier, 0, 0);
		
		try {
			numLevels = a.getInteger(R.styleable.Qualifier_numLevels, 2);
			innerPadding = a.getDimension(R.styleable.Qualifier_innerPadding, 0);
			level = a.getInteger(R.styleable.Qualifier_level, 1);
			deactImage = a.getDrawable(R.styleable.Qualifier_deactImage);
			actImage = a.getDrawable(R.styleable.Qualifier_actImage);
		} finally {
			a.recycle();
		}
		
		imageW = actImage.getIntrinsicWidth();
		imageH = actImage.getIntrinsicHeight();
	}
	
	@Override
	protected int getSuggestedMinimumWidth(){
		return (imageW*numLevels) + ((int)innerPadding*(numLevels-1));
	}

	@Override
	protected int getSuggestedMinimumHeight(){
		return imageH;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		
		int xpad = getPaddingLeft() + getPaddingRight();
		int ypad = getPaddingBottom() + getPaddingTop();
		
		int w = resolveSizeAndState(getSuggestedMinimumWidth(), widthMeasureSpec, 1);
		
		setMeasuredDimension(w + xpad, imageH + ypad);
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		actImage.setBounds(
				getPaddingLeft(), 
				getPaddingTop(), 
				getPaddingRight() + imageW, 
				getPaddingBottom() + imageH
			);
		actImage.draw(canvas);
		
		for(int i = 1; i < numLevels; i++){
			if(i<level){
				actImage.setBounds(
						getPaddingLeft() + ((int)innerPadding + imageW)*i, 
						getPaddingTop(), 
						getPaddingRight() + ((int)innerPadding + imageW)*i + imageW, 
						getPaddingBottom() + imageH
					);
				actImage.draw(canvas);
			}else{
				deactImage.setBounds(
						getPaddingLeft() + ((int)innerPadding + imageW)*i, 
						getPaddingTop(), 
						getPaddingRight() + ((int)innerPadding + imageW)*i + imageW, 
						getPaddingBottom() + imageH
					);
				deactImage.draw(canvas);
				
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){	
		
		if(event.getAction() == MotionEvent.ACTION_DOWN)
			setLevel(getLevelOnTouch(event.getX()));
		
		return super.onTouchEvent(event);
	}
	
	public int getLevelOnTouch(float x){
		return (int)Math.round(x/imageW);
	}
	
	public int getNumLevels(){
		return this.numLevels;
	}
	
	public int getLevel(){
		return this.level;
	}
	
	public void setNumLevels(int numLevels){
		this.numLevels = numLevels;
		invalidate();
		//requestLayout();
	}
	
	public void setLevel(int level){
		this.level = level;
		invalidate();
		//requestLayout();
	}

}
