package com.ekalips.fancybuttonproj;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.zhanghai.android.materialprogressbar.IndeterminateProgressDrawable;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


public class FancyButton extends RelativeLayout {

    interface AnimationEndListener{
        void animationEnded();
    }

    private static final String TAG = FancyButton.class.getSimpleName();



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

    private Paint strokePaint,fillPaint;
    private MaterialProgressBar bar;
    private TextView view;

    /*
        0 - stroke
        1 - fill
        2 - stroke and fill
     */
    private int style;
    private boolean hideAfterCollapse = true;
    private boolean isExpanded = true;
    private AnimationEndListener listener;

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setListener(AnimationEndListener listener) {
        this.listener = listener;
    }

    private AnimatorListenerAdapter hideProgressBarListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            bar.setVisibility(INVISIBLE);
        }
    };

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    private int initPadd = 16;
    private int trueSixtee = 60;


    public void init(Context context, AttributeSet attrs){

        this.setClickable(true);
        this.setWillNotDraw(false);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FancyButton);
        String text = ta.getString(R.styleable.FancyButton_text);
        int strokeColor = ta.getColor(R.styleable.FancyButton_strokeColor,Color.BLACK);
        int fillColor = ta.getColor(R.styleable.FancyButton_fillColor, Color.TRANSPARENT);
        int textColor = ta.getColor(R.styleable.FancyButton_textColor, Color.BLACK);
        int progressColor = ta.getColor(R.styleable.FancyButton_progressColor, Color.BLACK);
        int strokeWidth = ta.getInt(R.styleable.FancyButton_strokeWidth, 4);
        boolean capsText = ta.getBoolean(R.styleable.FancyButton_capsText,true);
        if (capsText && text!=null)
            text = text.toUpperCase();
        String temp = ta.getString(R.styleable.FancyButton_btnStyle);
        if (temp!=null)
            style = Integer.parseInt(temp);
        hideAfterCollapse = ta.getBoolean(R.styleable.FancyButton_hideFillAfterCollapse,true);
        ta.recycle();

        view = new TextView(context, attrs, android.R.attr.borderlessButtonStyle);
        view.setClickable(false);
        view.setFocusable(false);
        view.setTextColor(textColor);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        view.setText(text);
        this.addView(view);

        bar = new MaterialProgressBar(context);
        RelativeLayout.LayoutParams barParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        barParams.addRule(CENTER_IN_PARENT,TRUE);
        bar.setLayoutParams(barParams);
        bar.setIndeterminate(true);

        // fixes pre-Lollipop progressBar indeterminateDrawable tinting
        IndeterminateProgressDrawable drawable = new IndeterminateProgressDrawable(context);
        drawable.setTint(progressColor);
        bar.setIndeterminateDrawable(drawable);

        bar.setVisibility(INVISIBLE);
        this.addView(bar);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeJoin(Paint.Join.ROUND);
        strokePaint.setStrokeWidth(strokeWidth);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(fillColor);
        fillPaint.setStyle(Paint.Style.FILL);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        initPadd = Utils.dp2px(context.getResources(),5);
        trueSixtee = Utils.dp2px(context.getResources(),20);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (state == State.normal) {
            if (style == 0)
                canvas.drawPath(Utils.composeRoundedRectPath(initPadd, initPadd, canvas.getWidth() - initPadd, canvas.getHeight() - initPadd,initPadd), strokePaint);
            else if (style == 1)
                canvas.drawPath(Utils.composeRoundedRectPath(initPadd, initPadd, canvas.getWidth() - initPadd, canvas.getHeight() - initPadd,initPadd), fillPaint);
            else if (style == 2){
                canvas.drawPath(Utils.composeRoundedRectPath(initPadd, initPadd, canvas.getWidth() - initPadd, canvas.getHeight() - initPadd,initPadd), fillPaint);
                canvas.drawPath(Utils.composeRoundedRectPath(initPadd, initPadd, canvas.getWidth() - initPadd, canvas.getHeight() - initPadd,initPadd), strokePaint);
            }
            destLeft = (this.getRight()-this.getLeft())/2 - trueSixtee;
            destRight = (this.getRight()-this.getLeft())/2 + trueSixtee;
            destTop = (this.getBottom()-this.getTop())/2 - trueSixtee;
            destBot = (this.getBottom()-this.getTop())/2 + trueSixtee;
            circleR = Math.abs(destLeft-destRight);
            ObjectAnimator animator = ObjectAnimator.ofFloat(bar,"alpha",1,0);
            animator.addListener(hideProgressBarListener);
            animator.setDuration(0);
            animator.start();
        }
        else if (state == State.shrinked){
            if (!hideAfterCollapse){
                if (style == 1 || style == 2)
                    canvas.drawCircle(destRight-((destRight-destLeft)/2), destBot-((destBot-destTop)/2), circleR/2, fillPaint);

            }
            ObjectAnimator animator = ObjectAnimator.ofFloat(bar,"alpha",0,1);
            animator.setDuration(0);
            animator.start();
            bar.setVisibility(VISIBLE);
        }
        else {
            if (state == State.srink)
                if (style == 0)
                    canvas.drawPath(Utils.composeRoundedRectPath((float)(0 + nowPadW), (float) (0 + nowPadH), (float) (canvas.getWidth() - nowPadW), (float) (canvas.getHeight() - nowPadH), (float) nowRad), strokePaint);
                else if (style == 1)
                    canvas.drawPath(Utils.composeRoundedRectPath((float)(0 + nowPadW), (float) (0 + nowPadH), (float) (canvas.getWidth() - nowPadW), (float) (canvas.getHeight() - nowPadH), (float) nowRad), fillPaint);
                else if (style == 2)
                {
                    canvas.drawPath(Utils.composeRoundedRectPath((float)(0 + nowPadW), (float) (0 + nowPadH), (float) (canvas.getWidth() - nowPadW), (float) (canvas.getHeight() - nowPadH), (float) nowRad), fillPaint);
                    canvas.drawPath(Utils.composeRoundedRectPath((float)(0 + nowPadW), (float) (0 + nowPadH), (float) (canvas.getWidth() - nowPadW), (float) (canvas.getHeight() - nowPadH), (float) nowRad), strokePaint);
                }
            if (state == State.back) {
                if (style == 0)
                    canvas.drawPath(Utils.composeRoundedRectPath((float) (0 + nowPadW), (float) (0 + nowPadH), (float) (canvas.getWidth() - nowPadW), (float) (canvas.getHeight() - nowPadH), (float) nowRad), strokePaint);
                else if (style == 1)
                    canvas.drawPath(Utils.composeRoundedRectPath((float) (0 + nowPadW), (float) (0 + nowPadH), (float) (canvas.getWidth() - nowPadW), (float) (canvas.getHeight() - nowPadH), (float) nowRad), fillPaint);
                else if (style == 2){
                    canvas.drawPath(Utils.composeRoundedRectPath((float) (0 + nowPadW), (float) (0 + nowPadH), (float) (canvas.getWidth() - nowPadW), (float) (canvas.getHeight() - nowPadH), (float) nowRad), fillPaint);
                    canvas.drawPath(Utils.composeRoundedRectPath((float) (0 + nowPadW), (float) (0 + nowPadH), (float) (canvas.getWidth() - nowPadW), (float) (canvas.getHeight() - nowPadH), (float) nowRad), strokePaint);
                }
                if (bar.getVisibility() != INVISIBLE)
                    bar.setVisibility(INVISIBLE);
            }
        }
    }


    private double nowPadW = initPadd;
    private double nowPadH = initPadd;
    private double nowRad = initPadd;
    private float destLeft,destTop,destRight,destBot, circleR;
    private State state = FancyButton.State.normal;


    public void expand() {
        if (state == State.shrinked){
            state = State.back;
            ValueAnimator animator = ValueAnimator.ofInt(0,255);
            animator.setDuration(200);

            final float addWVal = (float) Math.abs(0-nowPadW + initPadd)/15f;
            final float addHVal = (float) Math.abs(0-nowPadH + initPadd)/15f;

            nowRad = circleR;
            final float addRad = Math.abs(circleR-10f)/15f;
            final int[] i = {0};
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FancyButton.this.invalidate();
                    nowPadH-=addHVal;
                    nowPadW-=addWVal;
                    nowRad-=addRad;
                    if (hideAfterCollapse) {
                        strokePaint.setAlpha((Integer) valueAnimator.getAnimatedValue());
                        if (fillPaint.getColor()!=Color.TRANSPARENT)
                            fillPaint.setAlpha((Integer) valueAnimator.getAnimatedValue());
                    }
                    view.setAlpha((int)valueAnimator.getAnimatedValue()/255f);
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    state= State.normal;
                    if (listener!=null)
                        listener.animationEnded();
                    isExpanded = true;
                }
            });
            animator.start();
        }
    }

    public void collapse() {
        if (state == State.normal){
            state= State.srink;
            nowPadW = initPadd;
            nowPadH = initPadd;
            nowRad = initPadd;
            ValueAnimator animator = ValueAnimator.ofInt(255,0);
            animator.setDuration(200);

            final float addWVal = Math.abs(initPadd - destLeft)/15f;
            final float addHVal = Math.abs(initPadd - destTop )/15f;
            final float addRad = circleR/15f;
            final int[] i = {0};



            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FancyButton.this.invalidate();
                    nowPadH+=addHVal;
                    nowPadW+=addWVal;
                    nowRad+=addRad;
                    if (hideAfterCollapse) {
                        strokePaint.setAlpha((Integer) valueAnimator.getAnimatedValue());
                        if (fillPaint.getColor()!=Color.TRANSPARENT)
                            fillPaint.setAlpha((Integer) valueAnimator.getAnimatedValue());
                    }
                    view.setAlpha((int)valueAnimator.getAnimatedValue()/255f);
                    i[0]++;
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    state= State.shrinked;
                    if (listener!=null)
                        listener.animationEnded();
                    isExpanded = false;
                }
            });
            animator.start();
        }
    }

    enum State{normal,srink,back,shrinked}
}
