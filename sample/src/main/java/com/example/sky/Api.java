package com.example.sky;

import sky.AutoID;
import sky.AutoExplain;
import java.lang.Integer;
import java.lang.String;
import com.example.sky.test.User;
/**
 * 1.@AutoID注释类,会自动生成属性ID
 * 2.ID由架构统一管理,你可以定义任何属性不需要添加(public,static,final)..
 * 3.@AutoExplain 描述和参数
 */
@AutoID(2076214460)
public final class Api {
  @AutoExplain(describe = "")
  public static final int RRRRRRR = -1290380970;
  @AutoExplain(describe = "aaa",params = {Integer.class,String.class,User.class})
  public static final int A = -61861115;
  @AutoExplain(describe = "aaa")
  public static final int SSS = 681030583;
  @AutoExplain(describe = "aaa")
  public static final int B = -61861114;
  @AutoExplain(describe = "aaa")
  public static final int C = -61861113;
  @AutoExplain(describe = "aaa")
  public static final int DDDD = -363350084;
  @AutoExplain(describe = "")
  public static final int FFFF = -363288516;
}
