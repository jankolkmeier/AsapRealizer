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
package asap.bml.ext.ssml.builder;

import saiba.bml.builder.BehaviourBuilder;
import saiba.bml.core.Behaviour;
import saiba.bml.core.SpeechBehaviour;

public class SSMLBehaviourBuilder
{
    private final BehaviourBuilder builder;
    private String ssmlContent = "";
    private String bmlContent = "";

    public SSMLBehaviourBuilder(String bmlId, String id)
    {
        builder = new BehaviourBuilder(SpeechBehaviour.xmlTag(), bmlId, id);
    }

    public SSMLBehaviourBuilder ssmlContent(String content)
    {
        ssmlContent = content;
        String str = content.replaceAll("<mark\\s+name", "{sync id");
        str = str.replaceAll("</mark>", "{/sync}");
        bmlContent = str.replaceAll("<.*?>", "");
        bmlContent = bmlContent.replaceAll("\\{sync", "<sync");
        bmlContent = bmlContent.replaceAll("\\{/sync\\}", "</sync>");
        return this;
    }

    public Behaviour build()
    {
        builder.content("<text>" + bmlContent + "</text>" + "<description type=\"application/ssml+xml\" priority=\"1\">"
                + "<speak xmlns=\"http://www.w3.org/2001/10/synthesis\">" + ssmlContent + "</speak>" + "</description>");
        return builder.build();
    }
}
