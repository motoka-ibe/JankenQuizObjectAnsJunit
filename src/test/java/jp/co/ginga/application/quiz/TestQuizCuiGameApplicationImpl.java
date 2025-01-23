package jp.co.ginga.application.quiz;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import jp.co.ginga.application.quiz.factory.QuizQuestionFactory;
import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.keyboard.Keyboard;
import jp.co.ginga.util.properties.MessageProperties;

/**
 * CUIクイズ実装クラス
 * @author Inoue / Isogai
 *
 */
public class TestQuizCuiGameApplicationImpl {

	private List<QuizQuestion> list;
	private QuizCuiGameApplicationImpl application;
	QuizQuestion quiz;

	/**
	 * testAction_01 正常系
	 * public void action() throws SystemException 
	 * 
	 * --確認事項--
	 * QuizQuestionFactoryのcreateQuizQuestion()の戻り値のsizeが3の場合
	 * 処理が正常終了すること
	 * --条件--
	 *	QuizQuestionFactoryのcreateQuizQuestion()の戻り値のsizeが3の場合
	* --検証項目--
	* 1. createQuizQuestion()が返すリストのサイズが3であること
	* 2. リストの各QuizQuestionオブジェクトのタイトル、本文、選択肢、正解が期待通りであること
	* 3. viewProblem()が3回呼び出されること
	* 4. judge()が3回呼び出されること
	* 5. viewResult()が1回呼び出されること
	* 6. 例外が発生せず、正常終了すること
	* 7.「昭和クイズゲームを開始します。」と「これで昭和クイズゲームは終了です。」が出力されていること。
	 */
	@Test
	public void testAction_01() {
		try {
			list = new ArrayList<QuizQuestion>();
			for (int i = 0; i < 3; i++) {
				list.add(new QuizQuestion("タイトル", "本文", "選択肢", 1));
			}
			QuizQuestionFactory mockFactory = mock(QuizQuestionFactory.class);

			when(mockFactory.createQuizQuestion()).thenReturn(list);
			QuizCuiGameApplicationImpl spyApplication = spy(QuizCuiGameApplicationImpl.class);
			doNothing().when(spyApplication).viewProblem(any());
			doNothing().when(spyApplication).judge(any());
			doNothing().when(spyApplication).viewResult();
			spyApplication.factory = mockFactory;

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッドの実行
			spyApplication.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findStartMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("昭和クイズゲームを開始します。\n")) {
					findStartMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			boolean findEndMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("これで昭和クイズゲームは終了です。\n")) {
					findEndMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findStartMessage);
			assertTrue(findEndMessage);

			//検証
			assertEquals(3, spyApplication.list.size());
			for (int i = 0; i < list.size(); i++) {
				assertEquals("タイトル", spyApplication.list.get(i).getTitle());
				assertEquals("本文", spyApplication.list.get(i).getBody());
				assertEquals(1, spyApplication.list.get(i).getCorrect());
			}
			verify(spyApplication, times(3)).viewProblem(any());
			verify(spyApplication, times(3)).judge(any());
			verify(spyApplication, times(1)).viewResult();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testAction_02 正常系
	 * public void action() throws SystemException 
	 * 
	 *  --確認事項--
	 * QuizQuestionFactoryのcreateQuizQuestion()の戻り値のsizeが0の場合
	 * 処理が正常終了すること
	 * --条件--
	 *	QuizQuestionFactoryのcreateQuizQuestion()の戻り値のsizeが0の場合
	 * --検証項目--
	 * 1. createQuizQuestion()が返すリストのサイズが0であること
	 * 2. QuizCuiGameApplicationImplのviewProblem()が0回呼び出されること
	 * 3. judge()が0回呼び出されること
	 * 4. viewResult()が1回呼び出されること
	 * 5. 例外が発生せず、正常終了すること
	 * 7.「昭和クイズゲームを開始します。」と「これで昭和クイズゲームは終了です。」が出力されていること。
	 */
	@Test
	public void testAction_02() {
		try {
			list = new ArrayList<QuizQuestion>();

			QuizQuestionFactory mockFactory = mock(QuizQuestionFactory.class);

			when(mockFactory.createQuizQuestion()).thenReturn(list);
			QuizCuiGameApplicationImpl spyApplication = spy(QuizCuiGameApplicationImpl.class);
			doNothing().when(spyApplication).viewProblem(any());
			doNothing().when(spyApplication).judge(any());
			doNothing().when(spyApplication).viewResult();
			spyApplication.factory = mockFactory;

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッドの実行
			spyApplication.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findStartMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("昭和クイズゲームを開始します。\n")) {
					findStartMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			boolean findEndMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("これで昭和クイズゲームは終了です。\n")) {
					findEndMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findStartMessage);
			assertTrue(findEndMessage);

			//検証
			assertEquals(0, spyApplication.list.size());
			verify(spyApplication, times(0)).viewProblem(any());
			verify(spyApplication, times(0)).judge(any());
			verify(spyApplication, times(1)).viewResult();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testAction_03 異常系
	 * public void action() throws SystemException 
	 * 
	 * --確認事項--
	 * QuizQuestionFactoryのcreateQuizQuestion()の戻り値がnullの場合
	 * SystemException(実行データ不良)が発行される
	 * --条件--
	 *	QuizQuestionFactoryのcreateQuizQuestion()の戻り値がnullの場合
	 * --検証項目--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2..SystemExceptionのメッセージ内容が正しいこと
	 */
	@Test
	public void testAction_03() {
		try {
			list = null;

			QuizQuestionFactory mockFactory = mock(QuizQuestionFactory.class);

			when(mockFactory.createQuizQuestion()).thenReturn(list);
			QuizCuiGameApplicationImpl spyApplication = spy(QuizCuiGameApplicationImpl.class);
			doNothing().when(spyApplication).viewProblem(any());
			doNothing().when(spyApplication).judge(any());
			doNothing().when(spyApplication).viewResult();
			spyApplication.factory = mockFactory;

			//テストメソッドの実行
			spyApplication.action();
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg()); //検証
		}
	}

	/**
	 * testViewProblem_01 正常系
	 * void viewProblem(final QuizQuestion quiz)
	 * 
	 * --確認事項--
	 * quizオブジェクトの内容を表示できるか
	 * --条件--
	 *	引数はquizオブジクェト
	 * --検証項目--
	 * 表示された内容が、想定と一致すること
	 */
	@Test
	public void testViewProblem_01() {
		try {
			quiz = new QuizQuestion("タイトル", "本文", "選択肢", 1);
			application = new QuizCuiGameApplicationImpl();
			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テスト対象実行
			application.viewProblem(quiz);

			//検証
			assertEquals("タイトル" + System.lineSeparator()
					+ "本文" + System.lineSeparator()
					+ "選択肢" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testViewProblem_02 異常系
	 * void viewProblem(final QuizQuestion quiz)
	 * 
	 * --確認事項--
	 * 引数がnullの場合、SystemExceptionが発行されること
	 * --条件--
	 *	引数はnull
	 * --検証項目--
	 * SystemExceptionが発行されること
	 */
	@Test
	public void testViewProblem_02() {
		try {
			application = new QuizCuiGameApplicationImpl();

			//テスト対象実行
			application.viewProblem(null);
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg()); //検証
		}
	}

	/**
	 * testJudge_01 異常系
	 * void judge(final QuizQuestion quiz) 
	 * 
	 * --確認事項--
	 * 引数がnullの場合、SystemExceptionが発行されること
	 * --条件--
	 *	引数はnull
	 * --検証項目--
	 * SystemExceptionが発行されること
	 */
	@Test
	public void testJudge_01() {
		try {
			application = new QuizCuiGameApplicationImpl();

			//テスト対象実行
			application.judge(null);
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg()); //検証
		}
	}

	/**
	 * testJudge_02 正常系
	 * void judge(final QuizQuestion quiz) 
	 * 
	 * --確認事項--
	 * correctが正しく判断されて、加算されること
	 * --条件--
	 *	引数quizのcorrectが2、かつKeyboard.getInt(1, 3)の実行結果が2の場合
	 * --検証項目--
	 * correctCountが加算され、処理が正常終了すること
	 */
	@Test
	public void testJudge_02() {
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 3)).thenReturn(2);
			quiz = new QuizQuestion("タイトル", "本文", "選択肢", 2);
			application = new QuizCuiGameApplicationImpl();

			//テスト対象実行
			application.judge(quiz);

			//検証
			assertEquals(1, application.correctCount);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testJudge_03 正常系
	 * void judge(final QuizQuestion quiz) 
	 * 
	 * --確認事項--
	 * correctが正しく判断されて、加算されないこと
	 * --条件--
	 *	引数quizのcorrectが1、かつKeyboard.getInt(1, 3)の実行結果が2の場合
	 * --検証項目--
	 * correctCountが0のまま、処理が正常終了すること
	 */
	@Test
	public void testJudge_03() {
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 3)).thenReturn(2);
			quiz = new QuizQuestion("タイトル", "本文", "選択肢", 1);
			application = new QuizCuiGameApplicationImpl();

