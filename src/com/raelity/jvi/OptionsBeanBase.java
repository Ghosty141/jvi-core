/*
 * OptionsBeanBase.java
 *
 * Created on January 23, 2007, 11:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.raelity.jvi;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.beans.PropertyVetoException;
import java.beans.SimpleBeanInfo;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.List;
import java.util.prefs.Preferences;
import org.openide.ErrorManager;

/**
 * Base class for jVi options beans. This method contains the read/write methods
 * for all options. Which options are made visible is controlled by the
 * optionsList given to the contstructor. Using this class, options are
 * grouped into different beans.
 *
 * @author erra
 */
public class OptionsBeanBase extends SimpleBeanInfo {
    private Class clazz;
    private List<String> optionsList;
    private String displayName;
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport( this );
    private final VetoableChangeSupport vcs = new VetoableChangeSupport( this ); 
    
    /** Creates a new instance of OptionsBeanBase */
    public OptionsBeanBase(Class clazz, String displayName,
                           List<String> optionsList) {
        this.clazz = clazz;
        this.displayName = displayName;
        this.optionsList = optionsList;
    }
    
    public BeanDescriptor getBeanDescriptor() {
        return new ThisBeanDescriptor();
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
	PropertyDescriptor[] descriptors
                    = new PropertyDescriptor[optionsList.size()];
	int i = 0;

	for(String name : optionsList) {
	    Option opt = Options.getOption(name);
            PropertyDescriptor d;
            try {
                d = new PropertyDescriptor(opt.getName(), clazz);
            } catch (IntrospectionException ex) {
                ex.printStackTrace();
                continue;
            }
	    d.setDisplayName(opt.getDisplayName());
	    d.setExpert(opt.isExpert());
	    d.setHidden(opt.isHidden());
	    d.setShortDescription(opt.getDesc());
            if(opt instanceof IntegerOption
               || opt instanceof StringOption) {
                d.setBound(true);
                d.setConstrained(true);
            }
	    descriptors[i++] = d;
	}
	return descriptors;
    }
    
    private class ThisBeanDescriptor extends BeanDescriptor {
        ThisBeanDescriptor() {
            super(clazz);
        }
        
        public String getDisplayName() {
	    return displayName;
        }
    }
    
