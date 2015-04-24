package com.jega.kairometer.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.jega.kairometer.R;


public class ProgressBar extends View {

	private float size;
	private float progress;
	
	private int width;
	private int height;
	
	private boolean active;

	private Paint activeProgressBG;
    private Paint activeProgress;
    private Paint activeOverload;
    private Paint deactiveProgress;
    private Paint deactiveOverload;
	private Handler mHandler = new Handler();

	private Runnable runnable = new Runnable(){
		@Override
		public void run() {
			setProgress(progress+(1f/3600f));
			mHandler.postDelayed(runnable, 1000);
		}
	};

	public ProgressBar(Context context) {
		super(context);
		this.size = 0;
		this.progress = 0;
		this.active = false;
		init();
	}

	public ProgressBar(Context context, float size, float progress, boolean active) {
		super(context);
		this.size = size;
		this.progress = progress;
		this.active = active;
		init();
	}


	public ProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressBar, 0, 0);
		try {
			size = a.getFloat(R.styleable.ProgressBar_size, 0);
			progress = a.getFloat(R.styleable.ProgressBar_progress, 0);
			active = a.getBoolean(R.styleable.ProgressBar_active, false);
		} finally {
			a.recycle();
		}
		init();
	}

	private void init(){

		activeProgressBG = new Paint(Paint.ANTI_ALIAS_FLAG);
		activeProgressBG.setColor(0xFFDDEEFF);

		activeProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
		activeProgress.setColor(0xFF88FF00);

		activeOverload = new Paint(Paint.ANTI_ALIAS_FLAG);
		activeOverload.setColor(0xFFFF8800);

		deactiveProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
		deactiveProgress.setColor(0xFFCCCCCC);

		deactiveOverload = new Paint(Paint.ANTI_ALIAS_FLAG);
		deactiveOverload.setColor(0xFFAAAAAA);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int xPad = getPaddingLeft() + getPaddingRight();
		int yPad = getPaddingBottom() + getPaddingTop();

		int suggestedMinimumWidth;
		int suggestedMinimumHeight;

		suggestedMinimumWidth = 0;
		suggestedMinimumHeight = 0;

		width = resolveSizeAndState(suggestedMinimumWidth, widthMeasureSpec, 1);
		height = resolveSizeAndState(suggestedMinimumHeight, heightMeasureSpec, 1);

		setMeasuredDimension(width + xPad, height + yPad);
	}

	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);

		if(active){
			canvas.drawRect(getPaddingLeft(), 0, getPaddingLeft() + getWidth(), getHeight(), activeProgressBG);
			if(progress < size)
				canvas.drawRect(getPaddingLeft(), 0, getPaddingLeft() + getWidth()*(progress/size), getHeight(), activeProgress);
			else{
				canvas.drawRect(getPaddingLeft(), 0, getPaddingLeft() + getWidth()*(size/progress), getHeight(), activeProgress);
				canvas.drawRect(getPaddingLeft() + getWidth()*(size/progress), 0, getPaddingLeft() + getWidth(), getHeight(), activeOverload);
			}
		}else{
			if(progress < size)
				canvas.drawRect(getPaddingLeft(), 0, getPaddingLeft() + getWidth()*(progress/size), getHeight(), deactiveProgress);
			else{
				canvas.drawRect(getPaddingLeft(), 0, getPaddingLeft() + getWidth()*(size/progress), getHeight(), deactiveProgress);
				canvas.drawRect(getPaddingLeft() + getWidth()*(size/progress), 0, getPaddingLeft() + getWidth(), getHeight(), deactiveOverload);
			}

		}
	}

	public float getSize(){
		return this.size;
	}

	public float getProgress(){
		return this.progress;
	}

	public boolean isActive(){
		return this.active;
	}

	public void setSize(float size){
		this.size = size;
		invalidate();
	}

	public void setProgress(float progress){
		this.progress = progress;
		invalidate();
	}

	public void activate(){
		this.active = true;
		invalidate();
		mHandler.post(runnable);
	}

	public void deactivate(){
		this.active = false;
		invalidate();
		mHandler.removeCallbacks(runnable);
	}
}
