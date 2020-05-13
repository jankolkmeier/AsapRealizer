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
package asap.ipaacaiuadapters;

/**
 * Constants for categories and payload keys
 * @author Herwin
 *
 */
public final class IpaacaBMLConstants
{
    private IpaacaBMLConstants(){}
    
    public static final String REALIZER_REQUEST_CATEGORY = "realizerRequest";
    public static final String REALIZER_FEEDBACK_CATEGORY = "realizerFeedback";
    public static final String REALIZER_REQUEST_KEY = "request";
    public static final String REALIZER_REQUEST_TYPE_KEY = "type"; // TODO: request type unused atm
    public static final String REALIZER_REQUEST_TYPE_BML = "bml";  //
    public static final String REALIZER_REQUEST_TYPE_BMLFILE = "bmlfile";  //
    public static final String BML_FEEDBACK_KEY = "bmlfeedback";
    public static final String BML_ID_KEY = "bmlid";
    public static final String IU_STATUS_KEY = "status";
    public static final String LAST_SYNC_ID_KEY = "lastSyncId";
    public static final String IU_ERROR_KEY = "error";
    public static final String IU_PREDICTED_END_TIME_KEY = "predictedEndTime";
    public static final String IU_PREDICTED_START_TIME_KEY = "predictedStartTime";
}
