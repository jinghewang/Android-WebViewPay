package cn.com.hbdworld.webviewpay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class PayActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String ALIPAY_SHOP = "https://qr.alipay.com/stx05107r5oaa4fyofbkh24";//商户
    public static final String ALIPAY_PERSON = "HTTPS://QR.ALIPAY.COM/FKX06148QMZIJDXGPKXXE7";//个人(支付宝里面我的二维码)
    public static final String ALIPAY_PERSON_2_PAY = "HTTPS://QR.ALIPAY.COM/FKX01415BIHINQT6TRU53F";//个人(支付宝里面我的二维码,然后提示让用的收款码)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        //do sth
        Button btn = (Button) this.findViewById(R.id.btn_click);
        btn.setOnClickListener(this);

        btn = (Button) this.findViewById(R.id.btn_ali);
        btn.setOnClickListener(this);

        btn = (Button) this.findViewById(R.id.btn_ali2);
        btn.setOnClickListener(this);

        btn = (Button) this.findViewById(R.id.btn_ali3);
        btn.setOnClickListener(this);

        btn = (Button) this.findViewById(R.id.btn_ali_receive);
        btn.setOnClickListener(this);
    }


    //判断是否安装支付宝app
    public static boolean checkAliPayInstalled(Context context) {

        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button)view;
        Toast.makeText(this,btn.getText(), Toast.LENGTH_LONG).show();
        Intent intent = null;
        String uri = null;
        String qrcode = null;
        String alipayqr = null;
        String fullqr = null;

        switch (view.getId()) {

            case R.id.btn_ali:
                uri = "alipays://platformapi/startApp";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                //openAppByPackageName(this,MainActivity.class,"22");
                break;

            case R.id.btn_ali2:
                uri = "alipayqr://platformapi/startapp?saId=10000007";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                //openAppByPackageName(this,MainActivity.class,"22");

                break;

            case R.id.btn_ali3:
                qrcode = "https://www.baidu.com/";
                try {
                    qrcode = URLEncoder.encode(qrcode, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                alipayqr = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + qrcode;
                fullqr = alipayqr + "%3F_s%3Dweb-other&_t=" + System.currentTimeMillis();

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullqr));
                startActivity(intent);
                break;


            case R.id.btn_ali_receive://支付宝收款
                //openAliPay2Pay(ALIPAY_PERSON);

                qrcode = ALIPAY_PERSON;
                try {
                    qrcode = URLEncoder.encode(qrcode, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                alipayqr = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + qrcode;
                fullqr = alipayqr + "%3F_s%3Dweb-other&_t=" + System.currentTimeMillis();

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullqr));
                startActivity(intent);

                break;

            default:

                Toast.makeText(this, "default", Toast.LENGTH_LONG).show();
                break;
        }
    }


    /**
     * 支付
     *
     * @param qrCode
     */
    private void openAliPay2Pay(String qrCode) {
        if (openAlipayPayPage(this, qrCode)) {
            Toast.makeText(this, "跳转成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "跳转失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean openAlipayPayPage(Context context, String qrcode) {
        try {
            qrcode = URLEncoder.encode(qrcode, "utf-8");
        } catch (Exception e) {
        }
        try {
            final String alipayqr = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + qrcode;
            final String fullqr =alipayqr + "%3F_s%3Dweb-other&_t=" + System.currentTimeMillis();
            System.out.println(fullqr);
            openUri(context, fullqr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送一个intent
     *
     * @param context
     * @param s
     */
    private static void openUri(Context context, String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        context.startActivity(intent);
    }


    //作者：天是一般蓝
    //链接：https://www.zhihu.com/question/36073077/answer/142135061
    //来源：知乎
    //著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
    //可以通过包名直接打开任何第三方的app。
    //以下代码用于启动支付宝app测试可行：
    //注：App类是Application的子类，这里也可以使用Activity代替
    //关于支付宝的包名，请自行查找。
    public void openAppByPackageName(Activity app, Context context, String packageName) throws PackageManager.NameNotFoundException {
        PackageInfo pi;
        try {
            pi = app.getPackageManager().getPackageInfo(packageName,0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.setPackage(pi.packageName);
            PackageManager pManager = app.getPackageManager();
            List<ResolveInfo> apps = pManager.queryIntentActivities(
                    resolveIntent, 0);
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//重点是加这个
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                context.startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


}
