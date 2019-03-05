package com.nivashini.numbersround.datatransaction;

public class MStepDetails {
    String firstNo = "", operator = "", secondNo = "", result = "";

    public MStepDetails(String firstNo, String operator, String secondNo, String result) {
        this.firstNo = firstNo;
        this.operator = operator;
        this.secondNo = secondNo;
        this.result = result;
    }

    public String getFirstNo() {
        return firstNo;
    }

    public String getOperator() {
        return operator;
    }

    public String getSecondNo() {
        return secondNo;
    }

    public String getResult() {
        return result;
    }
}
