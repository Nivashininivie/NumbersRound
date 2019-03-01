package com.nivashini.numbersround;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.nivashini.numbersround.utilspkg.AppUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int[] smallNosArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    int[] bigNosArray = {25, 50, 75, 100};
    int selectedBtnId = -1, oldBtnId = -1, btnClickCount = 0;

    LinearLayout linLayViewCont;
    MaterialButton btnGenerate;
    TextView tvSelectedNos;

    AppUtils appUtils = new AppUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linLayViewCont = findViewById(R.id.linLayParent);
        btnGenerate = findViewById(R.id.btnGenerate);
        tvSelectedNos = findViewById(R.id.tvSelectedNos);

        generateDynLayouts();

//        Collections.shuffle();
    }

    private void generateDynLayouts() {
        TableRow trBigNoCont = new TableRow(this);
        TableRow trSmallNocont1 = new TableRow(this);
        TableRow trSmallNocont2 = new TableRow(this);
        TableRow trSmallNocont3 = new TableRow(this);
        TableRow trSmallNocont4 = new TableRow(this);
        TableRow.LayoutParams trLayoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        trLayoutParams.setMargins(16, 16, 16, 16);
        List<Integer> bigNolist = new ArrayList<>(), smallNoList = new ArrayList<>();
        for (int i : bigNosArray)
            bigNolist.add(i);
        Collections.shuffle(bigNolist, new Random());
        for (int j : smallNosArray)
            smallNoList.add(j);
        Collections.shuffle(smallNoList, new Random());

        for (int i = 0; i < bigNolist.size(); i++) {
            MaterialButton button = new MaterialButton(this);
            button.setId(i);
            button.setOnClickListener(this);
            generateButton(trBigNoCont, button, String.valueOf(bigNolist.get(i)), 24, 100, 24, 100);
        }
        for (int j = 0; j < smallNoList.size(); j++) {
            if (j < 5) {
                MaterialButton button = new MaterialButton(this);
                button.setId(j);
                button.setOnClickListener(this);
                trSmallNocont1.setWeightSum(5);
                generateButton(trSmallNocont1, button, String.valueOf(smallNoList.get(j)), 8, 75, 8, 75);
            } else if (j < 10) {
                trSmallNocont2.setWeightSum(5);
                MaterialButton button = new MaterialButton(this);
                button.setId(j);
                button.setOnClickListener(this);
                generateButton(trSmallNocont2, button, String.valueOf(smallNoList.get(j)), 8, 75, 8, 75);
            }
        }
        Collections.shuffle(smallNoList, new Random());
        for (int j = 0; j < smallNoList.size(); j++) {
            if (j < 5) {
                MaterialButton button = new MaterialButton(this);
                trSmallNocont1.setWeightSum(5);
                button.setId(j);
                button.setOnClickListener(this);
                generateButton(trSmallNocont3, button, String.valueOf(smallNoList.get(j)), 8, 75, 8, 75);
            } else if (j < 10) {
                trSmallNocont2.setWeightSum(5);
                MaterialButton button = new MaterialButton(this);
                button.setId(j);
                // set other stuff and add to layout
                button.setOnClickListener(this);
                generateButton(trSmallNocont4, button, String.valueOf(smallNoList.get(j)), 8, 75, 8, 75);
            }
        }
        linLayViewCont.addView(trBigNoCont);
        linLayViewCont.addView(trSmallNocont1);
        linLayViewCont.addView(trSmallNocont2);
        linLayViewCont.addView(trSmallNocont3);
        linLayViewCont.addView(trSmallNocont4);
    }

    public void generateButton(TableRow tableRow, MaterialButton button, String text, int paddingStart, int paddingTop, int paddingEnd, int paddingBottom) {
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
        ArrayList<Integer> selectedNosList = new ArrayList<>();
        Button btn = (Button) v;
        btnClickCount++;
        if (btnClickCount > 6) {
            appUtils.showLongToast(this, "You have reached maximum no of clicks. Click generate button to proceed");
        }else {
            btn.setTextSize(14);
            selectedBtnId = v.getId();
            if (oldBtnId == -1) {
                oldBtnId = v.getId();
                selectedBtnId = v.getId();
                String selectedNo = btn.getText().toString();
                selectedNosList.add(Integer.parseInt(selectedNo));
            } else {
                selectedBtnId = v.getId();
                if (oldBtnId == selectedBtnId) {
                    btnClickCount--;
                    Log.i("oldBtnId", String.valueOf(oldBtnId));
                    Log.i("selectedBtnId", String.valueOf(selectedBtnId));
                    appUtils.showLongToast(this, "You have already selected this button");
                    return;
                } else {
                    Log.i("oldBtnId1", String.valueOf(oldBtnId));
                    Log.i("selectedBtnId1", String.valueOf(selectedBtnId));
                    String selectedNo = btn.getText().toString();
                    selectedNosList.add(Integer.parseInt(selectedNo));
                    String nos = android.text.TextUtils.join(",", selectedNosList);
                    tvSelectedNos.setText(nos);
                }
                oldBtnId = selectedBtnId;
            }
        }
    }
}