package pe.hgs.truler.phase.subphase;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import pe.hgs.truler.R;

public class PreventionVideo extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

	private VideoView video;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prevention_video);

		Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.newsroom);

		video = (VideoView) findViewById(R.id.video_pv_prevention);
		video.setOnCompletionListener(this);
		video.setOnPreparedListener(this);
		video.setVideoURI(uri);
		MediaController mc = new MediaController(this);
		video.setMediaController(mc);
		video.requestFocus();
	}

	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
		video.seekTo(0);
		video.start();
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {

	}
}
