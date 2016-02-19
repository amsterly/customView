package my.test.com.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;

public class DisplayActivity extends AppCompatActivity {

    private CanvasView id_canvasview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_display);
        id_canvasview = (CanvasView) findViewById(R.id.id_canvasview);
    }
    public void onBtnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.id_drawtext:
                id_canvasview.mMethod= CanvasView.Method.id_drawtext;
                break;
            case R.id.id_measuretext:
                break;
            case R.id.id_drawshape:
                id_canvasview.mMethod= CanvasView.Method.id_drawshape;
                break;
            case R.id.id_drawbyhand:
                id_canvasview.cleanPath();
                id_canvasview.mMethod = CanvasView.Method.id_drawbyhand;
                break;
            case R.id.id_drawClock:
                id_canvasview.mMethod= CanvasView.Method.id_drawClock;
                break;
            case R.id.id_drawGif:
                id_canvasview.mMethod= CanvasView.Method.id_drawGif;
                break;
            default:break;
        }
        id_canvasview.invalidate();
    }
}
