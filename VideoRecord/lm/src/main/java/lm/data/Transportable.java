package lm.data;

/**
 * Created by limin on 16/01/19.
 */
public interface Transportable {

	void writeToTransporter(Transporter dest);

	interface Creator<T> {
		T createFromTransporter(Transporter source);

		T[] newArray(int size);
	}
}