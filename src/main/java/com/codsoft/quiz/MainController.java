package com.codsoft.quiz;

import com.codsoft.quiz.models.Option;
import com.codsoft.quiz.models.Quiz;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class MainController implements Initializable {


    @FXML
    private Button btnConfirm;
    @FXML
    private Button btnStart;
    @FXML
    private Label lblTimer;
    @FXML
    private Label lblQuestionNumber;
    @FXML
    private Label lblQuestionText;
    @FXML
    private Pagination MainPgn;
    @FXML
    private RadioButton rbtnQ1;
    @FXML
    private RadioButton rbtnQ2;
    @FXML
    private RadioButton rbtnQ3;
    @FXML
    private RadioButton rbtnQ4;


    private Quiz quiz;
    private List<Option> questions ;


    private ToggleGroup toggleGroupOptions;
    private List<Map.Entry<String,List<Option>>> page;
    private int pageIndex;
    private int seconds = 2;

    private Timeline timeline;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questions = new ArrayList<>();

        //int index = MainPgn.currentPageIndexProperty().get();

        toggleGroupOptions = new ToggleGroup();
        rbtnQ1.setToggleGroup(toggleGroupOptions);
        rbtnQ2.setToggleGroup(toggleGroupOptions);
        rbtnQ3.setToggleGroup(toggleGroupOptions);
        rbtnQ4.setToggleGroup(toggleGroupOptions);


        quiz = new Quiz();

        //questionAnswers = unmodifiableObservableMap(observableMap(quiz.getMap()));
        //start();

    }

    private void start() {
        paginate();
        setQuestions();
        setPageEvents();
        setTimer();
    }

    private void setPageEvents(){


        MainPgn.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldNumber, Number newNumber) {
                pageIndex = newNumber.intValue();

                System.out.println(""+pageIndex);


                setQuestions();
            }
        });
    }

    private void setNewPage(){

    }
    private void paginate(){

        MainPgn.setPageCount(Quiz.getPageIndices());
        page = new ArrayList<>();


        for (var entry : quiz.getEntrySet()) {

            //adding (question+answers) as an element
            page.add(new AbstractMap.SimpleEntry<>(entry));

        }
    }

    private void setQuestions(){

        if(pageIndex <= page.size()) {
            var options = page.get(pageIndex).getValue();

            rbtnQ1.setText(options.get(0).getOptionAnswer());
            rbtnQ2.setText(options.get(1).getOptionAnswer());
            rbtnQ3.setText(options.get(2).getOptionAnswer());
            rbtnQ4.setText(options.get(3).getOptionAnswer());

            lblQuestionText.setText(page.get(pageIndex).getKey());

            lblQuestionNumber.setText("Question ".concat(String.valueOf((pageIndex+1))));
        }
    }

    @FXML
    protected void onOptionClick(ActionEvent actionEvent) {
        if(actionEvent.getSource() instanceof RadioButton radioButton){

            //System.out.println(radioButton.getText());


        }
    }

    @FXML
    protected void onConfirmClick(ActionEvent actionEvent) {
        RadioButton selectedOption = (RadioButton) toggleGroupOptions.getSelectedToggle();
        var toggles = toggleGroupOptions.getToggles();
        boolean hasSelectedToggle = false;

        for (var toggle : toggles){
            if(toggle.isSelected()) hasSelectedToggle =true;
        }

        if(!hasSelectedToggle&&seconds>0){
            CustomDialog.show("Quiz Confirm","You must select an option in order to answer.");
            return;
        }

        String selectedOptionValue = "";
        if(selectedOption != null)
         selectedOptionValue= selectedOption.getText();

        //if it's true that means it is the correct option
        var correctOption = quiz.IsCorrectOption(lblQuestionText.getText(),selectedOptionValue);
        if(correctOption){

            if(seconds >0)
                CustomDialog.show("Quiz Confirm","You answered correctly.");
            else
                CustomDialog.show("Quiz Confirm","You have ran out of time and answered correctly.");


        }else {

            if(seconds > 0)
                CustomDialog.show("Quiz Confirm","You answered incorrectly.");
            else
                CustomDialog.show("Quiz Confirm","You have ran out of time and answered incorrectly.");

        }

    }

    @FXML
    protected void onStartClick(ActionEvent actionEvent) {
        start();
        btnStart.setDisable(true);
        btnConfirm.setDisable(false);

        MainPgn.setDisable(false);
        enableOptions();
    }

    private void enableOptions() {
        rbtnQ1.setDisable(false);
        rbtnQ2.setDisable(false);
        rbtnQ3.setDisable(false);
        rbtnQ4.setDisable(false);
    }
    private void disableOptions() {
        rbtnQ1.setDisable(true);
        rbtnQ2.setDisable(true);
        rbtnQ3.setDisable(true);
        rbtnQ4.setDisable(true);
    }

    private void setTimer(){
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),
                actionEvent -> {


                    //decrement from 60 to 0 ,every second
                    if(seconds <= 0) {
                       // CustomDialog.show("Quiz Answer","You have ran out of time, the options have been disabled");
                        disableOptions();
                        btnConfirm.fire();
                        timeline.stop();
                    }
                    lblTimer.setText(String.valueOf(seconds--));

                });
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


}