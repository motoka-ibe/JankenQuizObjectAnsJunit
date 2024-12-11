package jp.co.ginga.application.janken.factory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

import jp.co.ginga.application.janken.jankenplayer.JankenPlayer;
import jp.co.ginga.util.exception.SystemException;

/**
 * じゃんけんプレーヤー製造クラス
 * @author Isogai
 *
 */
public class TestJankenPlayerFactory {

	//テストデータ	
	private int Type_human = 1;
	private int Type_npc = 2;
	private int Type_NotExist = 0;
	private String name = "プレーヤー1";
	private String npcName = "NPC1";
	private String noName = "";

	JankenPlayer player; //じゃんけんプレーヤー

	JankenPlayerFactory jankenPlayerFactory = new JankenPlayerFactory();

	/**
	 * testCreateJankenPlayer_01() 異常系
	 * public static JankenPlayer createJankenPlayer(final int type, final String name) throws SystemException
	 * 
	 * --確認事項--
	 * 空の名前をセットした場合にSystemExceptionを発行することを確認
	 * --条件--
	 * 名前が空の場合
	 * --検証項目--
	 * SystemExceptionを発行すること
	 */
	@Test
	public void testCreateJankenPlayer_01() {

		try {
			//テストメソッドの実行
			JankenPlayerFactory.createJankenPlayer(Type_human, noName);
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("プレーヤーの名前が設定されていません。", e.getSysMsg());
		}
	}

	/**
	 * testCreateJankenPlayer_02()　正常系
	 * public static JankenPlayer createJankenPlayer(final int type, final String name) throws SystemException
	 * 
	 * --確認事項--
	 * 名前が空でないとき、プレーヤーに名前がセットされること
	 * --条件--
	 * type人間の場合で、名前「プレーヤー1」をセット
	 * --検証項目--
	 * じゃんけんプレーヤーに名前「プレーヤー」がセットされていることを確認
	
	 */
	@Test
	public void testCreateJankenPlayer_02() {

		try {
			//テストメソッドの実行
			JankenPlayer player = JankenPlayerFactory.createJankenPlayer(Type_human, name);

			//検証
			assertEquals("プレーヤー1", player.getPlayerName());

		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testCreateJankenPlayer_03()　正常系
	 * public static JankenPlayer createJankenPlayer(final int type, final String name) throws SystemException
	 * 
	 * --確認事項--
	 * 名前が空でないとき、プレーヤーに名前がセットされること
	 * --条件--
	 * typeがNPCの場合で、名前「NPC1」をセット
	 * --検証項目--
	 * じゃんけんプレーヤーに名前「NPC1」がセットされていることを確認
	 */
	@Test
	public void testCreateJankenPlayer_03() {

		try {
			//テストメソッドの実行
			JankenPlayer player = JankenPlayerFactory.createJankenPlayer(Type_npc, npcName);

			//検証
			assertEquals("NPC1", player.getPlayerName());

		} catch (SystemException e) {
			e.printStackTrace();
		}

	}

	/**
	 * testCreateJankenPlayer_04() 異常系
	 * public static JankenPlayer createJankenPlayer(final int type, final String name) throws SystemException
	 * 
	 * --確認事項--
	 * 人間でもNPCでもない場合、SystemException(実行データ不良)が発行されること。
	 * --条件--
	 * typeが人間またはNPCの以外場合
	 *  * --検証項目--
	 * SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testCreateJankenPlayer_04() {

		try {
			//テストメソッドの実行
			JankenPlayerFactory.createJankenPlayer(Type_NotExist, name);
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("プレーヤータイプが正しく設定されていません。", e.getSysMsg());
		}
	}

}
