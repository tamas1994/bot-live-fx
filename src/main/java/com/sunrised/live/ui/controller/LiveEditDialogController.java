package com.sunrised.live.ui.controller;

import com.sunrised.live.model.Live;
import com.sunrised.live.ui.view.StartView;
import com.sunrised.live.ui.view.dialog.LiveEditDialog;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class LiveEditDialogController implements Initializable {

    @FXML
    private TextField livePageUrlField;
    @FXML
    private TextField pathField;
    @FXML
    private TextField saveNameField;
    @FXML
    private TextField statusField;

    @Autowired
    private LiveEditDialog liveEditDialog;

    @Autowired
    private StartView startView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void handleOk() {
        Live live = new Live(livePageUrlField.getText(), pathField.getText(), saveNameField.getText(), statusField.getText());
        livePageUrlField.setText(null);
        //pathField.setText(null);
        saveNameField.setText(null);
        liveEditDialog.setLive(live);
        liveEditDialog.getStage().close();
    }

    @FXML
    private void handleCancel() {
        liveEditDialog.getStage().close();
    }


}
