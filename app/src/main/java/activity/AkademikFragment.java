package activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import adapter.Config;

/**
 * Created by Ozi on 9/13/2016.
 */
public class AkademikFragment extends Fragment {

    TextView textNPM,textNama,textIPKLokal,textIPKUtama,textIPKTotal,textSKS;
    String username;
    String MHSJSON;
    String NILAIJSON;
    JSONArray mahasiswa = null;
    JSONArray nilai = null;
    private AppCompatButton buttonDetail;

    private static final String TAG_RESULTSMHS="result";
    private static final String TAG_NPM = "npm";
    private static final String TAG_NAMA = "nama";
    private static final String TAG_IPKLOKAL = "ipk_lokal";
    private static final String TAG_IPKUTAMA = "ipk_utama";
    private static final String TAG_IPKTOTAL = "ipk_total";
    private static final String TAG_SKS = "sks";

    public AkademikFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_akademik, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        textNPM = (TextView) rootView.findViewById(R.id.txtNPM);
        textNama = (TextView) rootView.findViewById(R.id.txtNama);
        textIPKLokal = (TextView) rootView.findViewById(R.id.txtIPKLokal);
        textIPKUtama = (TextView) rootView.findViewById(R.id.txtIPKUtama);
        textIPKTotal = (TextView) rootView.findViewById(R.id.txtIPKTotal);
        textSKS = (TextView) rootView.findViewById(R.id.txtSKS);
        getDataMahasiswa();

        buttonDetail = (AppCompatButton) rootView.findViewById(R.id.buttondetail);
        buttonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new RnilaiFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container_body,fragment);
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                fragment.setArguments(bundle);
                ft.commit();
            }
        });


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

    public void getDataMahasiswa(){

        class GetDataJsonMHS extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://mystudentapp.000webhostapp.com/akademik.php");


                InputStream inputStream = null;
                String result = null;
                try{
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("username",username));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


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
                MHSJSON=result;
                getDataNilai();

            }
        }
        GetDataJsonMHS g = new GetDataJsonMHS();
        g.execute();

    }

    public void getDataNilai(){
        class GetDataJsonMHS extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://mystudentapp.000webhostapp.com/nilaiakademik.php");


                InputStream inputStream = null;
                String result = null;
                try{
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("username",username));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


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
                NILAIJSON=result;
                showListMHS();

            }
        }
        GetDataJsonMHS g = new GetDataJsonMHS();
        g.execute();
    }

    protected void showListMHS () {
        try {
            JSONObject jsonObjc = new JSONObject(MHSJSON);
            mahasiswa = jsonObjc.getJSONArray(TAG_RESULTSMHS);
            JSONObject jsonObjd = new JSONObject(NILAIJSON);
            nilai = jsonObjd.getJSONArray(TAG_RESULTSMHS);



            JSONObject d = mahasiswa.getJSONObject(0);
            String npm = d.getString(TAG_NPM);
            String nama = d.getString(TAG_NAMA);


            JSONObject c = nilai.getJSONObject(0);
            String ipk_lokal = c.getString(TAG_IPKLOKAL);
            String ipk_utama = c.getString(TAG_IPKUTAMA);
            String ipk_total = c.getString(TAG_IPKTOTAL);
            String sks = c.getString(TAG_SKS);


            textNPM.setText(npm);
            textNama.setText(nama);
            textIPKLokal.setText(ipk_lokal);
            textIPKUtama.setText(ipk_utama);
            textIPKTotal.setText(ipk_total);
            textSKS.setText(sks);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}