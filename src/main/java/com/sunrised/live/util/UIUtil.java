package com.sunrised.live.util;

import de.felixroske.jfxsupport.GUIState;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.io.*;
import java.util.Optional;
import java.util.Properties;

/**
 * UI工具类
 */
public class UIUtil {

    private static boolean classical;

    private static boolean useFloat;

    static {
        try{
            Properties props = new Properties();
            props.load(new FileInputStream("configs/config.properties"));
            classical = props.get("app.mode").equals("classical");
            useFloat = props.get("app.run-in-background").toString().toLowerCase().equals("true");
            String back = props.getProperty("app.background");
            File file = new File("./configs/res/" + back);
            if (!file.exists()){
                UIUtil.writeFile(file,new FileInputStream("./configs/theme/"+props.getProperty("app.theme")+"/"+back));
            }
        }catch (Exception e){
            classical = false;
        }
    }

    public static String readFileAsText(InputStream in) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        String buff = line;
        while ((line = reader.readLine())!=null){
            buff = buff + line;
        }
        return buff;
    }

    public static byte[] readFile(FileInputStream in) throws Exception{
        DataInputStream din = new DataInputStream(in);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        while ((din.read(buff)>0)){
            bout.write(buff);
        }
        bout.flush();
       return bout.toByteArray();
    }

    public static void writeFile(File file,InputStream in){
        try (FileOutputStream fos = new FileOutputStream(file)){
            DataOutputStream dos = new DataOutputStream(fos);
            DataInputStream din = new DataInputStream(in);
            byte[] buf = new byte[1024];
            int length;
            while ((length = din.read(buf)) > 0){
                dos.write(buf,0,length);
            }
            dos.flush();
            dos.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void processWriteFile(File file,String content){
        try (FileOutputStream fos = new FileOutputStream(file)){
            BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(fos));
            bwr.write(content);
            bwr.flush();
            bwr.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean isClassical(){
        return classical;
    }

    public static boolean isUseFloat(){
        return useFloat;
    }

    public static Node findById(String id, ObservableList<Node> list){
        for (Node node:list) {
            if(id.equals(node.getId())){
                return node;
            }
        }
        return null;
    }

    public static Optional<ButtonType> showAlertDialog(String content, String title, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.initOwner(GUIState.getStage());
        try {
            return alert.showAndWait();
        }catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Optional<ButtonType> showAlertWithOwner(String content, String title, Alert.AlertType type, Window owner) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.initOwner(owner);
        try {
            return alert.showAndWait();
        }catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static double getScreenX(Node node) {
        return  node.localToScreen(0,0).getX();
    }

    public static double getScreenY(Node node) {
        return node.localToScreen(0,0).getY();
    }

}
