package com.codsoft.quiz.models;

import java.io.Serializable;

public class Option {
    private final String option;
    private final boolean isAnswer;

    public Option(String option,boolean isAnswer){
        this.option = option;
        this.isAnswer = isAnswer;
    }
    public String getOption(){
        return option;
    }
    public boolean IsAnswer(){
        return isAnswer;
    }
}
