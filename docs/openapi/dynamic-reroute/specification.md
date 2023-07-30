Sayandeep
Depends on
Surveillance Service
Flight Awareness
Conflict Advisory & Alert
Conformance Monitoring
Strategic Deconfliction

15.1 API: UAO/S-DSP
Input: 
Mission ID
Realtime position, velocity, acceleration , trajectory and device info
Output:
Suggested deconflicted UAS operational volume(s) from current time

15.2 API: DSP-DSP
Input: 
Mission ID
Realtime position
Output:
Suggested deconflicted UAS operational volume(s) from current time (pushed back to other DSPs)

15.3 API: DSP ← UFII
Input: Nil
Output:
Stream[Position for Missions]
Nihal
Merged
