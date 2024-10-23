package jp.co.ginga.cui.impl.quiz;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;

import jp.co.ginga.application.CuiGameApplication;
import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.keybord.Keybord;
import jp.co.ginga.util.properties.MessageProperties;

/**
 * CUIクイズゲーム実装クラス
 *
 * @author yoshi
 *
 */
public class QuizCuiGameApplicationImplTest{

	//テストデータ
	private int zero = 0;
	private int one = 1;
	private int two = 2;
	private int three = 3;
	private String alphabet = "a";
	private List<QuizQuestion> quizList = new ArrayList<QuizQuestion>();
	private List<QuizQuestion> emptyQuizList = new ArrayList<QuizQuestion>();
	private String errorMessage ;


	//テストクラス
	@InjectMocks
	private CuiGameApplication game = new QuizCuiGameApplicationImpl();

	@BeforeEach
	private void createTestData() {
		try {
			int numberOfQuiz = Integer.parseInt(MessageProperties.getMessage("quiz.number.questions"));
			for (int i = 0; i < numberOfQuiz; i++) {
				this.quizList.add(new QuizQuestion(MessageProperties.getMessage("quiz.question.titile" + (i + 1)),
						MessageProperties.getMessage("quiz.question.body" + (i + 1)),
						MessageProperties.getMessage("quiz.question.choice" + (i + 1)),
						Integer.parseInt(MessageProperties.getMessage("quiz.question.correct" + (i + 1)))));
			}

			this.errorMessage = MessageProperties.getMessage("error.properties.error");

		}catch(SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testAction001 正常系
	 * public void action() throws SystemException
	 * --確認事項--
	 * 実行中に例外が発生しないこと
	 * getIntが指定した値が戻されるようにmock化
	 * --条件--
	 * mock化したgetIntの戻り値は1回目は1、2回目は2、3回目は3
	 * --検証項目--
	 * 1. 例外が発生せず処理を終えること
	 * 2. getIntが3回呼び出されるか
	 */
	@Test
	public void testAction001() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			//入力値 1, , 2, 3
			mockKeybord.when(() -> Keybord.getInt(this.one, this.three)).thenReturn(this.one).thenReturn(this.two).thenReturn(this.three);
			
			//テストメソッド
			this.game.action();
			
			//検証
			mockKeybord.verify(() -> Keybord.getInt(this.one, this.three), times(3));
		
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateQuizList001 正常系
	 * private void createQuizList() throws SystemException
	 * --確認事項--
	 * listフィールドにそれぞれのデータが追加されたQuizQuestionインスタンスが3個追加されているか
	 * --条件--
	 * listフィールドの値は空のリスト
	 * --検証項目--
	 * 1.それぞれのインスタンスのフィールドにそれぞれのデータが追加されているか
	 * 2. listの長さが3であるか
	 */
	@Test
	public void testCreateQuizList001() {
		try{
			//準備
			Method method = QuizCuiGameApplicationImpl.class.getDeclaredMethod("createQuizList");
			Field listField = QuizCuiGameApplicationImpl.class.getDeclaredField("list");
			method.setAccessible(true);
			listField.setAccessible(true);
			listField.set(this.game, this.emptyQuizList);

			//テストメソッド
			method.invoke(this.game);

			//検証
			@SuppressWarnings("unchecked")
			List<QuizQuestion> list = (List<QuizQuestion>) listField.get(this.game);
			assertEquals(this.quizList.size(), list.size());
			for(int i = 0; i < list.size(); i++) {
				assertEquals(this.quizList.get(i).getProblemTitle(), list.get(i).getProblemTitle());
				assertEquals(this.quizList.get(i).getProblemBody(), list.get(i).getProblemBody());
				assertEquals(this.quizList.get(i).getProblemChoice(), list.get(i).getProblemChoice());
				assertEquals(this.quizList.get(i).getCorrect(), list.get(i).getCorrect());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateQuizList002 異常系
	 * private void createQuizList() throws SystemException
	 * --確認事項--
	 * listフィールドにそれぞれのデータが追加されたQuizQuestionインスタンスが0個追加されているか
	 * --条件--
	 * listフィールドの値は空のリスト
	 * --検証項目--
	 * 1. listの長さが0であるか
	 */
	@Test
	public void testCreateQuizList002() {
		//モック化
		try(MockedStatic<MessageProperties> mockMessageProperties = mockStatic(MessageProperties.class)){
			mockMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions")).thenReturn(String.valueOf(this.zero));
			
			//準備
			Method method = QuizCuiGameApplicationImpl.class.getDeclaredMethod("createQuizList");
			Field listField = QuizCuiGameApplicationImpl.class.getDeclaredField("list");
			method.setAccessible(true);
			listField.setAccessible(true);
			listField.set(this.game, this.emptyQuizList);

			//テストメソッド
			method.invoke(this.game);

			//検証
			@SuppressWarnings("unchecked")
			List<QuizQuestion> list = (List<QuizQuestion>) listField.get(this.game);
			assertEquals(this.emptyQuizList.size(), list.size());
			
		} catch (Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateQuizList003 異常系
	 * private void createQuizList() throws SystemException
	 * --確認事項--
	 * 問題数が異常値の場合、SystemExceptionが発生する
	 * --条件--
	 * listフィールドの値は空のリスト
	 * --検証項目--
	 * 1. SystemExceptionがスローされる
	 * 2. メッセージ「プロパティの値が不正です。」が含まれているか (取り出せないため未検証)
	 * 3. listフィールドの長さは0
	 */
	@Test
	public void testCreateQuizList003() {
		//モック化
		try(MockedStatic<MessageProperties> mockMessageProperties = mockStatic(MessageProperties.class)){
			mockMessageProperties.when(() -> MessageProperties.getMessage("quiz.number.questions")).thenReturn(this.alphabet);
			
			//準備
			Method method = QuizCuiGameApplicationImpl.class.getDeclaredMethod("createQuizList");
			Field listField = QuizCuiGameApplicationImpl.class.getDeclaredField("list");
			method.setAccessible(true);
			listField.setAccessible(true);
			listField.set(this.game, this.emptyQuizList);

			//テストメソッド
			InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> method.invoke(this.game));

			//検証
			@SuppressWarnings("unchecked")
			List<QuizQuestion> list = (List<QuizQuestion>) listField.get(this.game);
			assertEquals(e.getTargetException().getClass(), SystemException.class);
			assertEquals(this.emptyQuizList.size(), list.size());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testViewProblem001 正常系
	 * private void viewProblem(QuizQuestion quiz)
	 * --確認事項--
	 * title、body、choiceのゲッターが呼び出され、出力されるか
	 * --条件--
	 *	メソッドの引数はmock化したQuizQuestionクラス
	 * --検証項目--
	 * 1.title、body、choiceのゲッターが1回だけ呼び出されるか
	 */
	@Test
	public void testViewProblem001() {
		try{
			//準備
			Method method = QuizCuiGameApplicationImpl.class.getDeclaredMethod("viewProblem", QuizQuestion.class);
			method.setAccessible(true);
			
			//モック化
			QuizQuestion mockQuizQuestion = mock(QuizQuestion.class);
			
			//テストメソッド
			method.invoke(this.game, mockQuizQuestion);

			//検証
			verify(mockQuizQuestion, times(1)).getProblemTitle();
			verify(mockQuizQuestion, times(1)).getProblemBody();
			verify(mockQuizQuestion, times(1)).getProblemChoice();
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testJudge001 正常系
	 * private void judge(QuizQuestion quiz) throws SystemException
	 * --確認事項--
	 * 不正な値が入力されたら再度入力が求められ、正常な値が入力されたら正解数を増やして終了する
	 * --条件--
	 *	getIntは1回目はApplicationExceptionを発生し、2回目は1が入力されるようにmock化
	 * correctCountは0
	 * --検証項目--
	 * 1. correctカウントはインクリメントされるか
	 * 2. getIntは2回呼び出されるか
	 */
	@Test
	public void testJudge001() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt(this.one, this.three)).thenThrow(new ApplicationException(null)).thenReturn(this.one);
			QuizQuestion mockQuizQuestion = mock(QuizQuestion.class);
			when(mockQuizQuestion.getCorrect()).thenReturn(this.one);
			
			//準備
			Method method = QuizCuiGameApplicationImpl.class.getDeclaredMethod("judge", QuizQuestion.class);
			Field correctCountField = QuizCuiGameApplicationImpl.class.getDeclaredField("correctConut");
			method.setAccessible(true);
			correctCountField.setAccessible(true);
			correctCountField.set(this.game, this.zero);

			//テストメソッド
			method.invoke(this.game, mockQuizQuestion);

			//検証
			int correctCount = (int) correctCountField.get(this.game);
			assertEquals(this.one, correctCount);
			verify(mockQuizQuestion, times(2)).getCorrect();
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/**
	 * testViewResult001 正常系
	 * private void viewResult() throws SystemException
	 * --確認事項--
	 * 正解数に応じてメッセージが出力される
	 * --条件--
	 *	correctCountフィールドは0
	 * --検証項目--
	 * 1. correctCountフィールドに変更がないか
	 * 2. 例外が発生せずに終了するか
	 */
	@Test
	public void testViewResult001() {
		try{
			//準備
			Method method = QuizCuiGameApplicationImpl.class.getDeclaredMethod("viewResult");
			Field correctCountField = QuizCuiGameApplicationImpl.class.getDeclaredField("correctConut");
			method.setAccessible(true);
			correctCountField.setAccessible(true);
			correctCountField.set(this.game, this.zero);

			//テストメソッド
			method.invoke(this.game);

			//検証
			int correctCount = (int) correctCountField.get(this.game);
			assertEquals(this.zero, correctCount);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
