package com.sunrised.live.ui.view.dialog;

import com.sunrised.live.model.Live;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import de.felixroske.jfxsupport.GUIState;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;

@FXMLView("/view/LiveEditDialog.fxml")
public class LiveEditDialog extends AbstractFxmlView {

    @Getter
    private Stage stage;

    @Setter
    @Getter
    private Live live;

    @PostConstruct
    protected void initUI() throws Exception{
        AnchorPane pane = (AnchorPane)getView();
        Platform.runLater(()->{
            stage = new Stage();
            Scene sc = new Scene(pane);
            stage.setScene(sc);
            stage.initOwner(GUIState.getStage());
            stage.setResizable(false);
            stage.setTitle("分类管理");
        });
    }
}
