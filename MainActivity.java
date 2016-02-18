package my.test.com.customview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_DrugView:
                startActivity(new Intent(this,DrugActivity.class));
                break;
            case R.id.id_readFromAssets:
                String fileName = "curve_clean.bin";
                this.parseCurvebin(fileName);
//                String temp = getFromAssets(fileName);
//                Toast.makeText(this,temp,Toast.LENGTH_SHORT).show();
            case R.id.id_canvasView:
                startActivity(new Intent(this,DisplayActivity.class));
                break;
            default:
                break;
        }
    }

    private final String TAG = "MainActivity";
    public List<Object> parseCurvebin(String fileName){
        System.out.println("Test.parseCurvebin(byte[] raw) ...");
        try {
            List<Object> datas = new ArrayList<Object>();
            InputStream inputStream=getResources().getAssets().open(fileName) ;
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0 ,bytes.length);
            DataInputStream in = new DataInputStream(
                    new ByteArrayInputStream(bytes));

            final int CURVE_ENV_NUM = 6;
            final int CURVE_POINTS_NUM = 2000;

            byte[] num = new byte[4];


            in.read(num, 0, num.length);

//            int count = MainActivity.byteArrayToInt(num);
            int count = (num[3] << 24) + (num[2] << 16) + (num[1] << 8) + num[0];
            Log.i(TAG, "line count:" + count);
            byte[] curve = new byte[CURVE_ENV_NUM * 4 + CURVE_POINTS_NUM * 2 * 2];
            for(int i = 0; i < 1; i++){
                int len = in.read(curve, 0, curve.length);
                if(len != curve.length)
                    break;

                int pos = CURVE_ENV_NUM * 4;
                float[] chn1 = new float[CURVE_POINTS_NUM];
                for(int j = 0; j < chn1.length; j++){
                    float pre=(getUnsignedByte(curve[pos + j * 2 + 1]) << 8);
                    Log.i(TAG, "pre:"+pre);
                    float end=getUnsignedByte(curve[pos + j * 2]);
                    Log.i(TAG, "end:"+end);
                    chn1[j]=pre+end;
//                    chn1[j] = (float)((curve[pos + j * 2 + 1] << 8) + curve[pos + j * 2]);
                    Log.i(TAG, "chn1 pos:"+j+" value:" + (float)((curve[pos + j * 2 + 1] << 8) + curve[pos + j * 2]));
//                    Log.i(TAG, "chn1 pos:"+j);
                    Thread.sleep(10);

                }
                pos += chn1.length*2 ;
                float[] chn2 = new float[CURVE_POINTS_NUM];
                for(int j = 0; j < chn2.length; j++){
                    float pre=(getUnsignedByte(curve[pos + j * 2 + 1]) << 8);
                    Log.i(TAG, "pre:"+pre);
                    float end=getUnsignedByte(curve[pos + j * 2]);

                    Log.i(TAG, "end:"+end);
                    chn2[j]=pre+end;
//                    chn2[j] = (float)((curve[pos + j * 2 + 1] << 8) + curve[pos + j * 2]);
                    Log.i(TAG, "chn2 pos:"+j+" value:" + (float)((curve[pos + j * 2 + 1] << 8) + curve[pos + j * 2]));
//                    Log.i(TAG, "chn2 pos:"+j);
                    Thread.sleep(10);
                }
                // System.out.println("chn1: " + Arrays.toString(chn1));
                // System.out.println("chn2: " + Arrays.toString(chn2));

//                DetectorData data = new DetectorData(chn1, chn2);
//                datas.add(data);
            }
            return datas;
        }
        catch(EOFException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getUnsignedByte (byte data){      //将data字节型数据转换为0~255 (0xFF 即BYTE)。
        return data&0x0FF;
    }
    public static int byteArrayToInt(byte[] b) {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }
    public String getFromAssets(String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
