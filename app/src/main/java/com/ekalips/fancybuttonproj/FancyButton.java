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
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class FancyButton extends RelativeLayout {

    interface AnimationEndListener{
        void animationEnded();
    }

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

    private Paint strokePaint,fillPaint;
    private ProgressBar bar;
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

    public void init(Context context, AttributeSet attrs){

        this.setClickable(true);
        this.setWillNotDraw(false);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FancyButton);
        String text = ta.getString(R.styleable.FancyButton_text);
        int strokeColor = ta.getColor(R.styleable.FancyButton_strokeColor,Color.BLACK);
        int fillColor = ta.getColor(R.styleable.FancyButton_fillColor, Color.TRANSPARENT);
        int textColor = ta.getColor(R.styleable.FancyButton_textColor, Color.BLACK);
        int progressColor = ta.getColor(R.styleable.FancyButton_progressColor, strokeColor);
        int strokeWidth = ta.getInt(R.styleable.FancyButton_strokeWidth, 4);

        style = Integer.parseInt(ta.getString(R.styleable.FancyButton_btnStyle));
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

        bar = new ProgressBar(context);
        RelativeLayout.LayoutParams barParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        barParams.addRule(CENTER_IN_PARENT,TRUE);
        bar.setLayoutParams(barParams);
        bar.setIndeterminate(true);
//        bar.setIndeterminateTintList(ColorStateList.valueOf(Color.BLACK));

        // fixes pre-Lollipop progressBar indeterminateDrawable tinting
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(bar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, progressColor);
            bar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            bar.getIndeterminateDrawable().setColorFilter(progressColor, PorterDuff.Mode.SRC_IN);
        }

//        bar.setProgressTintList(ColorStateList.valueOf(Color.BLACK));
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
        Log.d(TAG, "init: " + metrics.density);


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // TODO Auto-generated method stub
        super.onLayout(changed, left, top, right, bottom);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



//        Log.d(TAG, "onDraw: " + this.getTransitionName());
        if (state == State.normal) {
//            Path path = FancyButton.composeRoundedRectPath(new RectF(this.getLeft() + 16, this.getTop() + 16, canvas.getWidth() - 16, canvas.getHeight() - 16),16);
            if (style == 0)
                canvas.drawPath(Utils.composeRoundedRectPath(16, 16, canvas.getWidth() - 16, canvas.getHeight() - 16,16), strokePaint);
            else if (style == 1)
                canvas.drawPath(Utils.composeRoundedRectPath(16, 16, canvas.getWidth() - 16, canvas.getHeight() - 16,16), fillPaint);
            else if (style == 2){
                canvas.drawPath(Utils.composeRoundedRectPath(16, 16, canvas.getWidth() - 16, canvas.getHeight() - 16,16), fillPaint);
                canvas.drawPath(Utils.composeRoundedRectPath(16, 16, canvas.getWidth() - 16, canvas.getHeight() - 16,16), strokePaint);
            }
            destLeft = (this.getRight()-this.getLeft())/2 - 60;
            destRight = (this.getRight()-this.getLeft())/2 + 60;
            destTop = (this.getBottom()-this.getTop())/2 - 60;
            destBot = (this.getBottom()-this.getTop())/2 + 60;
            circleR = Math.abs(destLeft-destRight);
            ObjectAnimator animator = ObjectAnimator.ofFloat(bar,"alpha",1,0);
            animator.addListener(hideProgressBarListener);
            animator.setDuration(0);
            animator.start();

//            Log.d(TAG, "onDraw: " + destLeft + " r: " + destRight + " t: " + destTop + " b: " + destBot + " r: " + circleR);
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
//        Log.d(TAG, "onDraw: " + nowRad);
    }


    private int initPadd = 16;
    private double nowPadW = 16;
    private double nowPadH = 16;
    private double nowRad = 10;
    private float destLeft,destTop,destRight,destBot, circleR;
    private State state = FancyButton.State.normal;


    public void expand() {
        if (state == State.shrinked){
            state = State.back;
            ValueAnimator animator = ValueAnimator.ofInt(0,255);
            animator.setDuration(200);

            final float addWVal = (float) Math.abs(0-nowPadW + 16)/15f;
            final float addHVal = (float) Math.abs(0-nowPadH + 16)/15f;

            nowRad = circleR;
            final float addRad = Math.abs(circleR-10f)/15f;
//            Log.d(TAG, "onClick: " + addWVal + "  add:" + addRad);
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
            nowPadW = 16;
            nowPadH = 16;
            nowRad = 10;
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
                        fillPaint.setAlpha((Integer) valueAnimator.getAnimatedValue());
                    }
                    view.setAlpha((int)valueAnimator.getAnimatedValue()/255f);
//                    Log.d(TAG, "onAnimationUpdate: " + strokePaint.getAlpha());
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
