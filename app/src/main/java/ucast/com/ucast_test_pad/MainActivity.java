package ucast.com.ucast_test_pad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.messagerprotocol.ChannelUtil;
import com.project.messagerprotocol.MessagePackaging;
import com.project.messagerprotocol.SendPackage;

import java.io.FileOutputStream;
import java.io.InputStream;

import fragments.BaseFragment;
import fragments.BluetoothFragment;
import fragments.DayinjiFragment;
import fragments.PortFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    public static final int LINKSUCCESSFUL = 1;      //连接成功
    public static final int LINKFAIL = 2;            //连接失败
    public static final int SETBAUDRATE = 3;         //设置波特率返回
    public static final int SENDPORTDATA = 4;         //发送串口数据
    public static final int RECEIVEPORTDATA = 5;     //接收串口数据
    public static final int SENDIMAGEPATH = 6;       //发送图片路径
    public static final int RECEIVEKKEYBOARD = 7;    //接收键盘数据
    public static final int DISCONNECT = 8;          //断开连接
    public static final int PRINTSTATE = 9;            //打印机状态返回
    public static final int TOU = 10;            //打印机状态返回


    private Button bluetootnBt;
    private Button erweimaBt;
    private Button citiaokaBt;
    private Button ickaBt;
    private Button nfckaBt;
    private Button dayinjiBt;
    private Button chuankouBt;
    private Button moneyBoxBt;
    private Button usbBt;
    private Button exit;

    private BluetoothFragment bluetoothFragment;
    private BaseFragment erweimaFragment;
    private BaseFragment citiaokaFragment;
    private BaseFragment ickaFragment;
    private BaseFragment nfckaFragment;
    private BaseFragment moneyFragment;
    private BaseFragment usbFragment;
    private DayinjiFragment dayinjiFragment;
    private PortFragment portFragment;

    private MessagePackaging messagePackaging;
    private MessageHandle messageHandle;


    private FragmentManager fm;

    //监听USB状态的广播
    BroadcastReceiver usbBroadReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();

            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                //usb连接
                String path=intent.getDataString();
                path=path.substring(11);
                String externalPath="/storage/external_storage/"
                        +path.substring(path.lastIndexOf("/")+1).trim();

