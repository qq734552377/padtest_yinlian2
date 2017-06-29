package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.project.messagerprotocol.SendPackage;
import com.project.messagerprotocol.Util;

import ucast.com.ucast_test_pad.MainActivity;
import ucast.com.ucast_test_pad.R;

public class PortFragment extends android.support.v4.app.Fragment {

    View v;
    EditText et1;
    EditText et2;

    Button bt1;
    Button bt2;
    Button ceshi;
    Button fasongbote;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.port_layout, container, false);


        bt1= (Button) v.findViewById(R.id.base_bt1);
        bt2= (Button) v.findViewById(R.id.base_bt2);
        ceshi= (Button) v.findViewById(R.id.base_ceshi);
        fasongbote= (Button) v.findViewById(R.id.base_fasongbote);

        et1= (EditText) v.findViewById(R.id.base_et1);
        et2= (EditText) v.findViewById(R.id.base_et2);



        bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setImageRedBackgronud(R.id.chuankouIv);
                getFragmentManager().popBackStack();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setImageBackgronud(R.id.chuankouIv);
                getFragmentManager().popBackStack();
            }
        });
        fasongbote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = et1.getText().toString().trim();
                ((MainActivity) getActivity()).sendPortMsg(SendPackage.PortBaudrate(string));

            }
        });

        ceshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = et2.getText().toString().trim();
                byte[] str = packagePortData(string);
                ((MainActivity) getActivity()).sendPortMsg(SendPackage.SerialData(str));
            }
        });

        return v;

    }
    //将字符串转换为16进制byte数组
    private byte[] packagePortData(String carString) {
        try {
            String[] str = carString.split(",");
            byte[] buffer = new byte[str.length];
            for (int i = 0; i < str.length; i++) {
                buffer[i] = (byte) (Integer.decode(str[i]) & 0xFF);
            }
            return buffer;
        } catch (Exception e) {
            return null;
        }
    }
//    public void setMessageTv(String msg){
//        if (message!=null)
//            message.setText(msg);
//    }
//    public void appenMessageTv(String msg){
//        if (message!=null)
//            message.append(msg + "\n");
//    }




}
