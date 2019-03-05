package com.nivashini.numbersround.utilspkg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.nivashini.numbersround.main.MainActivity;
import com.nivashini.numbersround.R;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*** Created by Nivashinim on 13-11-2017.*/

public class AppUtils {

    private Toast toast;
    private String currentToastMsg = "";

    public void showLongToast(Context context, String msg) {
        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        currentToastMsg = msg;
        toast.show();
    }

    public void showShortToast(Context context, String msg) {
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        currentToastMsg = msg;
        toast.show();
    }

    @SuppressLint("HardwareIds")
    public static String uniqueDeviceID(Context ctx) {
        return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public AppUtils() {
    }

    public SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences(AppConstant.SHARED_PREF_NAME, AppConstant.PRIVATE_MODE);
    }

    public void setToolBarTitle(ActionBar supportActionBar, String title) {
//        supportActionBar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        supportActionBar.setTitle(title);
    }

    public void setRvLayout(Context context, RecyclerView rvLayout) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvLayout.setLayoutManager(layoutManager);
    }

    public double roundTwoDecimals(double d) {
        return Double.valueOf(new DecimalFormat("#.##").format(d));
    }

    public String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(c.getTime());
    }

    public String getCalendarFormatDt(String date) {
        String dt[] = date.split("-");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, Integer.parseInt(dt[2]));
        cal.add(Calendar.MONTH, Integer.parseInt(dt[1]));
        cal.add(Calendar.YEAR, Integer.parseInt(dt[0]));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Log.i("Cal Date", simpleDateFormat.format(date));
        return simpleDateFormat.format(date);
//        return cal;
    }

    public long dateInTimeMillis(String strDate) {
        long timeInMilliseconds = 0;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
//            Date mDate = sdf.parse(strDate);
//            timeInMilliseconds = mDate.getTime();
//            System.out.println("Date in milli :: " + timeInMilliseconds);
            timeInMilliseconds = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(strDate).getTime();
            PrintStream printStream = System.out;
            String stringBuilder = "Date in milli : " + timeInMilliseconds;
            printStream.println(stringBuilder);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    public void goToFragment(final Context context, int container, Fragment fragment, boolean isAddToBackStack) {
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = null;
        if (container == R.id.main_act_frag_container) {
            manager = ((MainActivity) context).getSupportFragmentManager();
        }
        if (manager != null && !manager.popBackStackImmediate(backStateName, 0)) {
            if (isAddToBackStack) {
                manager.beginTransaction().replace(container, fragment, backStateName).addToBackStack(backStateName).commit();
            } else {
                manager.beginTransaction().replace(container, fragment, backStateName).disallowAddToBackStack().commit();
            }
        }
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}
