package sp2016.cs310.com.hw2furkankemikli.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by furkankemikli on 17.05.2016.
 */
public class TimeDateUtil {
    int currentYear;
    int currentMonth;
    int currentDay;
    int currentHour;
    int currentMinute;

    public  int yearSelected,monthSelected,daySelected,hourSelected,minuteSelected;

    DatePickerDialog.OnDateSetListener datePicker;
    TimePickerDialog.OnTimeSetListener timePicker;
    Calendar calendar;

    public TimeDateUtil(){
        calendar=Calendar.getInstance();
        this.currentYear=calendar.get(Calendar.YEAR);
        this.currentMonth=calendar.get(Calendar.MONTH);
        this.currentDay=calendar.get(Calendar.DAY_OF_MONTH);
        this.currentHour=calendar.get(Calendar.HOUR_OF_DAY);
        this.currentMinute=calendar.get(Calendar.MINUTE);
        calendar.getTimeInMillis();

    }
    public Calendar registerTimeDateListener(final Context context)
    {
        Log.i("TimeDateUtil","register");
        datePicker=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                yearSelected=year;
                monthSelected=monthOfYear;
                daySelected=dayOfMonth;
                Log.i("TimeDateUtil","onDateSet");
            }
        };
        timePicker =new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                hourSelected = hourOfDay;
                minuteSelected = min;
                Log.i("TimeDateUtil","onTimeSet");
                Log.i("TimeDateUtil",new Integer(daySelected).toString());
                Log.i("TimeDateUtil",new Integer(monthSelected).toString());
                Log.i("TimeDateUtil",new Integer(yearSelected).toString());
                Log.i("TimeDateUtil",new Integer(hourSelected).toString());
                Log.i("TimeDateUtil",new Integer(minuteSelected).toString());
                calendar.set(yearSelected, monthSelected, daySelected, hourSelected, minuteSelected,0);

            }
        };
        return calendar;
    }
    public Calendar showTimeDateDialog(Context context)
    {
        Log.i("TimeDateUtil","showTimeDateDialog");
        Calendar cal=registerTimeDateListener(context);
        DatePickerDialog dateDialog=new DatePickerDialog(context,datePicker, currentYear, currentMonth, currentDay);
        TimePickerDialog timeDialog=new TimePickerDialog(context, timePicker, currentHour, currentMinute, false);
        timeDialog.setTitle("Select time:");
        dateDialog.setTitle("Select date:");
        timeDialog.show();
        dateDialog.show();
        return cal;
    }
    public Calendar getCalendar(){
        return this.calendar;

    }
    public static String returnCurrentTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy h:mm a");
        Calendar calendar=Calendar.getInstance();
        String date=dateFormat.format(calendar.getTime());
        return date;
    }
}
