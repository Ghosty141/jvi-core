/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is jvi - vi editor clone.
 *
 * The Initial Developer of the Original Code is Ernie Rael.
 * Portions created by Ernie Rael are
 * Copyright (C) 2000-2010 Ernie Rael.  All Rights Reserved.
 *
 * Contributor(s): Ernie Rael <err@raelity.com>
 */

package com.raelity.jvi.swing;

/**
 * No code folding, pretty much a 1-1 mapping of line numbers.
 *
 * @author Ernie Rael <err at raelity.com>
 */
public class SwingViewMapNoFolding implements ViewMap
{
    SwingTextView tv;

    public SwingViewMapNoFolding(SwingTextView tv)
    {
        this.tv = tv;
    }

    public boolean isFontFixed()
    {
        return true;
    }

    public boolean isFontFixedHeight()
    {
        return true;
    }

    public boolean isFontFixedWidth()
    {
        return true;
    }

    public boolean isFolding()
    {
        return false;
    }

    public int viewLine(int docLine) throws RuntimeException
    {
        return docLine;
    }

    public int docLine(int viewLine)
    {
        return viewLine;
    }

    public int docLineOffset(int viewLine)
    {
        return tv.getBuffer().getLineStartOffset(viewLine);
    }

}