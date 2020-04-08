package com.sunrised.live.ui.controller;

import com.sunrised.live.LiveApplication;
import com.sunrised.live.biz.bean.Result;
import com.sunrised.live.model.RegisterResult;
import com.sunrised.live.util.RegisterUtil;
import com.sunrised.live.util.UIUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


@FXMLController
public class RegisterDialogController implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.keyField.setText(RegisterUtil.readKeyFromFile());
        String key = keyField.getText();
        String serialNumber = RegisterUtil.getMixSerialNumber();
        RegisterResult result = RegisterUtil.doRegister(key, serialNumber);
        Platform.runLater(() -> {
            UIUtil.showAlertDialog(result.getMessage(), "提示", Alert.AlertType.WARNING);
        });
    }

    @FXML
    private TextField keyField;

    @FXML
    private void handleRegister() {
        String key = keyField.getText();
        String serialNumber = RegisterUtil.getMixSerialNumber();
        RegisterUtil.saveKeyToFile(key);
        Optional<ButtonType> result = UIUtil.showAlertDialog("key已保存，是否立即关闭应用程序？", "提示", Alert.AlertType.WARNING);
        result.ifPresent(btnType -> {
            if (btnType.equals(ButtonType.OK)) {
                LiveApplication.getStage().close();
            }
        });
    }
}
