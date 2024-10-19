package com.example.myapplication.home_fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class ClockView extends View {
    private Paint blackPaint;
    private Paint whitePaint;
    private Paint greenPaint;
    private Paint circlePaint;

    public int stageIndex = 1;
    private String[] stages;
    public int currentRound = 1;
    private final int totalRounds = TabataRunFragment.tabataObject.getRounds();

    private static final int DIAMETER_DP = 335;
    private int radius;
    private float greenAngle = 360;
    private CountDownTimer countDownTimer;

    private long currentSecond;

    private long remainingTime;
    private boolean isPaused;

    private long lastSecond = 0;

    private final int greenColor = Color.parseColor("#1D7966");
    private final int blueColor = Color.parseColor("#00C2FF");

    private int stageTimeColor = greenColor;
    private int stageRoundColor = blueColor;

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        stages = new String[]{
                getResources().getString(R.string.workout),
                getResources().getString(R.string.rest)
        };

        blackPaint = addPaint(Color.BLACK, 15);
        whitePaint = addPaint(Color.WHITE, 1);
        greenPaint = addPaint(Color.parseColor("#1D7966"), 2);
        circlePaint = new Paint();

        greenPaint.setShadowLayer(4, 0, 0, Color.parseColor("#00C2FF"));

        circlePaint.setColor(greenPaint.getColor());
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setShadowLayer(4, 0, 0, Color.parseColor("#00C2FF"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredSize = (int) (DIAMETER_DP * getResources().getDisplayMetrics().density);
        int width = resolveSize(desiredSize, widthMeasureSpec);
        int height = resolveSize(desiredSize, heightMeasureSpec);

        setMeasuredDimension(width, height);

        radius = desiredSize / 2 - (int) (blackPaint.getStrokeWidth() / 2);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        canvas.drawCircle(centerX, centerY, radius, blackPaint);
        canvas.drawCircle(centerX, centerY, radius, whitePaint);

        drawGreenCircle(canvas, centerX, centerY);
    }

    private void drawGreenCircle(Canvas canvas, int centerX, int centerY) {

        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                270, -greenAngle, false, greenPaint);

        float endX = centerX + (float) (radius * Math.cos(Math.toRadians(270 - greenAngle)));
        float endY = centerY + (float) (radius * Math.sin(Math.toRadians(270 - greenAngle)));

        float circleRadius = 10f;

        canvas.drawCircle(endX, endY, circleRadius, circlePaint);
    }

    private Paint addPaint(int color, int width) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width * getResources().getDisplayMetrics().density);
        return paint;
    }

    public void startTimer(long durationMillis, long totalDurationMillis) {
        isPaused = false;

        countDownTimer = new CountDownTimer(durationMillis, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;

                greenAngle = 360 * ((float) millisUntilFinished / totalDurationMillis);
                invalidate();

                currentSecond = millisUntilFinished / 1000;

                if (currentSecond != lastSecond) {
                    lastSecond = currentSecond;

                    TextView currentStageTime = getRootView().findViewById(R.id.stageTimeView);

                    int minutes = (int) lastSecond / 60;
                    int seconds = (int) lastSecond % 60;
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds + 1);
                    currentStageTime.setText(formattedTime);
                }
            }


            @Override
            public void onFinish() {
                // Next stage

                TextView stageView = getRootView().findViewById(R.id.stageNameView);
                TextView stageTimeView = getRootView().findViewById(R.id.stageTimeView);
                TextView stageRoundView = getRootView().findViewById(R.id.stageRoundView);

                // Change round and time colours

                stageTimeColor = (stageTimeColor == blueColor) ? greenColor : blueColor;
                stageRoundColor = (stageRoundColor == blueColor) ? greenColor : blueColor;

                stageTimeView.setTextColor(stageTimeColor);

                SpannableString spannable = new SpannableString(currentRound + " / " + totalRounds);
                spannable.setSpan(new ForegroundColorSpan(stageRoundColor), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stageRoundView.setText(spannable);

                if (stageIndex < stages.length) {
                    stageView.setText(String.valueOf(stages[stageIndex]));
                    stageIndex++;
                } else {
                    // Next round

                    if (currentRound < totalRounds) {
                        currentRound++;

                        stageIndex = 1;

                        stageView.setText(stages[0]);

                        spannable = new SpannableString(currentRound + " / " + totalRounds);
                        spannable.setSpan(new ForegroundColorSpan(stageRoundColor), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        stageRoundView.setText(spannable);
                    } else {
                        stopTimer(true);
                        return;
                    }
                }

                int currentStageTime = TabataRunFragment.tabataObject.getIntervalMinutes() * 60 + TabataRunFragment.tabataObject.getIntervalSeconds();

                if (stageIndex != 1) {
                    currentStageTime = TabataRunFragment.tabataObject.getRestMinutes() * 60 + TabataRunFragment.tabataObject.getRestSeconds();
                }

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }

                startTimer(currentStageTime * 1000L, currentStageTime * 1000L);
            }
        }.start();
    }

    public void pauseTimer() {
        if (countDownTimer != null && !isPaused) {
            countDownTimer.cancel();
            isPaused = true;
        }
    }

    public void resumeTimer() {
        if (isPaused) {
            long totalDurationMillis;
            if (stageIndex == 1) {
                totalDurationMillis = TabataRunFragment.tabataObject.getIntervalMinutes() * 60 * 1000L +
                        TabataRunFragment.tabataObject.getIntervalSeconds() * 1000L;
            } else {
                totalDurationMillis = TabataRunFragment.tabataObject.getRestMinutes() * 60 * 1000L +
                        TabataRunFragment.tabataObject.getRestSeconds() * 1000L;
            }

            startTimer(remainingTime, totalDurationMillis);
            isPaused = false;
        }
    }


    public void stopTimer(boolean isFinished) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        if (isFinished) {
            TabataRunFragment.tabataState = null;
            TabataRunFragment.setButtonsClickable(getRootView(), true);

            stageIndex = 1;
            currentRound = 1;

            TextView currentStage = getRootView().findViewById(R.id.stageNameView);
            TextView currentStageTime = getRootView().findViewById(R.id.stageTimeView);
            TextView currentStageRound = getRootView().findViewById(R.id.stageRoundView);

            currentStage.setText(stages[0]);

            int totalSeconds = TabataRunFragment.tabataObject.getIntervalMinutes() * 60 + TabataRunFragment.tabataObject.getIntervalSeconds();
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", totalSeconds / 60, totalSeconds % 60);
            currentStageTime.setTextColor(greenColor);
            currentStageTime.setText(formattedTime);

            SpannableString spannable = new SpannableString("1 / " + totalRounds);
            spannable.setSpan(new ForegroundColorSpan(blueColor), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            currentStageRound.setText(spannable);

            MaterialButton runButton = getRootView().findViewById(R.id.tabataRun);
            runButton.setIconResource(R.drawable.ic_tabata_run);
        }

        greenAngle = 360;
        invalidate();
    }
}
