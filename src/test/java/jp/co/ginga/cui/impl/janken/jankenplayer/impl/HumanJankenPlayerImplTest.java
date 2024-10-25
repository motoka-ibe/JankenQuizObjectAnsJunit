package jp.co.ginga.cui.impl.janken.jankenplayer.impl;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;

import jp.co.ginga.application.janken.JankenParam;
import jp.co.ginga.application.janken.jankenplayer.HumanJankenPlayerImpl;
import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.keybord.Keybord;

/**
 * じゃんけんプレーヤ 人間
 * @author yoshi
 *
 */

public class HumanJankenPlayerImplTest{
	//テストデータ
	private int rock = JankenParam.ROCK.getInt();
	private int scissors = JankenParam.SCISSORS.getInt();
	private int paper = JankenParam.PAPER.getInt();
	private int illegalValue = -1;
	private int zeroValue = 0;
	private String playerName = "プレイヤー1";
	private String nullPlayerName = null;
	private int playerHand = rock;
	private int notSetting = 0;
	private int from = rock;
	private int to = paper;
	private String errorMessage = "パラメーターの値が不正です。";

	@InjectMocks
	private HumanJankenPlayerImpl player = new HumanJankenPlayerImpl(this.playerName);

	
	/**
	 * testConstructor001 正常系
	 * public HumanJankenPlayerImpl(String playerName)
	 * --確認事項--
	 * インスタンス生成時に渡された引数がフィールドに代入されているか
	 * --条件--
	 *	引数は文字列
	 * --検証項目--
	 * 1. インスタンス生成時に渡した文字列とplayerNameフィールドの値が等しいか
	 */
	@Test
	public void testConstructor001() {
		try {
			//準備
			Field playerNameField = HumanJankenPlayerImpl.class.getDeclaredField("playerName");
			playerNameField.setAccessible(true);
			
			//テストメソッド
			HumanJankenPlayerImpl player = new HumanJankenPlayerImpl(this.playerName);

			//検証
			String result = (String) playerNameField.get(player);
			assertEquals(this.playerName, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testConstructor002 正常系
	 * public HumanJankenPlayerImpl(String playerName)
	 * --確認事項--
	 * インスタンス生成時に渡された引数がフィールドに代入されているか
	 * --条件--
	 *	引数はnull
	 * --検証項目--
	 * 1. playerNameフィールドの値がnullであるか
	 */
	@Test
	public void testConstructor002() {
		try {
			//準備
			Field playerNameField = HumanJankenPlayerImpl.class.getDeclaredField("playerName");
			playerNameField.setAccessible(true);
			
			//テストメソッド
			HumanJankenPlayerImpl player = new HumanJankenPlayerImpl(this.nullPlayerName);

			//検証
			String result = (String) playerNameField.get(player);
			assertNull(result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetPlayerName001 正常系
	 * public String getPlayerName()
	 * --確認事項--
	 * playerNameフィールドの値が返されるか
	 * --条件--
	 *	playerNameフィールドの値は文字列
	 * --検証項目--
	 * 1. 戻り値とplayerNameフィールドの値が等しいか
	 */
	@Test
	public void testGetPlayerName001() {
		try {
			//準備
			Field playerNameField = HumanJankenPlayerImpl.class.getDeclaredField("playerName");
			playerNameField.setAccessible(true);
			playerNameField.set(this.player, this.playerName);

			//テストメソッド
			String result = this.player.getPlayerName();

			//検証
			assertEquals(this.playerName, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetPlayerName002 正常系
	 * public String getPlayerName()
	 * --確認事項--
	 * playerNameフィールドの値が返されるか
	 * --条件--
	 *	playerNameフィールドの値はnull
	 * --検証項目--
	 * 1. 戻り値はnullであるか
	 */
	@Test
	public void testGetPlayerName002() {
		try {
			//準備
			Field playerNameField = HumanJankenPlayerImpl.class.getDeclaredField("playerName");
			playerNameField.setAccessible(true);
			playerNameField.set(this.player, this.nullPlayerName);

			//テストメソッド
			String result = player.getPlayerName();

			//検証
			assertNull(result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetJankenHand001 正常系
	 * public int getJankenHand()
	 * --確認事項--
	 * playerHandフィールドの値が返されるか
	 * --条件--
	 *	playerHandフィールドの値は整数
	 * --検証項目--
	 * 1. 戻り値とplayerHandフィールドの値は等しいか
	 */
	@Test
	public void testGetJankenHand001() {
		try {
			//準備
			Field playerHandField = HumanJankenPlayerImpl.class.getDeclaredField("playerHand");
			playerHandField.setAccessible(true);
			playerHandField.set(this.player, this.playerHand);

			//テストメソッド
			int result = this.player.getJankenHand();

			//検証
			assertEquals(this.playerHand, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetJankenHand002 正常系
	 * public int getJankenHand()
	 * --確認事項--
	 * playerHandフィールドの値が返されるか
	 * --条件--
	 *	playerHandフィールドの値をセットしない
	 * --検証項目--
	 * 1. 戻り値は0であるか
	 */
	@Test
	public void testGetJankenHand002() {

		//テストメソッド
		int result = this.player.getJankenHand();

		//検証
		assertEquals(this.notSetting, result);
	}

	/**
	 * testSelectJankenHand001 正常系
	 * public void selectJankenHand() throws SystemException
	 * --確認事項--
	 * playerHandフィールドに列挙型の定数が代入される
	 * --条件--
	 * KeybordクラスはgetIntで1(グー)を返すmock
	 * --検証項目--
	 * 1. playerHandフィールドの値はJankenParamのROCKの値であるか
	 * 2. KeybordのgetIntは1回呼び出される
	 */
	@Test
	public void testSelectJankenHand001() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt(this.from, this.to)).thenReturn(this.rock);

			//テストメソッド
			this.player.selectJankenHand();

			//検証
			Field playerHandField = HumanJankenPlayerImpl.class.getDeclaredField("playerHand");
			playerHandField.setAccessible(true);
			int playerHand = (Integer) playerHandField.get(this.player);

			assertEquals(playerHand, this.rock);
			mockKeybord.verify(() -> Keybord.getInt(this.from, this.to), times(1));

		}catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectJankenHand002 正常系
	 * public void selectJankenHand() throws SystemException
	 * --確認事項--
	 * playerHandフィールドに列挙型の定数が代入される
	 * --条件--
	 *	 KeybordクラスはgetIntで2(チョキ)を返すmock
	 * --検証項目--
	 * 1. playerHandフィールドの値はJankenParamのSCISSORSの値であるか
	 * 2. KeybordのgetIntは1回呼び出される
	 */
	@Test
	public void testSelectJankenHand002() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt(this.from, this.to)).thenReturn(this.scissors);
			
			//準備
			Field playerHandField = HumanJankenPlayerImpl.class.getDeclaredField("playerHand");
			playerHandField.setAccessible(true);
			
			//テストメソッド
			this.player.selectJankenHand();

			//検証
			int playerHand = (Integer) playerHandField.get(this.player);
			assertEquals(playerHand, this.scissors);
			mockKeybord.verify(() -> Keybord.getInt(this.from, this.to), times(1));

		}catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectJankenHand003 正常系
	 * public void selectJankenHand() throws SystemException
	 * --確認事項--
	 * playerHandフィールドに列挙型の定数が代入される
	 * --条件--
	 * KeybordクラスはgetIntで3(パー)を返すmock
	 * --検証項目--
	 * 1. playerHandフィールドの値はJankenParamのPAPERの値であるか
	 * 2. KeybordのgetIntは1回呼び出される
	 */
	@Test
	public void testSelectJankenHand003() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt(this.from, this.to)).thenReturn(this.paper);
			
			//準備
			Field playerHandField = HumanJankenPlayerImpl.class.getDeclaredField("playerHand");
			playerHandField.setAccessible(true);
			
			//テストメソッド
			this.player.selectJankenHand();

			//検証
			int playerHand = (Integer) playerHandField.get(this.player);
			assertEquals(playerHand, this.paper);
			mockKeybord.verify(() -> Keybord.getInt(this.from, this.to), times(1));

		}catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectJankenHand004 正常系
	 * public void selectJankenHand() throws SystemException
	 * --確認事項--
	 * playerHandフィールドに列挙型の定数が代入される
	 * --条件--
	 * KeybordクラスはgetIntで1回目にApplicationExceptionが発生し、2回目に1(グー)を返すmock
	 * --検証項目--
	 * 1. playerHandフィールドの値はJankenParamのROCKの値であるか
	 * 2. KeybordのgetIntは2回呼び出される
	 */
	@Test
	public void testSelectJankenHand004() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt(this.from, this.to)).thenThrow(new ApplicationException(null)).thenReturn(this.rock);
			
			//準備
			Field playerHandField = HumanJankenPlayerImpl.class.getDeclaredField("playerHand");
			playerHandField.setAccessible(true);
			
			//テストメソッド
			this.player.selectJankenHand();

			//検証
			int playerHand = (Integer) playerHandField.get(this.player);
			assertEquals(playerHand, this.rock);
			mockKeybord.verify(() -> Keybord.getInt(this.from, this.to), times(2));

		}catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectJankenHand005 正常系
	 * public void selectJankenHand() throws SystemException
	 * --確認事項--
	 * KeybordのgetIntから範囲外の整数がJankenParamのgetEnumに渡された場合NullPointerExceptionが発生する
	 * --条件--
	 *	KeybordクラスはgetIntで-1を返すmock
	 * --検証項目--
	 * 1. NullPointerExceptionがスローされるか
	 * 2. KeybordのgetIntは1回呼び出される
	 */
	@Test
	public void testSelectJankenHand005() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt(this.from, this.to)).thenReturn(this.illegalValue);

			//テストメソッド 検証
			assertThrows(NullPointerException.class, () -> this.player.selectJankenHand());
			mockKeybord.verify(() -> Keybord.getInt(this.from, this.to), times(1));
		}
	}

	/**
	 * testSelectJankenHand006 正常系
	 * public void selectJankenHand() throws SystemException
	 * --確認事項--
	 * KeybordのgetIntから範囲外の0がJankenParamのgetEnumに渡された場合SystemExceptionが発生する
	 * --条件--
	 *	KeybordクラスはgetIntで0を返すmock
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. メッセージ「パラメーターの値が不正です。」が含まれているか
	 * 2. KeybordのgetIntは1回呼び出される
	 */
	@Test
	public void testSelectJankenHand006() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt(this.from, this.to)).thenReturn(this.zeroValue);

			//テストメソッド 検証
			SystemException e = assertThrows(SystemException.class, () -> this.player.selectJankenHand());
			assertEquals(this.errorMessage, e.getSysMsg());
			mockKeybord.verify(() -> Keybord.getInt(this.from, this.to), times(1));
		}
	}
}
