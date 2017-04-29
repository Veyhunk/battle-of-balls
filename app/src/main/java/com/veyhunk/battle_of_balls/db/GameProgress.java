package com.veyhunk.battle_of_balls.db;

import android.app.Activity;
import android.content.Context;

import com.veyhunk.battle_of_balls.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class GameProgress {
    private final static String fileName = "BallBastScore";
    private final Context context;

    public GameProgress(Context context) {
        this.context = context.getApplicationContext();
    }

    public void Save() {
        String stringSave = "";
        try {
            // 为root创建一个JSONObject对象
            JSONObject ball = new JSONObject();
            // 为root JSONObject对象添加一个“名称,值”对
            ball.put("BEST_SCORE", GameParams.BEST_SCORE);
            ball.put("PLAYER_NAME", GameParams.PLAYER_NAME);
            ball.put("PLAYER_TEAM_COLOR", GameParams.PLAYER_TEAM_COLOR);
            ball.put("BALL_GROW_SPEED", GameParams.BALL_GROW_SPEED);
            ball.put("BALL_MOVE_SPEED", GameParams.BALL_MOVE_SPEED);
            ball.put("BALL_WEIGHT_DEFAULT", GameParams.BALL_WEIGHT_DEFAULT);
            ball.put("AI_DIFFICULT", GameParams.AI_DIFFICULT);
            ball.put("TEAM_AMOUNT", GameParams.TEAM_PARAMS.TEAM_AMOUNT);
            ball.put("TEAM_MEMBER_AMOUNT", GameParams.TEAM_PARAMS.TEAM_MEMBER_AMOUNT);
            ball.put("TEAM_MEMBER_MAX", GameParams.TEAM_PARAMS.TEAM_MEMBER_MAX);
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

                GameParams.BEST_SCORE = ball.getString("BEST_SCORE");
                GameParams.PLAYER_NAME = ball.getString("PLAYER_NAME");
                GameParams.PLAYER_TEAM_COLOR = Integer.parseInt(ball.getString("PLAYER_TEAM_COLOR"));
                GameParams.BALL_GROW_SPEED = Float.parseFloat(ball.getString("BALL_GROW_SPEED"));
                GameParams.BALL_MOVE_SPEED = Float.parseFloat(ball.getString("BALL_MOVE_SPEED"));
                GameParams.BALL_WEIGHT_DEFAULT = Integer.parseInt(ball.getString("BALL_WEIGHT_DEFAULT"));
                GameParams.AI_DIFFICULT = Integer.parseInt(ball.getString("AI_DIFFICULT"));
                GameParams.TEAM_PARAMS.TEAM_AMOUNT = Integer.parseInt(ball.getString("TEAM_AMOUNT"));
                GameParams.TEAM_PARAMS.TEAM_MEMBER_AMOUNT = Integer.parseInt(ball.getString("TEAM_MEMBER_AMOUNT"));
                GameParams.TEAM_PARAMS.TEAM_MEMBER_MAX =Integer.parseInt(ball.getString("TEAM_MEMBER_MAX"));


                if (GameParams.PLAYER_NAME.length() == 0) {
                    GameParams.PLAYER_NAME = context.getString(R.string.default_name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                GameParams.BEST_SCORE = "0";
                GameParams.PLAYER_NAME = context.getString(R.string.default_name);
                GameParams.PLAYER_TEAM_COLOR = 0;
                GameParams.BALL_GROW_SPEED = 100;
                GameParams.BALL_MOVE_SPEED = 30;
                GameParams.BALL_WEIGHT_DEFAULT = 2500;
                GameParams.AI_DIFFICULT = 10;
                GameParams.TEAM_PARAMS.TEAM_AMOUNT = 4;
                GameParams.TEAM_PARAMS.TEAM_MEMBER_AMOUNT =3;
                GameParams.TEAM_PARAMS.TEAM_MEMBER_MAX =16;
            }

        } catch (IOException e) {
            e.printStackTrace();

            GameParams.BEST_SCORE = "0";
            GameParams.PLAYER_NAME = context.getString(R.string.default_name);
            GameParams.PLAYER_TEAM_COLOR = 0;
            GameParams.BALL_GROW_SPEED = 100;
            GameParams.BALL_MOVE_SPEED = 30;
            GameParams.BALL_WEIGHT_DEFAULT = 2500;
            GameParams.AI_DIFFICULT = 10;
            GameParams.TEAM_PARAMS.TEAM_AMOUNT = 4;
            GameParams.TEAM_PARAMS.TEAM_MEMBER_AMOUNT =3;
            GameParams.TEAM_PARAMS.TEAM_MEMBER_MAX =16;
        }
    }


}
