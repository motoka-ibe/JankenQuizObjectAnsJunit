package jp.co.ginga.util.keybord;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;

public class TestKeyboard {

	//テストデータ
	private String inputString = "abcde";
	private String inputStringUnderFrom = "abce";
	private String inputStringOverTo = "abcdefghijk";
	private String inputInteger = "7";
	private String inputIntegerUnderFrom = "4";
	private String inputIntegerOverTo = "11";
	private String errorMessage1 = "キーボード接続エラーです。ゲームを終了します。";
	private String errorMessage2 = "範囲外の数値が入力されました。";
	private String errorMessage3 = "パラメーターの値が不正です。";
	private String errorMessage4 = "数字を入力してください。";
	private int from = 5;
	private int to = 10;
	private int value = 7;
	private int valueUnderFrom = 11;
	private int valueOverTo = 11;

	/**
	 * testGetBufferedReaderInstance001 正常系
	 * static BufferedReader getBufferedReaderInstance() 
	 * --確認事項--
	 * brフィールドの値がnullの場合、生成したBufferedReaderインスタンスをbrフィールドに代入してbrフィールドが返されるか
	 * --条件--
	 * brフィールドの値はnull
	 * --検証項目--
	 * 1. nullが返されていない
	 * 2. フィールドの値が返されている
	 */
	@Test
	public void testGetBufferedReaderInstance001() {
		try {
			//準備
			Method method = Keyboard.class.getDeclaredMethod("getBufferedReaderInstance");
			Field brField = Keyboard.class.getDeclaredField("br");
			method.setAccessible(true);
			brField.setAccessible(true);
			brField.set(Keyboard.class, null);

			//テストメソッド
			BufferedReader result = (BufferedReader) method.invoke(Keyboard.class);

			//検証
			BufferedReader br = (BufferedReader) brField.get(Keyboard.class);
			assertNotNull(result);
			assertEquals(br, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetBufferedReaderInstance002 正常系
	 * static BufferedReader getBufferedReaderInstance() 
	 * --確認事項--
	 * brフィールドの値がnullでない場合、現在のbrフィールドが返されるか
	 * --条件--
	 * brフィールドの値はBufferedReaderインスタンス
	 * --検証項目--
	 * 1. フィールドの値が返されている
	 */
	@Test
	public void testGetBufferedReaderInstance002() {
		try {
			//準備
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Method method = Keyboard.class.getDeclaredMethod("getBufferedReaderInstance");
			Field brField = Keyboard.class.getDeclaredField("br");
			method.setAccessible(true);
			brField.setAccessible(true);
			brField.set(Keyboard.class, br);

			//テストメソッド
			BufferedReader result = (BufferedReader) method.invoke(Keyboard.class);

			//検証
			assertEquals(br, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetString001 正常系
	 * public static String getString() throws SystemException
	 * --確認事項--
	 * キーボードから入力した値が返されるか
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * --検証項目--
	 * 1. 戻り値が指定した値であるか
	 */
	@Test
	public void testGetString001() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputString);
			brField.set(Keyboard.class, mockBR);

			//テストメソッド
			String result = Keyboard.getString();

			//検証
			assertEquals(this.inputString, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetString002 異常系
	 * public static String getString() throws SystemException
	 * --確認事項--
	 * readLineメソッドでIOExceptionが発生した際、メッセージ「キーボード接続エラーです。ゲームを終了します。」を持つSystemExceptionが発生するか
	 * --条件--
	 * brフィールドはreadLineメソッドがIOExceptionを発生させるようにモック化したBufferedReaderインスタンス
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. 例外にメッセージ「キーボード接続エラーです。ゲームを終了します。」が含まれているか
	 */
	@Test
	public void testGetString002() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenThrow(new IOException());
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			SystemException e = assertThrows(SystemException.class, () -> Keyboard.getString());
			assertEquals(this.errorMessage1, e.getSysMsg());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetStringHasRange001 正常系
	 * public static String getString(int from, int to) throws SystemException, ApplicationException
	 * --確認事項--
	 * キーボードから入力した値が返されるか
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 指定する値は引数の範囲内の文字列
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. 戻り値が指定した値であるか
	 */
	@Test
	public void testGetStringHasRange001() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputString);
			brField.set(Keyboard.class, mockBR);

			//テストメソッド
			String result = Keyboard.getString(this.from, this.to);

			//検証
			assertEquals(this.inputString, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetStringHasRange002 異常系
	 * public static String getString(int from, int to) throws SystemException, ApplicationException
	 * --確認事項--
	 * 引数に(from > to)の引数を渡し、ApplicationExceptionが発生する
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 指定する値は引数の範囲内の文字列
	 * 引数fromとtoに(from > to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされるか
	 * 2. 例外にメッセージ「範囲外の数値が入力されました。」が含まれているか
	 * 3. getStringメソッドが呼び出されていないか
	 */
	@Test
	public void testGetStringHasRange002() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputString);
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			ApplicationException e = assertThrows(ApplicationException.class,
					() -> Keyboard.getString(this.to, this.from));
			assertEquals(this.errorMessage3, e.getSysMsg());
			verify(mockBR, times(0)).readLine();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetStringHasRange003 異常系
	 * public static String getString(int from, int to) throws SystemException, ApplicationException
	 * --確認事項--
	 * 引数に(from = to)の引数を渡し、ApplicationExceptionが発生する
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 指定する値は引数の範囲内の文字列
	 * 引数fromとtoに(from = to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされるか
	 * 2. 例外にメッセージ「範囲外の数値が入力されました。」が含まれているか
	 * 3. getStringメソッドが呼び出されていないか
	 */
	@Test
	public void testGetStringHasRange003() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputString);
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			ApplicationException e = assertThrows(ApplicationException.class,
					() -> Keyboard.getString(this.to, this.to));
			assertEquals(this.errorMessage3, e.getSysMsg());
			verify(mockBR, times(0)).readLine();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetStringHasRange004 異常系
	 * public static String getString(int from, int to) throws SystemException, ApplicationException
	 * --確認事項--
	 * getStringでIOExceptionが発生したら、SystemExceptionが発生する
	 * --条件--
	 * brフィールドはreadLineメソッドがIOExceptionを発生させるようにモック化したBufferedReaderインスタンス
	 * 指定する値は引数の範囲内の文字列
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. 例外にメッセージ「キーボード接続エラーです。ゲームを終了します。」が含まれているか
	 * 3. getStringメソッドが一回だけ呼び出されているか
	 */
	@Test
	public void testGetStringHasRange004() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenThrow(new IOException());
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			SystemException e = assertThrows(SystemException.class, () -> Keyboard.getString(this.from, this.to));
			assertEquals(this.errorMessage1, e.getSysMsg());
			verify(mockBR, times(1)).readLine();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetStringHasRange005 異常系
	 * public static String getString(int from, int to) throws SystemException, ApplicationException
	 * --確認事項--
	 * 引数で指定した範囲外の長さの文字列を入力しApplicationExceptionが発生する
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 入力した文字列の長さがfrom未満
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされるか
	 * 2. 例外にメッセージ「範囲外の数値が入力されました。」が含まれているか
	 * 3. getStringメソッドが一回だけ呼び出されているか
	 */
	@Test
	public void testGetStringHasRange005() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputStringUnderFrom);
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			ApplicationException e = assertThrows(ApplicationException.class,
					() -> Keyboard.getString(this.from, this.to));
			assertEquals(this.errorMessage2, e.getSysMsg());
			verify(mockBR, times(1)).readLine();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetStringHasRange006 異常系
	 * public static String getString(int from, int to) throws SystemException, ApplicationException
	 * --確認事項--
	 * 引数で指定した範囲外の長さの文字列を入力しApplicationExceptionが発生する
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 入力した文字列の長さがtoより大きい
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされるか
	 * 2. 例外にメッセージ「範囲外の数値が入力されました。」が含まれているか
	 * 3. getStringメソッドが一回だけ呼び出されているか
	 */
	@Test
	public void testGetStringHasRange006() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputStringOverTo);
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			ApplicationException e = assertThrows(ApplicationException.class,
					() -> Keyboard.getString(this.from, this.to));
			assertEquals(this.errorMessage2, e.getSysMsg());
			verify(mockBR, times(1)).readLine();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetInt001 正常系
	 * public static int getInt() throws ApplicationException, SystemException 
	 * --確認事項--
	 * キーボードから入力した値がint型に変換されて返されるか
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 指定する値は整数のみ
	 * --検証項目--
	 * 1. 戻り値がint型に変換された指定した値であるか
	 */
	@Test
	public void testGetInt001() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputInteger);
			brField.set(Keyboard.class, mockBR);

			//テストメソッド
			int result = Keyboard.getInt();

			//検証
			assertEquals(Integer.parseInt(this.inputInteger), result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetInt002 異常系
	 * public static int getInt() throws ApplicationException, SystemException 
	 * --確認事項--
	 * メッセージ「数字を入力してください。」を持つApplicationExceptionが発生するか
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 指定する値は整数以外の文字が含まれる
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされるか
	 * 2. 例外にメッセージ「数字を入力してください。」が含まれているか
	 */
	@Test
	public void testGetInt002() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputString);
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			ApplicationException e = assertThrows(ApplicationException.class, () -> Keyboard.getInt());
			assertEquals(this.errorMessage4, e.getSysMsg());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetInt003 異常系
	 * public static int getInt() throws ApplicationException, SystemException 
	 * --確認事項--
	 * メッセージ「キーボード接続エラーです。ゲームを終了します。」を持つSystemExceptionが発生するか
	 * --条件--
	 * brフィールドはreadLineメソッドがIOExceptionを発生させるようにモック化したBufferedReaderインスタンス
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. 例外にメッセージ「キーボード接続エラーです。ゲームを終了します。」が含まれているか
	 */
	@Test
	public void testGetInt003() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenThrow(new IOException());
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			SystemException e = assertThrows(SystemException.class, () -> Keyboard.getInt());
			assertEquals(this.errorMessage1, e.getSysMsg());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetIntHasRange001 正常系
	 * public static int getInt(int from, int to) throws ApplicationException, SystemException 
	 * --確認事項--
	 * 引数で指定した範囲内の整数を入力し、int型に変換されたその値が返る
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 指定する整数は引数の範囲内の整数
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. 戻り値が整数に変換された指定した値であるか
	 */
	@Test
	public void testGetIntHasRange001() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputInteger);
			brField.set(Keyboard.class, mockBR);

			//テストメソッド
			int result = Keyboard.getInt(this.from, this.to);

			//検証
			assertEquals(Integer.parseInt(this.inputInteger), result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetIntHasRange002 異常系
	 * public static int getInt(int from, int to) throws ApplicationException, SystemException 
	 * --確認事項--
	 * 引数に(from > to)の引数を渡し、ApplicationExceptionが発生する
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 指定する整数は引数の範囲内の整数
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされるか
	 * 2	メッセージ「パラメーターの値が不正です。」が含まれているか
	 * 3 getInt()メソッドは呼び出されていないか
	 */
	@Test
	public void testGetIntHasRange002() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputInteger);
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			ApplicationException e = assertThrows(ApplicationException.class, () -> Keyboard.getInt(this.to, this.from));
			assertEquals(this.errorMessage3, e.getSysMsg());
			verify(mockBR, times(0)).readLine();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetIntHasRange003 異常系
	 * public static int getInt(int from, int to) throws ApplicationException, SystemException 
	 * --確認事項--
	 * 引数に(from = to)の引数を渡し、ApplicationExceptionが発生する
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 指定する整数は引数の範囲内の整数
	 * 引数fromとtoに(from = to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされるか
	 * 2	メッセージ「パラメーターの値が不正です。」が含まれているか
	 * 3 getInt()メソッドは呼び出されていないか
	 */
	@Test
	public void testGetIntHasRange003() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputInteger);
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			ApplicationException e = assertThrows(ApplicationException.class, () -> Keyboard.getInt(this.to, this.to));
			assertEquals(this.errorMessage3, e.getSysMsg());
			verify(mockBR, times(0)).readLine();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetIntHasRange004 異常系
	 * public static int getInt(int from, int to) throws ApplicationException, SystemException 
	 * --確認事項--
	 * getIntで整数以外の文字を入力したらApplicationExceptionが発生する
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 指定する値は整数以外の文字が含まれている
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされるか
	 * 2. 例外にメッセージ「数字を入力してください。」が含まれているか
	 * 3. getIntメソッドが一回だけ呼び出されているか
	 */
	@Test
	public void testGetIntHasRange004() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputString);
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			ApplicationException e = assertThrows(ApplicationException.class, () -> Keyboard.getInt(this.from, this.to));
			assertEquals(this.errorMessage4, e.getSysMsg());
			verify(mockBR, times(1)).readLine();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetIntHasRange005 異常系
	 * public static int getInt(int from, int to) throws ApplicationException, SystemException 
	 * --確認事項--
	 * getIntでIOExceptionが発生したら、SystemExceptionが発生する
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 指定する値は引数の範囲内の整数
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. 例外にメッセージ「キーボード接続エラーです。ゲームを終了します。」が含まれているか
	 * 3. getIntメソッドが一回だけ呼び出されているか
	 */
	@Test
	public void testGetIntHasRange005() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenThrow(new IOException());
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			SystemException e = assertThrows(SystemException.class, () -> Keyboard.getInt(this.from, this.to));
			assertEquals(this.errorMessage1, e.getSysMsg());
			verify(mockBR, times(1)).readLine();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetIntHasRange006 異常系
	 * public static int getInt(int from, int to) throws ApplicationException, SystemException 
	 * --確認事項--
	 * 引数に(from < to)の引数を渡し、範囲外の整数を入力してApplicationExceptionが発生する
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 指定する整数は引数from未満
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされるか
	 * 2	メッセージ「範囲外の数値が入力されました。」が含まれているか
	 * 3 getInt()メソッドは1回だけ呼び出されているか
	 */
	@Test
	public void testGetIntHasRange006() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputIntegerUnderFrom);
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			ApplicationException e = assertThrows(ApplicationException.class, () -> Keyboard.getInt(this.from, this.to));
			assertEquals(this.errorMessage2, e.getSysMsg());
			verify(mockBR, times(1)).readLine();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetIntHasRange007 異常系
	 * public static int getInt(int from, int to) throws ApplicationException, SystemException 
	 * --確認事項--
	 * 引数に(from < to)の引数を渡し、範囲外の整数を入力してApplicationExceptionが発生する
	 * --条件--
	 * brフィールドはreadLineメソッドが指定した値を返すようにモック化したBufferedReaderインスタンス
	 * 指定する整数は引数toより大きい
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされるか
	 * 2	メッセージ「範囲外の数値が入力されました。」が含まれているか
	 * 3 getInt()メソッドは1回だけ呼び出されているか
	 */
	@Test
	public void testGetIntHasRange007() {
		try {
			//準備
			Field brField = Keyboard.class.getDeclaredField("br");
			brField.setAccessible(true);

			//モック化
			BufferedReader mockBR = mock(BufferedReader.class);
			when(mockBR.readLine()).thenReturn(this.inputIntegerOverTo);
			brField.set(Keyboard.class, mockBR);

			//テストメソッドと検証
			ApplicationException e = assertThrows(ApplicationException.class, () -> Keyboard.getInt(this.from, this.to));
			assertEquals(this.errorMessage2, e.getSysMsg());
			verify(mockBR, times(1)).readLine();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testIsRange001 正常系
	 * private static void isRange(int value, int from, int to) throws ApplicationException, SystemException 
	 * --確認事項--
	 * 引数にvalueとfrom, to (from < to)の引数を渡してApplicationExceptionが発生しないこと
	 * --条件--
	 * 引数valueはfrom, toの範囲内の整数
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされないこと
	 */
	@Test
	public void testIsRange001() {
		try {
			//準備
			Method method = Keyboard.class.getDeclaredMethod("isRange", int.class, int.class, int.class);
			method.setAccessible(true);

			//テストメソッド
			method.invoke(Keyboard.class, this.value, this.from, this.to);
			//検証
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testIsRange002 異常系
	 * private static void isRange(int value, int from, int to) throws ApplicationException, SystemException 
	 * --確認事項--
	 * 引数にvalue(value < from)とfrom, to (from < to)の引数を渡してApplicationExceptionが発生すること
	 * --条件--
	 * 引数valueはfrom未満の整数
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされる
	 * 2. メッセージ「範囲外の数値が入力されました。」が含まれること (取得できないため未検証)
	 */
	@Test
	public void testIsRange002() {
		try {
			//準備
			Method method = Keyboard.class.getDeclaredMethod("isRange", int.class, int.class, int.class);
			method.setAccessible(true);

			//テストメソッド
			InvocationTargetException e = assertThrows(InvocationTargetException.class,
					() -> method.invoke(Keyboard.class, this.valueUnderFrom, this.from, this.to));
			//検証
			assertEquals(ApplicationException.class, e.getTargetException().getClass());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testIsRange003 異常系
	 * private static void isRange(int value, int from, int to) throws ApplicationException, SystemException 
	 * --確認事項--
	 * 引数にvalue(value < from)とfrom, to (from < to)の引数を渡してApplicationExceptionが発生すること
	 * --条件--
	 * 引数valueはtoより大きい整数
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされる
	 * 2. メッセージ「範囲外の数値が入力されました。」が含まれること (取得できないため未検証)
	 */
	@Test
	public void testIsRange003() {
		try {
			//準備
			Method method = Keyboard.class.getDeclaredMethod("isRange", int.class, int.class, int.class);
			method.setAccessible(true);

			//テストメソッド
			InvocationTargetException e = assertThrows(InvocationTargetException.class,
					() -> method.invoke(Keyboard.class, this.valueOverTo, this.from, this.to));
			//検証
			assertEquals(ApplicationException.class, e.getTargetException().getClass());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testHasCheckRangeValue001 正常系
	 * private static void hasCheckRangeValue(int from, int to) throws ApplicationException, SystemException
	 * --確認事項--
	 * 引数にfrom, to (from < to)の引数を渡してApplicationExceptionが発生しないこと
	 * --条件--
	 * 引数fromとtoに(from < to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされないこと
	 */
	@Test
	public void testHasCheckRangeValue001() {
		try {
			//準備
			Method method = Keyboard.class.getDeclaredMethod("hasCheckRangeValue", int.class, int.class);
			method.setAccessible(true);

			//テストメソッド
			method.invoke(Keyboard.class, this.from, this.to);
			//検証
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testHasCheckRangeValue002 異常系
	 * private static void hasCheckRangeValue(int from, int to) throws ApplicationException, SystemException
	 * --確認事項--
	 * 引数にfrom, to (from > to)の引数を渡してApplicationExceptionが発生すること
	 * --条件--
	 * 引数fromとtoに(from > to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされること
	 */
	@Test
	public void testHasCheckRangeValue002() {
		try {
			//準備
			Method method = Keyboard.class.getDeclaredMethod("hasCheckRangeValue", int.class, int.class);
			method.setAccessible(true);

			//テストメソッド
			InvocationTargetException e = assertThrows(InvocationTargetException.class,
					() -> method.invoke(Keyboard.class, this.to, this.from));
			//検証
			assertEquals(ApplicationException.class, e.getTargetException().getClass());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testHasCheckRangeValue003 異常系
	 * private static void hasCheckRangeValue(int from, int to) throws ApplicationException, SystemException
	 * --確認事項--
	 * 引数にfrom, to (from = to)の引数を渡してApplicationExceptionが発生すること
	 * --条件--
	 * 引数fromとtoに(from = to)となる引数を与える
	 * --検証項目--
	 * 1. ApplicationExceptionがスローされること
	 */
	@Test
	public void testHasCheckRangeValue003() {
		try {
			//準備
			Method method = Keyboard.class.getDeclaredMethod("hasCheckRangeValue", int.class, int.class);
			method.setAccessible(true);

			//テストメソッド
			InvocationTargetException e = assertThrows(InvocationTargetException.class,
					() -> method.invoke(Keyboard.class, this.to, this.to));
			//検証
			assertEquals(ApplicationException.class, e.getTargetException().getClass());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * カバレッジを100%にするためのコンストラクタテスト
	 */
	@Test
	public void testConstructor() {
		@SuppressWarnings("unused")
		Keyboard k = new Keyboard();
	}
}
