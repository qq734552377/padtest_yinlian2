package fragments;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import tools.ExceptionApplication;
import ucast.com.ucast_test_pad.MainActivity;
import ucast.com.ucast_test_pad.R;

public class BluetoothFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener{

    View v;
    ListView lv;
    Button bt1;
    Button bt2;

    BluetoothAdapter adapter=null;
    List<String> bluetoothDevices=null;
    ArrayAdapter<String> arrayAdapter=null;
    ProgressDialog progressBar;
    private final UUID MY_UUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final String NAME="Bluetooth_socket";

    private BluetoothSocket clientSocket=null;
    private BluetoothDevice device11=null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        lv=(ListView) v.findViewById(R.id.textView1);
        bt1=(Button)v.findViewById(R.id.fail);
        bt2=(Button)v.findViewById(R.id.success);
//        bt1.setEnabled(false);
//        bt2.setEnabled(false);

        adapter=BluetoothAdapter.getDefaultAdapter();
        bluetoothDevices=new ArrayList<>();

        Set<BluetoothDevice> paireDevices = adapter.getBondedDevices();

        if (paireDevices.size() > 0) {
            for (BluetoothDevice device : paireDevices) {
                bluetoothDevices.add(device.getName() + "::" + device.getAddress());
            }
        }
        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1,
                bluetoothDevices);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(this);

        final IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        final IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        v.findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (adapter!=null) {
                    if (!adapter.isEnabled()) {
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        getActivity().startActivityForResult(intent, 1);
                    }
                }else {
                    Toast.makeText(getActivity(), "没有蓝牙设备", Toast.LENGTH_SHORT).show();
                }
            }
        });

        v.findViewById(R.id.saomiao).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ExceptionApplication.context.registerReceiver(receiver, filter1);
                ExceptionApplication.context.registerReceiver(receiver, filter2);

                if (adapter.isDiscovering()) {
                    adapter.cancelDiscovery();
                }
                adapter.startDiscovery();
                progressBar=ProgressDialog.show(getActivity(),"搜索中...","耐心等待");
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ((MainActivity)getActivity()).setImageRedBackgronud(R.id.bluetoothIv);
                getFragmentManager().popBackStack();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setImageBackgronud(R.id.bluetoothIv);
                getFragmentManager().popBackStack();
            }
        });
        return v;

    }




    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    String str = device.getName() + "::" + device.getAddress();
                    if (bluetoothDevices.indexOf(str) == -1) {
                        bluetoothDevices.add(str);
                        arrayAdapter.notifyDataSetChanged();
                        progressBar.dismiss();
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    progressBar.dismiss();
                    ExceptionApplication.context.unregisterReceiver(receiver);
                }

            }

        };
    };
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        if (bluetoothDevices!=null){
            if (adapter.isDiscovering()) {
                adapter.cancelDiscovery();
            }
            return;
        }
//        String s=arrayAdapter.getItem(position);
//        String item[]=s.split("::");
//        String address=item[1];
//        Log.e("class", "获取地址");
//        if (pair(address, "1234")){
//            bt1.setEnabled(true);
//        }


    }


}
