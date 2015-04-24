package com.jega.kairometer.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jega.kairometer.R;


public class InteractiveSpinner extends View {

    onChangeListener listener;

    private int min;
    private int max;
    private int minDigits;
	private int selectedItem;
	private int numItems;
	private int arrowWidth;
	private int arrowHeight;
	private int arrowPos;
	private int textColor;
	private int maxTextWidth;
	private int maxTextHeight;
	private int textPos;

	private int[] textWidth;

	private float innerPadding;
	private float textSize;
	private float contentWidth;

	private boolean loop;
    private Boolean isNumeric;

	private String orientation;
	private CharSequence[] items;
	
	private Bitmap arrow;
	
	private Paint textPaint;
	private Paint arrowPaint;

    public interface onChangeListener{

        public void onChange();
    }
	public void setOnChangeListener(onChangeListener eventListener) {
		listener = eventListener;
	}

	public InteractiveSpinner(Context context) {
		super(context);

        this.min = Integer.MIN_VALUE;
        this.max = Integer.MAX_VALUE;
        this.isNumeric = false;
		this.innerPadding = 0;
		this.contentWidth = 0;
		this.textSize = 20;
		this.textColor = 0;
		this.selectedItem = 1;
		this.orientation = "horizontal";
		this.items = null;
		this.loop = false;
		this.arrow = null;
		init();
	}

