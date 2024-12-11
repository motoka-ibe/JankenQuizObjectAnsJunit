package jp.co.ginga.util.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Properties;

import jp.co.ginga.util.exception.SystemException;

/**
 * SysetemMessagePropertiesクラス
 *
 */
public class MessageProperties {

	private static Properties properties;
	private final static String FILE = "properties/messages.properties";

	/**
	 * メッセージ取得処理
	 * @param resourceId
	 * @return
	 * @throws SystemException
	 */
	public static String getMessage(String resourceId) throws SystemException {

		//パラメーターチェック resourceId
		if (resourceId == null || resourceId.isEmpty()) {
			throw new SystemException(properties.getProperty("msg.error.argument"));
		}

		//プロパティファイル初期化処理
		MessageProperties.init();

		//メッセージ取得処理
		String msg = properties.getProperty(resourceId);

		//メッセージチェック処理
		if (msg == null) {
			throw new SystemException(properties.getProperty("error.nodata", resourceId));
		}

		return msg;
	}

	/**
	 * メッセージ取得処理 フォーマット
	 * @param resourceId
	 * @param arguments
	 * @return
	 * @throws SystemException
	 */
	public static String getMessage(String resourceId, String... arguments) throws SystemException {

		//パラメーターチェック resourceId,arguments
		if (resourceId == null || resourceId.isEmpty()
				|| arguments == null || arguments.length == 0) {
			//rguments.length == 0の場合はこちらは呼び出されない

			throw new SystemException(properties.getProperty("msg.error.argument"));
		}

		//プロパティファイル初期化処理
		MessageProperties.init();

		//メッセージ取得処理
		String msg = properties.getProperty(resourceId);

		//メッセージチェック処理
		if (msg == null) {

			throw new SystemException(properties.getProperty("msg.error.properties.nodata", resourceId));
		}

		return MessageFormat.format(msg, (Object[]) arguments);
	}

	/**
	 * プロパティファイル読込処理
	 * @throws SystemException
	 */
	private static void init() throws SystemException {

		try {

			if (properties == null) {
				properties = new Properties();
				InputStream is = MessageProperties.class.getClassLoader().getResourceAsStream(FILE);
				properties.load(new InputStreamReader(is, "UTF-8"));
			}

		} catch (IOException e) {
			//ファイルload処理を失敗した場合
			throw new SystemException(properties.getProperty("msg.error.properties.load"));

		}
	}
}
