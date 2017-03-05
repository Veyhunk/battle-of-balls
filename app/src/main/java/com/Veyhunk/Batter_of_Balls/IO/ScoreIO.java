package com.Veyhunk.Batter_of_Balls.IO;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import android.app.Activity;

public class ScoreIO extends Activity  {
	private   String fileName = "BallBastScore";

	public void saveScore(String stringSave) {
	
			try {

				FileOutputStream outputStream = openFileOutput(fileName,
						Activity.MODE_PRIVATE);
				outputStream.write(stringSave.getBytes());
				outputStream.flush();
				outputStream.close();
				System.out.println("save Ok:"+stringSave);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

	}
	

	public String readScore() {
		try {
			FileInputStream inputStream = this.openFileInput(fileName);
			byte[] bytes = new byte[1024];
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			while (inputStream.read(bytes) != -1) {
				arrayOutputStream.write(bytes, 0, bytes.length);
			}
			inputStream.close();
			arrayOutputStream.close();
			String content = new String(arrayOutputStream.toByteArray());
			System.out.println("read Ok:"+content);
			return content;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
