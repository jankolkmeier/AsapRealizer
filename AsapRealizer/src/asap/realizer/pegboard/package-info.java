/**
 * The PegBoard is AsapRealizer's flexible behavior plan representation. It maintains a set of TimePegs that symbolically link to the synchronization points of 
 * behaviors that are constrained to be at the same time. The timing of these TimePegs may be updated, which moves the timing of the associated synchronization points, 
 * but maintains the time constraints specified upon them. The PegBoard thus allows one to do timing modifications of the behavior plan as it is being executed, 
 * but in such a way that BML constraints remain satisfied and no expensive re-scheduling is needed.
 */
@hmi.util.NoEmptyClassWarning
package asap.realizer.pegboard;