//                showToast("usb已挂载");

                if (usbFragment!=null){
                    usbFragment.appenMessageTv(externalPath);
                }

            } else if(action.equals(Intent.ACTION_MEDIA_UNMOUNTED)){
                //usb断开
//                showToast("外部存储已经移除");
            }

            if (action.equals(Intent.ACTION_MEDIA_SHARED)) {
                showToast("正常读写");

            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //开启服务的连接
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChannelUtil.SingleInstance().Open(messageHandle);
            }
        }).start();
    }
    public void init(){
        bluetootnBt= (Button) findViewById(R.id.bluetootnBt);
        erweimaBt= (Button) findViewById(R.id.erweimaBt);
        citiaokaBt= (Button) findViewById(R.id.citiaokaBt);
        ickaBt= (Button) findViewById(R.id.ickaBt);
        nfckaBt= (Button) findViewById(R.id.nfckaBt);
        dayinjiBt= (Button) findViewById(R.id.dayinjiBt);
        chuankouBt= (Button) findViewById(R.id.chuankouBt);
        moneyBoxBt= (Button) findViewById(R.id.moneyBoxBt);
        usbBt= (Button) findViewById(R.id.usbBt);
        exit= (Button) findViewById(R.id.exit);



        bluetootnBt.setOnClickListener(this);
        erweimaBt.setOnClickListener(this);
        citiaokaBt.setOnClickListener(this);
        ickaBt.setOnClickListener(this);
        nfckaBt.setOnClickListener(this);
        dayinjiBt.setOnClickListener(this);
        chuankouBt.setOnClickListener(this);
        moneyBoxBt.setOnClickListener(this);
        usbBt.setOnClickListener(this);
        exit.setOnClickListener(this);


        bluetoothFragment=new BluetoothFragment();
        erweimaFragment=new BaseFragment();
        citiaokaFragment=new BaseFragment();
        ickaFragment=new BaseFragment();
        nfckaFragment=new BaseFragment();
        moneyFragment=new BaseFragment();
        usbFragment=new BaseFragment();
        dayinjiFragment=new DayinjiFragment();
        portFragment=new PortFragment();

        messageHandle=new MessageHandle(handler);
        fm=this.getSupportFragmentManager();

//        registUsbBord();


        copyCfg("aaaa.bmp");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        copyCfg("bbbb.bmp");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        copyCfg("cccc.bmp");

    }

    int oldId;
    @Override
    public void onClick(View v) {

        if (oldId == v.getId()){
            Log.e("Mainactivity", oldId + "   " + v.getId());
            return;
        }
        oldId=v.getId();

//        int count=fm.getBackStackEntryCount();
//        if (count>0){
//            fm.popBackStack();
//            return;
//        }
        switch (v.getId()){
            //蓝牙
            case R.id.bluetootnBt:
                setFragment(bluetoothFragment);
                break;
            //二维码
            case R.id.erweimaBt:
                Bundle data1 = new Bundle();
                data1.putInt("ID", R.id.erwemaIv);
                data1.putString("TITLE", "二维码测试界面");
                data1.putBoolean("FLAG", true);
                data1.putByteArray("SENDTYPE", SendPackage.ReadTwoDimensionalCode());
                erweimaFragment.setArguments(data1);
                setFragment(erweimaFragment);
                break;
            //磁条卡
            case R.id.citiaokaBt:
                Bundle data2 = new Bundle();
                data2.putInt("ID", R.id.citiaokaIv);
                data2.putString("TITLE", "磁条卡测试界面");
                data2.putBoolean("FLAG", false);
                citiaokaFragment.setArguments(data2);
                setFragment(citiaokaFragment);
                break;
            //Ic卡
            case R.id.ickaBt:
                Bundle data3 = new Bundle();
                data3.putInt("ID", R.id.ickaIv);
                data3.putString("TITLE", "IC卡测试界面");
                data3.putBoolean("FLAG", false);
                ickaFragment.setArguments(data3);
                setFragment(ickaFragment);
                break;
            //NFC
            case R.id.nfckaBt:
                Bundle data4 = new Bundle();
                data4.putInt("ID", R.id.nfckaIv);
                data4.putString("TITLE", "NFC测试界面");
                data4.putBoolean("FLAG", true);
                data4.putByteArray("SENDTYPE", SendPackage.ReadNfc());
                nfckaFragment.setArguments(data4);
                setFragment(nfckaFragment);
                break;
            //打印机
            case R.id.dayinjiBt:
                setFragment(dayinjiFragment);
                break;
            //底座串口
            case R.id.chuankouBt:
                setFragment(portFragment);
                break;
            //钱箱
            case R.id.moneyBoxBt :
                Bundle data5 = new Bundle();
                data5.putInt("ID", R.id.moneyBoxIv);
                data5.putString("TITLE", "钱箱测试界面");
                data5.putBoolean("FLAG", true);
                data5.putByteArray("SENDTYPE", SendPackage.OpenMoneyBox());
                moneyFragment.setArguments(data5);
                setFragment(moneyFragment);
                break;
            //USB
            case R.id.usbBt:
                Bundle data6 = new Bundle();
                data6.putInt("ID", R.id.usbIv);
                data6.putString("TITLE", "扫描枪测试界面,底座插入扫描抢扫描一维码或二维码即可");
                data6.putBoolean("FLAG", false);
                usbFragment.setArguments(data6);
                setFragment(usbFragment);
                break;

            //退出
            case R.id.exit:
                this.finish();
                break;
        }



    }

    public void setFragment(android.support.v4.app.Fragment fragment){
        if(fm.getBackStackEntryCount()>0){
            fm.popBackStack();
        }
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, fragment)
                .commit();
    }


    public void setImageBackgronud(int id){
        findViewById(id).setBackgroundResource(R.drawable.dui);
    }

    public void setImageRedBackgronud(int id){
        findViewById(id).setBackgroundResource(R.drawable.cuo);
    }


    Handler handler=new Handler(){
        public void handleMessage(android.os.Message message) {
            String data = message.getData().getString("data");
            switch (message.what) {
                case LINKSUCCESSFUL:
                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    break;
                case LINKFAIL:
                    Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
//                    if (dayinjiFragment!=null){
//                        dayinjiFragment.setDayinBt(true);
//                    }

                    break;
                case SETBAUDRATE:
                    Toast.makeText(MainActivity.this, "设置波特率" + data, Toast.LENGTH_SHORT).show();
                    break;
                case SENDPORTDATA:
                    Toast.makeText(MainActivity.this, "服务发送串口数据" + data, Toast.LENGTH_SHORT).show();
                    break;
                case RECEIVEPORTDATA:

                    break;
                case SENDIMAGEPATH:
                    Toast.makeText(MainActivity.this, "发送图片路径" + data, Toast.LENGTH_SHORT).show();
//                    if (dayinjiFragment!=null){
//                        dayinjiFragment.setDayinBt(true);
//                    }
                    break;
                case RECEIVEKKEYBOARD:
                    String newStr = "";
                    if (usbFragment!=null){
                        usbFragment.appenMessageTv(data);
                     }
                    break;

                case PRINTSTATE:
                    String paper = message.getData().getString("data1");
                    String temp = message.getData().getString("data2");
                    Toast.makeText(MainActivity.this, "底座状态:" + paper + "\n底座温度:" + temp, Toast.LENGTH_SHORT).show();
                    break;
                case TOU:
                    String data3 = message.getData().getString("data");
                    Toast.makeText(MainActivity.this, data3, Toast.LENGTH_SHORT).show();
                    break;
                case 100:
                    if (erweimaFragment!=null){
                        erweimaFragment.appenMessageTv(data);
                    }
                    break;
                case 101:
                    if (citiaokaFragment!=null){
                        citiaokaFragment.appenMessageTv(data);
                    }
                    break;
                case 102:
                    if (ickaFragment!=null){
                        ickaFragment.appenMessageTv(data);
                    }
                    break;
                case 103:
                    if (nfckaFragment!=null){
                        nfckaFragment.appenMessageTv(data);
                    }
                    break;

            }

            super.handleMessage(message);
        };
    };


    public boolean sendPortMsg(byte[] string){
        return ChannelUtil.SingleInstance().SendMessage(string);
    }


    @Override
    public void onBackPressed() {
//        MessagePackaging message = MermoyChannel.GetChannel(Util.Name);
//        if (message != null) {
//            message.close();
//        }
        ChannelUtil.SingleInstance().Dispose();
        super.onBackPressed();
    }


    public void showToast(String str){
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    public void registUsbBord(){
        //注册usb状态广播
        IntentFilter usbFilter = new IntentFilter();
        //未正确移除Sd卡
        usbFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        //插入外部存储,如SD卡,系统会检测SD卡,测试发出的广播
        usbFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
        //已拔出掉外部大容量存储设备发出的广播(SD卡,移动硬盘),不管有没有正确卸载都会发此广播
        usbFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        //插入SD卡并且正确安装(识别)时发出的广播
        usbFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        //扩展介质被移除
        usbFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        //开始扫描介质的一个目录
        usbFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        //已经扫描完介质的一个目录
        usbFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        //扩展介质存在，但是还没有被挂载
        usbFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        //扩展介质的挂载被解除，因为它已经作为 USB 大容量存储被共享。
        usbFilter.addAction(Intent.ACTION_MEDIA_SHARED);
        //设备进入 USB 大容量存储模式
        usbFilter.addAction(Intent.ACTION_UMS_CONNECTED);
        //设备从 USB 大容量存储模式退出
        usbFilter.addAction(Intent.ACTION_UMS_CONNECTED);
        usbFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        usbFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        usbFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbFilter.addAction("android.hardware.usb.action.USB_STATE");

        usbFilter.addDataScheme("file");
        registerReceiver(usbBroadReceiver, usbFilter);
    }


    @Override
    protected void onDestroy() {
//        unregisterReceiver(usbBroadReceiver);
//        MessagePackaging channel=MermoyChannel.GetChannel(Util.Name);
//        if(channel!=null)
//        {
//            channel.close();
//        }
//        Restart.EndTime();

        ChannelUtil.SingleInstance().Dispose();

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public  void copyCfg(String picName) {
        String dirPath = Environment.getExternalStorageDirectory().getPath() + "/"+picName;

        FileOutputStream os = null;
        InputStream is = null;
        int len = -1;
        try {
//            is = MainActivity.this.getAssets().open("aaaa.bmp");
            is = MainActivity.this.getClass().getClassLoader().getResourceAsStream("assets/"+picName);
            os = new FileOutputStream(dirPath);
            byte b[] = new byte[1024];

            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }

            is.close();
            os.close();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("MainActivity", "copyCfg "+e.toString());e.printStackTrace();
            showToast("打印测试图片写入失败");
        }
    }
}
