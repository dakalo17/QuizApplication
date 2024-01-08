package com.codsoft.quiz.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class FileManagement {
    //data reference https://starwalk.space/en/quiz/middle-school-trivia
    private final LinkedList<String> readFile;
    private final Scanner scanner;
    private InputStream inputStream;
    public FileManagement(){

        //retrieve the questions and corresponding answers
        inputStream =
                getClass().getResourceAsStream("/com/codsoft/quiz/data/data.txt");


        scanner = new Scanner(Objects.requireNonNull(inputStream));

        readFile = new LinkedList<>();

    }

    public void read(){


        while (scanner.hasNextLine()) {
            readFile.addLast(scanner.nextLine());
        }


        try{
            if(inputStream != null)
                inputStream.close();


        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public String getNext(){
        var value= readFile.poll();
        if(value == null && scanner!=null)
            scanner.close();

        return value;
    }

    public int rowCount(){
        return readFile.size();
    }
}
