package com.o9pathshala.student.test;

public class ClockUpdate {
	public static String clock(long millis) {
		millis/=1000;
		int millisUntilFinished = (int) millis;
		StringBuffer timeBuffer = new StringBuffer();
		String hours = String.valueOf((millisUntilFinished / 3600));
		if (hours.length() == 2) {
			timeBuffer.append(hours + " : ");
		} else {
			timeBuffer.append("0" + hours + " : ");
		}
		millisUntilFinished = millisUntilFinished
				- ((millisUntilFinished / 3600) * 3600);
		String minutes = String.valueOf(millisUntilFinished / 60);
		if (minutes.length() == 2) {
			timeBuffer.append(minutes + " : ");
		} else {
			timeBuffer.append("0" + minutes + " : ");
		}
		millisUntilFinished = millisUntilFinished
				- ((millisUntilFinished / 60) * 60);
		String seconds = String.valueOf(millisUntilFinished);
		if (seconds.length() == 2) {
			timeBuffer.append(seconds);
		} else {
			timeBuffer.append("0" + seconds);
		}
		return timeBuffer.toString();
	}
}
