package com.sunrised.live.ui.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import com.sunrised.live.service.MsgService;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 */
@FXMLController
public class StartController implements Initializable {

    @Autowired
    private MsgService msgService;

    @FXML
    private TextField txtSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onSearch(){
        System.out.println(msgService.sendMsg());
    }

    @PostConstruct
    public void initTypeTree(){

    }
}
