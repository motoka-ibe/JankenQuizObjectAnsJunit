package jp.co.ginga.main;

import jp.co.ginga.application.CuiGameApplication;
import jp.co.ginga.application.janken.JankenCuiGameApplicationImpl;
import jp.co.ginga.application.quiz.QuizCuiGameApplicationImpl;
import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.keyboard.Keyboard;
import jp.co.ginga.util.properties.MessageProperties;

/**
 * CUIゲーム クラス
 * @author yoshi
 *
 */
public class CuiGame {

	final static int QUIZGAME = 1;
	final static int JANKENGAME = 2;

	/**
	 * main処理
	 * @param args
	 * @throws SystemException 
	 */
	public static void main(String[] args) throws SystemException {

		try {
			CuiGameApplication cga = null;

			while (true) {
				System.out.println(MessageProperties.getMessage("msg.start"));
				try {
					switch (Keyboard.getInt(QUIZGAME, JANKENGAME)) {
					case 1:
						//クイズゲームクラスのインスタンス生成
						cga = new QuizCuiGameApplicationImpl();
						break;

					case 2:
						//じゃんけんゲームクラスのインスタンス生成
						cga = new JankenCuiGameApplicationImpl();
						break;

					default:
						System.out.println(MessageProperties.getMessage("error.keyboard"));
						return;
					}
					break;
				} catch (ApplicationException e) {
					System.out.println(MessageProperties.getMessage("msg.retype"));
				}
			}
			// 選択したゲームの開始
			cga.action();

		} catch (SystemException e) { //actionメソッドにてSystemExceptionが発生した場合
			System.out.println(MessageProperties.getMessage("error.stop"));
		} catch (Exception e) { //actionメソッドにてApplicationExceptionが発生した場合
			System.out.println(MessageProperties.getMessage("error.stop"));

		}
	}
}
