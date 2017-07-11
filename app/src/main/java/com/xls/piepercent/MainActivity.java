package com.xls.piepercent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xls.piepercentview.PiePercentView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF00FF00, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    private int[] values4 = new int[]{20,15,55,10};
    private int[] values5 = new int[]{30,5,25,15,25};
    private int[] values6 = new int[]{18,20,12,10,30,10};
    private PiePercentView mCPV;
    private Button initBtn,arcVibtn,innerVibtn;
    private int mCount=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCPV = (PiePercentView) findViewById(R.id.cpv);
        initBtn = (Button) findViewById(R.id.btn_init);
        arcVibtn = (Button) findViewById(R.id.btn_arc_visible);
        innerVibtn = (Button) findViewById(R.id.btn_inner_visible);

        initBtn.setOnClickListener(this);
        arcVibtn.setOnClickListener(this);
        innerVibtn.setOnClickListener(this);

    }


    private List<PiePercentView.PieData> initData(int count){
        int[] values = new int[count];
        switch (count){
            case 4:
                values = values4;
                break;
            case 5:
                values = values5;
                break;
            case 6:
                values = values6;
                break;
        }

        List<PiePercentView.PieData> dataList = new ArrayList<>();
        for(int i=0;i<count;i++){
            PiePercentView.PieData pieData = new PiePercentView.PieData();
            pieData.setColor(mColors[i]);
            pieData.setName("a"+i);
            pieData.setPercent(values[i]/100f);
            dataList.add(pieData);
        }
        return dataList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_init:
                mCPV.setData(initData(mCount++));
                if(mCount>6){
                    mCount=4;
                }
                arcVibtn.setText(mCPV.ismIsArcTextVisible()?"百分比隐藏":"百分比显示");
                innerVibtn.setText(mCPV.ismIsInnerTextVisible()?"内容隐藏":"内容显示");
                break;
            case R.id.btn_arc_visible:
                if("百分比隐藏".equals(arcVibtn.getText().toString())){
                    mCPV.setmIsArcTextVisible(false);
                    arcVibtn.setText("百分比显示");
                }else if("百分比显示".equals(arcVibtn.getText().toString())){
                    mCPV.setmIsArcTextVisible(true);
                    arcVibtn.setText("百分比隐藏");
                }

                break;
            case R.id.btn_inner_visible:
                if("内容隐藏".equals(innerVibtn.getText().toString())){
                    mCPV.setmIsInnerTextVisible(false);
                    innerVibtn.setText("内容显示");
                }else if("内容显示".equals(innerVibtn.getText().toString())){
                    mCPV.setmIsInnerTextVisible(true);
                    innerVibtn.setText("内容隐藏");
                }
                break;
        }
    }
}
