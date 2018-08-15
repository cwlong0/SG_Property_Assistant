package lm.sql;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by limin on 2016-01-28.
 */
public class TableBuilder {
	private final static String NUMBER_REX = "(^[\\d]+\\.?[\\d]*$)|(^(0x)([\\d]|[a-f]|[A~F])+)";

	private final ArrayList<Field> fields;

	private final HashMap<String, Field> fieldHashMap;

	private final ArrayList<String> primaryKeys;

	private final String table;

	public TableBuilder(String table) {
		if(table == null || table.length() == 0) {
			throw new Error("No table name!");
		}

		this.table = table;
		this.fields = new ArrayList<Field>();
		this.fieldHashMap = new HashMap<String, Field>();
		this.primaryKeys = new ArrayList<String>();
	}

	public TableBuilder addField(String field, DataType dataType, String defaultValue, Constraint... constraint) {
		if(field != null && !field.isEmpty() && !fieldHashMap.containsKey(field)) {
			Field f = new Field(field, dataType, defaultValue, constraint);
			fields.add(f);
			fieldHashMap.put(field, f);
		}
		return this;
	}

	public TableBuilder addField(String field, DataType dataType, Constraint... constraint) {
		if(field != null && !field.isEmpty() && !fieldHashMap.containsKey(field)) {
			Field f = new Field(field, dataType, constraint);
			fields.add(f);
			fieldHashMap.put(field, f);
		}
		return this;
	}

	public TableBuilder addPrimaryKey(String... fields) {
		for(String pk : fields) {
			if(fieldHashMap.containsKey(pk)) {
				primaryKeys.add(pk);
			}
		}
		return this;
	}

	public String build() {
		if(fields.isEmpty()) {
			throw new Error("No field!");
		}

		StringBuilder sb = new StringBuilder();

		sb.append("CREATE TABLE IF NOT EXISTS");
		sb.append(" ");
		sb.append(table);
		sb.append("(");

		for(int index = 0; index < fields.size(); index++) {
			StringBuilder fsb = new StringBuilder();
			if(index != 0) {
				fsb.append(",");
			}

			Field field = fields.get(index);

			fsb.append(field.field);
			fsb.append(" ");
			fsb.append(field.dataType);
			for(Constraint constraint : field.constraints) {
				if(constraint != Constraint.NONE) {
					fsb.append(" ");
					fsb.append(constraint.toString());
				}
			}

			if(field.defaultValue != null && !field.defaultValue.isEmpty()) {
				if((field.dataType == DataType.INTEGER || field.dataType == DataType.FLOAT ||
						field.dataType == DataType.DOUBLE)) {

					if(field.defaultValue.matches(NUMBER_REX)) {
						fsb.append(" DEFAULT(");
						fsb.append(field.defaultValue);
						fsb.append(")");
					}
				}
				else {
					fsb.append(" DEFAULT('");
					fsb.append(field.defaultValue);
					fsb.append("')");
				}
			}

			sb.append(fsb);
		}


		if(!primaryKeys.isEmpty()) {
			StringBuilder pk = new StringBuilder();

			pk.append(", PRIMARY KEY (");

			for(int index = 0; index < primaryKeys.size(); index++) {
				if(index != 0) {
					pk.append(",");
				}

				pk.append("'");
				pk.append(primaryKeys.get(index));
				pk.append("'");
			}
			pk.append(")");

			sb.append(pk);
		}
		sb.append(")");

		return sb.toString();
	}
}
