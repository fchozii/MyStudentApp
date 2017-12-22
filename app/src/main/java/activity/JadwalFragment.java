package activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import com.example.ozi.mystudentapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import adapter.Config;

/**
 * Created by Ozi on 9/13/2016.
 */
public class JadwalFragment extends Fragment {

    public static String username;

    JSONArray Jadwal = null;
    String JdwlJSON;

    private static final String TAG_RESULTS="result";
    private static final String TAG_HARI="hari";
    private static final String TAG_KDMK="kd_mk";
    private static final String TAG_NMMK = "nm_mk";
    private static final String TAG_WAKTU = "waktu";
    private static final String TAG_RUANG = "ruang";
    private static final String TAG_DOSEN = "dosen";

    ArrayList<HashMap<String, String>> jadwalListSenin,jadwalListSelasa,jadwalListRabu
            ,jadwalListKamis,jadwalListJumat,jadwalListSabtu;

    ListView listsenin,listselasa,listrabu,listkamis,listjumat,listsabtu;

    public JadwalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_jadwal, container, false);

        TabHost host = (TabHost) rootView.findViewById(R.id.tabHost);
        host.setup();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        TabHost.TabSpec spec = host.newTabSpec("Senin");
        spec.setContent(R.id.tabsenin);
        spec.setIndicator("Senin");
        host.addTab(spec);

        spec = host.newTabSpec("Selasa");
        spec.setContent(R.id.tabselasa);
        spec.setIndicator("Selasa");
        host.addTab(spec);

        spec = host.newTabSpec("Rabu");
        spec.setContent(R.id.tabrabu);
        spec.setIndicator("Rabu");
        host.addTab(spec);

        spec = host.newTabSpec("Kamis");
        spec.setContent(R.id.tabkamis);
        spec.setIndicator("Kamis");
        host.addTab(spec);

        spec = host.newTabSpec("Jumat");
        spec.setContent(R.id.tabjumat);
        spec.setIndicator("Jumat");
        host.addTab(spec);

        spec = host.newTabSpec("Sabtu");
        spec.setContent(R.id.tabsabtu);
        spec.setIndicator("Sabtu");
        host.addTab(spec);

        listsenin = (ListView) rootView.findViewById(R.id.listsenin);
        listselasa = (ListView) rootView.findViewById(R.id.listselasa);
        listrabu = (ListView) rootView.findViewById(R.id.listrabu);
        listkamis = (ListView) rootView.findViewById(R.id.listkamis);
        listjumat = (ListView) rootView.findViewById(R.id.listjumat);
        listsabtu = (ListView) rootView.findViewById(R.id.listsabtu);
        jadwalListSenin = new ArrayList<HashMap<String,String>>();
        jadwalListSelasa = new ArrayList<HashMap<String,String>>();
        jadwalListRabu = new ArrayList<HashMap<String,String>>();
        jadwalListKamis = new ArrayList<HashMap<String,String>>();
        jadwalListJumat = new ArrayList<HashMap<String,String>>();
        jadwalListSabtu = new ArrayList<HashMap<String,String>>();
        getJadwalData();
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getJadwalData() {

        class getDataJSONnilai extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://mystudentapp.000webhostapp.com/jadwal.php");


                InputStream inputStream = null;
                String result = null;

                try {
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("username", username));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();


                } catch (Exception e) {
                    // Oops
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return result;

            }

            @Override
            protected void onPostExecute(String result){
                JdwlJSON=result;
                showJadwal();
            }
        }
        getDataJSONnilai g = new getDataJSONnilai();
        g.execute();
    }

    protected void showJadwal(){
        try {
            JSONObject jsonObj = new JSONObject(JdwlJSON);
            Jadwal = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<Jadwal.length();i++){
                JSONObject c = Jadwal.getJSONObject(i);
                String hari = c.getString(TAG_HARI);
                String kd_mk = c.getString(TAG_KDMK);
                String nm_mk = c.getString(TAG_NMMK);
                String waktu = c.getString(TAG_WAKTU);
                String ruang = c.getString(TAG_RUANG);
                String dosen = c.getString(TAG_DOSEN);

                if(hari.equals("Senin")) {

                    HashMap<String, String> jadwal = new HashMap<String, String>();

                    jadwal.put(TAG_HARI, hari);
                    jadwal.put(TAG_KDMK, kd_mk);
                    jadwal.put(TAG_NMMK, nm_mk);
                    jadwal.put(TAG_WAKTU, waktu);
                    jadwal.put(TAG_RUANG, ruang);
                    jadwal.put(TAG_DOSEN, dosen);


                    jadwalListSenin.add(jadwal);
                }

                else if(hari.equals("Selasa")) {

                    HashMap<String, String> jadwal = new HashMap<String, String>();

                    jadwal.put(TAG_HARI, hari);
                    jadwal.put(TAG_KDMK, kd_mk);
                    jadwal.put(TAG_NMMK, nm_mk);
                    jadwal.put(TAG_WAKTU, waktu);
                    jadwal.put(TAG_RUANG, ruang);
                    jadwal.put(TAG_DOSEN, dosen);


                    jadwalListSelasa.add(jadwal);
                }

                else if(hari.equals("Rabu")) {

                    HashMap<String, String> jadwal = new HashMap<String, String>();

                    jadwal.put(TAG_HARI, hari);
                    jadwal.put(TAG_KDMK, kd_mk);
                    jadwal.put(TAG_NMMK, nm_mk);
                    jadwal.put(TAG_WAKTU, waktu);
                    jadwal.put(TAG_RUANG, ruang);
                    jadwal.put(TAG_DOSEN, dosen);


                    jadwalListRabu.add(jadwal);
                }

                else if(hari.equals("Kamis")) {

                    HashMap<String, String> jadwal = new HashMap<String, String>();

                    jadwal.put(TAG_HARI, hari);
                    jadwal.put(TAG_KDMK, kd_mk);
                    jadwal.put(TAG_NMMK, nm_mk);
                    jadwal.put(TAG_WAKTU, waktu);
                    jadwal.put(TAG_RUANG, ruang);
                    jadwal.put(TAG_DOSEN, dosen);


                    jadwalListKamis.add(jadwal);
                }

                else if(hari.equals("Jumat")) {

                    HashMap<String, String> jadwal = new HashMap<String, String>();

                    jadwal.put(TAG_HARI, hari);
                    jadwal.put(TAG_KDMK, kd_mk);
                    jadwal.put(TAG_NMMK, nm_mk);
                    jadwal.put(TAG_WAKTU, waktu);
                    jadwal.put(TAG_RUANG, ruang);
                    jadwal.put(TAG_DOSEN, dosen);


                    jadwalListJumat.add(jadwal);
                }

                else if(hari.equals("Sabtu")) {

                    HashMap<String, String> jadwal = new HashMap<String, String>();

                    jadwal.put(TAG_HARI, hari);
                    jadwal.put(TAG_KDMK, kd_mk);
                    jadwal.put(TAG_NMMK, nm_mk);
                    jadwal.put(TAG_WAKTU, waktu);
                    jadwal.put(TAG_RUANG, ruang);
                    jadwal.put(TAG_DOSEN, dosen);


                    jadwalListSabtu.add(jadwal);
                }

            }

            final ListAdapter adaptersenin = new SimpleAdapter(
                    getActivity(), jadwalListSenin, R.layout.jadwal_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_WAKTU,TAG_RUANG,TAG_DOSEN},
                    new int[]{R.id.kd_mk, R.id.nm_mk, R.id.waktu ,R.id.ruang, R.id.dosen});

            final ListAdapter adapterselasa = new SimpleAdapter(
                    getActivity(), jadwalListSelasa, R.layout.jadwal_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_WAKTU,TAG_RUANG,TAG_DOSEN},
                    new int[]{R.id.kd_mk, R.id.nm_mk, R.id.waktu ,R.id.ruang, R.id.dosen});

            final ListAdapter adapterrabu = new SimpleAdapter(
                    getActivity(), jadwalListRabu, R.layout.jadwal_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_WAKTU,TAG_RUANG,TAG_DOSEN},
                    new int[]{R.id.kd_mk, R.id.nm_mk, R.id.waktu ,R.id.ruang, R.id.dosen});

            final ListAdapter adapterkamis = new SimpleAdapter(
                    getActivity(), jadwalListKamis, R.layout.jadwal_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_WAKTU,TAG_RUANG,TAG_DOSEN},
                    new int[]{R.id.kd_mk, R.id.nm_mk, R.id.waktu ,R.id.ruang, R.id.dosen});

            final ListAdapter adapterjumat = new SimpleAdapter(
                    getActivity(), jadwalListJumat, R.layout.jadwal_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_WAKTU,TAG_RUANG,TAG_DOSEN},
                    new int[]{R.id.kd_mk, R.id.nm_mk, R.id.waktu ,R.id.ruang, R.id.dosen});

            final ListAdapter adaptersabtu = new SimpleAdapter(
                    getActivity(), jadwalListSabtu, R.layout.jadwal_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_WAKTU,TAG_RUANG,TAG_DOSEN},
                    new int[]{R.id.kd_mk, R.id.nm_mk, R.id.waktu ,R.id.ruang, R.id.dosen});



            listsenin.setAdapter(adaptersenin);
            listselasa.setAdapter(adapterselasa);
            listrabu.setAdapter(adapterrabu);
            listkamis.setAdapter(adapterkamis);
            listjumat.setAdapter(adapterjumat);
            listsabtu.setAdapter(adaptersabtu);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}