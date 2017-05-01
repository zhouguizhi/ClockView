package com.simple;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by Adminis on 2017/5/1.
 */
public class ClockView extends View {
    private static final String TAG = "ClockView";
    private Paint mPaint;
    private int widhth = 200;//控件的宽度
    private int height = 200;//控件的高度
    private int padding = 5;
    private Calendar mCalendar;
    private int mHour;//小时
    private int mMinuate;//分钟
    private int mSecond;//秒
    private float mDegrees ;//因为圆是360度 我们有12个刻度 所以就是360/12
    private int mHourLineLen;//时指针 线
    private int mMinuateLine;//分钟 线
    private int mSecondLine ;//表钟线
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };
    public ClockView(Context context) {
        this(context, null);
    }
    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widhth, height);
        mHourLineLen = (int) (widhth/2*0.6);
        mMinuateLine = (int) (widhth/2*0.7);
        mSecondLine = (int) (widhth/2*0.8);
    }
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#666666"));
        mPaint.setAntiAlias(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        drawCircle(canvas);
        drawScale(canvas);
        canvasCenterCircle(canvas);
        drawPointer(canvas);
        drawStr(canvas);
        mHandler.sendEmptyMessage(1);
    }
    /**
     * 绘制文字
     * @param canvas
     */
    private void drawStr(Canvas canvas) {
        mPaint.setTextSize(24);
        StringBuffer sb =  new StringBuffer();
        if(mHour<10){
            sb.append("0").append(String.valueOf(mHour)).append(":");
        }else{
            sb.append(String.valueOf(mHour)).append(":");
        }
        if(mMinuate<10){
            sb.append("0").append(String.valueOf(mMinuate)).append(":");
        }else{
            sb.append(String.valueOf(mMinuate)).append(":");
        }
        if(mSecond<10){
            sb.append("0").append(String.valueOf(mSecond));
        }else{
            sb.append(String.valueOf(mSecond));
        }
        String str = sb.toString();
        int strW = (int) mPaint.measureText(str);
        canvas.drawText(str, widhth / 2 - strW / 2, widhth / 2 + 30,mPaint);
    }
    /**
     * 在 圆中心绘制一个点
     * @param canvas
     */
    private void canvasCenterCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(widhth / 2, height / 2, 5, mPaint);
    }


    /**
     * 绘制圆
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(widhth / 2, height / 2, widhth / 2 - padding, mPaint);
    }
    /**
     * 绘制刻度
     * @param canvas
     */
    private void drawScale(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        for(int i=0;i<12;i++) {
            if (i % 3 == 0) {//  12  3  6  9对应的线长点
                canvas.drawLine(widhth / 2 - padding, padding, widhth / 2 - padding, padding + 4 + 15, mPaint);
            }else{
                canvas.drawLine(widhth / 2 - padding, padding, widhth / 2 - padding, padding + 4 + 8, mPaint);
            }
            canvas.rotate(30, widhth / 2, widhth / 2);
        }
    }

    /**
     * 绘制时  分 表 指针
     * @param canvas
     */
    private void drawPointer(Canvas canvas) {
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR);
        mMinuate = mCalendar.get(Calendar.MINUTE);
        mSecond = mCalendar.get(Calendar.SECOND);
        //小时的旋转度
        mDegrees = mHour*30+mMinuate/2;
        mPaint.setColor(Color.BLACK);
        canvas.save();
        canvas.rotate(mDegrees, widhth / 2, widhth / 2);
        canvas.drawLine(widhth / 2, height / 2, widhth / 2, widhth / 2 - mHourLineLen, mPaint);
        canvas.restore();
        //分钟
        mPaint.setColor(Color.parseColor("#666666"));
        mPaint.setStrokeWidth(5);
        mDegrees = mMinuate*6+mSecond/10;
        canvas.save();
        canvas.rotate(mDegrees, widhth / 2, widhth / 2);
        canvas.drawLine(widhth / 2, height / 2, widhth / 2, widhth / 2 - mMinuateLine, mPaint);
        canvas.restore();
        //绘制表针
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.parseColor("#666666"));
        mDegrees = mSecond*6;
        canvas.save();
        canvas.rotate(mDegrees, widhth / 2, widhth / 2);
        canvas.drawLine(widhth / 2, height / 2, widhth / 2, widhth / 2 - mSecondLine, mPaint);
        canvas.restore();
    }
}
