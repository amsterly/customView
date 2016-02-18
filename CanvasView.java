package my.test.com.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by Administrator on 2016/2/1.
 */
public class CanvasView extends View {
    private Paint mPaint;
    public  Method mMethod;
    private Path mPath;
    //000000000

    // 我们将使用android.graphics.Movie类来绘制GIF。
    private Movie mMovie;
    // 记录当前播放的位置。
    private long mMovieStart;

    enum Method
    {
        id_drawtext(0),id_measuretext(1),id_drawshape(2),id_drawbyhand(3),id_drawClock(4),id_drawGif(5),gone(9);
        private int value = 0;

        private Method(int value) {    //    必须是private的，否则编译错误
            this.value = value;
        }
        public static Method valueOf(int value) {    //    手写的从int到enum的转换函数
            switch (value) {
                case 0:
                    return id_drawtext;
                case 1:
                    return id_measuretext;
                case 2:
                    return id_drawshape;
                case 3:
                    return id_drawbyhand;
                default:
                    return gone;
            }
        }
        public int value() {
            return this.value;
        }

        }

    public CanvasView(Context context) {
        super(context);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mMethod=Method.gone;
        mPath = new Path();
        // 获取res/drawable目录下的GIF文件的输入流。
        InputStream input = context.getResources().openRawResource(R.drawable.happybear);
        // 从输入流中读入数据，并创建一个Movie对象。
        mMovie = Movie.decodeStream(input);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //  mPath.reset();  可以调用此方法清空所有数据。
                mPath.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(), event.getY());
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 如果当前尺寸或者位置发生了变化，则重新初始化各个变量。
        // 通过阅读View类的源码可知，在onSizeChanged、onLayout方法被调用的时候，
        //       就可以通过getWidth()和getHeight()来获取实际宽高了，具体请参阅前面两章博文。
        int min = Math.min(getWidth(), getHeight());
        radius = min / 2 - 20;
        hourPointLen = (int) (radius * 0.45);
        minutePointLen = (int) (radius * 0.7);
        secondPointLen = (int) (radius * 0.85);
        bigMarkLen = (int) (radius * 0.045);
        smallMarkLen = (int) (radius * 0.025);
        // 字体的大小随着半径的大小而变化。
        mPaint.setTextSize(radius / 8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("CanvasView", "onDraw");
        switch (mMethod)
        {
            case id_drawbyhand:
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(Color.RED);
                mPaint.setStrokeWidth(20);
                canvas.drawPath(mPath, mPaint);break;
            case id_drawtext:drawText("FFFFF",20,200,canvas);break;
            case id_measuretext:break;
            case id_drawshape:
                drawShape(canvas);break;
            case id_drawClock:
                drawClock(canvas);
            case id_drawGif:
                drawGIF(canvas);
                break;
            default:break;
        }
    }

    private void drawGIF(Canvas canvas) {
        // 获取当前时间。
        long now = android.os.SystemClock.uptimeMillis();
        if (mMovieStart == 0) {   // first time
            mMovieStart = now;
        }
        if (mMovie != null) {
            // 获取GIF文件的总时长。
            int dur = mMovie.duration();
            if (dur == 0) {
                dur = 1000;
            }
            // 计算当前需要播放的位置。
            int relTime = (int)((now - mMovieStart) % dur);
            // 将GIF调整到relTime所对应的帧上。
            mMovie.setTime(relTime);
            // 将当前帧绘制到canvas的(0,0)坐标上。
            mMovie.draw(canvas, 0, 0);
            // 绘制完后，调用下面的方法，触发下一次绘制。
            invalidate();
        }
    }

