package com.sunrised.live.ui.controller;

import com.sunrised.live.BusListener;
import com.sunrised.live.biz.TaskListener;
import com.sunrised.live.biz.bean.Result;
import com.sunrised.live.biz.service.LiveProcessService;
import com.sunrised.live.model.Live;
import com.sunrised.live.service.MsgService;
import com.sunrised.live.ui.view.StartView;
import com.sunrised.live.ui.view.dialog.LiveEditDialog;
import com.sunrised.live.util.UIUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@FXMLController
public class LiveListController implements Initializable {

    private static final int MAX_CONTENT_LENGTH = 20;

    @Resource
    private MsgService msgService;

    @Autowired
    private StartView startView;

    @Autowired
    private LiveEditDialog liveEditDialog;

    @Autowired
    LiveProcessService liveProcessService;

    @FXML
    private TableView<Live> liveTable;
    @FXML
    private TableColumn<Live, String> livePageUrlColumn;
    @FXML
    private TableColumn<Live, String> pathColumn;
    @FXML
    private TableColumn<Live, String> saveNameColumn;
    @FXML
    private TableColumn<Live, String> statusColumn;
    @FXML
    private TextArea textArea;
    @FXML
    private Label titleLabel;

    private BusListener busListener;
    @FXML
    private BusListener getBusListener() {
        if (busListener == null) {
            busListener = new BusListener() {
                @Override
                public void onMessage(String key, String message) {
                    Live selectedItem = liveTable.getSelectionModel().getSelectedItem();
                    if(selectedItem != null && selectedItem.getLivePageUrl().equals(key)) {
                        log(message);
                    }
                }
            };
        }
        return busListener;
    }

    /**
     * 初始化列表
     *
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
        statusColumn.setCellValueFactory(
                cellData -> cellData.getValue().statusProperty());
        liveTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setTile(newValue != null ? newValue.getLivePageUrl() : "未选中"));
    }

    private void setTile(String title) {
        titleLabel.setText(title);
        Platform.runLater(() -> textArea.setText(title+"\n"));
    }

    /**
     * 删除直播链接
     */
    @FXML
    private void handleDeleteLive() {

        Live selectedLive = liveTable.getSelectionModel().getSelectedItem();
        if (selectedLive != null) {
            if(liveProcessService.isTaskProcessing(selectedLive.getLivePageUrl())) {
                Optional<ButtonType> result = UIUtil.showAlertDialog("当前任务正在进行中，请先停止任务后再进行删除操作", "删除", Alert.AlertType.WARNING);
                return;
            }
            Optional<ButtonType> result = UIUtil.showAlertDialog("你确实要删除【" + selectedLive.getLivePageUrl() + "】吗？", "删除", Alert.AlertType.CONFIRMATION);
            result.ifPresent(btnType -> {
                if (btnType.equals(ButtonType.OK)) {
                    liveTable.getItems().remove(selectedLive);
                }
            });
        } else {
            Optional<ButtonType> result = UIUtil.showAlertDialog("请选择要删除的直播链接", "提示", Alert.AlertType.WARNING);
        }
    }

    /**
     * 添加新直播下载
     */
    @FXML
    private void handleNewLive() {
        liveEditDialog.setLive(new Live());
        Stage stg = liveEditDialog.getStage();
        if (liveEditDialog.getStage() != null && stg.getOwner() != startView.getStage()) {
            stg.initOwner(startView.getStage());
        }
        if (stg.isShowing()) {
            stg.requestFocus();
        } else {
            stg.showAndWait();
            Live live = liveEditDialog.getLive();
            if (isInputValid(live)) {
                liveTable.getItems().add(new Live(live.getLivePageUrl(), live.getPath(), live.getSaveName(), live.getStatus()));
            }
        }
    }

