package cn.com.hbdworld.webviewpay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.drm.DrmStore;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnClickListener {


    private WebView webView = null;
    private EditText url = null;
    private EditText referer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        this.url = ((EditText) this.findViewById(R.id.url));
        this.referer = (EditText) this.findViewById(R.id.referer);
        this.webView = (WebView) this.findViewById(R.id.wb);

        final Map extraHeaders = new HashMap();
        extraHeaders.put("Referer", this.referer.getText().toString());//例如 http://www.baidu.com ))

        WebSettings settings = this.webView.getSettings();
        this.webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading( final WebView view, String url) {

                //处理支付宝
                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true;
                }

                /**
                 * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
                 */
                final PayTask task = new PayTask(MainActivity.this);
                boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
                    @Override
                    public void onPayResult(final H5PayResultModel result) {
                        // 支付结果返回
                        final String url = result.getReturnUrl();
                        if (!TextUtils.isEmpty(url)) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    view.loadUrl(url);
                                }
                            });
                        }
                    }
                });

                /**
                 * 判断是否成功拦截
                 * 若成功拦截，则无需继续加载该URL；否则继续加载
                 */
                if (!isIntercepted) {
                    view.loadUrl(url);
                }
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
        webView.requestFocusFromTouch();

        Button btn = (Button) this.findViewById(R.id.btn_click);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String turl =url.getText().toString();
                Toast.makeText(MainActivity.this, turl, Toast.LENGTH_SHORT).show();
                webView.loadUrl(turl,extraHeaders);
            }
        });


        Button btn2 = (Button) this.findViewById(R.id.btn_wx);
        btn2.setOnClickListener(this);

        btn2 = (Button) this.findViewById(R.id.btn_wx2);
        btn2.setOnClickListener(this);

        btn2 = (Button) this.findViewById(R.id.btn_pay);
        btn2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //openAliPay2Pay(ALIPAY_PERSON);
        Button btn = (Button)v;
        Toast.makeText(this,btn.getText(), Toast.LENGTH_LONG).show();
        Intent intent = null;
        switch (v.getId()) {

            case R.id.btn_pay:
                intent = new Intent(MainActivity.this,PayActivity.class);
                startActivity(intent);

                break;

            case R.id.btn_wx:

                break;

            case R.id.btn_wx2:
                break;

            default:

                Toast.makeText(this, "default", Toast.LENGTH_LONG).show();
                break;
        }
    }




    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
