package com.sunrised.live;

import com.sunrised.live.biz.util.ToolUtil;
import com.sunrised.live.model.RegisterResult;
import com.sunrised.live.ui.Splash;
import com.sunrised.live.ui.view.StartView;
import com.sunrised.live.ui.view.dialog.RegisterDialog;
import com.sunrised.live.util.RegisterUtil;
import com.sunrised.live.util.UIUtil;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiveApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) throws Exception {
        //BeautyEyeLNFHelper.launchBeautyEyeLNF();
        ToolUtil.enhanceTools();
        //launch(LiveApplication.class, StartView.class, new Splash(), args);

        String key = RegisterUtil.readKeyFromFile();
        String serialNumber = RegisterUtil.getMixSerialNumber();
        RegisterResult result = RegisterUtil.doRegister(key, serialNumber);
        if(result.getStatus() == RegisterResult.STATUS_OK) {
            launch(LiveApplication.class, StartView.class, new Splash(), args);
        } else {
            launch(LiveApplication.class, RegisterDialog.class, new Splash(), args);
        }
    }

}
