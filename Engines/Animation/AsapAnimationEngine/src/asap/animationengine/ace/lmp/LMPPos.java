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
package asap.animationengine.ace.lmp;

import asap.animationengine.ace.GuidingSequence;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;

/**
 * Base class of all local motor programs for positioning in Cartesian space.
 * @author hvanwelbergen
 * @author Stefan Kopp (original C++ version)
 */
public abstract class LMPPos extends LMP
{
    public LMPPos(FeedbackManager bbf, BMLBlockPeg bmlBlockPeg, String bmlId, String id, PegBoard pb)
    {
        super(bbf, bmlBlockPeg, bmlId, id, pb);        
    }
    protected GuidingSequence gSeq;    

    public void setGuidingSeq(GuidingSequence gSeq)
    {
        /*
         * if (gSeq!=0) delete gSeq;
         * gSeq = seq.clone();
         * 
         * if ( gSeq!=0 && !gSeq->empty() )
         * {
         * //setStartTime(gstrokes.front().sT);
         * setEndTime( gSeq->getEndTime() );
         * 
         * // transform guiding strokes into local frame of reference
         * gSeq->transform( baseFrame );
         * }
         * else
         * cerr << "LMP_Pos::setGuidingSeq : empty trajectory!!" << endl;
         */
        this.gSeq = gSeq;
    }
    
   
}
