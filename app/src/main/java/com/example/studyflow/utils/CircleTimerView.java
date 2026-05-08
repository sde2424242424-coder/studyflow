package com.example.studyflow.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircleTimerView extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;

    private float progress = 0f; // 0 - 1

    public CircleTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.LTGRAY);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(20f);
        backgroundPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setColor(Color.BLUE);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(20f);
        progressPaint.setAntiAlias(true);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float size = Math.min(getWidth(), getHeight());
        float radius = size / 2 - 20;

        float cx = getWidth() / 2;
        float cy = getHeight() / 2;

        // фон
        canvas.drawCircle(cx, cy, radius, backgroundPaint);

        // прогресс
        canvas.drawArc(
                cx - radius,
                cy - radius,
                cx + radius,
                cy + radius,
                -90,
                360 * progress,
                false,
                progressPaint
        );
    }
}