    /**
     * 停止直播下载
     */
    @FXML
    private void handleStopLive() {
        Live selectedLive = liveTable.getSelectionModel().getSelectedItem();
        if (selectedLive != null) {
            Optional<ButtonType> result = UIUtil.showAlertDialog("确定要结束【" + selectedLive.getLivePageUrl() + "】吗？", "删除", Alert.AlertType.CONFIRMATION);
            result.ifPresent(btnType -> {
                if (btnType.equals(ButtonType.OK)) {
                    //System.out.println(msgService.stopLiveEvent());
                    liveProcessService.sendStopTaskRequest(selectedLive.getLivePageUrl());
                    selectedLive.setStatus("结束");
                }
            });
        } else {
            Optional<ButtonType> result = UIUtil.showAlertDialog("请选择要结束的直播链接", "提示", Alert.AlertType.WARNING);
        }
    }

    /**
     * 表单校验
     *
     * @param live
     * @return
     */
    private boolean isInputValid(Live live) {
        String errorMessage = "";

        if (StringUtils.isEmpty(live.getLivePageUrl())) {
            errorMessage += "直播地址不能为空!\n";
        }
        if (StringUtils.isEmpty(live.getPath())) {
            errorMessage += "存放路径不能为空!\n";
        }
        if (StringUtils.isEmpty(live.getSaveName())) {
            errorMessage += "文件名称不能为空!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // 消息提示
            Optional<ButtonType> result = UIUtil.showAlertDialog(errorMessage, "提示", Alert.AlertType.WARNING);
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
            TaskListener taskListener = new TaskListener() {
                @Override
                public void onTaskStop() {
                    System.out.println("onTaskStop");
                    selectedItem.setStatus("结束");
                }

                @Override
                public void onSendMsg(String msg) {
                    // todo msg -> 输出到控制台
                    getBusListener().onMessage(selectedItem.getLivePageUrl(),msg);
                }

                @Override
                public void onAddError(String errMsg) {
                    selectedItem.setStatus("异常");
                    Optional<ButtonType> result = UIUtil.showAlertDialog("启动任务异常，即将删除当前任务", "提示", Alert.AlertType.WARNING);
                    log.error("启动任务异常：{}", errMsg);
                    handleDeleteLive();
                }
            };


            Result result = liveProcessService.startTask(selectedItem.getLivePageUrl(), selectedItem.getPath(), selectedItem.getSaveName(), taskListener);
            if(result.getStatus() == 1) {
                selectedItem.setStatus("开始");
            }
        } else {
            Optional<ButtonType> result = UIUtil.showAlertDialog("请选择要下载的直播地址", "提示", Alert.AlertType.WARNING);
        }
    }

    /**
     * 日志打印
     *
     * @param str
     */
    private void log(String str) {
        log.error("current_log:"+str);
        Platform.runLater(() -> textArea.setText(limit(textArea.getText(), str)));
    }

    /**
     * 内容输出限制
     *
     * @param content
     * @return
     */
    private String contentLimit(String content) {
        if (StringUtils.isEmpty(content)) {
            return "";
        }

        int length = content.length();
        if(length > 1500) {
            return content.substring(length-1500, length);
        } else {
            return content;
        }
//        String[] arr = content.split("\n");
//        int size = arr.length;
//        if (size <= 25) {
//            return content;
//        } else {
//            StringBuffer sb = new StringBuffer();
//            for (int i = size - 25; i < size; i++) {
//                sb.append(arr[i]).append("\n");
//            }
//            return sb.toString();
//        }
    }

    public String limit(String content, String row) {
        if(content == null) {
            content = "Null Content...\n";
        }

        if(row == null) {
            row = "Null Content...\n";
        }

        String newContent = content+row+"\n";

        String[] split = newContent.split("\n");
        if(ArrayUtils.isEmpty(split)) {
            return content;
        }

        int currentLength = split.length;
        if(currentLength <= MAX_CONTENT_LENGTH) {
            return newContent;
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(split[0]).append("\n");
            for(int i= currentLength - MAX_CONTENT_LENGTH; i < currentLength; i++) {
                sb.append(split[i]).append("\n");
            }
            return sb.toString();
        }
    }
}
