package jp.co.ginga.application.janken.jankenplayer;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import jp.co.ginga.application.janken.JankenParam;
import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.keybord.Keyboard;

/**
 * じゃんけんプレーヤ 人間
 * @author Isogai
 *
 */
public class TestHumanJankenPlayerImpl {

	//テストデータ
	private int rock = JankenParam.ROCK.getInt();
	private int scissors = JankenParam.SCISSORS.getInt();
	private int paper = JankenParam.PAPER.getInt();
	private int draw = JankenParam.DRAW.getInt();
	private String playerName = "プレイヤー1";
	private String nullPlayerName = null;
	private int playerHand = rock;
	private int notSetting = 0;

	private HumanJankenPlayerImpl player = new HumanJankenPlayerImpl(this.playerName);
	private HumanJankenPlayerImpl nullPlayer = new HumanJankenPlayerImpl(this.nullPlayerName);

	/**
	 * testHumanJankenPlayerImpl_01 正常系
	 * public HumanJankenPlayerImpl(String playerName)
	 * 
	 * --確認事項--
	 * インスタンス生成時に渡された引数がフィールドに代入されているか
	 * --条件--
	 *	引数は文字列
	 * --検証項目--
	 * インスタンス生成時に渡した文字列とplayerNameフィールドの値が等しいこと
	 */
	@Test
	public void testHumanJankenPlayerImpl_01() {
		try {

			//検証
			String result = this.player.getPlayerName();
			assertEquals(this.playerName, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testHumanJankenPlayerImpl_02 正常系
	 * public HumanJankenPlayerImpl(String playerName)
	 * 
	 * --確認事項--
	 * インスタンス生成時に渡された引数がフィールドに代入されているか
	 * --条件--
	 *	引数はnull
	 * --検証項目--
	 * playerNameフィールドの値がnullであること
	 */
	@Test
	public void testHumanJankenPlayerImpl_02() {
		try {

			//検証
			String result = this.nullPlayer.getPlayerName();
			assertNull(result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetPlayerName_01 正常系
	 * public String getPlayerName()
	 * 
	 * --確認事項--
	 * playerNameフィールドの値が返されるか
	 * --条件--
	 *	playerNameフィールドの値は文字列
	 * --検証項目--
	 * 戻り値とplayerNameフィールドの値が等しいか
	 */
	@Test
	public void testGetPlayerName_01() {
		try {

			//テストメソッド
			String result = this.player.getPlayerName();

			//検証
			assertEquals(this.playerName, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetPlayerName_02 正常系
	 * public String getPlayerName()
	 * 
	 * --確認事項--
	 * playerNameフィールドの値が返されるか
	 * --条件--
	 *	playerNameフィールドの値はnull
	 * --検証項目--
	 * 戻り値はnullであるか
	 */
	@Test
	public void testGetPlayerName_02() {
		try {

			//テストメソッド
			String result = this.nullPlayer.getPlayerName();

			//検証
			assertNull(result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetJankenHand_01 正常系
	 * public int getJankenHand()
	 * 
	 * --確認事項--
	 * playerHandフィールドの値が返されるか
	 * --条件--
	 *	playerHandフィールドの値は整数
	 * --検証項目--
	 * 戻り値とplayerHandフィールドの値は等しいこと
	 */
	@Test
	public void testGetJankenHand_01() {
		try {

			//HumanJankenPlayerImplクラスのフィールドplayerHandにテストクラスで用意したplayerHandをセットする
			this.player.setPlayerHand(rock);

			//テストメソッド
			int result = this.player.getJankenHand();

			//検証
			assertEquals(this.playerHand, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetJankenHand_02 正常系
	 * public int getJankenHand()
	 * 
	 * --確認事項--
	 * playerHandフィールドの値が返されるか
	 * --条件--
	 *	playerHandフィールドの値をセットしない
	 * --検証項目--
	 * 戻り値が0であること
	 */
	@Test
	public void testGetJankenHand_02() {

		//テストメソッド
		int result = this.player.getJankenHand();

		//検証
		assertEquals(this.notSetting, result);
	}

	/**
	 * testSelectJankenHand_01 正常系
	 * public void selectJankenHand() throws SystemException
	 * 
	 * --確認事項--
	 * 1を入力した場合
	 * --条件--
	 * KeyboardクラスがgetIntで1(グー)を返すmock
	 * --検証項目--
	 * 1. playerHandフィールドの値はJankenParamのROCKの値であるか
	 * 2. KeyboardのgetIntは1回呼び出される
	 */
	@Test
	public void testSelectJankenHand_01() {
		// モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 3)).thenReturn(this.rock);

			// テストメソッド
			this.player.selectJankenHand();

			// playerHandの取得
			int playerHand = this.player.getJankenHand();

			// 検証
			assertEquals(this.rock, playerHand);
			mockKeyboard.verify(() -> Keyboard.getInt(1, 3), times(1));

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectJankenHand_02 正常系
	 * public void selectJankenHand() throws SystemException
	 * 
	 * --確認事項--
	 * 1を入力した場合
	 * --条件--
	 * KeyboardクラスがgetIntで1(チョキ)を返すmock
	 * --検証項目--
	 * 1. playerHandフィールドの値はJankenParamのSCISSORSの値であるか
	 * 2. KeyboardのgetIntは1回呼び出される
	 */
	@Test
	public void testSelectJankenHand_02() {
		// モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 3)).thenReturn(this.scissors);

			// テストメソッド
			this.player.selectJankenHand();

			// playerHandの取得
			int playerHand = this.player.getJankenHand();

			// 検証
			assertEquals(this.scissors, playerHand);
			mockKeyboard.verify(() -> Keyboard.getInt(1, 3), times(1));

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectJankenHand_03 正常系
	 * public void selectJankenHand() throws SystemException
	 * 
	 * --確認事項--
	 * 1を入力した場合
	 * --条件--
	 * KeyboardクラスがgetIntで1(パー)を返すmock
	 * --検証項目--
	 * 1. playerHandフィールドの値はJankenParamのPAPERの値であるか
	 * 2. KeyboardのgetIntは1回呼び出される
	 */
	@Test
	public void testSelectJankenHand_03() {
		// モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 3)).thenReturn(this.paper);

			// テストメソッド
			this.player.selectJankenHand();

			// playerHandの取得
			int playerHand = this.player.getJankenHand();

			// 検証
			assertEquals(this.paper, playerHand);
			mockKeyboard.verify(() -> Keyboard.getInt(1, 3), times(1));

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectJankenHand_04 正常系
	 * public void selectJankenHand() throws SystemException
	 * 
	 * --確認事項--
	 * KeyboardのgetIntから範囲外の0(DRAW)がJankenParamのgetEnumに渡された場合、SystemExceptionが発生する
	 * --条件--
	 *	KeyboardクラスがgetIntで0を返すmock
	 * --検証項目--
	 * コンソールに「パラメーターの値が不正です。」と出力されることを確認する
	 */
	@Test
	public void testSelectJankenHand_04() {
		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 3)).thenReturn(this.draw);

			// テストメソッド
			this.player.selectJankenHand();
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("パラメーターの値が不正です。", e.getSysMsg());
		}

	}

	/**
	 * testSelectJankenHand_05 異常系
	 * public void selectJankenHand() throws SystemException
	 * 
	 * --確認事項--
	 * 1～3以外を入力した場合
	 * --条件--
	 * KeyboardクラスがgetIntで1回目にApplicationExceptionが発生させて
	 * 2回目に1(グー)を返し無限ループを抜ける
	 * --検証項目--
	 * コンソールに「再入力を促すメッセージを出力し、処理を繰り返す。」と出力されることを確認する
	 */
	@Test
	public void testSelectJankenHand_05() {
		// モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 3)).thenThrow(new ApplicationException("")).thenReturn(this.rock);

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));
			// テストメソッド
			this.player.selectJankenHand();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("入力した値が不正です。再入力をお願いします。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

}
