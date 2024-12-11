package jp.co.ginga.util.keyboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.properties.MessageProperties;

/**
 * キーボードクラス
 *
 */
public class Keyboard {

	/**
	 * BufferedReaderオブジェクト
	 */
	static BufferedReader br;

	/**
	 * BufferedReaderインスタンス取得処理
	 * 
	 * @return
	 */
	static BufferedReader getBufferedReaderInstance() {
		if (br == null) {
			br = new BufferedReader(new InputStreamReader(System.in));
		}
		return br;
	}

	/**
	 * String値の取得
	 * 
	 * @return
	 * @throws SystemException
	 */
	public static String getString() throws SystemException {
		try {

			//標準入力処理
			return Keyboard.getBufferedReaderInstance().readLine();
		} catch (IOException e) {
			throw new SystemException(MessageProperties.getMessage("error.Keyboard"));
		}
	}

	/**
	 * String値の取得(文字列長さ範囲指定)
	 * 
	 * @return
	 * @throws SystemException
	 */
	public static String getString(int from, int to) throws SystemException, ApplicationException {
		//引数チェック
		hasCheckRangeValue(from, to);
		//入力文字列取得処理
		String str = Keyboard.getString();
		//範囲チェック
		isRange(str.length(), from, to);
		return str;
	}

	/**
	 * int値の取得
	 * 
	 * @return
	 * @throws SystemException
	 */
	public static int getInt() throws ApplicationException, SystemException {
		try {
			//入力文字列を数値型への変換処理
			return Integer.parseInt(Keyboard.getString());
		} catch (NumberFormatException e) {
			throw new ApplicationException(MessageProperties.getMessage("error.non.number"));
		}
	}

	/**
	 * int値の取得（範囲指定）
	 * 
	 * @return
	 * @throws SystemException
	 * @throws ApplicationException
	 */
	public static int getInt(int from, int to) throws ApplicationException, SystemException {
		//引数チェック
		hasCheckRangeValue(from, to);
		//入力数値取得処理
		int no = getInt();
		//範囲チェック
		isRange(no, from, to);
		return no;
	}

	/**
	 * 範囲チェック
	 * 
	 * @param value
	 * @param from
	 * @param to
	 * @throws ApplicationException
	 * @throws SystemException 
	 */
	static void isRange(int value, int from, int to) throws ApplicationException, SystemException {
		if (value < from || value > to) {
			throw new ApplicationException(MessageProperties.getMessage("error.outside.range"));
		}
	}

	/**
	 * 引数チェック
	 * 
	 * @param from
	 * @param to
	 * @throws ApplicationException
	 * @throws SystemException 
	 */
	static void hasCheckRangeValue(int from, int to) throws ApplicationException, SystemException {
		if (from >= to) {
			throw new ApplicationException(MessageProperties.getMessage("error.arg"));
		}
	}
}
