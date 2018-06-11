package lm.location;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * Created by limin on 2016/07/18.
 */
public class LMLocation implements Parcelable{
	public double latitude;

	public double longitude;

	public static double defLatitude;
	public static double defLongitude;

	public LMLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	protected LMLocation(Parcel in) {
		latitude = in.readDouble();
		longitude = in.readDouble();
	}

	public static final Creator<LMLocation> CREATOR = new Creator<LMLocation>() {
		@Override
		public LMLocation createFromParcel(Parcel in) {
			return new LMLocation(in);
		}

		@Override
		public LMLocation[] newArray(int size) {
			return new LMLocation[size];
		}
	};

	@Override
	public String toString() {
		return String.format(Locale.getDefault(), "%1$d: %2$f,%3$f", hashCode(), latitude, longitude);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
	}

	public static LMLocation getCurrent() {
		return new LMLocation(defLatitude, defLongitude);
	}

	public static double getCurrentLatitude() {
		return defLatitude;
	}

	public static double getCurrentLongitude() {
		return defLongitude;
	}
}
