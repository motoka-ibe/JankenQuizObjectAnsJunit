package jp.co.ginga.util.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class ApplicationExceptionTest {

	//テストデータ
	private String message = "エラーが発生しました";

	/**
	 * testConstructor001
	 * public ApplicationException(String sysMsg)
	 * --確認事項--
	 * インスタンス生成時に引数として渡したデータがsysMsgフィールドに代入されているか
	 * --条件--
	 * 引数は文字列
	 * --検証項目--
	 * 1. sysMsgフィールドに引数が代入されている
	 */
	@Test
	public void testConstructor001() {
		try {
			//準備
			Field sysMsgField = ApplicationException.class.getDeclaredField("sysMsg");
			sysMsgField.setAccessible(true);

			//テストメソッド
			ApplicationException e = new ApplicationException(this.message);

			//検証
			String result = (String) sysMsgField.get(e);
			assertEquals(this.message, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetSysMsg001 正常系
	 * public String getSysMsg()
	 * --確認事項--
	 * sysMsgフィールドの値が返されるか
	 * --条件--
	 * 引数は文字列
	 * --検証項目--
	 * 1. sysMsgフィールドの値が返されているか
	 */
	@Test
	public void testGetSysMsg001() {
		//準備
		ApplicationException e = new ApplicationException(this.message);

		//テストメソッド
		String result = e.getSysMsg();

		//検証
		assertEquals(this.message, result);
	}
}
