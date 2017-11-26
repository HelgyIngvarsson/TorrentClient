package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    int lY =15;
    File torrentFile;
    File outputDir;

    @FXML
    AnchorPane anch;
    @FXML
    AnchorPane downloadList;
    @FXML
    Label torPath;
    @FXML
    Label dirPath;

    final FileChooser fileChooser = new FileChooser();
    final DirectoryChooser dirChooser = new DirectoryChooser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    void chooseFile(){
        File file = fileChooser.showOpenDialog(anch.getScene().getWindow());
        if (file != null) {
            torrentFile = file;
            torPath.setText(torPath.getText()+" "+file.getAbsolutePath());
        }
    }

    @FXML
    void chooseDir(){
        File file = dirChooser.showDialog(anch.getScene().getWindow());
        if (file != null) {
            outputDir = file;
            dirPath.setText(dirPath.getText()+" "+file.getAbsolutePath());
        }
    }
    @FXML
    void download() throws IOException, NoSuchAlgorithmException {
        Label l = new Label(torrentFile.getName());
        Button b = new Button("Del");
        l.setLayoutY(lY);
        l.setMaxWidth(280);
        l.setWrapText(true);
        b.setLayoutY(lY-5);
        b.setLayoutX(280);
        lY+=30;
        ClientHandler cl = new ClientHandler(outputDir.getAbsolutePath(),torrentFile.getAbsolutePath());
        cl.start();
        torPath.setText("Path to torrent:");
        dirPath.setText("Download directory:");
        downloadList.getChildren().addAll(l,b);
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cl.del();
                cl.stop();
                downloadList.getChildren().removeAll(l,b);
                lY-=30;
            }
        });
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(1000),
                        ae -> {
                            l.setText(cl.getStatus());
                        }
                )
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

}
