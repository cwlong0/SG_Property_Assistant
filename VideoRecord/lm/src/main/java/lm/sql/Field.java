package lm.sql;

/**
 * Created by limin on 2016-01-28.
 */
public class Field {
	public String field;

	public DataType dataType;

	public String defaultValue;

	public Constraint[] constraints;

	public Field(String field, DataType dataType, String defaultValue, Constraint... constraint) {
		this.field = field;
		this.dataType = dataType;
		this.defaultValue = defaultValue;
		this.constraints = constraint;
	}

	public Field(String field, DataType dataType, Constraint... constraint) {
		this(field, dataType, null, constraint);
	}
}
