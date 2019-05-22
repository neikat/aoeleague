package com.sonic.aoeleague.updatelog;

public abstract class Action {

	public abstract void execute();
	public abstract void undo();
	public void undo(int times) {
		for (int i = 0; i < times; i++) {
			undo();
		}
	}
}
