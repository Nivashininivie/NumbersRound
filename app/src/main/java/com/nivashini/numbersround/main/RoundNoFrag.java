package com.nivashini.numbersround.main;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nivashini.numbersround.R;
import com.nivashini.numbersround.datatransaction.MStepDetails;
import com.nivashini.numbersround.datatransaction.RoundingStepsListAdapter;
import com.nivashini.numbersround.utilspkg.AppConstant;
import com.nivashini.numbersround.utilspkg.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class RoundNoFrag extends Fragment implements View.OnClickListener {

    private TextView tvTypedNo;
    private TextView tvTimer;
    private RecyclerView rvStepsInvolved;
    private ImageButton btnBackSpace;
    private LinearLayout linLayContainer;
    private ArrayList<String> noList = new ArrayList<>(), actualNoList = new ArrayList<>();
    private ArrayList<MStepDetails> stepsList = new ArrayList<>();
    private CountDownTimer timer;
    private Context context;

    private AppUtils appUtils = new AppUtils();
    private String strOperator = "", strFormula = "", strGeneratedNo = "";

    private int btnCount = 0, sum = 0, num1 = 0, num2 = 0, selectedBtnId1 = 0, selectedBtnId2 = 0;
    private boolean isOperatorClicked = false;

    public RoundNoFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_round_no, container, false);

        if (getActivity() != null) {
            context = getActivity();
            appUtils.setToolBarTitle(((MainActivity) getActivity()).getSupportActionBar(), AppConstant.ROUND_NO_TITLE);
        }

        TextView tvRandomNo = view.findViewById(R.id.tvRandomNo);
        rvStepsInvolved = view.findViewById(R.id.rvSteps);
        ImageButton btnAdd = view.findViewById(R.id.btnAdd);
        ImageButton btnSubtract = view.findViewById(R.id.btnSubtract);
        ImageButton btnMultiply = view.findViewById(R.id.btnMultiply);
        ImageButton btnDivide = view.findViewById(R.id.btnDivide);
        ImageButton btnEqual = view.findViewById(R.id.btnEqual);
        btnBackSpace = view.findViewById(R.id.btnBackSpace);
        tvTypedNo = view.findViewById(R.id.tvTypedNo);
        tvTimer = view.findViewById(R.id.tvTimer);
        linLayContainer = view.findViewById(R.id.linLayNoCont);
        appUtils.setRvLayout(getActivity(), rvStepsInvolved);

        if (getArguments() != null) {
            String strSelectedNos = getArguments().getString(AppConstant.SELECTED_NOS);
            strGeneratedNo = getArguments().getString(AppConstant.GENERATED_NO);
            strFormula = getArguments().getString(AppConstant.FORMULAE);
            if (strSelectedNos != null) {
                actualNoList = new ArrayList<>(Arrays.asList(strSelectedNos.split(",")));
                noList = new ArrayList<>(Arrays.asList(strSelectedNos.split(",")));
            }
        }

        tvRandomNo.setText(strGeneratedNo);
        generateDynLayouts();

        timer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                String strTimerText = String.valueOf(millisUntilFinished / 1000) + " seconds remaining";
                tvTimer.setText(strTimerText);
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(1000); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                tvTimer.startAnimation(anim);
            }

            @Override
            public void onFinish() {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.layout_custom_dlg_two_btn);
                dialog.setCancelable(false);

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
                final TextView tvSolution = dialog.findViewById(R.id.tvSolution);
                ImageView ivAlertIcon = dialog.findViewById(R.id.ivAlertIcon);

                ivAlertIcon.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_alert_bell));
                tvTitle.setText("Time Up!");
                tvDlgContent.setText("To check the solution, please click solution button, else click the restart button to start a new round");

                Button btnSolution = dialog.findViewById(R.id.btnAlertOkay);
                Button btnNewRound = dialog.findViewById(R.id.btnAlertCancel);
                btnSolution.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvSolution.setVisibility(View.VISIBLE);
                        tvSolution.setText("Solution is " + strFormula);
                    }
                });
                btnNewRound.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                    }
                });
                dialog.show();
            }
        }.start();

        btnAdd.setOnClickListener(this);
        btnSubtract.setOnClickListener(this);
        btnMultiply.setOnClickListener(this);
        btnDivide.setOnClickListener(this);
        btnEqual.setOnClickListener(this);
        btnBackSpace.setOnClickListener(this);

        return view;
    }

    private void generateDynLayouts() {
        TableRow tablerow1 = new TableRow(context);
        TableRow tablerow2 = new TableRow(context);
        TableRow.LayoutParams trLayoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        trLayoutParams.setMargins(0, 0, 0, 0);
        tablerow1.setGravity(Gravity.CENTER);
        tablerow2.setGravity(Gravity.CENTER);
        for (int i = 0; i < noList.size(); i++) {
            TextView button = new TextView(context);
            button.setId(View.generateViewId());
            button.setOnClickListener(this);
            if (i <= 2)
                generateButton(tablerow1, button, String.valueOf(noList.get(i)), 24, 60, 24, 60);
            else
                generateButton(tablerow2, button, String.valueOf(noList.get(i)), 24, 60, 24, 60);
        }
        linLayContainer.removeAllViews();
        linLayContainer.addView(tablerow1);
        linLayContainer.addView(tablerow2);
    }

    public void generateButton(TableRow tableRow, TextView button, String text, int paddingStart, int paddingTop, int paddingEnd, int paddingBottom) {
        TableRow.LayoutParams trLayoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        button.setLayoutParams(trLayoutParams);
        button.setText(text);
        button.setTextColor(context.getResources().getColor(R.color.colorAccent));
        button.setGravity(Gravity.CENTER);
        button.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        button.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom);
        tableRow.addView(button);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                btnBackSpace.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_backspace));
                if (num1 != 0 || num2 != 0) {
                    strOperator = "+";
                    isOperatorClicked = true;
                    tvTypedNo.setText(String.valueOf(num1) + strOperator);
                } else {
                    appUtils.showLongToast(context, "Select first number to proceed");
                }
                break;
            case R.id.btnMultiply:
                btnBackSpace.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_backspace));
                if (num1 != 0 || num2 != 0) {
                    strOperator = "*";
                    isOperatorClicked = true;
                    tvTypedNo.setText(String.valueOf(num1) + strOperator);
                } else {
                    appUtils.showLongToast(context, "Select first number to proceed");
                }
                break;
            case R.id.btnSubtract:
                btnBackSpace.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_backspace));
                if (num1 != 0 || num2 != 0) {
                    strOperator = "-";
                    isOperatorClicked = true;
                    tvTypedNo.setText(String.valueOf(num1) + strOperator);
                } else {
                    appUtils.showLongToast(context, "Select first number to proceed");
                }
                break;
            case R.id.btnDivide:
                btnBackSpace.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_backspace));
                if (num1 != 0 || num2 != 0) {
                    strOperator = "/";
                    isOperatorClicked = true;
                    tvTypedNo.setText(String.valueOf(num1) + strOperator);
                } else {
                    appUtils.showLongToast(context, "Select first number to proceed");
                }
                break;
            case R.id.btnEqual:
                if (btnCount == 2) {
                    Log.i("division result", String.valueOf(num1 % num2));
                    RoundingStepsListAdapter adapter;
                    int lastResult = 0;
                    if (strOperator.equals("/")) {
                        if (num1 % num2 != 0) {
                            appUtils.showLongToast(context, "Result discarded! Please select some other operation");
                            tvTypedNo.setText(String.valueOf(num1));
                            btnCount = 1;
                            makeTransformations(AppConstant.DELETE_LAST);
                        } else {
                            lastResult = sum;
                            if (lastResult == Integer.valueOf(strGeneratedNo)) {
                                timer.cancel();
                            } else {
                                tvTypedNo.setText(String.valueOf(lastResult));
                                stepsList.add(new MStepDetails(String.valueOf(num1), strOperator, String.valueOf(num2), String.valueOf(sum)));
                                strOperator = "";
                                sum = 0;
                                btnCount = 1;
                                num1 = lastResult;
                                btnBackSpace.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_clear));
//                            makeTransformations(AppConstant.REPLACE_WITH_RESULT);
                                btnBackSpace.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_clear));
                                TextView tv = getView().findViewById(selectedBtnId1);
                                tv.setVisibility(View.GONE);
                                TextView tv2 = getView().findViewById(selectedBtnId2);
                                tv2.setVisibility(View.GONE);
                                adapter = new RoundingStepsListAdapter(context, stepsList);
                                rvStepsInvolved.setAdapter(adapter);
                            }
                        }
                    } else if (sum > 999 || sum < 1) {
                        appUtils.showLongToast(context, "Result discarded! Please select some other operation");
                        tvTypedNo.setText(String.valueOf(num1));
                        btnCount = 1;
                        makeTransformations(AppConstant.DELETE_LAST);
                    } else {
                        lastResult = sum;
                        tvTypedNo.setText(String.valueOf(lastResult));
                        stepsList.add(new MStepDetails(String.valueOf(num1), strOperator, String.valueOf(num2), String.valueOf(sum)));
                        strOperator = "";
                        sum = 0;
                        btnCount = 1;
                        num1 = lastResult;
                        btnBackSpace.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_clear));
