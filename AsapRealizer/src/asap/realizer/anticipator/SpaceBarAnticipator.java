/*******************************************************************************
 * Copyright (C) 2009-2020 Human Media Interaction, University of Twente, the Netherlands
 *
 * This file is part of the Articulated Social Agents Platform BML realizer (ASAPRealizer).
 *
 * ASAPRealizer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License (LGPL) as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ASAPRealizer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ASAPRealizer.  If not, see http://www.gnu.org/licenses/.
 ******************************************************************************/
package asap.realizer.anticipator;

import hmi.util.PhysicsSync;
import hmi.util.SystemClock;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;

/**
 * Manages two TimePegs, linked to the time of a spacebar press and release. 
 * The corresponding sync points are called press and release respectively.   
 * @author welberge
 */
public class SpaceBarAnticipator extends Anticipator implements KeyListener, KeyInfo
{
    private TimePeg release;
    private TimePeg press;
    private SystemClock physicsClock;
    private boolean pressed = false;
    private SBAObservable observable;    
    
    private static class SBAObservable extends Observable
    {
        @Override
        public void setChanged()
        {
            super.setChanged();
        }
    }
    
    public void reset()
    {
        //TODO: make scheduler able to handle anticipator timepegs with unknown value!
        release.setGlobalValue(100000000);
        press.setGlobalValue(100000000);
    }
    
    public SpaceBarAnticipator(String id, PegBoard pb)
    {
        super(id,pb);
        release = new TimePeg(BMLBlockPeg.GLOBALPEG);
        press = new TimePeg(BMLBlockPeg.GLOBALPEG);
        observable = new SBAObservable();
        reset();
        
        addSynchronisationPoint("release", release);
        addSynchronisationPoint("press", press);
    }
    
    public void addObserver(Observer o) 
    {
        observable.addObserver(o);
    }
    
    public void setPhysicsClock(SystemClock phClock)
    {
        synchronized(PhysicsSync.getSync())
        {
            physicsClock = phClock;
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_SPACE)
        {
            if(!pressed)
            {    
                pressed = true;
                synchronized(PhysicsSync.getSync())
                {
                    press.setGlobalValue(physicsClock.getMediaSeconds());                    
                }
                observable.setChanged();
                observable.notifyObservers();
            }
        }        
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_SPACE)
        {
            pressed = false;
            synchronized(PhysicsSync.getSync())
            {
                release.setGlobalValue(physicsClock.getMediaSeconds());                
            }
            observable.setChanged();
            observable.notifyObservers();
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0)
    {
                
    }

    @Override
    public boolean isPressed()
    {
        return pressed;
    }
    
}
