package jp.co.ginga.application.janken.factory;

import jp.co.ginga.application.janken.jankenplayer.HumanJankenPlayerImpl;
import jp.co.ginga.application.janken.jankenplayer.JankenPlayer;
import jp.co.ginga.application.janken.jankenplayer.NpcJankenPlayerImpl;
import jp.co.ginga.util.exception.SystemException;

/**
 * じゃんけんプレーヤー製造クラス
 * @author Isogai
 *
 */
public class JankenPlayerFactory {
	
	public final static int HUMAN = 1;
	public final static int NPC = 2;

	/**
	 * 
	 * @param type
	 * @param name
	 * @return
	 * @throws SystemException
	 */
	public static JankenPlayer createJankenPlayer(final int type, final String name) throws SystemException {

		if (name.isEmpty()) {
			throw new SystemException("プレーヤーの名前が設定されていません。");
		}

		JankenPlayer player = null;
		switch (type) {
		case HUMAN:
			player = new HumanJankenPlayerImpl(name);
			break;
		case NPC:
			player = new NpcJankenPlayerImpl(name);
			break;
		default:
			throw new SystemException("プレーヤータイプが正しく設定されていません。");
		}
		return player;
	}

}
