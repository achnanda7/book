package com.achnanda7.bookinventory_nanda.core;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Process;
import android.support.v7.app.AlertDialog;

import com.achnanda7.bookinventory_nanda.activity.LoginActivity;
import com.achnanda7.bookinventory_nanda.database.dbHelper;
import com.achnanda7.bookinventory_nanda.helper.SessionManagement;


public class CoreApplication extends Application {
    SessionManagement session;
    public static CoreApplication app;
    private dbHelper dbInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        session = new SessionManagement(this);
        dbInstance = dbHelper.getDbInstance(getApplicationContext());

        app = this;
    }

    public dbHelper getDatabase() {
        return dbInstance;
    }

    public static CoreApplication getInstance() {
        return app;
    }

    public SessionManagement getSession() {
        return session;
    }

    public void showQuitConfirmation(Context konteks) {
        AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(konteks);
        kotakBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        kotakBuilder.setTitle("Quit Confirmation");
        kotakBuilder.setMessage("Do you want to quit ?");
        kotakBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        closeApplication();
                    }
                });
        kotakBuilder.setNegativeButton("no", null);
        kotakBuilder.create().show();
    }

    public void closeApplication() {
        Process.killProcess(Process.myPid());
    }

    public void logout() {
        if(session.getRememberMe()) {
            session.setKeyIsLoggedIn(false);
        } else {
            getSharedPreferences("bookscatalog", 0).edit().clear().commit();
        }
        CoreApplication.getInstance().getDatabase().clearAndInitDatabase();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
