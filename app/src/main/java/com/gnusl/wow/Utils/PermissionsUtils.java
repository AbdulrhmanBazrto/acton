package com.gnusl.wow.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;

public class PermissionsUtils {

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 74;
    public static final int EDIT_REPORT_SUCCESS = 200;
    public static final int CALENDAR_PERMISSION = 300;
    public static final int BRANCHES_PERMISSION = 400;
    public static final int CAMERA_PERMISSIONS_REQUEST = 500;
    public static final int GALLERY_PERMISSIONS_REQUEST = 500;

    public static boolean checkCameraPermissions(Context context) {

        ArrayList<String> scanReportPermissionsList = new ArrayList<>();

        scanReportPermissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        scanReportPermissionsList.add(Manifest.permission.CAMERA);

        return PermissionsUtils.askForPermissions(scanReportPermissionsList, context);

    }

    public static boolean checkReadGalleryPermissions(Context context) {

        ArrayList<String> scanReportPermissionsList = new ArrayList<>();

        scanReportPermissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        return PermissionsUtils.askForPermissions(scanReportPermissionsList, context);

    }

    public static boolean askForPermissions(ArrayList<String> requiredPermissions, Context context) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //should check for runtime permission

            int permissionCheck;

            Iterator<String> iterator = requiredPermissions.iterator();

            while (iterator.hasNext()) {

                permissionCheck = ContextCompat.checkSelfPermission(context,
                        iterator.next());

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        } else //no need for run time permissions
            return true;
    }

}
