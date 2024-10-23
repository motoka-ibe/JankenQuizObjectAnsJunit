package jp.co.ginga.cui.impl.janken;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.co.ginga.cui.impl.janken.jankenplayer.JankenPlayer;
import jp.co.ginga.cui.impl.janken.jankenplayer.impl.CpuJankenPlayerImpl;
import jp.co.ginga.cui.impl.janken.jankenplayer.impl.HumanJankenPlayerImpl;

/**
 * JankenApplicationImpの単体テスト実施クラス
 * @author yoshi
 *
 */
public class TestJankenApplicationImpl {

	/**
	 * judge()メソッドテスト　正常系
	 * 以下条件で実行し、以下の確認事項を確認する
	 * 確認事項
	 * ・judge（）メソッドの戻り値が1(グー)であること
	 *
	 * 実行条件
	 * 人間プレーヤー数2名
	 * プレーヤー情報
	 * １：
	 * プレーヤーネーム：プレーヤー1
	 * 選択した手：グー
	 * 2：
	 * プレーヤーネーム：プレーヤー2
	 * 選択した手：チョキ
	 *
	 */
	@Test
	public void case001_OK() throws Exception {

		//テストデータ作成
		List<JankenPlayer> testPlayerList = new ArrayList<JankenPlayer>();

		JankenPlayer player1 = new HumanJankenPlayerImpl("プレーヤ1");
		JankenPlayer player2 = new HumanJankenPlayerImpl("プレーヤ2");

		//プライベートなフィールドに値をセットする場合
		Class<? extends JankenPlayer> testData = player1.getClass();
		Field nameField = testData.getDeclaredField("playerHand");
		nameField.setAccessible(true);
		nameField.set(player1, 1);
		testPlayerList.add(player1);

		testData = player2.getClass();
		nameField = testData.getDeclaredField("playerHand");
		nameField.setAccessible(true);
		nameField.set(player2, 2);
		testPlayerList.add(player2);

		//テスト実施クラスのインスタンス生成処理
		JankenCuiGameApplicationImpl testobj = new JankenCuiGameApplicationImpl();

		//テストデータのセット
		testobj.setPlayerList(testPlayerList);

		//プライベートなメソッドを呼び出し戻り値を取得する場合
		Class<JankenCuiGameApplicationImpl> c = JankenCuiGameApplicationImpl.class;
		Method runMethod = c.getDeclaredMethod("judge");
		runMethod.setAccessible(true);
		int judgeResult = (int) runMethod.invoke(testobj);
		assertEquals(1, judgeResult);

	}

