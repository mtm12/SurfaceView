package surfaceview.demo.com.surfaceview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static surfaceview.demo.com.surfaceview.MainActivity.multiplier;

/**
 * Created by marct_000 on 10/9/2017.
 */

public class CustomView extends View {

    private Rect mRectSquare;
    private Paint mPaint;
    private Canvas canvas;

    public CustomView(Context context) {
        super(context);
        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        mRectSquare = new Rect();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    public void drawBrackets(){
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas){

            mPaint.setColor(Color.RED);
            //canvas.drawRect(220*mulitplier,447*mulitplier,1220*mulitplier,452*mulitplier, mPaint);

            mPaint.setColor(Color.WHITE);
            //x-start, y-start, x-end, y-end
            canvas.drawRect(300*multiplier,100*multiplier,310*multiplier,800*multiplier, mPaint);//vertical line
            canvas.drawRect(300*multiplier,100*multiplier,500*multiplier,110*multiplier, mPaint);//top line
            canvas.drawRect(300*multiplier,800*multiplier,500*multiplier,810*multiplier, mPaint);//botom line

            mPaint.setColor(Color.WHITE);
            canvas.drawRect(1130*multiplier,100*multiplier,1140*multiplier,800*multiplier, mPaint);
            canvas.drawRect(940*multiplier,100*multiplier,1140*multiplier,110*multiplier, mPaint);
            canvas.drawRect(940*multiplier,800*multiplier,1140*multiplier,810*multiplier, mPaint);


    }
}
