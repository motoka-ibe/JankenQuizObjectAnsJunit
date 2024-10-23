package jp.co.ginga.cui.impl.janken;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import jp.co.ginga.application.janken.JankenCuiGameApplicationImpl;
import jp.co.ginga.application.janken.JankenParam;
import jp.co.ginga.application.janken.jankenplayer.HumanJankenPlayerImpl;
import jp.co.ginga.application.janken.jankenplayer.JankenPlayer;
import jp.co.ginga.application.janken.jankenplayer.NpcJankenPlayerImpl;
import jp.co.ginga.util.exception.ApplicationException;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.keybord.Keybord;

/**
 * CUIじゃんけん実装クラス
 * @author yoshi
 *
 */
public class TestJankenCuiGameApplicationImpl{

	//テストデータ
	private int none = 0;
	private int two = 2;
	private int one = 1;
	private int zero = 0;
	private int illegalValue = -1;
	private int rock = JankenParam.ROCK.getInt();
	private int scissors = JankenParam.SCISSORS.getInt();
	private int paper = JankenParam.PAPER.getInt();
	private int draw = JankenParam.DRAW.getInt();
	private String playerName1 = "プレーヤー1";
	private String playerName2 = "プレーヤー2";
	private String cpuName1 = "CPU1";
	private String cpuName2 = "CPU2";
	private JankenPlayer humanPlayer1 = new HumanJankenPlayerImpl(playerName1);
	private JankenPlayer humanPlayer2 = new HumanJankenPlayerImpl(playerName2);
	private JankenPlayer cpuPlayer1 = new NpcJankenPlayerImpl(cpuName1);
	private JankenPlayer cpuPlayer2 = new NpcJankenPlayerImpl(cpuName2);
	private List<JankenPlayer> emptyPlayerList = new ArrayList<JankenPlayer>();
	private List<JankenPlayer> nullPlayerList = null;
	private List<JankenPlayer> playerList = new ArrayList<JankenPlayer>(Arrays.asList(humanPlayer1, cpuPlayer1));
	private List<JankenPlayer> humanPlayerList = new ArrayList<JankenPlayer>(Arrays.asList(humanPlayer1, humanPlayer2));
	private List<JankenPlayer> cpuPlayerList = new ArrayList<JankenPlayer>(Arrays.asList(cpuPlayer1, cpuPlayer2));

	@InjectMocks
	JankenCuiGameApplicationImpl jankenCuiGameApplicationImpl = new JankenCuiGameApplicationImpl();


	/**
	 * testInit001 正常系
	 * private void init()
	 * --確認事項--
	 * playerListがnullの場合、ArrayList<JankenPlayer>のインスタンスを代入する
	 * --条件--
	 *	playerListフィールドの値がnull
	 * --検証項目--
	 * 1. playerListフィールドの値がArrayList<JankenPlayer>のインスタンスであるか
	 * 2. playerListフィールドのサイズが0であるか
	 */
	@Test
	public void testInit001() {
		try {
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("init");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);
			
			//this.playerListをnullでセットする

			//テストメソッド
			method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			@SuppressWarnings("unchecked")
			List<JankenPlayer> playerList =(List<JankenPlayer>) playerListField.get(this.jankenCuiGameApplicationImpl);
			
			//nullじゃないこと
			assertNotNull(playerList);
			//参照先の型が List<JankenPlayer> instanceof true
			//assertEquals(playerList.size(), 0);

		}catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * testInit002 正常系
	 * private void init()
	 * --確認事項--
	 * playerListがnullでない場合、リストの要素を空にする
	 * --条件--
	 *	playerListフィールドの値がJankenPlayerインスタンスが追加されたリスト
	 * --検証項目--
	 * 1. playerListフィールドの値がnullでない
	 * 2. playerListフィールドのサイズが0であるか
	 */
	@Test
	public void testInit002() {
		try {
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("init");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.playerList);

			//テストメソッド
			method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			@SuppressWarnings("unchecked")
			List<JankenPlayer> playerList =(List<JankenPlayer>) playerListField.get(this.jankenCuiGameApplicationImpl);
			assertEquals(playerList, this.emptyPlayerList);
			assertEquals(playerList.size(), 0);

		}catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	
	
	/**
	 * CreateHumanOfJankenPlayer()のテスト 異常系
	 * playerListがNULLの場合
	 * SystemException(実行データ不良)が発行されること
	 */
	@Test
	public void TestCreateHumanOfJankenPlayer_Null() {
		jankenCuiGameApplicationImpl.createNpcOfJankenPlayer();
	}
	
	
	
	/**
	 * CreateHumanOfJankenPlayer()のテスト 異常系
	 * playerListがNULL以外の場合
	 * 負の整数を入力した場合
	 * 再入力を促すメッセージを出力し、処理を繰り返す。
	 */
	@Test
	public void TestCreateHumanOfJankenPlayer001() {
		
	}
	
	
	

