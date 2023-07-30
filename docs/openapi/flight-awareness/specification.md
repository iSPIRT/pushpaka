Sayandeep
Actor: Operator
Action: Define operation volume (incl. Contingency volume), send to DSP, recv existing or future potential hazards & flight restrictions

Dependents: Permission Service
Nihal
Criticality : Feature that can be added by some DSPs, but is not essential for Operations
Related : pre-departure planning
Stage: before ops and perm
Helps in planning before seeking permission.  

Open Questions:
How are DSP’s talking to each other?
	Central exchange? - UFII?


Merged
Regulated: No
Priority: High
Critical: Not essential for Operations
Related: Planning
Stage: before ops and permission
APIs required: UAO/S-DSP, DSP-DSP, DSP-UFII

8.1 API: UAO/S-DSP
Input: 
Purpose of mission
Operational Volume
(mandatory) Geofence
(mandatory) Time range
(optionally) waypoints + contingency radius
(optionally) volume
Device Details
Operator Clearance
Output:
Flight restrictions
Hazards

8.2 API: DSP-DSP
Input:
Purpose of mission
Search space (3D volume)
Output:
Flight restrictions
Hazards

8.3 API: DSP-UFII
Input:
Search volume
Output:
List of DSPs operating in 

Open questions
Does UFII provide routing mechanism for DSP-DSP communications?
