package lm.sql;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by limin on 2016-01-29.
 */
public class UpdateBuilder {
	private final String table;

	private final LinkedHashMap<String, Object> values = new LinkedHashMap<String, Object>();

	private String whereCause;

	private Object[] whereArgs;

	public UpdateBuilder(String table) {
		if(table == null || table.isEmpty()) {
			throw new Error("Table name is null or empty!");
		}
		this.table = table;
	}

	private UpdateBuilder addValue(String key, Object value) {
		if(key != null && !key.isEmpty()) {
			values.put(key, value);
		}
		return this;
	}

	public UpdateBuilder add(String key, int value) {
		return addValue(key, value);
	}

	public UpdateBuilder add(String key, long value) {
		return addValue(key, value);
	}

	public UpdateBuilder add(String key, float value) {
		return addValue(key, value);
	}

	public UpdateBuilder add(String key, double value) {
		return addValue(key, value);
	}

	public UpdateBuilder add(String key, byte[] value) {
		return addValue(key, value);
	}

	public UpdateBuilder add(String key, String value) {
		return addValue(key, value);
	}

	public UpdateBuilder setWhere(String whereCause, Object[] whereArgs) {
		this.whereCause = whereCause;
		this.whereArgs = whereArgs;
		return this;
	}

	public String build() {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(table);

		if(!values.isEmpty()) {
			sb.append(" SET ");
			boolean first = true;
			for(Map.Entry<String, Object> entry : values.entrySet()) {
				if(!first) {
					sb.append(",");
				}
				else {
					first = false;
				}

				sb.append(entry.getKey());

				if(entry.getValue() == null) {
					sb.append("=NULL");
				}
				else {
					Object v = entry.getValue();

					if(v instanceof Number) {
						sb.append("=");
						sb.append(v);
					}
					else if(v instanceof byte[]) {
						sb.append("=");
						sb.append(InsertBuilder.formatBlob((byte[]) v));
					}
					else {
						sb.append("='");
						sb.append(v);
						sb.append("'");
					}
				}


			}
		}

		if(whereCause != null && !whereCause.isEmpty()) {
			sb.append(" WHERE ");

		}

		return sb.toString();
	}
}
