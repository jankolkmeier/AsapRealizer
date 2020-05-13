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
package asap.realizerport;

/**
 * See package documentation.
 * @author welberge
 */
public interface RealizerPort
{
    /**
     * Add some listeners to which BML Feedback will be sent
     */
    void addListeners(BMLFeedbackListener ... listeners);
    
    
    void removeListener(BMLFeedbackListener l);
    
    /**
     * Removes all BMLListeners
     */
    void removeAllListeners();
    
    /**
     * Asks the realizer to perform a BML block. Non-blocking: this call will NOT block until the BML 
     * has been completely performed! It may block until the BML has been scheduled, though -- this is undetermined.
     */
    void performBML(String bmlString);
}