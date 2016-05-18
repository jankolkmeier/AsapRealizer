/**
 * Interfaces for lip sync providers. The typical setup is as follows:<br>
 * Lipsync providers are implemented for e.g. the AnimationEngine or FaceEngine and add, per Visime, some PlanUnit to their plan.
 * The implemented providers are registered in a SpeechEngine (TTSEngine, IncrementalTTSEngine) and called upon when some new Speech is constructed 
 * (e.g. in the Planner, or incrementally in some SpeechUnit).
 */
@hmi.util.NoEmptyClassWarning
package asap.realizer.lipsync;