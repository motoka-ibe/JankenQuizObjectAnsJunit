package jp.co.ginga.application.janken;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import jp.co.ginga.application.janken.jankenplayer.HumanJankenPlayerImpl;
import jp.co.ginga.application.janken.jankenplayer.JankenPlayer;
import jp.co.ginga.application.janken.jankenplayer.NpcJankenPlayerImpl;
import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.keyboard.Keyboard;

/**
 * CUIじゃんけん実装クラス
 * @author Isogai
 *
 */
public class TestJankenCuiGameApplicationImpl {

	//テストデータ
	private int zero = 0;
	private int five = 5;
	private int ten = 10;
	private int errorHand = 4;
	private int rock = JankenParam.ROCK.getInt();
	private int scissors = JankenParam.SCISSORS.getInt();
	private int paper = JankenParam.PAPER.getInt();
	private int draw = JankenParam.DRAW.getInt();

	private String[] humanNames = {
			"プレーヤー1", "プレーヤー2", "プレーヤー3",
			"プレーヤー4", "プレーヤー5", "プレーヤー6",
			"プレーヤー7", "プレーヤー8", "プレーヤー9",
			"プレーヤー10", "プレーヤー11"
	};

	private String[] npcNames = {
			"NPC1", "NPC2", "NPC3", "NPC4",
			"NPC5", "NPC6", "NPC7", "NPC8",
			"NPC9", "NPC10", "NPC11"
	};

	private JankenPlayer[] humanPlayers = new JankenPlayer[humanNames.length];
	private JankenPlayer[] npcPlayers = new JankenPlayer[npcNames.length];

	private List<JankenPlayer> emptyPlayerList = new ArrayList<JankenPlayer>();
	private List<JankenPlayer> nullPlayerList = null;

	JankenCuiGameApplicationImpl jankenCuiGameApplicationImpl = new JankenCuiGameApplicationImpl();

	/**
	 * testInit_01 正常系
	 * void init()
	 * 
	 * --確認事項--
	 * playerListがnullの場合、
	 * playerListのインスタンス生成されサイズが0であること
	 * --条件--
	 *	playerListフィールドの値がnull
	 * --検証項目--
	 * 1. playerListがArrayListのインスタンスであること
	 * 2. playerListフィールドのサイズが0であること
	 */
	@Test
	public void testInit_01() {

		// playerListをnullに設定
		jankenCuiGameApplicationImpl.setPlayerList(null);

		// テストメソッドを実行
		jankenCuiGameApplicationImpl.init();

		// playerListの状態を取得
		List<JankenPlayer> playerList = jankenCuiGameApplicationImpl.getPlayerList();

		// 検証
		assertNotNull(playerList); // init()後のplayerListがnullでないこと
		assertTrue(playerList instanceof ArrayList<?>); // playerListがArrayListのインスタンスであること
		assertEquals(0, playerList.size()); // playerListのサイズが0であること

	}

	/**
	 * testInit_02 正常系
	 * void init()
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合
	 * playerListのクリア処理によりサイズが0であること
	 * --条件--
	 *	playerListがNULL以外の場合
	 * --検証項目--
	 * 1. playerListがArrayListのインスタンスであること
	 * 2. playerListフィールドのサイズが0であること
	 */
	@Test
	public void testInit_02() {

		//emptyPlayerListにhumanPlayers[0]を追加
		emptyPlayerList.add(humanPlayers[0]);

		// playerListにemptyPlayerListをセット(playerListがnullでない場合)
		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

		// テストメソッドを実行
		jankenCuiGameApplicationImpl.init();

		// playerListの状態を取得
		List<JankenPlayer> playerList = jankenCuiGameApplicationImpl.getPlayerList();

		// 検証
		assertNotNull(playerList); // playerListがnullでないこと
		assertTrue(playerList instanceof ArrayList<?>); // playerListがArrayListのインスタンスであること
		assertEquals(0, playerList.size()); // playerListのサイズが0であること

	}