    //
    // Look like a good bean
    //
    
    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        this.pcs.addPropertyChangeListener( listener );
    }

    public void removePropertyChangeListener( PropertyChangeListener listener )
    {
        this.pcs.removePropertyChangeListener( listener );
    }
    
    public void addVetoableChangeListener( VetoableChangeListener listener )
    {
        this.vcs.addVetoableChangeListener( listener );
    }

    public void removeVetoableChangeListener( VetoableChangeListener listener )
    {
        this.vcs.addVetoableChangeListener( listener );
    } 
    
    //
    // All the known options
    //      The interface to preferences.
    //
    private Preferences prefs = ViManager.getViFactory().getPreferences();

    protected void put(String name, String val) throws PropertyVetoException {
        String old = getString(name);
	Option opt = Options.getOption(name);
        ((StringOption)opt).validate(val);
        this.vcs.fireVetoableChange( name, old, val );
	prefs.put(name, val);
        this.pcs.firePropertyChange( name, old, val );
    }

    protected void put(String name, int val) throws PropertyVetoException {
        int old = getint(name);
	Option opt = Options.getOption(name);
        ((IntegerOption)opt).validate(val);
        this.vcs.fireVetoableChange( name, old, val );
	prefs.putInt(name, val);
        this.pcs.firePropertyChange( name, old, val );
    }

    protected void put(String name, boolean val) {
	prefs.putBoolean(name, val);
    }

    private String getString(String name) {
	Option opt = Options.getOption(name);
	return prefs.get(name, opt.getDefault());
    }

    private int getint(String name) {
	Option opt = Options.getOption(name);
	return prefs.getInt(name, Integer.parseInt(opt.getDefault()));
    }

    private boolean getboolean(String name) {
	Option opt = Options.getOption(name);
	return prefs.getBoolean(name, Boolean.parseBoolean(opt.getDefault()));
    }
    
    //
    // All the known options
    //      The bean getter/setter
    //

    public void setViCommandEntryFrameOption(boolean arg) {
        put("viCommandEntryFrameOption", arg);
    }

    public boolean getViCommandEntryFrameOption() {
	return getboolean("viCommandEntryFrameOption");
    }

    public void setViBackspaceWrapPrevious(boolean arg) {
        put("viBackspaceWrapPrevious", arg);
    }

    public boolean getViBackspaceWrapPrevious() {
	return getboolean("viBackspaceWrapPrevious");
    }

    public void setViHWrapPrevious(boolean arg) {
        put("viHWrapPrevious", arg);
    }

    public boolean getViHWrapPrevious() {
	return getboolean("viHWrapPrevious");
    }

    public void setViLeftWrapPrevious(boolean arg) {
        put("viLeftWrapPrevious", arg);
    }

    public boolean getViLeftWrapPrevious() {
	return getboolean("viLeftWrapPrevious");
    }

    public void setViSpaceWrapNext(boolean arg) {
        put("viSpaceWrapNext", arg);
    }

    public boolean getViSpaceWrapNext() {
	return getboolean("viSpaceWrapNext");
    }

    public void setViLWrapNext(boolean arg) {
        put("viLWrapNext", arg);
    }

    public boolean getViLWrapNext() {
	return getboolean("viLWrapNext");
    }

    public void setViRightWrapNext(boolean arg) {
        put("viRightWrapNext", arg);
    }

    public boolean getViRightWrapNext() {
	return getboolean("viRightWrapNext");
    }

    public void setViTildeWrapNext(boolean arg) {
        put("viTildeWrapNext", arg);
    }

    public boolean getViTildeWrapNext() {
	return getboolean("viTildeWrapNext");
    }

    public void setViUnnamedClipboard(boolean arg) {
        put("viUnnamedClipboard", arg);
    }

    public boolean getViUnnamedClipboard() {
	return getboolean("viUnnamedClipboard");
    }

    public void setViJoinSpaces(boolean arg) {
        put("viJoinSpaces", arg);
    }

    public boolean getViJoinSpaces() {
	return getboolean("viJoinSpaces");
    }

    public void setViShiftRound(boolean arg) {
        put("viShiftRound", arg);
    }

    public boolean getViShiftRound() {
	return getboolean("viShiftRound");
    }

    public void setViNotStartOfLine(boolean arg) {
        put("viNotStartOfLine", arg);
    }

    public boolean getViNotStartOfLine() {
	return getboolean("viNotStartOfLine");
    }

    public void setViChangeWordBlanks(boolean arg) {
        put("viChangeWordBlanks", arg);
    }

    public boolean getViChangeWordBlanks() {
	return getboolean("viChangeWordBlanks");
    }

    public void setViTildeOperator(boolean arg) {
        put("viTildeOperator", arg);
    }

    public boolean getViTildeOperator() {
	return getboolean("viTildeOperator");
    }

    public void setViSearchFromEnd(boolean arg) {
        put("viSearchFromEnd", arg);
    }

    public boolean getViSearchFromEnd() {
	return getboolean("viSearchFromEnd");
    }

    public void setViWrapScan(boolean arg) {
        put("viWrapScan", arg);
    }

    public boolean getViWrapScan() {
	return getboolean("viWrapScan");
    }

    public void setViMetaEquals(boolean arg) {
        put("viMetaEquals", arg);
    }

    public boolean getViMetaEquals() {
	return getboolean("viMetaEquals");
    }

    public void setViMetaEscape(String arg) throws PropertyVetoException {
        put("viMetaEscape", arg);
    }

    public String getViMetaEscape() {
	return getString("viMetaEscape");
    }

    public void setViIgnoreCase(boolean arg) {
        put("viIgnoreCase", arg);
    }

    public boolean getViIgnoreCase() {
	return getboolean("viIgnoreCase");
    }

    public void setViExpandTabs(boolean arg) {
        put("viExpandTabs", arg);
    }

    public boolean getViExpandTabs() {
	return getboolean("viExpandTabs");
    }

    public void setViReport(int arg) throws PropertyVetoException {
        put("viReport", arg);
    }

    public int getViReport() {
	return getint("viReport");
    }

    public void setViBackspace(int arg) throws PropertyVetoException {
        put("viBackspace", arg);
    }

    public int getViBackspace() {
	return getint("viBackspace");
    }

    public void setViScrollOff(int arg) throws PropertyVetoException {
        put("viScrollOff", arg);
    }

    public int getViScrollOff() {
	return getint("viScrollOff");
    }

    public void setViShiftWidth(int arg) throws PropertyVetoException {
        put("viShiftWidth", arg);
    }

    public int getViShiftWidth() {
	return getint("viShiftWidth");
    }

    public void setViTabStop(int arg) throws PropertyVetoException {
        put("viTabStop", arg);
    }

    public int getViTabStop() {
	return getint("viTabStop");
    }

    public void setViReadOnlyHack(boolean arg) {
        put("viReadOnlyHack", arg);
    }

    public boolean getViReadOnlyHack() {
	return getboolean("viReadOnlyHack");
    }

    public void setViClassicUndo(boolean arg) {
        put("viClassicUndo", arg);
    }

    public boolean getViClassicUndo() {
	return getboolean("viClassicUndo");
    }

    public void setViDbgKeyStrokes(boolean arg) {
        put("viDbgKeyStrokes", arg);
    }

    public boolean getViDbgKeyStrokes() {
	return getboolean("viDbgKeyStrokes");
    }

    public void setViDbgCache(boolean arg) {
        put("viDbgCache", arg);
    }

    public boolean getViDbgCache() {
	return getboolean("viDbgCache");
    }

    public void setViDbgEditorActivation(boolean arg) {
        put("viDbgEditorActivation", arg);
    }

    public boolean getViDbgEditorActivation() {
	return getboolean("viDbgEditorActivation");
    }
}