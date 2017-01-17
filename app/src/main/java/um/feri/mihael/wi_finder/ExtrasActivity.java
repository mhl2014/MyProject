package um.feri.mihael.wi_finder;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class ExtrasActivity extends AppCompatActivity {

    private Button playButton;
    private VideoView videoDisplay;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extras);
        mediaController = new MediaController(this);

        videoDisplay = (VideoView) findViewById(R.id.extrasVideoPlayer);
        playButton = (Button) findViewById(R.id.extrasPlayButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriToVideo = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.wi_finder_video);
                videoDisplay.setVideoURI(uriToVideo);
                videoDisplay.setMediaController(mediaController);
                mediaController.setAnchorView(videoDisplay);
                videoDisplay.start();

            }
        });
    }
}
