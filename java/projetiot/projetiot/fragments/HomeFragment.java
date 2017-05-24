package projetiot.projetiot.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ubidots.ApiClient;
import com.ubidots.Variable;

import projetiot.projetiot.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String t,h,g,u;
    private TextView temperature,humidite,gaz,ultrason;
    private ProgressBar pbTemperature,pbHumidite,pBGaz,pbUltrason;
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        temperature = (TextView) v.findViewById(R.id.txt_Temperature);
        gaz  = (TextView) v.findViewById(R.id.txt_Gaz);
        humidite = (TextView) v.findViewById(R.id.txt_Humidite);
        ultrason = (TextView) v.findViewById(R.id.txtUltraSon);
        pbTemperature = (ProgressBar) v.findViewById(R.id.progressBarTemp);
        pBGaz = (ProgressBar) v.findViewById(R.id.progressBarGaz);
        pbHumidite = (ProgressBar) v.findViewById(R.id.progressBarHumidite);
        pbUltrason = (ProgressBar) v.findViewById(R.id.progressBarSon);
        new HomeFragment.ApiUbidots().execute();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

           // mBatteryLevel.setText(Integer.toString(level));
            new HomeFragment.ApiUbidots().execute();
        }
    };
    public class ApiUbidots extends AsyncTask<Void, Void, Void> {
        private final String API_KEY = "cfb80c2298654c7bb47445eda6475284d5aeea11";
        private final String TEMP_ID = "591ecb377625425f0a701954";
        private final String GAZ_ID = "aintegrer";
        private final String HUMIDITE_ID = "591ecc097625425f09a56831";
       // private final String ULTRA_SON_ID = "591ecc097625425f09a56831";

        @Override
        protected Void doInBackground(Void... params) {
            ApiClient apiClient = new ApiClient(API_KEY);
            Variable temperature = apiClient.getVariable(TEMP_ID);
           t = String.valueOf((int)temperature.getValues()[0].getValue());
          //  Variable gaz = apiClient.getVariable(GAZ_ID);
            Variable humidite = apiClient.getVariable(HUMIDITE_ID);
            h = String.valueOf((int)humidite.getValues()[0].getValue());
           // Variable ultrason = apiClient.getVariable(ULTRA_SON_ID);

           // batteryLevel.saveValue(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            temperature.setText(t);
            humidite.setText(h);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_DEFAULT));
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mBatteryReceiver);
        super.onStop();

    }
}
