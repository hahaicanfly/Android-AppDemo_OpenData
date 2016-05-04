package akira.com.opendata_1;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Akira on 2016/4/26.
 */
public class okHttp {


    OkHttpClient client = new OkHttpClient();

    String run(String Url) throws IOException {

        Request request = new Request.Builder().url(Url).build();

        Response response = client.newCall(request).execute();
        String resp = response.body().string();

        Log.i("AAAA", resp);
        return resp;
    }


}
