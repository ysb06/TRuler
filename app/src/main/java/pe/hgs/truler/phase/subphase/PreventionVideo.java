package pe.hgs.truler.phase.subphase;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import pe.hgs.truler.R;

public class PreventionVideo extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, View.OnClickListener {

	public static final String DANGER_ELBOW = "video_elbow";
	public static final String DANGER_SHOULDER = "video_shoulder";
	public static final String DANGER_WAIST = "video_waist";
	public static final String DANGER_KNEE = "video_knee";
	public static final String DANGER_ANKLE = "video_An";

	private VideoView video;

	private Button button01;
	private Button button02;
	private Button button03;
	private Button button05;
	private Button button06;
	private Uri video01;
	private Uri video02;
	private Uri video03;
	private Uri video04;
	private Uri video05;
	private Uri video06;

	private boolean bDangerEl = false;    //팔꿈치
	private boolean bDangerSh = false;    //어깨
	private boolean bDangerWa = false;    //허리
	private boolean bDangerKn = false;    //무릎
	private boolean bDangerAn = false;    //발목

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prevention_video);

		Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.newsroom);
		video01 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.gneck);
		video02 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.gshoulder);
		video03 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ghand);
		video04 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ghand);
		video05 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.gleg);
		video06 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.gwaist);

		video = (VideoView) findViewById(R.id.video_pv_prevention);
		video.setOnCompletionListener(this);
		video.setOnPreparedListener(this);
		video.setVideoURI(uri);
		MediaController mc = new MediaController(this);
		video.setMediaController(mc);
		video.requestFocus();

		button01 = (Button) findViewById(R.id.button_video_01);
		button02 = (Button) findViewById(R.id.button_video_02);
		button03 = (Button) findViewById(R.id.button_video_03);
		button05 = (Button) findViewById(R.id.button_video_05);
		button06 = (Button) findViewById(R.id.button_video_06);
		button01.setOnClickListener(this);
		button02.setOnClickListener(this);
		button03.setOnClickListener(this);
		button05.setOnClickListener(this);
		button06.setOnClickListener(this);


		Intent data = getIntent();
		bDangerEl = data.getBooleanExtra(DANGER_ELBOW, false);
		bDangerSh = data.getBooleanExtra(DANGER_SHOULDER, false);
		bDangerWa = data.getBooleanExtra(DANGER_WAIST, false);
		bDangerKn = data.getBooleanExtra(DANGER_KNEE, false);
		bDangerAn = data.getBooleanExtra(DANGER_ANKLE, false);
		// TODO: 2016-11-01 추후 위험 부위에 따라 표시 버튼 나타나게 할 지 정할 것

		if (bDangerEl) {
			button03.setBackgroundResource(R.drawable.video_button_hand_activated);
			video.setVideoURI(video03);
		}

		if (bDangerSh) {
			button01.setBackgroundResource(R.drawable.video_button_neck_activated);
			button02.setBackgroundResource(R.drawable.video_button_shoulder_activated);
			video.setVideoURI(video01);
		}

		if (bDangerWa) {
			button06.setBackgroundResource(R.drawable.video_button_waist_activated);
			video.setVideoURI(video06);
		}

		if (bDangerKn) {
			button05.setBackgroundResource(R.drawable.video_button_ankle_activated);
			video.setVideoURI(video05);
		}

		if (bDangerAn) {
			button05.setBackgroundResource(R.drawable.video_button_ankle_activated);
			video.setVideoURI(video05);
		}
	}

	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
		video.seekTo(0);
		video.start();
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {

	}

	private boolean bSwitch01 = false;
	private boolean bSwitch02 = false;
	private boolean bSwitch03 = false;
	private boolean bSwitch04 = false;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_video_01:
				play(video01);
				bSwitch02 = bSwitch03 = bSwitch04 = false;
				bSwitch01 = true;
				break;
			case R.id.button_video_02:
				play(video02);
				if(bSwitch03)
					bSwitch04 = true;
				else
					bSwitch01 = bSwitch02 = bSwitch03 = bSwitch04 = false;
				break;
			case R.id.button_video_03:
				play(video03);
				if(bSwitch04) {
					Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.newsroom);
					play(uri);
					bSwitch01 = bSwitch02 = bSwitch03 = bSwitch04 = false;
				}
				break;
			case R.id.button_video_04:
				play(video04);
				break;
			case R.id.button_video_05:
				play(video05);
				if (bSwitch02)
					bSwitch03 = true;
				else
					bSwitch01 = bSwitch02 = bSwitch03 = bSwitch04 = false;
				break;
			case R.id.button_video_06:
				if (bSwitch01)
					bSwitch02 = true;
				else
					bSwitch01 = bSwitch02 = bSwitch03 = bSwitch04 = false;
				play(video06);
				break;
		}
	}

	private void play(Uri uri) {
		video.setVideoURI(uri);
		video.seekTo(0);
		video.start();
	}
}
