package activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.ozi.mystudentapp.R;

/**
 * Created by Ozi on 9/13/2016.
 */
public class NewsFragment extends Fragment {

    String alamatweb;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        ((MainActivity)getActivity()).setTitle("Berita");
        Bundle bundle = this.getArguments();
        alamatweb = bundle.getString("alamatweb");

        WebView Berita = (WebView) rootView.findViewById(R.id.webBerita);
        Berita.loadUrl(alamatweb);

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
}
