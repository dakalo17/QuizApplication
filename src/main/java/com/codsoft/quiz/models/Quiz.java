package com.codsoft.quiz.models;

import java.util.*;

public class Quiz {
    private List<String> question;
    private static final Map<String,List<Option>> questionAnswersMap;

    static {
        Map<String,List<Option>> listMap = new LinkedHashMap<>();

        for (int j = 0; j < 10; j++) {

            List<Option> options = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                if(i == 1)
                    options.add(new Option("ans#"+(i+1) +"-#" + (j+1),true));
                else
                    options.add(new Option("ans#"+(i+1) +"-#" + (j+1),false));
            }



            listMap.put("q"+(j+1),options);
        }

        questionAnswersMap = Collections.unmodifiableMap(listMap);


    }

    public Quiz(){

    }
    public Quiz(List<String> question){
        this();
        this.question.addAll(question);
    }
    public Set<Map.Entry<String,List<Option>>> getEntrySet(){
        return questionAnswersMap.entrySet();
    }

    public List<Option> getOptions(String questionKey){
        return questionAnswersMap.get(questionKey);
    }

    public Map<String,List<Option>> getMap(){
        return questionAnswersMap;
    }

    public String getQuestion(int index){
        if(question.isEmpty())
            return null;

        return question.get(index);
    }
}
