package jp.co.ginga.application.janken;

import java.util.EnumSet;

import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.properties.MessageProperties;

/**
 * JankenParam ENUM
 * @author yoshi
 *
 */
public enum JankenParam {

	DRAW(0),
	ROCK(1),
	SCISSORS(2),
	PAPER(3),
	;
	
	
	/**
	 * ENUM設定値
	 */
    private int enumValue;

    // コンストラクタの定義
    private JankenParam(int enumValue) {
        this.enumValue = enumValue;
    }

    /**
     * ENUM設定値 取得処理
	 * @return enumValue
	 */
	public int getInt() {
		return this.enumValue;
	}


    /**
     * ENUM値 取得処理
     * @param value
     * @return enum値
     * @throws SystemException 
     */
    public static JankenParam getEnum(final int value) throws SystemException {
        for(JankenParam jp : EnumSet.allOf(JankenParam.class)) {
            if(jp.ordinal() == value) {  //ordinal() 列挙子の順番を取得
                return jp;
            }
        }
        throw new SystemException(MessageProperties.getMessage("error.stop"));
        
    }

}