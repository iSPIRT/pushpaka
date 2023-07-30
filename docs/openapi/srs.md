# Software Requirements Specification

## For UTM Ecosystem

Version 0.1  
Prepared by <author>  
<organization>  
<date created>  

Table of Contents
=================
* [Revision History](#revision-history)
* 1 [Introduction](#1-introduction)
  * 1.1 [Document Purpose](#11-document-purpose)
  * 1.2 [Product Scope](#12-product-scope)
  * 1.3 [Definitions, Acronyms and Abbreviations](#13-definitions-acronyms-and-abbreviations)
  * 1.4 [References](#14-references)
  * 1.5 [Document Overview](#15-document-overview)
* 2 [Product Overview](#2-product-overview)
  * 2.1 [Product Perspective](#21-product-perspective)
  * 2.2 [Product Functions](#22-product-functions)
  * 2.3 [Product Constraints](#23-product-constraints)
  * 2.4 [User Characteristics](#24-user-characteristics)
  * 2.5 [Assumptions and Dependencies](#25-assumptions-and-dependencies)
  * 2.6 [Apportioning of Requirements](#26-apportioning-of-requirements)
* 3 [Requirements](#3-requirements)
  * 3.1 [External Interfaces](#31-external-interfaces)
    * 3.1.1 [User Interfaces](#311-user-interfaces)
    * 3.1.2 [Hardware Interfaces](#312-hardware-interfaces)
    * 3.1.3 [Software Interfaces](#313-software-interfaces)
  * 3.2 [Functional](#32-functional)
  * 3.3 [Quality of Service](#33-quality-of-service)
    * 3.3.1 [Performance](#331-performance)
    * 3.3.2 [Security](#332-security)
    * 3.3.3 [Reliability](#333-reliability)
    * 3.3.4 [Availability](#334-availability)
  * 3.4 [Compliance](#34-compliance)
  * 3.5 [Design and Implementation](#35-design-and-implementation)
    * 3.5.1 [Installation](#351-installation)
    * 3.5.2 [Distribution](#352-distribution)
    * 3.5.3 [Maintainability](#353-maintainability)
    * 3.5.4 [Reusability](#354-reusability)
    * 3.5.5 [Portability](#355-portability)
    * 3.5.6 [Cost](#356-cost)
    * 3.5.7 [Deadline](#357-deadline)
    * 3.5.8 [Proof of Concept](#358-proof-of-concept)
* 4 [Verification](#4-verification)
* 5 [Appendixes](#5-appendixes)

## Revision History
| Name | Date    | Reason For Changes  | Version   |
| ---- | ------- | ------------------- | --------- |
|      |         |                     |           |
|      |         |                     |           |
|      |         |                     |           |

## Services

| #  | Service | SRS | API Spec |
| -- | --      | --  | --       |
| 1  | Registration | [SRS](./registry/srs.md) | [API](./registry/api.yaml) |
| 2  | Flight Awareness | [SRS](./flight-awareness/srs.md) | [API](./flight-awareness/api.yaml) |
| 3  | Strategic Deconfliction | [SRS](./strategic-dconf/srs.md) | [API](./strategic-dconf/api.yaml) |
| 4  | Conflict Advisory and Alert | [SRS](./conflict-advisory/srs.md) | [API](./conflict-advisory/api.yaml) |
| 5  | Communication and Navigation | [SRS](./cns/srs.md) | [API](./cns/api.yaml) |
| 6  | Weather | [SRS](./weather/srs.md) | [API](./weather/api.yaml) |
| 7  | Dynamic Airspace Density | [SRS](./dynamic-airspace-density/srs.md) | [API](./dynamic-airspace-density/api.yaml) |
| 8  | Discovery | [SRS](./discovery/srs.md) | [API](./discovery/api.yaml) |
| 9  | Messaging | [SRS](./messaging/srs.md) | [API](./messaging/api.yaml) |
| 10 | Airspace Authorisation | [SRS](./airspace-authorisation/srs.md) | [API](./airspace-authorisation/api.yaml) |
| 11 | Incident Reporting | [SRS](./incident-reporting/srs.md) | [API](./incident-reporting/api.yaml) |
| 12 | Log Management | [SRS](./log-mgmt/srs.md) | [API](./log-mgmt/api.yaml) |
| 13 | Flight Planning | [SRS](./flight-planning/srs.md) | [API](./flight-planning/api.yaml) |
| 14 | Flight Notification | [SRS](./flight-notification/srs.md) | [API](./flight-notification/api.yaml) |
| 15 | Dynamic Reroute | [SRS](./dynamic-reroute/srs.md) | [API](./dynamic-reroute/api.yaml) |
| 16 | Conformance Monitoring | [SRS](./conformance-monitoring/srs.md) | [API](./conformance-monitoring/api.yaml) |
| 17 | Risk Reduction | [SRS](./risk-reduction/srs.md) | [API](./risk-reduction/api.yaml) |
| 18 | Surveillance | [SRS](./surveillance/srs.md) | [API](./surveillance/api.yaml) |
| 19 | Airspace Organization and Management | [SRS](./airspace-org-mgmt/srs.md) | [API](./airspace-org-mgmt/api.yaml) |
| 20 | Mapping | [SRS](./mapping/srs.md) | [API](./mapping/api.yaml) |
| 21 | Restriction Management | [SRS](./restriction-mgmt/srs.md) | [API](./restriction-mgmt/api.yaml) |
| 22 | Drone Swarm Management | [SRS](./swarm/srs.md) | [API](./swarm/api.yaml) |
| 23 | Drone Port Management | [SRS](./port/srs.md) | [API](./port/api.yaml) |
| 24 | Drone Corridor Management | [SRS](./corridor/srs.md) | [API](./corridor/api.yaml) |
| 25 | Anti-drone measures /c-UAS | [SRS](./cuas/srs.md) | [API](./cuas/api.yaml) |

