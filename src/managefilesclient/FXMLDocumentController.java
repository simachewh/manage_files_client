/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managefilesclient;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import managefilesclient.sime.seven.ClientHandler;

/**
 *
 * @author Simachew
 */
public class FXMLDocumentController implements Initializable {

    ClientHandler clientHandler = new ClientHandler();
    ObservableList<String> content;
    @FXML
    GridPane root_gridPane;
    @FXML
    public Label listLabel;
    @FXML
    public BorderPane root_borderPane;
    @FXML
    public Button refreshButton;
    @FXML
    public ListView<String> filesListView;
    @FXML
    public Button downloadButton;
    @FXML
    public Button uploadButton;
    @FXML
    public Pane previewPane;
    @FXML
    public Pane controlsPane;

    @FXML
    Button nextButton;
    @FXML
    Button previousButton;

    ImageView imageView;
    TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert root_borderPane != null : "fx:id=\"root_boarderPane\" was not "
                + "injected check the FXML file";
        assert refreshButton != null : "refreshButton was not injected, check"
                + " your FXML file";
        assert previewPane != null : "previewPane was not injected, check"
                + " your FXML file";
        assert controlsPane != null : "controlsPane was not injected, check"
                + " your FXML file";
        assert downloadButton != null : "downloadButton was not injected, check"
                + " your FXML file";
        downloadButton.setDisable(true);
        assert uploadButton != null : "uploadButton was not injected, check"
                + " your FXML file";
        assert listLabel != null : "listLabel was not initialized, check "
                + "your FXML file";
        assert filesListView != null : "filesListView was not initialized, check "
                + "your FXML file";
        assert nextButton != null : "nextButton was not initialized, check "
                + "your FXML file";
        assert previousButton != null : "previousButton was not initialized, check "
                + "your FXML file";

        content = FXCollections
                .observableArrayList(clientHandler.getListOfFiles());
        if (content != null) {
            filesListView.setItems(content);
            filesListView.getSelectionModel().select(0);
            onListViewClicked();
        } 

    }

    @FXML
    public void onRefreshButtonClicked(MouseEvent event) {
        clientHandler = new ClientHandler();
        content = FXCollections
                .observableArrayList(clientHandler.getListOfFiles());
        filesListView.setItems(content);
    }

    @FXML
    public void onUploadButtonClicked(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFile = fileChooser.showOpenDialog(root_borderPane.getScene().getWindow());
        if (selectedFile != null) {
            clientHandler = new ClientHandler();
            clientHandler.upload(selectedFile);
        }
        onRefreshButtonClicked(event);
    }

    @FXML
    public void onDownloadButtonClicked(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName(filesListView.getSelectionModel().getSelectedItem());
        File toSaveoOn = fileChooser.showSaveDialog(root_borderPane.getScene().getWindow());

        String fileNameInTheList = filesListView.getSelectionModel().getSelectedItem();
        if (toSaveoOn != null) {
            clientHandler = new ClientHandler();
            clientHandler.download(fileNameInTheList, toSaveoOn);
        }

    }

    @FXML
    public void onListViewClicked() {
        //list will be empty if the app is running for the first time
        //therefore check for null and empty result comming from list
        String selected = "";
        if (filesListView.getSelectionModel() != null) {
            downloadButton.setDisable(false);
            selected = filesListView.getSelectionModel().getSelectedItem();
        }
        if (selected == null || selected.isEmpty()) {
            return;
        }
        String[] split = selected.split("\\.");
        String ext = split[1];
        if (ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg")) {
            clientHandler = new ClientHandler();
            imagePreview(clientHandler.preview(selected));
        } else if (ext.equalsIgnoreCase("txt")) {
            clientHandler = new ClientHandler();
            textPreview(clientHandler.preview(selected));
        }

    }

    public void onNextButtonClicked() {
        filesListView.getSelectionModel().selectNext();
        onListViewClicked();
    }

    public void onPrevioustButtonClicked() {
        filesListView.getSelectionModel().selectPrevious();
        onListViewClicked();
    }

    private void imagePreview(InputStream is) {
        Image image = new Image(is);
        imageView = new ImageView(image);
        imageView.setPreserveRatio(false);
        imageView.fitWidthProperty().bind(previewPane.widthProperty());
        imageView.fitHeightProperty().bind(previewPane.heightProperty());
        previewPane.getChildren().clear();
        previewPane.getChildren().add(imageView);
        previewPane.requestLayout();
    }

    private void textPreview(InputStream is) {
        textArea = new TextArea();
        textArea.resize(previewPane.getWidth(), previewPane.getHeight());
        Scanner s = new Scanner(is);
        while (s.hasNextLine()) {
            textArea.appendText(s.nextLine());
            textArea.appendText(System.getProperty("line.separator"));
        }
        previewPane.getChildren().clear();
        previewPane.getChildren().add(textArea);
        previewPane.requestLayout();
    }

}
