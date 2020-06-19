package com.goldze.mvvmhabit.game.equip;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadManagerUtils {
    private Context context;

    public DownloadManagerUtils(Context context) {
        this.context = context;
        getService();
        downLoadCompleteReceiver = new DownloadCompleteReceiver();
        downloadIds = new ArrayList<Long>();
    }

    DownloadManager downloadManager;

    private void getService() {
        String serviceString = Context.DOWNLOAD_SERVICE;
        downloadManager = (DownloadManager) context.getSystemService(serviceString);
    }

    List<Long> downloadIds;

    public long download(String uil, String title, String description) {
        Uri uri = Uri.parse(uil);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(title);
        request.setDescription(description);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.apk");
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setMimeType("application/vnd.android.package-archive");
        long id = downloadManager.enqueue(request);
        downloadIds.add(id);
        return id;
    }

    private boolean canDownloadState(Context context) {
        try {
            int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void enableDowaload(Context context) {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    public interface OnDownloadCompleted {
        public void onDownloadCompleted(long completeDownloadId);
    }

    OnDownloadCompleted onDownloadCompleted;

    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (onDownloadCompleted != null) {
                onDownloadCompleted.onDownloadCompleted(completeDownloadId);
            }
            for (int i = 0; i < downloadIds.size(); i++) {
                if (completeDownloadId == downloadIds.get(i)) {
                    downloadIds.remove(i);
                }
            }
        }
    }

    DownloadCompleteReceiver downLoadCompleteReceiver;

    public void registerReceiver(OnDownloadCompleted onDownloadCompleted) {
        context.registerReceiver(downLoadCompleteReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        this.onDownloadCompleted = onDownloadCompleted;
    }

    public void unregisterReceiver() {
        context.unregisterReceiver(downLoadCompleteReceiver);
    }

    public void installApk(Context context, String apkPath) {
        if (context == null || StringUtils.isEmpty(apkPath)) {
            return;
        }

        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);

    }

    public List<Long> getDownloadIds() {
        return downloadIds;
    }

}
