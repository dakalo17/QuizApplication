package com.codsoft.quiz.models;

import com.codsoft.quiz.utils.FileManagement;
import javafx.util.Pair;

import java.util.*;

public class Quiz {
    private List<String> question;
    private static int pageIndexCount;
    private static final Map<String,List<Option>> questionAnswersMap;
    private static final FileManagement fileManagement = new FileManagement();
    static {
        Map<String,List<Option>> listMap = new LinkedHashMap<>();


        fileManagement.read();
        var value = fileManagement.getNext();
        pageIndexCount = fileManagement.rowCount();
        for (int i = 0; i < fileManagement.rowCount(); i++) {

            String ques = "";
            List<Option> ops = new ArrayList<>();



            int count =0;
            while (value != null && count < 5) {

                //if its questions
                if (value.startsWith("^")) {
                    ques = value.replace("^","");
                }

                //if its options
                else {
                    if (value.startsWith("+")) {
                        value = value.replace("+","");
                        ops.add(new Option(value, true));
                    }
                    else
                        ops.add(new Option(value, false));
                }

                count++;
                value = fileManagement.getNext();
            }
            listMap.put(ques,ops);
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

    public Pair<Boolean,String> IsCorrectOption(String questionKey, String selectedOptionValue){


        for (var entry : questionAnswersMap.getOrDefault(questionKey,null)){
            if(entry.getOptionAnswer().equals(selectedOptionValue) &&
                    entry.IsAnswer())
                return new Pair<>(true,entry.getOptionAnswer());
        }
        return new Pair<>(false,"");
    }

    public String getAnswer(String questionKey){
        for (var options:questionAnswersMap.getOrDefault(questionKey,null)){
           if(options.IsAnswer()){
               return options.getOptionAnswer();
           }
        }
        return null;
    }
    public Map<String,List<Option>> getMap(){
        return questionAnswersMap;
    }

    public String getQuestion(int index){
        if(question.isEmpty())
            return null;

        return question.get(index);
    }

    public static int getPageIndices(){
        return (pageIndexCount+1)/5;
    }
}
