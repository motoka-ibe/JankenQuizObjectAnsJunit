package jp.co.ginga.cui.impl.quiz;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

/**
 * クイズ問題クラス
 * @author yoshi
 *
 */
public class QuizQuestionTest {
	//テストデータ
	private String title = "問題1";
	private String body = "昭和40年頃の理髪店の料金はいくらだったでしょうか？";
	private String choice = "1. 560円\n2. 350円\n3. 1000円\n";
	private int correct = 2;


	/**
	 * testConstructor001 正常系
	 * public QuizQuestion(String problemTitle, String problemBody, String problemChoice, int correct)
	 * --確認事項--
	 * インスタンス生成時に引数を渡し、それぞれの対応するフィールドに値を代入しているか
	 * --条件--
	 * problemTitle, problemBody, problemChoiceフィールドに文字列、correctに整数を渡す
	 * --検証項目--
	 * 1. インスタンス生成時にそれぞれのフィールドに値が代入されるか
	 */
	@Test
	public void testConstructor001() {
		try {
			//準備
			Field problemTitleField = QuizQuestion.class.getDeclaredField("problemTitle");
			Field problemBodyField = QuizQuestion.class.getDeclaredField("problemBody");
			Field problemChoiceField = QuizQuestion.class.getDeclaredField("problemChoice");
			Field correctField = QuizQuestion.class.getDeclaredField("correct");
			problemTitleField.setAccessible(true);
			problemBodyField.setAccessible(true);
			problemChoiceField.setAccessible(true);
			correctField.setAccessible(true);
			
			//テストメソッド
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			
			//検証
			String title = (String) problemTitleField.get(quiz);
			String body = (String) problemBodyField.get(quiz);
			String choice = (String) problemChoiceField.get(quiz);
			int correct = (int) correctField.get(quiz);
			assertEquals(this.title, title);
			assertEquals(this.body, body);
			assertEquals(this.choice, choice);
			assertEquals(this.correct, correct);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testConstructor002 異常系
	 * public QuizQuestion(String problemTitle, String problemBody, String problemChoice, int correct)
	 * --確認事項--
	 * インスタンス生成時に引数を渡し、それぞれの対応するフィールドに値を代入しているか
	 * --条件--
	 * problemTitle, problemBody, problemChoiceフィールドにnull、correctに整数を渡す
	 * --検証項目--
	 * 1. インスタンス生成時にそれぞれのフィールドにnullが代入されるか
	 */
	@Test
	public void testConstructor002() {
		try {
			//準備
			Field problemTitleField = QuizQuestion.class.getDeclaredField("problemTitle");
			Field problemBodyField = QuizQuestion.class.getDeclaredField("problemBody");
			Field problemChoiceField = QuizQuestion.class.getDeclaredField("problemChoice");
			Field correctField = QuizQuestion.class.getDeclaredField("correct");
			problemTitleField.setAccessible(true);
			problemBodyField.setAccessible(true);
			problemChoiceField.setAccessible(true);
			correctField.setAccessible(true);
			
			//テストメソッド
			QuizQuestion quiz = new QuizQuestion(null, null, null, this.correct);
			
			//検証
			String title = (String) problemTitleField.get(quiz);
			String body = (String) problemBodyField.get(quiz);
			String choice =  (String) problemChoiceField.get(quiz);
			int correct = (int) correctField.get(quiz);

			assertNull( title);
			assertNull(body);
			assertNull(choice);
			assertEquals(this.correct, correct);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetProblemTitle001 正常系
	 * public String getProblemTitle()
	 * --確認事項--
	 * problemTitleフィールドの値が返されるか
	 * --条件--
	 * problemTitleフィールドの値は文字列
	 * --検証項目--
	 * 1. 戻り値とproblemTitleフィールドの値が等しいか
	 */
	@Test
	public void testGetProblemTitle001() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field problemTitleField = QuizQuestion.class.getDeclaredField("problemTitle");
			problemTitleField.setAccessible(true);
			problemTitleField.set(quiz, this.title);

			//テストメソッド
			String result = quiz.getProblemTitle();

			//検証
			assertEquals(this.title, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetProblemTitle002 正常系
	 * public String getProblemTitle()
	 * --確認事項--
	 * problemTitleフィールドの値が返されるか
	 * --条件--
	 * problemTitleフィールドの値はnull
	 * --検証項目--
	 * 1. 戻り値がnullであるか
	 */
	@Test
	public void testGetProblemTitle002() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field problemTitleField = QuizQuestion.class.getDeclaredField("problemTitle");
			problemTitleField.setAccessible(true);
			problemTitleField.set(quiz, null);

			//テストメソッド
			String result = quiz.getProblemTitle();

			//検証
			assertNull(result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSetProblemTitle001 正常系
	 * public void setProblemTitle(String problemTitle)
	 * --確認事項--
	 * problemTitleフィールドに値がセットされるか
	 * --条件--
	 * problemTitleフィールドの値はnull
	 * --検証項目--
	 * 1. セットした値とproblemTitleフィールドの値が等しいか
	 */
	@Test
	public void testSetProblemTitle001() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field problemTitleField = QuizQuestion.class.getDeclaredField("problemTitle");
			problemTitleField.setAccessible(true);
			problemTitleField.set(quiz, null);

			//テストメソッド
			quiz.setProblemTitle(this.title);

			//検証
			String result = (String) problemTitleField.get(quiz);
			assertEquals(this.title, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSetProblemTitle002 異常系
	 * public void setProblemTitle(String problemTitle)
	 * --確認事項--
	 * problemTitleフィールドにnullがセットされるか
	 * --条件--
	 * problemTitleフィールドの値は文字列
	 * --検証項目--
	 * 1. problemTitleフィールドの値がnullであるか
	 */
	@Test
	public void testSetProblemTitle002() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field problemTitleField = QuizQuestion.class.getDeclaredField("problemTitle");
			problemTitleField.setAccessible(true);
			problemTitleField.set(quiz, this.title);

			//テストメソッド
			quiz.setProblemTitle(null);

			//検証
			String result = (String) problemTitleField.get(quiz);
			assertNull(result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetProblemBody001 正常系
	 * public String getProblemBody()
	 * --確認事項--
	 * problemBodyフィールドの値が返されるか
	 * --条件--
	 * problemBodyフィールドの値は文字列
	 * --検証項目--
	 * 1. 戻り値とproblemBodyフィールドの値が等しいか
	 */
	@Test
	public void testGetProblemBody001() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field problemBodyField = QuizQuestion.class.getDeclaredField("problemBody");
			problemBodyField.setAccessible(true);
			problemBodyField.set(quiz, this.body);

			//テストメソッド
			String result = quiz.getProblemBody();

			//検証
			assertEquals(this.body, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetProblemBody002 異常系
	 * public String getProblemBody()
	 * --確認事項--
	 * problemBodyフィールドの値が返されるか
	 * --条件--
	 * problemBodyフィールドの値はnull
	 * --検証項目--
	 * 1. 戻り値がnullであるか
	 */
	@Test
	public void testGetProblemBody002() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field problemBodyField = QuizQuestion.class.getDeclaredField("problemBody");
			problemBodyField.setAccessible(true);
			problemBodyField.set(quiz, null);

			//テストメソッド
			String result = quiz.getProblemBody();

			//検証
			assertNull(result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * testSetProblemBody001 正常系
	 * public void setProblemBody(String problemBody)
	 * --確認事項--
	 * problemBodyフィールドに値がセットされるか
	 * --条件--
	 * problemBodyフィールドの値はnull
	 * --検証項目--
	 * 1. セットした値とproblemBodyフィールドの値が等しいか
	 */
	@Test
	public void testSetProblemBody001() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field problemBodyField = QuizQuestion.class.getDeclaredField("problemBody");
			problemBodyField.setAccessible(true);
			problemBodyField.set(quiz, null);
			
			//テストメソッド
			quiz.setProblemBody(this.body);

			//検証
			String result = (String) problemBodyField.get(quiz);
			assertEquals(this.body, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSetProblemBody002 異常系
	 * public void setProblemBody(String problemBody)
	 * --確認事項--
	 * problemBodyフィールドに値がセットされるか
	 * --条件--
	 * problemBodyフィールドの値は文字列
	 * --検証項目--
	 * 1. problemBodyフィールドの値がnullであるか
	 */
	@Test
	public void testSetProblemBody002() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field problemBodyField = QuizQuestion.class.getDeclaredField("problemBody");
			problemBodyField.setAccessible(true);
			problemBodyField.set(quiz, this.body);

			//テストメソッド
			quiz.setProblemBody(null);

			//検証
			String result = (String) problemBodyField.get(quiz);
			assertNull(result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetProblemChoice001 正常系
	 * public String getProblemChoice()
	 * --確認事項--
	 * problemChoiceフィールドの値が返されるか
	 * --条件--
	 * problemChoiceフィールドの値は文字列
	 * --検証項目--
	 * 1. 戻り値とproblemChoiceフィールドの値が等しいか
	 */
	@Test
	public void testGetProblemChoice001() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field problemChoiceField = QuizQuestion.class.getDeclaredField("problemChoice");
			problemChoiceField.setAccessible(true);
			problemChoiceField.set(quiz, this.choice);

			//テストメソッド
			String result = quiz.getProblemChoice();

			//検証
			assertEquals(this.choice, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetProblemChoice002 異常系
	 * public String getProblemChoice()
	 * --確認事項--
	 * problemChoiceフィールドの値が返されるか
	 * --条件--
	 * problemChoiceフィールドの値はnull
	 * --検証項目--
	 * 1. 戻り値がnullであるか
	 */
	@Test
	public void testGetProblemChoice002() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field problemChoiceField = QuizQuestion.class.getDeclaredField("problemChoice");
			problemChoiceField.setAccessible(true);
			problemChoiceField.set(quiz, null);

			//テストメソッド
			String result = quiz.getProblemChoice();

			//検証
			assertNull(result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSetProblemChoice001 正常系
	 * public void setProblemChoice(String problemChoice)
	 * --確認事項--
	 * problemChoiceフィールドに値がセットされるか
	 * --条件--
	 * problemChoiceフィールドの値はnull
	 * --検証項目--
	 * 1. セットした値とproblemBodyフィールドの値が等しいか
	 */
	@Test
	public void testSetProblemChoice001() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field problemChoiceField = QuizQuestion.class.getDeclaredField("problemChoice");
			problemChoiceField.setAccessible(true);
			problemChoiceField.set(quiz, this.choice);

			//テストメソッド
			quiz.setProblemChoice(this.choice);

			//検証
			String result = (String) problemChoiceField.get(quiz);
			assertEquals(this.choice, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSetProblemChoice002 異常系
	 * public void setProblemChoice(String problemChoice)
	 * --確認事項--
	 * problemChoiceフィールドに値がセットされるか
	 * --条件--
	 * problemChoiceフィールドの値は文字列
	 * --検証項目--
	 * 1. problemBodyフィールドの値がnullであるか
	 */
	@Test
	public void testSetProblemChoice002() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field problemChoiceField = QuizQuestion.class.getDeclaredField("problemChoice");
			problemChoiceField.setAccessible(true);
			problemChoiceField.set(quiz, this.choice);

			//テストメソッド
			quiz.setProblemChoice(null);

			//検証
			String result = (String) problemChoiceField.get(quiz);
			assertNull(result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetCorrect001 正常系
	 * public int getCorrect()
	 * --確認事項--
	 * correctフィールドの値が返されるか
	 * --条件--
	 * correctフィールドの値は整数
	 * --検証項目--
	 * 1. 戻り値とcorrectフィールドの値が等しいか
	 */
	@Test
	public void testGetCorrect001() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field correctField = QuizQuestion.class.getDeclaredField("correct");
			correctField.setAccessible(true);
			correctField.set(quiz, this.correct);

			//テストメソッド
			int result = quiz.getCorrect();

			//検証
			assertEquals(this.correct, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSetCorrect001 正常系
	 * public void setCorrect(int correct)
	 * --確認事項--
	 * correctフィールドに値が代入されるか
	 * --条件--
	 * correctフィールドの値は0
	 * --検証項目--
	 * 1. セットした値ととcorrectフィールドの値が等しいか
	 */
	@Test
	public void testSetCorrect001() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);
			Field correctField = QuizQuestion.class.getDeclaredField("correct");
			correctField.setAccessible(true);
			correctField.set(quiz, 0);
			
			//テストメソッド
			quiz.setCorrect(this.correct);

			//検証
			int result = (int) correctField.get(quiz);
			assertEquals(this.correct, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

}