	/**
	 * judge()メソッドテスト　正常系
	 * 以下条件で実行し、以下の確認事項を確認する
	 * 確認事項
	 * ・judge（）メソッドの戻り値が2(チョキ)であること
	 *
	 * 実行条件
	 * 人間プレーヤー数2名
	 * プレーヤー情報
	 * １：
	 * プレーヤーネーム：人間プレーヤー1
	 * 選択した手：パー
	 * 2：
	 * プレーヤーネーム：人間プレーヤー2
	 * 選択した手：チョキ
	 *
	 */
	@Test
	public void case002_OK() {

		//テストデータ作成
		List<JankenPlayer> testPlayerList = new ArrayList<JankenPlayer>();

		JankenPlayer player1 = new HumanJankenPlayerImpl("プレーヤ1");
		JankenPlayer player2 = new HumanJankenPlayerImpl("プレーヤ2");

		try {
			//プライベートなフィールドに値をセットする場合
			Class<? extends JankenPlayer> testData = player1.getClass();
			Field nameField = testData.getDeclaredField("playerHand");
			nameField.setAccessible(true);
			nameField.set(player1, 3);
			testPlayerList.add(player1);

			testData = player2.getClass();
			nameField = testData.getDeclaredField("playerHand");
			nameField.setAccessible(true);
			nameField.set(player2, 2);
			testPlayerList.add(player2);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			fail();
		}

		//テスト実施クラスのインスタンス生成処理
		JankenCuiGameApplicationImpl testobj = new JankenCuiGameApplicationImpl();

		//テストデータのセット
		testobj.setPlayerList(testPlayerList);

		try {
			//プライベートなメソッドを呼び出し戻り値を取得する場合
			Class<JankenCuiGameApplicationImpl> c = JankenCuiGameApplicationImpl.class;
			Method runMethod = c.getDeclaredMethod("judge");
			runMethod.setAccessible(true);
			int judgeResult = (int) runMethod.invoke(testobj);
			assertEquals(2, judgeResult);

		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * judge()メソッドテスト　正常系
	 * 以下条件で実行し、以下の確認事項を確認する
	 * 確認事項
	 * ・judge（）メソッドの戻り値が3(パー)であること
	 *
	 * 実行条件
	 * 人間プレーヤー数2名
	 * プレーヤー情報
	 * １：
	 * プレーヤーネーム：人間プレーヤー1
	 * 選択した手：パー
	 * 2：
	 * プレーヤーネーム：人間プレーヤー2
	 * 選択した手：グー
	 *
	 */
	@Test
	public void case003_OK() {

		//テストデータ作成
		List<JankenPlayer> testPlayerList = new ArrayList<JankenPlayer>();

		JankenPlayer player1 = new CpuJankenPlayerImpl("CPUプレーヤ1");
		JankenPlayer player2 = new CpuJankenPlayerImpl("CPUプレーヤ2");

		try {
			//プライベートなフィールドに値をセットする場合
			Class<? extends JankenPlayer> testData = player1.getClass();
			Field nameField = testData.getDeclaredField("playerHand");
			nameField.setAccessible(true);
			nameField.set(player1, 3);
			testPlayerList.add(player1);

			testData = player2.getClass();
			nameField = testData.getDeclaredField("playerHand");
			nameField.setAccessible(true);
			nameField.set(player2, 1);
			testPlayerList.add(player2);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			fail();
		}

		//テスト実施クラスのインスタンス生成処理
		JankenCuiGameApplicationImpl testobj = new JankenCuiGameApplicationImpl();

		//テストデータのセット
		testobj.setPlayerList(testPlayerList);

		try {
			//プライベートなメソッドを呼び出し戻り値を取得する場合
			Class<JankenCuiGameApplicationImpl> c = JankenCuiGameApplicationImpl.class;
			Method runMethod = c.getDeclaredMethod("judge");
			runMethod.setAccessible(true);
			int judgeResult = (int) runMethod.invoke(testobj);
			assertEquals(3, judgeResult);

		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * judge()メソッドテスト　正常系
	 * 以下条件で実行し、以下の確認事項を確認する
	 * 確認事項
	 * ・judge（）メソッドの戻り値が1(グー)であること
	 *
	 * 実行条件
	 * CPUプレーヤー数2名
	 * プレーヤー情報
	 * １：
	 * プレーヤーネーム：CPUプレーヤー1
	 * 選択した手：グー
	 * 2：
	 * プレーヤーネーム：CPUプレーヤー2
	 * 選択した手：チョキ
	 *
	 */
	@Test
	public void case004_OK() {

		//テストデータ作成
		List<JankenPlayer> testPlayerList = new ArrayList<JankenPlayer>();

		JankenPlayer player1 = new HumanJankenPlayerImpl("プレーヤ1");
		JankenPlayer player2 = new HumanJankenPlayerImpl("プレーヤ2");

		try {
			//プライベートなフィールドに値をセットする場合
			Class<? extends JankenPlayer> testData = player1.getClass();
			Field nameField = testData.getDeclaredField("playerHand");
			nameField.setAccessible(true);
			nameField.set(player1, 1);
			testPlayerList.add(player1);

			testData = player2.getClass();
			nameField = testData.getDeclaredField("playerHand");
			nameField.setAccessible(true);
			nameField.set(player2, 2);
			testPlayerList.add(player2);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			fail();
		}

		//テスト実施クラスのインスタンス生成処理
		JankenCuiGameApplicationImpl testobj = new JankenCuiGameApplicationImpl();

		//テストデータのセット
		testobj.setPlayerList(testPlayerList);

		try {
			//プライベートなメソッドを呼び出し戻り値を取得する場合
			Class<JankenCuiGameApplicationImpl> c = JankenCuiGameApplicationImpl.class;
			Method runMethod = c.getDeclaredMethod("judge");
			runMethod.setAccessible(true);
			int judgeResult = (int) runMethod.invoke(testobj);
			assertEquals(1, judgeResult);

		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			fail();
		}

	}

}
