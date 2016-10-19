package com.example.externie.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RedBaoService extends AccessibilityService {
    public RedBaoService() {
    }
    //该对象代表了整个窗口视图的快照
    private AccessibilityNodeInfo mRootNodeInfo = null;
    //已经拆过的红包收集容器
    private Set<AccessibilityNodeInfo> RB_Set = new HashSet<AccessibilityNodeInfo>();
    //微信红包收集器
    private Set<AccessibilityNodeInfo> MC_Set = new HashSet<AccessibilityNodeInfo>();
    //qq红包收集器
    private Set<AccessibilityNodeInfo> QQ_Set = new HashSet<AccessibilityNodeInfo>();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
//        System.out.println("所有程序");
        mRootNodeInfo = event.getSource();
        if(mRootNodeInfo == null)
            return;//没有获得窗口视图快照

        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
            //窗口内容发生了变化
            this.contentChange();
        }

        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            //窗口状态发生了变化
            this.windowStateChange();
        }
    }

    public void contentChange(){
        System.out.println("内容改变");
        //窗口内容发生变化
        this.searchRedBao("领取红包");
        this.searchRedBao("查看红包");
//        this.searchRedBao("口令红包");
//        this.searchRedBao("点击拆开");
//        List<AccessibilityNodeInfo> dianjiShuruRedBaoList = mRootNodeInfo.findAccessibilityNodeInfosByText("点击输入口令");
//        List<AccessibilityNodeInfo> sendKoulinList = mRootNodeInfo.findAccessibilityNodeInfosByText("发送");
//        if(dianjiShuruRedBaoList.size()>0){
//            System.out.println("点击输入口令：来自窗口内容");
//            AccessibilityNodeInfo dianjiShuruNodeInfo = dianjiShuruRedBaoList.get(0);
//            dianjiShuruNodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            System.out.println("点击发送");
//            AccessibilityNodeInfo sendNodeInfo = sendKoulinList.get(0);
//            sendNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        }
    }

    public void windowStateChange(){
        System.out.println("窗口状态发生变化");
        //窗口状态发生了变化
        if(this.isProfilePage()){
            this.parseProfileList();
        }
//        List<AccessibilityNodeInfo> redBaoProfileList = mRootNodeInfo.findAccessibilityNodeInfosByText("红包详情");
//        if (redBaoProfileList.size()>0){
//            //已经抢到了红包，现在需要退出这个界面
//            AccessibilityNodeInfo redBaoProfileNode = redBaoProfileList.get(redBaoProfileList.size()-1);
//            AccessibilityNodeInfo parentProfile = redBaoProfileNode.getParent();
//            for(int i = 0 ; i<parentProfile.getChildCount() ; i++){
//                parentProfile.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            }
//        }
//        this.quitAndWaitNextRedBao("红包详情");
//        this.quitAndWaitNextRedBao("已存入余额");
//
//        this.openMCRedBao();
//        this.checkRedBaoOutorNot("QQ");
//        this.checkRedBaoOutorNot("MicroChat");
//
//        //qq的情况
//
//        List<AccessibilityNodeInfo> koulinRedBaoProfileList = mRootNodeInfo.findAccessibilityNodeInfosByText("口令红包");
//        if (koulinRedBaoProfileList.size()>0){
//            AccessibilityNodeInfo koulinNodeInfo = koulinRedBaoProfileList.get(0);
//            AccessibilityNodeInfo koulinParentNodeInfo = koulinNodeInfo.getParent();
//            System.out.println("口令红包视图界面有几个孩子："+koulinParentNodeInfo.getChildCount());
//            koulinParentNodeInfo.getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        }
//
//        List<AccessibilityNodeInfo> sendKoulinList = mRootNodeInfo.findAccessibilityNodeInfosByText("发送");
//        List<AccessibilityNodeInfo> dianjiShuruRedBaoList = mRootNodeInfo.findAccessibilityNodeInfosByText("点击输入口令");
//        if(dianjiShuruRedBaoList.size()>0){
//            System.out.println("点击输入口令:来自窗口状态变化");
//            AccessibilityNodeInfo dianjiShuruNodeInfo = dianjiShuruRedBaoList.get(0);
//            dianjiShuruNodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            System.out.println("点击发送");
//            AccessibilityNodeInfo sendNodeInfo = sendKoulinList.get(0);
//            sendNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        }
    }

    private boolean isProfilePage(){
        List<AccessibilityNodeInfo> redBaoProfile = mRootNodeInfo.findAccessibilityNodeInfosByText("红包详情");
        List<AccessibilityNodeInfo> wxSafePay = mRootNodeInfo.findAccessibilityNodeInfosByText("微信安全支付");
        if (redBaoProfile.size()>0 && wxSafePay.size()>0){
            return true;
        }
        return false;
    }

    public RedBaoService(AccessibilityNodeInfo mRootNodeInfo) {
        this.mRootNodeInfo = mRootNodeInfo;
    }

    private void parseProfileList(){
        List<AccessibilityNodeInfo> nameList = mRootNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bdm");
        List<AccessibilityNodeInfo> moneyList = mRootNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bdq");

        String str = "";
        if (nameList.size() != moneyList.size()){
            str = "结果错误，名字和钱的个数对不上号";
        }else{
            str += "抢包情况\n";
            for(int i = 0; i < nameList.size() ; i++ ){
                String name = nameList.get(i).getText().toString();
                String money = moneyList.get(i).getText().toString();
                Log.d("EXTERNIE:", name);
                Log.d("EXTERNIE:", money);
                System.out.println(String.format("EXTERNIE:%s", name));
                System.out.println(String.format("EXTERNIE:%s", money));
                str+=String.format("名字：*%s* -- 点数：*%s*\n", name, money);
            }
        }
        ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", str);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    private void quitAndWaitNextRedBao(String str){//已经抢到红包退出那个界面
        List<AccessibilityNodeInfo> redBaoProfileList = mRootNodeInfo.findAccessibilityNodeInfosByText(str);
        if (redBaoProfileList.size()>0){
            //已经抢到了红包，现在需要退出这个界面
            AccessibilityNodeInfo redBaoProfileNode = redBaoProfileList.get(redBaoProfileList.size()-1);
            AccessibilityNodeInfo parentProfile = redBaoProfileNode.getParent();
            for(int i = 0 ; i<parentProfile.getChildCount() ; i++){
                parentProfile.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    private void searchRedBao(String redBaoType){
        List<AccessibilityNodeInfo> nodeList = mRootNodeInfo.findAccessibilityNodeInfosByText(redBaoType);
        if(nodeList.size()>0){
            //至少找到一个红包的NodeInfo
//            Iterator<AccessibilityNodeInfo> it = nodeList.iterator();
//            while (it.hasNext()){
//                AccessibilityNodeInfo tempNode = it.next();
//                if (RB_Set.add(tempNode)){//存在一个没有打开过的红包，那么我们现在贱贱的打开它吧，哈哈哈。
//                    System.out.println("找到一个没有打开过的红包");
//                    tempNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                }else{
//                    System.out.println("找到一个已经拆过的红包");
//                }
//            }
            //找到最新的那个拆开
            AccessibilityNodeInfo lastOne = nodeList.get(nodeList.size() - 1);
            System.out.println("找到最新的一个，并拆开");
        }
    }

    private void checkRedBaoOutorNot(String AppType){//如果红包被领完我们需要点击左上角的关闭按钮，无论是微信还是QQ都一样
        String MC_str = "手慢了，红包派完了";
        String QQ_str = "来晚一步，红包被领完了";
        String str;
        if (AppType.equals("QQ")){
            str = QQ_str;
        }else{
            str = MC_str;
        }
        List<AccessibilityNodeInfo> redBaoOutList = mRootNodeInfo.findAccessibilityNodeInfosByText(str);
        if (redBaoOutList.size()>0){
            //红包派完了
            AccessibilityNodeInfo redBaoOutNode = redBaoOutList.get(redBaoOutList.size() - 1);
            AccessibilityNodeInfo parentOut = redBaoOutNode.getParent();
            System.out.println("红包派完了"+parentOut.getChildCount());
            if (AppType.equals("MicroChat"))
                parentOut.getChild(2).performAction(AccessibilityNodeInfo.ACTION_CLICK);//微信关闭的node序号，不要问我为什么是2，发红包测试出来的
            else
                parentOut.getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);//qq关闭的node序号，不要问我为什么是0，也是发红包测试出来的
        }
    }

    private void openMCRedBao(){//微信比较奇葩，点击红包后还要拆红包，呵呵
        List<AccessibilityNodeInfo> openRedBaoList = mRootNodeInfo.findAccessibilityNodeInfosByText("给你发了一个红包");
        if (openRedBaoList.size()<=0)
            openRedBaoList = mRootNodeInfo.findAccessibilityNodeInfosByText("发了一个红包，金额随机");
//        openRedBaoList.addAll(mRootNodeInfo.findAccessibilityNodeInfosByText("给你发了一个红包"));
//        openRedBaoList.addAll(mRootNodeInfo.findAccessibilityNodeInfosByText("发了一个红包，金额随机"));
        if (openRedBaoList.size()>0){
            //找到了拆红包文本视图
            AccessibilityNodeInfo lastedRedBaoNodeInfo = openRedBaoList.get(openRedBaoList.size()-1);
            AccessibilityNodeInfo parentNode = lastedRedBaoNodeInfo.getParent();
            for (int i = 0 ; i < parentNode.getChildCount() ; i++){
                AccessibilityNodeInfo temp = parentNode.getChild(i);
                temp.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    @Override
    public void onInterrupt() {

    }


}
