# 3. Two-Tab Navigation Paradigm and Fast Plant Insertion Flow

## Context
The single-screen 3D HUD previously forced administrative data, modals, and telemetry into floating glassmorphism widgets overlaying the Three.js viewport. Users identified this as cluttered and generic "AI slop", particularly suffering from ambiguity regarding where and how to insert new plants into the garden. While the backend possessed a mature CRUD API (`/api/tanaman`), the frontend hardcoded 40 polybags without an intuitive insertion flow.

## Decision
1. **Two-Tab Structural Navigation**:
   - **Tab 1 (`Kebun 3D`)**: Clean, unobstructed 3D greenhouse view with minimal micro-HUD telemetry and direct interaction on plant meshes and empty polybag slots.
   - **Tab 2 (`Manajemen Tanaman`)**: Grounded, high-contrast, data-dense workstation with solid cards, full plant inventory table, health filtering, and direct CRUD actions.
2. **Minimalist Fast Plant Insertion Flow**:
   - Introduce a prominent primary button `+ Tambah Tanaman` at the head of the Management view, as well as clickable empty polybags in the 3D viewport.
   - The insertion modal requires only two user inputs: **Nama Tanaman** and **Varietas**. The system automatically computes and assigns the next available polybag ID (`PB-xx`) and sets today's planting date before issuing `POST /api/tanaman`.
3. **Anti-Slop Materiality**:
   - Limit translucent glassmorphism to subtle status accents. Use warm-white matte surfaces, crisp hairline borders, and high-contrast typography for core data cards to optimize legibility and operational trust.
