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
package asap.bml.ext.bmla.feedback;

/**
 * Status of the BML block<br>
 * NONE
 * IN_PREP:         The BML realizer is currently scheduling the behavior (predictionFeedback)<br>
 * PENDING:         The BML realizer has scheduled the BML behavior block and it's waiting to be activated (predictionFeedback)<br>
 * LURKING:         The BML behavior block is waiting to be executed (on append targets) (predictionFeedback)<br>
 * IN_EXEC:         The BML behavior block is currently being executed by the realizer (blockProgress, start)<br>
 * SUBSIDING:       The BML behavior block is currently subsiding, that is all behaviors are currently either done or in their relax phase (blockProgress, relax)<br>
 * DONE:            The BML behavior block has been executed (blockProgress, end)<br> 
 * REVOKED:         The BML behavior block is revoked before executed is started (blockProgress, end)<br>
 * INTERRUPTED:     The BML behavior block is interrupted during execution (blockProgress, end)<br>
 * FAILED:          The BML realizer could not realize the BML behavior block (blockProgress, end)<br>
 * @author hvanwelbergen
 *
 */
public enum BMLABlockStatus
{
    NONE, IN_PREP, PENDING, LURKING, IN_EXEC, SUBSIDING, DONE, REVOKED, INTERRUPTED, FAILED;
}
