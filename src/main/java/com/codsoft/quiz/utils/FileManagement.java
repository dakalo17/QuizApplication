package com.codsoft.quiz.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class FileManagement {

    private final LinkedList<String> readFile;
    private final Scanner scanner;
    private InputStream inputStream;
    public FileManagement(){
        inputStream =
                getClass().getResourceAsStream("/com/codsoft/quiz/data/data.txt");
        if(inputStream == null)
            System.out.println("ERRRRR");
        scanner = new Scanner(inputStream);

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
