package com.nivashini.numbersround.main;


import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.google.android.material.button.MaterialButton;
import com.nivashini.numbersround.R;
import com.nivashini.numbersround.utils.AppConstant;
import com.nivashini.numbersround.utils.AppUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SelectNoFrag extends Fragment implements View.OnClickListener {

    private int[] smallNosArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private int[] bigNosArray = {25, 50, 75, 100};
    private int oldBtnId = -1, btnClickCount = 0;
    private String strGeneratedNo = "", formula = "";
    private LinearLayout linLayViewCont;
    private AppUtils appUtils = new AppUtils();
    private ArrayList<Integer> selectedNosList = new ArrayList<>();
    private ArrayList<String> arrayListSymbol = new ArrayList<>();
    private Context context;

    public SelectNoFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_number, container, false);

        if (getActivity() != null) {
            context = getActivity();
            appUtils.setToolBarTitle(((MainActivity) getActivity()).getSupportActionBar(), AppConstant.SELECTED_NOS);
        }

        linLayViewCont = view.findViewById(R.id.linLayParent);
        MaterialButton btnGenerate = view.findViewById(R.id.btnGenerate);
        btnGenerate.setOnClickListener(this);

        //Generate buttons with numbers hidden
        generateDynLayouts();

        //adding arithmetic operators in a list and shuffling it
        arrayListSymbol.add("+");
        arrayListSymbol.add("-");
        arrayListSymbol.add("*");
        arrayListSymbol.add("/");
        Collections.shuffle(arrayListSymbol);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        selectedNosList.clear();
        btnClickCount = 0;
    }

    private void generateDynLayouts() {
        TableRow trBigNoCont = new TableRow(context);
        TableRow trSmallNocont1 = new TableRow(context);
        TableRow trSmallNocont2 = new TableRow(context);
        TableRow trSmallNocont3 = new TableRow(context);
        TableRow trSmallNocont4 = new TableRow(context);
        TableRow.LayoutParams trLayoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        trLayoutParams.setMargins(16, 16, 16, 16);
        List<Integer> bigNolist = new ArrayList<>(), smallNoList = new ArrayList<>();

        //shuffling the array value to show on button randomly
        for (int i : bigNosArray)
            bigNolist.add(i);
        Collections.shuffle(bigNolist, new Random());
        for (int j : smallNosArray)
            smallNoList.add(j);
        Collections.shuffle(smallNoList, new Random());

        for (int i = 0; i < bigNolist.size(); i++) {
            MaterialButton button = new MaterialButton(context);
            button.setId(View.generateViewId());
            button.setOnClickListener(this);
            generateButton(trBigNoCont, button, String.valueOf(bigNolist.get(i)), 24, 100, 24, 100);
        }
        for (int j = 0; j < smallNoList.size(); j++) {
            MaterialButton button = new MaterialButton(context);
            button.setId(View.generateViewId());
            button.setOnClickListener(this);
            if (j < 5) {
                trSmallNocont1.setWeightSum(5);
                generateButton(trSmallNocont1, button, String.valueOf(smallNoList.get(j)), 8, 75, 8, 75);
            } else if (j < 10) {
                trSmallNocont2.setWeightSum(5);
                generateButton(trSmallNocont2, button, String.valueOf(smallNoList.get(j)), 8, 75, 8, 75);
            }
        }
        Collections.shuffle(smallNoList, new Random());
        for (int j = 0; j < smallNoList.size(); j++) {
            MaterialButton button = new MaterialButton(context);
            button.setId(View.generateViewId());
            button.setOnClickListener(this);
            if (j < 5) {
                trSmallNocont3.setWeightSum(5);
                generateButton(trSmallNocont3, button, String.valueOf(smallNoList.get(j)), 8, 75, 8, 75);
            } else if (j < 10) {
                trSmallNocont4.setWeightSum(5);
                generateButton(trSmallNocont4, button, String.valueOf(smallNoList.get(j)), 8, 75, 8, 75);
            }
        }
        linLayViewCont.addView(trBigNoCont);
        linLayViewCont.addView(trSmallNocont1);
        linLayViewCont.addView(trSmallNocont2);
        linLayViewCont.addView(trSmallNocont3);
        linLayViewCont.addView(trSmallNocont4);
    }

    // set other stuff and add to layout
    private void generateButton(TableRow tableRow, MaterialButton button, String text, int paddingStart, int paddingTop, int paddingEnd, int paddingBottom) {
        TableRow.LayoutParams trLayoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        trLayoutParams.setMargins(16, 16, 16, 16);
        int[] androidColors = getResources().getIntArray(R.array.random_colors_array);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        button.setCornerRadius(16);
        button.setLayoutParams(trLayoutParams);
        button.setText(text);
        button.setGravity(Gravity.CENTER);
        button.setTextSize(0);
        button.setBackgroundColor(randomAndroidColor);
        button.getBackground().setColorFilter(randomAndroidColor, PorterDuff.Mode.DST_OVER);
        button.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom);
        tableRow.addView(button);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGenerate) {
            if (selectedNosList.size() < 6) {
                appUtils.showLongToast(context, "Please select 6 numbers to proceed");
            } else {
                boolean isNoGenerated = generateRandomNumber();
                if (isNoGenerated) {
                    String nos = android.text.TextUtils.join(",", selectedNosList);
                    RoundNoFrag roundNoFrag = new RoundNoFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstant.SELECTED_NOS, nos);
                    bundle.putString(AppConstant.FORMULAE, formula);
                    bundle.putString(AppConstant.GENERATED_NO, strGeneratedNo);
//                bundle.putIntegerArrayList(AppConstant.SELECTED_NOS, selectedNosList);
                    roundNoFrag.setArguments(bundle);
                    appUtils.goToFragment(context, R.id.main_act_frag_container, roundNoFrag, true);
                    selectedNosList.clear();
                } else {

                }
            }

        } else {
            Button btn = (Button) v;
            btnClickCount++;
            if (btnClickCount > 6) {
                appUtils.showLongToast(context, "You have reached maximum no of clicks. Click generate button to proceed");
            } else {
                btn.setTextSize(14);
                int selectedBtnId;
                if (oldBtnId == -1) {
                    oldBtnId = v.getId();
                    selectedBtnId = v.getId();
                    String selectedNo = btn.getText().toString();
                    selectedNosList.add(Integer.parseInt(selectedNo));
                } else {
                    selectedBtnId = v.getId();
                    if (oldBtnId == selectedBtnId) {
                        btnClickCount--;
                        appUtils.showLongToast(context, "You have already selected this button");
                        return;
                    } else {
                        String selectedNo = btn.getText().toString();
                        selectedNosList.add(Integer.parseInt(selectedNo));
                    }
                    oldBtnId = selectedBtnId;
                }
            }
        }
    }

    private boolean generateRandomNumber() {
        Collections.shuffle(selectedNosList);
        double total = selectedNosList.get(0);
        StringBuilder stringBufferFormulae = new StringBuilder();
        int j = 0;
        for (int i = 1; i < selectedNosList.size(); i++) {
            boolean sus = false;
            String s1;
            if (i != 1) s1 = "\n";
            else s1 = String.valueOf((int) total);

            if (arrayListSymbol.get(j).equals("/")) {
                if (!isDouble(String.valueOf(total / selectedNosList.get(i)))) {
                    sus = true;
                    total = total / selectedNosList.get(i);
                    stringBufferFormulae.append(s1).append("/").append(selectedNosList.get(i));
                }
            } else if (arrayListSymbol.get(j).equals("-")) {
                if (total - selectedNosList.get(i) > 0) {
                    stringBufferFormulae.append(s1).append("-").append(selectedNosList.get(i));
                    total = total - selectedNosList.get(i);
                    sus = true;
                }
            }
            if (!sus) {
                if (arrayListSymbol.get(j).equals("+")) {
                    stringBufferFormulae.append(s1).append("+").append(selectedNosList.get(i));
                    total = total + selectedNosList.get(i);
                } else if (arrayListSymbol.get(j).equals("*")) {
                    stringBufferFormulae.append(s1).append("*").append(selectedNosList.get(i));
                    total = total * selectedNosList.get(i);
                }
            }

            if (j == 3) {
                j = 0;
                Collections.shuffle(arrayListSymbol);
            } else
                j++;
        }

//        Log.i("formulae", stringBufferFormulae.toString());
//        Log.i("total", String.valueOf(total));

        if (total < 1 || total > 999) {
            generateRandomNumber();
            return false;
        } else {
            strGeneratedNo = String.valueOf(Math.round(total));
            formula = stringBufferFormulae.toString();
            Log.i("formula", formula);
            return true;
        }
    }

    private boolean isDouble(String str) {
        try {
            double myDouble = Double.parseDouble(str);
            myDouble -= (int) myDouble; //this way you are making the (for example) 10.3 = 0.3
            return !(myDouble == 0);
            /*return myDouble == 0;*/ //this way you check if the result is not zero. if it's zero it was an integer, elseway it was a double
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