			//テスト対象実行
			application.judge(quiz);

			//検証
			assertEquals(0, application.correctCount);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testJudge_04 正常系
	 * void judge(final QuizQuestion quiz) 
	 * 
	 * --確認事項--
	 * correctが正しく判断されて、加算されないこと
	 * --条件--
	 *	1回目のKeyboard.getInt(1, 3)ではApplicationExceptionが発行される
	 *　2回目のKeyboard.getInt(1, 3)では2が返却される
	 * --検証項目--
	 * correctCountが加算され、処理が正常終了すること
	 */
	@Test
	public void testJudge_04() {
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 3)).thenThrow(new ApplicationException("")).thenReturn(2);
			quiz = new QuizQuestion("タイトル", "本文", "選択肢", 2);
			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			application = new QuizCuiGameApplicationImpl();

			//テスト対象実行
			application.judge(quiz);

			//検証
			assertEquals(1, application.correctCount);
			assertEquals("選択肢から答えを入力してください\n" + System.lineSeparator()
					+ "範囲外の数値が入力されました。"
					+ System.lineSeparator()
					+ "選択肢から答えを入力してください\n" + System.lineSeparator(),
					out.toString());

			mockKeyboard.verify(() -> Keyboard.getInt(1, 3), times(2));

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testJudge_05 異常系
	 * void judge(final QuizQuestion quiz) 
	 * 
	 * --確認事項--
	 * SystemExceptionが発行され、処理がループせずに終了すること
	 * --条件--
	 *	Keyboard.getInt(1, 3)でSystemExceptionが発行される
	 * --検証項目--
	 * SystemExceptionが発行され、処理がループせずに終了すること
	 */
	@Test
	public void testJudge_05() {
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			mockKeyboard.when(() -> Keyboard.getInt(1, 3)).thenThrow(new SystemException("システムエラーが発生しました。終了します。"));
			quiz = new QuizQuestion("タイトル", "本文", "選択肢", 2);
			application = new QuizCuiGameApplicationImpl();

			//テスト対象実行
			application.judge(quiz);

			//検証
			assertEquals(0, application.correctCount);
			mockKeyboard.verify(() -> Keyboard.getInt(1, 3), times(1));
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			//検証
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}
	}

	/**
	 * testViewResult_01 正常系
	 * void viewResult()
	 * 
	 * --確認事項--
	 * 結果表示ができるか
	 * --条件--
	 *	MessageProperties.getMessage("quiz.msg.correct" + this.correctCount)が正常に処理された場合
	 * --検証項目--
	 * 処理が正常に終了すること
	 * getMessageが一回呼び出されること
	 */
	@Test
	public void testViewResult_01() {
		try (MockedStatic<MessageProperties> mockMessageProperties = mockStatic(MessageProperties.class)) {
			mockMessageProperties.when(() -> MessageProperties.getMessage(anyString())).thenReturn("2問正解");
			application = new QuizCuiGameApplicationImpl();

			//テスト対象実行
			application.viewResult();

			//検証
			mockMessageProperties.verify(() -> MessageProperties.getMessage(anyString()), times(1));

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testViewResult_02 異常系
	 * void viewResult()
	 * 
	 * --確認事項--
	 * SystemExceptionが発行されるか
	 * --条件--
	 *	MessageProperties.getMessage("quiz.msg.correct" + this.correctCount)がSystemExceptionを発行する場合
	 * --検証項目--
	 * SystemExceptionが発行されること
	 * メッセージ「システムエラーが発生しました。終了します。」が出力されること。
	 */
	@Test
	public void testViewResult_02() {
		try (MockedStatic<MessageProperties> mockMessageProperties = mockStatic(MessageProperties.class)) {
			mockMessageProperties.when(() -> MessageProperties.getMessage(anyString()))
					.thenThrow(new SystemException("システムエラーが発生しました。終了します。"));
			application = new QuizCuiGameApplicationImpl();

			//テスト対象実行
			application.viewResult();
			fail();

			//検証
			mockMessageProperties.verify(() -> MessageProperties.getMessage(anyString()), times(1));

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());//検証
		}
	}

	/**
	 * testViewResult_03 正常系
	 * void viewResult()
	 * 
	 * --確認事項--
	 * 結果表示ができるか
	 * --条件--
	 *	MessageProperties.getMessage("quiz.msg.correct" + this.correctCount)が正常に処理された場合
	 *  corectCount0の場合
	 * --検証項目--
	 * 表示された内容が、想定と一致すること
	 * 「もう少し昭和に興味をもってください」が出力されること
	 */
	@Test
	public void testViewResult_03() {
		try {
			application = new QuizCuiGameApplicationImpl();
			application.correctCount = 0;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テスト対象実行
			application.viewResult();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("もう少し昭和に興味をもってください\n")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testViewResult_04 正常系
	 * void viewResult()
	 * 
	 * --確認事項--
	 * 結果表示ができるか
	 * --条件--
	 *	MessageProperties.getMessage("quiz.msg.correct" + this.correctCount)が正常に処理された場合
	 *  corectCount01の場合
	 * --検証項目--
	 * 表示された内容が、想定と一致すること
	 * 「1問正解です。もし難しいようなら、お父さん、お母さんに聞いてみたら\n」が出力されること
	 */
	@Test
	public void testViewResult_04() {
		try {
			application = new QuizCuiGameApplicationImpl();
			application.correctCount = 1;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テスト対象実行
			application.viewResult();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("1問正解です。もし難しいようなら、お父さん、お母さんに聞いてみたら\n")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testViewResult_05 正常系
	 * void viewResult()
	 * 
	 * --確認事項--
	 * 結果表示ができるか
	 * --条件--
	 *	MessageProperties.getMessage("quiz.msg.correct" + this.correctCount)が正常に処理された場合
	 *  corectCount01の場合
	 * --検証項目--
	 * 表示された内容が、想定と一致すること
	 * 「2問正解です。あと少しでエキスパートです。頑張ってください\n」が出力されること
	 */
	@Test
	public void testViewResult_05() {
		try {
			application = new QuizCuiGameApplicationImpl();
			application.correctCount = 2;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テスト対象実行
			application.viewResult();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("2問正解です。あと少しでエキスパートです。頑張ってください\n")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testViewResult_06 正常系
	 * void viewResult()
	 * 
	 * --確認事項--
	 * 結果表示ができるか
	 * --条件--
	 *	MessageProperties.getMessage("quiz.msg.correct" + this.correctCount)が正常に処理された場合
	 *  corectCount01の場合
	 * --検証項目--
	 * 表示された内容が、想定と一致すること
	 * 「全問正解です。昭和のエキスパートですね\n」が出力されること
	 */
	@Test
	public void testViewResult_06() {
		try {
			application = new QuizCuiGameApplicationImpl();
			application.correctCount = 3;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テスト対象実行
			application.viewResult();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("全問正解です。昭和のエキスパートですね\n")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

}
