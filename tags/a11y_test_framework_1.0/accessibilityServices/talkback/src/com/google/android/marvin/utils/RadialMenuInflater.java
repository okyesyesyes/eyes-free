
package com.google.android.marvin.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.googlecode.eyesfree.widget.RadialMenu;
import com.googlecode.eyesfree.widget.RadialMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Acts as an extension of {@link MenuInflater} for {@link RadialMenu}s by
 * allowing inflated {@link RadialMenuItem}s from XML to be returned directly to
 * a caller.
 *
 * @author caseyburkhardt@google.com (Casey Burkhardt)
 */
public class RadialMenuInflater extends MenuInflater {
    private final DummyRadialMenu mHolderMenu;

    public RadialMenuInflater(Context context) {
        super(context);
        mHolderMenu = new DummyRadialMenu(context);
    }

    /**
     * Returns a list of {@link MenuItem}s as generated by the inflater.
     *
     * @param menuRes The resource ID of the menu to inflate.
     * @return a {@link List} of the menu items in the main content area of the
     *         menu.
     */
    public List<RadialMenuItem> inflate(int menuRes) {
        // TODO(caseyburkhardt): See if supporting corners is feasible.
        mHolderMenu.clear();
        inflate(menuRes, mHolderMenu);
        final ArrayList<RadialMenuItem> items = new ArrayList<RadialMenuItem>(mHolderMenu.size());

        for (int i = 0; i < mHolderMenu.size(); ++i) {
            items.add(mHolderMenu.getItem(i));
        }

        return items;
    }

    private static class DummyRadialMenu extends RadialMenu {

        public DummyRadialMenu(Context context) {
            super(context, new DialogInterface() {

                @Override
                public void dismiss() {
                    // Do nothing.
                }

                @Override
                public void cancel() {
                    // Do nothing.
                }
            });
        }
    }
}
