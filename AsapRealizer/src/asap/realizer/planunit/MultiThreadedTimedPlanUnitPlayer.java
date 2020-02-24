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
package asap.realizer.planunit;

import hmi.util.RuntimeExceptionLoggingRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

/**
 * Plays or stops a TimedPlanUnit at time t. This playback/stopping is ran in a separate thread.
 * The MultiThreadedTimedPlanUnitPlayer makes sure that the same TimedPlanUnit is not played/stopped twice (or more often)
 * concurrently.
 * @author welberge
 */
public class MultiThreadedTimedPlanUnitPlayer extends AbstractTimedPlanUnitPlayer
{
    private final static int NUM_THREADS = 25;
    private Map<TimedPlanUnit, CountDownLatch> playMap = new HashMap<TimedPlanUnit, CountDownLatch >();
    private final ExecutorService exec = Executors.newFixedThreadPool(NUM_THREADS);
    
    public MultiThreadedTimedPlanUnitPlayer()
    {
        playExceptions = Collections.synchronizedList(new ArrayList<TimedPlanUnitPlayException>());
        stopExceptions = Collections.synchronizedList(new ArrayList<TimedPlanUnitPlayException>());    
    }
    
    public void playUnit(TimedPlanUnit su, double t)
    {
        CountDownLatch prevLatch = playMap.get(su);
        CountDownLatch curLatch = new CountDownLatch(1);
        playMap.put(su, curLatch);
        exec.submit(new RuntimeExceptionLoggingRunnable(new TPUPlayRunner(su,t,prevLatch,curLatch)));        
    }
    
    @Override
    public void stopUnit(TimedPlanUnit su, double t)
    {
        CountDownLatch prevLatch = playMap.get(su);
        CountDownLatch curLatch = new CountDownLatch(1);
        playMap.put(su, curLatch);
        exec.submit(new RuntimeExceptionLoggingRunnable(new TPUStopRunner(su,t,prevLatch,curLatch)));            
    }
    
    class TPUStopRunner implements Runnable
    {
        private final double time;
        private final TimedPlanUnit tpu;
        private final CountDownLatch prevLatch;
        private final CountDownLatch curLatch;
        
        public TPUStopRunner(TimedPlanUnit tpu, double t, CountDownLatch prev, CountDownLatch cur)
        {
            this.tpu = tpu;
            this.time = t;
            prevLatch = prev;
            curLatch = cur;
        }
        @Override
        public void run()
        {
            if(prevLatch!=null)
            {
                try
                {
                    prevLatch.await();
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
            try
            {
                tpu.stop(time);
            }
            catch (TimedPlanUnitPlayException e)
            {
                stopExceptions.add(e);
            }
            curLatch.countDown();
            
        }        
    }
    
    class TPUPlayRunner implements Runnable
    {
        private final double time;
        private final TimedPlanUnit tpu;
        private final CountDownLatch prevLatch;
        private final CountDownLatch curLatch;
        
        public TPUPlayRunner(TimedPlanUnit tpu, double t, CountDownLatch prev, CountDownLatch cur)
        {
            this.tpu = tpu;
            this.time = t;
            prevLatch = prev;
            curLatch = cur;
        }

        @Override
        public void run()
        {
            if(prevLatch!=null)
            {
                try
                {
                    prevLatch.await();
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
            try
            {
                if (tpu.getState().isLurking())
                {
                    tpu.start(time);
                }
                tpu.play(time);
            }
            catch (TimedPlanUnitPlayException e)
            {
                playExceptions.add(e);
            }
            curLatch.countDown();
        }
    }

    @Override
    public ImmutableCollection<TimedPlanUnitPlayException> getPlayExceptions()
    {
        ImmutableList<TimedPlanUnitPlayException>ex;
        synchronized(playExceptions)
        {
            ex = new ImmutableList.Builder<TimedPlanUnitPlayException>().addAll(playExceptions).build();
        }
        return ex;
    }

    @Override
    public ImmutableCollection<TimedPlanUnitPlayException> getStopExceptions()
    {
        ImmutableList<TimedPlanUnitPlayException>ex;
        synchronized(stopExceptions)
        {
            ex = new ImmutableList.Builder<TimedPlanUnitPlayException>().addAll(stopExceptions).build();
        }
        return ex;
    }    
}
