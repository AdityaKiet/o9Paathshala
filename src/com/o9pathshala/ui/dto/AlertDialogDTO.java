package com.o9pathshala.ui.dto;

import android.content.Context;

public class AlertDialogDTO {
	private String title;
	private String message;
	private String positiveButon;
	private String negativeButton;
	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPositiveButon() {
		return positiveButon;
	}

	public void setPositiveButon(String positiveButon) {
		this.positiveButon = positiveButon;
	}

	public String getNegativeButton() {
		return negativeButton;
	}

	public void setNegativeButton(String negativeButton) {
		this.negativeButton = negativeButton;
	}

}
