# agrisync-dtr-submodule

# AgriSync Daily Time Record (DTR) Web Subsystem

An elegant, standalone administrative payroll-auditing and text-stream parsing application engineered to process agricultural workforce tracking data with absolute mathematical and logical precision. 

This repository serves as the definitive source code workspace for the core operational sub-module designed under strict Software Requirements Specification (SRS) engineering guardrails.

---

## 🚀 Key Architectural Highlights

* **Vibe Coding Foundations:** Built entirely using explicit prompt engineering parameters within automated AI IDE workflows to translate business rules directly into verified, type-safe Java production files.
* **Asynchronous Text Stream Parsing:** Implements the native browser `FileReader API` to split and process raw CSV files row by row via optimized regular expressions (`/\r?\n/`) without locking the client interface threads.
* **Volatile In-Memory Security Layer:** Secures administrative access privileges using a thread-safe, memory-isolated `ConcurrentHashMap` session collection, keeping transient state properties completely clear of database data-hazard exposure.
* **Pre-Commit Ingestion Guardrails:** Intercepts data-save execution lines to check records in real time, dynamically catching registration dropouts (flagged with pale red `.row-invalid` style alerts) or duplicate calendar date conflicts (flagged with amber `.row-duplicate` warnings) before running repository database commits.

---

## 🛠️ Technology Stack Ecosystem

* **Backend Framework:** Java 17 / Spring Boot 3+ / Spring Data JPA
* **Database Management System:** MySQL Relational Database (Target Schema: `mauswag`)
* **Client User Interface:** Semantic HTML5 / Tailwind CSS Framework Typography / Lucide Vector Icons
* **Boilerplate Utility:** Project Lombok Annotations (`@Data`, `@RequiredArgsConstructor`)

---

## 📖 Deployment, Installation, and Database Setup

To configure your local environment variables, generate the physical MySQL relational entity schemas, seed initial administrative authentication profiles, and boot the embedded Apache Tomcat web server using Maven, please consult our step-by-step deployment document:

👉 **Read the full deployment instructions inside [HELP.md](./HELP.md).**

---

## 📋 Software Requirements Specification (SRS)
The system's behavioral models, precise data dictionaries, and structural interface mappings are detailed extensively within the official **AgriSync Systems SRS Document (Section 01–10)**, compiled under an editorial Minimalist Beige Magazine layout format.
