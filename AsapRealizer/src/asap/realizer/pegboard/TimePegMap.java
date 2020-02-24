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
package asap.realizer.pegboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.jcip.annotations.ThreadSafe;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

;

//XXX if more performance/flexibility is needed, perhaps move to a hypersql database (http://hsqldb.org/)?
@ThreadSafe
final class TimePegMap
{
    private Multimap<TimePeg, PegKey> inversePegMap = HashMultimap.create();
    private Map<PegKey, TimePeg> pegMap = new HashMap<PegKey, TimePeg>();

    public synchronized void put(PegKey key, TimePeg tp)
    {
        pegMap.put(key, tp);
        inversePegMap.put(tp.getLink(), key);
    }

    public synchronized TimePeg get(PegKey key)
    {
        return pegMap.get(key);
    }

    public synchronized Collection<Entry<PegKey, TimePeg>> getEntries(final String bmlId, final String behaviorId)
    {
        Collection<Entry<PegKey, TimePeg>> entries = Collections2.filter(pegMap.entrySet(), new Predicate<Entry<PegKey, TimePeg>>()
        {
            @Override
            public boolean apply(Entry<PegKey, TimePeg> arg)
            {
                return arg.getKey().bmlId.equals(bmlId) && arg.getKey().id.equals(behaviorId);
            }
        });
        return entries;
    }

    private synchronized Collection<Entry<PegKey, TimePeg>> getEntries(final String bmlId)
    {
        Collection<Entry<PegKey, TimePeg>> entries = Collections2.filter(pegMap.entrySet(), new Predicate<Entry<PegKey, TimePeg>>()
        {
            @Override
            public boolean apply(Entry<PegKey, TimePeg> arg)
            {
                return arg.getKey().bmlId.equals(bmlId);
            }
        });
        return entries;
    }

    public synchronized ImmutableSet<String> getBehaviours(final String bmlId)
    {
        Collection<Entry<PegKey, TimePeg>> entries = getEntries(bmlId);
        Collection<String> behCol = Collections2.transform(entries, new Function<Entry<PegKey, TimePeg>, String>()
        {
            @Override
            public String apply(Entry<PegKey, TimePeg> arg)
            {
                return arg.getKey().id;
            }
        });
        return ImmutableSet.copyOf(behCol);
    }

    public synchronized ImmutableSet<String> getSyncs(final String bmlId, final String behaviorId)
    {
        Collection<Entry<PegKey, TimePeg>> entries = getEntries(bmlId, behaviorId);
        Collection<String> syncCol = Collections2.transform(entries, new Function<Entry<PegKey, TimePeg>, String>()
        {
            @Override
            public String apply(Entry<PegKey, TimePeg> arg)
            {
                return arg.getKey().syncId;
            }
        });
        return ImmutableSet.copyOf(syncCol);
    }

    public synchronized ImmutableSet<TimePeg> get(final String bmlId, final String behaviorId)
    {
        Collection<Entry<PegKey, TimePeg>> entries = getEntries(bmlId, behaviorId);
        Collection<TimePeg> pegCol = Collections2.transform(entries, new Function<Entry<PegKey, TimePeg>, TimePeg>()
        {

            @Override
            public TimePeg apply(Entry<PegKey, TimePeg> arg)
            {
                return arg.getValue();
            }
        });
        return ImmutableSet.copyOf(pegCol);
    }

    /**
     * Get all PegKeys linked to tp (also through offset pegs)
     */
    public synchronized ImmutableSet<PegKey> get(TimePeg tp)
    {
        return ImmutableSet.copyOf(inversePegMap.get(tp.getLink()));
    }

    public synchronized void clear()
    {
        pegMap.clear();
        inversePegMap.clear();
    }

    public synchronized ImmutableMap<PegKey, TimePeg> getTimePegMap()
    {
        return ImmutableMap.copyOf(pegMap);
    }

    public synchronized ImmutableSet<PegKey> getPegKeySet()
    {
        return ImmutableSet.copyOf(pegMap.keySet());
    }

    public synchronized void removeBehaviour(String bmlId, String id)
    {
        List<Entry<PegKey, TimePeg>> removeEntries = new ArrayList<Entry<PegKey, TimePeg>>();
        for (Entry<PegKey, TimePeg> entry : pegMap.entrySet())
        {
            if (entry.getKey().getId().equals(id) && entry.getKey().getBmlId().equals(bmlId))
            {
                removeEntries.add(entry);
            }
        }

        for (Entry<PegKey, TimePeg> entry : removeEntries)
        {
            pegMap.remove(entry.getKey());
            Collection<PegKey> pks = inversePegMap.get(entry.getValue().getLink());
            pks.remove(entry.getKey());
        }
    }

    public synchronized void shiftCluster(BehaviorCluster bc, double shift)
    {
        Set<TimePeg> pegsToShift = new HashSet<TimePeg>();
        for (BehaviorKey bk : bc.getBehaviors())
        {
            pegsToShift.addAll(get(bk.getBmlId(), bk.getBehaviorId()));
        }

        for (TimePeg tp : pegsToShift)
        {
            if (tp.getGlobalValue() != TimePeg.VALUE_UNKNOWN)
            {
                tp.setLocalValue(tp.getLocalValue() + shift);
            }
        }
    }
}
