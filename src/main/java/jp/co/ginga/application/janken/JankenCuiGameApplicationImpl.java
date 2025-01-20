package jp.co.ginga.application.janken;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import jp.co.ginga.application.CuiGameApplication;
import jp.co.ginga.application.janken.factory.JankenPlayerFactory;
import jp.co.ginga.application.janken.jankenplayer.JankenPlayer;
import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.keyboard.Keyboard;
import jp.co.ginga.util.properties.MessageProperties;

/**
 * CUIじゃんけん実装クラス
 * @author Isogai
 *
 */
public class JankenCuiGameApplicationImpl implements CuiGameApplication {

	/**
	 * じゃんけんプレイヤーリスト
	 */
	List<JankenPlayer> playerList;

	/**
	 * 勝ち手
	 */
	int winHand;

	/**
	 * ゲームを続ける
	 */
	final static int CONTINUE = 1;

	/**
	 * ゲームを終了する
	 */
	final static int STOP = 2;

	/**
	 * プレイヤー人数の最小値
	 */
	final static int MIN_PLAYER = 2;

	/**
	 * プレイヤー人数の最大値
	 */
	final static int MAX_PLAYER = 10;

	/**
	 * じゃんけんゲーム処理
	 * @throws SystemException
	 */
	@Override
	public void action() throws SystemException {

		while (true) {

			while (true) {
				System.out.println(MessageProperties.getMessage("janken.msg.start"));

				//初期化処理
				this.init();

				//人間プレーヤーオブジェクト生成処理
				this.createHumanOfJankenPlayer();

				//CPUプレーヤーオブジェクト生成処理
				this.createNpcOfJankenPlayer();

				//じゃんけんプレーヤー数チェック処理
				if (isCheckJankenPlayerCount()) {
					break;
				}
				System.out.println(MessageProperties.getMessage("janken.msg.player.count.error"));
			}

			while (true) {

				//じゃんけんプレーヤのじゃんけんの手選択処理
				this.selectPlayerHand();

				//判定処理
				this.winHand = this.judge();
				if (this.winHand != JankenParam.DRAW.getInt()) {
					break;
				}

				System.out.println(MessageProperties.getMessage("janken.msg.game.draw"));

			}

			//勝利者表示処理
			this.viewWinner();

			//じゃんけんゲームコンティニュー判定処理
			if (hasGameContinue() == false) {
				break;
			}

		}

		System.out.println(MessageProperties.getMessage("janken.msg.end"));

	}

	/**
	 * じゃんけんゲーム初期化処理
	 * playerListがnullの場合、	playerListのインスタンス生成をする
	 * それ以外の場合はクリア処理をする
	 */
	void init() {

		//プレーヤリストの初期処理
		if (this.playerList == null) {
			this.playerList = new ArrayList<JankenPlayer>();
		} else {
			this.playerList.clear();
		}

	}

	/**
	 * 人間プレーヤーオブジェクト生成処理
	 * playerListがNULLの場合はシステムを終了する
	 * じゃんけんプレイヤーは2名～10名とする
	 * 人間プレイヤー数入力は0～10名とし、それ以外の入力は再入力を促す
	 * @throws SystemException
	 */
	void createHumanOfJankenPlayer() throws SystemException {

		//playerListのNULL判定
		if (this.playerList == null) {
			throw new SystemException(MessageProperties.getMessage("error.stop"));
		}

		while (true) {

			try {

				System.out.println(MessageProperties.getMessage("janken.msg.create.human"));
				int value = Keyboard.getInt(0, 10);

				for (int i = 0; i < value; i++) {
					//人間プレーヤオブジェクト生成処理
					playerList.add(JankenPlayerFactory.createJankenPlayer(JankenPlayerFactory.HUMAN,
							MessageProperties.getMessage("janken.msg.playername.human") + (i + 1)));
				}

				break;

			} catch (ApplicationException e) {
				System.out.println(MessageProperties.getMessage("msg.retype"));

			}
		}

	}

	/**
	 * NPCプレーヤーオブジェクト生成処理
	 * playerListがNULLの場合はシステムを終了する
	 * じゃんけんプレイヤーは2名～10名とする
	 * NPCプレイヤー数入力は0～10名とし、それ以外の入力は再入力を促す
	 * @throws SystemException
	 */
	void createNpcOfJankenPlayer() throws SystemException {

		//playerListのNULL判定
		if (this.playerList == null) {
			throw new SystemException(MessageProperties.getMessage("error.stop"));
		}

		while (true) {

			try {

				System.out.println(MessageProperties.getMessage("janken.msg.create.npc"));
				int value = Keyboard.getInt(0, 10);

				for (int i = 0; i < value; i++) {
					//CPUプレーヤオブジェクト生成処理
					playerList.add(JankenPlayerFactory.createJankenPlayer(JankenPlayerFactory.NPC,
							MessageProperties.getMessage("janken.msg.playername.npc") + (i + 1)));
				}

				break;

			} catch (ApplicationException e) {
				System.out.println(MessageProperties.getMessage("msg.retype"));
			}

		}

	}

