/*
 * Copyright (C) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.marvin.shell;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Class for encapsulating the information needed to start up an application
 * 
 * @author clchen@google.com (Charles L. Chen)
 */
public class AppEntry {
    private String title;

    private String packageName;

    private String className;

    private String scriptName;

    private Drawable icon;

    private ArrayList<Param> params;

    private ResolveInfo rInfo;

    AppEntry(String appTitle, String appPackageName, String appClassName, String appScriptName,
            Drawable appIcon, ArrayList<Param> parameters) {
        title = appTitle;
        packageName = appPackageName;
        className = appClassName;
        scriptName = appScriptName;
        icon = appIcon;
        params = parameters;
    }

    AppEntry(String appTitle, ResolveInfo info, ArrayList<Param> parameters) {
        title = appTitle;
        packageName = null;
        className = null;
        icon = null;
        rInfo = info;
        params = parameters;
    }

    String getTitle() {
        return title;
    }

    String getPackageName() {
        if (packageName != null) {
            return packageName;
        }
        return rInfo.activityInfo.packageName;
    }

    String getClassName() {
        if (className != null) {
            return className;
        }
        return rInfo.activityInfo.name;
    }

    String getScriptName() {
        if (scriptName != null) {
            return scriptName;
        }
        return "";
    }

    Drawable getIcon(PackageManager pm) {
        if (icon != null) {
            return icon;
        }
        return rInfo.loadIcon(pm);
    }

    ArrayList<Param> getParams() {
        return params;
    }

    private boolean addToXml(String str) {
        return ((null != str) && (str.length() > 0));
    }

    /**
     * Returns a String xml representation of this appInfo element.
     * 
     * @return String xml representation of this appInfo object
     */
    public String toXml() {
        String xmlStr = "    <appInfo";
        if (addToXml(getPackageName())) {
            xmlStr = xmlStr + " package='" + getPackageName() + "'";
        }
        if (addToXml(getClassName())) {
            xmlStr = xmlStr + " class='" + getClassName() + "'";
        }
        if (addToXml(getScriptName())) {
            xmlStr = xmlStr + " script='" + getScriptName() + "'";
        }
        xmlStr = xmlStr + "/>\n";

        // TODO: Populate "params" and "icon"
        return xmlStr;
    }
}
