package jp.co.ginga.application.quiz;

/**
 * クイズ問題クラス
 * @author yoshi
 *
 */
public class QuizQuestion {

	/**
	 * 問題 タイトル
	 */
	private String title;

	/**
	 * 問題 本文
	 */
	private String body;

	/**
	 * 問題 選択分
	 */
	private String choice;

	/**
	 * 正解
	 */
	private int correct;

	/**
	 * コンストラクタ
	 * @param title
	 * @param body
	 * @param choice
	 * @param correct
	 */
	public QuizQuestion(String title, String body, String choice, int correct) {
		this.title = title;
		this.body = body;
		this.choice = choice;
		this.correct = correct;
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title セットする title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body セットする body
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return choice
	 */
	public String getChoice() {
		return choice;
	}

	/**
	 * @param choice セットする choice
	 */
	public void setChoice(String choice) {
		this.choice = choice;
	}

	/**
	 * @return correct
	 */
	public int getCorrect() {
		return correct;
	}

	/**
	 * @param correct セットする correct
	 */
	public void setCorrect(int correct) {
		this.correct = correct;
	}

}
