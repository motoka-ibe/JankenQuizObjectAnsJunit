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
	private String problemTitle;

	/**
	 * 問題 本文
	 */
	private String problemBody;

	/**
	 * 問題 選択分
	 */
	private String problemChoice;

	/**
	 * 正解
	 */
	private int correct;

	/**
	 * コンストラクタ
	 * @param problemTitle
	 * @param problemBody
	 * @param problemChoice
	 * @param correct
	 */
	public QuizQuestion(String problemTitle, String problemBody, String problemChoice, int correct) {
		this.problemTitle = problemTitle;
		this.problemBody = problemBody;
		this.problemChoice = problemChoice;
		this.correct = correct;
	}

	/**
	 * @return problemTitle
	 */
	public String getProblemTitle() {
		return problemTitle;
	}

	/**
	 * @param problemTitle セットする problemTitle
	 */
	public void setProblemTitle(String problemTitle) {
		this.problemTitle = problemTitle;
	}

	/**
	 * @return problemBody
	 */
	public String getProblemBody() {
		return problemBody;
	}

	/**
	 * @param problemBody セットする problemBody
	 */
	public void setProblemBody(String problemBody) {
		this.problemBody = problemBody;
	}

	/**
	 * @return problemChoice
	 */
	public String getProblemChoice() {
		return problemChoice;
	}

	/**
	 * @param problemChoice セットする problemChoice
	 */
	public void setProblemChoice(String problemChoice) {
		this.problemChoice = problemChoice;
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
