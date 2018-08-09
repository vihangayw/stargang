package com.paidtocode.stargang.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.request.helper.impl.QuestionsRequestHelperImpl;
import com.paidtocode.stargang.api.response.Ancestor;
import com.paidtocode.stargang.api.response.Error;
import com.paidtocode.stargang.api.response.QuestionListResponse;
import com.paidtocode.stargang.modal.Answer;
import com.paidtocode.stargang.modal.AnswerList;
import com.paidtocode.stargang.modal.Question;
import com.paidtocode.stargang.ui.adapter.CardPagerAdapter;
import com.paidtocode.stargang.util.ShadowTransformer;
import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link QuestionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionsFragment extends Fragment implements CardPagerAdapter.ComponentClickListener {
	private ViewPager mViewPager;
	private View btnSave;
	private CardPagerAdapter mCardAdapter;
	private PageIndicatorView pageIndicatorView;
	private TextView textView;

	public QuestionsFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment QuestionsFragment.
	 */
	public static QuestionsFragment newInstance() {
//		QuestionsFragment fragment = new QuestionsFragment();
//		Bundle args = new Bundle();
//		fragment.setArguments(args);
		return new QuestionsFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_end, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initializeViews(view);
		setListeners();
		loadData();
	}

	private void loadData() {
		new QuestionsRequestHelperImpl().questionsList(new APIHelper.PostManResponseListener() {
			@Override
			public void onResponse(Ancestor ancestor) {
				if (ancestor.getStatus()) {
					if (ancestor instanceof QuestionListResponse) {
						List<Question> data = ((QuestionListResponse) ancestor).getData();
						if (data != null) {
							mCardAdapter = new CardPagerAdapter(QuestionsFragment.this, getContext());
							for (Question question : data) {
								mCardAdapter.addCardItem(question);
							}
							ShadowTransformer mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
							mCardShadowTransformer.enableScaling(true);

							mViewPager.setAdapter(mCardAdapter);
							mViewPager.setPageTransformer(false, mCardShadowTransformer);
							mViewPager.setOffscreenPageLimit(data.size());
						} else {
							mViewPager.setVisibility(View.GONE);
							pageIndicatorView.setVisibility(View.GONE);
							btnSave.setVisibility(View.GONE);
							if (getActivity() != null) {
								if (!TextUtils.isEmpty(ancestor.getMessage())) {
									textView.setText(ancestor.getMessage());
									textView.setVisibility(View.VISIBLE);
									Toast.makeText(getActivity(),
											ancestor.getMessage(), Toast.LENGTH_SHORT).show();
								}
							}
						}
					}
				}
			}

			@Override
			public void onError(Error error) {
				if (getActivity() != null) {
					if (!TextUtils.isEmpty(error.getMessage())) {
						Toast.makeText(getActivity(),
								error.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	private void setListeners() {
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCardAdapter != null && mCardAdapter.getmData() != null) {
					List<Question> questions = mCardAdapter.getmData();
					AnswerList answerList = new AnswerList(new ArrayList<Answer>());
					for (Question question : questions) {
						if (TextUtils.isEmpty(question.getSelectedAns())) {
							if (getActivity() != null)
								Toast.makeText(getActivity(), "Answer all questions to submit",
										Toast.LENGTH_SHORT).show();
							return;
						}
						answerList.getQa().add(new Answer(question.getqID(), question.getSelectedAns()));
					}
					if (answerList.getQa().size() == questions.size()) {
						sendAnswers(answerList);
					} else {
						if (getActivity() != null)
							Toast.makeText(getActivity(), "Something went wrong",
									Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	private void sendAnswers(AnswerList answerList) {
		new QuestionsRequestHelperImpl().answerQs(answerList, new APIHelper.PostManResponseListener() {
			@Override
			public void onResponse(Ancestor ancestor) {
				if (ancestor.getStatus()) {
					if (ancestor.getMessage() != null) {
						if (getActivity() != null)
							Toast.makeText(getActivity(),
									"" + ancestor.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onError(Error error) {
				if (getActivity() != null) {
					if (!TextUtils.isEmpty(error.getMessage())) {
						Toast.makeText(getActivity(),
								error.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	private void initializeViews(View view) {
		mViewPager = view.findViewById(R.id.viewPager);
		btnSave = view.findViewById(R.id.btn_save);
		textView = view.findViewById(R.id.lbl_info);
		pageIndicatorView = view.findViewById(R.id.pageIndicatorView);
		pageIndicatorView.setAnimationType(AnimationType.THIN_WORM);
		pageIndicatorView.setViewPager(mViewPager);
	}

	@Override
	public void onItemClick(Question cardItem) {

	}
}
