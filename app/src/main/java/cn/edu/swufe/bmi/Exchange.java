package cn.edu.swufe.bmi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Exchange extends AppCompatActivity implements Runnable {
    private final String TAG = "Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;
    EditText rmb;
    TextView show;
    Handler handler;

    public void run() {
        Log.i(TAG, "run: run()......");
        for(int i=1;i<3;i++){
            Log.i(TAG, "run: i=" + i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
        //msg.what = 5;
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);
        //获取网络数据
        URL url = null;
        try {    url = new URL("http://www.usd-cny.com/icbc.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = inputStream2String(in);
            Log.i(TAG, "run: html=" + html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        rmb = (EditText) findViewById(R.id.rmb);
        show = (TextView) findViewById(R.id.show);
        SharedPreferences sharePreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        PreferenceManager.getDefaultSharedPreferences(this);
        dollarRate = sharePreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharePreferences.getFloat("euro_rate",0.0f);
        wonRate = sharePreferences.getFloat("won_rate",0.0f);
        Thread t = new Thread(this);
        t.start();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage: getMessage msg = " + str);
                    show.setText(str);
                }
                super.handleMessage(msg);
            }
        };
    }

    public void onClick(View btn) {
        Log.i(TAG, "onClick: ");
        String str = rmb.getText().toString();
        Log.i(TAG, "onClick: get str=" + str);
        float r = 0;
        if (str.length() > 0) {
            r = Float.parseFloat(str);
        } else {
            //用户没有输入内容
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, "onClick: r=" + r);
        if (btn.getId() == R.id.dollar) {
            show.setText(String.valueOf(r * dollarRate));
        } else if (btn.getId() == R.id.euro) {
            show.setText(String.valueOf(r * euroRate));
        } else {
            show.setText(String.valueOf(r * wonRate));
        }
    }

    public void openOne(View btn) {
        Intent config = new Intent(this, ConfigActivity.class);
        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("euro_rate_key", euroRate);
        config.putExtra("won_rate_key", wonRate);
        Log.i(TAG, "openOne: dollarRate=" + dollarRate);
        Log.i(TAG, "openOne: euroRate=" + euroRate);
        Log.i(TAG, "openOne: wonRate=" + wonRate);
        //startActivity(config);
        startActivityForResult(config,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode==2){
        Bundle bundle = data.getExtras();
        dollarRate = bundle.getFloat("key_dollar",0.1f);
        euroRate = bundle.getFloat("key_euro",0.1f);
        wonRate = bundle.getFloat("key_won",0.1f);
        Log.i(TAG, "onActivityResult: dollarRate=" + dollarRate);
        Log.i(TAG, "onActivityResult: euroRate=" + euroRate);
        Log.i(TAG, "onActivityResult: wonRate=" + wonRate);    }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
