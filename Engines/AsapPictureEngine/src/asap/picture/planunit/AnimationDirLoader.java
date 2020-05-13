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
package asap.picture.planunit;

import hmi.util.Resources;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import asap.picture.display.PictureDisplay;

public class AnimationDirLoader {

    private String animationDir = "";
    private String resourceDir = "";
    private ArrayList<String> imageIds;
    private PictureDisplay display = null;

    public AnimationDirLoader(String resourceDir, String animationDir, PictureDisplay display) {
        if (!resourceDir.endsWith("/")) {
            resourceDir += "/";
        }
        this.resourceDir = resourceDir;
        if (!animationDir.endsWith("/")) {
            animationDir += "/";
        }
        this.animationDir = animationDir;
        this.display = display;
        loadImages();
    }

    private void loadImages() {
        imageIds = new ArrayList<String>();

        File dir;
        try {
            dir = getDirHandle();
            File[] files = dir.listFiles();
            for (File file : files) {
                String imageId = resourceDir + animationDir + file.getName();
                System.out.println("id:" + imageId + " path:" + resourceDir + animationDir + " name" + file.getName());
                display.preloadImage(imageId, resourceDir + animationDir, file.getName());
                imageIds.add(imageId);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private File getDirHandle() throws URISyntaxException {
        Resources r = new Resources(resourceDir);
        URL u = r.getURL(animationDir);
        URI uri = u.toURI();
        File f = new File(uri);
        return f;
    }

    public int getNumberOfImages() {
        return imageIds.size();
    }

    public String getImageId(int n) {
        return imageIds.get(n);
    }
}
