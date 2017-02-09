package com.onlylemi.mapview.library.layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.utils.MapMath;

import java.util.List;

import college.wyk.app.R;

/**
 * MarkLayer
 *
 * @author: onlylemi
 */
public class MarkLayer extends MapBaseLayer {

    private List<PointF> marks;
    private List<String> marksName;
    private MarkIsClickListener markIsClickListener;
    private MarkUnClickListener markUnClickListener;

    private Bitmap bmpMark, bmpMarkTouch;

    private float radiusMark;
    private boolean isClickMark = false;
    private int num = -1;

    private Paint paint;

    public MarkLayer(MapView mapView) {
        this(mapView, null, null);
    }

    public MarkLayer(MapView mapView, List<PointF> marks, List<String> marksName) {
        super(mapView);
        this.marks = marks;
        this.marksName = marksName;

        initLayer();
    }

    private void initLayer() {
        radiusMark = setValue(14f);

        bmpMark = BitmapFactory.decodeResource(mapView.getResources(), R.mipmap.off);
        bmpMarkTouch = BitmapFactory.decodeResource(mapView.getResources(), R.mipmap.on);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void onTouch(MotionEvent event) {
        if (marks != null) {
            if (!marks.isEmpty()) {
                float[] goal = mapView.convertMapXYToScreenXY(event.getX(), event.getY());
                for (int i = 0; i < marks.size(); i++) {
                    if (MapMath.getDistanceBetweenTwoPoints(goal[0], goal[1],
                            marks.get(i).x - bmpMark.getWidth() / 2, marks.get(i).y - bmpMark
                                    .getHeight() / 2) <= 50) {
                        num = i;
                        isClickMark = true;
                        break;
                    }

                    if (i == marks.size() - 1) {
                        if (isClickMark) {
                            markUnClickListener.markUnClick(num);
                            mapView.refresh();
                        }
                        isClickMark = false;
                    }
                }
            }

            if (markIsClickListener != null) {
                if (isClickMark) {
                    markIsClickListener.markIsClick(num);
                }
                mapView.refresh();
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float
            currentRotateDegrees) {
        if (isVisible && marks != null) {
            canvas.save();
            if (!marks.isEmpty()) {
                for (int i = 0; i < marks.size(); i++) {
                    PointF mark = marks.get(i);
                    float[] goal = {mark.x, mark.y};
                    currentMatrix.mapPoints(goal);

                    paint.setColor(Color.parseColor("#212121"));
                    paint.setShadowLayer(2f, 0, 1f, Color.GRAY);
                    paint.setTextSize(radiusMark);
                    paint.setTextAlign(Paint.Align.CENTER);
                    //mark name
                    if (false && mapView.getCurrentZoom() > 1.0 && marksName != null
                            && marksName.size() == marks.size()) {
                        canvas.drawText(marksName.get(i), goal[0] - radiusMark + 20, goal[1] -
                                radiusMark / 2 + 20, paint);
                    }
                    //mark ico
                    if (i == num && isClickMark) {
                        canvas.drawBitmap(bmpMarkTouch, goal[0] - bmpMarkTouch.getWidth() / 2,
                                goal[1] - bmpMarkTouch.getHeight(), paint);
                    } else {
                        canvas.drawBitmap(bmpMark, goal[0] - bmpMark.getWidth() / 2,
                                goal[1] - bmpMark.getHeight(), paint);
                    }
                }
            }
            canvas.restore();
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<PointF> getMarks() {
        return marks;
    }

    public void setMarks(List<PointF> marks) {
        this.marks = marks;
    }

    public List<String> getMarksName() {
        return marksName;
    }

    public void setMarksName(List<String> marksName) {
        this.marksName = marksName;
    }

    public boolean isClickMark() {
        return isClickMark;
    }

    public void setMarkIsClickListener(MarkIsClickListener listener) {
        this.markIsClickListener = listener;
    }

    public void setMarkUnClickListener(MarkUnClickListener listener) {
        this.markUnClickListener = listener;
    }

    interface MarkIsClickListener {
        void markIsClick(int num);
    }

    interface MarkUnClickListener {
        void markUnClick(int num);
    }

}
