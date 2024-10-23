package jp.co.ginga.application.janken.jankenplayer;

import java.util.Random;

import jp.co.ginga.application.janken.JankenParam;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.properties.MessageProperties;

/**
 * じゃんけんプレーヤ NPC
 * 
 *
 */
public class NpcJankenPlayerImpl implements JankenPlayer {

	/**
	 * 	プレーヤ―名
	 */
	private String playerName;

	/**
	 * プレーヤーが選択した手
	 */
	private int playerHand;

	/**
	 * コンストラクタ
	 * 
	 * @param playerName
	 */
	public NpcJankenPlayerImpl(final String playerName) {
		this.playerName = playerName;
	}

	/**
	 * プレーヤー名取得
	 * 
	 * @return
	 */
	@Override
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * じゃんけんの手情報取得
	 * 
	 * @return
	 */
	@Override
	public int getJankenHand() {
		return this.playerHand;
	}

	/**
	 * じゃんけんの手選択処理
	 * 
	 * @throws SystemException
	 */
	@Override
	public void selectJankenHand() throws SystemException {

		// Random()は、nextIntのカッコ内の文字"未満"の数字をランダムに表示する
		switch (JankenParam.getEnum(new Random().nextInt(3) + 1)) {
		case ROCK:
			playerHand = JankenParam.ROCK.getInt();
			break;
		case SCISSORS:
			playerHand = JankenParam.SCISSORS.getInt();
			break;
		case PAPER:
			playerHand = JankenParam.PAPER.getInt();
			break;
		default:
			throw new SystemException(MessageProperties.getMessage("error.arg"));
		}
	}

}
