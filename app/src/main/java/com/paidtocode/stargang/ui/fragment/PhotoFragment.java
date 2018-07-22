package com.paidtocode.stargang.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.ui.adapter.PhotoAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment implements PhotoAdapter.OnComponentClickListener {

	private TextView txtInfo;
	private FloatingActionButton fab;
	private PhotoAdapter adapter;

	public PhotoFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment EditUserFragment.
	 */
	public static PhotoFragment newInstance() {
		PhotoFragment fragment = new PhotoFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_photos_user, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
	}

	private void initViews(View view) {
		fab = view.findViewById(R.id.add_images);
		txtInfo = view.findViewById(R.id.txt_info);
		txtInfo.setVisibility(View.GONE);

		RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		adapter = new PhotoAdapter(null, this, getContext());
		recyclerView.setAdapter(adapter);
	}


	@Override
	public void onComponentClick(View itemView, int position) {

	}
}
