package hq.demo.net.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import hq.demo.net.R;

public class CircleProgressBar extends View {

    private float radius = -1;
    private float mRoundWidth = -1;
    private float rate = 0.33f;
    private float rateToRadius = 0.2f;
    private float rateOval = 0.48f;

    private int progressBackground = getResources().getColor(R.color.circle_progress_bg_color);

    private int progressColor = getResources().getColor(R.color.circle_progress_color);

    private int max = -1;

    private int progress = -1;

    Paint paint = new Paint();
    Paint paintBorder = new Paint();

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.cpb);

        progressBackground = array.getColor(R.styleable.cpb_progress_background, getResources().getColor(R.color.circle_progress_bg_color));
        progressColor = array.getColor(R.styleable.cpb_progress_color, getResources().getColor(R.color.circle_progress_color));

        paint.setAntiAlias(true);
        paintBorder.setAntiAlias(true);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setColor(progressColor);

        array.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        radius = getWidth() * rate;
        mRoundWidth = radius * rateToRadius;
        paintBorder.setStrokeWidth(mRoundWidth);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (radius != -1 && getVisibility() == VISIBLE) {
            float centre = getWidth() / 2; //获取圆心的x坐标

            canvas.drawCircle(centre, centre, radius, paintBorder);

            float angle = 0;
            if (max > 0 && progress >= 0) {
                angle = (float) progress / (float) max * 360f;
            }
            paint.setColor(progressColor);
            RectF oval = new RectF(centre - radius + mRoundWidth * rateOval,
                            centre - radius + mRoundWidth * rateOval,
                            centre + radius - mRoundWidth * rateOval,
                            centre + radius - mRoundWidth * rateOval);

            canvas.drawArc(oval, 270, angle, true, paint);
        }

    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            invalidate();
        }
    }

    /**
     * set radius
     *
     * @param radius
     * @attr ref R.styleable#radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * set progress background color
     *
     * @param progressBackground
     * @attr ref R.styleable#progress_background
     */
    public void setProgressBackground(int progressBackground) {
        this.progressBackground = progressBackground;
    }

    /**
     * set progress color
     *
     * @param progressColor
     * @attr ref R.styleable#progress_color
     */
    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    /**
     * set max progress value
     *
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * set progress value
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
