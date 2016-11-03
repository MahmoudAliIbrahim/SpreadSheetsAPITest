package dk.izbrannick.glutter.sheetstest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "HttpExample";
    ArrayList<Team> teams = new ArrayList<>();
    private ListView listview;
    private String SpreadSheetURL_ = "https://spreadsheets.google.com/tq?key=15nFv1Ap8NHAwFW0NU6ow9DGLdI4sT4pLiCUfdFmW6XQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            t.start();
        }
    }

    Thread t = new Thread() {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // update TextView here!
                            new DownloadWebPageTask(new AsyncResult() {
                                @Override
                                public void onResult(JSONObject object) {
                                    processJson(object);
                                }
                            }).execute(SpreadSheetURL_);
                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };

    private void processJson(JSONObject object) {

        try {
            teams = new ArrayList<>();
            JSONArray rows = object.getJSONArray("rows");

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                int position = columns.getJSONObject(0).getInt("v");
                String name = columns.getJSONObject(1).getString("v");
                int wins = columns.getJSONObject(3).getInt("v");
                int draws = columns.getJSONObject(4).getInt("v");
                int losses = columns.getJSONObject(5).getInt("v");
                int points = columns.getJSONObject(19).getInt("v");
                Team team = new Team(position, name, wins, draws, losses, points);
                teams.add(team);
            }

            final TeamsAdapter adapter = new TeamsAdapter(this, R.layout.team, teams);
            listview.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
