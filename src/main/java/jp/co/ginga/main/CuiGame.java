package jp.co.ginga.main;

import jp.co.ginga.application.CuiGameApplication;
import jp.co.ginga.application.janken.JankenCuiGameApplicationImpl;
import jp.co.ginga.application.quiz.QuizCuiGameApplicationImpl;
import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.keybord.Keybord;
import jp.co.ginga.util.properties.MessageProperties;

/**
 * CUIゲーム クラス
 * @author yoshi
 *
 */
public class CuiGame {
	
	/**
	 * main処理
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			CuiGameApplication cga = null;

			while (true) {
				System.out.println(MessageProperties.getMessage("msg.start"));
				try {
					switch (Keybord.getInt(1, 2)) {
					case 1:
						//クイズゲームクラスのインスタん生成
						cga = new QuizCuiGameApplicationImpl();
						break;

					case 2:
						//じゃんけんゲームクラスのインスタん生成
						cga = new JankenCuiGameApplicationImpl();
						break;

					default:
						System.out.println(MessageProperties.getMessage("error.keybord"));
						return;
					}

					break;
				} catch (ApplicationException e) {
					System.out.println(MessageProperties.getMessage("msg.retype"));
				}

			}

			// 選択したゲームの開始
			cga.action();

		} catch (SystemException e) {
			//システムメッセージの出力処理
			System.out.println(e.getSysMsg());
		} catch (Exception e) {
			e.printStackTrace();
			//ログファイル書き出す？？
			
		}
	}
}
