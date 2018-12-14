package me.mikasa.picturescanner.listener;

import java.util.List;

/**
 * Created by mikasacos on 2018/9/22.
 */

/**
 * 权限管理接口
 */
public interface PermissionListener {
    void onGranted();
    //void onGranted(List<String>grantedPermission);
    void onDenied(List<String> deniedPermission);
}
