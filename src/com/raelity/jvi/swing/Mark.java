/**
 * Title:        jVi<p>
 * Description:  A VI-VIM clone.
 * Use VIM as a model where applicable.<p>
 * Copyright:    Copyright (c) Ernie Rael<p>
 * Company:      Raelity Engineering<p>
 * @author Ernie Rael
 * @version 1.0
 */

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
 * Copyright (C) 2000 Ernie Rael.  All Rights Reserved.
 * 
 * Contributor(s): Ernie Rael <err@raelity.com>
 */
package com.raelity.jvi.swing;

import javax.swing.text.*;
import com.raelity.jvi.ViTextView;
import com.raelity.jvi.ViMark;
import com.raelity.jvi.ViFPOS;
import com.raelity.jvi.MarkException;
import com.raelity.jvi.MarkOrphanException;
import com.raelity.jvi.*;
import com.raelity.text.TextUtil.MySegment;

/**
 * A Mark in vi specifies a row and column. The row "floats" as lines are
 * inserted and deleted earlier in the file. However the column is set when the
 * mark is created and does not change if characters are added on the same
 * line before the column.
 * <b>NEEDSWORK:</b> CLEAN UP Mark
 * <ul>
 * <li>A Mark should really only have the document, not the TextView.
 * Can do this when there is handling of doc to/from view.
 * <li>Consider garbage collection issues, this is holding ref to
 * both doc and editor.
 * <li>Add file name as class member?
 * <li>Before throwing OrphanMark, search for document in known editors.
 * </ul>
 */
class Mark implements ViMark {
  private Position pos;         // This tracks the line number
  private int col;
  private Document doc;
  private TextView editor;	// This is a ViPane, NEEDSWORK: remove

  /**
   * If the mark offset is not valid then this mark is converted into
   * a null mark.
   */
  void setOffset(int offset, ViTextView editor) {

    setEditor((TextView)editor);
    try {
      Position aPos = doc.createPosition(offset);
      setPos(aPos);
    } catch(BadLocationException ex) {
      pos = null;
      doc = null;
      editor = null;
      return;
    }
  }

  /**
   * If the mark offset is not valid then this mark is converted into
   * a null mark.
   */
  public void setMark(ViFPOS fpos, ViTextView editor) {
    if(fpos instanceof ViMark) {
      Mark m = (Mark)fpos;
      assert m.editor == this.editor && m.editor == editor;
      setData(m);
    } else {
      // adapted from FPOS.set
      int column = fpos.getColumn();
      int startOffset = G.curwin.getLineStartOffset(fpos.getLine());
      int endOffset = G.curwin.getLineEndOffset(fpos.getLine());
      
      if(column >= endOffset - startOffset) {
        ViManager.dumpStack("column " + column
                            + ", limit " + (endOffset - startOffset - 1));
        column = endOffset - startOffset - 1;
      }
      setOffset(startOffset + column, editor);
    }
  }

  private void setEditor(TextView editor) {
    this.editor = editor;
    doc = editor.getEditorComponent().getDocument();
  }

  Document getDoc() {
    checkMark();
    return doc;
  }

  private void setPos(Position pos) {
    checkMark();
    this.pos = pos;
    this.col = editor.getColumnNumber(pos.getOffset());
  }

  private void setPos(Position pos, int col) {
    checkMark();
    this.pos = pos;
    this.col = col;
  }

  private Position getPos() {
    checkMark();
    return pos;
  }

  public int getLine() {
    // NEEDSWORK: should use doc, not editor
    checkMark();
    return editor.getLineNumber(pos.getOffset());
  }

  /**
   * NOTE: may return column position of newline
   */
  public int getColumn() {
    // NEEDSWORK: should use doc, not editor
    MySegment seg = editor.getLineSegment(getLine());
    int len = seg.length() - 1;
    return seg.length() <= 0 ? 0 : Math.min(col, len);
  }

  // NEEDSWORK: Mark.getOffset(): get rid of this, bad usage
  public int getOffset() {
    checkMark();
    return editor.getLineStartOffsetFromOffset(pos.getOffset()) + getColumn();
  }

  public void setData(ViMark mark_arg) {
    Mark mark = (Mark)mark_arg;
    this.doc = mark.doc;
    this.editor = mark.editor;
    this.pos = mark.pos;
    this.col = mark.col;
  }

  public void invalidate() {
    doc = null;
  }

  public ViFPOS copy() {
    Mark m = new Mark();
    m.setData(this);
    return m;
  }

  final void checkMark() {
    if(doc == null) throw new MarkException("Uninitialized Mark");
    if(doc != editor.getEditorComponent().getDocument()) {
      throw new MarkOrphanException("Mark Document Change");
    }
  }

  final public int compareTo(Object o) {
    ViFPOS p = (ViFPOS)o;
    if(this.getOffset() < p.getOffset()) {
      return -1;
    } else if(this.getOffset() > p.getOffset()) {
      return 1;
    } else {
      return 0;
    }
  }

  public boolean equals(Object o) {
    if(o instanceof ViFPOS) {
      ViFPOS fpos = (ViFPOS)o;
      return getOffset() == fpos.getOffset();
    }
    return false;
  }

  /** This is optional, may throw an UnsupportedOperationException */
  public void set(int line, int col) {
    throw new UnsupportedOperationException();
  }

  /** This is optional, may throw an UnsupportedOperationException */
  public void setColumn(int col) {
    throw new UnsupportedOperationException();
  }
  /** This is optional, may throw an UnsupportedOperationException */
  public void setLine(int line) {
    throw new UnsupportedOperationException();
  }

  public void set(ViFPOS fpos) {
    throw new UnsupportedOperationException();
  }

  public String toString() {
    return	  "offset: " + getOffset()
	      	+ " lnum: " + getLine()
	      	+ " col: " + getColumn()
		;
  }
}

// vi:set sw=2 ts=8:
