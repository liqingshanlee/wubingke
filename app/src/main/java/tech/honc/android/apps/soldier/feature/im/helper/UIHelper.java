package tech.honc.android.apps.soldier.feature.im.helper;

import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import tech.honc.android.apps.soldier.feature.im.YWSDKGlobalConfig;
import tech.honc.android.apps.soldier.feature.im.advice.operation.ChattingOperation;
import tech.honc.android.apps.soldier.feature.im.advice.operation.ConversationListOperation;
import tech.honc.android.apps.soldier.feature.im.advice.pageui.ChattingUI;
import tech.honc.android.apps.soldier.feature.im.advice.pageui.ConversationListUI;

/**
 * Created by wangh on 2016-5-20-0020.
 */
public class UIHelper {

  public static void initCustomUI() {

    AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_POINTCUT, ChattingOperation.class);
    AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT, ChattingUI.class);
    AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_OPERATION_POINTCUT,
        ConversationListOperation.class);
    AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT,
        ConversationListUI.class);

    //全局配置修改
    AdviceBinder.bindAdvice(PointCutEnum.YWSDK_GLOBAL_CONFIG_POINTCUT, YWSDKGlobalConfig.class);
    //绑定@功能
    //AdviceBinder.bindAdvice(PointCutEnum.TRIBE_ACTIVITY_AT_MSG_LIST, AtMsgListUI.class);
  }
}
