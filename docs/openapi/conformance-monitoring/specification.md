Sayandeep
Monitors conformance of a UAS position to pre-departure operational volume.
16.1 API: UAO/S → DSP
Input:
Mission ID
Stream<Position, Velocity, Acceleration>
Processing:
Calculate Conformance Volume = conformance threshold applied between Flight Geography & Operation Volume
Output:
Stream<Notification>
Notification of
Deviation from Flight Geography

16.2 API: DSP → DSP
Input: 
Search Volume
Output:
Stream<Notification>
Notification of
Deviation from Conformance Volume for any active Mission

16.3 API: DSP → UFII
Input: Nil
Output:
Stream<Notification>
Notification of
Deviation from Conformance Volume for any active Mission
