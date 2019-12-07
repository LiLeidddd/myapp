package com.myapp.lzj.myapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class MyView extends View {
    private MyThread myThread;

    private Paint paint;//画笔

    private RectF rectF=new RectF(150,150,380,380);
    private int sweepAngle=0;//弧的当前度数
    private int sweepAngleAdd=20;//弧的每次增加度数
    private Random random =new Random();
    private boolean running=true;//控制循环

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint=new Paint();
        paint.setTextSize(60);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        if(widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
            
        }else {
            width = (int)(getPaddingLeft()+getPaddingRight()+rectF.width()*2);
        }

        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;

        }else {
            height = (int)(getPaddingTop()+getPaddingBottom()+rectF.height()*2);
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("Myview","onDraw");
        if(myThread==null) {
            myThread = new MyThread();
            myThread.start();
        }else{
            canvas.drawArc(rectF,0,sweepAngle,true,paint);
            //第一个参数构造圆形的左上右下边界，四个变量
            //第二个参数是开始的角度
//            第三个参数是最终的角度
            //第四个参数 true表示画出半径，false表示不使用中心即即画弧
            //paint 表示画笔
        }
    }

    private class MyThread extends Thread{

        @Override
        public void run() {
            while (running){
                logic();
                postInvalidate();;//重新绘制，会调用onDraw；
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void  logic(){
        sweepAngle+=sweepAngleAdd;//每次增加弧度

        //随机设置画笔的颜色
        int r=random.nextInt(255);
        int g=random.nextInt(255);
        int b=random.nextInt(255);
        paint.setARGB(255,r,g,b);//第一个参数表示透明度

        if(sweepAngle>=360) {
            sweepAngle = 0;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        running=false;//销毁View的时候设置成false，退出无限循环线程；
        super.onDetachedFromWindow();
    }
}
