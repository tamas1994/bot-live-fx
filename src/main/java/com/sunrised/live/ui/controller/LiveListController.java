package com.sunrised.live.ui.controller;

import com.google.gson.Gson;
import com.sunrised.live.model.Live;
import com.sunrised.live.service.MsgService;
import com.sunrised.live.ui.view.StartView;
import com.sunrised.live.ui.view.dialog.LiveEditDialog;
import com.sunrised.live.util.UIUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
@FXMLController
public class LiveListController implements Initializable {

    @Resource
    private MsgService msgService;

    @Autowired
    private StartView startView;

    @Autowired
    private LiveEditDialog liveEditDialog;

    @FXML
    private TableView<Live> liveTable;
    @FXML
    private TableColumn<Live, String> livePageUrlColumn;
    @FXML
    private TableColumn<Live, String> pathColumn;
    @FXML
    private TableColumn<Live, String> saveNameColumn;
    @FXML
    private TextArea textArea;
    @FXML
    private Label titleLabel;

    /**
     * 初始化列表
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        liveTable.getItems().addAll(msgService.getLiveList());

        livePageUrlColumn.setCellValueFactory(
                cellData -> cellData.getValue().livePageUrlProperty());
        pathColumn.setCellValueFactory(
                cellData -> cellData.getValue().pathProperty());
        saveNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().saveNameProperty());
        liveTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setTile(newValue != null? newValue.getLivePageUrl() : "未选中"));
    }

    private void setTile(String title) {
        titleLabel.setText(title);
    }

    /**
     * 删除直播链接
     */
    @FXML
    private void handleDeleteLive() {

        Live selectedLive = liveTable.getSelectionModel().getSelectedItem();
        if (selectedLive != null) {
            Optional<ButtonType> result = UIUtil.showAlertDialog("你确实要删除【"+selectedLive.getLivePageUrl()+"】吗？","删除", Alert.AlertType.CONFIRMATION);
            result.ifPresent(btnType->{
                if(btnType.equals(ButtonType.OK)){
                    liveTable.getItems().remove(selectedLive);
                }
            });
        } else {
            Optional<ButtonType> result = UIUtil.showAlertDialog("请选择要删除的直播链接","提示", Alert.AlertType.WARNING);
        }
    }

    /**
     * 添加新直播下载
     */
    @FXML
    private void handleNewLive() {
        liveEditDialog.setLive(new Live());
        Stage stg = liveEditDialog.getStage();
        if(liveEditDialog.getStage() != null && stg.getOwner() != startView.getStage()){
            stg.initOwner(startView.getStage());
        }
        if(stg.isShowing()){
            stg.requestFocus();
        }else{
            stg.showAndWait();
            Live live = liveEditDialog.getLive();
            if(isInputValid(live)) {
                liveTable.getItems().add(new Live(live.getLivePageUrl(), live.getPath(), live.getSaveName()));
            }
        }
    }

    /**
     * 停止直播下载
     */
    @FXML
    private void handleStopLive() {
        Live selectedLive = liveTable.getSelectionModel().getSelectedItem();
        if(selectedLive != null) {
            Optional<ButtonType> result = UIUtil.showAlertDialog("确定要停止【"+selectedLive.getLivePageUrl()+"】吗？","删除", Alert.AlertType.CONFIRMATION);
            result.ifPresent(btnType->{
                if(btnType.equals(ButtonType.OK)){
                    System.out.println(msgService.stopLiveEvent());
                }
            });
        } else {
            Optional<ButtonType> result = UIUtil.showAlertDialog("请选择要结束的直播链接","提示", Alert.AlertType.WARNING);
        }
    }

    /**
     * 表单校验
     * @param live
     * @return
     */
    private boolean isInputValid(Live live) {
        String errorMessage = "";

        if (StringUtils.isEmpty(live.getLivePageUrl())) {
            errorMessage += "直播地址不能为空!\n";
        }
        if(StringUtils.isEmpty(live.getPath())) {
            errorMessage += "存放路径不能为空!\n";
        }
        if (StringUtils.isEmpty(live.getSaveName())) {
            errorMessage += "文件名称不能为空!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // 消息提示
            Optional<ButtonType> result = UIUtil.showAlertDialog(errorMessage,"提示", Alert.AlertType.WARNING);
            return false;
        }
    }

    /**
     * 开始直播下载
     */
    @FXML
    private void handleStartLive() {
        Live selectedItem = liveTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            log(selectedItem.getLivePageUrl()+"...."+msgService.sendMsg());
        } else {
            Optional<ButtonType> result = UIUtil.showAlertDialog("请选择要下载的直播地址","提示", Alert.AlertType.WARNING);
        }
    }

    /**
     * 日志打印
     * @param str
     */
    private void log(String str) {
        String text = textArea.getText();
        textArea.setText(contentLimit(text+str+"\n"));
    }

    /**
     * 内容输出限制
     * @param content
     * @return
     */
    private String contentLimit(String content) {
        if(StringUtils.isEmpty(content)) {
            return "";
        }

        String[] arr = content.split("\n");
        int size = arr.length;
        if(size <= 25) {
            return content;
        } else {
            StringBuffer sb = new StringBuffer();
            for(int i = size-25; i < size; i++) {
                sb.append(arr[i]).append("\n");
            }
            return sb.toString();
        }
//        int length = content.length();
//        if(length > 2500) {
//            return content.substring(length-2500, length);
//        } else {
//            return content;
//        }
    }
}
