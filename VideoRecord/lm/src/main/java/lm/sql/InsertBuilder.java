package lm.sql;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import lm.util.Builder;

/**
 * Created by limin on 2016-01-29.
 */
public class InsertBuilder extends Builder<String> {
	private final String table;

	private final LinkedHashMap<String, Object> values = new LinkedHashMap<>();

	private String selection;

	public InsertBuilder(String table) {
		if(table == null || table.isEmpty()) {
			throw new Error("table name is null or empty!");
		}

		this.table = table;
	}

	private InsertBuilder addValue(String key, Object value) {
		if(key != null && !key.isEmpty() && value != null) {
			values.put(key, value);
		}
		return this;
	}

	public InsertBuilder add(String key, int value) {
		return addValue(key, value);
	}

	public InsertBuilder add(String key, long value) {
		return addValue(key, value);
	}

	public InsertBuilder add(String key, float value) {
		return addValue(key, value);
	}

	public InsertBuilder add(String key, double value) {
		return addValue(key, value);
	}

	public InsertBuilder add(String key, byte[] value) {
		return addValue(key, value);
	}

	public InsertBuilder add(String key, String value) {
		return addValue(key, value);
	}

	public InsertBuilder addSelection(String selection) {
		this.selection = selection;
		return this;
	}

	@Override
	public String build() {
		StringBuilder sb = new StringBuilder();

		sb.append("INSERT INTO ");
		sb.append(table);
		if(selection == null || selection.isEmpty()) {
			StringBuilder f = new StringBuilder();
			StringBuilder v = new StringBuilder();

			for(Map.Entry<String, Object> entry : values.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				if(f.length() != 0) {
					f.append(",");
					v.append(",");
				}

				f.append(key);

				if(value instanceof Number) {
					v.append(value);
				}
				else if(value instanceof byte[]) {
					v.append(formatBlob((byte[]) value));
				}
				else {
					v.append("'");
					v.append(value);
					v.append("'");
				}
			}

			sb.append("(");
			sb.append(f);
			sb.append(") ");
			sb.append("VALUES(");
			sb.append(v);
			sb.append(")");
		}
		else {
			sb.append(" ");
			sb.append(selection);
		}


		return sb.toString();
	}

	static String formatBlob(byte[] value) {
		StringBuilder sb = new StringBuilder();

		final int fragLength = 10240;

		ArrayList<Thread> threads = new ArrayList<Thread>();

		for(int index = 0; index < value.length; index += fragLength) {
			Thread thread = new FormatBlobFragment(value, index, fragLength);
			thread.start();
			threads.add(thread);
		}

		for(Thread thread : threads) {
			try {
				thread.join();
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

		sb.append("x'");
		for(Thread thread : threads) {
			sb.append(thread.toString());
		}
		sb.append("'");

		return sb.toString();
	}

	static class FormatBlobFragment extends Thread {
		private final byte[] bytes;
		private final int offset, length;
		private String mBlob;

		public FormatBlobFragment(byte[] bytes, int offset, int length) {
			this.bytes = bytes;
			this.offset = offset;
			this.length = length;
		}

		@Override
		public void run() {
			StringBuilder sb = new StringBuilder();
			for(int index = offset; index < (offset + length) && index < bytes.length; index++) {
				sb.append(String.format(Locale.ENGLISH, "%02x", bytes[index]));
			}
			mBlob = sb.toString();
		}

		@Override
		public String toString() {
			return mBlob;
		}
	}
}