    public void cleanPath()
    {
        mPath.reset();
        invalidate();
    }
    public void drawText(String s, int x, int y,Canvas canvas) {
        // 使用measureText方法测量字符串宽度
        Paint p = new Paint();
        p.setTextSize(80f);
        canvas.drawText(s, x, y, p);
        canvas.drawLine(x, y, x + p.measureText(s), y, p);
        // 使用getTextBounds方法测量字符串宽度
        y += 100;
        canvas.drawText(s, x, y, p);
        Rect bounds = new Rect();
        // 字符串的宽高会为放到bounds里面。
        p.getTextBounds(s, 0, s.length(), bounds);
        canvas.drawLine(x, y, x + bounds.width(), y, p);
//        invalidate();
    }
    public void drawShape(Canvas canvas)
    {
        // 定义一个画笔对象。
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        // 将整个canvas染成蓝色。也可以调用canvas.drawARGB(100, 255, 0, 0);来设置具体的颜色值。
        canvas.drawColor(Color.BLUE);
        // 使用画笔p在canvas上绘画出一条直线，直线的起点为(10,10)，结束点为(10,40)。
        canvas.drawLine(10, 10, 10, 40, p);
        // 使用画笔p在canvas上绘画出一个矩形，矩形的左上角坐标为(20,20)，右下角坐标为(40,50)。
        canvas.drawRect(new Rect(20,20,40,50), p);
        // 使用画笔p在canvas上绘画出一个圆形，圆心坐标为(150,150)，半径为60。
        canvas.drawCircle(150, 150, 60, p);
        // 使用画笔p在canvas上绘画出一个圆角矩形，矩形左上角坐标为(80,220)，右下角坐标为(210,280)，x和y方向上的圆角半径为(10,10)。
        canvas.drawRoundRect(new RectF(80,520,510,280), 10, 10, p);
        // 我们提供一个RectF对象作为弧形的外切矩形，系统就知道弧形的位置和尺寸了。
        // 下面的代码是从-90度开始画，画一个300度的弧形。
        // 我们常规认为12点方向是0度，但在这里默认3点方向是0度，因而要从-90度开始画弧线。
        // 这个弧形运行时的效果，请看上面示意图中的第一个，后面三个以此类推。
        canvas.drawArc(new RectF(100,400,250,550), -90, 300, true, p);
        // 绘制一个弧形，只绘制弧线，不填充内容。
        p.setStyle(Paint.Style.STROKE);
        // 设置线的粗（宽度）为5，线宽对Paint.Style.FILL无效、对文本字体无效。
        p.setStrokeWidth(5);
        canvas.drawArc(new RectF(300,500,450,650), -90, 300, true, p);
        // 绘制一个弧形，但useCenter字段为false。具体效果请看上面示意图中的第三个。
        canvas.drawArc(new RectF(500,500,650,650), -90, 300, false, p);
        // 绘制一个弧形，但useCenter字段为false。
        p.setStyle(Paint.Style.FILL);
        canvas.drawArc(new RectF(700,500,850,650), -90, 300, false, p);

        // 通过修改线的宽度，这样弧形就变粗了，这个特性可以用来模仿ProgressBar。
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        // 画一个非圆角弧形
        canvas.drawArc(new RectF(100,700,250,850), -90, 300, false, p);
        // 画一个圆角弧形
        p.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(new RectF(500,700,650,850), -90, 300, false, p);
    }
    private void drawLine(float startX, float startY, float stopX, float stopY,
                          Paint paint, Canvas canvas, int type) {
        Calendar curTime = Calendar.getInstance();
        canvas.save();
        float rotate = 0, num = curTime.get(type);
        switch (type) {
            case Calendar.HOUR:
                float offsetDegree = (curTime.get(Calendar.MINUTE) / 10.0f - 1) * 6;
                rotate = (num == 12 ? 0 : num * 30 + offsetDegree);
                break;
            case Calendar.MINUTE:
            case Calendar.SECOND:
                rotate = (num == 0 ? 0 : num * 6);
                break;
        }
        canvas.rotate(rotate);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        canvas.restore();
    }
    // 表盘的半径
    private int radius;
    // 时分秒三个指针的长度
    private int hourPointLen;
    private int minutePointLen;
    private int secondPointLen;
    // 表盘上的大、小刻度线的长度
    private int smallMarkLen;
    private int bigMarkLen;
    private Rect rect = new Rect();
    public void drawClock(Canvas canvas) {
        mPaint.setStrokeWidth(1);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextAlign(Paint.Align.CENTER);

        canvas.save();
        // 将绘点移动到View的中心。
        canvas.translate(getWidth() / 2, getHeight() / 2);
        // 绘制表盘，其实就是一个圆形。
        canvas.drawCircle(0, 0, radius, mPaint);
        // 绘制表的刻度
        int count = 60, degree, lineLen;
        for (int i = 0; i < count; i++) {
            degree = 360 / count * i;
            lineLen = (i % 5 == 0 ? bigMarkLen : smallMarkLen);
            canvas.save();
            canvas.rotate(degree);
            canvas.translate(0, -radius);
            canvas.drawLine(0, 0, 0, lineLen, mPaint);
            // 绘制表盘上的数字。
            if (i % 5 == 0) {
                int numberInt = (i / 5 == 0 ? 12 : i / 5);
                String numberStr = String.valueOf(numberInt);
                // 获取字体的尺寸，因为下面会用到字体的高度。
                mPaint.getTextBounds(numberStr, 0, numberStr.length(), rect);
                int xOffsetRate = 0;
                float yOffsetRate = 0;
                if (numberInt != 6 && numberInt != 12) {    // 1~5或者7~11
                    xOffsetRate = (numberInt < 6 ? 1 : -1);
                }
                if (numberInt != 3 && numberInt != 9) {    // 10~2或者4~8
                    yOffsetRate = (numberInt > 9 || numberInt < 3 ? 1 : -0.25f);
                }
                // 调整绘点的位置，以便稍后绘制数字时，数字不会跑偏。
                canvas.translate(xOffsetRate * (rect.width() / 2),
                        2 * lineLen + yOffsetRate * (rect.height() / 2));
                // 然后将绘点旋转回去，不然数字也会旋转。
                canvas.rotate(-degree);
                canvas.drawText(numberStr, 0, 0, mPaint);
            }
            canvas.restore();
        }
        // 绘制指针尾部的圆点。
        canvas.drawCircle(0, 0, 7, mPaint);
        // 绘制时针、分针、秒针。
        drawLine(0, 10, 0, -hourPointLen, mPaint, canvas, Calendar.HOUR);
        drawLine(0, 10, 0, -minutePointLen, mPaint, canvas, Calendar.MINUTE);
        drawLine(0, 10, 0, -secondPointLen, mPaint, canvas, Calendar.SECOND);
        // 1秒后进行重绘。
        postInvalidateDelayed(1000);
        canvas.restore();
    }
//    public void drawText2()
//    {
//        Paint p=new Paint();
//        // 修改画笔的颜色，下面使用的是android.graphics.Color类。
//        mPaint.setColor(Color.RED);
//        // 字体大小
//        mPaint.setTextSize(70);
//        // 字体下面加下划线
//        mPaint.setUnderlineText(true);
//        // 从(10,10)的位置开始，绘制一行文本。
//        mCanvas.drawText("Hello Wrold!", 10, 100, mPaint);
//        // 加粗字体。 如果字体的型号比较小，那么加粗的效果可能就不是很明显。
//        mPaint.setFakeBoldText(true);
//        // 给字体加上删除线。
//        mPaint.setStrikeThruText(true);
//        mCanvas.drawText("Hello Wrold2!", 10, 300, mPaint);
//        // 设置文本在水平方向上的倾斜比例，负数向右倾斜，正数向左倾斜。
//        mPaint.setTextSkewX(-0.3f);
//        // 设置文本水平方向上的对齐方法，以坐标(10,500)为例：
//        //   Paint.Align.LEFT :   将文本的左下角放到(10,500)的位置，默认设置。
//        //   Paint.Align.CENTER : 将文本的底边中心点放到(10,500)的位置。
//        //   Paint.Align.RIGHT :  将文本的右下角放到(10,500)的位置。
//        mPaint.setTextAlign(Paint.Align.LEFT);
//        mCanvas.drawText("Hello Wrold3!", 10, 500, mPaint);
//        mPaint_line.setTextSize(80f);
//
//        //按照既定点 绘制文本内容
//        mCanvas.drawPosText("Android", new float[]{
//                10,610, //第一个字母在坐标10,610
//                120,640, //第二个字母在坐标120,640
//                230,670, //....
//                340,700,
//                450,730,
//                560,760,
//                670,790,
//        }, mPaint);
//    }
}
