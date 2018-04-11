package gdut.bsx.share2.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

/**
 * @author baishixian
 * @date 2018/3/29 11:00
 */
public class DemoActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 100;
    private static final int REQUEST_SHARE_FILE_CODE = 120;
    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 121;

    private TextView tvShareFileUri;
    private Uri shareFileUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        tvShareFileUri = findViewById(R.id.tv_share_file_url);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_PERMISSION);
            } else {
                Toast.makeText(this, "缺少文件读写权限，可能会造成无法分享文件", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "缺少文件读写权限，可能会造成无法分享文件", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void handlerShare(View view) {
        switch (view.getId()) {
            case R.id.bt_choose_share_file:
                openFileChooser();
                break;
            case R.id.bt_share_text:
                new Share2.Builder(this)
                        .setContentType(ShareContentType.TEXT)
                        .setTextContent("This is a test message.")
                        .setTitle("Share Text")
                        // .forcedUseSystemChooser(false)
                        .build()
                        .shareBySystem();
                break;
            case R.id.bt_share_image:
                  new Share2.Builder(this)
                          .setContentType(ShareContentType.IMAGE)
                          .setShareFileUri(getShareFileUri())
                          //.setShareToComponent("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI")
                          .setTitle("Share Image")
                          .build()
                          .shareBySystem();
                break;
            case R.id.bt_share_audio:
                new Share2.Builder(this)
                        .setContentType(ShareContentType.AUDIO)
                        .setShareFileUri(getShareFileUri())
                        .setTitle("Share Audio")
                        .build()
                        .shareBySystem();
                break;
            case R.id.bt_share_video:
                new Share2.Builder(this)
                        .setContentType(ShareContentType.VIDEO)
                        .setShareFileUri(getShareFileUri())
                        .setTitle("Share Video")
                        .build()
                        .shareBySystem();
                break;
            case R.id.bt_share_file:
                new Share2.Builder(this)
                        .setContentType(ShareContentType.FILE)
                        .setShareFileUri(getShareFileUri())
                        .setTitle("Share File")
                        .setOnActivityResult(REQUEST_SHARE_FILE_CODE)
                        .build()
                        .shareBySystem();
                break;
            default:
                break;
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), FILE_SELECT_CODE);
            overridePendingTransition(0, 0);
        } catch (Exception ex) {
            // Potentially direct the user to the Market with OnProgressChangeListener Dialog
            Toast.makeText(this, getString(R.string.please_install_filemanager), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DemoActivity", "requestCode=" + requestCode + " resultCode=" + resultCode);
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            shareFileUrl = data.getData();
            tvShareFileUri.setText(shareFileUrl.toString());

            // String filePath = FileUtil.getFileRealPath(this, shareFileUrl);
            // shareFileUrl = FileUtil.getFileUri(this, null, new File(filePath));
        } else if (requestCode == REQUEST_SHARE_FILE_CODE){
            // todo share complete.
        }
    }

    public Uri getShareFileUri() {
        if (shareFileUrl == null) {
            tvShareFileUri.setText("Please choose a file to share.");
        }
        return shareFileUrl;
    }
}
