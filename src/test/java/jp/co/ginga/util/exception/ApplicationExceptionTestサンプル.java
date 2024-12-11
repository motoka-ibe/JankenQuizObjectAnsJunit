package jp.co.ginga.util.exception;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

/**
 * ApplicationExceptionクラス
 *
 */
public class ApplicationExceptionTestサンプル {

	//	/**
	//	 * システムメッセージ
	//	 */
	//	private String sysMsg;
	//
	//	/**
	//	 * コンストラクタ
	//	 * @param sysMsg
	//	 */
	//	public ApplicationExceptionTest(String sysMsg) {
	//		this.sysMsg = sysMsg;
	//
	//	}
	//
	//	/**
	//	 * @return sysMsg
	//	 */
	//	public String getSysMsg() {
	//		return sysMsg;
	//	}

	/**
	 * ApplicationExceptionTest(String sysMsg)のテスト
	 */
	@Test
	public void applicationExceptionTest() {

		// 事前準備
		String value = "test1";

		//テスト
		ApplicationException ae = new ApplicationException(value);

		//検証
		assertEquals(value, ae.getSysMsg());

	}

	/**
	 * getSysMsg()のテスト
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void getSysMsgTest()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		// 事前準備
		String value = "test2";

		//プライベートなフィールドに値をセットする場合
		ApplicationException ae = new ApplicationException(value);
		Field nameField = ae.getClass().getDeclaredField("sysMsg");
		nameField.setAccessible(true);
		nameField.set(ae, value);

		//テスト
		//検証
		assertEquals(value, ae.getSysMsg());

	}

}
