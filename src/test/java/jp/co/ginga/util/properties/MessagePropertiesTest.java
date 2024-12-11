package jp.co.ginga.util.properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import jp.co.ginga.util.exception.SystemException;

/**
 * SysetemMessagePropertiesクラス
 *
 */
public class MessagePropertiesTest {

	private final String FILE = "properties/messages.properties";
	private final String CODE = "UTF-8";

	//テストデータ
	private String messageId1 = "msg.start";
	private String messageId2 = "janken.msg.game.winner";
	private String errorMessageId1 = "msg.error.argument";
	private String errorMessageId2 = "error.nodata";
	private String errorMessageId3 = "msg.error.properties.load";
	private String errorMessageId4 = "msg.error.properties.nodata";
	private String emptyMessageId = "";
	private String message1 = "これからゲームを開始します\n遊ぶゲームを選択してください.\n1:昭和クイズゲーム 2:じゃんけん";
	private String message2 = "勝利者は、プレーヤーです。";
	private String embedMessage = "プレーヤー";
	private String errorMessage = "msg.error.argument";
	private String errorMessage2 = "error.nodata";
	private String errorMessage3 = "msg.error.properties.load";
	private String errorMessage4 = "msg.error.properties.nodata";

	/**
	 * testInit001 正常系
	 * private static void init() throws SystemException
	 * --確認事項--
	 * propertiesフィールドの値がnullの場合、生成したPropertiesインスタンスをpropertiesフィールドに代入しファイルを読み込むこと
	 * --条件--
	 * propertiesフィールドの値はnull
	 * --検証項目--
	 * 1. propertiesフィールドのPropertiesインスタンスが代入されていること
	 * 2. 指定したファイルが読み込まれていること
	 */
	@Test
	public void testInit001() {
		try {
			//準備
			Method method = MessageProperties.class.getDeclaredMethod("init");
			Field propertiesField = MessageProperties.class.getDeclaredField("properties");
			propertiesField.setAccessible(true);
			method.setAccessible(true);
			propertiesField.set(MessageProperties.class, null);

			//テストメソッド
			method.invoke(MessageProperties.class);

			//検証
			Properties properties = new Properties();
			InputStream is = MessageProperties.class.getClassLoader().getResourceAsStream(this.FILE);
			properties.load(new InputStreamReader(is, this.CODE));
			Properties result = (Properties) propertiesField.get(MessageProperties.class);
			assertEquals(properties, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testInit002 正常系
	 * private static void init() throws SystemException
	 * --確認事項--
	 * propertiesフィールドがnullではない場合は何もしないこと
	 * --条件--
	 * propertiesフィールドの値はPropertiesインスタンス
	 * --検証項目--
	 * 1. propertiesフィールドが変化しないこと
	 */
	@Test
	public void testInit002() {
		try {
			//準備
			Method method = MessageProperties.class.getDeclaredMethod("init");
			Field propertiesField = MessageProperties.class.getDeclaredField("properties");
			propertiesField.setAccessible(true);
			method.setAccessible(true);
			Properties properties = new Properties();
			InputStream is = MessageProperties.class.getClassLoader()
					.getResourceAsStream("properties/messages.properties");
			properties.load(new InputStreamReader(is, "UTF-8"));
			propertiesField.set(MessageProperties.class, properties);

			//テストメソッド
			method.invoke(MessageProperties.class);

			//検証
			Properties result = (Properties) propertiesField.get(MessageProperties.class);
			assertEquals(properties, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testInit003 異常系
	 * private static void init() throws SystemException
	 * --確認事項--
	 * propertiesフィールドがnullの場合、ファイルを読み込む際にIOExceptionが発生し、SystemExceptionが発生すること
	 * --条件--
	 * propertiesフィールドの値はnull
	 * --検証項目--
	 * 1. SystemExceptionがスローされる
	 * 2. メッセージ「msg.error.properties.load」が含まれるか (取り出せないため未検証)
	 */
	@Test
	public void testInit003() {
		//モック化
		try (MockedConstruction<Properties> mockProperties = mockConstruction(
				Properties.class,
				(properties, context) -> {
					doThrow(new IOException()).when(properties).load(any(InputStreamReader.class));
					doReturn(this.errorMessage3).when(properties).getProperty(this.errorMessageId3);
				})) {
			//準備
			Method method = MessageProperties.class.getDeclaredMethod("init");
			Field propertiesField = MessageProperties.class.getDeclaredField("properties");
			propertiesField.setAccessible(true);
			method.setAccessible(true);
			propertiesField.set(MessageProperties.class, null);

			//テストメソッド
			InvocationTargetException e = assertThrows(InvocationTargetException.class,
					() -> method.invoke(MessageProperties.class));

			//検証
			assertEquals(SystemException.class, e.getTargetException().getClass());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetMessage001 正常系
	 * public static String getMessage(String resourceId) throws SystemException
	 * --確認事項--
	 * 引数に文字列を渡し、メッセージプロパティから文字列を取得する
	 * --条件--
	 * 引数resourceIdはメッセージプロパティにある文字列
	 * --検証項目--
	 * 1. メッセージが取得できるか
	 */
	@Test
	public void testGetMessage001() {
		try {
			//準備 (前回のテストのモックが残っているため)
			Field propertiesField = MessageProperties.class.getDeclaredField("properties");
			propertiesField.setAccessible(true);
			propertiesField.set(MessageProperties.class, null);

			//テストメソッド
			String result = MessageProperties.getMessage(this.messageId1);

			//検証
			assertEquals(this.message1, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetMessage002 異常系
	 * public static String getMessage(String resourceId) throws SystemException
	 * --確認事項--
	 * 引数にnullを渡し、SystemExceptionが発生すること　＊
	 * --条件--
	 * 引数resourceIdはnull
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. メッセージ「msg.error.argument」が含まれているか
	 * 
	 * ＊(initメソッドをパラメーターチェックよりも前に実行しないとpropertiesがnullのためNullPointerExceptionが発生する)
	 */
	@Test
	public void testGetMessage002() {
		try {
			//準備
			Field propertiesField = MessageProperties.class.getDeclaredField("properties");
			propertiesField.setAccessible(true);

			//モック化
			Properties mockProperties = mock(Properties.class);
			when(mockProperties.getProperty(this.errorMessageId1)).thenReturn(this.errorMessage);
			propertiesField.set(MessageProperties.class, mockProperties);

			//テストメソッド、検証
			SystemException e = assertThrows(SystemException.class, () -> MessageProperties.getMessage(null));
			assertEquals(this.errorMessage, e.getSysMsg());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetMessage003 異常系
	 * public static String getMessage(String resourceId) throws SystemException
	 * --確認事項--
	 * 引数に空の文字列を渡し、SystemExceptionが発生すること　＊
	 * --条件--
	 * 引数resourceIdは空の文字列
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. メッセージ「msg.error.argument」が含まれているか
	 * 
	 * ＊(initメソッドをパラメーターチェックよりも前に実行しないとpropertiesがnullのためNullPointerExceptionが発生する)
	 */
	@Test
	public void testGetMessage003() {
		try {
			//準備
			Field propertiesField = MessageProperties.class.getDeclaredField("properties");
			propertiesField.setAccessible(true);

			//モック化
			Properties mockProperties = mock(Properties.class);
			when(mockProperties.getProperty(this.errorMessageId1)).thenReturn(this.errorMessage);
			propertiesField.set(MessageProperties.class, mockProperties);

			//テストメソッド、検証
			SystemException e = assertThrows(SystemException.class,
					() -> MessageProperties.getMessage(this.emptyMessageId));
			assertEquals(this.errorMessage, e.getSysMsg());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetMessage004 異常系
	 * public static String getMessage(String resourceId) throws SystemException
	 * --確認事項--
	 * 引数に文字列を渡してプロパティの初期化に失敗しSystemExceptionが発生すること
	 * --条件--
	 * 引数resourceIdはメッセージプロパティにある文字列
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. メッセージ「msg.error.properties.load」が含まれているか 
	 */
	@Test
	public void testGetMessage004() {
		//モック化
		try (MockedConstruction<Properties> mockProperties = mockConstruction(
				Properties.class,
				(properties, context) -> {
					doThrow(new IOException()).when(properties).load(any(InputStreamReader.class));
					doReturn(this.errorMessage3).when(properties).getProperty(this.errorMessageId3);
				})) {
			//準備
			Field propertiesField = MessageProperties.class.getDeclaredField("properties");
			propertiesField.setAccessible(true);
			propertiesField.set(MessageProperties.class, null);

			//テストメソッド、検証
			SystemException e = assertThrows(SystemException.class,
					() -> MessageProperties.getMessage(this.messageId1));
			assertEquals(this.errorMessage3, e.getSysMsg());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetMessage005 異常系
	 * public static String getMessage(String resourceId) throws SystemException
	 * --確認事項--
	 * 引数に文字列を渡してSystemExceptionが発生すること
	 * --条件--
	 * 引数resourceIdはメッセージプロパティにある文字列
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. メッセージ「error.nodata」が含まれているか ＊
	 * 
	 * ＊(メッセージプロパティに「error.nodata」が存在しないため例外にnullが渡されている)
	 */
	@Test
	public void testGetMessage005() {
		try {
			//準備
			Field propertiesField = MessageProperties.class.getDeclaredField("properties");
			propertiesField.setAccessible(true);

			//モック化
			Properties mockProperties = mock(Properties.class);
			when(mockProperties.getProperty(this.messageId1)).thenReturn(null);
			when(mockProperties.getProperty(this.errorMessageId2, this.messageId1)).thenReturn(this.errorMessage2);
			propertiesField.set(MessageProperties.class, mockProperties);

			//テストメソッド、検証
			SystemException e = assertThrows(SystemException.class,
					() -> MessageProperties.getMessage(this.messageId1));
			assertEquals(this.errorMessage2, e.getSysMsg());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetMessageEmbed001 正常系
	 * public static String getMessage(String resourceId, String... arguments) throws SystemException
	 * --確認事項--
	 * 引数に文字列と埋め込む文字列を渡してメッセージプロパティから文字列を取得する
	 * --条件--
	 * 引数resourceIdはメッセージプロパティにある文字列
	 * 埋め込むデータは文字列
	 * --検証項目--
	 * 1. メッセージプロパティから文字列を取得できるか
	 */
	@Test
	public void testGetMessageEmbed001() {
		try {
			//テストメソッド
			String result = MessageProperties.getMessage(this.messageId2, this.embedMessage);

			//検証
			assertEquals(this.message2, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetMessageEmbed002 異常系
	 * public static String getMessage(String resourceId, String... arguments) throws SystemException
	 * --確認事項--
	 * 引数に空の文字列と埋め込む文字列を渡してSystemExceptionが発生するか
	 * --条件--
	 * 引数resourceIdは空の文字列
	 * 埋め込むデータは文字列
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. メッセージ「msg.error.argument」が含まれるか
	 */
	@Test
	public void testGetMessageEmbed002() {
		try {
			//準備
			Field propertiesField = MessageProperties.class.getDeclaredField("properties");
			propertiesField.setAccessible(true);

			//モック化
			Properties mockProperties = mock(Properties.class);
			when(mockProperties.getProperty(this.errorMessageId1)).thenReturn(this.errorMessage);
			propertiesField.set(MessageProperties.class, mockProperties);

			//テストメソッド、検証
			SystemException e = assertThrows(SystemException.class,
					() -> MessageProperties.getMessage(this.emptyMessageId, this.embedMessage));
			assertEquals(this.errorMessage, e.getSysMsg());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetMessageEmbed003 異常系
	 * public static String getMessage(String resourceId, String... arguments) throws SystemException
	 * --確認事項--
	 * 引数にnullと埋め込む文字列を渡してSystemExceptionが発生するか
	 * --条件--
	 * 引数resourceIdはnull
	 * 埋め込むデータは文字列
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. メッセージ「msg.error.argument」が含まれるか
	 */
	@Test
	public void testGetMessageEmbed003() {
		try {
			//準備
			Field propertiesField = MessageProperties.class.getDeclaredField("properties");
			propertiesField.setAccessible(true);

			//モック化
			Properties mockProperties = mock(Properties.class);
			when(mockProperties.getProperty(this.errorMessageId1)).thenReturn(this.errorMessage);
			propertiesField.set(MessageProperties.class, mockProperties);

			//テストメソッド、検証
			SystemException e = assertThrows(SystemException.class,
					() -> MessageProperties.getMessage(null, this.embedMessage));
			assertEquals(this.errorMessage, e.getSysMsg());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetMessageEmbed004 異常系
	 * public static String getMessage(String resourceId, String... arguments) throws SystemException
	 * --確認事項--
	 * 引数に文字列とnullを渡してSystemExceptionが発生するか
	 * --条件--
	 * 引数resourceIdはメッセージプロパティにある文字列
	 * 埋め込むデータはnull
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. メッセージ「msg.error.argument」が含まれるか
	 */
	@Test
	public void testGetMessageEmbed004() {
		try {
			//準備
			Field propertiesField = MessageProperties.class.getDeclaredField("properties");
			propertiesField.setAccessible(true);

			//モック化
			Properties mockProperties = mock(Properties.class);
			when(mockProperties.getProperty(this.errorMessageId1)).thenReturn(this.errorMessage);
			propertiesField.set(MessageProperties.class, mockProperties);

			//テストメソッド、検証
			SystemException e = assertThrows(SystemException.class,
					() -> MessageProperties.getMessage(this.message2, null));
			assertEquals(this.errorMessage, e.getSysMsg());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetMessageEmbed005 異常系
	 * public static String getMessage(String resourceId, String... arguments) throws SystemException
	 * --確認事項--
	 * 引数に文字列と埋め込む文字列を渡してプロパティの初期化に失敗しSystemExceptionが発生すること
	 * --条件--
	 * 引数resourceIdはメッセージプロパティにある文字列
	 * 埋め込むデータは文字列
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. メッセージ「msg.error.properties.load」が含まれているか 
	 */
	@Test
	public void testGetMessageEmbed005() {
		//モック化
		try (MockedConstruction<Properties> mockProperties = mockConstruction(
				Properties.class,
				(properties, context) -> {
					doThrow(new IOException()).when(properties).load(any(InputStreamReader.class));
					doReturn(this.errorMessage3).when(properties).getProperty(this.errorMessageId3);
				})) {
			//準備
			Field propertiesField = MessageProperties.class.getDeclaredField("properties");
			propertiesField.setAccessible(true);
			propertiesField.set(MessageProperties.class, null);

			//テストメソッド、検証
			SystemException e = assertThrows(SystemException.class,
					() -> MessageProperties.getMessage(this.messageId2, this.embedMessage));
			assertEquals(this.errorMessage3, e.getSysMsg());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetMessageEmbed006 異常系
	 * public static String getMessage(String resourceId) throws SystemException
	 * --確認事項--
	 * 引数に文字列と埋め込む文字列を渡してSystemExceptionが発生すること
	 * --条件--
	 * 引数resourceIdはメッセージプロパティにある文字列
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. メッセージ「msg.error.properties.nodata」が含まれているか ＊
	 * 
	 * ＊(メッセージプロパティに「msg.error.properties.nodata」が存在しないため例外にnullが渡されている)
	 */
	@Test
	public void testGetMessageEmbed006() {

		try {
			//準備
			Field propertiesField = MessageProperties.class.getDeclaredField("properties");
			propertiesField.setAccessible(true);

			//モック化
			Properties mockProperties = mock(Properties.class);
			when(mockProperties.getProperty(this.messageId2)).thenReturn(null);
			when(mockProperties.getProperty(this.errorMessageId4, this.messageId2)).thenReturn(this.errorMessage4);
			propertiesField.set(MessageProperties.class, mockProperties);

			//テストメソッド、検証
			SystemException e = assertThrows(SystemException.class,
					() -> MessageProperties.getMessage(this.messageId2, this.embedMessage));
			assertEquals(this.errorMessage4, e.getSysMsg());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * カバレッジを100%にするためのコンストラクタテスト
	 */
	@Test
	public void testConstructor001() {
		@SuppressWarnings("unused")
		MessageProperties m = new MessageProperties();
	}
}
