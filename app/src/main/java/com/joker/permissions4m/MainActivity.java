package com.joker.permissions4m;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.joker.annotation.PermissionsCustomRationale;
import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsRationale;
import com.joker.annotation.PermissionsRequestSync;
import com.joker.api.Permissions4M;
import com.joker.api.support.PermissionsPageManager;
import com.joker.api.wrapper.Wrapper;
import com.joker.permissions4m.other.ToastUtil;

import static com.joker.permissions4m.MainActivity.CALENDAR_CODE;
import static com.joker.permissions4m.MainActivity.LOCATION_CODE;
import static com.joker.permissions4m.MainActivity.SENSORS_CODE;

@PermissionsRequestSync(permission = {Manifest.permission.BODY_SENSORS, Manifest.permission
        .ACCESS_FINE_LOCATION, Manifest.permission.READ_CALENDAR},
        value = {SENSORS_CODE, LOCATION_CODE, CALENDAR_CODE})
public class MainActivity extends AppCompatActivity {
    public static final int CALENDAR_CODE = 7;
    public static final int SENSORS_CODE = 8;
    public static final int LOCATION_CODE = 9;
    private static final int STORAGE_CODE = 1;
    private static final int CALL_LOG_CODE = 2;
    private static final int SMS_CODE = 5;
    private static final int AUDIO_CODE = 6;
    private static final int READ_CONTACTS_CODE = 10;
    private Button mCallButton;
    private Button mStorageButton;
    private Button mSmsButton;
    private Button mAudioButton;
    private Button mOneButton;
    private Button mSingleButton;
    private Button mManagerButton;
    private Button mPermissionPageButton;
    private Button mPageListenerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCallButton = (Button) findViewById(R.id.btn_call);
        mStorageButton = (Button) findViewById(R.id.btn_storage);
        mAudioButton = (Button) findViewById(R.id.btn_audio);
        mSmsButton = (Button) findViewById(R.id.btn_sms);
        mOneButton = (Button) findViewById(R.id.btn_one);
        mSingleButton = (Button) findViewById(R.id.btn_single);
        mManagerButton = (Button) findViewById(R.id.btn_manager);
        mPermissionPageButton = (Button) findViewById(R.id.btn_permission_page);
        mPageListenerButton = (Button) findViewById(R.id.btn_page_listener);

