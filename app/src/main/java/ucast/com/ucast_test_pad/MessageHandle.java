package ucast.com.ucast_test_pad;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.project.messagerprotocol.MessageCallback.IMessageCallback;
import com.project.messagerprotocol.MessagePackaging;
import com.project.messagerprotocol.Model.BaudrateBlack;
import com.project.messagerprotocol.Model.DisConnect;
import com.project.messagerprotocol.Model.ICCardData;
import com.project.messagerprotocol.Model.ICCardImputOut;
import com.project.messagerprotocol.Model.ImagePathBlack;
import com.project.messagerprotocol.Model.MagneticCard;
import com.project.messagerprotocol.Model.MessageBase;
import com.project.messagerprotocol.Model.NFCCardImputOut;
import com.project.messagerprotocol.Model.NfcCardDData;
import com.project.messagerprotocol.Model.NfcCardPass;
import com.project.messagerprotocol.Model.PortDataBlack;
import com.project.messagerprotocol.Model.PrintStateBlack;
import com.project.messagerprotocol.Model.ReceiveKeyBoard;
import com.project.messagerprotocol.Model.ReceiveSerialData;
import com.project.messagerprotocol.Model.TwoCodeMsg;
import com.project.messagerprotocol.ReceivePackage;
import com.project.messagerprotocol.Util;

/**
 * Created by Administrator on 2016/6/8.
 */
public class MessageHandle implements IMessageCallback {

    private Handler handler;

    public MessageHandle(Handler handler){
        this.handler=handler;
    }


    /**
     * 回调函数接收数据的地方
     */
    @Override
    public void Receive(MessagePackaging messagePackaging, String s) {
        MessageBase msg = ReceivePackage.Receive(s);
        if (msg == null)
            return;
        //设置波特率
        if (msg instanceof BaudrateBlack) {
            sendBaudrateBlack((BaudrateBlack) msg);
            return;
        }
        //发送串口数据是否成功
        if (msg instanceof PortDataBlack) {
            sendPortDataBlack((PortDataBlack) msg);
            return;
        }
        //获取底座外接的串口数据
        if (msg instanceof ReceiveSerialData) {
            receivePortData((ReceiveSerialData) msg);
            return;
        }
        //查询打印机当前的状态
        if (msg instanceof PrintStateBlack) {
            sendPrintStateBlack((PrintStateBlack) msg);
            return;
        }
        //发送图片是否成功
        if (msg instanceof ImagePathBlack) {
            sendImagePathBlack((ImagePathBlack) msg);
            return;
        }
        //获取底座发送的键盘数据
        if (msg instanceof ReceiveKeyBoard) {
            receiveKeyBoard((ReceiveKeyBoard) msg);
            return;
        }
        //服务断线消息
        if (msg instanceof DisConnect) {
            receiveDisConnect((DisConnect) msg);
            return;
        }
        //磁卡的 1轨道，2轨道，3轨道
        if (msg instanceof MagneticCard) {
            MagneticCards((MagneticCard) msg);
            return;
        }
        //获取二维码的读取结果
        if (msg instanceof TwoCodeMsg) {
            QRMsg((TwoCodeMsg) msg);
            return;
        }
        //插入和拔出IC
        if (msg instanceof ICCardImputOut) {
            ICCardResult((ICCardImputOut) msg);
            return;
        }
        //获取IC卡数据
        if (msg instanceof ICCardData) {
            ICCardDatas((ICCardData) msg);
            return;
        }
        //获取NFC 有卡无卡
        if (msg instanceof NFCCardImputOut) {
            NcfCardResult((NFCCardImputOut) msg);
            return;
        }
        //获取NFC卡数据
        if (msg instanceof NfcCardDData) {
            NcfCardDDatas((NfcCardDData) msg);
            return;
        }
        //获取NFC卡数据的透传
        if (msg instanceof NfcCardPass) {
            NcfCardDPass((NfcCardPass) msg);
            return;
        }
    }

