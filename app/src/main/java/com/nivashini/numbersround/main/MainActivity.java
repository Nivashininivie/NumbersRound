package com.nivashini.numbersround.main;

import android.os.Bundle;

import com.nivashini.numbersround.R;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.main_act_frag_container,new SelectNoFrag(), SelectNoFrag.class.getName()).commit();
    }
}