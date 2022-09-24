package com.example.trymaya.ui.plan;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trymaya.R;
import com.example.trymaya.databinding.FragmentPlanBinding;

import java.util.Calendar;

public class PlanFragment extends Fragment implements View.OnClickListener {
    private FragmentPlanBinding binding;
    Activity activity = getActivity();
    Button btnDatePicker,btnTimePicker;

    EditText txtDate, txtTime;
    int mYear, mMonth, mDay, mHour, mMinute;
    long  givenTime=0;
    long givenDate=0;
    long totalTime=0;
    int ElapsedTime;
    long tot;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PlanViewModel planViewModel =
                new ViewModelProvider(this).get(PlanViewModel.class);

        binding = FragmentPlanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        super.onCreateView(inflater,container,savedInstanceState);



        View v = inflater.inflate(R.layout.fragment_plan, container, false);
        btnDatePicker = (Button) v.findViewById(R.id.button1);
        btnTimePicker = (Button) v.findViewById(R.id.btn_time);
        txtDate = (EditText) v.findViewById(R.id.in_date);
        txtTime = (EditText) v.findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        createNotificationChannel();

        Button button = v.findViewById(R.id.button);

        button.setOnClickListener(view -> {

            Toast.makeText(getActivity(),"Reminder is set", Toast.LENGTH_SHORT).show();
//Toast.makeText(this, "Reminder is set" , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PlanFragment.this.getActivity(),ReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);


            long timeAtButtonClick = System.currentTimeMillis();
            long tenSecondsInMillis = 7*86400*1000  - ElapsedTime - totalTime;
            tot = tenSecondsInMillis;

            alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSecondsInMillis, pendingIntent);
        });
return v;

       // final TextView textView = binding.textPlan;
        //planViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
       // return root;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "jereme";
            String description = "bo";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("jereme", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            Calendar cal = Calendar.getInstance();
                            int yearr = cal.get(Calendar.YEAR);
                            int monthh = cal.get(Calendar.MONTH);
                            int day = cal.get(Calendar.DATE);
                            int currentIndex = (year - 1970) * 12 + monthh;
                            ElapsedTime = day-dayOfMonth;
                            ElapsedTime = ElapsedTime*86400*1000;
                            //txtDate.setText(ElapsedTime +":");
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            //txtTime.setText(hourOfDay + ":" + minute);

                            // txtTime.setText(ElapsedTime +":");
                            Calendar no = Calendar.getInstance();
                            int hourN = no.get(Calendar.HOUR_OF_DAY);
                            int minuteN = no.get(Calendar.MINUTE);
                            totalTime = minuteN - minute;
                            totalTime = totalTime*60*1000;
                            txtTime.setText(tot +":");
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}
