package com.codsoft.quiz;

import com.codsoft.quiz.models.Option;
import com.codsoft.quiz.models.Quiz;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {


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
        paginate();
        setQuestions();
        setPageEvents();

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

            rbtnQ1.setText(options.get(0).getOption());
            rbtnQ2.setText(options.get(1).getOption());
            rbtnQ3.setText(options.get(2).getOption());
            rbtnQ4.setText(options.get(3).getOption());

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
        if(selectedOption.getText().isEmpty()){
            //dialog
            return;
        }
        String selectedOptionKey = selectedOption.getText();

        quiz.getOptions(selectedOptionKey);

    }
}