	/**
	 * CreateHumanOfJankenPlayer()のテスト 正常系
	 * playerListがNULL以外の場合
	 * 0～10の整数を入力した場合
	 * 入力した整数分のオブジェクト生成されること
	 */
	@Test
	public void TestCreateHumanOfJankenPlayer002() {
		
	}
	
	
	/**
	 * CreateHumanOfJankenPlayer()のテスト 異常系
	 * playerListがNULL以外の場合
	 * 整数11以上の値を入力した場合
	 * 再入力を促すメッセージを出力し、処理を繰り返す。
	 */
	@Test
	public void TestCreateHumanOfJankenPlayer003() {
		
	}
	
	
	/**
	 * testCreateHumanOfJankenPlayer001 正常系
	 * private void createHumanOfJankenPlayer() throws SystemException
	 * --確認事項--
	 * 入力した数分のHumanPlayerインスタンスを生成してplayerListフィールドに追加する
	 * --条件--
	 * KeybordクラスのgetIntでは2を返すmock
	 *	playerListフィールドの値は空のリスト
	 * --検証項目--
	 * 1. playerListフィールドに2個のHumanPlayerインスタンスが追加されているか
	 * 2. それぞれ追加されたインスタンスが持つデータが指定した値であるか
	 * 3. getIntメソッドは1回だけ呼び出されているか
	 */
	@Test
	public void testCreateHumanOfJankenPlayer001() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt()).thenReturn(this.two);

			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("createHumanOfJankenPlayer");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.emptyPlayerList);

			//テストメソッド
			method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			@SuppressWarnings("unchecked")
			List<JankenPlayer> playerList =(List<JankenPlayer>) playerListField.get(this.jankenCuiGameApplicationImpl);
			assertEquals(playerList.size(), this.two);
			assertEquals(playerList.get(0).getPlayerName(), this.humanPlayer1.getPlayerName());
			assertEquals(playerList.get(1).getPlayerName(), this.humanPlayer2.getPlayerName());
			mockKeybord.verify(() -> Keybord.getInt(), times(1));

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateHumanOfJankenPlayer002 正常系
	 * private void createHumanOfJankenPlayer() throws SystemException
	 * --確認事項--
	 * 入力した数分のHumanPlayerインスタンスを生成してplayerListフィールドに追加する
	 * --条件--
	 * KeybordクラスのgetIntでは0を返すmock
	 *	playerListフィールドの値は空のリスト
	 * --検証項目--
	 * 1. playerListフィールドに0個のHumanPlayerインスタンスが追加されているか
	 * 2. getIntメソッドは1回だけ呼び出されているか
	 */
	@Test
	public void testCreateHumanOfJankenPlayer002() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt()).thenReturn(this.none);

			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("createHumanOfJankenPlayer");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.emptyPlayerList);

			//テストメソッド
			method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			@SuppressWarnings("unchecked")
			List<JankenPlayer> playerList =(List<JankenPlayer>) playerListField.get(this.jankenCuiGameApplicationImpl);
			assertEquals(playerList.size(), this.none);
			mockKeybord.verify(() -> Keybord.getInt(), times(1));

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateHumanOfJankenPlayer003 正常系
	 * private void createHumanOfJankenPlayer() throws SystemException
	 * --確認事項--
	 * 不正な値を入力したら再度入力が求められ入力した数分のHumanPlayerインスタンスを生成してplayerListフィールドに追加する
	 * --条件--
	 * KeybordクラスのgetIntでは一回目はApplicationExceptionが発生し、二回目に2を返すmock
	 *	playerListフィールドの値は空のリスト
	 * --検証項目--
	 * 1. playerListフィールドに2個のHumanPlayerインスタンスが追加されているか
	 * 2. それぞれ追加されたインスタンスが持つデータが指定した値であるか
	 * 3. getIntメソッドは2回だけ呼び出されているか
	 */
	@Test
	public void testCreateHumanOfJankenPlayer003() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt()).thenThrow(new ApplicationException(null)).thenReturn(this.two);

			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("createHumanOfJankenPlayer");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.emptyPlayerList);

			//テストメソッド
			method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			@SuppressWarnings("unchecked")
			List<JankenPlayer> playerList =(List<JankenPlayer>) playerListField.get(this.jankenCuiGameApplicationImpl);
			assertEquals(playerList.size(), this.two);
			assertEquals(playerList.get(0).getPlayerName(), this.humanPlayer1.getPlayerName());
			assertEquals(playerList.get(1).getPlayerName(), this.humanPlayer2.getPlayerName());
			mockKeybord.verify(() -> Keybord.getInt(), times(2));

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateHumanOfJankenPlayer004 異常系
	 * private void createHumanOfJankenPlayer() throws SystemException
	 * --確認事項--
	 * playerListフィールドにnullが代入されている場合、NullPointerExceptionが発生する
	 * --条件--
	 * KeybordクラスのgetIntでは2を返すmock
	 *	playerListフィールドの値はnull
	 * --検証項目--
	 * 1. NullPointerExceptionが発生するか
	 * 2. getIntメソッドは1回だけ呼び出されているか
	 */
	@Test
	public void testCreateHumanOfJankenPlayer004() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt()).thenReturn(this.two);

			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("createHumanOfJankenPlayer");
			method.setAccessible(true);

			//テストメソッド
			InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> method.invoke(this.jankenCuiGameApplicationImpl));

			//検証
			assertEquals(NullPointerException.class , e.getTargetException().getClass());
			mockKeybord.verify(() -> Keybord.getInt(), times(1));
		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateCpuOfJankenPlayer001 正常系
	 * private void createCpuOfJankenPlayer() throws SystemException
	 * --確認事項--
	 * 入力した数分のCpuPlayerインスタンスを生成してplayerListフィールドに追加する
	 * --条件--
	 * KeybordクラスのgetIntでは2を返すmock
	 *	playerListフィールドの値は空のリスト
	 * --検証項目--
	 * 1. playerListフィールドに2個のCpuPlayerインスタンスが追加されているか
	 * 2. それぞれ追加されたインスタンスが持つデータが指定した値であるか
	 * 3. getIntメソッドは1回だけ呼び出されているか
	 */
	@Test
	public void testCreateCpuOfJankenPlayer001() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt()).thenReturn(this.two);

			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("createCpuOfJankenPlayer");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.emptyPlayerList);

			//テストメソッド
			method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			@SuppressWarnings("unchecked")
			List<JankenPlayer> playerList =(List<JankenPlayer>) playerListField.get(this.jankenCuiGameApplicationImpl);
			assertEquals(playerList.size(), this.two);
			assertEquals(playerList.get(0).getPlayerName(), this.cpuPlayer1.getPlayerName());
			assertEquals(playerList.get(1).getPlayerName(), this.cpuPlayer2.getPlayerName());
			mockKeybord.verify(() -> Keybord.getInt(), times(1));

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateCpuOfJankenPlayer002 正常系
	 * private void createCpuOfJankenPlayer() throws SystemException
	 * --確認事項--
	 * 入力した数分のHumanPlayerインスタンスを生成してplayerListフィールドに追加する
	 * --条件--
	 * KeybordクラスのgetIntでは0を返すmock
	 *	playerListフィールドの値は空のリスト
	 * --検証項目--
	 * 1. playerListフィールドに0個のHumanPlayerインスタンスが追加されているか
	 * 2. getIntメソッドは1回だけ呼び出されているか
	 */
	@Test
	public void testCreateCpuOfJankenPlayer002() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt()).thenReturn(this.none);

			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("createCpuOfJankenPlayer");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.emptyPlayerList);

			//テストメソッド
			method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			@SuppressWarnings("unchecked")
			List<JankenPlayer> playerList =(List<JankenPlayer>) playerListField.get(this.jankenCuiGameApplicationImpl);
			assertEquals(playerList.size(), this.none);
			mockKeybord.verify(() -> Keybord.getInt(), times(1));

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateCpuOfJankenPlayer003 正常系
	 * private void createCpuOfJankenPlayer() throws SystemException
	 * --確認事項--
	 * 不正な値を入力したら再度入力が求められ入力した数分のCpuPlayerインスタンスを生成してplayerListフィールドに追加する
	 * --条件--
	 * KeybordクラスのgetIntでは一回目はApplicationExceptionが発生し、二回目に2を返すmock
	 *	playerListフィールドの値は空のリスト
	 * --検証項目--
	 * 1. playerListフィールドに2個のCpuPlayerインスタンスが追加されているか
	 * 2. それぞれ追加されたインスタンスが持つデータが指定した値であるか
	 * 3. getIntメソッドは2回だけ呼び出されているか
	 */
	@Test
	public void testCreateCpuOfJankenPlayer003() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt()).thenThrow(new ApplicationException(null)).thenReturn(this.two);

			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("createCpuOfJankenPlayer");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.emptyPlayerList);

			//テストメソッド
			method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			@SuppressWarnings("unchecked")
			List<JankenPlayer> playerList =(List<JankenPlayer>) playerListField.get(this.jankenCuiGameApplicationImpl);
			assertEquals(playerList.size(), this.two);
			assertEquals(playerList.get(0).getPlayerName(), this.cpuPlayer1.getPlayerName());
			assertEquals(playerList.get(1).getPlayerName(), this.cpuPlayer2.getPlayerName());
			mockKeybord.verify(() -> Keybord.getInt(), times(2));

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testCreateCpuOfJankenPlayer004 異常系
	 * private void createCpuOfJankenPlayer() throws SystemException
	 * --確認事項--
	 * playerListフィールドにnullが代入されている場合、NullPointerExceptionが発生する
	 * --条件--
	 * KeybordクラスのgetIntでは2を返すmock
	 *	playerListフィールドの値はnull
	 * --検証項目--
	 * 1. NullPointerExceptionが発生するか
	 * 2. getIntメソッドは1回だけ呼び出されているか
	 */
	@Test
	public void testCreateCpuOfJankenPlayer004() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt()).thenReturn(this.two);

			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("createCpuOfJankenPlayer");
			method.setAccessible(true);

			//テストメソッド
			InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> method.invoke(this.jankenCuiGameApplicationImpl));
			//検証
			assertEquals(NullPointerException.class , e.getTargetException().getClass());
			mockKeybord.verify(() -> Keybord.getInt(), times(1));

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectJankenHand001 正常系
	 * private void selectJankenHand() throws SystemException
	 * --確認事項--
	 * playerListフィールドに追加されている各々のプレイヤーインスタンスからselectJankenHandメソッドを呼び出す
	 * --条件--
	 * playerListフィールドにはmock化されたhumanPlayerとcpuPlayerの2個が追加されている
	 * --検証項目--
	 * 1. それぞれのプレイヤーインスタンスからselectJankenHandメソッドを1回ずつ呼び出されているか
	 */
	@Test
	public void testSelectJankenHand001() {
		try{
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("selectJankenHand");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);

			//モック化
			JankenPlayer mockHumanPlayer = mock(HumanJankenPlayerImpl.class);
			JankenPlayer mockCpuPlayer = mock(NpcJankenPlayerImpl.class);
			doNothing().when(mockHumanPlayer).selectJankenHand();
			doNothing().when(mockCpuPlayer).selectJankenHand();
			List<JankenPlayer> playerList = new ArrayList<JankenPlayer>(Arrays.asList(mockHumanPlayer, mockCpuPlayer));
			playerListField.set(this.jankenCuiGameApplicationImpl, playerList);

			//テストメソッド
			method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			verify(mockHumanPlayer, times(1)).selectJankenHand();
			verify(mockCpuPlayer, times(1)).selectJankenHand();

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectJankenHand002 異常系
	 * private void selectJankenHand() throws SystemException
	 * --確認事項--
	 * playerListフィールドに追加されている各々のプレイヤーインスタンスからselectJankenHandメソッドを呼び出す
	 * --条件--
	 * playerListフィールドには何も追加されていない
	 * --検証項目--
	 * 1. playerListフィールドから一度も要素が取り出されていないか
	 */
	@Test
	public void testSelectJankenHand002() {
		try{
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("selectJankenHand");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);

			//モック化
			@SuppressWarnings("unchecked")
			List<JankenPlayer> playerList = mock(new ArrayList<JankenPlayer>().getClass());
			playerListField.set(this.jankenCuiGameApplicationImpl, playerList);

			//テストメソッド
			method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			verify(playerList, times(0)).get(0);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSelectJankenHand003 異常系
	 * private void selectJankenHand() throws SystemException
	 * --確認事項--
	 * playerListフィールドにnullが代入されている場合、NullPointerExceptionが発生する
	 * --条件--
	 * playerListフィールドはnull
	 * --検証項目--
	 * 1. NullPointerExceptionが発生すること
	 */
	@Test
	public void testSelectJankenHand003() {
		try{
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("selectJankenHand");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.nullPlayerList);

			//テストメソッド
			InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> method.invoke(this.jankenCuiGameApplicationImpl));

			//検証
			assertEquals(NullPointerException.class , e.getTargetException().getClass());

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testJudge001 正常系
	 * private int judge() throws SystemException
	 * --確認事項--
	 * プレイヤーから手を取り出し、フラグを切り替えて勝ちの手を返す
	 * --条件--
	 * playerListフィールドにグーを出したCpuプレイヤー、チョキを出したhumanプレイヤーが追加されている
	 * --検証項目--
	 * 1. 戻り値がROCKに対応する定数と等しいか
	 * 2. プレイヤーインスタンスからgetJankenHandメソッドがそれぞれ1回呼び出されているか
	 */
	@Test
	public void testJudge001() {
		try{
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("judge");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);

			//モック化
			JankenPlayer humanPlayer = mock(HumanJankenPlayerImpl.class);
			JankenPlayer cpuPlayer = mock(NpcJankenPlayerImpl.class);
			when(humanPlayer.getJankenHand()).thenReturn(this.scissors);
			when(cpuPlayer.getJankenHand()).thenReturn(this.rock);
			List<JankenPlayer> playerList = new ArrayList<JankenPlayer>(Arrays.asList(humanPlayer, cpuPlayer));
			playerListField.set(this.jankenCuiGameApplicationImpl, playerList);

			//テストメソッド
			int result = (int) method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			assertEquals(this.rock, result);
			verify(humanPlayer, times(1)).getJankenHand();
			verify(cpuPlayer, times(1)).getJankenHand();

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testJudge002 正常系
	 * private int judge() throws SystemException
	 * --確認事項--
	 * プレイヤーから手を取り出し、フラグを切り替えて勝ちの手を返す
	 * --条件--
	 * playerListフィールドにグーを出したCpuプレイヤー、パーを出したhumanプレイヤーが追加されている
	 * --検証項目--
	 * 1. 戻り値がPAPERに対応する定数と等しいか
	 * 2. プレイヤーインスタンスからgetJankenHandメソッドがそれぞれ1回呼び出されているか
	 */
	@Test
	public void testJudge002() {
		try{
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("judge");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);

			//モック化
			JankenPlayer humanPlayer = mock(HumanJankenPlayerImpl.class);
			JankenPlayer cpuPlayer = mock(NpcJankenPlayerImpl.class);
			when(humanPlayer.getJankenHand()).thenReturn(this.paper);
			when(cpuPlayer.getJankenHand()).thenReturn(this.rock);
			List<JankenPlayer> playerList = new ArrayList<JankenPlayer>(Arrays.asList(humanPlayer, cpuPlayer));
			playerListField.set(this.jankenCuiGameApplicationImpl, playerList);

			//テストメソッド
			int result = (int) method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			assertEquals(this.paper, result);
			verify(humanPlayer, times(1)).getJankenHand();
			verify(cpuPlayer, times(1)).getJankenHand();

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * testJudge003 正常系
	 * private int judge() throws SystemException
	 * --確認事項--
	 * プレイヤーから手を取り出し、フラグを切り替えて勝ちの手を返す
	 * --条件--
	 * playerListフィールドにチョキを出したCpuプレイヤー、パーを出したhumanプレイヤーが追加されている
	 * --検証項目--
	 * 1. 戻り値がSCISSORSに対応する定数と等しいか
	 * 2. プレイヤーインスタンスからgetJankenHandメソッドがそれぞれ1回呼び出されているか
	 */
	@Test
	public void testJudge003() {
		try{
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("judge");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);

			//モック化
			JankenPlayer humanPlayer = mock(HumanJankenPlayerImpl.class);
			JankenPlayer cpuPlayer = mock(NpcJankenPlayerImpl.class);
			when(humanPlayer.getJankenHand()).thenReturn(this.paper);
			when(cpuPlayer.getJankenHand()).thenReturn(this.scissors);
			List<JankenPlayer> playerList = new ArrayList<JankenPlayer>(Arrays.asList(humanPlayer, cpuPlayer));
			playerListField.set(this.jankenCuiGameApplicationImpl, playerList);

			//テストメソッド
			int result = (int) method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			assertEquals(this.scissors, result);
			verify(humanPlayer, times(1)).getJankenHand();
			verify(cpuPlayer, times(1)).getJankenHand();

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testJudge002 正常系
	 * private int judge() throws SystemException
	 * --確認事項--
	 * プレイヤーから手を取り出し、フラグを切り替えて勝ちの手を返す
	 * --条件--
	 * playerListフィールドにグーを出したCpuプレイヤー、グーを出したhumanプレイヤーが追加されている
	 * --検証項目--
	 * 1. 戻り値がDRAWに対応する定数と等しいか
	 * 2. プレイヤーインスタンスからgetJankenHandメソッドがそれぞれ1回呼び出されているか
	 */
	@Test
	public void testJudge004() {
		try{
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("judge");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);

			//モック化
			JankenPlayer humanPlayer = mock(HumanJankenPlayerImpl.class);
			JankenPlayer cpuPlayer = mock(NpcJankenPlayerImpl.class);
			when(humanPlayer.getJankenHand()).thenReturn(this.rock);
			when(cpuPlayer.getJankenHand()).thenReturn(this.rock);
			List<JankenPlayer> playerList = new ArrayList<JankenPlayer>(Arrays.asList(humanPlayer, cpuPlayer));
			playerListField.set(this.jankenCuiGameApplicationImpl, playerList);

			//テストメソッド
			int result = (int) method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			assertEquals(this.draw, result);
			verify(humanPlayer, times(1)).getJankenHand();
			verify(cpuPlayer, times(1)).getJankenHand();

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testJudge005 異常系
	 * private int judge() throws SystemException
	 * --確認事項--
	 * プレイヤーから手を取り出し、異常値である場合はNullPointerExceptionが発生する
	 * --条件--
	 * playerListフィールドにグーを出したCpuプレイヤー、異常値を出したhumanプレイヤーが追加されている
	 * --検証項目--
	 * 1. NullPointerExceptionがスローされるか
	 * 2. humanプレイヤー、Cpuプレイヤーの順番であるため、getJankenHandが１回と０回呼び出されているか
	 */
	@Test
	public void testJudge005() {
		try{
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("judge");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);

			//モック化
			JankenPlayer humanPlayer = mock(HumanJankenPlayerImpl.class);
			JankenPlayer cpuPlayer = mock(NpcJankenPlayerImpl.class);
			when(humanPlayer.getJankenHand()).thenReturn(this.illegalValue);
			when(cpuPlayer.getJankenHand()).thenReturn(this.rock);
			List<JankenPlayer> playerList = new ArrayList<JankenPlayer>(Arrays.asList(humanPlayer, cpuPlayer));
			playerListField.set(this.jankenCuiGameApplicationImpl, playerList);

			//テストメソッド
			InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> method.invoke(this.jankenCuiGameApplicationImpl));

			//検証
			assertEquals(NullPointerException.class , e.getTargetException().getClass());
			verify(humanPlayer, times(1)).getJankenHand();
			verify(cpuPlayer, times(0)).getJankenHand();

		}catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testJudge006 異常系
	 * private int judge() throws SystemException
	 * --確認事項--
	 * プレイヤーから手を取り出し、異常値であるDRAWの定数の場合はSystemExceptionが発生する
	 * --条件--
	 * playerListフィールドにグーを出したCpuプレイヤー、異常値を出したhumanプレイヤーが追加されている
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. メッセージ「システムエラーが発生しました。終了します。」が含まれるか (取り出せないため未検証)
	 * 3. humanプレイヤー、Cpuプレイヤーの順番であるため、getJankenHandが１回と０回呼び出されているか
	 */
	@Test
	public void testJudge006() {
		try{
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("judge");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);

			//モック化
			JankenPlayer humanPlayer = mock(HumanJankenPlayerImpl.class);
			JankenPlayer cpuPlayer = mock(NpcJankenPlayerImpl.class);
			when(humanPlayer.getJankenHand()).thenReturn(this.draw);
			when(cpuPlayer.getJankenHand()).thenReturn(this.rock);
			List<JankenPlayer> playerList = new ArrayList<JankenPlayer>(Arrays.asList(humanPlayer, cpuPlayer));
			playerListField.set(this.jankenCuiGameApplicationImpl, playerList);

			//テストメソッド
			InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> method.invoke(this.jankenCuiGameApplicationImpl));

			//検証
			assertEquals(SystemException.class , e.getTargetException().getClass());
			verify(humanPlayer, times(1)).getJankenHand();
			verify(cpuPlayer, times(0)).getJankenHand();

		}catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testViewWinner001 正常系
	 * private void viewWinner() throws SystemException
	 * --確認事項--
	 * winnHandフィールドと同じ値をもつプレイヤーのplayerNameフィールドの値をを出力する
	 * --条件--
	 * playerListフィールドにグーを出したhumanプレイヤーのモック、チョキを出したcpuプレイヤーのモックが追加されている
	 * winHandフィールドの値をROCKに対応する定数
	 * --検証項目--
	 * 1. getJankenHandメソッドが各々1回呼び出されているか
	 * 2. humanプレイヤーのgetPlayerNameメソッドが1回、cpuプレイヤーのgetPlayerNameメソッドは0回呼び出されているか
	 */
	@Test
	public void testViewWinner001() {
		try{
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("viewWinner");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			Field winHandField = JankenCuiGameApplicationImpl.class.getDeclaredField("winHand");
			method.setAccessible(true);
			playerListField.setAccessible(true);
			winHandField.setAccessible(true);

			//モック化
			JankenPlayer humanPlayer = mock(HumanJankenPlayerImpl.class);
			JankenPlayer cpuPlayer = mock(NpcJankenPlayerImpl.class);
			when(humanPlayer.getJankenHand()).thenReturn(this.rock);
			when(cpuPlayer.getJankenHand()).thenReturn(this.scissors);
			when(humanPlayer.getPlayerName()).thenReturn(this.playerName1);
			when(cpuPlayer.getPlayerName()).thenReturn(this.cpuName1);
			List<JankenPlayer> playerList = new ArrayList<JankenPlayer>(Arrays.asList(humanPlayer, cpuPlayer));
			playerListField.set(this.jankenCuiGameApplicationImpl, playerList);
			winHandField.set(this.jankenCuiGameApplicationImpl, this.rock);

			//テストメソッド
			method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			verify(humanPlayer, times(1)).getJankenHand();
			verify(cpuPlayer, times(1)).getJankenHand();
			verify(humanPlayer, times(1)).getPlayerName();
			verify(cpuPlayer, times(0)).getPlayerName();

		}catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testIsCheckJankenPlayerCount001 正常系
	 * private void isCheckJankenPlayerCount() throws SystemException
	 * --確認事項--
	 * playerListフィールドの長さが２以上の場合はtrueを返すか
	 * --条件--
	 * playerListフィールドは要素が2個追加されているリスト
	 * --検証項目--
	 * 1. 戻り値がtrueであるか
	 */
	@Test
	public void testIsCheckJankenPlayerCount001() {
		try{
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("isCheckJankenPlayerCount");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.playerList);

			//テストメソッド
			boolean result = (boolean)method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			assertEquals(true, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testIsCheckJankenPlayerCount002 正常系
	 * private void isCheckJankenPlayerCount() throws SystemException
	 * --確認事項--
	 * playerListフィールドの長さが２未満の場合はfalseを返すか
	 * --条件--
	 * playerListフィールドは空のリスト
	 * --検証項目--
	 * 1. 戻り値がfalseであるか
	 */
	@Test
	public void testIsCheckJankenPlayerCount002() {
		try{
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("isCheckJankenPlayerCount");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.emptyPlayerList);

			//テストメソッド
			boolean result = (boolean)method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			assertEquals(false, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testIsCheckJankenPlayerCount003 異常系
	 * private void isCheckJankenPlayerCount() throws SystemException
	 * --確認事項--
	 * playerListフィールドがnullの場合NullPointerExceptionが発生する
	 * --条件--
	 * playerListフィールドはnull
	 * --検証項目--
	 * 1. NullPointerExceptionが発生するか
	 */
	@Test
	public void testIsCheckJankenPlayerCount003() {
		try{
			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("isCheckJankenPlayerCount");
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			method.setAccessible(true);
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.nullPlayerList);

			//テストメソッド
			InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> method.invoke(this.jankenCuiGameApplicationImpl));

			//検証
			assertEquals(NullPointerException.class , e.getTargetException().getClass());

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testHasGameContinue001 正常系
	 * private void hasGameContinue() throws SystemException
	 * --確認事項--
	 * 1を入力するとtrueを返す
	 * --条件--
	 * KeybordクラスはgetIntメソッドで1を返すモック
	 * --検証項目--
	 * 1. 戻り値がtrueであるか
	 * 2. getIntメソッドが1回呼び出されているか
	 */
	@Test
	public void testHasGameContinue001() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt(this.one, this.two)).thenReturn(this.one);

			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("hasGameContinue");
			method.setAccessible(true);

			//テストメソッド
			boolean result = (boolean)method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			assertEquals(result, true);
			mockKeybord.verify(() -> Keybord.getInt(this.one, this.two), times(1));

		}catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testHasGameContinue002 正常系
	 * private void hasGameContinue() throws SystemException
	 * --確認事項--
	 * 2を入力するとfalseを返す
	 * --条件--
	 * KeybordクラスはgetIntメソッドで2を返すモック
	 * --検証項目--
	 * 1. 戻り値がfalseであるか
	 * 2. getIntメソッドが1回呼び出されているか
	 */
	@Test
	public void testHasGameContinue002() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt(this.one, this.two)).thenReturn(this.two);

			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("hasGameContinue");
			method.setAccessible(true);

			//テストメソッド
			boolean result = (boolean)method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			assertEquals(result, false);
			mockKeybord.verify(() -> Keybord.getInt(this.one, this.two), times(1));

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testHasGameContinue003 正常系
	 * private void hasGameContinue() throws SystemException
	 * --確認事項--
	 * 異常値を入力すると再度入力が求められる
	 * --条件--
	 * KeybordクラスはgetIntメソッドで1回目はApplicationExceptionを発生し、2回目は1を返すモック
	 * --検証項目--
	 * 1. 戻り値がtrueであるか
	 * 2. getIntメソッドが2回呼び出されているか
	 */
	@Test
	public void testHasGameContinue003() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt(this.one, this.two)).thenThrow(new ApplicationException(null)).thenReturn(this.one);

			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("hasGameContinue");
			method.setAccessible(true);

			//テストメソッド
			boolean result = (boolean)method.invoke(this.jankenCuiGameApplicationImpl);

			//検証
			assertEquals(result, true);
			mockKeybord.verify(() -> Keybord.getInt(this.one, this.two), times(2));

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testHasGameContinue004 異常系
	 * private void hasGameContinue() throws SystemException
	 * --確認事項--
	 * 範囲外の整数を入力し処理が進んでしまった場合、SystemExceptionを発生させる
	 * --条件--
	 * KeybordクラスはgetIntメソッドで異常値を返すモック
	 * --検証項目--
	 * 1. SystemExceptionがスローされるか
	 * 2. getIntメソッドが1回呼び出されているか
	 */
	@Test
	public void testHasGameContinue004() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class)){
			mockKeybord.when(() -> Keybord.getInt(this.one, this.two)).thenReturn(this.illegalValue);

			//準備
			Method method = JankenCuiGameApplicationImpl.class.getDeclaredMethod("hasGameContinue");
			method.setAccessible(true);

			//テストメソッド
			InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> method.invoke(this.jankenCuiGameApplicationImpl));

			//検証
			assertEquals(SystemException.class , e.getTargetException().getClass());
			mockKeybord.verify(() -> Keybord.getInt(this.one, this.two), times(1));

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetPlayerList001 正常系
	 * ppublic List<JankenPlayer> getPlayerList()
	 * --確認事項--
	 * playerListフィールドの値を返す
	 * --条件--
	 * playerListフィールドにArrayList<JankenPlayer>インスタンスが代入され, 要素が追加されている
	 * --検証項目--
	 * 1. 戻り値とplayerListフィールドの値が等しいか
	 * 2. 追加されている要素のデータは指定した値であるか
	 */
	@Test
	public void testGetPlayerList001() {
		try {
			//準備
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.playerList);

			//テストメソッド
			List<JankenPlayer> result = this.jankenCuiGameApplicationImpl.getPlayerList();

			//検証
			@SuppressWarnings("unchecked")
			List<JankenPlayer> playerList = (List<JankenPlayer>) playerListField.get(this.jankenCuiGameApplicationImpl);
			assertEquals(playerList, result);
			assertEquals(this.humanPlayer1.getPlayerName(), result.get(0).getPlayerName());
			assertEquals(this.cpuPlayer1.getPlayerName(), result.get(1).getPlayerName());

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetPlayerList002 正常系
	 * ppublic List<JankenPlayer> getPlayerList()
	 * --確認事項--
	 * playerListフィールドの値を返す
	 * --条件--
	 * playerListフィールドにArrayList<JankenPlayer>インスタンスが代入され, 要素が追加されていない
	 * --検証項目--
	 * 1. 戻り値とplayerListフィールドの値が等しいか
	 * 2. 追加されている要素のデータは指定した値であるか
	 */
	@Test
	public void testGetPlayerList002() {
		try {
			//準備
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.emptyPlayerList);

			//テストメソッド
			List<JankenPlayer> result = this.jankenCuiGameApplicationImpl.getPlayerList();

			//検証
			//検証
			@SuppressWarnings("unchecked")
			List<JankenPlayer> playerList = (List<JankenPlayer>) playerListField.get(this.jankenCuiGameApplicationImpl);
			assertEquals(playerList, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetPlayerList003 正常系
	 * ppublic List<JankenPlayer> getPlayerList()
	 * --確認事項--
	 * playerListフィールドの値を返す
	 * --条件--
	 * playerListフィールドにnullが代入されている場合
	 * --検証項目--
	 * 1. 戻り値がnullであるか
	 */
	@Test
	public void testGetPlayerList003() {
		try {
			//準備
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.nullPlayerList);

			//テストメソッド
			List<JankenPlayer> result = this.jankenCuiGameApplicationImpl.getPlayerList();

			//検証
			assertNull(result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSetPlayerList001 正常系
	 * ppublic void setPlayerList(List<JankenPlayer> playerList)
	 * --確認事項--
	 * playerListフィールドに値を代入する
	 * --条件--
	 * playerListフィールドの値はnull
	 * 引数に要素が追加されているArrayList<JankenPlayer>インスタンスを渡す
	 * --検証項目--
	 * 1. playerListフィールドの値と引数の値が等しいか
	 * 2. 追加されている要素のデータは指定した値であるか
	 */
	@Test
	public void testSetPlayerList001() {
		try {
			//準備
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			playerListField.setAccessible(true);

			//テストメソッド
			this.jankenCuiGameApplicationImpl.setPlayerList(this.playerList);

			//検証
			@SuppressWarnings("unchecked")
			List<JankenPlayer> result = (List<JankenPlayer>) playerListField.get(this.jankenCuiGameApplicationImpl);
			assertEquals(this.playerList.size(), result.size());
			assertEquals(this.humanPlayer1.getPlayerName(), result.get(0).getPlayerName());
			assertEquals(this.cpuPlayer1.getPlayerName(), result.get(1).getPlayerName());

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSetPlayerList002 正常系
	 * ppublic void setPlayerList(List<JankenPlayer> playerList)
	 * --確認事項--
	 * playerListフィールドに値を代入する
	 * --条件--
	 * playerListフィールドの値はnull
	 * 引数に要素が追加されていないArrayList<JankenPlayer>インスタンスを渡す
	 * --検証項目--
	 * 1. playerListフィールドの値と引数の値が等しいか
	 */
	@Test
	public void testSetPlayerList002() {
		try {
			//準備
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			playerListField.setAccessible(true);

			//テストメソッド
			this.jankenCuiGameApplicationImpl.setPlayerList(this.emptyPlayerList);

			//検証
			@SuppressWarnings("unchecked")
			List<JankenPlayer> result = (List<JankenPlayer>) playerListField.get(this.jankenCuiGameApplicationImpl);
			assertEquals(this.emptyPlayerList.size(), result.size());

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSetPlayerList003 異常系
	 * ppublic void setPlayerList(List<JankenPlayer> playerList)
	 * --確認事項--
	 * playerListフィールドに値を代入する
	 * --条件--
	 * playerListフィールドの値はnull
	 * 引数にnullを渡す
	 * --検証項目--
	 * 1. playerListフィールドの値はnullであるか
	 */
	@Test
	public void testSetPlayerList003() {
		try {
			//準備
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("playerList");
			playerListField.setAccessible(true);

			//テストメソッド
			this.jankenCuiGameApplicationImpl.setPlayerList(this.nullPlayerList);

			//検証
			@SuppressWarnings("unchecked")
			List<JankenPlayer> result = (List<JankenPlayer>) playerListField.get(this.jankenCuiGameApplicationImpl);
			assertNull(result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testGetWinHand001 正常系
	 * ppublic int getWinHand()
	 * --確認事項--
	 * winHandフィールドの値を返す
	 * --条件--
	 * winHandフィールドの値は1
	 * --検証項目--
	 * 1. 戻り値とwinHandフィールドの値が等しいか
	 */
	@Test
	public void testGetWinHand001() {
		try {
			//準備
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("winHand");
			playerListField.setAccessible(true);
			playerListField.set(this.jankenCuiGameApplicationImpl, this.one);

			//テストメソッド
			int result = this.jankenCuiGameApplicationImpl.getWinHand();

			//検証
			assertEquals(this.one, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testSetPlayerList001 正常系
	 * ppublic int setWinHand()
	 * --確認事項--
	 * winHandフィールドに値を代入する
	 * --条件--
	 * winHandフィールドの値は0
	 * --検証項目--
	 * 1. 引数とwinHandフィールドの値が等しいか
	 */
	@Test
	public void testSetWinHand001() {
		try {
			//準備
			Field playerListField = JankenCuiGameApplicationImpl.class.getDeclaredField("winHand");
			playerListField.setAccessible(true);

			//テストメソッド
			this.jankenCuiGameApplicationImpl.setWinHand(this.one);

			//検証
			int result = (int) playerListField.get(this.jankenCuiGameApplicationImpl);
			assertEquals(this.one, result);

		}catch(Exception  e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * testAction001 正常系
	 * public void action() throws SystemException
	 * --確認事項--
	 * humna:1人, cpu:1人, プレーヤー数2人以上 1回目2未満にして入力を再度求める
	 * human:グー、cpu:チョキ、1回目あいこにして再度入力を求める
	 * 継続し、同じ入力で繰り返して終了する
	 * --条件--
	 * KeybordクラスはgetIntで指定した値を返すモック
	 * RandomクラスはgetIntで指定した値を返すモック
	 * --検証項目--
	 * 1. SystemExceptionも含めて例外が発生しないことを確認
	 */
	@Test
	public void testAction001() {
		//モック化
		try(MockedStatic<Keybord> mockKeybord = mockStatic(Keybord.class);
				//CpuPlayerチョキ、CpuPlayer:チョキ、CpuPlayer:チョキ
			 MockedConstruction<Random> mockRandom = mockConstruction(
					 Random.class,
					 (random, context) -> when(random.nextInt(this.paper)).thenReturn(this.scissors-1)
			)
		){
			//入力(プレイヤー数) ー＞ HumanPlayer:1、CpuPlayer:0 |HumanPlayer1、CpuPlayer1 |HumanPlayer1、CpuPlayer1
			mockKeybord.when(() -> Keybord.getInt()).thenReturn(this.one).thenReturn(this.zero).thenReturn(this.one).thenReturn(this.one).thenReturn(this.one).thenReturn(this.one);
			//入力(継続するか)ー＞継続する | 終了する
			mockKeybord.when(() -> Keybord.getInt(this.one, this.two)).thenReturn(this.one).thenReturn(this.two);
			//入力(humanの手選択) ー＞ HumanPlayer:チョキ、HumanPlayer:グー、HumanPlayer:グー
			mockKeybord.when(() -> Keybord.getInt(this.rock, this.paper)).thenReturn(this.scissors).thenReturn(this.rock).thenReturn(this.rock);

			//テストメソッド
			this.jankenCuiGameApplicationImpl.action();
			

			//検証
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
