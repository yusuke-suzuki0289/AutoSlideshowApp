package jp.techacademy.yusuke2.suzuki.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    int judge = 0; //判定用変数
    private Cursor cursor; //メンバ変数として宣言
    Timer timer = new Timer(); //停止の場合にtimer.cancel();を使用したいため、ここでインスタンス生成

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ボタンの取得とリスナーの登録(必ずOnclickの外で定義する→基本的にはOnCreate)
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(this);

        //パーミッションを表示する（Android 6.0以降の場合）
        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo();
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //パーミッションに対するユーザの選択結果を受け取る
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                }
                break;
            default:
                break;
        }
    }

    //初期画像表示
    public void getContentsInfo() {
        // indexからIDを取得し、そのIDから画像のURIを取得する
        // URIとは目的のデータを示すために使われるもの。PCであればファイルをディレクトリで指定するが、AndroidではURIで指定する感じ。
        // 画像の情報URIを全件取得する
        if (judge == 0) {
            ContentResolver resolver = getContentResolver();//ContentResolverでContentProviderのデータを参照
            cursor = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                    null, // 項目(null = 全項目)
                    null, // フィルタ条件(null = フィルタなし)
                    null, // フィルタ用パラメータ
                    null // ソート (null ソートなし)
            );

            cursor.moveToFirst();
            setImageURI();

        } else if (judge == 1) {
            cursor.moveToFirst();
            setImageURI();

        } else if (judge == 2) {
            if(cursor.isLast () == true) {
                cursor.moveToFirst();
            }else{
                cursor.moveToNext();
            }
                setImageURI();

        } else if (judge == 3) {
            if(cursor.isFirst () == true) {
                cursor.moveToLast();
            }else{
                cursor.moveToPrevious();
            }
            setImageURI();

        } else if (judge == 4) {
            if (cursor.isLast() == true) {
                cursor.moveToFirst();
            } else {
                cursor.moveToNext();
            }
            setImageURI();
        }
    }


    public void setImageURI() {
        //URIが入っているカラムの箇所の取得
        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        //画像ファイルの固有番号を確認
        Long id = cursor.getLong(fieldIndex);
        //固有番号からURLの取得
        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
        imageVIew.setImageURI(imageUri);
    }

    public void onClick(View v) {

        //先頭に戻るボタンが押された場合/
        if (v.getId() == R.id.button1) {
            judge = 1;
            getContentsInfo();
            Log.d("javatest","ボタン１");
            //進むボタンが押された場合
        } else if (v.getId() == R.id.button2) {
            judge = 2;
            getContentsInfo();
            Log.d("javatest","ボタン２");
            //戻るボタンが押された場合
        } else if (v.getId() == R.id.button3) {
            judge = 3;
            getContentsInfo();
            Log.d("javatest","ボタン３");
            //再生ボタンが押された場合
        } else if (v.getId() == R.id.button4) {
            judge = 4;
            long period;
            Button button1 = (Button) findViewById(R.id.button1);
            Button button2 = (Button) findViewById(R.id.button2);
            Button button3 = (Button) findViewById(R.id.button3);
            final Button button4 = (Button) findViewById(R.id.button4);

            String buttontxt4 = button4.getText().toString(); //入力文字の取得

            if (buttontxt4.equals("再生")) {
                period = 2000; //タイマーの秒数をローカル変数として宣言(ミリ秒単位で設定要)
                button1.setEnabled(false);//ボタン1無効化
                button2.setEnabled(false);//ボタン2無効化
                button3.setEnabled(false);//ボタン3無効化
                button4.setText("停止");  //ボタン4の名称を「停止」に変更

                if(timer == null) {
                    timer = new Timer(); //タイマーの生成
                    timer.schedule(new TimerTask() {    //スケジュールの実行
                        @Override
                        public void run() {
                            button4.post(new Runnable() {
                                public void run() {
                                    getContentsInfo();
                                    return;
                                }
                            });
                        }
                    }, 0, period);

                }else{
                    Log.d("javatest", "タイマーの処理不正が発生。");
                }
            }else{
                period = 0;
                if(timer != null){
                    timer.cancel();
                    timer = null;
                }
                button1.setEnabled(true);//ボタン1無効化
                button2.setEnabled(true);//ボタン2無効化
                button3.setEnabled(true);//ボタン3無効化
                button4.setText("再生");  //ボタン名「再生」に変更

            }
            getContentsInfo();
            Log.d("javatest", "ボタン４");
            }

    }

}