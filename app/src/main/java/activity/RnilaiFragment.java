package activity;

import android.app.Activity;
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

/**
 * Created by Ozi on 9/13/2016.
 */
public class RnilaiFragment extends Fragment {

    public static String username;

    JSONArray Nilai = null;
    String NilaiJSON;

    private static final String TAG_RESULTS="result";
    private static final String TAG_KDMK="kd_matkul";
    private static final String TAG_NMMK="nm_matkul";
    private static final String TAG_JENIS = "jenis";
    private static final String TAG_SKS = "sks";
    private static final String TAG_NILAI = "nilai";
    private static final String TAG_MUTU = "mutu";
    private static final String TAG_SEMESTER ="semester";

    ArrayList<HashMap<String, String>> semester1List, semester2List, semester3List, semester4List, semester5List, semester6List,
            semester7List,semester8List;

    ListView listsemester1,listsemester2,listsemester3,listsemester4,listsemester5,listsemester6, listsemester7, listsemester8;

    public RnilaiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rnilai, container, false);

        TabHost host = (TabHost) rootView.findViewById(R.id.tabHost);
        host.setup();

        Bundle bundle = this.getArguments();
        username = bundle.getString("username");

        TabHost.TabSpec spec = host.newTabSpec("Semester 1");
        spec.setContent(R.id.tabsemester1);
        spec.setIndicator("Semester 1");
        host.addTab(spec);

        spec = host.newTabSpec("Semester 2");
        spec.setContent(R.id.tabsemester2);
        spec.setIndicator("Semester 2");
        host.addTab(spec);

        spec = host.newTabSpec("Semester 3");
        spec.setContent(R.id.tabsemester3);
        spec.setIndicator("Semester 3");
        host.addTab(spec);

        spec = host.newTabSpec("Semester 4");
        spec.setContent(R.id.tabsemester4);
        spec.setIndicator("Semester 4");
        host.addTab(spec);

        spec = host.newTabSpec("Semester 5");
        spec.setContent(R.id.tabsemester5);
        spec.setIndicator("Semester 5");
        host.addTab(spec);

        spec = host.newTabSpec("Semester 6");
        spec.setContent(R.id.tabsemester6);
        spec.setIndicator("Semester 6");
        host.addTab(spec);

        spec = host.newTabSpec("Semester 7");
        spec.setContent(R.id.tabsemester7);
        spec.setIndicator("Semester 7");
        host.addTab(spec);

        spec = host.newTabSpec("Semester 8");
        spec.setContent(R.id.tabsemester8);
        spec.setIndicator("Semester 8");
        host.addTab(spec);

        listsemester1 = (ListView) rootView.findViewById(R.id.listsemester1);
        listsemester2 = (ListView) rootView.findViewById(R.id.listsemester2);
        listsemester3 = (ListView) rootView.findViewById(R.id.listsemester3);
        listsemester4 = (ListView) rootView.findViewById(R.id.listsemester4);
        listsemester5 = (ListView) rootView.findViewById(R.id.listsemester5);
        listsemester6 = (ListView) rootView.findViewById(R.id.listsemester6);
        listsemester7 = (ListView) rootView.findViewById(R.id.listsemester7);
        listsemester8 = (ListView) rootView.findViewById(R.id.listsemester8);
        semester1List = new ArrayList<HashMap<String,String>>();
        semester2List = new ArrayList<HashMap<String,String>>();
        semester3List = new ArrayList<HashMap<String,String>>();
        semester4List = new ArrayList<HashMap<String,String>>();
        semester5List = new ArrayList<HashMap<String,String>>();
        semester6List = new ArrayList<HashMap<String,String>>();
        semester7List = new ArrayList<HashMap<String,String>>();
        semester8List = new ArrayList<HashMap<String,String>>();
        getNilaiData();
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

    public void getNilaiData() {

        class getDataJSONnilai extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://mystudentapp.000webhostapp.com/rnilai.php");


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
                NilaiJSON=result;
                showListNilai();
            }
        }
        getDataJSONnilai g = new getDataJSONnilai();
        g.execute();
    }

    protected void showListNilai(){
        try {
            JSONObject jsonObj = new JSONObject(NilaiJSON);
            Nilai = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<Nilai.length();i++){
                JSONObject c = Nilai.getJSONObject(i);
                String kd_mk = c.getString(TAG_KDMK);
                String nm_mk = c.getString(TAG_NMMK);
                String jenis = c.getString(TAG_JENIS);
                String sks = c.getString(TAG_SKS);
                String nilai = c.getString(TAG_NILAI);
                String mutu = c.getString(TAG_MUTU);
                String semester = c.getString(TAG_SEMESTER);

                switch (semester) {
                    case "65":
                        HashMap<String, String> rangkumans1 = new HashMap<String, String>();
                        rangkumans1.put(TAG_KDMK, kd_mk);
                        rangkumans1.put(TAG_NMMK, nm_mk);
                        rangkumans1.put(TAG_JENIS, jenis);
                        rangkumans1.put(TAG_SKS, sks);
                        rangkumans1.put(TAG_NILAI, nilai);
                        rangkumans1.put(TAG_MUTU, mutu);
                        rangkumans1.put(TAG_SEMESTER, semester);
                        semester1List.add(rangkumans1);
                        break;

                    case "66" :
                    case "214":
                    case "110":
                    case "114":
                        HashMap<String, String> rangkumans2 = new HashMap<String, String>();
                        rangkumans2.put(TAG_KDMK, kd_mk);
                        rangkumans2.put(TAG_NMMK, nm_mk);
                        rangkumans2.put(TAG_JENIS, jenis);
                        rangkumans2.put(TAG_SKS, sks);
                        rangkumans2.put(TAG_NILAI, nilai);
                        rangkumans2.put(TAG_MUTU, mutu);
                        rangkumans2.put(TAG_SEMESTER, semester);
                        semester2List.add(rangkumans2);
                        break;

                    case "67" :
                    case "115":
                    case "111":
                        HashMap<String, String> rangkumans3 = new HashMap<String, String>();
                        rangkumans3.put(TAG_KDMK, kd_mk);
                        rangkumans3.put(TAG_NMMK, nm_mk);
                        rangkumans3.put(TAG_JENIS, jenis);
                        rangkumans3.put(TAG_SKS, sks);
                        rangkumans3.put(TAG_NILAI, nilai);
                        rangkumans3.put(TAG_MUTU, mutu);
                        rangkumans3.put(TAG_SEMESTER, semester);
                        semester3List.add(rangkumans3);
                        break;

                    case "68":
                    case "215":
                    case "210":
                        HashMap<String, String> rangkumans4 = new HashMap<String, String>();
                        rangkumans4.put(TAG_KDMK, kd_mk);
                        rangkumans4.put(TAG_NMMK, nm_mk);
                        rangkumans4.put(TAG_JENIS, jenis);
                        rangkumans4.put(TAG_SKS, sks);
                        rangkumans4.put(TAG_NILAI, nilai);
                        rangkumans4.put(TAG_MUTU, mutu);
                        rangkumans4.put(TAG_SEMESTER, semester);
                        semester4List.add(rangkumans4);
                        break;

                    case "69":
                    case "116":
                    case "112":
                        HashMap<String, String> rangkumans5 = new HashMap<String, String>();
                        rangkumans5.put(TAG_KDMK, kd_mk);
                        rangkumans5.put(TAG_NMMK, nm_mk);
                        rangkumans5.put(TAG_JENIS, jenis);
                        rangkumans5.put(TAG_SKS, sks);
                        rangkumans5.put(TAG_NILAI, nilai);
                        rangkumans5.put(TAG_MUTU, mutu);
                        rangkumans5.put(TAG_SEMESTER, semester);
                        semester5List.add(rangkumans5);
                        break;

                    case "70":
                    case "216":
                    case "211":
                        HashMap<String, String> rangkumans6 = new HashMap<String, String>();
                        rangkumans6.put(TAG_KDMK, kd_mk);
                        rangkumans6.put(TAG_NMMK, nm_mk);
                        rangkumans6.put(TAG_JENIS, jenis);
                        rangkumans6.put(TAG_SKS, sks);
                        rangkumans6.put(TAG_NILAI, nilai);
                        rangkumans6.put(TAG_MUTU, mutu);
                        rangkumans6.put(TAG_SEMESTER, semester);
                        semester6List.add(rangkumans6);
                        break;

                    case "71":
                        HashMap<String, String> rangkumans7 = new HashMap<String, String>();
                        rangkumans7.put(TAG_KDMK, kd_mk);
                        rangkumans7.put(TAG_NMMK, nm_mk);
                        rangkumans7.put(TAG_JENIS, jenis);
                        rangkumans7.put(TAG_SKS, sks);
                        rangkumans7.put(TAG_NILAI, nilai);
                        rangkumans7.put(TAG_MUTU, mutu);
                        rangkumans7.put(TAG_SEMESTER, semester);
                        semester7List.add(rangkumans7);
                        break;

                    case "72":
                        HashMap<String, String> rangkuman8 = new HashMap<String, String>();
                        rangkuman8.put(TAG_KDMK, kd_mk);
                        rangkuman8.put(TAG_NMMK, nm_mk);
                        rangkuman8.put(TAG_JENIS, jenis);
                        rangkuman8.put(TAG_SKS, sks);
                        rangkuman8.put(TAG_NILAI, nilai);
                        rangkuman8.put(TAG_MUTU, mutu);
                        rangkuman8.put(TAG_SEMESTER, semester);
                        semester8List.add(rangkuman8);
                        break;
                }

            }

            final ListAdapter semester1adapter = new SimpleAdapter(
                    getActivity(), semester1List, R.layout.rnilai_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_JENIS,TAG_SKS,TAG_NILAI,TAG_MUTU,TAG_SEMESTER},
                    new int[]{R.id.kd_matkul, R.id.nm_matkul, R.id.jenis ,R.id.sks, R.id.nilai, R.id.mutu, R.id.semester}
            );

            final ListAdapter semester2adapter = new SimpleAdapter(
                    getActivity(), semester2List, R.layout.rnilai_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_JENIS,TAG_SKS,TAG_NILAI,TAG_MUTU,TAG_SEMESTER},
                    new int[]{R.id.kd_matkul, R.id.nm_matkul, R.id.jenis ,R.id.sks, R.id.nilai, R.id.mutu, R.id.semester}
            );

            final ListAdapter semester3adapter = new SimpleAdapter(
                    getActivity(), semester3List, R.layout.rnilai_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_JENIS,TAG_SKS,TAG_NILAI,TAG_MUTU,TAG_SEMESTER},
                    new int[]{R.id.kd_matkul, R.id.nm_matkul, R.id.jenis ,R.id.sks, R.id.nilai, R.id.mutu, R.id.semester}
            );

            final ListAdapter semester4adapter = new SimpleAdapter(
                    getActivity(), semester4List, R.layout.rnilai_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_JENIS,TAG_SKS,TAG_NILAI,TAG_MUTU,TAG_SEMESTER},
                    new int[]{R.id.kd_matkul, R.id.nm_matkul, R.id.jenis ,R.id.sks, R.id.nilai, R.id.mutu, R.id.semester}
            );

            final ListAdapter semester5adapter = new SimpleAdapter(
                    getActivity(), semester5List, R.layout.rnilai_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_JENIS,TAG_SKS,TAG_NILAI,TAG_MUTU,TAG_SEMESTER},
                    new int[]{R.id.kd_matkul, R.id.nm_matkul, R.id.jenis ,R.id.sks, R.id.nilai, R.id.mutu, R.id.semester}
            );

            final ListAdapter semester6adapter = new SimpleAdapter(
                    getActivity(), semester6List, R.layout.rnilai_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_JENIS,TAG_SKS,TAG_NILAI,TAG_MUTU,TAG_SEMESTER},
                    new int[]{R.id.kd_matkul, R.id.nm_matkul, R.id.jenis ,R.id.sks, R.id.nilai, R.id.mutu, R.id.semester}
            );

            final ListAdapter semester7adapter = new SimpleAdapter(
                    getActivity(), semester7List, R.layout.rnilai_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_JENIS,TAG_SKS,TAG_NILAI,TAG_MUTU,TAG_SEMESTER},
                    new int[]{R.id.kd_matkul, R.id.nm_matkul, R.id.jenis ,R.id.sks, R.id.nilai, R.id.mutu, R.id.semester}
            );

            final ListAdapter semester8adapter = new SimpleAdapter(
                    getActivity(), semester8List, R.layout.rnilai_list,
                    new String[]{TAG_KDMK,TAG_NMMK,TAG_JENIS,TAG_SKS,TAG_NILAI,TAG_MUTU,TAG_SEMESTER},
                    new int[]{R.id.kd_matkul, R.id.nm_matkul, R.id.jenis ,R.id.sks, R.id.nilai, R.id.mutu, R.id.semester}
            );
            listsemester1.setAdapter(semester1adapter);
            listsemester2.setAdapter(semester2adapter);
            listsemester3.setAdapter(semester3adapter);
            listsemester4.setAdapter(semester4adapter);
            listsemester5.setAdapter(semester5adapter);
            listsemester6.setAdapter(semester6adapter);
            listsemester7.setAdapter(semester7adapter);
            listsemester8.setAdapter(semester8adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}