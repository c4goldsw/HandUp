package com.handup.handup.controller.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handup.handup.R;
import com.handup.handup.helper.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    private ListView menuList;

    private OnFragmentInteractionListener mListener;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MenuFragment.
     */
    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*===========================================================================================
    View (UI) methods
    ===========================================================================================*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ui = inflater.inflate(R.layout.fragment_menu, container, false);

        //set up the nav bar and the list view inside of  it
        String[] options = {"Logout", "Settings", "Help", "About"};

        Drawable[] menuIcons = {getActivity().getResources().getDrawable(R.drawable.ic_logout_24dp),
        getActivity().getResources().getDrawable(R.drawable.ic_settings_24dp),
        getActivity().getResources().getDrawable(R.drawable.ic_help_24dp),
        getActivity().getResources().getDrawable(R.drawable.ic_stars_24dp),
        };

        menuList = (ListView) ui.findViewById(R.id.menu_list);

        menuList.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.menu_item
             , R.id.menu_list_text, options));

        menuList.setOnItemClickListener(new MenuListClickListener());

        return ui;
    }

    /*===========================================================================================
    Controller methods
    ===========================================================================================*/

    /**
     * Listener object for items in the drawer listview
     */
    public class MenuListClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            selectItem(position);
        }
    }

    /**
     *
     * @param pos position of the item in the list we selected
     */
    private void selectItem(int pos){

        if(pos == Constants.MENUFRAGMENT_LOGOUT){
            mListener.menuFragmentInteraction(Constants.MENUFRAGMENT_LOGOUT);
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void menuFragmentInteraction(int option);
    }
}
