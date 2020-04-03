package com.sunrised.live;

import com.sunrised.live.biz.util.ToolUtil;
import com.sunrised.live.ui.Splash;
import com.sunrised.live.ui.view.StartView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiveApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) throws Exception {
        //BeautyEyeLNFHelper.launchBeautyEyeLNF();
        ToolUtil.enhanceTools();
        launch(LiveApplication.class, StartView.class, new Splash(), args);
    }

}
