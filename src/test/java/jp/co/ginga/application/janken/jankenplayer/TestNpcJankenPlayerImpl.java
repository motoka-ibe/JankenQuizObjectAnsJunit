package jp.co.ginga.application.janken.jankenplayer;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import jp.co.ginga.application.janken.JankenParam;
import jp.co.ginga.util.exception.SystemException;

/**
 * NPCプレーヤ 人間
 * @author Isogai
 *
 */
public class TestNpcJankenPlayerImpl {

	//テストデータ
	private int rock = JankenParam.ROCK.getInt();
	private int scissors = JankenParam.SCISSORS.getInt();
	private int paper = JankenParam.PAPER.getInt();
	private int draw = JankenParam.DRAW.getInt();
	private String playerName = "NPC1";
	private String nullPlayerName = null;
	private int playerHand = rock;
	private int notSetting = 0;

	private NpcJankenPlayerImpl player = new NpcJankenPlayerImpl(this.playerName);
	private NpcJankenPlayerImpl nullPlayer = new NpcJankenPlayerImpl(this.nullPlayerName);

	/**
	 * testNpcJankenPlayerImpl_01 正常系
	 * public NPCJankenPlayerImpl(String playerName)
	 * 
	 * --確認事項--
	 * インスタンス生成時に渡された引数がフィールドに代入されているか
	 * --条件--
	 *	引数は文字列
	 * --検証項目--
	 * インスタンス生成時に渡した文字列とplayerNameフィールドの値が等しいこと
	 */
	@Test
	public void testNpcJankenPlayerImpl_01() {
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
	 * testNpcJankenPlayerImpl_02 正常系
	 * public NpcJankenPlayerImpl(String playerName)
	 * 
	 * --確認事項--
	 * インスタンス生成時に渡された引数がフィールドに代入されているか
	 * --条件--
	 *	引数はnull
	 * --検証項目--
	 * 1. playerNameフィールドの値がnullであること
	 */
	@Test
	public void testNpcJankenPlayerImpl_02() {
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
	 * playerNameフィールドの値が返されているか
	 * --条件--
	 *	playerNameフィールドの値は文字列
	 * --検証項目--
	 * 戻り値とplayerNameフィールドの値が等しいこと
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
	 * playerNameフィールドの値が返されているか
	 * --条件--
	 *	playerNameフィールドの値はnull
	 * --検証項目--
	 * 戻り値の値がnullであること
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
	 * testgetPlayerHand_01 正常系
	 * public int getPlayerHand()
	 * 
	 * --確認事項--
	 * playerHandフィールドの値が返されるか
	 * --条件--
	 *	playerHandフィールドの値は整数
	 * --検証項目--
	 * 1. 戻り値とplayerHandフィールドの値は等しいか
	 */
	@Test
	public void testgetPlayerHand_01() {

		//NpcJankenPlayerImplクラスのフィールドplayerHandにテストクラスで用意したplayerHandをセットする
		this.player.setPlayerHand(this.playerHand);

		// テストメソッド
		int result = this.player.getPlayerHand();

		// 検証
		assertEquals(result, this.playerHand);
	}

	/**
	 * testgetPlayerHand_02 正常系
	 * public int getPlayerHand()
	 * 
	 * --確認事項--
	 * playerHandフィールドの値が返されるか
	 * --条件--
	 *	playerHandフィールドの値をセットしない
	 * --検証項目--
	 * 1. 戻り値は0であるか
	 */
	@Test
	public void testgetPlayerHand_02() {

		//playerHandフィールドの値をセットしない

		//テストメソッド
		int result = this.player.getPlayerHand();

		//検証
		assertEquals(this.notSetting, result);
	}

	/**
	 * testselectPlayerHand_01() 正常系
	 * public void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * じゃんけんの手がグーの場合
	 * ROCKをplayerHandに代入して返すこと
	 * --条件--
	 * KeybordクラスがnextIntで0を返すmock
	 * --検証項目--
	 * ROCKをplayerHandに代入して返すこと
	 */
	@Test
	public void testselectPlayerHand_01() {
		// モック化
		try (MockedConstruction<Random> mockRandom = mockConstruction(
				Random.class,
				(random, context) -> when(random.nextInt(3)).thenReturn(this.rock - 1))) {
			// テストメソッド
			this.player.selectPlayerHand();

			// 検証
			assertEquals(this.rock, this.player.getPlayerHand()); // 期待する手と実際の手が一致するか確認
			assertEquals(mockRandom.constructed().size(), 1); // モックが1つだけ作成されたことを確認
			verify(mockRandom.constructed().get(0), times(1)).nextInt(3); // verify：nextInt(3)メソッドが1回呼ばれたことを確認

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testselectPlayerHand_02() 正常系
	 * public void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * じゃんけんの手がチョキの場合
	 * ROCKをplayerHandに代入して返すこと
	 * --条件--
	 * KeybordクラスがnextIntで1を返すmock
	 * --検証項目--
	 * SCISSORSをplayerHandに代入して返すこと
	 */
	@Test
	public void testselectPlayerHand_02() {
		// モック化
		try (MockedConstruction<Random> mockRandom = mockConstruction(
				Random.class,
				(random, context) -> when(random.nextInt(3)).thenReturn(this.scissors - 1))) {
			// テストメソッド
			this.player.selectPlayerHand();

			// 検証
			assertEquals(this.scissors, this.player.getPlayerHand()); // 期待する手と実際の手が一致するか確認
			assertEquals(mockRandom.constructed().size(), 1); // モックが1つだけ作成されたことを確認
			verify(mockRandom.constructed().get(0), times(1)).nextInt(3); // verify：nextInt(3)メソッドが1回呼ばれたことを確認

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testselectPlayerHand_03() 正常系
	 * public void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * じゃんけんの手がパーの場合
	 * ROCKをplayerHandに代入して返すこと
	 * --条件--
	 * KeybordクラスがnextIntで2を返すmock
	 * --検証項目--
	 * PAPERをplayerHandに代入して返すこと
	 */
	@Test
	public void testselectPlayerHand_03() {
		// モック化
		try (MockedConstruction<Random> mockRandom = mockConstruction(
				Random.class,
				(random, context) -> when(random.nextInt(3)).thenReturn(this.paper - 1))) {
			// テストメソッド
			this.player.selectPlayerHand();

			// 検証
			assertEquals(this.paper, this.player.getPlayerHand()); // 期待する手と実際の手が一致するか確認
			assertEquals(mockRandom.constructed().size(), 1); // モックが1つだけ作成されたことを確認
			verify(mockRandom.constructed().get(0), times(1)).nextInt(3); // verify：nextInt(3)メソッドが1回呼ばれたことを確認

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testselectPlayerHand_04() 異常系
	 * public void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * じゃんけんの手があいこの場合
	 * コンソールに「パラメーターの値が不正です。」と出力されることを確認する
	 * --条件--
	 * KeybordクラスがnextIntで-1を返すmock
	 * --検証項目--
	 * コンソールに「パラメーターの値が不正です。」と出力されることを確認する
	 */
	@Test
	public void testselectPlayerHand_04() {
		// モック化
		try (MockedConstruction<Random> mockRandom = mockConstruction(
				Random.class,
				(random, context) -> when(random.nextInt(3)).thenReturn(this.draw - 1))) {

			this.player.selectPlayerHand();
			fail();

			// テストメソッド

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("パラメーターの値が不正です。", e.getSysMsg());
		}

	}

}
