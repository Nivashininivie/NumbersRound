package com.nivashini.numbersround.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nivashini.numbersround.R;
import com.nivashini.numbersround.main.MainActivity;

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

    public void showCustomDlgWithOkay(final Context context, String dlgTitle, String dlgContent) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_custom_alert_single_btn_dlg);
//        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window != null) {
            Point size = new Point();
            Display display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);
            int width = size.x;
            window.setLayout((int) (width * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        TextView tvTitle = dialog.findViewById(R.id.tvDlgTitle);
        TextView tvDlgContent = dialog.findViewById(R.id.tvAlertContent);
        ImageView ivAlertIcon = dialog.findViewById(R.id.ivAlertIcon);

        ivAlertIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_alert_bell));
        tvTitle.setText(dlgTitle);
        tvDlgContent.setText(dlgContent);

        Button btnOkay = dialog.findViewById(R.id.btnAlertOkay);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((MainActivity) context).getSupportFragmentManager().popBackStackImmediate();
            }
        });
        dialog.show();
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}
