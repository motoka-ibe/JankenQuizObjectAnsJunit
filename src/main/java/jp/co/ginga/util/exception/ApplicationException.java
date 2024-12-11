package jp.co.ginga.util.exception;

/**
 * ApplicationExceptionクラス
 *
 */
public class ApplicationException extends RuntimeException {

	/**
	 * システムメッセージ
	 */
	private String sysMsg;

	/**
	 * コンストラクタ
	 * @param sysMsg
	 */
	public ApplicationException(String sysMsg) {
		this.sysMsg = sysMsg;

	}

	/**
	 * @return sysMsg
	 */
	public String getSysMsg() {
		return sysMsg;
	}

}
