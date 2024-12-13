package jp.co.ginga.application.quiz;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * クイズ問題クラス
 * @author Isogai
 *
 */
public class TestQuizQuestion {

	//テストデータ
	private String title = "問題";
	private String body = "内容";
	private String choice = "選択肢";
	private int correct = 1;

	/**
	 * testQuizQuestion_01() 正常系
	 * public QuizQuestion(String problemTitle, String problemBody, String problemChoice, int correct) {
	 * 
	 * --確認事項--
	 * インスタンス生成時に引数を渡し、それぞれの対応するフィールドに値を代入しているか
	 * --条件--
	 * 引数がnull以外の場合
	 * 引数にテストデータの「this.title, this.body, this.choice, this.correct 」をセット
	 * --検証項目--
	 * インスタンス生成時の引数と、引数で渡した値が等しいこと
	 */
	@Test
	public void testQuizQuestion_01() {
		try {
			//インスタンス生成をしてそれぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);

			//テストメソッド実行
			String title = quiz.getTitle();
			String body = quiz.getBody();
			String choice = quiz.getChoice();
			int correct = quiz.getCorrect();

			//検証
			assertEquals(this.title, title);
			assertEquals(this.body, body);
			assertEquals(this.choice, choice);
			assertEquals(this.correct, correct);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testConstructor_02() 正常系
	 * public QuizQuestion(String problemTitle, String problemBody, String problemChoice, int correct)
	 * 
	 * --確認事項--
	 * インスタンス生成時に引数を渡し、それぞれの対応するフィールドに値を代入しているか
	 * --条件--
	 * problemTitle, problemBody, problemChoiceフィールドにnull、correctに整数0を渡す
	 * --検証項目--
	 * 1. インスタンス生成時にそれぞれのフィールドにnullが代入されるか
	 */
	@Test
	public void testConstructor_02() {
		try {
			//インスタンス生成をしてそれぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(null, null, null, 0);

			//テストメソッド実行
			String title = quiz.getTitle();
			String body = quiz.getBody();
			String choice = quiz.getChoice();
			int correct = quiz.getCorrect();

			//検証
			assertNull(title);
			assertNull(body);
			assertNull(choice);
			assertEquals(0, correct);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testgetTitle_01() 正常系
	 * public String getTitle()
	 * 
	 * --確認事項--
	 * problemTitleフィールドの値が返されるか
	 * --条件--
	 * problemTitleフィールドの値がnull以外の場合(文字列)
	 * --検証項目--
	 * 1. 戻り値とproblemTitleフィールドの値が等しいこと
	 */
	@Test
	public void testgetTitle_01() {
		try {
			//インスタンス生成をしてそれぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);

			//テストメソッド
			String result = quiz.getTitle();

			//検証
			assertEquals(this.title, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testgetTitle_02 正常系
	 * public String getTitle()
	 * 
	 * --確認事項--
	 * problemTitleフィールドの値が返されるか
	 * --条件--
	 * problemTitleフィールドの値がnullの場合
	 * --検証項目--
	 *1. problemTitleフィールドの値がnullであるか
	 */
	@Test
	public void testgetTitle_02() {
		try {
			//インスタンス生成をしてそれぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(null, null, null, 1);
			//テストメソッド
			String result = quiz.getTitle();

			//検証
			assertNull(result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testsetTitle_01 正常系
	 * public void setTitle(String problemTitle)
	 * 
	 * --確認事項--
	 * problemTitleフィールドに値がセットされるか
	 * --条件--
	 * problemTitleフィールドの値がnull以外の場合(文字列)
	 * --検証項目--
	 * 1. セットした値とproblemTitleフィールドの値が等しいか
	 */
	@Test
	public void testsetTitle_01() {
		try {
			// インスタンス生成をして、それぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);

			// テストメソッドを呼び出し
			quiz.setTitle(this.title);

			// フィールドの値を取得
			String result = quiz.getTitle();

			// 値が等しいか確認
			assertEquals(this.title, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testsetTitle_02 正常系
	 *public void setTitle(String problemTitle)
	 * 
	 * --確認事項--
	 * problemTitleフィールドにnullがセットされるか
	 * --条件--
	 * problemTitleフィールドの値がnullの場合
	 * --検証項目--
	 * 1. problemTitleフィールドの値がnullであるか
	 */
	@Test
	public void testsetTitle_02() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);

			//テストメソッド
			quiz.setTitle(null);

			//検証
			String result = quiz.getTitle();

			assertNull(result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testgetBody_01() 正常系
	 * public String getBody()
	 * 
	 * --確認事項--
	 * problemTitleフィールドの値が返されるか
	 * --条件--
	 * problemTitleフィールドの値がnull以外の場合(文字列)
	 * --検証項目--
	 * 1. 戻り値とproblemTitleフィールドの値が等しいこと
	 */
	@Test
	public void testProblemBody_01() {
		try {
			//インスタンス生成をしてそれぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);

			//テストメソッド
			String result = quiz.getBody();

			//検証
			assertEquals(this.body, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testgetBody_02 正常系
	 * public String getBody()
	 * 
	 * --確認事項--
	 * problemTitleフィールドの値が返されるか
	 * --条件--
	 * problemTitleフィールドの値がnullの場合
	 * --検証項目--
	 *1. problemTitleフィールドの値がnullであるか
	 */
	@Test
	public void testProblemBody_02() {
		try {
			//インスタンス生成をしてそれぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(null, null, null, 1);
			//テストメソッド
			String result = quiz.getBody();

			//検証
			assertNull(result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testsetBody_01 正常系
	 * public void setBody(String problemBody) 
	 * 
	 * --確認事項--
	 * problemTitleフィールドに値がセットされるか
	 * --条件--
	 * problemTitleフィールドの値がnull以外の場合(文字列)
	 * --検証項目--
	 * 1. セットした値とproblemTitleフィールドの値が等しいか
	 */
	@Test
	public void testsetBody_01() {
		try {
			// インスタンス生成をして、それぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);

			// テストメソッドを呼び出し
			quiz.setBody(this.body);

			// フィールドの値を取得
			String result = quiz.getBody();

			// 値が等しいか確認
			assertEquals(this.body, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testsetBody_02 正常系
	 * public void setBody(String problemBody) 
	 * 
	 * --確認事項--
	 * problemTitleフィールドにnullがセットされるか
	 * --条件--
	 * problemTitleフィールドの値がnullの場合
	 * --検証項目--
	 * 1. problemTitleフィールドの値がnullであるか
	 */
	@Test
	public void testsetBody_02() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);

			//テストメソッド
			quiz.setBody(null);

			//検証
			String result = quiz.getBody();

			assertNull(result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testgetChoice_01() 正常系
	 * public String getChoice()
	 * 
	 * --確認事項--
	 * ProblemChoiceフィールドの値が返されるか
	 * --条件--
	 * ProblemChoiceフィールドの値がnull以外の場合(文字列)
	 * --検証項目--
	 * 1. 戻り値とProblemChoiceフィールドの値が等しいこと
	 */
	@Test
	public void testgetChoice_01() {
		try {
			//インスタンス生成をしてそれぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);

			//テストメソッド
			String result = quiz.getChoice();

			//検証
			assertEquals(this.choice, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testgetChoice_02 正常系
	 * public String getChoice()
	 * 
	 * --確認事項--
	 * ProblemChoiceフィールドの値が返されるか
	 * --条件--
	 * ProblemChoiceフィールドの値がnullの場合
	 * --検証項目--
	 * 1. ProblemChoiceフィールドの値がnullであるか
	 */
	@Test
	public void testgetChoice_02() {
		try {
			//インスタンス生成をしてそれぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(null, null, null, 1);
			//テストメソッド
			String result = quiz.getChoice();

			//検証
			assertNull(result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testsetChoice_01 正常系
	 * public void setChoice(String problemChoice)
	 * 
	 * --確認事項--
	 * problemTitleフィールドに値がセットされるか
	 * --条件--
	 * problemTitleフィールドの値がnull以外の場合(文字列)
	 * --検証項目--
	 * 1. セットした値とproblemTitleフィールドの値が等しいか
	 */
	@Test
	public void testsetChoice_01() {
		try {
			// インスタンス生成をして、それぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);

			// テストメソッドを呼び出し
			quiz.setChoice(this.title);

			// フィールドの値を取得
			String result = quiz.getChoice();

			// 値が等しいか確認
			assertEquals(this.title, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testsetChoice_02 正常系
	 * public void setChoice(String problemChoice)
	 * 
	 * --確認事項--
	 * problemTitleフィールドにnullがセットされるか
	 * --条件--
	 * problemTitleフィールドの値がnullの場合
	 * --検証項目--
	 * 1. problemTitleフィールドの値がnullであるか
	 */
	@Test
	public void testsetChoice_02() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);

			//テストメソッド
			quiz.setChoice(null);

			//検証
			String result = quiz.getChoice();

			assertNull(result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetCorrect_01() 正常系
	 * public int getCorrect()
	 * 
	 * --確認事項--
	 * Correcフィールドの値が返されるか
	 * --条件--
	 * Correcフィールドの値が0以外の場合(整数)
	 * --検証項目--
	 * 1. 戻り値とCorrecフィールドの値が等しいこと
	 */
	@Test
	public void testGetCorrect_01() {
		try {
			//インスタンス生成をしてそれぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);

			//テストメソッド
			int result = quiz.getCorrect();

			//検証
			assertEquals(this.correct, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * ttestGetCorrect_02 正常系
	 * public int getCorrect()
	 * 
	 * --確認事項--
	 * Correcフィールドの値が返されるか
	 * --条件--
	 * Correcフィールドの値が0の場合
	 * --検証項目--
	 * 1. Correcフィールドの値が0であるか
	 */
	@Test
	public void testGetCorrect_02() {
		try {
			//インスタンス生成をしてそれぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(null, null, null, 0);
			//テストメソッド
			int result = quiz.getCorrect();

			//検証
			assertEquals(0, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSetCorrect_01 正常系
	 * public void setCorrect(int correct)
	 * 
	 * --確認事項--
	 * Correcフィールドに値がセットされるか
	 * --条件--
	 * Correcフィールドの値が0以外の場合(整数)
	 * --検証項目--
	 * 1. セットした値とCorrecフィールドの値が等しいか
	 */
	@Test
	public void testSetCorrect_01() {
		try {
			// インスタンス生成をして、それぞれの対応するフィールドに値を代入
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);

			// テストメソッドを呼び出し
			quiz.setCorrect(this.correct);

			// フィールドの値を取得
			int result = quiz.getCorrect();

			// 値が等しいか確認
			assertEquals(this.correct, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSetCorrect_02 正常系
	 * public void setCorrect(int correct)
	 * 
	 * --確認事項--
	 * Correcフィールドに0がセットされるか
	 * --条件--
	 * Correcフィールドの値が0の場合
	 * --検証項目--
	 * 1. Correcフィールドの値が0であるか
	 */
	@Test
	public void testSetCorrect_02() {
		try {
			//準備
			QuizQuestion quiz = new QuizQuestion(this.title, this.body, this.choice, this.correct);

			//テストメソッド
			quiz.setCorrect(0);

			//検証
			int result = quiz.getCorrect();

			// 値が等しいか確認
			assertEquals(0, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
