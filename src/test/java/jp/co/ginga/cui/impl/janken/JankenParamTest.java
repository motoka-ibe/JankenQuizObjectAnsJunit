package jp.co.ginga.cui.impl.janken;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

/**
 * JankenParam ENUM
 * @author yoshi
 *
 */
public class JankenParamTest {

	//テストデータ
		private int rock = 1;
		private int scissors = 2;
		private int paper = 3;
		private int draw = 0;
		private int illegalValue = -1;


	/**
	 *testConstructor001 正常系
	 * private JankenParam(int enumValue)
	 * --確認事項--
	 * 列挙型から取り出した定数は指定された値であるか
	 * --条件--
	 *	なし
	 * --検証項目--
	 * 1. ROCKの定数は1であるか
	 */
	@Test
	public void testConstructor001() {
		try {
			//準備
			Field enumValueField = JankenParam.class.getDeclaredField("enumValue");
			enumValueField.setAccessible(true);
			
			//テストメソッド
			JankenParam param = JankenParam.ROCK;

			//検証
			int result = (int) enumValueField.get(param);
			assertEquals(this.rock, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 *testConstructor002 正常系
	 * private JankenParam(int enumValue)
	 * --確認事項--
	 * 列挙型から取り出した定数は指定された値であるか
	 * --条件--
	 *	なし
	 * --検証項目--
	 * 1. SCISSORSの定数は2であるか
	 */
	@Test
	public void testConstructor002() {
		try {
			//準備
			Field enumValueField = JankenParam.class.getDeclaredField("enumValue");
			enumValueField.setAccessible(true);
			
			//テストメソッド
			JankenParam param = JankenParam.SCISSORS;

			//検証
			int result = (int) enumValueField.get(param);
			assertEquals(this.scissors, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 *testConstructor003 正常系
	 * private JankenParam(int enumValue)
	 * --確認事項--
	 * 列挙型から取り出した定数は指定された値であるか
	 * --条件--
	 *	なし
	 * --検証項目--
	 * 1. PAPERの定数は3であるか
	 */
	@Test
	public void testConstructor003() {
		try {
			//準備
			Field enumValueField = JankenParam.class.getDeclaredField("enumValue");
			enumValueField.setAccessible(true);
			
			//テストメソッド
			JankenParam param = JankenParam.PAPER;

			//検証
			int result = (int) enumValueField.get(param);
			assertEquals(this.paper, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 *testConstructor004 正常系
	 * private JankenParam(int enumValue)
	 * --確認事項--
	 * 列挙型から取り出した定数は指定された値であるか
	 * --条件--
	 *	なし
	 * --検証項目--
	 * 1. DRAWの定数は0であるか
	 */
	@Test
	public void testConstructor004() {
		try {
			//準備
			Field enumValueField = JankenParam.class.getDeclaredField("enumValue");
			enumValueField.setAccessible(true);
			
			//テストメソッド
			JankenParam param = JankenParam.DRAW;

			//検証
			int result = (int) enumValueField.get(param);
			assertEquals(this.draw, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetInt001 正常系
	 * public int getInt()
	 * --確認事項--
	 * 列挙型がもつenumValueフィールドの値が返されているか
	 * --条件--
	 *	なし
	 * --検証項目--
	 * 1. enumのが持つenumValueは指定した値であるか
	 */
	@Test
	public void testGetInt001() {
		//準備
		JankenParam param = JankenParam.ROCK;
		
		//テストメソッド
		int result = param.getInt();
		
		//検証
		assertEquals(this.rock, result);
	}

	/**
	 * testGetEnum001 正常系
	 * public static JankenParam getEnum(final int value)
	 * --確認事項--
	 * 引数と同じ値の列挙型が返されるか
	 * --条件--
	 *	引数に整数を与える
	 * --検証項目--
	 * 1. 引数と同じ値の列挙型が返されるか
	 * 2. 列挙型が持つ定数と引数が同じか
	 */
	@Test
	public void testGetEnum001() {
		//テストメソッド
		JankenParam result = JankenParam.getEnum(this.rock);
		
		//検証
		assertEquals(JankenParam.ROCK, result);
		assertEquals(this.rock, result.getInt());
	}


	/**
	 * testGetEnum002 正常系
	 * public static JankenParam getEnum(final int value)
	 * --確認事項--
	 * 引数と同じ値の列挙型がない場合はnullを返す
	 * --条件--
	 *	引数に整数を与える
	 * --検証項目--
	 * 1. 戻り値がnullであるか
	 */
	@Test
	public void testGetEnum002() {
		//テストメソッド
		JankenParam result = JankenParam.getEnum(this.illegalValue);
		
		//検証
		assertNull(result);
	}

}