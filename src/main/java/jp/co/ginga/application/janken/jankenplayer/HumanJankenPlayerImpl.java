package jp.co.ginga.application.janken.jankenplayer;

import jp.co.ginga.application.janken.JankenParam;
import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.keybord.Keybord;
import jp.co.ginga.util.properties.MessageProperties;

/**
 * じゃんけんプレーヤ 人間
 * @author yoshi
 *
 */
public class HumanJankenPlayerImpl implements JankenPlayer {

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
	 * @param name
	 */
	public HumanJankenPlayerImpl(final String playerName) {
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
	 */
	@Override
	public void selectJankenHand() throws SystemException {

		while (true) {
			try {

				System.out.println(MessageProperties.getMessage("janken.msg.select.hand.human"));

				int selectHand = Keybord.getInt(1, 3);

				switch (JankenParam.getEnum(selectHand)) {
				case ROCK:
					this.playerHand = JankenParam.ROCK.getInt();
					break;
				case SCISSORS:
					this.playerHand = JankenParam.SCISSORS.getInt();
					break;
				case PAPER:
					this.playerHand = JankenParam.PAPER.getInt();
					break;
				default:
					throw new SystemException(MessageProperties.getMessage("error.arg"));
				}

				break;

			} catch (ApplicationException e) {
				System.out.println(MessageProperties.getMessage("msg.retype"));
			}

		}
	}

	public void setPlayerHand(int i) {
		this.playerHand = i;
	}

}
