package jp.co.ginga.util.keybord;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;


/**
 * キーボードクラス
 *
 */
public class KeybordTestサンプル {

	@Test
	public void getBufferedReaderInstance001() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		// 事前準備
		Keybord k = new Keybord();
		//プライベートなフィールドに値をセットする場合
		Field nameField = k.getClass().getDeclaredField("br");
		nameField.setAccessible(true);
		nameField.set(k,null);

		// テスト
		BufferedReader br = Keybord.getBufferedReaderInstance();

		// 検証
		assertNotNull(br);
//		assertInstanceOf(BufferedReader.class,br); //Junit5では見つからない
	}


	@Test
	public void getBufferedReaderInstance002() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		// 事前準備
		Keybord k = new Keybord();
		BufferedReader testBr = new BufferedReader(new InputStreamReader(System.in));
		//プライベートなフィールドに値をセットする場合
		Field nameField = k.getClass().getDeclaredField("br");
		nameField.setAccessible(true);
		nameField.set(k,testBr);

		// テスト
		BufferedReader resultBr = Keybord.getBufferedReaderInstance();

		// 検証
		assertEquals(testBr,resultBr);
//		assertInstanceOf(BufferedReader.class,resultBr); //Junit5では見つからない
	}

}
