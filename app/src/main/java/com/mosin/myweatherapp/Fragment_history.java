package com.mosin.myweatherapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class Fragment_history extends Fragment {
    private int indexArr;
    private String cityChoice;
    SharedPreferences sharedPreferences;
    HistoryList list = new HistoryList();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        LinkedList<String> data = list.getHistoryList();
        initRecyclerView(data, view);
    }

    public void initRecyclerView(LinkedList<String> data, View view) {
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        final RecyclerAdapter adapter = new RecyclerAdapter(data);
        recyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            LinkedList<String> cityNamesArr = list.getHistoryList();

            @Override
            public void onItemClick(View view, int position) {
                indexArr = recyclerView.getChildLayoutPosition(view);
                cityChoice = cityNamesArr.get(indexArr).toString();
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cityName", cityChoice);
                editor.apply();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new Fragment_main())
                        .commit();
            }
        });
    }
}
