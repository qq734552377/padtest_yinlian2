package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ucast.com.ucast_test_pad.MainActivity;
import ucast.com.ucast_test_pad.R;

public class BaseFragment extends android.support.v4.app.Fragment {

    View v;
    TextView title;
    TextView message;

    Button bt1;
    Button bt2;
    Button ceshi;
    int id;

    byte [] type;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.base_layout, container, false);

        title= (TextView) v.findViewById(R.id.base_title);
        message= (TextView) v.findViewById(R.id.base_message);
        bt1= (Button) v.findViewById(R.id.base_bt1);
        bt2= (Button) v.findViewById(R.id.base_bt2);
        ceshi= (Button) v.findViewById(R.id.base_ceshi);
        Bundle data = getArguments();
        id=data.getInt("ID");
        title.setText(data.getString("TITLE"));
        if (!data.getBoolean("FLAG")){
            v.findViewById(R.id.ceshi_layout).setVisibility(View.GONE);
            ceshi.setVisibility(View.GONE);
        }
        type=data.getByteArray("SENDTYPE");

        ceshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //相应操作
                ((MainActivity)getActivity()).sendPortMsg(type);
            }
        });


        bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ((MainActivity)getActivity()).setImageRedBackgronud(id);
                getFragmentManager().popBackStack();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setImageBackgronud(id);
                getFragmentManager().popBackStack();
            }
        });
        return v;

    }

    public void setMessageTv(String msg){
        if (message!=null)
            message.setText(msg);
    }
    public void appenMessageTv(String msg){
        if (message!=null)
            message.append(msg + "\n");
    }




}
