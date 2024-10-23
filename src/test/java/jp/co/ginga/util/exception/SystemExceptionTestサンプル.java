package jp.co.ginga.util.exception;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

/**
 * SystemExceptionクラス
 *
 */
public class SystemExceptionTestサンプル{

	/**
	 * SystemExceptionTest(String sysMsg)のテスト
	 */
	@Test
	public void applicationExceptionTest() {

		// 事前準備
		String value = "test1";

		//テスト
		SystemException se = new SystemException(value);

		//検証
		assertEquals(value,se.getSysMsg());

	}

	/**
	 * getSysMsg()のテスト
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@Test
	public void getSysMsgTest() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

		// 事前準備
		String value = "test2";

		//プライベートなフィールドに値をセットする場合
		SystemException se = new SystemException(value);
		Field nameField = se.getClass().getDeclaredField("sysMsg");
		nameField.setAccessible(true);
		nameField.set(se,value);

		//テスト
		//検証
		assertEquals(value,se.getSysMsg());


	}
}
