package jp.co.ginga.application;

import jp.co.ginga.util.exception.SystemException;

/**
 * CUIゲームアプリケーション インターフェース
 *
 */
public interface CuiGameApplication {

	/**
	 * ゲーム開始処理
	 * @throws SystemException
	 */
	public void action() throws SystemException;

}
