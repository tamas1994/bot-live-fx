package com.sunrised.live.ui.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.stage.Stage;
import lombok.Getter;

import javax.annotation.PostConstruct;

@FXMLView(value = "/view/LiveList.fxml")
public class StartView extends AbstractFxmlView {

    @Getter
    private Stage stage;

    @PostConstruct
    protected void initUI() throws Exception{
        stage = getStage();
    }
}
