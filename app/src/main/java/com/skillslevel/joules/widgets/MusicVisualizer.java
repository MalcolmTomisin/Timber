package com.skillslevel.joules.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Random;

/**
        * a music visualizer sort of animation (with random data)
        */

public class MusicVisualizer extends View {
    Random random = new Random();

    Paint paint = new Paint();
    private Runnable animateView = new Runnable() {
        @Override
        public void run() {

            //run every 150 ms
            postDelayed(this, 150);

            invalidate();
        }
    };

    public MusicVisualizer(Context context) {
        super(context);
        new MusicVisualizer(context, null);
    }

    public MusicVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);

        //start runnable
        removeCallbacks(animateView);
        post(animateView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //set paint style, Style.FILL will fill the color, Style.STROKE will stroke the color
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(getDimensionInPixel(0), getHeight() - (20 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(7), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(10), getHeight() - (20 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(17), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(20), getHeight() - (20 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(27), getHeight(), paint);
    }

    public void setColor(int color) {
        paint.setColor(color);
        invalidate();
    }

    //get all dimensions in dp so that views behaves properly on different screen resolutions
    private int getDimensionInPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            removeCallbacks(animateView);
            post(animateView);
        } else if (visibility == GONE) {
            removeCallbacks(animateView);
        }
    }

}
