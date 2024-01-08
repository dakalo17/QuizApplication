package com.codsoft.quiz.models;

public class Option {
    private final String option;
    private final boolean isAnswer;

    public Option(String option,boolean isAnswer){
        this.option = option;
        this.isAnswer = isAnswer;
    }
    public String getOptionAnswer(){
        return option;
    }
    public boolean IsAnswer(){
        return isAnswer;
    }
}