	/**
	 * じゃんけんの手を選択する処理
	 * playerListがNULLの場合はシステムを終了する
	 * じゃんけんプレイヤーが2名～10名以外の場合はシステムを終了する
	 * @throws SystemException
	 */
	void selectPlayerHand() throws SystemException {

		//playerListがNULLまたはじゃんけんプレイヤー数が異常な場合(2名～10名以外)
		if (this.playerList == null || MAX_PLAYER < playerList.size() || playerList.size() < MIN_PLAYER) {
			throw new SystemException(MessageProperties.getMessage("error.stop"));
		}

		for (int i = 0; i < playerList.size(); i++) {
			//じゃんけんプレーヤのじゃんけんの手を選択する処理
			playerList.get(i).selectPlayerHand();
		}

	}

	/**
	 * じゃんけん判定処理
	 * playerListがNULLの場合はシステムを終了する
	 * じゃんけんプレイヤーは2名～10名以外の場合はシステムを終了する
	 * @return じゃんけんの勝ち手
	 * @throws SystemException
	 */
	int judge() throws SystemException {

		//playerListがNULLまたはじゃんけんプレイヤー数が異常な場合(2名～10名以外)
		if (this.playerList == null || MAX_PLAYER < playerList.size() || playerList.size() < MIN_PLAYER) {
			throw new SystemException(MessageProperties.getMessage("error.stop"));
		}

		boolean rockFlag = false;
		boolean scissorsFlag = false;
		boolean paperFlag = false;

		//じゃんけんプレーヤーが選択した手の状況を取得する処理
		for (JankenPlayer player : playerList) {
			switch (JankenParam.getEnum(player.getPlayerHand())) {
			case ROCK:
				rockFlag = true;
				break;
			case SCISSORS:
				scissorsFlag = true;
				break;
			case PAPER:
				paperFlag = true;
				break;
			default:
				throw new SystemException(MessageProperties.getMessage("error.arg"));
			}
		}

		//判定処理
		if (rockFlag == true && scissorsFlag == true && paperFlag == false) {
			return JankenParam.ROCK.getInt();
		} else if (rockFlag == false && scissorsFlag == true && paperFlag == true) {
			return JankenParam.SCISSORS.getInt();
		} else if (rockFlag == true && scissorsFlag == false && paperFlag == true) {
			return JankenParam.PAPER.getInt();
		} else {
			return JankenParam.DRAW.getInt();
		}

	}

	/**
	 * 勝利者表示処理
	 * playerListがNULLの場合はシステムを終了する
	 * じゃんけんプレイヤーは2名～10名以外の場合はシステムを終了する
	 * winHandがROCK、SCISSORS、PAPER以外の場合はシステムを終了する
	 * 勝利者の名前はスペースで区切らるように表示する
	 * 勝利者がいない場合はシステムを終了する
	 * @throws SystemException
	 */
	void viewWinner() throws SystemException {

		//playerListがNULLまたはじゃんけんプレイヤー数が異常(2名～10名以外)の場合
		if (this.playerList == null || MAX_PLAYER < playerList.size() || playerList.size() < MIN_PLAYER) {
			throw new SystemException(MessageProperties.getMessage("error.stop"));
		}
		//winHandがROCK、SCISSORS、PAPER以外の場合
		if (this.winHand != JankenParam.ROCK.getInt() && this.winHand != JankenParam.SCISSORS.getInt()
				&& this.winHand != JankenParam.PAPER.getInt()) {
			throw new SystemException(MessageProperties.getMessage("error.stop"));
		}

		StringBuilder sb = new StringBuilder();
		for (JankenPlayer player : playerList) {
			if (this.winHand == player.getPlayerHand()) {
				String playerName = player.getPlayerName(); //プレーヤーの名前を取得
				if (StringUtils.isEmpty(playerName)) { //プレーヤーの名前をisEmpty()チェック
					throw new SystemException(MessageProperties.getMessage("error.stop"));
				}
				sb.append(playerName + " ");
			}
		}
		String winners = sb.toString();

		System.out.println(MessageProperties.getMessage("janken.msg.game.winner", winners));

	}

	/**
	 * じゃんけんプレーヤー数チェック処理
	 * playerListがNULLの場合はシステムを終了する
	 * @return Jankenプレーヤオブジェクトが2～10人の場合は true / それ以外の場合は false
	 * @throws SystemException
	 */
	boolean isCheckJankenPlayerCount() throws SystemException {

		//playerListがNULLの場合
		if (this.playerList == null) {
			throw new SystemException(MessageProperties.getMessage("error.stop"));

		}
		if (MAX_PLAYER < playerList.size() || playerList.size() < MIN_PLAYER) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * じゃんけんゲーム継続問い合わせ処理
	 * 1,2以外を入力した場合は再入力を促す
	 * @return true:続ける false:終了
	 * @throws SystemException
	 */
	boolean hasGameContinue() throws SystemException {

		while (true) {
			try {
				System.out.println(MessageProperties.getMessage("janken.msg.game.continue"));
				switch (Keyboard.getInt(1, 2)) {
				case CONTINUE:
					return true;
				case STOP:
					return false;
				default:
					throw new SystemException(MessageProperties.getMessage("error.arg"));
				}

			} catch (ApplicationException e) {
				System.out.println(MessageProperties.getMessage("msg.retype"));
			}
		}
	}

	//テスト用
	/**
	 * @return playerList
	 */
	public List<JankenPlayer> getPlayerList() {
		return playerList;
	}

	/**
	 * @param playerList セットする playerList
	 */
	public void setPlayerList(List<JankenPlayer> playerList) {
		this.playerList = playerList;
	}

	/**
	 * @param winHand セットする winHand
	 */
	public void setWinHand(int winHand) {
		this.winHand = winHand;
	}

}
