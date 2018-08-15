package lm.util;

import java.util.ArrayList;

public class TaskThreadArray extends TaskArray {

	@Override
	public void onRun() throws Exception {
		ArrayList<TaskThread> threads = new ArrayList<>();

		for(Task task : this) {
			TaskThread taskThread = new TaskThread(task);
			taskThread.start();

			threads.add(taskThread);
		}

		for(TaskThread taskThread : threads) {
			taskThread.join();
		}
	}

	private static class TaskThread extends Thread {
		private Task mTask;

		public TaskThread(Task task) {
			mTask = task;
		}

		@Override
		public void run() {
			try {
				mTask.run();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
