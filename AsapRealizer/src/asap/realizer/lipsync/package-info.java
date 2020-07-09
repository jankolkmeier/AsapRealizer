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
/**
 * Interfaces for lip sync providers. The typical setup is as follows:<br>
 * Lipsync providers are implemented for e.g. the AnimationEngine or FaceEngine and add, per Visime, some PlanUnit to their plan.
 * The implemented providers are registered in a SpeechEngine (TTSEngine, IncrementalTTSEngine) and called upon when some new Speech is constructed 
 * (e.g. in the Planner, or incrementally in some SpeechUnit).
 */
@hmi.util.NoEmptyClassWarning
package asap.realizer.lipsync;