package veyhunk.battle_of_balls.db;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class GameProgress extends Activity  {
	private static String fileName = "BallBastScore";
    private Context context;

    public GameProgress(Context context) {
        this.context=context.getApplicationContext();
    }
	public void Save() {
		String stringSave = "";
		try {
			// 为root创建一个JSONObject对象
			JSONObject ball = new JSONObject();
			// 为root JSONObject对象添加一个“名称,值”对
			ball.put("bestScore", GameParams.bestScore);
			ball.put("ballName", GameParams.ballName);
			ball.put("ballColorIndex", GameParams.ballColorIndex);
			ball.put("ballGrowSpeed", GameParams.ballGrowSpeed);
			ball.put("ballMoveSpeed", GameParams.ballMoveSpeed);
			ball.put("aiDifficult", GameParams.aiDifficult);
			stringSave = ball.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {

			FileOutputStream outputStream = context.openFileOutput(fileName,
					Activity.MODE_PRIVATE);
			outputStream.write(stringSave.getBytes());
			outputStream.flush();
			outputStream.close();
			System.out.println("save Ok:" + stringSave);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void Read() {

		try {
			FileInputStream inputStream = context.openFileInput(fileName);
			byte[] bytes = new byte[1024];
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			while (inputStream.read(bytes) != -1) {
				arrayOutputStream.write(bytes, 0, bytes.length);
			}
			inputStream.close();
			arrayOutputStream.close();
			String content = new String(arrayOutputStream.toByteArray());
			System.out.println("read Ok:" + content.trim());
			// return content.trim();
            try {
                // initialize
                // object_result
                JSONObject ball = new JSONObject(content.trim());
                // handle result

                GameParams.bestScore = ball.getString("bestScore");
                GameParams.ballName = ball.getString("ballName");
                GameParams.ballColorIndex = Integer.parseInt(ball.getString("ballColorIndex"));
                GameParams.ballGrowSpeed = Float.parseFloat(ball.getString("ballGrowSpeed"));
                GameParams.ballMoveSpeed = Float.parseFloat(ball.getString("ballMoveSpeed"));
                GameParams.aiDifficult = Integer.parseInt(ball.getString("aiDifficult"));


                if (GameParams.ballName.length() == 0) {
                    GameParams.ballName = "感情淡了放盐啊";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

		} catch (IOException e) {
			e.printStackTrace();

			GameParams.bestScore = "0";
			GameParams.ballName = "感情淡了放盐啊";
			GameParams.ballColorIndex = 0;
			GameParams.ballGrowSpeed = 10;
			GameParams.ballMoveSpeed = 10;
			GameParams.aiDifficult = 10;
		}
    }


}
