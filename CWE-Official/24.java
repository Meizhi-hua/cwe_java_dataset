public class test {
    public void fun() {
        IntentFilter filter = new IntentFilter("com.example.URLHandler.openURL");
        MyReceiver receiver = new MyReceiver();
        registerReceiver(receiver, filter);
    }
}
public class UrlHandlerReceiver extends BroadcastReceiver {
@Override
    public void onReceive(Context context, Intent intent) {
        if("com.example.URLHandler.openURL".equals(intent.getAction())) {
        String URL = intent.getStringExtra("URLToOpen");
        int length = URL.length();
        }
    }
}
