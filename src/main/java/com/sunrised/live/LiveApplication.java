package com.sunrised.live;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.sunrised.live.ui.Splash;
import com.sunrised.live.ui.view.StartView;

@SpringBootApplication
public class LiveApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) throws Exception{
        //BeautyEyeLNFHelper.launchBeautyEyeLNF();
        launch(LiveApplication.class,StartView.class,new Splash(),args);
    }

}
