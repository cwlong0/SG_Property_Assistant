package lm.location;

/**
 * Created by limin on 2016/07/18.
 */
public class LMLocationUtil {

	public static native LMLocation toGoogle(LMLocation gps);

	static {
		System.loadLibrary("location");
	}
}
