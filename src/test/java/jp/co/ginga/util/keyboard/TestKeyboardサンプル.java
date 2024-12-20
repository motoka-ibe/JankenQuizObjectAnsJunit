package jp.co.ginga.util.keyboard;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import jp.co.ginga.util.keyboard.Keyboard;

/**
 * キーボードクラス
 *
 */
public class TestKeyboardサンプル {

	@Test
	public void getBufferedReaderInstance001()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		// 事前準備
		Keyboard k = new Keyboard();
		//プライベートなフィールドに値をセットする場合
		Field nameField = k.getClass().getDeclaredField("br");
		nameField.setAccessible(true);
		nameField.set(k, null);

		// テスト
		BufferedReader br = Keyboard.getBufferedReaderInstance();

		// 検証
		assertNotNull(br);
		//		assertInstanceOf(BufferedReader.class,br); //Junit5では見つからない
	}

	@Test
	public void getBufferedReaderInstance002()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		// 事前準備
		Keyboard k = new Keyboard();
		BufferedReader testBr = new BufferedReader(new InputStreamReader(System.in));
		//プライベートなフィールドに値をセットする場合
		Field nameField = k.getClass().getDeclaredField("br");
		nameField.setAccessible(true);
		nameField.set(k, testBr);

		// テスト
		BufferedReader resultBr = Keyboard.getBufferedReaderInstance();

		// 検証
		assertEquals(testBr, resultBr);
		//		assertInstanceOf(BufferedReader.class,resultBr); //Junit5では見つからない
	}

}
