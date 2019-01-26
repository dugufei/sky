package sky.example.di;

import sky.AutoID;
import sky.AutoExplain;
/**
 * 1.@AutoID注释类,会自动生成属性ID
 * 2.ID由架构统一管理,你可以定义任何属性不需要添加(public,static,final)..
 * 3.@AutoExplain 描述和参数
 */
@AutoID(-1605130299)
public final class TestID {
  @AutoExplain(describe = "")
  public static final int A = 1780568348;
  @AutoExplain(describe = "")
  public static final int B = 1780568349;
  @AutoExplain(describe = "")
  public static final int C = 1780568350;
  @AutoExplain(describe = "")
  public static final int D = 1780568351;
}
