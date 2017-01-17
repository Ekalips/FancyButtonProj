package com.wldev.fancybuttonproj;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.Sampler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class FancyButton extends RelativeLayout implements View.OnClickListener{
    private static final String TAG = FancyButton.class.getSimpleName();

    public FancyButton(Context context) {
        super(context);
        init(context,null);
    }

    public FancyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public FancyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FancyButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    Paint paint;
    ProgressBar bar;
    TextView view;
    public void init(Context context,AttributeSet attrs){

        this.setClickable(true);
        this.setOnClickListener(this);
        this.setWillNotDraw(false);
        int[] attrsArray = new int[] {
                android.R.attr.text, // 0
                android.R.attr.layout_width, // 1
                android.R.attr.layout_height // 2
        };

        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        String text = ta.getString(0);
        Log.d(TAG, "init: " + text);
        ta.recycle();

        view = new TextView(context, attrs, android.R.attr.borderlessButtonStyle);
        view.setOnClickListener(this);
        view.setClickable(true);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        view.setText(text);
        this.addView(view);

        bar = new ProgressBar(context);
        RelativeLayout.LayoutParams barParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        barParams.addRule(CENTER_IN_PARENT,TRUE);
        bar.setLayoutParams(barParams);
        bar.setIndeterminate(true);
        bar.setIndeterminateTintList(ColorStateList.valueOf(Color.BLACK));
//        bar.setProgressTintList(ColorStateList.valueOf(Color.BLACK));
        bar.setVisibility(INVISIBLE);
        this.addView(bar);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // TODO Auto-generated method stub
        super.onLayout(changed, left, top, right, bottom);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (state == State.normal) {
            canvas.drawRoundRect(this.getLeft() + 16, this.getTop() + 16, canvas.getWidth() - 16, canvas.getHeight() - 16, 10, 10, paint);
            destLeft = (this.getRight()-this.getLeft())/2 - 60;
            destRight = (this.getRight()-this.getLeft())/2 + 60;
            destTop = (this.getBottom()-this.getTop())/2 - 60;
            destBot = (this.getBottom()-this.getTop())/2 + 60;
            circleR = Math.abs(destLeft-destRight)/2;
            ObjectAnimator animator = ObjectAnimator.ofFloat(bar,"alpha",1,0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    bar.setVisibility(INVISIBLE);
                }
            });
            animator.start();
        }
        else if (state == State.shrinked){
//            canvas.drawRoundRect(destLeft, destTop,destRight,destBot, circleR,circleR, paint);
            ObjectAnimator animator = ObjectAnimator.ofFloat(bar,"alpha",0,1);
            animator.start();
            bar.setVisibility(VISIBLE);
        }
        else {
            if (state == State.srink)
                canvas.drawRoundRect((float) (this.getLeft() + nowPadW), (float) (this.getTop() + nowPadH), (float) (canvas.getWidth() - nowPadW), (float) (canvas.getHeight() - nowPadH), (float) nowRad, (float) nowRad, paint);
            if (state == State.back) {
                canvas.drawRoundRect((float) (this.getLeft() + nowPadW), (float) (this.getTop() + nowPadH), (float) (canvas.getWidth() - nowPadW), (float) (canvas.getHeight() - nowPadH), (float) nowRad, (float) nowRad, paint);
                if (bar.getVisibility() != INVISIBLE)
                    bar.setVisibility(INVISIBLE);
            }
        }
//        Log.d(TAG, "onDraw: destL: " + destLeft + " nowLeft: " + this.getLeft()+nowPadW);
//        Log.d(TAG, "onDraw: " + nowRad);
    }


    int initPadd = 16;
    double nowPadW = 16;
    double nowPadH = 16;
    double nowRad = 10;
    float destLeft,destTop,destRight,destBot, circleR;
    State state = FancyButton.State.normal;
    @Override
    public void onClick(final View view) {
        Log.d(TAG, "onClick: ");

        if (state == State.normal){
            state=State.srink;
            nowPadW = 16;
            nowPadH = 16;
            nowRad = 10;
            ValueAnimator animator = ValueAnimator.ofInt(255,0);
            animator.setDuration(200);

            final float addWVal = Math.abs(this.getLeft() - destLeft + initPadd)/15f;
            final float addHVal = Math.abs(this.getTop() - destTop + initPadd)/15f;
            final float addRad = circleR/15;
            final int[] i = {0};



            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FancyButton.this.invalidate();
                    nowPadH+=addHVal;
                    nowPadW+=addWVal;
                    nowRad+=addRad;
                    paint.setAlpha((Integer) valueAnimator.getAnimatedValue());
                    view.setAlpha((int)valueAnimator.getAnimatedValue()/255f);
                    Log.d(TAG, "onAnimationUpdate: " + paint.getAlpha());
                    i[0]++;
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    state=State.shrinked;
                    Log.d(TAG, "onAnimationEnd: " + i[0]);
                }
            });
            animator.start();
        }
        else if (state == State.shrinked){
            state = State.back;
            ValueAnimator animator = ValueAnimator.ofInt(0,255);
            animator.setDuration(200);

            final float addWVal = (float) Math.abs(this.getLeft()-nowPadW + 16)/15f;
            final float addHVal = (float) Math.abs(this.getTop()-nowPadH + 16)/15f;

            nowRad = circleR;
            final float addRad = Math.abs(circleR-10f)/15f;
            Log.d(TAG, "onClick: " + addWVal + "  add:" + addRad);
            final int[] i = {0};
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FancyButton.this.invalidate();
                    nowPadH-=addHVal;
                    nowPadW-=addWVal;
                    nowRad-=addRad;
                    paint.setAlpha((Integer) valueAnimator.getAnimatedValue());
                    view.setAlpha((int)valueAnimator.getAnimatedValue()/255f);
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    state=State.normal;
                    Log.d(TAG, "onAnimationEnd: " + nowRad);
//                    Log.d(TAG, "onAnimationEnd: " + i[0]);
                }
            });
            animator.start();
        }

    }

    enum State{normal,srink,back,shrinked}
}
