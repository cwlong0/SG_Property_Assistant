package lm.sql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lm.util.Builder;

/**
 * Created by limin on 2016-02-01.
 */
public class QueryBuilder extends Builder<String>{
	private final String table;

	private final String[] columns;

	private String selection;

	private Object[] selectionArgs;

	private String groupBy;

	private String having;

	private String orderBy;

	private String limit;

	public QueryBuilder(String[] columns) {
		this(null, columns);
	}

	public QueryBuilder(String table, String[] columns) {
		this.table = table;
		this.columns = columns;
	}

	public QueryBuilder setSelection(String selection) {
		return setSelection(selection, null);
	}

	public QueryBuilder setSelection(String selection, Object[] selectionArgs) {
		this.selection = selection;
		this.selectionArgs = selectionArgs;
		return this;
	}

	public QueryBuilder setGroupBy(String groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public QueryBuilder setHaving(String having) {
		this.having = having;
		return this;
	}

	public QueryBuilder setOrderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public QueryBuilder setLimit(String limit) {
		this.limit = limit;
		return this;
	}

	public QueryBuilder setLimit(int page, int count) {
		this.limit = page + "," + count;
		return this;
	}

	@Override
	public String build() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		if(columns == null) {
			sb.append(" *");
		}
		else {
			sb.append(" ");
			for(int index = 0; index < columns.length; index++) {
				if(index != 0) {
					sb.append(",");
				}

				sb.append(columns[index]);
			}
		}

		if(table != null && !table.isEmpty()) {
			sb.append(" FROM ");
			sb.append(table);
		}

		if(selection != null && !selection.isEmpty()) {
			sb.append(" WHERE ");
			if(selectionArgs != null && selectionArgs.length != 0) {
				Pattern p = Pattern.compile("(\\?)");
				Matcher m = p.matcher(selection);
				StringBuffer buffer = new StringBuffer();

				int index = 0;
				while(m.find()) {
					Object object = selectionArgs[index++];

					String replace = object instanceof Number ?
							object.toString() : "'" + object.toString() + "'";

					m.appendReplacement(buffer, replace);
				}
				sb.append(buffer);
			}
			else {
				sb.append(selection);
			}
		}

		if(groupBy != null && !groupBy.isEmpty()) {
			sb.append(" GROUP BY ");
			sb.append(groupBy);
		}

		if(having != null && !having.isEmpty()) {
			sb.append(" HAVING ");
			sb.append(having);
		}

		if(orderBy != null && !orderBy.isEmpty()) {
			sb.append(" ORDER By ");
			sb.append(orderBy);
		}

		if(limit != null && !limit.isEmpty()) {
			sb.append(" LIMIT ");
			sb.append(limit);
		}

		return sb.toString();
	}
}