        mSingleButton = (Button) findViewById(R.id.btn_single);
        // 多个申请
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(MainActivity.this)
                        .requestForce(true)
                        .requestCode(CALL_LOG_CODE)
                        .requestPermission(Manifest.permission.READ_CALL_LOG)
                        .request();
            }
        });
        mStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(MainActivity.this)
                        .requestForce(true)
                        .requestCode(STORAGE_CODE)
                        .requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .request();
            }
        });

        // 自定义多个申请
        mSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(MainActivity.this)
                        .requestForce(true)
                        .requestPermission(Manifest.permission.READ_SMS)
                        .requestCode(SMS_CODE)
                        .request();
            }
        });
        mAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(MainActivity.this)
                        .requestPermission(Manifest.permission.RECORD_AUDIO)
                        .requestCode(AUDIO_CODE)
                        .request();
            }
        });

        // 同步申请
        mOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M
                        .get(MainActivity.this)
                        .requestSync();
            }
        });

        mSingleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SingleActivity.class));
            }
        });

        mManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PermissionsPageManager.getIntent(MainActivity.this));
            }
        });

        mPermissionPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PermissionsPageManager.getIntent(MainActivity.this));
            }
        });

        mPageListenerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(MainActivity.this)
                        .requestForce(true)
                        .requestPermission(Manifest.permission.READ_CONTACTS)
                        .requestCode(READ_CONTACTS_CODE)
                        .requestCallback(new Wrapper.PermissionRequestListener() {
                            @Override
                            public void permissionGranted() {
                                ToastUtil.show("读取通讯录权限成功 in activity with listener");
                            }

                            @Override
                            public void permissionDenied() {
                                ToastUtil.show("读取通讯录权失败 in activity with listener");
                            }

                            @Override
                            public void permissionRationale() {
                                ToastUtil.show("请打开读取通讯录权限 in activity with listener");
                            }
                        })
                        .requestPageType(Permissions4M.PageType.MANAGER_PAGE)
                        .requestPage(new Wrapper.PermissionPageListener() {
                            @Override
                            public void pageIntent(final Intent intent) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage("读取通讯录权限申请：\n我们需要您开启读取通讯录权限(in activity with listener)")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                            }
                        })
                        .request();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainActivity.this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //====================================================================
    @PermissionsGranted({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncGranted(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("地理位置权限授权成功 in activity with annotation");
                Log.e("TAG", "syncGranted: 地理位置权限授权成功 ");
                break;
            case SENSORS_CODE:
                ToastUtil.show("传感器权限授权成功 in activity with annotation");
                Log.e("TAG", "syncGranted: 传感器权限授权成功 ");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("读取日历权限授权成功 in activity with annotation");
                Log.e("TAG", "syncGranted: 读取日历权限授权成功 ");
                break;
            default:
                break;
        }
    }

    @PermissionsDenied({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncDenied(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("地理位置权限授权失败 in activity with annotation");
                Log.e("TAG", "syncDenied: 地理位置权限授权失败 ");
                break;
            case SENSORS_CODE:
                ToastUtil.show("传感器权限授权失败 in activity with annotation");
                Log.e("TAG", "syncDenied: 传感器权限授权失败 ");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("读取日历权限授权失败 in activity with annotation");
                Log.e("TAG", "syncDenied: 读取日历权限授权失败 ");
                break;
            default:
                break;
        }
    }

    @PermissionsRationale({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncRationale(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("请开启地理位置权限 in activity with annotation");
                break;
            case SENSORS_CODE:
                ToastUtil.show("请开启传感器权限 in activity with annotation");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("请开启读取日历权限 in activity with annotation");
                break;
            default:
                break;
        }
    }

    //====================================================================
    @PermissionsGranted({STORAGE_CODE, CALL_LOG_CODE})
    public void storageAndCallGranted(int code) {
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("设备存储权限授权成功 in activity with annotation");
                break;
            case CALL_LOG_CODE:
                ToastUtil.show("读取通话记录权限授权成功 in activity with annotation");
                break;
            default:
                break;
        }
    }

    @PermissionsDenied({STORAGE_CODE, CALL_LOG_CODE})
    public void storageAndCallDenied(int code) {
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("设备存储权限授权失败 in activity with annotation");
                break;
            case CALL_LOG_CODE:
                ToastUtil.show("读取通话记录权限授权失败 in activity with annotation");
                break;
            default:
                break;
        }
    }

    @PermissionsRationale({STORAGE_CODE, CALL_LOG_CODE})
    public void storageAndCallRationale(int code) {
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("请开启设备存储权限授权 in activity with annotation");
                break;
            case CALL_LOG_CODE:
                ToastUtil.show("请开启读取通话记录权限授权 in activity with annotation");
                break;
            default:
                break;
        }
    }

    //====================================================================
    @PermissionsGranted({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioGranted(int code) {
        switch (code) {
            case SMS_CODE:
                ToastUtil.show("短信权限申请成功 in activity with annotation");
                break;
            case AUDIO_CODE:
                ToastUtil.show("录音权限申请成功 in activity with annotation");
                break;
            default:
                break;
        }
    }

    @PermissionsDenied({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioDenied(int code) {
        switch (code) {
            case SMS_CODE:
                ToastUtil.show("短信权限申请失败 in activity with annotation");
                break;
            case AUDIO_CODE:
                ToastUtil.show("录音权限申请失败 in activity with annotation");
                break;
            default:
                break;
        }
    }

    @PermissionsCustomRationale({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioCustomRationale(int code) {
        switch (code) {
            case SMS_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("短信权限申请：\n我们需要您开启短信权限(in activity with annotation)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请自行处理申请权限，两者方法等价
                                // 方法1.使用框架封装方法
                                Permissions4M.get(MainActivity.this)
                                        .requestForce(true)
                                        .requestPermission(Manifest.permission.READ_SMS)
                                        .requestCode(SMS_CODE)
                                        .request();
                                // 方法2.使用自身方法
//                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
//                                        .permission.READ_SMS}, SMS_CODE);
                            }
                        })
                        .show();
                break;
            case AUDIO_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("录音权限申请：\n我们需要您开启录音权限(in activity with annotation)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请自行处理申请权限，两者方法等价
                                // 方法1.使用框架封装方法
                                Permissions4M.get(MainActivity.this)
                                        .requestForce(true)
                                        .requestPermission(Manifest.permission.RECORD_AUDIO)
                                        .requestCode(AUDIO_CODE)
                                        .request();
                                // 方法2.使用自身方法
//                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
//                                        .permission.RECORD_AUDIO}, AUDIO_CODE);
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }
}
