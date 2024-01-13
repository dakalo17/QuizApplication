package com.codsoft.quiz;

import com.codsoft.quiz.models.Option;
import com.codsoft.quiz.models.Quiz;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {


    @FXML
    private Label lblTotalScore;
    @FXML
    private TableView<ObservableList<String>> tbvMain;
    @FXML
    private Button btnNextPage;
    @FXML
    private Button btnConfirm;
    @FXML
    private Button btnStart;
    @FXML
    private Label lblTimer;
    @FXML
    private Label lblQuestionNumber;
    @FXML
    private TextArea txtQuestionText;

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

    private ObservableList<ObservableList<String>> rowEntryList;

    private ToggleGroup toggleGroupOptions;
    private List<Map.Entry<String,List<Option>>> page;
    private int pageIndex;
    private int seconds;
    private int scoreSum;

    private static final int QUIZ_MAX_DURATION = 10;
    private Timeline timeline;

    private final static List<String> DEFAULT_COLUMNS;
    static {
        DEFAULT_COLUMNS = List.of("Question#", "Correct Answer", "Score");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        setColumnFactory();
        rowEntryList = FXCollections.observableArrayList();
        //int index = MainPgn.currentPageIndexProperty().get();

        toggleGroupOptions = new ToggleGroup();
        rbtnQ1.setToggleGroup(toggleGroupOptions);
        rbtnQ2.setToggleGroup(toggleGroupOptions);
        rbtnQ3.setToggleGroup(toggleGroupOptions);
        rbtnQ4.setToggleGroup(toggleGroupOptions);


        quiz = new Quiz();

        //hideQuestion();

    }

    private void start() {
        paginate();
        setQuestions();
        setTimer();
    }

    private void paginate(){
        page = new ArrayList<>();

        for (var entry : quiz.getEntrySet()) {

            //adding (question+answers) as an element
            page.add(new AbstractMap.SimpleEntry<>(entry));

        }
    }

    private void hideQuestion(){
        if(pageIndex < page.size()) {
            rbtnQ1.setText("***");
            rbtnQ2.setText("***");
            rbtnQ3.setText("***");
            rbtnQ4.setText("***");

            txtQuestionText.setText("******");
            lblQuestionNumber.setText("Question ".concat(String.valueOf((pageIndex+1))));

        }

    }
    private void setQuestions(){

        if(pageIndex < page.size()) {
            var options = page.get(pageIndex).getValue();

            rbtnQ1.setText(options.get(0).getOptionAnswer());
            rbtnQ2.setText(options.get(1).getOptionAnswer());
            rbtnQ3.setText(options.get(2).getOptionAnswer());
            rbtnQ4.setText(options.get(3).getOptionAnswer());

            txtQuestionText.setText(page.get(pageIndex).getKey());

            lblQuestionNumber.setText("Question ".concat(String.valueOf((pageIndex+1))));
        }else{
            //the final page should show the score
            //spMain.


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
        var correctOption = quiz.IsCorrectOption(txtQuestionText.getText(),selectedOptionValue);

        if(correctOption.getKey()){

            if(seconds >0)
                CustomDialog.show("Quiz Confirm","You answered correctly.");
            else
                CustomDialog.show("Quiz Confirm","You have ran out of time and answered correctly.");



            btnConfirm.setDisable(true);
        }else {

            if(seconds > 0)
                CustomDialog.show("Quiz Confirm","You answered incorrectly.");
            else
                CustomDialog.show("Quiz Confirm","You have ran out of time and answered incorrectly.");


            btnConfirm.setDisable(true);

        }

        var answer = quiz.getAnswer(txtQuestionText.getText());
        setTable1(answer,correctOption.getKey());
        disableOptions();
        btnNextPage.setDisable(false);



    }

    private void setTable1(String selectedOptionValue,boolean isCorrect) {

        var score =0 ;
        if(isCorrect)
            score = Math.abs( QUIZ_MAX_DURATION - Math.abs(seconds - QUIZ_MAX_DURATION) );
        timeline.stop();
        setTable(selectedOptionValue,score);
    }

    @FXML
    protected void onStartClick(ActionEvent actionEvent) {
        seconds=QUIZ_MAX_DURATION;
        start();
        btnStart.setDisable(true);
        btnConfirm.setDisable(false);
        btnNextPage.setDisable(true);

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
                       // CustomDialog.show("Quiz Answer","You have run out of time, the options have been disabled");
                        disableOptions();
                        btnConfirm.fire();
                        btnConfirm.setDisable(true);
                        timeline.stop();
                    }
                    //decrement the seconds variable each second
                    lblTimer.setText(String.valueOf(seconds--));

                });
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }


    @FXML
    protected void onLeftPageClick(ActionEvent event) {
    //omitted
    }

    private void nextPage(){
        pageIndex++;
        hideQuestion();
        btnStart.setDisable(false);

    }
    @FXML
    protected void onNextPageClick(ActionEvent event) {
        //if its the last page
        if(pageIndex == page.size()-1 ){btnNextPage.setDisable(true);return;}
        nextPage();
        btnNextPage.setDisable(true);
        if(toggleGroupOptions.getSelectedToggle() != null)
            toggleGroupOptions.getSelectedToggle().setSelected(false);

        //meaning the last page
        if(pageIndex >= page.size()){
            btnNextPage.setDisable(true);
        }
    }

    private void setColumnFactory(){

        for (int i = 0; i < DEFAULT_COLUMNS.size(); i++) {
            String colName = DEFAULT_COLUMNS.get(i);

            TableColumn<ObservableList<String>,String> col = new TableColumn<>(colName);

            final int colIndex = i;
            col.setCellValueFactory(val -> {
                var value = val.getValue();

                return new SimpleStringProperty(value.get(colIndex));
            });
            tbvMain.getColumns().add(col);

        }
        tbvMain.setItems(rowEntryList);
    }
    private void setTable(String correctAnswer,int score){

        ObservableList<String> ol = FXCollections.observableArrayList();



        //question
        ol.add(String.valueOf(pageIndex+1));
        //correct answer
        ol.add(correctAnswer);
        //score
        ol.add(String.valueOf(score));

        rowEntryList.add(ol);

        tbvMain.setItems(rowEntryList);
        scoreSum += score;
        lblTotalScore.setText(String.valueOf(scoreSum));

    }
}