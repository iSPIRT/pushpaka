# UTM Concept of Operations

ID: `I05`  
Status: `WORKING DRAFT`  
Version: `1`  

**Authors**

* Sayandeep Purkayasth ([corresponding](mailto:sayandeep@deepcyan.ai?subject=Pushpaka I05 UTM Policy Comments))
* Hrishikesh Ballal
* Siddharth Shetty
* Manish Shukla
* Amit Garg
* Siddharth Ravikumar
* George Thomas

**Outline**  

[TOC]

## State of the art

See [here](./state-of-the-art.md).

## Actors

1. Manufacturer
1. Pilot
1. Operator
1. Insurer
1. Civil Aviation Authority
1. Data Consumer
1. UAS Owner
1. Law Enforcement
1. Security Agencies
1. Air Navigation Service Provider
1. Disaster Management Agencies
1. Military
1. BCAS
1. General Public
1. Other UTM-SP or instances thereof
1. Pilot Certifier
1. UAS Type Certifier
1. UAS Testing Agency

## Use cases

### Manufacturer

1. Manage Registration with CAA

### Pilot

1. Manage Registration with CAA
1. Plan a Flight
1. Apply for Flight Permit

### Operator

1. Manage Registration with CAA
1. Manage Pilots
1. Manage UASs

### Insurer

1. Accident Investigations

### Civil Aviation Authority

i.e. DGCA

1. Audit
	1. Pilot
	1. Operator
1. Manage registration
	1. Manufacturer
	1. Pilot
	1. Operator

### Data Consumer

Consumer for data collected by a UAS during an operation.

1. Data access

### UAS Ownership, etc.

1. Lease
1. Rent
1. Transfer
1. Buy
1. Scrap

### Law Enforcement

1. Find Pilot/Operator against FIR
1. Track UAS
1. Find Rogue UAS
1. Counter UAS action

### Security Agencies

1. Find Pilot/Operator against FIR
1. Track UAS
1. Find rogue UAS
1. Counter UAS action

### Air Navigation Service Provider

i.e. AAI

1. Flight permission in controlled airspace
1. Traffic Management
	1. Deconfliction

### Military

i.e. IAF, Navy, Coast Guard, HAL

1. Permission in applicable military controlled airspace
1. Traffic Management
	1. Deconfliction

### BCAS

1. Counter UAS action

### General Public

1. Legal action against Drone Pilot/Operator
1. Raise claim against accident

### Government/Semi-Governmental Bodies

1. UIDAI
1. AERA
1. DGFT
1. CIN MCA/RoC
1. GSTIN
1. MoF
1. QCI-CB
1. Passport, VISA/OCI (MEA)
1. WPC
1. Driving License RTO
1. Bharatkosh MoF

## UTM Architecture

### Considerations for design

### Services

#### Registration

1. Manufacturer Profile Management
1. Pilot Profile Management
1. UIN Application
1. Permission Management
1. Operator Profile Management
1. RPAS Acquisition/Transfer
1. UAOP Application

#### Operations

1. Flight Awareness
1. Strategic Deconfliction
1. Conflict Advisory and Alert
1. Communication and Navigation
1. Weather
1. Dynamic Airspace Density
1. Discovery
1. Messaging
1. Airspace Authorisation
1. Incident Reporting
1. Log Management
1. Flight Planning
1. Flight Notification
1. Dynamic Reroute
1. Conformance Monitoring
1. Risk Reduction
1. Surveillance
1. Airspace Organization and Management
1. Mapping
1. Restriction Management
1. Drone Swarm Management
1. Drone Port Management
1. Drone Corridor Management
1. Anti-drone measures /c-UAS
1. Emergency Management
1. Interface with ATC

## Rules of business

### High Priority activities

	923/2012 are:

* police and customs missions 
* traffic surveillance and pursuit missions 
* environmental control missions conducted by, or on behalf of public authorities 
* search and rescue 
* medical flights 
* evacuations 
* firefighting 
* exemptions required to ensure the security of flights by heads of State, Ministers and comparable State functionaries

## Ecosystem Evolution Timeline

### Phases (high level)

| Milestone | Services                                                                                                                                           | Timeline            |
| --------- | -------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------- |
| M0        | Registration, Restriction Management V0, Incident Reporting                                                                                        | Till Feb 2022       |
| M1        | Communication and Navigation, Flight Awareness, Permission Management, Discovery, Airspace Authorisation, Messaging                                | Feb 2022 - Feb 2023 |
| M2        | Flight Notification, Strategic Deconfliction, Conflict Advisory and Alert, Dynamic Airspace Density, Weather                                       | Feb 2023 - Feb 2024 |
| M3        | Emergency Management, Conformance Monitoring, Restriction Management V1, Log Management, Interface with ATC, Swarm Management, Corridor Management | Feb 2024 - Feb 2025 |

### Phases (detail)

| Milestone | Service                      | Description                                                                                         | Timeline      |
| --------- | ---------------------------- | --------------------------------------------------------------------------------------------------- | ------------- |
| M0        | Registration                 | Pilot, Manufacturer Org., Training Org., Operator Org. registration and profile management          | Till Feb 2022 |
|           | Restriction Management V0    | Airspace Rules                                                                                      |               |
|           | Incident Reporting           |                                                                                                     |               |
| M1        | Communication and Navigation |                                                                                                     | Till Feb 2023 |
|           | Flight Awareness             | Get info about flights in Volume of Interest                                                        |               |
|           | Permission Management        | Allow UTMs to issue flight permits                                                                  |               |
|           | Discovery                    | Allow UTMs to discover each other                                                                   |               |
|           | Airspace Authorisation       | Integration with Non-CAA authorities for seeking flight permission                                  |               |
|           | Messaging                    | Allow Pilots/Ops to create/search TFR/NOTAM/UVR                                                     |               |
| M2        | Flight Notification          | Get flights in AOI                                                                                  | Till Feb 2024 |
|           | Strategic Deconfliction      | Pre-flight deconfliction with issued flight plans based on volume restrictions and intersections    |               |
|           | Conflict Advisory and Alert  | Get constraints, advisory, directives for conflicts to aid pilot based deconfliction                |               |
|           | Dynamic Airspace Density     | Get expected airspace density for volume of interest                                                |               |
|           | Weather                      | Get wind, visibility, temperature, precipitation prediction for Flight                              |               |
| M3        | Emergency Management         | UVR/NOTAM/TFR preemption rules                                                                      | Till Feb 2025 |
|           | Conformance Monitoring       | Check Remote ID Tracking for conformance with buffers                                               |               |
|           | Restriction Management V1    |                                                                                                     |               |
|           | Swarm Management             | Allow UTMs to manage swarm coordination                                                             |               |
|           | Corridor Management          | Maintain Corridor geometries and operational constraints, allow UTMs to coordinate ops in corridors |               |
|           | Log Management               | Flight Log storage and analytics                                                                    |               |
|           | Interface with ATC           |                                                                                                     |               |
