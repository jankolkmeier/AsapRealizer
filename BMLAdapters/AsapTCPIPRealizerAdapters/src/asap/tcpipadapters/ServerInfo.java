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
package asap.tcpipadapters;

/** 
See package documentation.

@author Dennis Reidsma
  */
public class ServerInfo
{
  
  private int bmlPort       = 7500;
  private int feedbackPort  = 7501;
  private String serverName = "127.0.0.1";
  
  public ServerInfo(String serverName, int bmlPort, int feedbackPort)
  {
    this.bmlPort      = bmlPort;
    this.feedbackPort = feedbackPort;
    this.serverName   = serverName;
  }
  
  public int getBmlPort()
  {
    return bmlPort;
  }
  public int getFeedbackPort()
  {
    return feedbackPort;
  }
  public String getServerName()
  {
    return serverName;
  }
  
  public ServerInfo copy()
  {
    return new ServerInfo(serverName, bmlPort, feedbackPort);
  }
  
}