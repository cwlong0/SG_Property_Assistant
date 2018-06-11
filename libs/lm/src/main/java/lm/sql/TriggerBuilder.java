package lm.sql;

import java.util.ArrayList;

/**
 * Created by limin on 2016-01-28.
 */
public class TriggerBuilder {
	private final String trigger;

	private final TriggerEvent event;

	private final String column;

	private final String table;

	private TriggerWhen when;

	private final ArrayList<String> logic = new ArrayList<>();

	public TriggerBuilder(String trigger, TriggerEvent event, String column, String table) {
		if(trigger == null || trigger.isEmpty() || table == null || table.isEmpty()) {
			throw new Error("Trigger name and table name is null or empty");
		}

		this.column = column;
		this.trigger = trigger;
		this.event = event;
		this.table = table;
		this.when = TriggerWhen.NONE;
	}

	public TriggerBuilder(String trigger, TriggerEvent event, String table) {
		this(trigger, event, null, table);
	}

	public TriggerBuilder setWhen(TriggerWhen when) {
		this.when = when;
		return this;
	}

	public TriggerBuilder addTriggerLogic(String sql) {
		logic.add(sql);
		return this;
	}

	public String build() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TRIGGER IF NOT EXISTS ");
		sb.append(trigger);
		if(when != TriggerWhen.NONE) {
			sb.append(" ");
			sb.append(when.toString());
		}

		sb.append(" ");
		sb.append(event.toString());

		if(column != null && !column.isEmpty()) {
			sb.append(" OF ");
			sb.append(column);
		}

		sb.append(" ON ");
		sb.append(table);
		sb.append(" BEGIN ");

		for(String sql : logic) {
			sb.append(sql);
			sb.append(";");
		}

		sb.append(" END;");
		return sb.toString();
	}
}