package com.tokopedia.events.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.tokopedia.events.R;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAddTickets#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddTickets extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "typecount";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mTicketTypeCount;
    private int mParam2;
    private int minTickets = 0;
    private int maxTickets = 5;
    int currUnits = 0;

    private ArrayList<ImageButton> addButtonList;
    private ArrayList<ImageButton> minusButtonList;

    private OnFragmentInteractionListener mListener;

    public FragmentAddTickets() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param typecount Number of Ticket Types.
     * @param param2    Parameter 2.
     * @return A new instance of fragment FragmentAddTickets.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddTickets newInstance(int typecount, int param2) {
        FragmentAddTickets fragment = new FragmentAddTickets();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, typecount);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTicketTypeCount = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
        addButtonList = new ArrayList<>(mTicketTypeCount);
        minusButtonList = new ArrayList<>(mTicketTypeCount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        NestedScrollView scrollView = (NestedScrollView) inflater.inflate(R.layout.fragment_add_tickets, container, false);
        //scrollView.setNestedScrollingEnabled(false);
        ViewGroup rootView = scrollView.findViewById(R.id.add_ticket_layout);
        ButtonListener listener = new ButtonListener();

        for (int i = 0; i < mTicketTypeCount; i++) {
            View view = inflater.inflate(R.layout.add_tickets_layout, null);
            ImageButton add = view.findViewById(R.id.btn_increment);
            ImageButton minus = view.findViewById(R.id.btn_decrement);
            TextView count = view.findViewById(R.id.tv_ticket_cnt);
            add.setOnClickListener(listener);
            add.setTag(count);
            minus.setOnClickListener(listener);
            minus.setTag(count);
            addButtonList.add(add);
            minusButtonList.add(minus);
            rootView.addView(view);
        }
        scrollView.setFillViewport(false);
        return scrollView;
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
        void onFragmentInteraction(int price, int units);
    }

    class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.btn_increment){
                currUnits++;
                TextView tv = (TextView) view.getTag();
                tv.setText(String.valueOf(currUnits));
                mListener.onFragmentInteraction(170000,currUnits);
            } else if(view.getId()==R.id.btn_decrement){
                if(currUnits>0){
                    currUnits--;
                    TextView tv = (TextView) view.getTag();
                    tv.setText(String.valueOf(currUnits));
                    mListener.onFragmentInteraction(170000,currUnits);

                }

            }
        }
    }
}
