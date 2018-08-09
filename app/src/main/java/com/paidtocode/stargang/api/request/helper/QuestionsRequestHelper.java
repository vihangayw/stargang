package com.paidtocode.stargang.api.request.helper;


import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.modal.AnswerList;

/**
 * Created by Vihanga on 3/8/2018.
 */

public interface QuestionsRequestHelper {

	void questionsList(APIHelper.PostManResponseListener listener);

	void answerQs(AnswerList answerList, APIHelper.PostManResponseListener listener);

}