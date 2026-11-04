# 4. Dedicated Full-Page Analytics Tab View

## Context
Previously, the garden analytics (growth charts, variety distributions, weather correlation trends, and treatment logs) were trapped inside a floating pop-up modal (`#analytics-modal`). Squeezing three Chart.js visualizers, KPI cards, and log feeds into an 88vh modal resulted in cramped canvas dimensions, horizontal scrolling friction, and visual clutter characteristic of "AI slop". Furthermore, having both modal views and full-page views created an inconsistent navigation hierarchy.

## Decision
1. **Third Primary Tab (`Analitik Kebun`)**:
   - Promote Analytics from a pop-up dialog to a first-class top-level tab alongside `Kebun 3D` and `Manajemen Tanaman`.
   - The analytics workspace uses a clean, modern-minimal, data-dense layout with ample breathing room for time-series charts, responsive grid KPI cards, and an agronomical treatments timeline.
2. **Whole-Garden Aggregate by Default**:
   - Opening the Analytics tab defaults to aggregate garden telemetry (all 40 polybags combined), offering dropdown controls to filter by individual polybag (`PB-01` to `PB-40`) or time window (`7d`, `30d`, `hst`).
3. **Single Source of Truth**:
   - Retire the old pop-up modal mechanism. All analytics entry points (such as the navigation dock's `btn-analytics` and inspector cards) route seamlessly to `switchMainTab('analytics')`.
