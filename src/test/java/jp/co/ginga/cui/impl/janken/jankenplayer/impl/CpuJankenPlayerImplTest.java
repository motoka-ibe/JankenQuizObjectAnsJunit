package jp.co.ginga.cui.impl.janken.jankenplayer.impl;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;

import jp.co.ginga.cui.impl.janken.JankenParam;
import jp.co.ginga.util.exception.SystemException;

/**
 * じゃんけんプレーヤ CPU
 *
 *
 */
public class CpuJankenPlayerImplTest {
	//テストデータ
	private int rock = JankenParam.ROCK.getInt();
	private int scissors = JankenParam.SCISSORS.getInt();
	private int paper = JankenParam.PAPER.getInt();
	private int illegalValue = -1;
	private int zeroValue = 0;
	private String playerName = "CPU1";
	private String nullPlayerName = null;
	private int playerHand = rock;
	private int notSetting = 0;
	private int from = rock;
	private int to = paper;
	private String errorMessage = "パラメーターの値が不正です。";

	@InjectMocks
	private CpuJankenPlayerImpl player = new CpuJankenPlayerImpl(this.playerName);

	/**
	 * testConstructor001 正常系
	 * public CpuJankenPlayerImpl(String playerName)
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
			Field playerNameField = CpuJankenPlayerImpl.class.getDeclaredField("playerName");
			playerNameField.setAccessible(true);
			
			//テストメソッド
			CpuJankenPlayerImpl player = new CpuJankenPlayerImpl(this.playerName);

			//検証
			String result = String.valueOf(playerNameField.get(player));
			assertEquals(this.playerName, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testConstructor002 正常系
	 * public CpuJankenPlayerImpl(String playerName)
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
			Field playerNameField = CpuJankenPlayerImpl.class.getDeclaredField("playerName");
			playerNameField.setAccessible(true);
			
			//テストメソッド
			CpuJankenPlayerImpl player = new CpuJankenPlayerImpl(this.nullPlayerName);

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
			Field playerNameField = CpuJankenPlayerImpl.class.getDeclaredField("playerName");
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
			Field playerNameField = CpuJankenPlayerImpl.class.getDeclaredField("playerName");
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
			Field playerHandField = CpuJankenPlayerImpl.class.getDeclaredField("playerHand");
			playerHandField.setAccessible(true);
			playerHandField.set(this.player, this.playerHand);

			//テストメソッド
			int result = this.player.getJankenHand();

			//検証
			assertEquals(result, this.playerHand);

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
		assertEquals(result, this.notSetting);
	}


	/**
	 * testSelectJankenHand001 正常系
	 * public void selectJankenHand() throws SystemException
	 * --確認事項--
	 * playerHandフィールドに列挙型の定数が代入される
	 * --条件--
	 * 生成されるRandomインスタンスはnextIntが0を返すmock
	 * --検証項目--
	 * 1. playerHandフィールドの値はJankenParamのROCKの値であるか
	 * 2. Randomインスタンスは一個生成されるか
	 * 3. netxIntは1回だけ呼び出されるか
	 */
	@Test
	public void testSelectJankenHand001() {
		//モック化
		try (MockedConstruction<Random> mockRandom = mockConstruction(
				Random.class, 
				(random, context) -> when(random.nextInt(this.to)).thenReturn(this.rock-1)
			)
		){
			//準備
			Field playerHandField = CpuJankenPlayerImpl.class.getDeclaredField("playerHand");
			playerHandField.setAccessible(true);
			
			//テストメソッド
			this.player.selectJankenHand();

			//検証
			int playerHand = (Integer) playerHandField.get(this.player);
			assertEquals(playerHand, this.rock);
			assertEquals(mockRandom.constructed().size(), 1);
			verify(mockRandom.constructed().get(0), times(1)).nextInt(this.to);

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
	 * 生成されるRandomインスタンスはnextIntが1を返すmock
	 * --検証項目--
	 * 1. playerHandフィールドの値はJankenParamのSCISSORSの値であるか
	 * 2. Randomインスタンスは一個生成されるか
	 * 3. netxIntは1回だけ呼び出されるか
	 */
	@Test
	public void testSelectJankenHand002() {
		//モック化
		try (MockedConstruction<Random> mockRandom = mockConstruction(
				Random.class, 
				(random, context) -> when(random.nextInt(this.to)).thenReturn(this.scissors-1)
			)
		){
			//準備
			Field playerHandField = CpuJankenPlayerImpl.class.getDeclaredField("playerHand");
			playerHandField.setAccessible(true);
			
			//テストメソッド
			this.player.selectJankenHand();

			//検証
			int playerHand = (Integer) playerHandField.get(this.player);
			assertEquals(playerHand, this.scissors);
			assertEquals(mockRandom.constructed().size(), 1);
			verify(mockRandom.constructed().get(0), times(1)).nextInt(this.to);

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
	 * 生成されるRandomインスタンスはnextIntが2を返すmock
	 * --検証項目--
	 * 1. playerHandフィールドの値はJankenParamのPAPERの値であるか
	 * 2. Randomインスタンスは一個生成されるか
	 * 3. netxIntは1回だけ呼び出されるか
	 */
	@Test
	public void testSelectJankenHand003() {
		//モック化
		try (MockedConstruction<Random> mockRandom = mockConstruction(
				Random.class, 
				(random, context) -> when(random.nextInt(this.to)).thenReturn(this.paper-1)
			)
		){
			//準備
			Field playerHandField = CpuJankenPlayerImpl.class.getDeclaredField("playerHand");
			playerHandField.setAccessible(true);
			
			//テストメソッド
			this.player.selectJankenHand();

			//検証
			int playerHand = (Integer) playerHandField.get(this.player);
			assertEquals(playerHand, this.paper);
			assertEquals(mockRandom.constructed().size(), 1);
			verify(mockRandom.constructed().get(0), times(1)).nextInt(this.to);

		}catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectJankenHand004 異常系
	 * public void selectJankenHand() throws SystemException
	 * --確認事項--
	 * RandomクラスのnextIntで範囲外の整数がJankenParamのgetEnumに渡される場合、NullPointerExceptionが発生する
	 * --条件--
	 * 生成されるRandomインスタンスはnextIntが-2を返すmock
	 * --検証項目--
	 * 1. NullPointerExceptionがスローされるか
	 * 2. Randomインスタンスは一個生成されるか
	 * 3. netxIntは1回だけ呼び出されるか
	 */
	@Test
	public void testSelectJankenHand004() {
		//モック化
		try (MockedConstruction<Random> mockRandom = mockConstruction(
				Random.class, 
				(random, context) -> when(random.nextInt(this.to)).thenReturn(this.illegalValue-1)
			)
		){
			//テストメソッド 、検証
			assertThrows(NullPointerException.class, () -> this.player.selectJankenHand());
			assertEquals(mockRandom.constructed().size(), 1);
			verify(mockRandom.constructed().get(0), times(1)).nextInt(this.to);
		}
	}

	/**
	 * testSelectJankenHand005 異常系
	 * public void selectJankenHand() throws SystemException
	 * --確認事項--
	 * RandomクラスのnextIntで範囲外の-1がJankenParamのgetEnumに渡される場合、SystemExceptionが発生する
	 * --条件--
	 * 生成されるRandomインスタンスはnextIntが-1を返すmock
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. メッセージ「パラメーターの値が不正です。」が含まれているか
	 * 3. Randomインスタンスは一個生成されるか
	 * 4. netxIntは1回だけ呼び出されるか
	 */
	@Test
	public void testSelectJankenHand005() {
		//モック化
		try (MockedConstruction<Random> mockRandom = mockConstruction(
				Random.class, 
				(random, context) -> when(random.nextInt(this.to)).thenReturn(this.zeroValue-1)
			)
		){
			//テストメソッド、検証
			SystemException e = assertThrows(SystemException.class, () -> this.player.selectJankenHand());
			assertEquals(this.errorMessage, e.getSysMsg());
			assertEquals(mockRandom.constructed().size(), 1);
			verify(mockRandom.constructed().get(0), times(1)).nextInt(this.to);
		}
	}

}
