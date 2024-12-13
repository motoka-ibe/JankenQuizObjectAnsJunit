package jp.co.ginga.application.quiz.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.MockedStatic;

import jp.co.ginga.application.quiz.QuizQuestion;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.properties.MessageProperties;

/**
 * クイズ製造 クラス
 * @author Isogai
 *
 */
public class TestQuizQuestionFactory {

	/**
	 * testCreateQuizQuestion_01() 正常系
	 * public List<QuizQuestion> createQuizQuestion() throws SystemException
	 * 
	 * --確認事項-- 
	 * mockで引数を指定してメソッドを実行
	 * listの要素にすべて値が入り、String型はnull,int型は0出ないことを確認
	 * --条件--
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「3」の場合
	 * MessageProperties.getMessage("quiz.question.title" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.body" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.choice" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「3」の場合
	 * --検証項目--
	 * 1.listが返却され、listのsizeが3であること
	 * 2.listの要素がすべて値が設定されていること(nullと0でないこと)
	 */
	@Test
	public void testCreateQuizQuestion_01() throws SystemException {

		try (MockedStatic<MessageProperties> mocMessageProperties = mockStatic(MessageProperties.class)) {

			//anyString()を引数に応じて返せるように、anyInt()、anyString()などでデフォルト値を設定してあげる
			mocMessageProperties.when(() -> MessageProperties.getMessage(anyString())).thenReturn("");

			//MessageProperties.getMessage("quiz.number.questions")にthenReturnで3を送る
			mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions")).thenReturn("3");

			for (int i = 1; i <= 3; i++) {
				final int index = i; // ラムダ式内で使うために最終的な変数を作成
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.title" + index))
						.thenReturn("title" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.body" + index))
						.thenReturn("body" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.choice" + index))
						.thenReturn("choice" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.correct" + index))
						.thenReturn(String.valueOf(index));
			}

			//QuizQuestionFactoryクラスのインスタンス生成
			QuizQuestionFactory quizQuestionFactory = new QuizQuestionFactory();

			//テストメソッドの実行
			List<QuizQuestion> list = quizQuestionFactory.createQuizQuestion();
			// Assert
			assertEquals(3, list.size()); //istのsizeが3であることを確認

			for (QuizQuestion question : list) {
				assertNotNull(question.getTitle()); //null出ないことを確認
				assertNotNull(question.getBody()); //null出ないことを確認
				assertNotNull(question.getChoice()); //null出ないことを確認
				assertNotEquals(0, question.getCorrect()); //0でないことを確認
			}
		}
	}

	/**
	 * testCreateQuizQuestion_02() 正常系
	 * public List<QuizQuestion> createQuizQuestion() throws SystemException
	 * 
	 * --確認事項-- 
	 * mockで引数を指定してメソッドを実行
	 * listの要素にすべて値が入り、String型はnull,int型は0であること確認
	 * --条件--
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「3」の場合
	 * MessageProperties.getMessage("quiz.question.title" + (i + 1))の実行結果がnullの場合
	 * MessageProperties.getMessage("quiz.question.body" + (i + 1))の実行結果がnullの場合
	 * MessageProperties.getMessage("quiz.question.choice" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「0」の場合
	 * --検証項目--
	 * 1.listが返却され、listのsizeが3であること
	 * 2.listの要素がすべて値が設定されていること(nullか0であること)
	 */
	@Test
	public void testCreateQuizQuestion_02() throws SystemException {

		try (MockedStatic<MessageProperties> mocMessageProperties = mockStatic(MessageProperties.class)) {

			//anyString()を引数に応じて返せるように、anyInt() など、anyString()などでデフォルト値を設定してあげる
			mocMessageProperties.when(() -> MessageProperties.getMessage(anyString())).thenReturn("");

			//MessageProperties.getMessage("quiz.number.questions")にthenReturnで3を送る
			mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions")).thenReturn("3");

			for (int i = 1; i <= 3; i++) {
				final int index = i; // ラムダ式内で使うために最終的な変数を作成
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.title" + index))
						.thenReturn(null);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.body" + index))
						.thenReturn(null);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.choice" + index))
						.thenReturn(null);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.correct" + index))
						.thenReturn(String.valueOf(0));
			}

			//QuizQuestionFactoryクラスのインスタンス生成
			QuizQuestionFactory quizQuestionFactory = new QuizQuestionFactory();

			//テストメソッドの実行
			List<QuizQuestion> list = quizQuestionFactory.createQuizQuestion();
			// Assert
			assertEquals(3, list.size()); //istのsizeが3であることを確認

			for (QuizQuestion question : list) {
				assertNull(question.getTitle()); //nullであることを確認
				assertNull(question.getBody()); //nullであることを確認
				assertNull(question.getChoice()); //nullであることを確認
				assertEquals(0, question.getCorrect()); //0であることを確認
			}
		}
	}

	/**
	 * testCreateQuizQuestion_03() 正常系
	 * public List<QuizQuestion> createQuizQuestion() throws SystemException
	 * 
	 * --確認事項-- 
	 * mockで引数を指定してメソッドを実行
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「0」の場合
	 * listのsizeが0であること
	 * --条件--
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「0」の場合
	 * MessageProperties.getMessage("quiz.question.title" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.body" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.choice" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「3」の場合
	 * --検証項目--
	 * 1.listが返却され、listのsizeが0であること
	 */
	@Test
	public void testCreateQuizQuestion_03() throws SystemException {

		try (MockedStatic<MessageProperties> mocMessageProperties = mockStatic(MessageProperties.class)) {

			//anyString()を引数に応じて返せるように、anyInt()、anyString()などでデフォルト値を設定してあげる
			mocMessageProperties.when(() -> MessageProperties.getMessage(anyString())).thenReturn("");

			//MessageProperties.getMessage("quiz.number.questions")にthenReturnで3を送る
			mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions")).thenReturn("0");

			for (int i = 1; i <= 3; i++) {
				final int index = i; // ラムダ式内で使うために最終的な変数を作成
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.title" + index))
						.thenReturn("title" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.body" + index))
						.thenReturn("body" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.choice" + index))
						.thenReturn("choice" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.correct" + index))
						.thenReturn(String.valueOf(index));
			}

			//QuizQuestionFactoryクラスのインスタンス生成
			QuizQuestionFactory quizQuestionFactory = new QuizQuestionFactory();

			//テストメソッドの実行
			List<QuizQuestion> list = quizQuestionFactory.createQuizQuestion();
			// Assert
			assertEquals(0, list.size()); //istのsizeが3であることを確認

		}
	}

	/**
	 * testCreateQuizQuestion_04() 異常系
	 * public List<QuizQuestion> createQuizQuestion() throws SystemException
	 * 
	 * --確認事項-- 
	 * mockで引数を指定してメソッドを実行
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「あいう」の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「あいう」の場合
	 * MessageProperties.getMessage("quiz.question.title" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.body" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.choice" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「3」の場合
	 * --検証項目--
	 * SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testCreateQuizQuestion_04() throws SystemException {

		try (MockedStatic<MessageProperties> mocMessageProperties = mockStatic(MessageProperties.class)) {

			//anyString()を引数に応じて返せるように、anyInt()、anyString()などでデフォルト値を設定してあげる
			mocMessageProperties.when(() -> MessageProperties.getMessage(anyString())).thenReturn("");

			//MessageProperties.getMessage("quiz.number.questions")にthenReturnで3を送る
			mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions")).thenReturn("あいう");

			for (int i = 1; i <= 3; i++) {
				final int index = i; // ラムダ式内で使うために最終的な変数を作成
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.title" + index))
						.thenReturn("title" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.body" + index))
						.thenReturn("body" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.choice" + index))
						.thenReturn("choice" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.correct" + index))
						.thenReturn(String.valueOf(index));
			}

			//QuizQuestionFactoryクラスのインスタンス生成
			QuizQuestionFactory quizQuestionFactory = new QuizQuestionFactory();
			try {
				//テストメソッドの実行
				quizQuestionFactory.createQuizQuestion();
				fail();

			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * testCreateQuizQuestion_05() 異常系
	 * public List<QuizQuestion> createQuizQuestion() throws SystemException
	 * 
	 * --確認事項-- 
	 * mockで引数を指定してメソッドを実行
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「」の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「」の場合
	 * MessageProperties.getMessage("quiz.question.title" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.body" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.choice" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「3」の場合
	 * --検証項目--
	 * SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testCreateQuizQuestion_05() throws SystemException {

		try (MockedStatic<MessageProperties> mocMessageProperties = mockStatic(MessageProperties.class)) {

			//anyString()を引数に応じて返せるように、anyInt()、anyString()などでデフォルト値を設定してあげる
			mocMessageProperties.when(() -> MessageProperties.getMessage(anyString())).thenReturn("");

			//MessageProperties.getMessage("quiz.number.questions")にthenReturnで3を送る
			mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions")).thenReturn("");

			for (int i = 1; i <= 3; i++) {
				final int index = i; // ラムダ式内で使うために最終的な変数を作成
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.title" + index))
						.thenReturn("title" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.body" + index))
						.thenReturn("body" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.choice" + index))
						.thenReturn("choice" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.correct" + index))
						.thenReturn(String.valueOf(index));
			}

			//QuizQuestionFactoryクラスのインスタンス生成
			QuizQuestionFactory quizQuestionFactory = new QuizQuestionFactory();
			try {
				//テストメソッドの実行
				quizQuestionFactory.createQuizQuestion();
				fail();

			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * testCreateQuizQuestion_06() 異常系
	 * public List<QuizQuestion> createQuizQuestion() throws SystemException
	 * 
	 * --確認事項-- 
	 * mockで引数を指定してメソッドを実行
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果がSystemExceptionの場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果がSystemExceptionの場合
	 * MessageProperties.getMessage("quiz.question.title" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.body" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.choice" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「3」の場合
	 * --検証項目--
	 * SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testCreateQuizQuestion_06() throws SystemException {

		try (MockedStatic<MessageProperties> mocMessageProperties = mockStatic(MessageProperties.class)) {

			//anyString()を引数に応じて返せるように、anyInt()、anyString()などでデフォルト値を設定してあげる
			mocMessageProperties.when(() -> MessageProperties.getMessage(anyString())).thenReturn("");

			//MessageProperties.getMessage("quiz.number.questions")にthenReturnで3を送る
			mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions"))
					.thenThrow(new SystemException(""));

			for (int i = 1; i <= 3; i++) {
				final int index = i; // ラムダ式内で使うために最終的な変数を作成
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.title" + index))
						.thenReturn("title" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.body" + index))
						.thenReturn("body" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.choice" + index))
						.thenReturn("choice" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.correct" + index))
						.thenReturn(String.valueOf(index));
			}

			//QuizQuestionFactoryクラスのインスタンス生成
			QuizQuestionFactory quizQuestionFactory = new QuizQuestionFactory();
			try {
				//テストメソッドの実行
				quizQuestionFactory.createQuizQuestion();
				fail();

			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * testCreateQuizQuestion_07() 正常系
	 * public List<QuizQuestion> createQuizQuestion() throws SystemException
	 * 
	 * --確認事項-- 
	 * mockで引数を指定してメソッドを実行
	 * MessageProperties.getMessage("quiz.question.title" + (i + 1))の実行結果がSystemExceptionの場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「3」の場合
	 * MessageProperties.getMessage("quiz.question.title" + (i + 1))の実行結果がSystemExceptionの場合
	 * MessageProperties.getMessage("quiz.question.body" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.choice" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「3」の場合
	 * --検証項目--
	 * SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testCreateQuizQuestion_07() throws SystemException {

		try (MockedStatic<MessageProperties> mocMessageProperties = mockStatic(MessageProperties.class)) {

			//anyString()を引数に応じて返せるように、anyInt()、anyString()などでデフォルト値を設定してあげる
			mocMessageProperties.when(() -> MessageProperties.getMessage(anyString())).thenReturn("");

			//MessageProperties.getMessage("quiz.number.questions")にthenReturnで3を送る
			mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions")).thenReturn("3");

			for (int i = 1; i <= 3; i++) {
				final int index = i; // ラムダ式内で使うために最終的な変数を作成
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.title" + index))
						.thenThrow(new SystemException(""));
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.body" + index))
						.thenReturn("body" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.choice" + index))
						.thenReturn("choice" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.correct" + index))
						.thenReturn(String.valueOf(index));
			}

			//QuizQuestionFactoryクラスのインスタンス生成
			QuizQuestionFactory quizQuestionFactory = new QuizQuestionFactory();
			try {
				//テストメソッドの実行
				quizQuestionFactory.createQuizQuestion();
				fail();

			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * testCreateQuizQuestion_08() 正常系
	 * public List<QuizQuestion> createQuizQuestion() throws SystemException
	 * 
	 * --確認事項-- 
	 * mockで引数を指定してメソッドを実行
	 * MessageProperties.getMessage("quiz.question.body" + (i + 1))の実行結果がSystemExceptionの場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「3」の場合
	 * MessageProperties.getMessage("quiz.question.title" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.body" + (i + 1))の実行結果がSystemExceptionの場合
	 * MessageProperties.getMessage("quiz.question.choice" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「3」の場合
	 * --検証項目--
	 * SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testCreateQuizQuestion_08() throws SystemException {

		try (MockedStatic<MessageProperties> mocMessageProperties = mockStatic(MessageProperties.class)) {

			//anyString()を引数に応じて返せるように、anyInt()、anyString()などでデフォルト値を設定してあげる
			mocMessageProperties.when(() -> MessageProperties.getMessage(anyString())).thenReturn("");

			//MessageProperties.getMessage("quiz.number.questions")にthenReturnで3を送る
			mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions")).thenReturn("3");

			for (int i = 1; i <= 3; i++) {
				final int index = i; // ラムダ式内で使うために最終的な変数を作成
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.title" + index))
						.thenReturn("title" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.body" + index))
						.thenThrow(new SystemException(""));
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.choice" + index))
						.thenReturn("choice" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.correct" + index))
						.thenReturn(String.valueOf(index));
			}

			//QuizQuestionFactoryクラスのインスタンス生成
			QuizQuestionFactory quizQuestionFactory = new QuizQuestionFactory();
			try {
				//テストメソッドの実行
				quizQuestionFactory.createQuizQuestion();
				fail();

			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * testCreateQuizQuestion_09() 正常系
	 * public List<QuizQuestion> createQuizQuestion() throws SystemException
	 * 
	 * --確認事項-- 
	 * mockで引数を指定してメソッドを実行
	 * MessageProperties.getMessage("quiz.question.choice" + (i + 1))の実行結果がSystemExceptionの場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「3」の場合
	 * MessageProperties.getMessage("quiz.question.title" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.body" + (i + 1))の実行結果がSystemExceptionの場合
	 * MessageProperties.getMessage("quiz.question.choice" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「3」の場合
	 * --検証項目--
	 * SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testCreateQuizQuestion_09() throws SystemException {

		try (MockedStatic<MessageProperties> mocMessageProperties = mockStatic(MessageProperties.class)) {

			//anyString()を引数に応じて返せるように、anyInt()、anyString()などでデフォルト値を設定してあげる
			mocMessageProperties.when(() -> MessageProperties.getMessage(anyString())).thenReturn("");

			//MessageProperties.getMessage("quiz.number.questions")にthenReturnで3を送る
			mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions")).thenReturn("3");

			for (int i = 1; i <= 3; i++) {
				final int index = i; // ラムダ式内で使うために最終的な変数を作成
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.title" + index))
						.thenReturn("title" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.body" + index))
						.thenReturn("body" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.choice" + index))
						.thenThrow(new SystemException(""));
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.correct" + index))
						.thenReturn(String.valueOf(index));
			}

			//QuizQuestionFactoryクラスのインスタンス生成
			QuizQuestionFactory quizQuestionFactory = new QuizQuestionFactory();
			try {
				//テストメソッドの実行
				quizQuestionFactory.createQuizQuestion();
				fail();

			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * testCreateQuizQuestion_10() 正常系
	 * public List<QuizQuestion> createQuizQuestion() throws SystemException
	 * 
	 * --確認事項-- 
	 * mockで引数を指定してメソッドを実行
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「あいう」の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「3」の場合
	 * MessageProperties.getMessage("quiz.question.title" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.body" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.choice" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「あいう」の場合
	 * --検証項目--
	 * SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testCreateQuizQuestion_10() throws SystemException {

		try (MockedStatic<MessageProperties> mocMessageProperties = mockStatic(MessageProperties.class)) {

			//anyString()を引数に応じて返せるように、anyInt()、anyString()などでデフォルト値を設定してあげる
			mocMessageProperties.when(() -> MessageProperties.getMessage(anyString())).thenReturn("");

			//MessageProperties.getMessage("quiz.number.questions")にthenReturnで3を送る
			mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions")).thenReturn("3");

			for (int i = 1; i <= 3; i++) {
				final int index = i; // ラムダ式内で使うために最終的な変数を作成
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.title" + index))
						.thenReturn("title" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.body" + index))
						.thenReturn("body" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.choice" + index))
						.thenReturn("choice" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.correct" + index))
						.thenReturn("あいう");
			}

			//QuizQuestionFactoryクラスのインスタンス生成
			QuizQuestionFactory quizQuestionFactory = new QuizQuestionFactory();
			try {
				//テストメソッドの実行
				quizQuestionFactory.createQuizQuestion();
				fail();

			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * testCreateQuizQuestion_11() 正常系
	 * public List<QuizQuestion> createQuizQuestion() throws SystemException
	 * 
	 * --確認事項-- 
	 * mockで引数を指定してメソッドを実行
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「」の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「3」の場合
	 * MessageProperties.getMessage("quiz.question.title" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.body" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.choice" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「」の場合
	 * --検証項目--
	 * SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testCreateQuizQuestion_11() throws SystemException {

		try (MockedStatic<MessageProperties> mocMessageProperties = mockStatic(MessageProperties.class)) {

			//anyString()を引数に応じて返せるように、anyInt()、anyString()などでデフォルト値を設定してあげる
			mocMessageProperties.when(() -> MessageProperties.getMessage(anyString())).thenReturn("");

			//MessageProperties.getMessage("quiz.number.questions")にthenReturnで3を送る
			mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions")).thenReturn("3");

			for (int i = 1; i <= 3; i++) {
				final int index = i; // ラムダ式内で使うために最終的な変数を作成
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.title" + index))
						.thenReturn("title" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.body" + index))
						.thenReturn("body" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.choice" + index))
						.thenReturn("choice" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.correct" + index))
						.thenReturn("");
			}

			//QuizQuestionFactoryクラスのインスタンス生成
			QuizQuestionFactory quizQuestionFactory = new QuizQuestionFactory();
			try {
				//テストメソッドの実行
				quizQuestionFactory.createQuizQuestion();
				fail();

			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * testCreateQuizQuestion_12() 正常系
	 * public List<QuizQuestion> createQuizQuestion() throws SystemException
	 * 
	 * --確認事項-- 
	 * mockで引数を指定してメソッドを実行
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果がSystemExceptionの場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * MessageProperties.getMessage("quiz.number.questions")の実行結果が「3」の場合
	 * MessageProperties.getMessage("quiz.question.title" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.body" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.choice" + (i + 1))の実行結果がnull以外の場合
	 * MessageProperties.getMessage("quiz.question.correct" + (i + 1))の実行結果が「」の場合
	 * --検証項目--
	 * SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testCreateQuizQuestion_12() throws SystemException {

		try (MockedStatic<MessageProperties> mocMessageProperties = mockStatic(MessageProperties.class)) {

			//anyString()を引数に応じて返せるように、anyInt()、anyString()などでデフォルト値を設定してあげる
			mocMessageProperties.when(() -> MessageProperties.getMessage(anyString())).thenReturn("");

			//MessageProperties.getMessage("quiz.number.questions")にthenReturnで3を送る
			mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions")).thenReturn("3");

			for (int i = 1; i <= 3; i++) {
				final int index = i; // ラムダ式内で使うために最終的な変数を作成
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.title" + index))
						.thenReturn("title" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.body" + index))
						.thenReturn("body" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.choice" + index))
						.thenReturn("choice" + index);
				mocMessageProperties.when(() -> MessageProperties.getMessage("quiz.question.correct" + index))
						.thenThrow(new SystemException(""));
			}

			//QuizQuestionFactoryクラスのインスタンス生成
			QuizQuestionFactory quizQuestionFactory = new QuizQuestionFactory();
			try {
				//テストメソッドの実行
				quizQuestionFactory.createQuizQuestion();
				fail();

			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}
}
