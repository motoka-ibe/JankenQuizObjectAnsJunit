package jp.co.ginga.application.quiz;

import static org.junit.Assert.assertEquals;
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
import jp.co.ginga.util.keybord.Keybord;
import jp.co.ginga.util.properties.MessageProperties;

public class TestQuizCuiGameApplicationImpl {

	private List<QuizQuestion> list;
	private QuizCuiGameApplicationImpl application;
	QuizQuestion quiz;

	/**
	 * testAction_01 正常系
	 * public HumanJankenPlayerImpl(String playerName)
	 * 
	 * --確認事項--
	 * インスタンス生成時に渡された引数がフィールドに代入されているか
	 * --条件--
	 *	引数は文字列
	 * --検証項目--
	 * インスタンス生成時に渡した文字列とplayerNameフィールドの値が等しいこと
	 */
	//	@Test
	public void testAction_01() {
		try {
			list = new ArrayList<QuizQuestion>();
			for (int i = 0; i < 3; i++) {
				list.add(new QuizQuestion("タイトル", "本文", "選択肢", 1));
			}

			QuizQuestionFactory mockFactory = mock(QuizQuestionFactory.class);
			// 10を返すように設定
			when(mockFactory.createQuizQuestion()).thenReturn(list);

			QuizCuiGameApplicationImpl spyApplication = spy(QuizCuiGameApplicationImpl.class);
			doNothing().when(spyApplication).viewProblem(any());
			doNothing().when(spyApplication).judge(any());
			doNothing().when(spyApplication).viewResult();

			//検証
			spyApplication.action();

			verify(spyApplication, times(3)).viewProblem(any());
			verify(spyApplication, times(3)).judge(any());
			verify(spyApplication, times(1)).viewResult();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	//	@Test
	public void testAction_02() {
		try {
			list = new ArrayList<QuizQuestion>();

			QuizQuestionFactory mockFactory = spy(QuizQuestionFactory.class);
			// 10を返すように設定
			//			when(mockFactory.createQuizQuestion()).thenReturn(list);
			doReturn(list).when(mockFactory).createQuizQuestion();

			QuizCuiGameApplicationImpl spyApplication = spy(QuizCuiGameApplicationImpl.class);
			doNothing().when(spyApplication).viewResult();

			//検証
			spyApplication.action();

			verify(spyApplication, times(0)).viewProblem(any());
			verify(spyApplication, times(0)).judge(any());
			verify(spyApplication, times(1)).viewResult();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
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
			//検証
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
			//検証
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
	 *	引数quizのcorrectが2、かつKeybord.getInt(1, 3)の実行結果が2の場合
	 * --検証項目--
	 * correctCountが加算され、処理が正常終了すること
	 */
	@Test
	public void testJudge_02() {
		try (MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)) {
			mockKeybord.when(() -> Keybord.getInt(1, 3)).thenReturn(2);
			quiz = new QuizQuestion("タイトル", "本文", "選択肢", 2);
			application = new QuizCuiGameApplicationImpl();
			//テスト対象実行
			application.judge(quiz);
			//検証
			assertEquals(1, application.correctConut);
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
	 *	引数quizのcorrectが3、かつKeybord.getInt(1, 3)の実行結果が2の場合
	 * --検証項目--
	 * correctCountが0のまま、処理が正常終了すること
	 */
	@Test
	public void testJudge_03() {
		try (MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)) {
			mockKeybord.when(() -> Keybord.getInt(1, 3)).thenReturn(2);
			quiz = new QuizQuestion("タイトル", "本文", "選択肢", 1);
			application = new QuizCuiGameApplicationImpl();
			//テスト対象実行
			application.judge(quiz);
			//検証
			assertEquals(0, application.correctConut);
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
	 *	1回目のKeybord.getInt(1, 3)ではApplicationExceptionが発行される
	 *　2回目のKeybord.getInt(1, 3)では2が返却される
	 * --検証項目--
	 * correctCountが加算され、処理が正常終了すること
	 */
	@Test
	public void testJudge_04() {
		try (MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)) {
			mockKeybord.when(() -> Keybord.getInt(1, 3)).thenThrow(new ApplicationException("")).thenReturn(2);
			quiz = new QuizQuestion("タイトル", "本文", "選択肢", 2);
			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			application = new QuizCuiGameApplicationImpl();
			//テスト対象実行
			application.judge(quiz);
			//検証
			assertEquals(1, application.correctConut);
			assertEquals("選択肢から答えを入力してください\n" + System.lineSeparator()
					+ "範囲外の数値が入力されました。"
					+ System.lineSeparator()
					+ "選択肢から答えを入力してください\n" + System.lineSeparator(),
					out.toString());

			mockKeybord.verify(() -> Keybord.getInt(1, 3), times(2));

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
	 *	Keybord.getInt(1, 3)でSystemExceptionが発行される
	 * --検証項目--
	 * SystemExceptionが発行され、処理がループせずに終了すること
	 */
	@Test
	public void testJudge_05() {
		try (MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)) {
			mockKeybord.when(() -> Keybord.getInt(1, 3)).thenThrow(new SystemException("システムエラーが発生しました。終了します。"));
			quiz = new QuizQuestion("タイトル", "本文", "選択肢", 2);
			application = new QuizCuiGameApplicationImpl();
			//テスト対象実行
			application.judge(quiz);
			//検証
			assertEquals(0, application.correctConut);
			mockKeybord.verify(() -> Keybord.getInt(1, 3), times(1));
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
	 *	MessageProperties.getMessage("quiz.msg.correct" + this.correctConut)が正常に処理された場合
	 * --検証項目--
	 * 表示された内容が、想定と一致すること
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
	 *	MessageProperties.getMessage("quiz.msg.correct" + this.correctConut)がSystemExceptionを発行する場合
	 * --検証項目--
	 * SystemExceptionが発行されること
	 */
	@Test
	public void testViewResult_02() {
		try (MockedStatic<MessageProperties> mockMessageProperties = mockStatic(MessageProperties.class)) {
			mockMessageProperties.when(() -> MessageProperties.getMessage(anyString()))
					.thenThrow(new SystemException("システムエラーが発生しました。終了します。"));
			application = new QuizCuiGameApplicationImpl();
			//テスト対象実行
			application.viewResult();
			//検証
			mockMessageProperties.verify(() -> MessageProperties.getMessage(anyString()), times(1));

		} catch (SystemException e) {
			e.printStackTrace();
			//検証
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}
	}

}
