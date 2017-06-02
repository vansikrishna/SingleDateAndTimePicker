package com.github.florent37.sample.singledateandtimepicker;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivityWithDoublePicker extends AppCompatActivity {

    @Bind(R.id.doubleText)
    TextView doubleText;
    @Bind(R.id.singleText)
    TextView singleText;
    @Bind(R.id.side_layout)
    LinearLayout side_layout;

    SimpleDateFormat simpleDateFormat;
    SingleDateAndTimePickerDialog.Builder singleBuilder;
    DoubleDateAndTimePickerDialog.Builder doubleBuilder;
    @Bind(R.id.include_picker)
    SingleDateAndTimePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_double_picker);
        ButterKnife.bind(this);
        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM HH:mm", Locale.getDefault());
        initPickerWidth();
        initIncludeLayout();
    }

    private void initPickerWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int w = displaymetrics.widthPixels/2;
        int h = dpToPx(230);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        picker.setLayoutParams(params);
        picker.setGravity(Gravity.RIGHT);
        side_layout.setLayoutParams(params);
        picker.setListener(new SingleDateAndTimePicker.Listener() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                Toast.makeText(getApplicationContext(), displayed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void initIncludeLayout() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 2017);
        final Date minDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 5);
        final Date maxDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 2);
        final Date defaultDate = calendar.getTime();

        if (defaultDate != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(defaultDate);
            picker.selectDate(calendar);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (singleBuilder != null)
            singleBuilder.close();
        if (doubleBuilder != null)
            doubleBuilder.close();
    }

    @OnClick(R.id.singleLayout)
    public void simpleClicked() {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 2017);
        final Date minDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 5);
        final Date maxDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 2);
        final Date defaultDate = calendar.getTime();

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .curved()

                .backgroundColor(Color.BLACK)
                .mainColor(Color.GREEN)
                //.displayDays(false)
                .displayDtSelector(true)
                .displayHourMinuteLabels(true)
                //.displayHours(false)
                //.displayMinutes(false)

                //.mustBeOnFuture()

                //.minutesStep(15)
                //.mustBeOnFuture()
                .defaultDate(defaultDate)
                .minDateRange(minDate)
                .maxDateRange(maxDate)

                .title("Simple")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        singleText.setText(simpleDateFormat.format(date));
                    }
                });
        singleBuilder.display();
    }

    @OnClick(R.id.doubleLayout)
    public void doubleClicked() {

        final Date now = new Date();
        final Calendar calendarMin = Calendar.getInstance();
        final Calendar calendarMax = Calendar.getInstance();

        calendarMin.setTime(now); // Set min now
        calendarMax.setTime(new Date(now.getTime() + TimeUnit.DAYS.toMillis(150))); // Set max now + 150 days

        final Date minDate = calendarMin.getTime();
        final Date maxDate = calendarMax.getTime();

        doubleBuilder = new DoubleDateAndTimePickerDialog.Builder(this)
                //.bottomSheet()
                //.curved()

                .backgroundColor(Color.BLACK)
                .mainColor(Color.GREEN)
                .minutesStep(15)
                .mustBeOnFuture()

                .minDateRange(minDate)
                .maxDateRange(maxDate)
                //.defaultDate(now)
                .tab0Date(now)
                .tab1Date(new Date(now.getTime() + TimeUnit.HOURS.toMillis(1)))

                .title("Double")

                .tab0Text("Depart")
                .tab1Text("Return")
                .listener(new DoubleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(List<Date> dates) {
                        final StringBuilder stringBuilder = new StringBuilder();
                        for (Date date : dates) {
                            stringBuilder.append(simpleDateFormat.format(date)).append("\n");
                        }
                        doubleText.setText(stringBuilder.toString());
                    }
                });
        doubleBuilder.display();
    }
}