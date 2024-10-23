package jp.co.ginga.util.exception;

/**
 * SystemExceptionクラス
 *
 */
public class SystemException extends Exception {

	/**
	 * システムメッセージ
	 */
	private String sysMsg;

	/**
	 * コンストラクタ
	 * @param sysMsg
	 */
	public SystemException(String sysMsg) {
		this.sysMsg = sysMsg;
	}

	/**
	 * @return sysMsg
	 */
	public String getSysMsg() {
		return sysMsg;
	}

}