//                        makeTransformations(AppConstant.REPLACE_WITH_RESULT);
                        btnBackSpace.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_clear));
                        TextView tv = getView().findViewById(selectedBtnId1);
                        tv.setVisibility(View.GONE);
                        TextView tv2 = getView().findViewById(selectedBtnId2);
                        tv2.setVisibility(View.GONE);
                        adapter = new RoundingStepsListAdapter(context, stepsList);
                        rvStepsInvolved.setAdapter(adapter);
                    }
                }
                break;
            case R.id.btnBackSpace:
                appUtils.showLongToast(context, "Logn clicked");
                if (btnBackSpace.getDrawable().equals(context.getResources().getDrawable(R.drawable.ic_clear)))
                    makeTransformations(AppConstant.CLEAR_ALL);
                else
                    makeTransformations(AppConstant.DELETE_LAST);
                break;
            default: {
                TextView btn = (TextView) v;
                btnBackSpace.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_backspace));
                btnCount++;
                if (btnCount == 1) {
                    num1 = Integer.parseInt(btn.getText().toString());
                    tvTypedNo.setText(String.valueOf(num1));
                    selectedBtnId1 = btn.getId();
//                    tvBtn1 = btn;
                    makeTransformations(AppConstant.DISABLE);
                    btn.setEnabled(false);
                } else if (btnCount == 2) {
                    if (!isOperatorClicked) {
                        btnCount--;
                        appUtils.showLongToast(context, "Select an operator to perform calculation");
                    } else {
                        selectedBtnId2 = btn.getId();
//                        tvBtn2 = btn;
                        makeTransformations(AppConstant.DISABLE);
                        num2 = Integer.parseInt(btn.getText().toString());
                        Log.i("Val of num2", String.valueOf(num2));
                        tvTypedNo.setText(String.valueOf(num1) + strOperator + String.valueOf(num2));
                        switch (strOperator) {
                            case "+":
                                sum = num1 + num2;
                                break;
                            case "-":
                                sum = num1 - num2;
                                break;
                            case "*":
                                sum = num1 * num2;
                                break;
                            case "/":
                                sum = num1 / num2;
                                break;
                        }
                        btn.setEnabled(false);
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void makeTransformations(String action) {
        if (linLayContainer.getChildCount() != 0) {
            for (int a = 0; a < linLayContainer.getChildCount(); a++) {
                View linLayview = linLayContainer.getChildAt(a);
                if (linLayview instanceof TableRow) {
                    TableRow tableRow = (TableRow) linLayview;
                    for (int b = 0; b < tableRow.getChildCount(); b++) {
                        View trView = tableRow.getChildAt(b);
                        if (trView instanceof TextView) {
                            switch (action) {
                                case AppConstant.CLEAR_ALL:
                                    trView.setEnabled(true);
                                    noList.clear();
                                    noList.addAll(actualNoList);
                                    generateDynLayouts();
                                    break;
                                case AppConstant.DELETE_LAST:
                                    if (trView.getId() == selectedBtnId1) {
                                        tvTypedNo.setText("");
                                    } else if (trView.getId() == selectedBtnId2) {
                                        tvTypedNo.setText(num1 + strOperator);
                                    } else {
                                        tvTypedNo.setText(String.valueOf(num1)); //to remove any operator if available
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        } else {
            appUtils.showShortToast(context, "btn pressed");
        }
    }
}


//    String num1 = "", num2 = "";
//                                    if (trView.getId() == selectedBtnId1) {
//                                        num1 = ((TextView) trView).getText().toString();
//                                        noList.remove(((TextView) trView).getText().toString());
//                                    } else {
////                                        num2 = ((TextView) trView).getText().toString();
//                                        noList.remove(((TextView) trView).getText().toString());
//                                    }

//                                    int pos = noList.indexOf(num1);

//                                    for (int i = 0; i < noList.size(); i++) {
//                                        Log.i("calss in loop", String.valueOf(i));
//                                        if (trView.getId() == selectedBtnId1) {
////                                            num1 = ((TextView) trView).getText().toString();
//                                            noList.remove(((TextView) trView).getText().toString());
////                                            selectedBtnId1 = i;
//                                        } else if (trView.getId() == selectedBtnId2) {
////                                            num2 = ((TextView) trView).getText().toString();
////                                            selectedBtnId2 = i;
//                                            noList.remove(((TextView) trView).getText().toString());
//                                        }
//                                    }


//                                    for (Iterator<String> iterator = noList.iterator(); iterator.hasNext(); ) {
//                                        String item = iterator.next();
//                                        if (String.valueOf(num1).equals(item) || String.valueOf(num2).equals(item))
//                                            iterator.remove();
//                                        System.out.println(item);
//                                    }
//                                    appUtils.showLongToast(context, "size of array aft removing" + String.valueOf(noList.size()));