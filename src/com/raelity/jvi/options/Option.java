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
package com.raelity.jvi.options;

import com.raelity.jvi.core.Options.Category;
import java.awt.Color;
import java.beans.PropertyVetoException;

public abstract class Option {
    protected String name;
    protected String displayName;
    protected String stringValue;
    protected String desc;
    protected String defaultValue;
    protected boolean fExpert;
    protected boolean fHidden;
    Category category;
    
    protected boolean fPropogate; // used in logic, not part of option type
    
    public Option(String key, String defaultValue) {
        this(key, defaultValue, true);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Option(String key, String defaultValue, boolean doInit) {
	name = key;
	this.defaultValue = defaultValue;
	fExpert = false;
        fHidden = false;
        if(doInit)
            initialize();
    }

    protected void initialize() {
	fPropogate = false;
	String initialValue = OptUtil.getPrefs().get(name, defaultValue);
	setValue(initialValue);
	fPropogate = true;

    }

    abstract void setValue(String value);

    public String getValue() {
	return stringValue;
    }

    public String getName() {
	return name;
    }
    
    public String getDefault() {
	return defaultValue;
    }
    
    public String getDesc() {
	return desc;
    }

    public String getDisplayName() {
	if(displayName != null) {
	    return displayName;
	} else {
	    return name;
	}
    }

    public boolean isExpert() {
	return fExpert;
    }

    public boolean isHidden() {
	return fHidden;
    }
    
    public void setHidden(boolean f) {
        fHidden = f;
    }
    
    public void setExpert(boolean f) {
        fExpert = f;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setDesc(String desc)
    {
        if (this.desc != null) {
            throw new Error("option: " + name + " already has a description.");
        }
        this.desc = desc;
    }

    public void setDisplayName(String displayName)
    {
        if (this.displayName != null) {
            throw new Error("option: " + name + " already has a display name.");
        }
        this.displayName = displayName;
    }

    /**
     * The preferences data base has changed, stay in sync.
     * Do not propogate change back to data base.
     */
    void preferenceChange(String newValue) {
	fPropogate = false;
        try {
	    //System.err.println("preferenceChange " + name + ": " + newValue);
            setValue(newValue);
        } finally {
	    fPropogate = true;
        }
    }

    protected void propogate() {
	if(fPropogate) {
            OptUtil.getPrefs().put(name, stringValue);
	}
    }
    
    public int getInteger() {
        throw new ClassCastException(this.getClass().getSimpleName()
                                     + " is not an IntegerOption");
    }
    
    public boolean getBoolean() {
        throw new ClassCastException(this.getClass().getSimpleName()
                                     + " is not a BooleanOption");
    }
    
    public String getString() {
        throw new ClassCastException(this.getClass().getSimpleName()
                                     + " is not a StringOption");
    }
    
    public Color getColor() {
        throw new ClassCastException(this.getClass().getSimpleName()
                                     + " is not a ColorOption");
    }
    
    public void validate(int val) throws PropertyVetoException {
        throw new ClassCastException(this.getClass().getSimpleName()
                                     + " is not an IntegerOption");
    }
    
    public void validate(boolean val) throws PropertyVetoException {
        throw new ClassCastException(this.getClass().getSimpleName()
                                     + " is not a BooleanOption");
    }
    
    public void validate(String val) throws PropertyVetoException {
        throw new ClassCastException(this.getClass().getSimpleName()
                                     + " is not a StringOption");
    }
    
    public void validate(Color val) throws PropertyVetoException {
        throw new ClassCastException(this.getClass().getSimpleName()
                                     + " is not a ColorOption");
    }
    
    public void validate(Object val) throws PropertyVetoException {
        if(val instanceof String)
            validate((String)val);
        else if(val instanceof Color)
            validate((Color)val);
        else if(val instanceof Boolean)
            validate(((Boolean)val).booleanValue());
        else if(val instanceof Integer)
            validate(((Integer)val).intValue());
        else 
            throw new ClassCastException(val.getClass().getSimpleName()
                                    + " is not int, boolean, Color or String");
    }
}

// vi: sw=4 et
