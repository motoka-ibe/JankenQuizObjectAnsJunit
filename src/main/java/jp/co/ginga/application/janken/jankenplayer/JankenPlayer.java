package jp.co.ginga.application.janken.jankenplayer;

import jp.co.ginga.util.exception.SystemException;

/**
 * じゃんけんプレーヤー　インターフェース
 * @author yoshi
 *
 */
public interface JankenPlayer {

	/**
	 * プレーヤー名取得
	 * @return
	 */
	public String getPlayerName();

	/**
	 * じゃんけんの手情報取得
	 * @return
	 */
	public int getPlayerHand();

	/**
	 * じゃんけんの手選択処理
	 * @throws SystemException
	 */
	public void selectPlayerHand() throws SystemException;

}
