package lm.util;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class TaskArray extends Task implements Iterable<Task> {

	private final ArrayList<Task> mTasks = new ArrayList<>();

	public void add(Task task) {
		synchronized(mTasks) {
			mTasks.add(task);
		}
	}

	public int size() {
		return mTasks.size();
	}

	public Task get(int index) {
		return mTasks.get(index);
	}

	@Override
	public Iterator<Task> iterator() {
		return mTasks.iterator();
	}
}
