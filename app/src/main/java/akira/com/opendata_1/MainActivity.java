package akira.com.opendata_1;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {

    private Button btnGET;
    private String Url;
    private Context context;
    private ListView listv;
    private MyAdapter adapter;
    private TextView tvtitle, tvAddr, tvTel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGET = (Button) findViewById(R.id.btnGET);
        Url = getResources().getString(R.string.libDist_URL);
        listv = (ListView) findViewById(R.id.list);
        tvtitle = (TextView) findViewById(R.id.tvTitle);
        tvAddr = (TextView) findViewById(R.id.tvAddr);
        tvTel = (TextView) findViewById(R.id.tvTel);
        context = this;

    }

    /**
     * Notification
     **/

    public void setNotification(Context context, int notifyID) {

        notifyID = 1; //通知編號

        final Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // 通知音效的URI，在這裡使用系統內建的通知音效
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.mu03)//圖示
                        .setContentTitle("新北博物館") //標題
                        .setContentText("新北博物館 Open Data") //內容
                        .setAutoCancel(true)                //點擊後消失
                        .setVibrate(new long[]{0, 500})     //震動效果new long[] {a,b,c,d} 延遲a毫秒→震動b毫秒→延遲c毫秒→震動d毫秒
                        .setLights(0xff0000ff, 3000, 100)   //三色燈，閃光顏色/持續毫秒/停頓毫秒
                        .setSound(soundUri);                //效果音

        // 取得系統的通知服務
        NotificationManager mNotificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //發送通知
        mNotificationManager.notify(notifyID, mBuilder.build());
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnGET:
                new okHttpGet(Url, adapter, context, listv).start();
                setNotification(context, 1);
                break;


        }
    }

    /**Network**/

    private class okHttpGet extends Thread {

        String Url;
        private InputStream inputStream = null;
        private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OkHttpClient client = new OkHttpClient();
        ArrayList<HashMap<String, String>> distArrayList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> distMap = new HashMap<String, String>();

        private MyAdapter adapter;
        private Context context;
        private ListView listv;

        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                distArrayList = (ArrayList<HashMap<String, String>>) msg.obj;


                adapter = new MyAdapter(MainActivity.this, distArrayList);

                listv.setAdapter(adapter);
                listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HashMap<String, Object> item =
                                (HashMap<String, Object>) parent.getItemAtPosition(position);
                        intentMap(item);
                    }
                });
            }
        };

        public void intentMap(HashMap<String, Object> item) {
            String X = (String) item.get("X");
            String Y = (String) item.get("Y");
            String title = (String) item.get("title");
            String address = (String) item.get("address");

            Intent intentMap = new Intent(MainActivity.this, MapsActivity.class);
            intentMap.putExtra("X", X);
            intentMap.putExtra("Y", Y);
            intentMap.putExtra("title", title);
            intentMap.putExtra("address", address);
            startActivity(intentMap);
        }

        //建構式
        public okHttpGet(String Url, MyAdapter adapter, Context context, ListView listv) {

            this.Url = Url;
            this.adapter = adapter;
            this.context = context;
            this.listv = listv;
        }

        @Override
        public void run() {
            try {

                //GET Data from internet @param  resp [JSONArray]
                Request request = new Request.Builder().url(Url).build();
                Response response = client.newCall(request).execute();
                inputStream = response.body().byteStream();
                byte[] buffer = new byte[128]; // buffer (每次讀取長度)
                int readSize = 0; // 當下讀取長度

                while ((readSize = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, readSize);
                }
                final String resp = outputStream.toString();
                JSONArray respJSONArray = new JSONArray(resp);
                parseJSONData(respJSONArray);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        public void parseJSONData(JSONArray content) {

            try {
                for (int i = 0; i < content.length(); i++) {
                    //取得每個JSONObject
                    JSONObject lib = content.getJSONObject(i);

                    distMap = new HashMap<String, String>();
                    distMap.put("title", lib.getString("title"));
                    distMap.put("address", lib.getString("address"));
                    distMap.put("tel", lib.getString("tel"));
                    distMap.put("X", lib.getString("wgs84aX"));
                    distMap.put("Y", lib.getString("wgs84aY"));
                    distArrayList.add(distMap);
                }

                Log.i("AAAA", String.valueOf(distArrayList));

                Message message = handler.obtainMessage(1, distArrayList);
                handler.sendMessage(message);


                //Show UI
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
