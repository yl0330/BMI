package cn.edu.swufe.bmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView hight;
    TextView weight;
    TextView daan;
    TextView tip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hight = (TextView) findViewById(R.id.hight);
        weight = (TextView) findViewById(R.id.weigth);
        daan = (TextView) findViewById(R.id.daan);
        tip = (TextView) findViewById(R.id.tip);
        Button btn = (Button) findViewById(R.id.send);
    }
    public void bim(View btn){
        String h = hight.getText().toString();
        float nh = Float.parseFloat(h);
        String w = weight.getText().toString();
        float nw = Float.parseFloat(w);
        daan.setText(String.valueOf(nh/(nw*nw)));

    }
}
