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

import hmi.util.CircularBuffer;
import hmi.util.Clock;
import hmi.util.PhysicsSync;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;

/**
 * Anticipates the tempo of spacebar presses and aligns a list of user provided synchronization points to 
 * this anticipated tempo.
 * @author welberge
 *
 */
public class SpaceBarTempoAnticipator extends Anticipator implements KeyListener, KeyInfo
{
    private List<TimePeg> orderedSynchs = Collections.synchronizedList(new ArrayList<TimePeg>());    

    private Clock physicsClock;
    private boolean pressed = false;
    private SBAObservable observable;    
    private static final int PREDICTION_SIZE = 3;
    private CircularBuffer<Double> tempoBuffer = new CircularBuffer<Double>(PREDICTION_SIZE);
    private Logger logger = LoggerFactory.getLogger(SpaceBarTempoAnticipator.class.getName());
    
    private static class SBAObservable extends Observable
    {
        @Override
        public void setChanged()
        {
            super.setChanged();
        }
    }
    
    public SpaceBarTempoAnticipator(String id, PegBoard pb)
    {
        super(id,pb);
        observable = new SBAObservable();
    }
    
    /**
     * @return a read-only view of the time pegs handled by this anticipator
     */
    @Override
    public List<TimePeg> getTimePegs()
    {
        return Collections.unmodifiableList(orderedSynchs);
    }
    
    public void setPhysicsClock(Clock phClock)
    {
        physicsClock = phClock;        
    }
    
    @Override
    public void addSynchronisationPoint(String syncRef, TimePeg sp)
    {
        super.addSynchronisationPoint(syncRef,sp);
        orderedSynchs.add(sp);
    }
    
    public void updateTempo(double tempo, double startTime)
    {
        boolean update = false;
        double prevTime = 0;
        synchronized(orderedSynchs)
        {
            for(TimePeg p:orderedSynchs)
            {
                if(update)
                {
                    p.setGlobalValue(prevTime+tempo);
                    prevTime += tempo;
                }
                else if(p.getGlobalValue()==TimePeg.VALUE_UNKNOWN || p.getGlobalValue()>=startTime) 
                {
                    prevTime = startTime;
                    if(p.getGlobalValue()-startTime>tempo*0.5)
                    {
                        prevTime+=tempo;
                    }
                    update = true;
                }
            }
        }
    }
    
    
    public void addObserver(Observer o) 
    {
        observable.addObserver(o);
    }
    
    private double getTempo()
    {
        int i = 0;
        double totalTime = 0;
        double dPrev = 0;
        for(double d:tempoBuffer)
        {
            if(i>0)
            {                
                totalTime+=d-dPrev;             
            }
            dPrev = d;
            i++;
        }
        if(i>1)
        {
            logger.debug("tempo {}",totalTime/(i-1));
            return totalTime/(i-1);
        }
        logger.debug("tempo=1");
        return 1;
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
                    tempoBuffer.add(physicsClock.getMediaSeconds());
                    updateTempo(getTempo(),physicsClock.getMediaSeconds());                    
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