	/**
	 * testCreateHumanOfJankenPlayer_01() 異常系
	 * void createHumanOfJankenPlayer() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULLの場合、SystemException(実行データ不良)が発行されること
	 * --条件--
	 *	playerListフィールドの値がnull
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.SystemExceptionのメッセージ内容が正しいこと
	 */
	@Test
	public void testCreateHumanOfJankenPlayer_01() {

		//playerListがNULLの場合
		jankenCuiGameApplicationImpl.setPlayerList(nullPlayerList);

		try {
			jankenCuiGameApplicationImpl.createHumanOfJankenPlayer(); //テストメソッドの実行
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg()); //検証
		}

	}

	/**
	 * testCreateHumanOfJankenPlayer_02() 異常系
	 * void createHumanOfJankenPlayer() throws SystemException
	 *
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * 0～10以外の整数を入力した場合、
	 * getInt()メソッドがApplicationExceptionを発行する。
	 * その場合、再入力を促すメッセージを出力し、処理を繰り返す
	 * --条件--
	 * playerListがNULL以外の場合で
	 * getInt()にApplicationExceptionを発行させた場合
	 * 再入力を促すメッセージを出力し、処理を繰り返す。
	 * 正しい数値が入るまで無限ループしてしまうのでApplicationExceptionを呼んだ後、
	 * getInt()の引数に正常値0を送り処理は正常に終了させる
	 * --検証事項--
	 * 1.0を送るのでプレーヤーリストサイズが0のままであることを確認
	 * 2.再入力を促すメッセージを出力し、処理を繰り返すことを確認する。
	 */
	@Test
	public void testCreateHumanOfJankenPlayer_02() {

		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			//getInt()がApplicationExceptionを発行→そのあと0という正常値をかえす
			mockKeyboard.when(() -> Keyboard.getInt(0, 10)).thenThrow(new ApplicationException("")).thenReturn(0);
			;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			/*
			 * -----解説------
			 * ByteArrayOutputStreamは、
			 * バイトデータをメモリ内に蓄積するためのストリーム。
			 * OutputStreamの一種で、書き込まれたデータはすぐにメモリに保存され、後でその内容を取得できます。
			 * 例えば、標準出力に出力される文字列を、メモリ内に保持しておくことができます。
			 *
			 * System.setOut(PrintStream out)は、
			 * Javaの標準出力(通常はコンソール)を指定したPrintStreamにリダイレクトするためのメソッド。
			 * ここでnew PrintStream(out)を使用することで、標準出力がByteArrayOutputStreamに書き込まれるようになります。
			 * つまり、System.out.println()などで出力された内容は、コンソールに表示されるのではなく、outに保存されます。
			 */

			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化
			jankenCuiGameApplicationImpl.createHumanOfJankenPlayer(); // テストメソッドの実行

			//検証
			List<JankenPlayer> playerList = jankenCuiGameApplicationImpl.getPlayerList();

			assertEquals(0, playerList.size()); // プレーヤーリストが0のままならOK
			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("人間プレーヤー数を入力してください。" + System.lineSeparator()
					+ "入力した値が不正です。再入力をお願いします。" + System.lineSeparator()
					+ "人間プレーヤー数を入力してください。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateHumanOfJankenPlayer_03() 正常系
	 * void createHumanOfJankenPlayer() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で整数0を入力した場合
	 * 入力した整数分のオブジェクト生成されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * 整数0を入力した場合
	 * --検証事項--
	 * 1.playerListが0であること
	 * 2.モックオブジェクトに対してKeyboard.getInt(0, 10)が1回呼び出されていること
	 */
	@Test
	public void testCreateHumanOfJankenPlayer_03() {
		// モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			// 0を返すように設定
			mockKeyboard.when(() -> Keyboard.getInt(0, 10)).thenReturn(this.zero);

			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

			jankenCuiGameApplicationImpl.createHumanOfJankenPlayer(); // テストメソッドの実行

			// 検証
			List<JankenPlayer> playerList = jankenCuiGameApplicationImpl.getPlayerList();

			assertEquals(0, playerList.size());
			mockKeyboard.verify(() -> Keyboard.getInt(0, 10), times(1));

			/* 
			 *  -----解説------
			 * () -> Keyboard.getInt(0, 10)の部分は
			 * ラムダ式でKeyboard.getInt(0, 10) の呼び出しをして
			 * mockKeyboard.verify(...)でmockKeyboard というモックオブジェクトに対して
			 * 特定のメソッドが期待通りに呼び出されたかどうかを確認している
			 * times(1)は指定したメソッドが正確に1回呼び出されたことを確認するためのメソッド
			 */

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateHumanOfJankenPlayer_04() 正常系
	 * void createHumanOfJankenPlayer() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で整数5を入力した場合
	 * 入力した整数分のオブジェクト生成されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * 整数5を入力した場合
	 * --検証事項--
	 * 1.playerListが5であること
	 * 2.モックオブジェクトに対してKeyboard.getInt(0, 10)が1回呼び出されていること
	 */
	@Test
	public void testCreateHumanOfJankenPlayer_04() {
		// モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			// 10を返すように設定
			mockKeyboard.when(() -> Keyboard.getInt(0, 10)).thenReturn(this.five);

			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

			jankenCuiGameApplicationImpl.createHumanOfJankenPlayer(); // テストメソッドの実行

			// 検証
			List<JankenPlayer> playerList = jankenCuiGameApplicationImpl.getPlayerList();
			assertEquals(5, playerList.size());
			mockKeyboard.verify(() -> Keyboard.getInt(0, 10), times(1));

			/* 
			 *  -----解説------
			 * () -> Keyboard.getInt(0, 10)の部分は
			 * ラムダ式でKeyboard.getInt(0, 10) の呼び出しをして
			 * mockKeyboard.verify(...)でmockKeyboard というモックオブジェクトに対して
			 * 特定のメソッドが期待通りに呼び出されたかどうかを確認している
			 * times(1)は指定したメソッドが正確に1回呼び出されたことを確認するためのメソッド
			 */

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateHumanOfJankenPlayer_05() 正常系
	 * void createHumanOfJankenPlayer() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で整数10を入力した場合
	 * 入力した整数分のオブジェクト生成されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * 整数10を入力した場合
	 * --検証事項--
	 * 1.playerListが5であること
	 * 2.モックオブジェクトに対してKeyboard.getInt(0, 10)が1回呼び出されていること
	 */
	@Test
	public void testCreateHumanOfJankenPlayer_05() {
		// モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			// 10を返すように設定
			mockKeyboard.when(() -> Keyboard.getInt(0, 10)).thenReturn(this.ten);

			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

			jankenCuiGameApplicationImpl.createHumanOfJankenPlayer(); // テストメソッドの実行

			// 検証
			List<JankenPlayer> playerList = jankenCuiGameApplicationImpl.getPlayerList();
			assertEquals(10, playerList.size());
			mockKeyboard.verify(() -> Keyboard.getInt(0, 10), times(1));

			/* 
			 *  -----解説------
			 * () -> Keyboard.getInt(0, 10)の部分は
			 * ラムダ式でKeyboard.getInt(0, 10) の呼び出しをして
			 * mockKeyboard.verify(...)でmockKeyboard というモックオブジェクトに対して
			 * 特定のメソッドが期待通りに呼び出されたかどうかを確認している
			 * times(1)は指定したメソッドが正確に1回呼び出されたことを確認するためのメソッド
			 */

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateNpcOfJankenPlayer_01() 異常系
	 * void createNpcOfJankenPlayer() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULLの場合、SystemException(実行データ不良)が発行されること
	 * --条件--
	 *	playerListフィールドの値がnull
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.SystemExceptionのメッセージ内容が正しいこと
	 */
	@Test
	public void testCreateNpcOfJankenPlayer_01() {

		//playerListがNULLの場合
		jankenCuiGameApplicationImpl.setPlayerList(nullPlayerList);

		try {
			jankenCuiGameApplicationImpl.createNpcOfJankenPlayer(); //テストメソッドの実行
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg()); //検証
		}

	}

	/**
	 * testCreateNpcOfJankenPlayer_02() 異常系
	 * void createNpcOfJankenPlayer() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で0～10以外の整数を入力した場合、
	 * getInt()メソッドがApplicationExceptionを発行する。
	 * その場合、再入力を促すメッセージを出力し、処理を繰り返す
	 * --条件--
	 * playerListがNULL以外の場合で
	 * getInt()にApplicationExceptionを発行させた場合
	 * 再入力を促すメッセージを出力し、処理を繰り返す。
	 * 正しい数値が入るまで無限ループしてしまうのでApplicationExceptionを呼んだ後、getInt()の引数に正常値0を送り処理は正常に終了させる
	 * --検証事項--
	 * 1.0を送るのでプレーヤーリストサイズが0のままであることを確認
	 * 2.再入力を促すメッセージを出力し、処理を繰り返すことを確認する。
	 */
	@Test
	public void testCreateNpcOfJankenPlayer_02() {

		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			//getInt()がApplicationExceptionを発行→そのあと0という正常値をかえす
			mockKeyboard.when(() -> Keyboard.getInt(0, 10)).thenThrow(new ApplicationException("")).thenReturn(0);

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			/*
			 * -----解説------
			 * ByteArrayOutputStreamは、
			 * バイトデータをメモリ内に蓄積するためのストリーム。
			 * OutputStreamの一種で、書き込まれたデータはすぐにメモリに保存され、後でその内容を取得できます。
			 * 例えば、標準出力に出力される文字列を、メモリ内に保持しておくことができます。
			 *
			 * System.setOut(PrintStream out)は、
			 * Javaの標準出力(通常はコンソール)を指定したPrintStreamにリダイレクトするためのメソッド。
			 * ここでnew PrintStream(out)を使用することで、標準出力がByteArrayOutputStreamに書き込まれるようになります。
			 * つまり、System.out.println()などで出力された内容は、コンソールに表示されるのではなく、outに保存されます。
			 */

			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化
			jankenCuiGameApplicationImpl.createNpcOfJankenPlayer(); // テストメソッドの実行

			//検証
			List<JankenPlayer> playerList = jankenCuiGameApplicationImpl.getPlayerList();

			assertEquals(0, playerList.size()); // プレーヤーリストが0のままならOK

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("NPCプレーヤー数を入力してください。" + System.lineSeparator()
					+ "入力した値が不正です。再入力をお願いします。" + System.lineSeparator()
					+ "NPCプレーヤー数を入力してください。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateNpcOfJankenPlayer_03() 正常系
	 * void createNpcOfJankenPlayer() throws SystemException
	 * 
	 * --確認事項-- 
	 * playerListがNULL以外の場合で整数0を入力した場合
	 * 入力した整数分のオブジェクト生成されること
	 * --条件--
	 *  playerListがNULL以外の場合
	 *  整数0を入力した場合
	 * --検証事項--
	 * 1.playerListが0であること
	 * 2.モックオブジェクトに対してKeyboard.getInt(0, 10)が1回呼び出されていること
	 */
	@Test
	public void testCreateNpcOfJankenPlayer_03() {
		// モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			// 0を返すように設定
			mockKeyboard.when(() -> Keyboard.getInt(0, 10)).thenReturn(this.zero);

			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

			jankenCuiGameApplicationImpl.createNpcOfJankenPlayer(); // テストメソッドの実行

			// 検証
			List<JankenPlayer> playerList = jankenCuiGameApplicationImpl.getPlayerList();
			assertEquals(0, playerList.size());
			mockKeyboard.verify(() -> Keyboard.getInt(0, 10), times(1));

			/* 
			 *  -----解説------
			 * () -> Keyboard.getInt(0, 10)の部分は
			 * ラムダ式でKeyboard.getInt(0, 10) の呼び出しをして
			 * mockKeyboard.verify(...)でmockKeyboard というモックオブジェクトに対して
			 * 特定のメソッドが期待通りに呼び出されたかどうかを確認している
			 * times(1)は指定したメソッドが正確に1回呼び出されたことを確認するためのメソッド
			 */

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateNpcOfJankenPlayer_04()  正常系
	 * void createNpcOfJankenPlayer() throws SystemException
	 * 
	 * --確認事項-- 
	 * playerListがNULL以外の場合で整数5を入力した場合
	 * 入力した整数分のオブジェクト生成されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * 整数5を入力した場合
	 * --検証事項--
	 * 1.playerListが5であること
	 * 2.モックオブジェクトに対してKeyboard.getInt(0, 10)が1回呼び出されていること
	 */
	@Test
	public void testCreateNpcOfJankenPlayer_04() {
		// モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			// 10を返すように設定
			mockKeyboard.when(() -> Keyboard.getInt(0, 10)).thenReturn(this.five);

			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

			jankenCuiGameApplicationImpl.createNpcOfJankenPlayer(); // テストメソッドの実行

			// 検証
			List<JankenPlayer> playerList = jankenCuiGameApplicationImpl.getPlayerList();
			assertEquals(5, playerList.size());
			mockKeyboard.verify(() -> Keyboard.getInt(0, 10), times(1));

			/* 
			 *  -----解説------
			 * () -> Keyboard.getInt(0, 10)の部分は
			 * ラムダ式でKeyboard.getInt(0, 10) の呼び出しをして
			 * mockKeyboard.verify(...)でmockKeyboard というモックオブジェクトに対して
			 * 特定のメソッドが期待通りに呼び出されたかどうかを確認している
			 * times(1)は指定したメソッドが正確に1回呼び出されたことを確認するためのメソッド
			 */

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateNpcOfJankenPlayer_05()  正常系
	 * void createNpcOfJankenPlayer() throws SystemException
	 * 
	 * --確認事項-- 
	 * playerListがNULL以外の場合で整数10を入力した場合
	 * 入力した整数分のオブジェクト生成されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * 整数10を入力した場合
	 * --検証事項--
	 * 1.playerListが10であること
	 * 2.モックオブジェクトに対してKeyboard.getInt(0, 10)が1回呼び出されていること
	 */
	@Test
	public void testCreateNpcOfJankenPlayer_05() {
		// モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			// 10を返すように設定
			mockKeyboard.when(() -> Keyboard.getInt(0, 10)).thenReturn(this.ten);

			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

			jankenCuiGameApplicationImpl.createNpcOfJankenPlayer(); // テストメソッドの実行

			// 検証
			List<JankenPlayer> playerList = jankenCuiGameApplicationImpl.getPlayerList();
			assertEquals(10, playerList.size());
			mockKeyboard.verify(() -> Keyboard.getInt(0, 10), times(1));

			/* 
			 *  -----解説------
			 * () -> Keyboard.getInt(0, 10)の部分は
			 * ラムダ式でKeyboard.getInt(0, 10) の呼び出しをして
			 * mockKeyboard.verify(...)でmockKeyboard というモックオブジェクトに対して
			 * 特定のメソッドが期待通りに呼び出されたかどうかを確認している
			 * times(1)は指定したメソッドが正確に1回呼び出されたことを確認するためのメソッド
			 */

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectPlayerHand_01() 異常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項-
	 * playerListがNULLの場合、SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULLの場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.SystemExceptionのメッセージ内容が正しいこと
	 */
	@Test
	public void testSelectPlayerHand_01() {
		//playerListがNULLの場合
		jankenCuiGameApplicationImpl.setPlayerList(nullPlayerList);

		try {
			jankenCuiGameApplicationImpl.selectPlayerHand(); //テストメソッドの実行
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg()); //検証
		}

	}

	/**
	 * testSelectPlayerHand_02()  異常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)、
	 * Jankenプレーヤーオブジェクトが1の場合、
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * リストにhumanPlayerを1人追加する
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.SystemExceptionのメッセージ内容が正しいこと
	 */
	@Test
	public void testSelectPlayerHand_02() {

		//人間プレーヤーの数を定義
		final int humanObject = 1;

		// playerListを初期化
		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

		//emptyPlayerListにhumanPlayersをhumanObject分追加
		for (int i = 0; i < humanObject; i++) {
			emptyPlayerList.add(humanPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.selectPlayerHand(); //テストメソッドの実行
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg()); //検証
		}

	}

	/**
	 * testSelectPlayerHand_03() 正常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * Jankenプレーヤー数の数分、selectPlayerHand()が実行されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにmockHumanPlayerを2人追加する
	 * --検証事項--
	 * Jankenプレーヤー数の数分、selectPlayerHand()が実行されていること
	 */
	@Test
	public void testSelectPlayerHand_03() {

		final int humanObject = 2;
		final int npcObject = 0;
		final int jankenPlayerObject = humanObject + npcObject;

		// playerListを初期化
		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

		// モック化
		//mockHumanPlayerをhumanObject分emptyPlayerListに追加
		for (int i = 0; i < humanObject; i++) {
			JankenPlayer mockHumanPlayer = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayer);
		}

		try {
			for (int i = 0; i < jankenPlayerObject; i++) {
				doNothing().when(emptyPlayerList.get(i)).selectPlayerHand();
			}

			/*
			 * doNothing()は、
			 * Mockitoのテスティングフレームワークのメソッドで、
			 * モックオブジェクトに対する特定のメソッド呼び出しをスタブ(定義)する際に使用されます。
			 * 具体的には、モック化されたオブジェクトのメソッドが呼ばれたときに「何もしない」ように設定します。
			 */

			// テストメソッド実行
			jankenCuiGameApplicationImpl.selectPlayerHand();

			// 検証(Jankenプレーヤー数の数分、selectPlayerHand()が実行されていること)
			for (int i = 0; i < jankenPlayerObject; i++) {
				verify(emptyPlayerList.get(i)).selectPlayerHand();
			}

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectPlayerHand_04() 正常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが5の場合
	 * Jankenプレーヤー数の数分、selectPlayerHand()が実行されていること
	 * --条件--
	 * playerListがNULL以外の場合 
	 * PlayerListにmockHumanPlayerを5人追加する
	 * --検証事項--
	 * Jankenプレーヤー数の数分、selectPlayerHand()が実行されていること
	 */
	@Test
	public void testSelectPlayerHand_04() {

		final int humanObject = 5;
		final int npcObject = 0;
		final int jankenPlayerObject = humanObject + npcObject;

		// playerListを初期化
		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

		// モック化
		//mockHumanPlayerをhumanObject分emptyPlayerListに追加
		for (int i = 0; i < humanObject; i++) {
			JankenPlayer mockHumanPlayer = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayer);
		}

		try {
			for (int i = 0; i < jankenPlayerObject; i++) {
				doNothing().when(emptyPlayerList.get(i)).selectPlayerHand();
			}

			// テストメソッド実行
			jankenCuiGameApplicationImpl.selectPlayerHand();

			// 検証(Jankenプレーヤー数の数分、selectPlayerHand()が実行されていること)
			for (int i = 0; i < jankenPlayerObject; i++) {
				verify(emptyPlayerList.get(i)).selectPlayerHand();
			}

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectPlayerHand_05() 正常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * Jankenプレーヤー数の数分、selectPlayerHand()が実行されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにmockHumanPlayerを10人追加する
	 * --検証事項--
	 * Jankenプレーヤー数の数分、selectPlayerHand()が実行されていること
	 */
	@Test
	public void testSelectPlayerHand_05() {

		final int humanObject = 10;
		final int npcObject = 0;
		final int jankenPlayerObject = humanObject + npcObject;

		// playerListを初期化
		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

		// モック化
		//mockHumanPlayerをhumanObject分emptyPlayerListに追加
		for (int i = 0; i < humanObject; i++) {
			JankenPlayer mockHumanPlayer = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayer);
		}

		try {
			for (int i = 0; i < jankenPlayerObject; i++) {
				doNothing().when(emptyPlayerList.get(i)).selectPlayerHand();
			}

			// テストメソッド実行
			jankenCuiGameApplicationImpl.selectPlayerHand();

			// 検証
			for (int i = 0; i < jankenPlayerObject; i++) {
				verify(emptyPlayerList.get(i)).selectPlayerHand();
			}

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectPlayerHand_06() 異常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが11の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを11人追加する
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.SystemExceptionのメッセージ内容が正しいこと
	 */
	@Test
	public void testSelectPlayerHand_06() {

		final int humanObject = 11;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		//emptyPlayerListにhumanPlayersをhumanObject分追加
		for (int i = 0; i < humanObject; i++) {
			emptyPlayerList.add(humanPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.selectPlayerHand();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testSelectPlayerHand_07() 異常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)、
	 * Jankenプレーヤーオブジェクトが1の場合、
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * リストにnpcPlayerを1人追加する
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.SystemExceptionのメッセージ内容が正しいこと
	 */
	@Test
	public void testSelectPlayerHand_07() {

		final int npcObject = 1;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		//emptyPlayerListにnpcPlayersをnpcObject分追加
		for (int i = 0; i < npcObject; i++) {
			emptyPlayerList.add(npcPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.selectPlayerHand(); //テストメソッドの実行
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg()); //検証
		}

	}

	/**
	 * testSelectPlayerHand08() 正常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * オブジェクトの数分、selectPlayerHand()が行われること
	 * --条件--
	 * playerListがNULL以外の場合
	 * npcObjectが2の場合
	 * --検証事項--
	 * オブジェクトの数分、selectPlayerHand()が行われること
	 */
	@Test
	public void testSelectPlayerHand_08() {

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

		final int humanObject = 0;
		final int npcObject = 2;
		final int jankenPlayerObject = humanObject + npcObject;

		// モック化
		//mockNpcPlayerをnpcObject分emptyPlayerListに追加
		for (int i = 0; i < npcObject; i++) {
			JankenPlayer mockNpcPlayer = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayer);
		}

		try {
			for (int i = 0; i < jankenPlayerObject; i++) {
				doNothing().when(emptyPlayerList.get(i)).selectPlayerHand();
			}

			// テストメソッド実行
			jankenCuiGameApplicationImpl.selectPlayerHand();

			// 検証
			for (int i = 0; i < jankenPlayerObject; i++) {
				verify(emptyPlayerList.get(i)).selectPlayerHand();
			}

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectPlayerHand09() 正常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが5の場合
	 * オブジェクトの数分、selectPlayerHand()が行われること
	 * --条件--
	 * playerListがNULL以外の場合
	 * npcObjectが5の場合
	 * --検証事項--
	 * オブジェクトの数分、selectPlayerHand()が行われること
	 */
	@Test
	public void testSelectPlayerHand_09() {

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

		final int humanObject = 0;
		final int npcObject = 5;
		final int jankenPlayerObject = humanObject + npcObject;

		// モック化
		//mockNpcPlayerをnpcObject分emptyPlayerListに追加
		for (int i = 0; i < npcObject; i++) {
			JankenPlayer mockNpcPlayer = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayer);
		}

		try {
			for (int i = 0; i < jankenPlayerObject; i++) {
				doNothing().when(emptyPlayerList.get(i)).selectPlayerHand();
			}

			// テストメソッド実行
			jankenCuiGameApplicationImpl.selectPlayerHand();

			// 検証
			for (int i = 0; i < jankenPlayerObject; i++) {
				verify(emptyPlayerList.get(i)).selectPlayerHand();
			}

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectPlayerHand10() 正常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * オブジェクトの数分、selectPlayerHand()が行われること
	 * --条件--
	 * playerListがNULL以外の場合
	 * npcObjectが10の場合
	 * --検証事項--
	 * オブジェクトの数分、selectPlayerHand()が行われること
	 */
	@Test
	public void testSelectPlayerHand_10() {

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

		final int humanObject = 0;
		final int npcObject = 10;
		final int jankenPlayerObject = humanObject + npcObject;

		// モック化
		//mockNpcPlayerをnpcObject分emptyPlayerListに追加
		for (int i = 0; i < npcObject; i++) {
			JankenPlayer mockNpcPlayer = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayer);
		}

		try {
			for (int i = 0; i < jankenPlayerObject; i++) {
				doNothing().when(emptyPlayerList.get(i)).selectPlayerHand();
			}

			// テストメソッド実行
			jankenCuiGameApplicationImpl.selectPlayerHand();

			// 検証
			for (int i = 0; i < jankenPlayerObject; i++) {
				verify(emptyPlayerList.get(i)).selectPlayerHand();
			}

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectPlayerHand_OverObject11() 異常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが11の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを11人追加する
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.SystemExceptionのメッセージ内容が正しいこと
	 */
	@Test
	public void testSelectPlayerHand_11() {

		final int npcObject = 11;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		//emptyPlayerListにnpcObject分追加
		for (int i = 0; i < npcObject; i++) {
			emptyPlayerList.add(npcPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.selectPlayerHand();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testSelectPlayerHand_12() 正常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * オブジェクトの数分、selectPlayerHand()が行われること
	 * --条件--
	 * playerListがNULL以外の場合
	 * humanObjectが1,npcObjectが1の場合
	 * --検証事項--
	 * オブジェクトの数分、selectPlayerHand()が行われること
	 */
	@Test
	public void testSelectPlayerHand_12() {

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

		final int humanObject = 1;
		final int npcObject = 1;
		final int jankenPlayerObject = humanObject + npcObject;

		// モック化
		for (int i = 0; i < humanObject; i++) {
			JankenPlayer mockHumanPlayer = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayer);
		}

		// モック化
		for (int i = 0; i < npcObject; i++) {
			JankenPlayer mockNpcPlayer = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayer);
		}

		try {
			for (int i = 0; i < jankenPlayerObject; i++) {
				doNothing().when(emptyPlayerList.get(i)).selectPlayerHand();
			}

			// テストメソッド実行
			jankenCuiGameApplicationImpl.selectPlayerHand();

			// 検証
			for (int i = 0; i < jankenPlayerObject; i++) {
				verify(emptyPlayerList.get(i)).selectPlayerHand();
			}

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectPlayerHand_13() 正常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが5の場合
	 * オブジェクトの数分、selectPlayerHand()が行われること
	 * --条件--
	 * playerListがNULL以外の場合
	 * humanObjectが2,npcObjectが3の場合
	 * --検証事項--
	 * オブジェクトの数分、selectPlayerHand()が行われること
	 */
	@Test
	public void testSelectPlayerHand_13() {

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

		final int humanObject = 2;
		final int npcObject = 3;
		final int jankenPlayerObject = humanObject + npcObject;

		// モック化
		for (int i = 0; i < humanObject; i++) {
			JankenPlayer mockHumanPlayer = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayer);
		}

		// モック化
		for (int i = 0; i < npcObject; i++) {
			JankenPlayer mockNpcPlayer = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayer);
		}

		try {
			for (int i = 0; i < jankenPlayerObject; i++) {
				doNothing().when(emptyPlayerList.get(i)).selectPlayerHand();
			}

			// テストメソッド実行
			jankenCuiGameApplicationImpl.selectPlayerHand();

			// 検証
			for (int i = 0; i < jankenPlayerObject; i++) {
				verify(emptyPlayerList.get(i)).selectPlayerHand();
			}

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectPlayerHand_14() 正常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * オブジェクトの数分、selectPlayerHand()が行われること
	 * --条件--
	 * playerListがNULL以外の場合
	 * humanObjectが5,npcObjectが5の場合
	 * --検証事項--
	 * オブジェクトの数分、selectPlayerHand()が行われること
	 */
	@Test
	public void testSelectPlayerHand_14() {

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

		final int humanObject = 5;
		final int npcObject = 5;
		final int jankenPlayerObject = humanObject + npcObject;

		// モック化
		for (int i = 0; i < humanObject; i++) {
			JankenPlayer mockHumanPlayer = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayer);
		}

		// モック化
		for (int i = 0; i < npcObject; i++) {
			JankenPlayer mockNpcPlayer = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayer);
		}

		try {
			for (int i = 0; i < jankenPlayerObject; i++) {
				doNothing().when(emptyPlayerList.get(i)).selectPlayerHand();
			}

			// テストメソッド実行
			jankenCuiGameApplicationImpl.selectPlayerHand();

			// 検証
			for (int i = 0; i < jankenPlayerObject; i++) {
				verify(emptyPlayerList.get(i)).selectPlayerHand();
			}

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectPlayerHand_15() 異常系
	 * void selectPlayerHand() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが11の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件-
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを6人分追加する
	 * PlayerListにnpcPlayersを5人分追加する
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.SystemExceptionのメッセージ内容が正しいこと
	 */
	@Test
	public void testSelectPlayerHand_15() {

		final int npcPlayersCount = 5;
		final int humanPlayersCount = 6;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		for (int i = 0; i < humanPlayersCount; i++) {
			emptyPlayerList.add(humanPlayers[i]);
		}

		for (int i = 0; i < npcPlayersCount; i++) {
			emptyPlayerList.add(npcPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.selectPlayerHand();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_01() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULLの場合、SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_01() {
		//playerListがNULLの場合
		jankenCuiGameApplicationImpl.setPlayerList(nullPlayerList);

		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_02() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが1の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを1人追加
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_02() {

		final int humanPlayersCount = 1;

		// playerListを初期化
		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

		//emptyPlayerListにhumanPlayersを1人分追加
		for (int i = 0; i < humanPlayersCount; i++) {
			emptyPlayerList.add(humanPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_03() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で0の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で0の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_03() {

		final int humanObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.draw);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.draw);

		//テストメソッド
		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_04() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で4の場合
	 * JankenParamクラスのgetEnum()でSystemExceptionになる
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で4の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_04() {

		final int humanObject = 2;

		// playerListを初期化
		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.errorHand);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.errorHand);

		//テストメソッド
		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_05() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(グー)
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(グー)
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_05() {

		final int humanObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);

		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}
	}

	/**
	 * testJudge_06() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(チョキ)
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(チョキ)
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_06() {

		final int humanObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}
	}

	/**
	 * testJudge_07() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(パー)
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(パー)
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_07() {

		final int humanObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_08() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(グーとパー)
	 * paperを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(グーとパー)
	 * --検証事項--
	 * 1.paperを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_08() {

		final int humanObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.paper, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}
	}

	/**
	 * testJudge_09() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(チョキとグー)
	 * rockを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(チョキとグー)
	 * --検証事項--
	 * 1.rockを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_09() {

		final int humanObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.rock, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}
	}

	/**
	 * testJudge_10() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(パーとチョキ)
	 * scissorsを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(パーとチョキ)
	 * --検証事項--
	 * 1.scissorsを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_10() {

		final int humanObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.scissors, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}
	}

	/**
	 * testJudge_11() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で0の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で0の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_11() {

		final int humanObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < humanObject; i++) {
			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.draw);
		}

		//テストメソッド
		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_12() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で4の場合
	 * JankenParamクラスのgetEnum()でSystemExceptionになる
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で4の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_12() {

		final int humanObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}
		for (int i = 0; i < humanObject; i++) {
			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.errorHand);
		}

		//テストメソッド
		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_13() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(グー)
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(グー)
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge13() {

		final int humanObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}
		for (int i = 0; i < humanObject; i++) {
			when(emptyPlayerList.get(i).getPlayerHand()).thenReturn(this.rock);
		}
		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_14() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(チョキ)
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(チョキ)
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge14() {

		final int humanObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}
		for (int i = 0; i < humanObject; i++) {
			when(emptyPlayerList.get(i).getPlayerHand()).thenReturn(this.scissors);
		}
		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_15() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(パー)
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(パー)
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge15() {

		final int humanObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}
		for (int i = 0; i < humanObject; i++) {
			when(emptyPlayerList.get(i).getPlayerHand()).thenReturn(this.paper);
		}
		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_16() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(グーとパー)
	 * paperを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(グーとパー)
	 * --検証事項--
	 * 1.paperを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge16() {

		final int humanObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.paper);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.paper, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_17() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(チョキとグー)
	 * rockを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(チョキとグー)
	 * --検証事項--
	 * 1.rockを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge17() {

		final int humanObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.rock, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_18() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(パーとチョキ)
	 * scissorsを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(パーとチョキ)
	 * --検証事項--
	 * 1.scissorsを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge18() {

		final int humanObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.scissors, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_19() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * JankenParamの値がすべて異なる場合
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを10人追加
	 * JankenParamの値がすべて異なる場合
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge19() {

		final int humanObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_20() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが11の場合
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを11人追加
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */

	@Test
	public void testJudge20() {

		final int humanObject = 11;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_21() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが1の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを1人追加
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_21() {

		final int npcObject = 1;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_22() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で0の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で0の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_22() {

		final int npcObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.draw);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.draw);

		//テストメソッド
		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_23() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で4の場合
	 * JankenParamクラスのgetEnum()でSystemExceptionになる
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で4の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_23() {

		final int npcObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.errorHand);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.errorHand);

		//テストメソッド
		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_24() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(グー)
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(グー)
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_24() {

		final int npcObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);

		for (int i = 0; i < npcObject; i++) {
			verify(mockNpcPlayers[i], times(1)).getPlayerHand();
		}
	}

	/**
	 * testJudge_25() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(チョキ)
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(チョキ)
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_25() {

		final int npcObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);
		for (int i = 0; i < npcObject; i++) {
			verify(mockNpcPlayers[i], times(1)).getPlayerHand();
		}
	}

	/**
	 * testJudge_26() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(パー)
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(パー)
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_26() {

		final int npcObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);
		for (int i = 0; i < npcObject; i++) {
			verify(mockNpcPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_27() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(グーとパー)
	 * paperを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(グーとパー)
	 * --検証事項--
	 * 1.paperを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_27() {

		final int npcObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.paper, result);
		for (int i = 0; i < npcObject; i++) {
			verify(mockNpcPlayers[i], times(1)).getPlayerHand();
		}
	}

	/**
	 * testJudge_28() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(チョキとグー)
	 * rockを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(チョキとグー)
	 * --検証事項--
	 * 1.rockを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_28() {

		final int npcObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.rock, result);
		for (int i = 0; i < npcObject; i++) {
			verify(mockNpcPlayers[i], times(1)).getPlayerHand();
		}
	}

	/**
	 * testJudge_29() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(パーとチョキ)
	 * scissorsを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを2人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(パーとチョキ)
	 * --検証事項--
	 * 1.scissorsを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_29() {

		final int npcObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.scissors, result);
		for (int i = 0; i < npcObject; i++) {
			verify(mockNpcPlayers[i], times(1)).getPlayerHand();
		}
	}

	/**
	 * testJudge_30() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で0の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で0の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_30() {

		final int npcObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.draw);
		}

		//テストメソッド
		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_31() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で4の場合
	 * JankenParamクラスのgetEnum()でSystemExceptionになる
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で4の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_31() {

		final int npcObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}
		for (int i = 0; i < npcObject; i++) {
			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.errorHand);
		}

		//テストメソッド
		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_32() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(グー)
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(グー)
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge32() {

		final int npcObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}
		for (int i = 0; i < npcObject; i++) {
			when(emptyPlayerList.get(i).getPlayerHand()).thenReturn(this.rock);
		}
		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);
		for (int i = 0; i < npcObject; i++) {
			verify(mockNpcPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_33() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(チョキ)
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(チョキ)
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge33() {

		final int npcObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}
		for (int i = 0; i < npcObject; i++) {
			when(emptyPlayerList.get(i).getPlayerHand()).thenReturn(this.scissors);
		}
		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);
		for (int i = 0; i < npcObject; i++) {
			verify(mockNpcPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_34() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(パー)
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(パー)
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_34() {

		final int npcObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}
		for (int i = 0; i < npcObject; i++) {
			when(emptyPlayerList.get(i).getPlayerHand()).thenReturn(this.paper);
		}
		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);
		for (int i = 0; i < npcObject; i++) {
			verify(mockNpcPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_35() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(グーとパー)
	 * paperを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(グーとパー)
	 * --検証事項--
	 * 1.paperを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge35() {

		final int npcObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.paper);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.paper, result);
		for (int i = 0; i < npcObject; i++) {
			verify(mockNpcPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_36() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(チョキとグー)
	 * rockを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(チョキとグー)
	 * --検証事項--
	 * 1.rockを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge36() {

		final int npcObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.rock, result);
		for (int i = 0; i < npcObject; i++) {
			verify(mockNpcPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_37() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(パーとチョキ)
	 * scissorsを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを10人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(パーとチョキ)
	 * --検証事項--
	 * 1.scissorsを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge37() {

		final int humanObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.scissors, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_38() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * JankenParamの値がすべて異なる場合
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * JankenParamの値がすべて異なる場合
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge38() {

		final int humanObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);
		for (int i = 0; i < humanObject; i++) {
			verify(mockHumanPlayers[i], times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_39() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが11の場合
	 * JankenParamの値がすべて異なる場合
	 * DRAWを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを11人追加
	 * JankenParamの値がすべて異なる場合
	 * --検証事項--
	 * 1.drawを返すことを確認
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge39() {

		final int npcObject = 11;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_40() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で0の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを1人追加
	 * PlayerListにnpcPlayersを1人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で0の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_40() {

		final int humanObject = 1;
		final int npcObject = 1;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.draw);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.draw);

		//テストメソッド
		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_41() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で4の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを1人追加
	 * PlayerListにnpcPlayersを1人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で4の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_41() {

		final int humanObject = 1;
		final int npcObject = 1;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.errorHand);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.errorHand);

		//テストメソッド
		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_42() 正常系
	 * int judge() throws SystemException 
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1種類の場合(グー)
	 * drawを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを1人追加
	 * PlayerListにnpcPlayersを1人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1種類の場合(グー)
	 * --検証事項--
	 * 1.drawを返すこと
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_42() {

		final int humanObject = 1;
		final int npcObject = 1;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);

		for (int i = 0; i < emptyPlayerList.size(); i++) {
			JankenPlayer jankenPlayer = emptyPlayerList.get(i);
			verify(jankenPlayer, times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_43() 正常系
	 * int judge() throws SystemException 
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1種類の場合(チョキ)
	 * drawを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを1人追加
	 * PlayerListにnpcPlayersを1人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1種類の場合(チョキ)
	 * --検証事項--
	 * 1.drawを返すこと
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_43() {

		final int humanObject = 1;
		final int npcObject = 1;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);

		for (int i = 0; i < emptyPlayerList.size(); i++) {
			JankenPlayer jankenPlayer = emptyPlayerList.get(i);
			verify(jankenPlayer, times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_44() 正常系
	 * int judge() throws SystemException 
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1種類の場合(パー)
	 * drawを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを1人追加
	 * PlayerListにnpcPlayersを1人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1種類の場合(パー)
	 * --検証事項--
	 * 1.drawを返すこと
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_44() {

		final int humanObject = 1;
		final int npcObject = 1;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);

		for (int i = 0; i < emptyPlayerList.size(); i++) {
			JankenPlayer jankenPlayer = emptyPlayerList.get(i);
			verify(jankenPlayer, times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_45() 正常系
	 * int judge() throws SystemException 
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(グーとパー)
	 * paperを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを1人追加
	 * PlayerListにnpcPlayersを1人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(グーとパー)
	 * --検証事項--
	 * 1.paperを返すこと
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_45() {

		final int humanObject = 1;
		final int npcObject = 1;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.paper, result);

		for (int i = 0; i < emptyPlayerList.size(); i++) {
			JankenPlayer jankenPlayer = emptyPlayerList.get(i);
			verify(jankenPlayer, times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_46() 正常系
	 * int judge() throws SystemException 
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(チョキとグー)
	 * rockを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを1人追加
	 * PlayerListにnpcPlayersを1人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(チョキとグー)
	 * --検証事項--
	 * 1.rockを返すこと
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_46() {

		final int humanObject = 1;
		final int npcObject = 1;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.rock, result);

		for (int i = 0; i < emptyPlayerList.size(); i++) {
			JankenPlayer jankenPlayer = emptyPlayerList.get(i);
			verify(jankenPlayer, times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_47() 正常系
	 * int judge() throws SystemException 
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(パーとチョキ)
	 * scissorsを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを1人追加
	 * PlayerListにnpcPlayersを1人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(パーとチョキ)
	 * --検証事項--
	 * 1.scissorsを返すこと
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_47() {

		final int humanObject = 1;
		final int npcObject = 1;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.scissors, result);

		for (int i = 0; i < emptyPlayerList.size(); i++) {
			JankenPlayer jankenPlayer = emptyPlayerList.get(i);
			verify(jankenPlayer, times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_48() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で0の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを5人追加
	 * PlayerListにnpcPlayersを5人追加 * 
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で0の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_48() {

		final int humanObject = 5;
		final int npcObject = 5;
		final int jankenObject = humanObject + npcObject;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		for (int i = 0; i < jankenObject; i++) {
			when(emptyPlayerList.get(i).getPlayerHand()).thenReturn(this.draw);
		}

		//テストメソッド
		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testJudge_49() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で4の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを5人追加
	 * PlayerListにnpcPlayersを5人追加 * 
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3以外の場合で4の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_49() {

		final int humanObject = 5;
		final int npcObject = 5;
		final int jankenObject = humanObject + npcObject;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		for (int i = 0; i < jankenObject; i++) {
			when(emptyPlayerList.get(i).getPlayerHand()).thenReturn(this.errorHand);
		}

		//テストメソッド
		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}
	}

	/**
	 * testJudge_50() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1種類の場合(グー)
	 * drawを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを5人追加
	 * PlayerListにnpcPlayersを5人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1～3の場合で1種類の場合(グー)
	 * --検証事項--
	 * 1.drawを返すこと
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_50() {

		final int humanObject = 5;
		final int npcObject = 5;
		final int jankenObject = humanObject + npcObject;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		for (int i = 0; i < jankenObject; i++) {
			when(emptyPlayerList.get(i).getPlayerHand()).thenReturn(this.rock);
		}
		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);

		for (int i = 0; i < emptyPlayerList.size(); i++) {
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_51() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1種類の場合(チョキ)
	 * drawを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを5人追加
	 * PlayerListにnpcPlayersを5人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1種類の場合(チョキ)
	 * --検証事項--
	 * 1.drawを返すこと
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_51() {

		final int humanObject = 5;
		final int npcObject = 5;
		final int jankenObject = humanObject + npcObject;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		for (int i = 0; i < jankenObject; i++) {
			when(emptyPlayerList.get(i).getPlayerHand()).thenReturn(this.scissors);
		}
		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);

		for (int i = 0; i < emptyPlayerList.size(); i++) {
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_52() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が1種類の場合(パー)
	 * drawを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを5人追加
	 * PlayerListにnpcPlayersを5人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が1種類の場合(パー)
	 * --検証事項--
	 * 1.drawを返すこと
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_52() {

		final int humanObject = 5;
		final int npcObject = 5;
		final int jankenObject = humanObject + npcObject;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		for (int i = 0; i < jankenObject; i++) {
			when(emptyPlayerList.get(i).getPlayerHand()).thenReturn(this.paper);
		}
		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);

		for (int i = 0; i < emptyPlayerList.size(); i++) {
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_53() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(グーとパー)
	 * paperを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを5人追加
	 * PlayerListにnpcPlayersを5人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(グーとパー)
	 * --検証事項--
	 * 1.paperを返すこと
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_53() {

		final int humanObject = 5;
		final int npcObject = 5;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.paper);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.paper, result);

		for (int i = 0; i < emptyPlayerList.size(); i++) {
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_54() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(チョキとグー)
	 * rockを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを5人追加
	 * PlayerListにnpcPlayersを5人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(チョキとグー)
	 * --検証事項--
	 * 1.rockを返すこと
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_54() {

		final int humanObject = 5;
		final int npcObject = 5;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.rock, result);

		for (int i = 0; i < emptyPlayerList.size(); i++) {
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_55() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(パーとチョキ)
	 * scissorsを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを5人追加
	 * PlayerListにnpcPlayersを5人追加
	 * プレーヤオブジェクトが持つじゃんけんの手が2種類の場合(パーとチョキ)
	 * --検証事項--
	 * 1.scissorsを返すこと
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_55() {

		final int humanObject = 5;
		final int npcObject = 5;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.scissors, result);

		for (int i = 0; i < emptyPlayerList.size(); i++) {
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
		}
	}

	/**
	 * testJudge_56() 正常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * JankenParamの値がすべて異なる場合
	 * drawを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを5人追加
	 * PlayerListにnpcPlayersを5人追加
	 * JankenParamの値がすべて異なる場合
	 * --検証事項--
	 * 1.drawを返すこと
	 * 2.jankenPlayerの数、getPlayerHand()が呼び出されていること
	 */
	@Test
	public void testJudge_56() {

		final int humanObject = 5;
		final int npcObject = 5;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.paper);
		when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.rock);
		when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);
		when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);

		//テストメソッド
		int result = 0;
		try {
			result = jankenCuiGameApplicationImpl.judge();
		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

		//検証
		assertEquals(this.draw, result);

		for (int i = 0; i < emptyPlayerList.size(); i++) {
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
			verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
		}

	}

	/**
	 * testJudge_57() 異常系
	 * int judge() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが11の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを5人追加
	 * PlayerListにnpcPlayersを6人追加
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testJudge_57() {

		final int humanObject = 5;
		final int npcObject = 6;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.judge();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testViewWinner_01() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULLの場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULLの場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_01() {

		//playerListがNULLの場合
		jankenCuiGameApplicationImpl.setPlayerList(nullPlayerList);

		try {
			jankenCuiGameApplicationImpl.viewWinner(); //テストメソッドの実行
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg()); //検証
		}

	}

	/**
	 * testViewWinner_02() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが1の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを1人追加
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_02() {

		final int humanObject = 1;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		//emptyPlayerListにnpcPlayersを1人分追加
		for (int i = 0; i < humanObject; i++) {
			emptyPlayerList.add(humanPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.viewWinner();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testtViewWinner_03() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手がグーの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * 勝利者の手がグーの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_03() {

		final int humanObject = 2;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			}

			when(mockHumanPlayers[0].getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayers[1].getPlayerHand()).thenReturn(this.scissors);

			for (int i = 0; i < humanObject; i++) {
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]);
			}

			for (int i = 0; i < humanObject; i++) {
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.rock;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner(); //コンソールにて確認??

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < humanObject; i++) {
				verify(mockHumanPlayers[i], times(1)).getPlayerHand();
			}

			// Rock を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < humanObject; i++) {
				if (mockHumanPlayers[i].getPlayerHand() == this.rock) {
					verify(mockHumanPlayers[i], times(1)).getPlayerName();
				}
			}

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.rock == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_04() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手がパーの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * 勝利者の手がチョキの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_04() {

		final int humanObject = 2;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			}

			when(mockHumanPlayers[0].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[1].getPlayerHand()).thenReturn(this.paper);

			for (int i = 0; i < humanObject; i++) {
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]);
			}

			for (int i = 0; i < humanObject; i++) {
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.scissors;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner(); //コンソールにて確認?

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < humanObject; i++) {
				verify(mockHumanPlayers[i], times(1)).getPlayerHand();
			}

			// Rock を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < humanObject; i++) {
				if (mockHumanPlayers[i].getPlayerHand() == this.scissors) {
					verify(mockHumanPlayers[i], times(1)).getPlayerName();
				}
			}
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.scissors == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_05() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手がパーの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * 勝利者の手がパーの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_05() {

		final int humanObject = 2;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); //初期化

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			}

			when(mockHumanPlayers[0].getPlayerHand()).thenReturn(this.paper);
			when(mockHumanPlayers[1].getPlayerHand()).thenReturn(this.rock);

			for (int i = 0; i < humanObject; i++) {
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]);
			}

			for (int i = 0; i < humanObject; i++) {
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.paper;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner(); //コンソールにて確認??

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < humanObject; i++) {
				verify(mockHumanPlayers[i], times(1)).getPlayerHand();
			}

			// Rock を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < humanObject; i++) {
				if (mockHumanPlayers[i].getPlayerHand() == this.paper) {
					verify(mockHumanPlayers[i], times(1)).getPlayerName();
				}
			}
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.paper == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_06() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手がグー・チョキ・パー、あいこの以外の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * 勝利者の手がグー・チョキ・パー、あいこの以外の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_06() {

		final int humanObject = 2;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			}

			for (int i = 0; i < humanObject; i++) {
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			//勝利者の手がグー・チョキ・パーの以外の場合
			jankenCuiGameApplicationImpl.winHand = errorHand;

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testtViewWinner_07() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手がDRAWの場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * 勝利者の手がDRAWの場合
	 * --検証事項--
	  * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_07() {

		final int humanObject = 2;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			}

			for (int i = 0; i < humanObject; i++) {
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			//勝利者の手がグー・チョキ・パーの以外の場合
			jankenCuiGameApplicationImpl.winHand = this.draw;

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testtViewWinner_08() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 勝利者の手がグーの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを10人追加
	 * 勝利者の手がグーの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_08() {

		final int humanObject = 10;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			}
			when(mockHumanPlayers[0].getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayers[1].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[2].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[3].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[4].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[5].getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayers[6].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[7].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[8].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[9].getPlayerHand()).thenReturn(this.scissors);

			for (int i = 0; i < humanObject; i++) {
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]);
			}

			for (int i = 0; i < humanObject; i++) {
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.rock;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < humanObject; i++) {
				verify(mockHumanPlayers[i], times(1)).getPlayerHand();
			}

			// Rock を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < humanObject; i++) {
				if (mockHumanPlayers[i].getPlayerHand() == this.rock) {
					verify(mockHumanPlayers[i], times(1)).getPlayerName();
				}
			}

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.rock == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_09() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 勝利者の手がチョキの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを10人追加
	 * 勝利者の手がチョキの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_09() {

		final int humanObject = 10;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			}
			when(mockHumanPlayers[0].getPlayerHand()).thenReturn(this.paper);
			when(mockHumanPlayers[1].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[2].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[3].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[4].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[5].getPlayerHand()).thenReturn(this.paper);
			when(mockHumanPlayers[6].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[7].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[8].getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayers[9].getPlayerHand()).thenReturn(this.scissors);

			for (int i = 0; i < humanObject; i++) {
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]);
			}

			for (int i = 0; i < humanObject; i++) {
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.scissors;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < humanObject; i++) {
				verify(mockHumanPlayers[i], times(1)).getPlayerHand();
			}

			// Rock を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < humanObject; i++) {
				if (mockHumanPlayers[i].getPlayerHand() == this.scissors) {
					verify(mockHumanPlayers[i], times(1)).getPlayerName();
				}
			}

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.scissors == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_10() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 勝利者の手がパーの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを10人追加
	 * 勝利者の手がパーの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_10() {

		final int humanObject = 10;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			}
			when(mockHumanPlayers[0].getPlayerHand()).thenReturn(this.paper);
			when(mockHumanPlayers[1].getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayers[2].getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayers[3].getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayers[4].getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayers[5].getPlayerHand()).thenReturn(this.paper);
			when(mockHumanPlayers[6].getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayers[7].getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayers[8].getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayers[9].getPlayerHand()).thenReturn(this.rock);

			for (int i = 0; i < humanObject; i++) {
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]);
			}

			for (int i = 0; i < humanObject; i++) {
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.paper;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < humanObject; i++) {
				verify(mockHumanPlayers[i], times(1)).getPlayerHand();
			}

			// Rock を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < humanObject; i++) {
				if (mockHumanPlayers[i].getPlayerHand() == this.paper) {
					verify(mockHumanPlayers[i], times(1)).getPlayerName();
				}
			}

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.paper == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testViewWinner_11() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが11の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを11人追加
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_11() {

		final int humanObject = 11;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		//emptyPlayerListにnpcPlayersを1人分追加
		for (int i = 0; i < humanObject; i++) {
			emptyPlayerList.add(humanPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.viewWinner();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testViewWinner_12() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが1の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを1人追加
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_12() {

		final int npcObject = 1;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		//emptyPlayerListにnpcPlayersを1人分追加
		for (int i = 0; i < npcObject; i++) {
			emptyPlayerList.add(npcPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.viewWinner();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testtViewWinner_03() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手がグーの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを2人追加
	 * 勝利者の手がグーの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_13() {

		final int npcObject = 2;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			}

			when(mockNpcPlayers[0].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[1].getPlayerHand()).thenReturn(this.scissors);

			for (int i = 0; i < npcObject; i++) {
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
			}

			for (int i = 0; i < npcObject; i++) {
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.rock;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < npcObject; i++) {
				verify(mockNpcPlayers[i], times(1)).getPlayerHand();
			}

			// Rock を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < npcObject; i++) {
				if (mockNpcPlayers[i].getPlayerHand() == this.rock) {
					verify(mockNpcPlayers[i], times(1)).getPlayerName();
				}
			}

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.rock == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_14() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手がチョキの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを2人追加
	 * 勝利者の手がチョキの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_14() {

		final int npcObject = 2;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			}

			when(mockNpcPlayers[0].getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayers[1].getPlayerHand()).thenReturn(this.scissors);

			for (int i = 0; i < npcObject; i++) {
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
			}

			for (int i = 0; i < npcObject; i++) {
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.scissors;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < npcObject; i++) {
				verify(mockNpcPlayers[i], times(1)).getPlayerHand();
			}

			// Rock を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < npcObject; i++) {
				if (mockNpcPlayers[i].getPlayerHand() == this.scissors) {
					verify(mockNpcPlayers[i], times(1)).getPlayerName();
				}
			}

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.scissors == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_15() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手がパーの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを2人追加
	 * 勝利者の手がパーの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_15() {

		final int npcObject = 2;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			}

			when(mockNpcPlayers[0].getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayers[1].getPlayerHand()).thenReturn(this.rock);

			for (int i = 0; i < npcObject; i++) {
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
			}

			for (int i = 0; i < npcObject; i++) {
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.rock;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < npcObject; i++) {
				verify(mockNpcPlayers[i], times(1)).getPlayerHand();
			}

			// Rock を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < npcObject; i++) {
				if (mockNpcPlayers[i].getPlayerHand() == this.rock) {
					verify(mockNpcPlayers[i], times(1)).getPlayerName();
				}
			}

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.rock == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_16() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手がグー・チョキ・パー、あいこの以外の場合
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを2人追加
	 * 勝利者の手がグー・チョキ・パー、あいこの以外の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_16() {

		final int npcObject = 2;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			}

			when(mockNpcPlayers[0].getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayers[1].getPlayerHand()).thenReturn(this.rock);

			for (int i = 0; i < npcObject; i++) {
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
			}

			for (int i = 0; i < npcObject; i++) {
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.errorHand;

			//テストメソッド

			jankenCuiGameApplicationImpl.viewWinner();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testtViewWinner_17() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手があいこの場合
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを2人追加
	 * 勝利者の手があいこの場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_17() {

		final int npcObject = 2;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			}

			when(mockNpcPlayers[0].getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayers[1].getPlayerHand()).thenReturn(this.paper);

			for (int i = 0; i < npcObject; i++) {
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
			}

			for (int i = 0; i < npcObject; i++) {
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.draw;

			//テストメソッド

			jankenCuiGameApplicationImpl.viewWinner();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testtViewWinner_18() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 勝利者の手がグーの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * 勝利者の手がグーの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_18() {

		final int npcObject = 10;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			}
			when(mockNpcPlayers[0].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[1].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[2].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[3].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[4].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[5].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[6].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[7].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[8].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[9].getPlayerHand()).thenReturn(this.scissors);

			for (int i = 0; i < npcObject; i++) {
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
			}

			for (int i = 0; i < npcObject; i++) {
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.rock;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < npcObject; i++) {
				verify(mockNpcPlayers[i], times(1)).getPlayerHand();
			}

			// Rock を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < npcObject; i++) {
				if (mockNpcPlayers[i].getPlayerHand() == this.rock) {
					verify(mockNpcPlayers[i], times(1)).getPlayerName();
				}
			}

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.rock == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_19() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 勝利者の手がチョキの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * 勝利者の手がチョキの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_19() {

		final int npcObject = 10;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			}
			when(mockNpcPlayers[0].getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayers[1].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[2].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[3].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[4].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[5].getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayers[6].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[7].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[8].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[9].getPlayerHand()).thenReturn(this.scissors);

			for (int i = 0; i < npcObject; i++) {
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
			}

			for (int i = 0; i < npcObject; i++) {
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.scissors;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < npcObject; i++) {
				verify(mockNpcPlayers[i], times(1)).getPlayerHand();
			}

			// scissors を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < npcObject; i++) {
				if (mockNpcPlayers[i].getPlayerHand() == this.scissors) {
					verify(mockNpcPlayers[i], times(1)).getPlayerName();
				}
			}

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.scissors == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_20() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 勝利者の手がパーの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * 勝利者の手がパーの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_20() {

		final int npcObject = 10;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // 初期化

			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			}
			when(mockNpcPlayers[0].getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayers[1].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[2].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[3].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[4].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[5].getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayers[6].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[7].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[8].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[9].getPlayerHand()).thenReturn(this.rock);

			for (int i = 0; i < npcObject; i++) {
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
			}

			for (int i = 0; i < npcObject; i++) {
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.paper;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < npcObject; i++) {
				verify(mockNpcPlayers[i], times(1)).getPlayerHand();
			}

			// paper を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < npcObject; i++) {
				if (mockNpcPlayers[i].getPlayerHand() == this.paper) {
					verify(mockNpcPlayers[i], times(1)).getPlayerName();
				}
			}

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.paper == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_21() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 勝利者の手がグー・チョキ・パー、あいこの以外の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * 勝利者の手がグー・チョキ・パー、あいこの以外の場合
	
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_21() {

		final int npcObject = 10;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			}
			when(mockNpcPlayers[0].getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayers[1].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[2].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[3].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[4].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[5].getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayers[6].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[7].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[8].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[9].getPlayerHand()).thenReturn(this.rock);

			for (int i = 0; i < npcObject; i++) {
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
			}

			for (int i = 0; i < npcObject; i++) {
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.errorHand;

			//勝利者の手がグー・チョキ・パーの以外の場合
			jankenCuiGameApplicationImpl.winHand = errorHand;

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}
	}

	/**
	 * testtViewWinner_22() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 勝利者の手があいこの場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * 勝利者の手があいこの場合
	
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_22() {

		final int npcObject = 10;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // セッターを使う

			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			}
			when(mockNpcPlayers[0].getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayers[1].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[2].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[3].getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayers[4].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[5].getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayers[6].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[7].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[8].getPlayerHand()).thenReturn(this.rock);
			when(mockNpcPlayers[9].getPlayerHand()).thenReturn(this.rock);

			for (int i = 0; i < npcObject; i++) {
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
			}

			for (int i = 0; i < npcObject; i++) {
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			jankenCuiGameApplicationImpl.winHand = this.draw;

			//勝利者の手がグー・チョキ・パーの以外の場合
			jankenCuiGameApplicationImpl.winHand = errorHand;

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}
	}

	/**
	 * testViewWinner_23() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが1の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを1人追加
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_23() {

		final int npcObject = 11;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

		//emptyPlayerListにnpcPlayersを1人分追加
		for (int i = 0; i < npcObject; i++) {
			emptyPlayerList.add(npcPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.viewWinner();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testViewWinner_24() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手がグーの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにmockHumanPlayerを1人追加、mockNpcPlayerを1人追加
	 * 勝利者の手がグーの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_24() {

		try {

			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

			// モック化をしてmockHumanPlayerとmockNpcPlayerを作成
			JankenPlayer mockHumanPlayer = mock(HumanJankenPlayerImpl.class);
			JankenPlayer mockNpcPlayer = mock(NpcJankenPlayerImpl.class);

			when(mockHumanPlayer.getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayer.getPlayerName()).thenReturn(this.humanNames[0]);

			emptyPlayerList.add(mockHumanPlayer);

			when(mockNpcPlayer.getPlayerHand()).thenReturn(this.scissors);
			when(mockNpcPlayer.getPlayerName()).thenReturn(this.npcNames[0]);

			emptyPlayerList.add(mockNpcPlayer);

			jankenCuiGameApplicationImpl.winHand = this.rock;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			verify(mockHumanPlayer, times(1)).getPlayerHand();
			verify(mockNpcPlayer, times(1)).getPlayerHand();
			verify(mockHumanPlayer, times(1)).getPlayerName();
			verify(mockNpcPlayer, times(1)).getPlayerName();

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.rock == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testViewWinner_25() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手がチョキの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにmockHumanPlayerを1人追加、mockNpcPlayerを1人追加
	 * 勝利者の手がチョキの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_25() {

		try {

			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

			// モック化をしてmockHumanPlayerとmockNpcPlayerを作成
			JankenPlayer mockHumanPlayer = mock(HumanJankenPlayerImpl.class);
			JankenPlayer mockNpcPlayer = mock(NpcJankenPlayerImpl.class);

			when(mockHumanPlayer.getPlayerHand()).thenReturn(this.scissors);
			when(mockHumanPlayer.getPlayerName()).thenReturn(this.humanNames[0]);

			emptyPlayerList.add(mockHumanPlayer);

			when(mockNpcPlayer.getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayer.getPlayerName()).thenReturn(this.npcNames[0]);

			emptyPlayerList.add(mockNpcPlayer);

			jankenCuiGameApplicationImpl.winHand = this.scissors;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			verify(mockHumanPlayer, times(1)).getPlayerHand();
			verify(mockNpcPlayer, times(1)).getPlayerHand();
			verify(mockHumanPlayer, times(1)).getPlayerName();
			verify(mockNpcPlayer, times(1)).getPlayerName();

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.scissors == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testViewWinner_26() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手がパーの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにmockHumanPlayerを1人追加、mockNpcPlayerを1人追加
	 * 勝利者の手がパーの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_26() {

		try {

			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

			// モック化をしてmockHumanPlayerとmockNpcPlayerを作成
			JankenPlayer mockHumanPlayer = mock(HumanJankenPlayerImpl.class);
			JankenPlayer mockNpcPlayer = mock(NpcJankenPlayerImpl.class);

			when(mockHumanPlayer.getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayer.getPlayerName()).thenReturn(this.humanNames[0]);

			emptyPlayerList.add(mockHumanPlayer);

			when(mockNpcPlayer.getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayer.getPlayerName()).thenReturn(this.npcNames[0]);

			emptyPlayerList.add(mockNpcPlayer);

			jankenCuiGameApplicationImpl.winHand = this.paper;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			verify(mockHumanPlayer, times(1)).getPlayerHand();
			verify(mockNpcPlayer, times(1)).getPlayerHand();
			verify(mockHumanPlayer, times(1)).getPlayerName();
			verify(mockNpcPlayer, times(1)).getPlayerName();

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.paper == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testViewWinner_27() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手がグー・チョキ・パー、あいこの以外の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにmockHumanPlayerを1人追加、mockNpcPlayerを1人追加
	 * 勝利者の手がグー・チョキ・パー、あいこの以外の場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_27() {

		try {

			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

			// モック化をしてmockHumanPlayerとmockNpcPlayerを作成
			JankenPlayer mockHumanPlayer = mock(HumanJankenPlayerImpl.class);
			JankenPlayer mockNpcPlayer = mock(NpcJankenPlayerImpl.class);

			when(mockHumanPlayer.getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayer.getPlayerName()).thenReturn(this.humanNames[0]);

			emptyPlayerList.add(mockHumanPlayer);

			when(mockNpcPlayer.getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayer.getPlayerName()).thenReturn(this.npcNames[0]);

			emptyPlayerList.add(mockNpcPlayer);

			jankenCuiGameApplicationImpl.winHand = this.errorHand;
			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();
			fail();

		} catch (Exception e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", ((SystemException) e).getSysMsg());
		}
	}

	/**
	 * testViewWinner_28() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 勝利者の手があいこの場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにmockHumanPlayerを1人追加、mockNpcPlayerを1人追加
	 * 勝利者の手があいこの場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_28() {

		try {

			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList); // playerListを初期化

			// モック化をしてmockHumanPlayerとmockNpcPlayerを作成
			JankenPlayer mockHumanPlayer = mock(HumanJankenPlayerImpl.class);
			JankenPlayer mockNpcPlayer = mock(NpcJankenPlayerImpl.class);

			when(mockHumanPlayer.getPlayerHand()).thenReturn(this.rock);
			when(mockHumanPlayer.getPlayerName()).thenReturn(this.humanNames[0]);

			emptyPlayerList.add(mockHumanPlayer);

			when(mockNpcPlayer.getPlayerHand()).thenReturn(this.paper);
			when(mockNpcPlayer.getPlayerName()).thenReturn(this.npcNames[0]);

			emptyPlayerList.add(mockNpcPlayer);

			jankenCuiGameApplicationImpl.winHand = this.draw;
			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}
	}

	/**
	 * testtViewWinner_29() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 勝利者の手がグーの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを5人追加、npcPlayersを5人追加
	 * 勝利者の手がグーの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_29() {

		final int humanObject = 5;
		final int npcObject = 5;
		final int jankenPlayerObject = humanObject + npcObject;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			//mockHumanPlayersのモック化
			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]);
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			////mockNpcPlayersのモック化
			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.scissors);
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.scissors);
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);

			jankenCuiGameApplicationImpl.winHand = this.rock;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < jankenPlayerObject; i++) {
				verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
			}

			// Rock を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < jankenPlayerObject; i++) {
				if (emptyPlayerList.get(i).getPlayerHand() == this.rock) {
					verify(emptyPlayerList.get(i), times(1)).getPlayerName();
				}
			}

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.rock == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_30() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 勝利者の手がチョキの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを4人追加、npcPlayersを6人追加
	 * 勝利者の手がチョキの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_30() {

		final int humanObject = 4;
		final int npcObject = 6;
		final int jankenPlayerObject = humanObject + npcObject;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			//mockHumanPlayersのモック化
			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]);
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			////mockNpcPlayersのモック化
			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.paper);

			jankenCuiGameApplicationImpl.winHand = this.scissors;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < jankenPlayerObject; i++) {
				verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
			}

			// Rock を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < jankenPlayerObject; i++) {
				if (emptyPlayerList.get(i).getPlayerHand() == this.scissors) {
					verify(emptyPlayerList.get(i), times(1)).getPlayerName();
				}
			}

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.scissors == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_31() 正常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 勝利者の手がパーの場合
	 * 勝利者名が表示されていること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを6人追加、npcPlayersを4人追加
	 * 勝利者の手がパーの場合
	 * --検証事項--
	 * 1.勝利者名が表示されていること
	 * 2.getPlayerHand()がそれぞれ一度ずつ呼び出されていること
	 * 3.getPlayerName()が勝利者に対して一度呼び出されていること
	 * 
	 */
	@Test
	public void testViewWinner_31() {

		final int humanObject = 6;
		final int npcObject = 4;
		final int jankenPlayerObject = humanObject + npcObject;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			//mockHumanPlayersのモック化
			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]);
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			////mockNpcPlayersのモック化
			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.paper);

			jankenCuiGameApplicationImpl.winHand = this.paper;

			//System.setOutメソッドでByteArrayOutputStreamへリダイレクトさせ、その内容を比較
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();

			//検証
			//プレーヤー数、getPlayerHand()が呼び出せれているか確認
			for (int i = 0; i < jankenPlayerObject; i++) {
				verify(emptyPlayerList.get(i), times(1)).getPlayerHand();
			}

			// paper を持っているプレーヤー数、 getPlayerName() が呼び出されてるか確認
			for (int i = 0; i < jankenPlayerObject; i++) {
				if (emptyPlayerList.get(i).getPlayerHand() == this.paper) {
					verify(emptyPlayerList.get(i), times(1)).getPlayerName();
				}
			}

			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (this.paper == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			//出力の確認(printlnの改行部分はlineSeparatorを付ける)
			assertEquals("勝利者は、" + winners + "です。" + System.lineSeparator(),
					out.toString());

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testtViewWinner_32() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 勝利者の手がグー・チョキ・パー、あいこの以外の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを6人追加、npcPlayersを4人追加
	 * 勝利者の手がグー・チョキ・パー、あいこの以外の場合
	
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_32() {

		final int humanObject = 6;
		final int npcObject = 4;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			//mockHumanPlayersのモック化
			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]);
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			////mockNpcPlayersのモック化
			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.paper);

			jankenCuiGameApplicationImpl.winHand = this.errorHand;

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testtViewWinner_33() 異常系
	 * void viewWinner() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 勝利者の手があいこの場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを6人追加、npcPlayersを4人追加
	 * 勝利者の手があいこの場合
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_33() {

		final int humanObject = 6;
		final int npcObject = 4;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			//mockHumanPlayersのモック化
			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]);
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			////mockNpcPlayersのモック化
			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]);
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.scissors);
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.paper);

			jankenCuiGameApplicationImpl.winHand = this.draw;

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testViewWinner_34() 異常系
	 * void viewWinner() throws SystemException 
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが11の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを5人追加1、npcPlayersを6人追加
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_34() {

		final int humanObject = 5;
		final int npcObject = 6;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

		JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
		JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

		//mockHumanPlayersのモック化
		for (int i = 0; i < humanObject; i++) {
			mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
			emptyPlayerList.add(mockHumanPlayers[i]);
		}

		////mockNpcPlayersのモック化
		for (int i = 0; i < npcObject; i++) {
			mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
			emptyPlayerList.add(mockNpcPlayers[i]);
		}

		try {
			jankenCuiGameApplicationImpl.viewWinner();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testViewWinner_35() 異常系
	 * void viewWinner() throws SystemException 
	 * 
	 * --確認事項--
	 * 勝利者の名前が空の場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * 人数や手の値などの前提条件がクリアしていること
	 * プレーヤーの名前をセットしない
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_35() {
		final int humanObject = 6;
		final int npcObject = 4;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			//mockHumanPlayersのモック化
			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			////mockNpcPlayersのモック化
			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.rock);
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.paper);

			jankenCuiGameApplicationImpl.setWinHand(this.paper);

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testViewWinner_36() 異常系
	 * void viewWinner() throws SystemException 
	 * 
	 * --確認事項--
	 * 名前がNULLの場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * 人数や手の値などの前提条件がクリアしていること
	 * thenThrowでNullPointerExceptionを送る
	 * --検証事項--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testViewWinner_36() {

		final int humanObject = 1;
		final int npcObject = 1;

		try {
			jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			//mockHumanPlayersのモック化
			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]);
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			//mockNpcPlayersのモック化
			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenThrow(new NullPointerException("")); //thenThrowでNullPointerExceptionを送る
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper);
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);

			jankenCuiGameApplicationImpl.winHand = this.paper;

			//テストメソッド
			jankenCuiGameApplicationImpl.viewWinner();
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testIsCheckJankenPlayerCount_01
	 * private void isCheckJankenPlayerCount() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNullの場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * playerListがNullの場合
	 * --検証項目--
	 * 1. SystemException(実行データ不良)が発行されること
	 * 2. メッセージ内容が正しいこと
	 */
	@Test
	public void testIsCheckJankenPlayerCount_01() {
		//playerListがNULLの場合
		jankenCuiGameApplicationImpl.setPlayerList(nullPlayerList);

		try {
			jankenCuiGameApplicationImpl.isCheckJankenPlayerCount();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}

	}

	/**
	 * testIsCheckJankenPlayerCount_02() 異常系
	 * private void isCheckJankenPlayerCount() throws SystemException
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが1の場合
	 * falseを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを1人追加
	 * --検証項目--
	 * falseが返されることを確認
	 */
	@Test
	public void testIsCheckJankenPlayerCount_02() {
		final int playerObject = 1;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

		//humanObject分、emptyPlayerListにhumanPlayersを追加
		for (int i = 0; i < playerObject; i++) {
			emptyPlayerList.add(humanPlayers[i]);
		}

		try {
			boolean result = jankenCuiGameApplicationImpl.isCheckJankenPlayerCount();

			//検証
			assertEquals(false, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testIsCheckJankenPlayerCount_03() 正常系
	 * private void isCheckJankenPlayerCount() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが2の場合
	 * trueを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを2人追加
	 * --検証項目--
	 * trueが返されることを確認
	 */
	@Test
	public void testIsCheckJankenPlayerCount_03() {
		final int playerObject = 2;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

		//humanObject分、emptyPlayerListにhumanPlayersを追加
		for (int i = 0; i < playerObject; i++) {
			emptyPlayerList.add(humanPlayers[i]);
		}

		try {
			boolean result = jankenCuiGameApplicationImpl.isCheckJankenPlayerCount();

			//検証
			assertEquals(true, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testIsCheckJankenPlayerCount_04()　正常系
	 * private void isCheckJankenPlayerCount() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが10の場合
	 * trueを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにnpcPlayersを10人追加
	 * --検証項目--
	 * trueが返されることを確認
	 */
	@Test
	public void testIsCheckJankenPlayerCount_04() {
		final int playerObject = 10;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

		//humanObject分、emptyPlayerListにhumanPlayersを追加
		for (int i = 0; i < playerObject; i++) {
			emptyPlayerList.add(npcPlayers[i]);
		}

		try {
			boolean result = jankenCuiGameApplicationImpl.isCheckJankenPlayerCount();

			//検証
			assertEquals(true, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testIsCheckJankenPlayerCount_05() 異常系
	 * private void isCheckJankenPlayerCount() throws SystemException
	 * 
	 * --確認事項--
	 * playerListがNULL以外の場合で
	 * Jankenプレーヤーオブジェクトが11の場合
	 * falseを返す
	 * --条件--
	 * playerListがNULL以外の場合
	 * PlayerListにhumanPlayersを11人追加
	 * --検証項目--
	 * falseが返されることを確認
	 */
	@Test
	public void testIsCheckJankenPlayerCount_05() {

		final int playerObject = 11;

		jankenCuiGameApplicationImpl.setPlayerList(emptyPlayerList);

		//humanObject分、emptyPlayerListにhumanPlayersを追加
		for (int i = 0; i < playerObject; i++) {
			emptyPlayerList.add(humanPlayers[i]);
		}

		try {
			boolean result = jankenCuiGameApplicationImpl.isCheckJankenPlayerCount();

			//検証
			assertEquals(false, result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * testHasGameContinue01() 正常系
	 * boolean hasGameContinue() throws SystemException
	 * 
	 * --確認事項--
	 * 整数の1を入力した場合
	 * trueを返すこと
	 * --条件--
	 * 整数の1を入力した場合
	 * --検証項目--
	 * trueを返すこと
	 */
	@Test
	public void testHasGameContinue_01() {

		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			// 3を返すように設定
			mockKeyboard.when(() -> Keyboard.getInt(1, 2)).thenReturn(1);

			boolean result = jankenCuiGameApplicationImpl.hasGameContinue();

			//検証
			assertEquals(true, result);

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testHasGameContinue02() 正常系
	 * boolean hasGameContinue() throws SystemException 
	 * 
	 * --確認事項--
	 * 整数の2を入力した場合
	 * falseを返すこと
	 * --条件--
	 * 整数の2を入力した場合
	 * --検証項目--
	 * falseを返すこと
	 */
	@Test
	public void testHasGameContinue_02() {

		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			// 3を返すように設定
			mockKeyboard.when(() -> Keyboard.getInt(1, 2)).thenReturn(2);

			boolean result = jankenCuiGameApplicationImpl.hasGameContinue();

			//検証
			assertEquals(false, result);

		} catch (SystemException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testHasGameContinue03() 異常系
	 * boolean hasGameContinue() throws SystemException 
	 * 
	 * --確認事項--
	 * 整数の1,2以外を入力した場合
	 * 再入力を促すメッセージを出力し、処理を繰り返す。
	 * --条件--
	 * 整数の1,2以外を入力した場合を想定して
	 * ApplicationExceptionを発行させる
	 * 繰り返し処理の後は「正常値2」を引数とし処理を終わらせる
	 * --検証項目--
	 * 1.コンソールに「再入力を促すメッセージを出力し、処理を繰り返す。」と出力されることを確認する
	 * 2.処理が繰り返されていることを確認
	 */
	@Test
	public void testHasGameContinue_03() {

		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			// -1を返すように設定
			mockKeyboard.when(() -> Keyboard.getInt(1, 2)).thenThrow(new ApplicationException("")).thenReturn(2);

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			jankenCuiGameApplicationImpl.hasGameContinue();

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

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testHasGameContinue04() 異常系
	 * boolean hasGameContinue() throws SystemException 
	 * 
	 * --確認事項--
	 * 整数の1,2以外を入力した場合で
	 * 再入力を促すメッセージが出ず、整数の1,2以外の引数が入ってしまった場合
	 * --条件--
	 *整数の1,2以外の引数が入ってしまった場合
	 * --検証項目--
	 * 1.SystemException(実行データ不良)が発行されること
	 * 2.メッセージ内容が正しいこと
	 */
	@Test
	public void testHasGameContinue_04() {

		//モック化
		try (MockedStatic<Keyboard> mockKeyboard = mockStatic(Keyboard.class)) {
			// -1を返すように設定
			mockKeyboard.when(() -> Keyboard.getInt(1, 2)).thenReturn(3);

			jankenCuiGameApplicationImpl.hasGameContinue();
			fail();
		} catch (SystemException e) {
			e.printStackTrace();
			assertEquals("システムエラーが発生しました。終了します。", e.getSysMsg());
		}
	}

	/**
	 * testAction_01() 異常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが1の場合
	 * 人間プレーヤーの数入力で1の数字を入力
	 * Npcプレーヤーの数入力で0の数字を入力
	 * プレーヤ数の合計値エラーにより、「プレーヤー数が2～10名になるように入力してください。」
	 * のメッセージを出力し、処理を繰り返すことを確認
	 * thenReturn(2)で正常値を返し無理やり処理を進めている
	 * 途中で入力処理をやめてるのでcatchされます。
	 * --条件--
	 * 人間プレーヤーの数入力で1の数字を入力
	 * Npcプレーヤーの数入力で0の数字を入力
	 * thenReturn(2)で正常値を返し無理やり処理を進める
	 * --検証項目--
	 * 1.プレーヤ数の合計値エラーにより、「プレーヤー数が2～10名になるように入力してください。」
	 * のメッセージを出力し、処理を繰り返すことをコンソールで確認
	 */
	@Test
	public void testAction_01() {

		try {
			//スパイ化
			//モックで、実際のオブジェクトをラップして、元のメソッドの動作を保持しつつ
			//一部のメソッドの動作をオーバーライドしたり、呼び出しを監視したりする手法
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			// モック人間プレーヤーの設定
			JankenPlayer mockPlayer1 = mock(HumanJankenPlayerImpl.class);
			when(mockPlayer1.getPlayerName()).thenReturn(this.humanNames[0]);
			when(mockPlayer1.getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			emptyPlayerList.add(mockPlayer1);

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); // setPlayerList メソッドを使用してリストを設定する
			// スパイ化
			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doNothing().when(spyGame).selectPlayerHand();
			doNothing().when(spyGame).viewWinner();
			doReturn(false).doReturn(true).when(spyGame).isCheckJankenPlayerCount();

			// 勝者の手をモック
			doReturn(this.rock).when(spyGame).judge(); // 勝者が出す手
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();
			//		assertTrue(output.contains("勝者は")); // 勝者の出力メッセージが含まれているかを確認

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("プレーヤー数が2～10名になるように入力してください。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_02() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(グー)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 人間プレーヤーの1人目をグー、2人目をチョキに設定
	 * 勝利者の手がが1種類の場合(グー)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_02() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 2;
			final int winHands = this.rock;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_03() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(チョキ)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 人間プレーヤーの1人目をパー、2人目をチョキに設定
	 * 勝利者の手がが1種類の場合(チョキ)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_03() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 2;
			final int winHands = this.scissors;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_04() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(パー)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 人間プレーヤーの1人目をパー、2人目をグーに設定
	 * 勝利者の手がが1種類の場合(パー)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_04() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 2;
			final int winHands = this.rock;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_05() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * じゃんけんの判定結果があいこ(勝利者がいない)
	 * あいこにより、「あいこです。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの判定結果があいこ(勝利者がいない)
	 * 2回目にグーを勝ち手として無限ループを終了させている
	 * --検証項目--
	 * 1.あいこにより、「あいこです。」のメッセージが出力されること
	 */
	@Test
	public void testAction_05() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 2;
			final int winHands = this.rock;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(this.draw).doReturn(winHands).when(spyGame).judge(); //1回目はあいこにして2回目に勝利者の手をグーに設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("あいこです。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (

		SystemException e) {
			e.printStackTrace();
		}

	}

	/**
	 * testAction_06() 異常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が異常(1～3以外)な値
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が異常(1～3以外)な値
	 * --検証項目--
	 * 1.SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testAction_06() {
		try {
			// スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			// Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 2;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); // 名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// ここで例外をスローするように設定
			when(emptyPlayerList.get(0).getPlayerHand()).thenThrow(new ApplicationException(""));
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock); // 正常な手を設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList);

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();

			// actionメソッドを呼び出す
			spyGame.action();

			// ここに来てはいけないのでテストを失敗させる
			fail();

		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_07() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(グー)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 人間プレーヤーの1人目をグー、2～10人目をチョキに設定
	 * 勝利者の手がが1種類の場合(グー)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_07() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 10;
			final int winHands = this.rock;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_08() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(チョキ)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 人間プレーヤーの1人目をパー、2～10人目をチョキに設定
	 * 勝利者の手がが1種類の場合(チョキ)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_08() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 10;
			final int winHands = this.scissors;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_09() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(パー)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 人間プレーヤーの1人目をパー、2～10人目をグーに設定
	 * 勝利者の手がが1種類の場合(パー)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_09() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 10;
			final int winHands = this.paper;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_10() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * じゃんけんの判定結果があいこ(勝利者がいない)
	 * あいこにより、「あいこです。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの判定結果があいこ(勝利者がいない)
	 * --検証項目--
	 * 1.あいこにより、「あいこです。」のメッセージが出力されること
	 */
	@Test
	public void testAction_10() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 10;
			final int winHands = this.paper;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(this.draw).doReturn(winHands).when(spyGame).judge(); // あいこ→勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("あいこです。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_06() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が異常(1～3以外)な値
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が異常(1～3以外)な値
	 * --検証項目--
	 * 1.SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testAction_11() {
		try {
			// スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			// Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 10;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); // 名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// ここで例外をスローするように設定
			when(emptyPlayerList.get(0).getPlayerHand()).thenThrow(new ApplicationException(""));
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock); // 正常な手を設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList);

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();

			// actionメソッドを呼び出す
			spyGame.action();

			// ここに来てはいけないのでテストを失敗させる
			fail();

		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_12() 異常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが11の場合
	 * プレーヤ数の合計値エラーにより、「プレーヤー数が2～10名になるように入力してください。」
	 * のメッセージを出力し、処理を繰り返すことを確認
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが11の場合
	 * --検証項目--
	 * 1.プレーヤ数の合計値エラーにより、「プレーヤー数が2～10名になるように入力してください。」
	 * のメッセージを出力し、処理を繰り返すことをコンソールで確認
	 */
	@Test
	public void testAction_12() {

		try {
			//スパイ化
			//モックで、実際のオブジェクトをラップして、元のメソッドの動作を保持しつつ
			//一部のメソッドの動作をオーバーライドしたり、呼び出しを監視したりする手法
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			// モック人間プレーヤーの設定
			// Jankenプレーヤーオブジェクトが2の場合
			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 11;
			final int winHands = this.rock;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(10).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(false).doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doNothing().when(spyGame).selectPlayerHand();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doNothing().when(spyGame).viewWinner();
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("プレーヤー数が2～10名になるように入力してください。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_13() 異常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが1の場合
	 * プレーヤ数の合計値エラーにより、「プレーヤー数が2～10名になるように入力してください。」
	 * のメッセージを出力し、処理を繰り返すことを確認
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが1の場合
	 * --検証項目--
	 * 1.プレーヤ数の合計値エラーにより、「プレーヤー数が2～10名になるように入力してください。」
	 * のメッセージを出力し、処理を繰り返すことをコンソールで確認
	 */
	@Test
	public void testAction_13() {

		try {
			//スパイ化
			//モックで、実際のオブジェクトをラップして、元のメソッドの動作を保持しつつ
			//一部のメソッドの動作をオーバーライドしたり、呼び出しを監視したりする手法
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			// モックNpcプレーヤーの設定
			JankenPlayer mockPlayer1 = mock(NpcJankenPlayerImpl.class);
			when(mockPlayer1.getPlayerName()).thenReturn(this.npcNames[0]);
			when(mockPlayer1.getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			emptyPlayerList.add(mockPlayer1);

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); // setPlayerList メソッドを使用してリストを設定する
			// スパイ化
			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doNothing().when(spyGame).selectPlayerHand();
			doNothing().when(spyGame).viewWinner();
			doReturn(false).doReturn(true).when(spyGame).isCheckJankenPlayerCount();

			// 勝者の手をモック
			doReturn(this.rock).when(spyGame).judge(); // 勝者が出す手
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();
			//		assertTrue(output.contains("勝者は")); // 勝者の出力メッセージが含まれているかを確認

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("プレーヤー数が2～10名になるように入力してください。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_14() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(グー)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(人間プレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * Npcプレーヤーの1人目をグー、2人目をチョキに設定
	 * 勝利者の手がが1種類の場合(グー)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_14() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int npcObject = 2;
			final int winHands = this.rock;

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_15() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(チョキ)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * Npcプレーヤーの1人目をパー、2人目をチョキに設定
	 * 勝利者の手がが1種類の場合(チョキ)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_15() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int npcObject = 2;
			final int winHands = this.scissors;

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_16() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(パー)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * Npcプレーヤーの1人目をパー、2人目をグーに設定
	 * 勝利者の手がが1種類の場合(パー)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_16() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int npcObject = 2;
			final int winHands = this.rock;

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_17() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * じゃんけんの判定結果があいこ(勝利者がいない)
	 * あいこにより、「あいこです。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの判定結果があいこ(勝利者がいない)
	 * 2回目にグーを勝ち手として無限ループを終了させている
	 * --検証項目--
	 * 1.あいこにより、「あいこです。」のメッセージが出力されること
	 */
	@Test
	public void testAction_17() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int npcObject = 2;
			final int winHands = this.rock;

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(this.draw).doReturn(winHands).when(spyGame).judge(); //1回目はあいこにして2回目に勝利者の手をグーに設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("あいこです。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (

		SystemException e) {
			e.printStackTrace();
		}

	}

	/**
	 * testAction_18() 異常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が異常(1～3以外)な値
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が異常(1～3以外)な値
	 * --検証項目--
	 * 1.SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testAction_18() {
		try {
			// スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			// Jankenプレーヤーオブジェクトが2の場合
			final int npcObject = 2;

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); // 名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			// ここで例外をスローするように設定
			when(emptyPlayerList.get(0).getPlayerHand()).thenThrow(new ApplicationException(""));
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock); // 正常な手を設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList);

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();

			// actionメソッドを呼び出す
			spyGame.action();

			// ここに来てはいけないのでテストを失敗させる
			fail();

		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_19() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(グー)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * Npcプレーヤーの1人目をグー、2～10人目をチョキに設定
	 * 勝利者の手がが1種類の場合(グー)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_19() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int npcObject = 10;
			final int winHands = this.rock;

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_20() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(チョキ)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * Npcプレーヤーの1人目をパー、2～10人目をチョキに設定
	 * 勝利者の手がが1種類の場合(チョキ)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_20() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int npcObject = 10;
			final int winHands = this.scissors;

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_21() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(パー)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * Npcプレーヤーの1人目をパー、2～10人目をグーに設定
	 * 勝利者の手がが1種類の場合(パー)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_21() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int npcObject = 10;
			final int winHands = this.paper;

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_22() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * じゃんけんの判定結果があいこ(勝利者がいない)
	 * あいこにより、「あいこです。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの判定結果があいこ(勝利者がいない)
	 * --検証項目--
	 * 1.あいこにより、「あいこです。」のメッセージが出力されること
	 */
	@Test
	public void testAction_22() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int npcObject = 10;
			final int winHands = this.paper;

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(this.draw).doReturn(winHands).when(spyGame).judge(); // あいこ→勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("あいこです。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_23() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が異常(1～3以外)な値
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が異常(1～3以外)な値
	 * --検証項目--
	 * 1.SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testAction_23() {
		try {
			// スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			// Jankenプレーヤーオブジェクトが2の場合
			final int npcObject = 10;

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); // 名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			// ここで例外をスローするように設定
			when(emptyPlayerList.get(0).getPlayerHand()).thenThrow(new ApplicationException(""));
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock); // 正常な手を設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList);

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();

			// actionメソッドを呼び出す
			spyGame.action();

			// ここに来てはいけないのでテストを失敗させる
			fail();

		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_24() 異常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが11の場合
	 * プレーヤ数の合計値エラーにより、「プレーヤー数が2～10名になるように入力してください。」
	 * のメッセージを出力し、処理を繰り返すことを確認
	 * --条件--
	 * Jankenプレーヤーオブジェクトが1種類(Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが11の場合
	 * --検証項目--
	 * 1.プレーヤ数の合計値エラーにより、「プレーヤー数が2～10名になるように入力してください。」
	 * のメッセージを出力し、処理を繰り返すことをコンソールで確認
	 */
	@Test
	public void testAction_24() {

		try {
			//スパイ化
			//モックで、実際のオブジェクトをラップして、元のメソッドの動作を保持しつつ
			//一部のメソッドの動作をオーバーライドしたり、呼び出しを監視したりする手法
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			// モック人間プレーヤーの設定
			// Jankenプレーヤーオブジェクトが2の場合
			//Jankenプレーヤーオブジェクトが2の場合
			final int npcObject = 11;
			final int winHands = this.rock;

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(10).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(false).doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doNothing().when(spyGame).selectPlayerHand();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doNothing().when(spyGame).viewWinner();
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("プレーヤー数が2～10名になるように入力してください。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_25() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(グー)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 人間プレーヤーをグー、Npcプレーヤーをチョキに設定
	 * 勝利者の手がが1種類の場合(グー)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_25() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 1;
			final int npcObject = 1;
			final int winHands = this.rock;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_26() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(チョキ)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 人間プレーヤーをパー、Npcプレーヤーをチョキに設定
	 * 勝利者の手がが1種類の場合(チョキ)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_26() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 1;
			final int npcObject = 1;
			final int winHands = this.scissors;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_27() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(パー)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * 人間プレーヤーをパー、Npcプレーヤーをチョキに設定
	 * 勝利者の手がが1種類の場合(パー)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_27() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 1;
			final int npcObject = 1;
			final int winHands = this.paper;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_28() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * じゃんけんの判定結果があいこ(勝利者がいない)
	 * あいこにより、「あいこです。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの判定結果があいこ(勝利者がいない)
	 * --検証項目--
	 * 1.あいこにより、「あいこです。」のメッセージが出力されること
	 */
	@Test
	public void testAction_28() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 1;
			final int npcObject = 1;
			final int winHands = this.paper;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(draw).doReturn(winHands).when(spyGame).judge(); // あいこ→勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("あいこです。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_29() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が異常(1～3以外)な値
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが2の場合
	 * じゃんけんの手選択で、選択された値が異常(1～3以外)な値 
	 * --検証項目--
	 * 1.SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testAction_29() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 1;
			final int npcObject = 1;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			// ここで例外をスローするように設定
			when(emptyPlayerList.get(0).getPlayerHand()).thenThrow(new ApplicationException(""));
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock); // 正常な手を設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList);

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();

			// actionメソッドを呼び出す
			spyGame.action();

			// ここに来てはいけないのでテストを失敗させる
			fail();

		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_30() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(グー)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 1人目をグー、2人目以降をチョキに設定
	 * 勝利者の手がが1種類の場合(パー)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_30() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 5;
			final int npcObject = 5;
			final int winHands = this.rock;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors); // 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors); // 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors); // 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.scissors); // 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_31() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(チョキ)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 1人目をパー、2人目以降をチョキに設定
	 * 勝利者の手がが1種類の場合(チョキ)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_31() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 5;
			final int npcObject = 5;
			final int winHands = this.scissors;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors); // 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.scissors); // 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.scissors); // 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.scissors); // 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.scissors);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_32() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * 勝利者の手が1種類の場合(パー)
	 * 勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * 1人目をパー、2人目以降をグーに設定
	 * 勝利者の手がが1種類の場合(パー)
	 * --検証項目--
	 * 1.勝利結果にともない、「勝利者は、～です。」のメッセージが出力されること
	 */
	@Test
	public void testAction_32() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが10の場合
			final int humanObject = 5;
			final int npcObject = 5;
			final int winHands = this.paper;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			//勝利者の名前を取得
			StringBuilder sb = new StringBuilder();
			for (JankenPlayer player : emptyPlayerList) {
				if (winHands == player.getPlayerHand()) {
					sb.append(player.getPlayerName() + " ");
				}
			}
			String winners = sb.toString();

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("勝利者は、" + winners + "です。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_33() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が正常(1～3)な値
	 * じゃんけんの判定結果があいこ(勝利者がいない)
	 * あいこにより、「あいこです。」のメッセージが出力されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの判定結果があいこ(勝利者がいない)
	 * --検証項目--
	 * 1.あいこにより、「あいこです。」のメッセージが出力されること
	 */
	@Test
	public void testAction_33() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが10の場合
			final int humanObject = 5;
			final int npcObject = 5;
			final int winHands = this.paper;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.scissors); // 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(draw).doReturn(winHands).when(spyGame).judge(); // あいこ→勝利者の手を設定
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("あいこです。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_34() 異常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が異常(1～3以外)な値
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが10の場合
	 * じゃんけんの手選択で、選択された値が異常(1～3以外)な値
	 * 勝利者の手がが1種類の場合(パー)
	 * --検証項目--
	 * 1.SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testAction_34() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 5;
			final int npcObject = 5;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenThrow(new ApplicationException("")); //異常値を設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList);

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();

			// actionメソッドを呼び出す
			spyGame.action();

			// ここに来てはいけないのでテストを失敗させる
			fail();

		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_35() 異常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * Jankenプレーヤーオブジェクトが2種類(人間プレーヤー、Npcプレーヤー)
	 * Jankenプレーヤーオブジェクトが11の場合
	 * プレーヤ数の合計値エラーにより、「プレーヤー数が2～10名になるように入力してください。」のメッセージを出力し、処理を繰り返す。
	 * --条件--
	 * Jankenプレーヤーオブジェクトが11の場合
	 * --検証項目--
	 * 1.プレーヤ数の合計値エラーにより、「プレーヤー数が2～10名になるように入力してください。」のメッセージを出力し、処理を繰り返す。
	 */
	@Test
	public void testAction_35() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 5;
			final int npcObject = 6;
			final int winHands = this.paper;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(2).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(3).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(4).getPlayerHand()).thenReturn(this.rock); // 手を直接設定
			when(emptyPlayerList.get(5).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(6).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(7).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(8).getPlayerHand()).thenReturn(this.rock);// 手を直接設定
			when(emptyPlayerList.get(9).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(false).doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doNothing().when(spyGame).selectPlayerHand();
			doReturn(winHands).when(spyGame).judge(); // 勝利者の手を設定
			doNothing().when(spyGame).viewWinner();
			doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("プレーヤー数が2～10名になるように入力してください。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_36() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * じゃんけんが終了し「1(続ける)」を入力した場合
	 * 再度じゃんけんゲームを開始し、「じゃんけんゲームを開始します。」のメッセージが出力されること
	 * --条件--
	 * じゃんけんが終了し「1(続ける)」を入力した場合
	 * --検証項目--
	 * 1.再度じゃんけんゲームを開始し、「じゃんけんゲームを開始します。」のメッセージが2回出力されていること
	 */
	@Test
	public void testAction_36() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 1;
			final int npcObject = 1;
			final int winHands = this.paper;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // あいこ→勝利者の手を設定
			doReturn(true).doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続ける→続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// メッセージが2回出力されているかを確認
			int messageCount = 0;
			for (String line : outputLines) {
				System.out.println(line); // デバッグ用
				if (line.equals("じゃんけんゲームを開始します。")) {
					messageCount++;
				}
			}

			// メッセージが2回見つかったことを確認
			assertEquals(2, messageCount);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_37() 正常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * じゃんけんが終了し「2(終了)」を入力した場合
	 * じゃんけんが終了し、「じゃんけんゲームを終了します。」のメッセージが出力されること
	 * --条件--
	 * じゃんけんが終了し「2(終了)」を入力した場合
	 * --検証項目--
	 * 1.じゃんけんが終了し、「じゃんけんゲームを終了します。」のメッセージが出力されること
	 */
	@Test
	public void testAction_37() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 1;
			final int npcObject = 1;
			final int winHands = this.paper;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // あいこ→勝利者の手を設定
			doReturn(true).doReturn(false).when(spyGame).hasGameContinue(); // ゲームを続ける→続けない設定

			// System.outのキャプチャ
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			// actionメソッドを呼び出す
			spyGame.action();

			// 出力の確認
			String output = out.toString();

			// 出力の確認(split()メソッドで出力された文字列を行ごとに分割)
			String[] outputLines = output.split(System.lineSeparator());

			// for分で該当するメッセージを探す
			boolean findMessage = false;
			for (String line : outputLines) {
				System.out.println(line); //デバック用
				if (line.equals("じゃんけんゲームを終了します。")) {
					findMessage = true;
					break; // メッセージが見つかったらループを抜ける
				}
			}

			// メッセージが見つかったことを確認
			assertTrue(findMessage);

			// System.outを元に戻す
			System.setOut(System.out);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * testAction_38() 異常系
	 * public void action() throws SystemException
	 * 
	 * --確認事項--
	 * じゃんけんが終了し1と2以外を入力した場合で
	 * 再入力を促す「入力した値が不正です。再入力をお願いします。」のメッセージが出力されず
	 * 整数の1,2以外の引数を受け取ってしまった場合
	 * SystemException(実行データ不良)が発行されること
	 * --条件--
	 * じゃんけんが終了し1と2以外を入力した場合
	 * 再入力を促す「入力した値が不正です。再入力をお願いします。」のメッセージが出力されず
	 * hasGameContinueがSystemExceptionをthrowしたとき
	 * --検証項目--
	 * 1.SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void testAction_38() {

		try {
			//スパイ化
			JankenCuiGameApplicationImpl spyGame = spy(JankenCuiGameApplicationImpl.class);

			//Jankenプレーヤーオブジェクトが2の場合
			final int humanObject = 1;
			final int npcObject = 1;
			final int winHands = this.paper;

			// モック人間プレーヤーの設定
			JankenPlayer[] mockHumanPlayers = new JankenPlayer[humanObject];

			for (int i = 0; i < humanObject; i++) {
				mockHumanPlayers[i] = mock(HumanJankenPlayerImpl.class);
				when(mockHumanPlayers[i].getPlayerName()).thenReturn(this.humanNames[i]); //名前を設定
				emptyPlayerList.add(mockHumanPlayers[i]);
			}

			// モックNpcプレーヤーの設定
			JankenPlayer[] mockNpcPlayers = new JankenPlayer[npcObject];

			for (int i = 0; i < npcObject; i++) {
				mockNpcPlayers[i] = mock(NpcJankenPlayerImpl.class);
				when(mockNpcPlayers[i].getPlayerName()).thenReturn(this.npcNames[i]); //名前を設定
				emptyPlayerList.add(mockNpcPlayers[i]);
			}

			when(emptyPlayerList.get(0).getPlayerHand()).thenReturn(this.paper); // 手を直接設定
			when(emptyPlayerList.get(1).getPlayerHand()).thenReturn(this.rock);// 手を直接設定

			// プレーヤーリストを設定
			spyGame.setPlayerList(emptyPlayerList); //リストを設定する

			// モックの設定
			doNothing().when(spyGame).init();
			doNothing().when(spyGame).createHumanOfJankenPlayer();
			doNothing().when(spyGame).createNpcOfJankenPlayer();
			doReturn(true).when(spyGame).isCheckJankenPlayerCount();
			doReturn(winHands).when(spyGame).judge(); // あいこ→勝利者の手を設定
			doThrow(new SystemException("")).when(spyGame).hasGameContinue(); // ゲームを続ける→続けない設定

			// actionメソッドを呼び出す
			spyGame.action();
			fail();

		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

}
