
package jp.co.ginga.main;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import jp.co.ginga.application.janken.JankenCuiGameApplicationImpl;
import jp.co.ginga.application.quiz.QuizCuiGameApplicationImpl;
import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.keybord.Keyboard;

/**
 * CUIゲーム クラス
 * @author isogai
 *
 */
public class TestCuiGame {

	//テストデータ
	private int quizGame = 1;
	private int jankenGame = 2;
	private int illegalValue = -1;

	/**
	 * testMain_01 正常系
	 * public static void main(String[] args)
	 * --確認事項--
	 * 整数の1を入力した場合
	 * QuizCuiGameApplicationImplが実行される
	 * --条件--
	 * getIntでは整数の1を入力するmock
	 * mock化したゲームインスタンスが生成される
	 * --検証項目--
	 * 1. getIntメソッドが1回呼び出されているか
	 * 2. actionメソッドが1回呼び出されているか
	 * 3. 処理が正常に終了すること(例外が発生しないこと)
	 */
	@Test
	public void testMain_01() {
		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class);
				MockedConstruction<QuizCuiGameApplicationImpl> mockQuizGame = mockConstruction(
						QuizCuiGameApplicationImpl.class,
						(quizGame, context) -> doNothing().when(quizGame).action())) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 2)).thenReturn(this.quizGame);

			//テストメソッド
			CuiGame.main(null);

			//検証
			mockKeyboard.verify(() -> Keyboard.getInt(1, 2), times(1));
			verify(mockQuizGame.constructed().get(0), times(1)).action();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testMain_02 異常系
	 * public static void main(String[] args)
	 * --確認事項--
	 * 整数の1を入力した場合
	 * QuizCuiGameApplicationImplが実行される
	 * actionメソッドからSystemExceptionが発生すると処理が終了する
	 * --条件--
	 * getIntでは一回目は1を入力するmock
	 * 生成されるゲームインスタンスはSystemExceptionが発生するmock
	 * --検証項目--
	 * 1. getIntメソッドが1回呼び出されているか
	 * 2. actionメソッドが1回呼び出されているか
	 * 3. エラーメッセージが出力され内容が正しいことを確認
	 */
	@Test
	public void testMain_02() {
		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class);
				MockedConstruction<QuizCuiGameApplicationImpl> mockQuizGame = mockConstruction(
						QuizCuiGameApplicationImpl.class,
						(jankenGame, context) -> doThrow(new SystemException(null)).when(jankenGame).action())) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 2)).thenReturn(this.quizGame);

			//System.setErrメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			CuiGame.main(null);

			//検証
			mockKeyboard.verify(() -> Keyboard.getInt(1, 2), times(1)); //getIntメソッドが1回呼び出されているか
			verify(mockQuizGame.constructed().get(0), times(1)).action(); //actionメソッドが1回呼び出されているか

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("システムエラーが発生しました。終了します。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testMain_03 異常系
	 * public static void main(String[] args)
	 * --確認事項--
	 * 整数の1を入力した場合
	 * QuizCuiGameApplicationImplが実行される
	 * actionメソッドからSystemExceptionが発生すると処理が終了する
	 * --条件--
	 * getIntでは一回目は1を入力するmock
	 * 生成されるゲームインスタンスはApplicationExceptionが発生するmock
	 * --検証項目--
	 * 1. getIntメソッドが1回呼び出されているか
	 * 2. actionメソッドが1回呼び出されているか
	 * 3. エラーメッセージが出力され内容が正しいことを確認
	 */
	@Test
	public void testMain_03() {
		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class);
				MockedConstruction<QuizCuiGameApplicationImpl> mockQuizGame = mockConstruction(
						QuizCuiGameApplicationImpl.class,
						(jankenGame, context) -> doThrow(new ApplicationException(null)).when(jankenGame).action())) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 2)).thenReturn(this.quizGame);

			//System.setErrメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			CuiGame.main(null);

			//検証
			mockKeyboard.verify(() -> Keyboard.getInt(1, 2), times(1)); //getIntメソッドが1回呼び出されているか
			verify(mockQuizGame.constructed().get(0), times(1)).action(); //actionメソッドが1回呼び出されているか

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("システムエラーが発生しました。終了します。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testMain_04 正常系
	 * public static void main(String[] args)
	 * --確認事項--
	 * 整数の1を入力した場合
	 * JankenCuiGameApplicationImplが実行される
	 * --条件--
	 * getIntでは整数の2を入力するmock
	 * mock化したゲームインスタンスが生成される
	 * --検証項目--
	 * 1. getIntメソッドが1回呼び出されているか
	 * 2. actionメソッドが1回呼び出されているか
	 * 3. 処理が正常に終了すること(例外が発生しないこと)
	 */
	@Test
	public void testMain_04() {
		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class);
				MockedConstruction<JankenCuiGameApplicationImpl> mockJankenGame = mockConstruction(
						JankenCuiGameApplicationImpl.class,
						(quizGame, context) -> doNothing().when(quizGame).action())) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 2)).thenReturn(this.jankenGame);

			//テストメソッド
			CuiGame.main(null);

			//検証
			mockKeyboard.verify(() -> Keyboard.getInt(1, 2), times(1));
			verify(mockJankenGame.constructed().get(0), times(1)).action();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testMain_05 異常系
	 * public static void main(String[] args)
	 * --確認事項--
	 * 整数の1を入力した場合
	 * JankenCuiGameApplicationImplが実行される
	 * actionメソッドからSystemExceptionが発生すると処理が終了する
	 * --条件--
	 * getIntでは一回目は1を入力するmock
	 * 生成されるゲームインスタンスはApplicationExceptionが発生するmock
	 * --検証項目--
	 * 1. getIntメソッドが1回呼び出されているか
	 * 2. actionメソッドが1回呼び出されているか
	 * 3. エラーメッセージが出力され内容が正しいことを確認
	 */
	@Test
	public void testMain_05() {
		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class);
				MockedConstruction<JankenCuiGameApplicationImpl> mockJankenGame = mockConstruction(
						JankenCuiGameApplicationImpl.class,
						(jankenGame, context) -> doThrow(new SystemException(null)).when(jankenGame).action())) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 2)).thenReturn(this.jankenGame);

			//System.setErrメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			CuiGame.main(null);

			//検証
			mockKeyboard.verify(() -> Keyboard.getInt(1, 2), times(1)); //getIntメソッドが1回呼び出されているか
			verify(mockJankenGame.constructed().get(0), times(1)).action(); //actionメソッドが1回呼び出されているか

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("システムエラーが発生しました。終了します。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testMain_06 異常系
	 * public static void main(String[] args)
	 * --確認事項--
	 * 整数の1を入力した場合
	 * JankenCuiGameApplicationImplが実行される
	 * actionメソッドからApplicationExceptionが発生すると処理が終了する
	 * --条件--
	 * getIntでは一回目は1を入力するmock
	 * 生成されるゲームインスタンスはApplicationExceptionが発生するmock
	 * --検証項目--
	 * 1. getIntメソッドが1回呼び出されているか
	 * 2. actionメソッドが1回呼び出されているか
	 * 3. エラーメッセージが出力され内容が正しいことを確認
	 */
	@Test
	public void testMain_06() {
		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class);
				MockedConstruction<JankenCuiGameApplicationImpl> mockJankenGame = mockConstruction(
						JankenCuiGameApplicationImpl.class,
						(jankenGame, context) -> doThrow(new ApplicationException(null)).when(jankenGame).action())) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 2)).thenReturn(this.jankenGame);

			//System.setErrメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			CuiGame.main(null);

			//検証
			mockKeyboard.verify(() -> Keyboard.getInt(1, 2), times(1)); //getIntメソッドが1回呼び出されているか
			verify(mockJankenGame.constructed().get(0), times(1)).action(); //actionメソッドが1回呼び出されているか

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("システムエラーが発生しました。終了します。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testMain_07 異常系
	 * public static void main(String[] args)
	 * --確認事項--
	 * 整数の1,2以外を入力した場合
	 * 再入力を促すメッセージを出力し、処理を繰り返す。
	 * --条件--
	 * getIntでは整数の1,2以外を入力するので、ApplicationExceptionが発生し、2回目で1を入力するmock
	 * --検証項目--
	 * 1. getIntメソッドが2回呼び出されているか
	 * 2. actionメソッドが1回呼び出されているか
	 * 3. エラーメッセージが出力され内容が正しいことを確認
	 */
	@Test
	public void testMain_07() {
		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class);
				MockedConstruction<QuizCuiGameApplicationImpl> mockQuizGame = mockConstruction(
						QuizCuiGameApplicationImpl.class,
						(quizGame, context) -> doNothing().when(quizGame).action())) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 2)).thenThrow(new ApplicationException(""))
					.thenReturn(this.quizGame);

			//System.setErrメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			CuiGame.main(null);

			//検証
			mockKeyboard.verify(() -> Keyboard.getInt(1, 2), times(2)); //getIntメソッドが1回呼び出されているか
			verify(mockQuizGame.constructed().get(0), times(1)).action(); //actionメソッドが1回呼び出されているか

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("入力した値が不正です。再入力をお願いします。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testMain_08 異常系
	 * public static void main(String[] args)
	 * --確認事項--
	 * 整数の1,2以外を入力した場合に
	 * 再入力を促すメッセージが出ず、整数の1,2以外の引数が入ってしまった場合
	 * --条件--
	 * getIntにillegalValueを入力するmock
	 * --検証項目--
	 * 1. getIntメソッドが1回呼び出されているか
	 * 2. actionメソッドが1回呼び出されているか
	 * 3. ゲームインスタンスが生成されていないことを確認
	 */
	@Test
	public void testMain_08() {
		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class);
				MockedConstruction<JankenCuiGameApplicationImpl> mockJankenGame = mockConstruction(
						JankenCuiGameApplicationImpl.class,
						(jankenGame, context) -> doThrow(new Exception()).when(jankenGame).action());
				MockedConstruction<QuizCuiGameApplicationImpl> mockQuizGame = mockConstruction(
						QuizCuiGameApplicationImpl.class,
						(quizGame, context) -> doNothing().when(quizGame).action())) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 2)).thenReturn(this.illegalValue);

			//System.setErrメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			CuiGame.main(null);

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("キーボード接続エラーです。ゲームを終了します。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			//検証
			assertTrue(findMessage);// メッセージが見つかったことを確認
			mockKeyboard.verify(() -> Keyboard.getInt(1, 2), times(1)); //getIntメソッドが1回呼び出されているか
			assertEquals(0, mockQuizGame.constructed().size()); //ゲームインスタンスが生成されていないことを確認
			assertEquals(0, mockJankenGame.constructed().size()); //ゲームインスタンスが生成されていないことを確認

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * カバレッジを100%にするためのコンストラクタテスト
	 */
	@Test
	public void testConstructor_01() {
		@SuppressWarnings("unused") //未使用の変数やメソッドに関するコンパイラの警告を抑制するアノテーション
		CuiGame c = new CuiGame();
	}

}
