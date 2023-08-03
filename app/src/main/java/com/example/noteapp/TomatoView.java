package com.example.noteapp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
public class TomatoView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint timePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mColor = Color.parseColor("#D1D1D1");
    private int centerX;
    private int centerY;
    private int radius;
    private RectF mRectF = new RectF();
    public static final float START_ANGLE = -90;
    public static final int MAX_TIME = 60;
    private float sweepVelocity = 0;
    private String textTime = "00:00";
    //分钟
    private int time;
    //倒计时
    private int countdownTime;
    private float touchX;
    private float touchY;
    private float offsetY;

    public int status = STATUS_STOPPED;

    public final static int STATUS_STARTED = 1;//状态1为开始
    public final static int STATUS_STOPPED = 2;//停止
    public final static int STATUS_PAUSED = 3;

    private CountDownTimer countDownTimer;
    private ValueAnimator valueAnimator;

    public TomatoView(Context context) {
        super(context);
    }

    public TomatoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TomatoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //获取位置
    public static float dpToPixel(float dp) {//获取屏幕大小
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();//与手机型号有关，获取的有可能不是正确尺寸
        return dp * metrics.density;//px = dp * density
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        centerX = width / 2;//x轴上在屏幕中间
        centerY = height / 2;
        radius = (int) dpToPixel(120);
        setMeasuredDimension(width, height);

    }
    //通过位置画圆
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        //白圆
        canvas.save();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dpToPixel(5));
        canvas.drawCircle(centerX, centerY, radius, mPaint);
        canvas.restore();
        //灰圆
        canvas.save();
        mPaint.setColor(mColor);
        canvas.drawArc(mRectF, START_ANGLE, 360 * sweepVelocity, false, mPaint);
        canvas.restore();
        //时间
        canvas.save();
        timePaint.setColor(Color.WHITE);
        timePaint.setStyle(Paint.Style.FILL);
        timePaint.setTextSize(dpToPixel(40));
        canvas.drawText(textTime, centerX - timePaint.measureText(textTime) / 2,
                centerY - (timePaint.ascent() + timePaint.descent()) / 2, timePaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (status == STATUS_STARTED) {
            return true;
        }
        float x = event.getX();
        float y = event.getY();
        boolean isContained = isContained(x, y);//计算位置
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN://计时下的位置变化
                if (isContained) {
                    touchX = x;
                    touchY = y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isContained) {
                    float offsetX = x - touchX;
                    offsetY = y - touchY;
                    time = (int) (offsetY / 2 / radius * MAX_TIME);//时间int类型,转换后有精度问题
                    if (time <= 0) {
                        time = 0;
                    }
                    textTime = formatTime(time);
                    countdownTime = time * 60;//分钟*60化为秒
                    invalidate();
                }
                break;
        }
        return true;
    }
    //计算位置
    private boolean isContained(float x, float y) {
        if (Math.sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY)) > radius) {
            return false;
        } else {
            return true;
        }
    }
    //滑动得到的分钟
    private String formatTime(int time) {
        StringBuilder sb = new StringBuilder();
        if (time < 10) {
            sb.append("0" + time + ":00");
        } else {
            sb.append(time + ":00");
        }
        return sb.toString();
    }
    //转换分：秒
    private String formatCountdownTime(int countdownTime) {
        StringBuilder sb = new StringBuilder();
        int minute = countdownTime / 60;
        int second = countdownTime - 60 * minute;
        if (minute < 10) {
            sb.append("0" + minute + ":");
        } else {
            sb.append(minute + ":");
        }
        if (second < 10) {
            sb.append("0" + second);
        } else {
            sb.append(second);
        }
        return sb.toString();
    }

    /**
     * 计时开始
     */
    public void start() {
        status = STATUS_STARTED;
//countdownTime上面将其化为秒了
        valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.setDuration(countdownTime * 1000);//设置动画时长，单位是毫秒
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            sweepVelocity = (float) animation.getAnimatedValue();
            mColor = Color.parseColor("#D1D1D1");
            invalidate();
        });
        valueAnimator.start();
                        //倒计时（总时间，每次减去的时间）//单位毫秒
        countDownTimer = new CountDownTimer(countdownTime * 1000 + 998, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTime = (countdownTime * 1000 - 998) / 1000;//去掉倒计时的显示有问题
                textTime = formatCountdownTime(countdownTime);//转换分：秒
                invalidate();
            }

            @Override
            public void onFinish() {
                mColor = Color.BLACK;
                sweepVelocity = 0;
                status = STATUS_STOPPED;
                invalidate();
            }
        }.start();
    }

    /**
     * 计时结束
     */
    public void stop() {
        sweepVelocity = 0;
        countdownTime = 0;
        time = 0;
        textTime = "00:00";
        status = STATUS_STOPPED;
        invalidate();

        valueAnimator.cancel();
        countDownTimer.cancel();
    }
}
