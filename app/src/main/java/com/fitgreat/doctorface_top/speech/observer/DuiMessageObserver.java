package com.fitgreat.doctorface_top.speech.observer;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.MessageObserver;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.alibaba.fastjson.JSON;
import com.fitgreat.doctorface_top.speech.model.MessageBean;
import com.fitgreat.doctorface_top.speech.model.WeatherBean;
import com.fitgreat.doctorface_top.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 客户端MessageObserver, 用于处理客户端动作的消息响应.
 */
public class DuiMessageObserver implements MessageObserver {

    private boolean mIsFirstVar = true;
    private boolean mHasvar = false;
    private String[] mSubscribeKeys = new String[]{
            "sys.dialog.state",
            "context.output.text",
            "context.input.text",
            "context.widget.content",
            "context.widget.list",
            "context.widget.web",
            "context.widget.media",
            "context.widget.custom",
            "sys.dialog.start",
    };
    private static final String TAG = "DuiMessageObserver";

    public DuiMessageObserver() {
    }

    public void regist() {
        DDS.getInstance().getAgent().subscribe(mSubscribeKeys, this);
    }

    // 注销当前更新消息
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    /**
     * @param message context.output.text
     * @param data    {"skillName":"宣教","skillId":"2020071500000056","intentName":"识别宣教任务","taskName":"执行任务","text":"好的，我马上到二号床去做入院宣教"}
     */
    @Override
    public void onMessage(String message, String data) {
        try {
            LogUtils.d("CommandTodo", "  |message| : " + message + "  oneshot模式  " + (DDS.getInstance().getAgent().getWakeupEngine().getOneshotState()));
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
        LogUtils.json("CommandTodo", data);
        MessageBean bean = null;
        switch (message) {
            case "context.output.text":
                bean = new MessageBean();
                String txt = "";
                try {
                    JSONObject jo = new JSONObject(data);
                    txt = jo.optString("text", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bean.setText(txt);
                bean.setType(MessageBean.TYPE_OUTPUT);
                LogUtils.d("CommandTodo", "context.output.text = " + bean.getText());
//                EventBus.getDefault().post(bean);
                break;
            case "context.input.text":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    if (jo.has("var")) {
                        String var = jo.optString("var", "");
                        if (mIsFirstVar) {
                            mIsFirstVar = false;
                            mHasvar = true;
                            bean.setText(var);
                            bean.setType(MessageBean.TYPE_INPUT);
                        } else {
                            bean.setText(var);
                            bean.setType(MessageBean.TYPE_INPUT);
                        }
                    }
                    if (jo.has("text")) {
                        if (mHasvar) {
                            mHasvar = false;
                            mIsFirstVar = true;
                        }
                        String text = jo.optString("text", "");
                        bean.setText(text);
                        bean.setType(MessageBean.TYPE_INPUT);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LogUtils.d("CommandTodo", "context.input.text = " + bean.getText());
                if (bean.getText().length() >= 3) {
//                    EventBus.getDefault().post(bean);
                }
                break;
            case "context.widget.content":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    String title = jo.optString("title", "");
                    String subTitle = jo.optString("subTitle", "");
                    String imgUrl = jo.optString("imageUrl", "");
                    bean.setTitle(title);
                    bean.setSubTitle(subTitle);
                    bean.setImgUrl(imgUrl);
                    bean.setType(MessageBean.TYPE_WIDGET_CONTENT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "context.widget.list":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    JSONArray array = jo.optJSONArray("content");
                    if (array == null || array.length() == 0) {
                        return;
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        String title = object.optString("title", "");
                        String subTitle = object.optString("subTitle", "");
                        MessageBean b = new MessageBean();
                        b.setTitle(title);
                        b.setSubTitle(subTitle);
                        bean.addMessageBean(b);
                    }
                    int currentPage = jo.optInt("currentPage");
                    bean.setCurrentPage(currentPage);
                    bean.setType(MessageBean.TYPE_WIDGET_LIST);
                    int itemsPerPage = jo.optInt("itemsPerPage");
                    bean.setItemsPerPage(itemsPerPage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "context.widget.web":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    String url = jo.optString("url");
                    bean.setUrl(url);
                    bean.setType(MessageBean.TYPE_WIDGET_WEB);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "context.widget.custom":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    String name = jo.optString("name");
                    if (name.equals("weather")) {
                        bean.setWeatherBean(JSON.parseObject(data, WeatherBean.class));
                        bean.setType(MessageBean.TYPE_WIDGET_WEATHER);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "context.widget.media":
                JSONObject jsonObject;
                int count = 0;
                String name = "";
                try {
                    jsonObject = new JSONObject(data);
                    count = jsonObject.optInt("count");
                    name = jsonObject.optString("widgetName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (count > 0) { //跳转播放页面
//                    Intent intent = new Intent(AirFaceApp.getContext(), PlayerActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("data", data);
//                    AirFaceApp.getContext().startActivity(intent);
                }
                break;
            case "sys.dialog.state":
//                EventBus.getDefault().post(new DialogStateEvent(data));
                break;
            case "sys.dialog.start":
//                EventBus.getDefault().post(new RobotSignalEvent(RobotConfig.WAKE_WORD_DIALOG, ""));
                break;
            default:
        }
    }

}