    public void NcfCardDPass(NfcCardPass nfcCardPass) {
        String portData = Util.getDataOX(nfcCardPass.Data);
        sengHandle(portData, MainActivity.RECEIVEPORTDATA);
    }

    public void MagneticCards(MagneticCard magneticCard) {
        sengHandle(magneticCard.Data, 101);
    }

    public void ICCardDatas(ICCardData icCardData) {
        String portData = Util.getDataOX(icCardData.Data);
        sengHandle(portData, 102);
    }

    public void NcfCardDDatas(NfcCardDData ncfCardDData) {
        String portData = Util.getDataX(ncfCardDData.Data);
        sengHandle(portData,103);
    }

    public void NcfCardResult(NFCCardImputOut nfcCardImputOut) {
        String str = nfcCardImputOut.Result ? "NFC有卡" : "NFC无卡";
        sengHandle(str, 103);
    }

    public void ICCardResult(ICCardImputOut icCardImputOut) {
        String str = icCardImputOut.Result ? "IC插入" : "IC拔出";
        sengHandle(str, 102);
    }

    public void QRMsg(TwoCodeMsg twoCodeMsg) {
        sengHandle(twoCodeMsg.Data, 100);
    }

    public void sendBaudrateBlack(BaudrateBlack baudrateBlack) {
        if (baudrateBlack.ServiceStatus) {
            String string = baudrateBlack.Result ? "成功" : "失败";
            sengHandle(string, MainActivity.SETBAUDRATE);
        } else {
            sengHandle("查询底座失败，可能服务未连接", MainActivity.TOU);
        }
    }

    //发送串口数据返回  @1201, 1,1, 0$
    public void sendPortDataBlack(PortDataBlack portDataBlack) {
        String string = portDataBlack.Result ? "成功" : "失败";
        sengHandle(string, MainActivity.SENDPORTDATA);
    }

    public void receivePortData(ReceiveSerialData receiveSerialData) {
        try {
            String portData = new String(receiveSerialData.Data);
            sengHandle(portData, MainActivity.RECEIVEPORTDATA);
        } catch (Exception e) {

        }
    }

    //打印机状态返回
    public void sendPrintStateBlack(PrintStateBlack printStateBlack) {
        try {
            if (printStateBlack.ServiceStatus) {
                int temp = printStateBlack.Temperature;
                String papers = printStateBlack.YesOrNo ? "无纸" : "有纸";
                sengTwoHandle(papers, temp + "", MainActivity.PRINTSTATE);
            } else {
                sengHandle("查询底座失败，可能服务未连接", MainActivity.TOU);
            }
        } catch (Exception e) {

        }
    }


    public void sendImagePathBlack(ImagePathBlack imagePathBlack) {
        String string = imagePathBlack.Result ? "成功" : "失败";
        sengHandle(string, MainActivity.SENDIMAGEPATH);
    }

    public void receiveKeyBoard(ReceiveKeyBoard receiveKeyBoard) {
        String keyBoard = new String(receiveKeyBoard.Data);
        sengHandle(keyBoard, MainActivity.RECEIVEKKEYBOARD);
    }

    public void receiveDisConnect(DisConnect disConnect) {
        //掉线
        String string = disConnect.Result == 1 ? "服务与底座掉线了" : "服务与底座可能未连接";
        sengHandle(string, MainActivity.LINKFAIL);
    }

    public void sengHandle(String value, int pag) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("data", value);
        msg.setData(bundle);
        msg.what = pag;
        handler.sendMessage(msg);
    }

    public void sengTwoHandle(String paper, String temp, int pag) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("data1", paper);
        bundle.putString("data2", temp);
        msg.setData(bundle);
        msg.what = pag;
        //参数 Message ，需要传给那个Activity的名称 备注:前提需要创建过这个Activity
        handler.sendMessage(msg);
    }
}
