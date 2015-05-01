package com.o9pathshala.student.slidingmenu.nav_logic;

import android.app.Activity;
import android.content.Context;

import com.o9pathshala.ui.AlertDialogActivity;
import com.o9pathshala.ui.IAlertLogic;
import com.o9pathshala.ui.dto.AlertDialogDTO;

public class BackPressed {
	private Context context;
	public BackPressed(Context context) {
		this.context = context;
	}
	public void backPressed() {
		
		IAlertLogic iAlertLogic = new IAlertLogic() {
			
			public void positiveButton() {
				((Activity)context).finish();
			}

			public void negativeButton() {
			}
		};
		AlertDialogDTO alertDialogDTO = new AlertDialogDTO();
		alertDialogDTO.setContext(((Activity)context));
		alertDialogDTO.setMessage("Are you sure you want to exit..");
		alertDialogDTO.setTitle("Alert !!");
		alertDialogDTO.setPositiveButon("Exit");
		alertDialogDTO.setNegativeButton("Cancel");
		AlertDialogActivity.alertDialogDisplay(alertDialogDTO, iAlertLogic);
	}
}
