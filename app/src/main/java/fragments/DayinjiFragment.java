package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.messagerprotocol.SendPackage;

import ucast.com.ucast_test_pad.GeneratePicture;
import ucast.com.ucast_test_pad.MainActivity;
import ucast.com.ucast_test_pad.R;

public class DayinjiFragment extends android.support.v4.app.Fragment {

    View v;
    TextView title;


    Button bt1;
    Button bt2;
//    Button bt3;
    Button bt4;
    Button fail;
    Button success;
    String imagePath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dayinji_layout, container, false);

        bt1= (Button) v.findViewById(R.id.base_bt1);
        bt2= (Button) v.findViewById(R.id.base_bt2);
//        bt3= (Button) v.findViewById(R.id.base_bt3);
        bt4= (Button) v.findViewById(R.id.base_bt4);
        fail= (Button) v.findViewById(R.id.base_fail);
        success= (Button) v.findViewById(R.id.base_sucess);
        title= (EditText) v.findViewById(R.id.base_et);


        bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).sendPortMsg(SendPackage.PrintState());
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String photoString = title.getText().toString();
                if (photoString.equals("")) {
                    imagePath = GeneratePicture.ALBUM_PATH + "/bb.bmp";
                    return;
                }
                imagePath = GeneratePicture.getBitMap(photoString);
                if (imagePath != null) {
                    Toast.makeText(getActivity(), "图片生成成功!", Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).sendPortMsg(SendPackage.ImagePath(imagePath));
                } else {
                    Toast.makeText(getActivity(), "图片生成失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        bt3.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (imagePath != null) {
//                    ((MainActivity) getActivity()).sendPortMsg(SendPackage.ImagePath(imagePath));
////                    bt3.setEnabled(false);
//                } else {
//                    Toast.makeText(getActivity(), "没有图片！", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        bt4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                title.setText("");
                //TODO 打印指定图片
                String path1=GeneratePicture.ALBUM_PATH + "/aaaa.bmp";
                String path2=GeneratePicture.ALBUM_PATH + "/bbbb.bmp";
                String path3=GeneratePicture.ALBUM_PATH + "/cccc.bmp";


                ((MainActivity) getActivity()).sendPortMsg(SendPackage.ImagePath(path3));
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ((MainActivity) getActivity()).sendPortMsg(SendPackage.ImagePath(path2));
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((MainActivity) getActivity()).sendPortMsg(SendPackage.ImagePath(path1));



            }




        });
        fail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setImageRedBackgronud(R.id.dayinjiIv);
                getFragmentManager().popBackStack();
            }
        });
        success.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setImageBackgronud(R.id.dayinjiIv);
                getFragmentManager().popBackStack();
            }
        });
        return v;

    }


    public void setDayinBt(boolean state){
        bt1.setEnabled(state);
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
