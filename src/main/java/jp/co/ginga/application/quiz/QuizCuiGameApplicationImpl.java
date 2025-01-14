package jp.co.ginga.application.quiz;

import java.util.List;

import jp.co.ginga.application.CuiGameApplication;
import jp.co.ginga.application.quiz.factory.QuizQuestionFactory;
import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.keyboard.Keyboard;
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
	int correctCount;

	/**
	 * QuizQuestionFactoryのインスタンス生成
	 */
	QuizQuestionFactory factory = new QuizQuestionFactory();

	/**
	 * クイズゲーム開始処理
	 */
	@Override
	public void action() throws SystemException {

		System.out.println(MessageProperties.getMessage("quiz.msg.start"));

		// クイズリスト作成
		
		list = factory.createQuizQuestion();
		if (list == null) {
			throw new SystemException(MessageProperties.getMessage("error.stop"));
		}
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
		System.out.println(quiz.getTitle());
		System.out.println(quiz.getBody());
		System.out.println(quiz.getChoice());

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

				if (quiz.getCorrect() == Keyboard.getInt(1, 3)) {
					this.correctCount++;
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

		System.out.println(MessageProperties.getMessage("quiz.msg.correct" + this.correctCount));

	}

}
