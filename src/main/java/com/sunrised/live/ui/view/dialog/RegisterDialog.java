package com.sunrised.live.ui.view.dialog;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;

@FXMLView("/view/RegisterDialog.fxml")
public class RegisterDialog extends AbstractFxmlView {

    @Getter
    private Stage stage;

    @Getter
    @Setter
    private String key;

    @PostConstruct
    protected void initUI() throws Exception{

    }
}