	public InteractiveSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.InteractiveSpinner, 0, 0);
		try {
            isNumeric = a.getBoolean(R.styleable.InteractiveSpinner_isNumeric, false);
            min = a.getInteger(R.styleable.InteractiveSpinner_min, Integer.MIN_VALUE);
            max = a.getInteger(R.styleable.InteractiveSpinner_max, Integer.MAX_VALUE);
            minDigits = a.getInteger(R.styleable.InteractiveSpinner_digits, 1);
			innerPadding = a.getDimension(R.styleable.InteractiveSpinner_innerPadding, 0);
			contentWidth = a.getDimension(R.styleable.InteractiveSpinner_contentWidth, 0);
			textSize = a.getDimension(R.styleable.InteractiveSpinner_textSize, 20);
			textColor = a.getColor(R.styleable.InteractiveSpinner_textColor, 0);
			selectedItem = a.getInteger(R.styleable.InteractiveSpinner_selectedItem, 0);
			orientation = a.getString(R.styleable.InteractiveSpinner_orientation);
			items = a.getTextArray(R.styleable.InteractiveSpinner_items);
			loop = a.getBoolean(R.styleable.InteractiveSpinner_loop, false);
			arrow = drawableToBitmap(a.getDrawable(R.styleable.InteractiveSpinner_arrow));
		} finally {
			a.recycle();
		}
		init();
	}

	private void init(){
        if(!isNumeric) {
            textWidth = new int[items.length];
            numItems = items.length;
        }else if(selectedItem<min)
            selectedItem = min;


		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(textColor);
		textPaint.setTextSize(textSize);

		arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		arrowWidth = arrow.getWidth();
		arrowHeight = arrow.getHeight();

        Rect textBounds = getTextBounds();
		maxTextWidth = Math.max(textBounds.width(), (int)contentWidth);
		maxTextHeight = textBounds.height();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int xPad = getPaddingLeft() + getPaddingRight();
		int yPad = getPaddingBottom() + getPaddingTop();

		int suggestedMinimumWidth;
		int suggestedMinimumHeight;
		int w = 0;
		int h = 0;

        if (orientation.equals("horizontal")) {
            suggestedMinimumWidth = arrowWidth * 2 + (int) innerPadding * 2 + maxTextWidth;
            suggestedMinimumHeight = Math.max(arrowHeight, maxTextHeight);

            w = resolveSizeAndState(suggestedMinimumWidth, widthMeasureSpec, 1);
            h = resolveSizeAndState(suggestedMinimumHeight, heightMeasureSpec, 1);

            textPos = getPaddingTop() + h / 2 + maxTextHeight / 2;
            arrowPos = getPaddingTop() + h / 2 - arrowHeight / 2;
        } else {
            suggestedMinimumWidth = Math.max(arrowWidth, maxTextWidth);
            suggestedMinimumHeight = arrowHeight * 2 + (int) innerPadding * 2 + maxTextHeight;

            w = resolveSizeAndState(suggestedMinimumWidth, widthMeasureSpec, 1);
            h = resolveSizeAndState(suggestedMinimumHeight, heightMeasureSpec, 1);

            textPos = getPaddingLeft() + w / 2 - maxTextWidth / 2;
            arrowPos = getPaddingLeft() + w / 2 - arrowWidth / 2;
        }

		setMeasuredDimension(w + xPad, h + yPad);
	}

	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);

		if(orientation.equals("horizontal")){
            drawHorizontal(canvas);
		}else{
            drawVertical(canvas);
		}
	}

    private void drawVertical(Canvas canvas) {
        int left;
        int top;
        left = arrowPos;
        top = getPaddingTop();

        if(!loop && selectedItem == 0)
            arrowPaint.setAlpha(100);
        else
            arrowPaint.setAlpha(255);

        canvas.drawBitmap(rotateBitmap(arrow, -90), left, top, arrowPaint);

        top = top + arrowHeight + (int)innerPadding + maxTextHeight;

        if(!isNumeric) {
            canvas.drawText(items[selectedItem].toString(), textPos + (maxTextWidth / 2 - textWidth[selectedItem] / 2), top, textPaint);
        }else{
            String text = String.valueOf(selectedItem);
            if(text.length() < minDigits)
                for(int i=text.length(); i<minDigits; i++)
                    text = "0" + text;
            int textWidth = getTextWidth(text, textPaint);
            canvas.drawText(text, textPos + (maxTextWidth/2 - textWidth/2 ), top, textPaint);
        }

        top = top + (int)innerPadding;

        arrowPaint.setAlpha(255);
        if(!isNumeric)
            if(!loop && selectedItem == items.length-1)
                arrowPaint.setAlpha(100);
        else
            if(!loop && selectedItem == max)
                arrowPaint.setAlpha(100);

        canvas.drawBitmap(rotateBitmap(arrow, 90), left, top, arrowPaint);
    }

    private void drawHorizontal(Canvas canvas) {
        int left;
        int top;
        left = getPaddingLeft();
        top = arrowPos;

        if(!loop && selectedItem == 0)
            arrowPaint.setAlpha(100);
        else
            arrowPaint.setAlpha(255);

        canvas.drawBitmap(rotateBitmap(arrow, 180), left, top, arrowPaint);

        if(!isNumeric) {
            left = left + (int)innerPadding + arrowWidth + (maxTextWidth/2 - textWidth[selectedItem]/2);
            canvas.drawText(items[selectedItem].toString(), (float) left, textPos, textPaint);
            left = left + textWidth[selectedItem] + (maxTextWidth/2 - textWidth[selectedItem]/2) + (int)innerPadding;
        }else {
            String text = String.valueOf(selectedItem);
            int textWidth = getTextWidth(text, textPaint);

            left = left + (int)innerPadding + arrowWidth + (maxTextWidth/2 - textWidth/2);
            canvas.drawText(text, (float) left, textPos, textPaint);
            left = left + textWidth + (maxTextWidth/2 - textWidth/2) + (int)innerPadding;
        }


        arrowPaint.setAlpha(255);
        if(!isNumeric)
            if(!loop && selectedItem == items.length-1)
                arrowPaint.setAlpha(100);
            else
            if(!loop && selectedItem == max)
                arrowPaint.setAlpha(100);

        canvas.drawBitmap(arrow, left, top, arrowPaint);
    }

    @Override
	public boolean onTouchEvent(MotionEvent event){

		if(orientation.equals("horizontal")){
			if(event.getX() < getWidth()/2) previousItem();
			else nextItem();
		}else{
			if(event.getY() < getHeight()/2) previousItem();
			else nextItem();
		}

		return super.onTouchEvent(event);
	}

	public int getSelectedIndex(){
		return selectedItem+1;
	}

	public String getSelectedItem(){
		return items[selectedItem].toString();
	}

	public int getNumItems(){
		return numItems;
	}

	public void nextItem(){
        if(!isNumeric) {
            if (selectedItem + 1 < items.length) {
                selectedItem++;
                notifyChange();
                invalidate();
            } else if (loop) {
                selectedItem = 0;
                notifyChange();
                invalidate();
            }
        }else{
            if (selectedItem + 1 <= max) {
                selectedItem++;
                notifyChange();
                invalidate();
            } else if (loop) {
                selectedItem = min;
                notifyChange();
                invalidate();
            }
        }
	}

    private void notifyChange() {
        if( listener != null ) listener.onChange();
    }

    public void previousItem(){
		if(selectedItem > 0){
			selectedItem--;
            notifyChange();
            invalidate();
		}else if(loop){
            if(!isNumeric)
			    selectedItem = items.length-1;
            else
                selectedItem = max;
            notifyChange();
            invalidate();
		}
	}

	public void selectItem(int i){
		selectedItem = i-1;
        notifyChange();
        invalidate();
	}

	public Rect getTextBounds(){
        if(isNumeric) {
            Rect textBounds = new Rect();
            String strToMeasure = String.valueOf(max);
            textPaint.getTextBounds(strToMeasure, 0, strToMeasure.length(), textBounds);
            return new Rect(0, 0, textBounds.width(), textBounds.height());
        }
        int maxWidth=0;
        int maxHeight=0;
        for (int i = 0; i < items.length; i++) {
            Rect textBounds = new Rect();
            textPaint.getTextBounds(items[i].toString(), 0, items[i].length(), textBounds);
            textWidth[i] = textBounds.width();
            maxWidth = Math.max(maxWidth, textBounds.width());
            maxHeight = Math.max(maxHeight, textBounds.height());
        }
        return new Rect(0, 0, maxWidth, maxHeight);
	}

    public int getTextWidth(String text, Paint paint){
        Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        return textBounds.width();
    }

	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap);
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, float deg){
		Matrix matrix = new Matrix();
		matrix.postRotate(deg);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

    public void setMax(int max) {
        this.max = max;
    }
}
