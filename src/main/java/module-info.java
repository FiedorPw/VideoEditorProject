module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires ffmpeg;
    requires org.slf4j;


    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1;
}