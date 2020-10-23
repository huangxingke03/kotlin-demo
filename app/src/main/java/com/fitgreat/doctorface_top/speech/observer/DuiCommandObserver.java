package com.fitgreat.doctorface_top.speech.observer;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dsk.duiwidget.CommandObserver;

/**
 * 客户端CommandObserver, 用于处理客户端动作的执行以及快捷唤醒中的命令响应.
 * 返回数据
 */
public class DuiCommandObserver implements CommandObserver {
    private String[] mSubscribeCommands = new String[]{
            "sys.dialog.state",
            "context.output.text",
            "context.input.text",
            "context.widget.content",
            "context.widget.list",
            "context.widget.web",
            "context.widget.media",
            "context.widget.custom",
            "robot.action.navigation.operation.task", //先导航,后执行任务
            "robot.action.operation.task", //执行任务
            "robot.action.navigation", //导航
            "robot.action.termination", //终止   terminationTag
            "robot.action.charge",//冲电
    };
    private String toTask;
    private String doTask;
//    private LocationEntity locationOne;
//    private OperationInfo operationOne;

    public DuiCommandObserver() {
//        gson = new Gson();
    }

    // 注册当前更新消息
    public void regist() {
        DDS.getInstance().getAgent().subscribe(mSubscribeCommands,
                this);
    }

    // 注销当前更新消息
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    /**
     * @param command robot.action.task
     * @param data    {"do":"入院宣教","to":"二号床","taskName":"执行任务","skillId":"2020071500000056","skillName":"宣教","intentName":"识别宣教任务"}
     */
    @Override
    public void onCall(String command, String data) {
//        boolean emergencyTag = SpUtils.getBoolean(MyApp.getContext(), CLICK_EMERGENCY_TAG, false);
//        LogUtils.d("CommandTodo", "command: " + command + " data: " + data+"  急停按钮是否被按下  "+emergencyTag);
//        if (emergencyTag){ //急停按钮被按下
//            EventBus.getDefault().post(new ActionEvent(PLAY_TASK_PROMPT_INFO, "抱歉我的急停按钮已启动，无法执行该任务"));
//        }else { //急停按钮没有被按下
//            emergencyNotClickOn(data);
//        }
    }

    /**
     * 急停按钮没有按下时,根据不同的指令下发不同的任务
     * @param data
     */
    private void emergencyNotClickOn(String data) {
//        try {
//            JSONObject jsonObject = new JSONObject(data);
//            CommandDataEvent commandDataEvent = new CommandDataEvent();
//            if (jsonObject.has("terminationTag")) { //指令停止
//                commandDataEvent.setCommandType(1);
//                LogUtils.d("CommandTodo", "取消指令");
//            } else if (jsonObject.has("do") && jsonObject.has("to")) { //根据指令先执行导航任务,后执行操作任务
//                doTask = jsonObject.getString("do");
//                toTask = jsonObject.getString("to");
//                operationOne = LocalCashUtils.getOperationOne(doTask);
//                locationOne = LocalCashUtils.getLocationOne(toTask);
//                LogUtils.d("CommandTodo", "指令先导航,后执行任务");
//                LogUtils.json("CommandTodo", JSON.toJSONString(operationOne));
//                LogUtils.json("CommandTodo", JSON.toJSONString(locationOne));
//                if (operationOne != null && locationOne != null) { //导航点  操作任务本地都有
//                    commandDataEvent.setToData(toTask);
//                    commandDataEvent.setDoData(doTask);
//                }
//                if (operationOne == null && locationOne != null) { //导航点有  操作任务没有
//                    EventBus.getDefault().post(new ActionEvent(PLAY_TASK_PROMPT_INFO, AirFaceApp.getContext().getResources().getString(R.string.prompt_no_task)));
//                    return;
//                }
//                if (operationOne != null && locationOne == null) { //导航点没有  操作任务有
//                    EventBus.getDefault().post(new ActionEvent(PLAY_TASK_PROMPT_INFO, AirFaceApp.getContext().getResources().getString(R.string.prompt_no_navigation)));
//                    return;
//                }
//            } else if (jsonObject.has("operationName")) { //指令执行任务
//                doTask = jsonObject.getString("operationName");
//                operationOne = LocalCashUtils.getOperationOne(doTask);
//                LogUtils.d("CommandTodo", "没有导航只执行任务");
//                LogUtils.json("CommandTodo", JSON.toJSONString(operationOne));
//                if (operationOne != null) { //本地有任务信息
//                    commandDataEvent.setDoData(doTask);
//                } else { //当前医院没有任务信息
//                    EventBus.getDefault().post(new ActionEvent(PLAY_TASK_PROMPT_INFO, AirFaceApp.getContext().getResources().getString(R.string.prompt_no_task)));
//                    return;
//                }
//            } else if (jsonObject.has("locationName")) { //指令执行导航任务
//                toTask = jsonObject.getString("locationName");
//                locationOne = LocalCashUtils.getLocationOne(toTask);
//                LogUtils.d("CommandTodo", "只导航指令");
//                LogUtils.json("CommandTodo", JSON.toJSONString(locationOne));
//                if (locationOne != null) {
//                    commandDataEvent.setToData(toTask);
//                } else { //当前医院没有该导航点信息
//                    EventBus.getDefault().post(new ActionEvent(PLAY_TASK_PROMPT_INFO, AirFaceApp.getContext().getResources().getString(R.string.prompt_no_navigation)));
//                    return;
//                }
//            } else if (jsonObject.has("chargeTag")) { //冲电任务
//                LogUtils.d("CommandTodo", "冲电指令");
//                commandDataEvent.setCommandType(2);
//            }
//            EventBus.getDefault().post(commandDataEvent);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}
