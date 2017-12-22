package activity;

/**
 * Created by Ozi on 9/13/2016.
 */
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.ozi.mystudentapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class BerandaFragment extends Fragment {

    String newsJSON;
    JSONArray artikel = null;
    ArrayList<HashMap<String, String>> beritaList;
    ListView list;

    private static final String TAG_RESULTS="result";
    private static final String TAG_ID="id";
    private static final String TAG_JUDUL = "judul";
    private static final String TAG_TANGGAL = "tanggal";
    private static final String TAG_ALAMATWEB = "alamatweb";

    public BerandaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_beranda, container, false);
        list = (ListView) rootView.findViewById(R.id.listView);
        beritaList = new ArrayList<HashMap<String,String>>();
        getDataBerita();
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void getDataBerita(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://mystudentapp.000webhostapp.com/news.php");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                newsJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(newsJSON);
            artikel = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<artikel.length();i++){
                JSONObject c = artikel.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String judul = c.getString(TAG_JUDUL);
                String tanggal = c.getString(TAG_TANGGAL);
                String alamatweb = c.getString(TAG_ALAMATWEB);

                HashMap<String,String> persons = new HashMap<String,String>();

                persons.put(TAG_ID,id);
                persons.put(TAG_JUDUL,judul);
                persons.put(TAG_TANGGAL,tanggal);
                persons.put(TAG_ALAMATWEB,alamatweb);

                beritaList.add(persons);
            }

            final ListAdapter adapter = new SimpleAdapter(
                    getActivity(), beritaList, R.layout.news_list,
                    new String[]{TAG_JUDUL,TAG_TANGGAL},
                    new int[]{R.id.judul, R.id.tanggal}
            );

            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    adapter.getItem(position);

                    Fragment fragment = new NewsFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container_body,fragment);
                    Bundle bundle = new Bundle();
                    bundle.putString("alamatweb",beritaList.get(position).get(TAG_ALAMATWEB));
                    fragment.setArguments(bundle);
                    ft.commit();



                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
