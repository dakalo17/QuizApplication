module com.codsoft.quiz {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.codsoft.quiz to javafx.fxml;
    exports com.codsoft.quiz;
}