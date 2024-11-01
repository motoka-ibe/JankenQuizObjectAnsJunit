package jp.co.ginga.application.quiz;

import java.util.List;

import jp.co.ginga.application.CuiGameApplication;
import jp.co.ginga.application.quiz.factory.QuizQuestionFactory;
import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.keybord.Keybord;
import jp.co.ginga.util.properties.MessageProperties;

/**
 * CUIクイズゲーム実装クラス
 * 
 * @author yoshi
 *
 */
public class QuizCuiGameApplicationImpl implements CuiGameApplication {

	/**
	 * クイズ問題リスト
	 */
	List<QuizQuestion> list;

	/**
	 * 正解数
	 */
	int correctConut;

	/**
	 * クイズゲーム開始処理
	 */
	@Override
	public void action() throws SystemException {

		System.out.println(MessageProperties.getMessage("quiz.msg.start"));

		// クイズリスト作成
		QuizQuestionFactory factory = new QuizQuestionFactory();
		list = factory.createQuizQuestion();

		for (QuizQuestion quiz : list) {

			// クイズ情報表示
			this.viewProblem(quiz);

			// クイズ判定処理
			this.judge(quiz);

		}

		// クイズゲーム結果表示
		this.viewResult();

		System.out.println(MessageProperties.getMessage("quiz.msg.end"));
	}


	/**
	 * クイズ問題表示処理
	 * 
	 * @param quis
	 * @throws SystemException 
	 */
	void viewProblem(final QuizQuestion quiz) throws SystemException {
		//quizがnullか
		if (quiz == null) {
			throw new SystemException(MessageProperties.getMessage("error.stop"));
		}
		System.out.println(quiz.getProblemTitle());
		System.out.println(quiz.getProblemBody());
		System.out.println(quiz.getProblemChoice());
		

	}

	/**
	 * クイズ判定処理
	 * 
	 * @param quiz
	 * @throws SystemException
	 * 
	 */
	void judge(final QuizQuestion quiz) throws SystemException {
		//quizがnullか
		if (quiz == null) {
			throw new SystemException(MessageProperties.getMessage("error.stop"));
		}
		while (true) {

			try {
				System.out.println(MessageProperties.getMessage("quiz.msg.input"));

				if (quiz.getCorrect() == Keybord.getInt(1, 3)) {
					this.correctConut++;
				}

				break;
			} catch (ApplicationException e) {
				System.out.println(MessageProperties.getMessage("error.outside.range"));
			}

		}
	}

	/**
	 * クイズゲーム結果表示処理
	 * 
	 * @throws SystemException
	 * 
	 */
	void viewResult() throws SystemException {

		System.out.println(MessageProperties.getMessage("quiz.msg.correct" + this.correctConut));

	}

}
