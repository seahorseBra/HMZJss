package com.example.hmzj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private SpeechRecognizer mIat;
    private TextView mTextView;
    private Button mStartBtn;
    private Button mEndBtn;
    private String mWord = "";
    protected StringBuilder SB = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSpeech();
        mTextView = (TextView) findViewById(R.id.text);
        mStartBtn = (Button) findViewById(R.id.start);
        mEndBtn = (Button) findViewById(R.id.end);
        mStartBtn.setOnClickListener(this);
        mEndBtn.setOnClickListener(this);
    }


    private void initSpeech() {
        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        mIat = SpeechRecognizer.createRecognizer(this, null);
        //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        SB = new StringBuilder();
    }

    private RecognizerListener mRecoListener = new RecognizerListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            if (!TextUtils.isEmpty(results.getResultString())) {
                JSONObject jsonObject = JSONObject.parseObject(results.getResultString());
                JSONArray ws = jsonObject.getJSONArray("ws");
                if (ws != null && !ws.isEmpty()) {
                    for (int i = 0; i < ws.size(); i++) {
                        JSONObject jsonObject1 = ws.getJSONObject(i);
                        JSONArray cw = jsonObject1.getJSONArray("cw");
                        for (int j = 0; j < cw.size(); j++) {
                            JSONObject jsonObject2 = cw.getJSONObject(j);
                            String w = jsonObject2.getString("w");
                            if (!TextUtils.isEmpty(w)) {
                                SB.append(w);
                            }
                        }
                    }
                }
                mTextView.setText(SB.toString());
            }
        }

        //会话发生错误回调接口
        public void onError(SpeechError error) {
            String plainDescription = error.getPlainDescription(true);//获取错误码描述
            Log.d("MainActivity:", "onError() called with: error = [" + plainDescription + "]");
        }

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        //开始录音
        public void onBeginOfSpeech() {
        }

        //音量值0~30
        public void onVolumeChanged(int volume) {
        }

        //结束录音
        public void onEndOfSpeech() {
        }

        //扩展用接口
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                mIat.startListening(mRecoListener);
                break;
            case R.id.end:
                mIat.stopListening();
                break;
        }
    }
}
