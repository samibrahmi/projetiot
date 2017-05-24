package projetiot.projetiot.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import projetiot.projetiot.R;
import com.ubidots.ApiClient;
import com.ubidots.Variable;


public class Lampe extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Lampe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Lampe.
     */
    // TODO: Rename and change types and number of parameters
    public static Lampe newInstance(String param1, String param2) {
        Lampe fragment = new Lampe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
   // private static final String BATTERY_LEVEL = "level";
    private Switch etatLampe;
    private ImageView imgLights;
    private ConstraintLayout cstLayout;
    private TextView txtEtatLumiere;
   /* private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BATTERY_LEVEL, 0);
            Log.d("Batterie level",""+level);
            mBatteryLevel.setText(Integer.toString(level));
            new Lampe.ApiUbidots().execute(level);
        }
    };*/
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

        View v= inflater.inflate(R.layout.fragment_lampe, container, false);

        etatLampe = (Switch) v.findViewById(R.id.switch_lampe);
        imgLights = (ImageView) v.findViewById(R.id.imageViewLights);
        cstLayout = (ConstraintLayout) v.findViewById(R.id.constraint_layout_container);
        txtEtatLumiere = (TextView) v.findViewById(R.id.txt_etat_lumiere);
       /* if(!isConnected(getActivity())) buildDialog(getActivity()).show();
        else {
            // we have internet connection, so it is save to connect to the internet here
            new TheTask().execute();
        }*/

        etatLampe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
               // switchStatus.setText("Switch is currently ON");
                Log.d("Cnx","isChecked");
                new ApiUbidots().execute(1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imgLights.setImageDrawable(getResources().getDrawable(R.drawable.lights_on_house, getActivity().getApplicationContext().getTheme()));
                    cstLayout.setBackgroundResource(R.color.color_lights_on_background);
                    txtEtatLumiere.setText("Lumière allumée");
                } else {
                    imgLights.setImageDrawable(getResources().getDrawable(R.drawable.lights_on_house));
                    cstLayout.setBackgroundResource(R.color.color_lights_on_background);
                    txtEtatLumiere.setText("Lumière allumée");
                }
            }else{
               // switchStatus.setText("Switch is currently OFF");
                Log.d("Cnx","isnotChecked");
                new ApiUbidots().execute(0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imgLights.setImageDrawable(getResources().getDrawable(R.drawable.lights_off_house, getActivity().getApplicationContext().getTheme()));
                    cstLayout.setBackgroundResource(R.color.color_lights_off_background);
                    txtEtatLumiere.setText("Lumière éteinte");
                } else {
                    imgLights.setImageDrawable(getResources().getDrawable(R.drawable.lights_off_house));
                    cstLayout.setBackgroundResource(R.color.color_lights_off_background);
                    txtEtatLumiere.setText("Lumière éteinte");
                }
            }

        }
    });
        return v;
    }
    public class ApiUbidots extends AsyncTask<Integer, Void, Void> {
        private final String API_KEY = "cfb80c2298654c7bb47445eda6475284d5aeea11";
        private final String VARIABLE_ID = "5923293f7625427b38e8d345";

        @Override
        protected Void doInBackground(Integer... params) {
            ApiClient apiClient = new ApiClient(API_KEY);
            try {
                Variable batteryLevel = apiClient.getVariable(VARIABLE_ID);

                batteryLevel.saveValue(params[0]);
            }
            catch (Exception e) {
               // Toast.makeText(Lampe.this,"Erreur de connexion au serveur",Toast.LENGTH_LONG).show();
                Log.d("Cnx","Connexion seerveur ko");
            }

            return null;
        }


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
}
