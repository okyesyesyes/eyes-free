/*
 * Copyright (C) 2011 Google Inc.
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

package com.googlecode.eyesfree.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.SubMenu;
import android.view.View;

/**
 * Implements a radial sub-menu.
 * 
 * @author alanv@google.com (Alan Viverette)
 */
public class RadialSubMenu extends RadialMenu implements SubMenu {
    private final RadialMenuItem mMenuItem;

    /**
     * Creates a new radial sub-menu and associated radial menu item.
     * 
     * @param context The parent context.
     * @param groupId The menu item's group identifier.
     * @param itemId The menu item's identifier (should be unique).
     * @param order The order of this item.
     * @param title The text to be displayed for this menu item.
     */
    /* package */RadialSubMenu(Context context, DialogInterface parent, int groupId, int itemId,
            int order, CharSequence title) {
        super(context, parent);

        mMenuItem = new RadialMenuItem(context, groupId, itemId, order, title, this);
    }

    @Override
    public void clearHeader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RadialMenuItem getItem() {
        return mMenuItem;
    }

    @Override
    public SubMenu setHeaderIcon(int iconRes) {
        mMenuItem.setIcon(iconRes);

        return this;
    }

    @Override
    public SubMenu setHeaderIcon(Drawable icon) {
        mMenuItem.setIcon(icon);

        return this;
    }

    @Override
    public SubMenu setHeaderTitle(int titleRes) {
        mMenuItem.setTitle(titleRes);

        return this;
    }

    @Override
    public SubMenu setHeaderTitle(CharSequence title) {
        mMenuItem.setTitle(title);

        return this;
    }

    @Override
    public SubMenu setHeaderView(View view) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SubMenu setIcon(int iconRes) {
        mMenuItem.setIcon(iconRes);

        return this;
    }

    @Override
    public SubMenu setIcon(Drawable icon) {
        mMenuItem.setIcon(icon);

        return this;
    }

}
