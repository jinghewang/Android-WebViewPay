package cn.com.hbdworld.webviewpay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private WebView wb = null;
    private EditText url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        this.url = ((EditText) this.findViewById(R.id.url));
        this.wb = (WebView) this.findViewById(R.id.wb);


        final Map extraHeaders = new HashMap();
        extraHeaders.put("Referer", "http://wxpay.wxutil.com");//例如 http://www.baidu.com ))

        WebSettings settings = this.wb.getSettings();
        settings.setJavaScriptEnabled(true);
        this.wb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        settings.setJavaScriptEnabled(true);


        //打开页面时， 自适应屏幕：
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);

        //便页面支持缩放：
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);

        //如果webView中需要用户手动输入用户名、密码或其他，则webview必须设置支持获取手势焦点。
        wb.requestFocusFromTouch();



        Button btn = (Button) this.findViewById(R.id.btn_click);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String turl =url.getText().toString();
                Toast.makeText(MainActivity.this, turl, Toast.LENGTH_SHORT).show();
                wb.loadUrl(turl,extraHeaders);
            }
        });
    }
}
