# DRACO
## Distributed Real-time Asset Calculation & Observation Engine

> A distributed, event-driven financial risk engine built in Java that simulates how investment banks process live market data, continuously value portfolios, calculate risk, generate alerts, and maintain audit logs.

---

# Overview

DRACO is a backend simulation of a real-time financial risk engine inspired by the systems used in investment banks such as Goldman Sachs, JPMorgan Chase, and Morgan Stanley.

The system continuously generates market data, distributes incoming events across multiple worker threads, updates portfolio valuations, computes financial risk metrics, generates alerts when predefined thresholds are exceeded, and records every important system event through an immutable audit log.

The primary objective of this project is to demonstrate concepts in:

- Distributed Systems
- Concurrent Programming
- Event-Driven Architecture
- Object-Oriented Design
- Financial Risk Processing

---

# Features

- 📈 Live Market Data Simulation
- ⚡ Producer-Consumer Architecture
- 🧵 Multi-threaded Worker Processing
- 🔀 Symbol-Based Workload Partitioning
- 🗂 Thread-safe Market Data Cache
- 💼 Portfolio Management
- 💰 Portfolio Valuation Engine
- 📊 Risk Calculation Engine
- 🚨 Real-time Alert Generation
- 📝 Audit Logging
- 🔒 Immutable Domain Models
- ⚙ Layered Service Architecture

---

# Architecture

```text
                    +------------------------+
                    | Market Data Producer   |
                    +------------------------+
                               |
                               ▼
                    +------------------------+
                    |   Blocking Queue       |
                    +------------------------+
                               |
                               ▼
                    +------------------------+
                    |      Dispatcher        |
                    +------------------------+
                               |
          +--------------------+--------------------+
          |                    |                    |
          ▼                    ▼                    ▼
     Worker Node 1        Worker Node 2       Worker Node 3
          |                    |                    |
          +--------------------+--------------------+
                               |
                               ▼
                    +------------------------+
                    |   Portfolio Manager    |
                    +------------------------+
                               |
                               ▼
              +--------------------------------------+
              | Portfolio Valuation Service          |
              +--------------------------------------+
                               |
                               ▼
                    +------------------------+
                    |     Risk Service       |
                    +------------------------+
                               |
                               ▼
                    +------------------------+
                    |    Alert Service       |
                    +------------------------+
                               |
                               ▼
                    +------------------------+
                    |    Audit Logger        |
                    +------------------------+
```

---

# Project Structure

```text
src
│
├── cache
│     └── MarketDataCache.java
│
├── config
│     └── RiskConfiguration.java
│
├── dispatcher
│     └── Dispatcher.java
│
├── distributed
│     ├── Partitioner.java
│     └── WorkerNode.java
│
├── model
│     ├── Account.java
│     ├── Alert.java
│     ├── AlertType.java
│     ├── AuditEvent.java
│     ├── EventType.java
│     ├── MarketTick.java
│     ├── Portfolio.java
│     ├── PortfolioValuationResult.java
│     ├── Position.java
│     ├── RiskResult.java
│     └── ValuationResult.java
│
├── producer
│     └── MarketDataProducer.java
│
├── queue
│     └── MarketQueue.java
│
├── service
│     ├── AlertService.java
│     ├── AuditLogger.java
│     ├── PortfolioManager.java
│     ├── PortfolioValuationService.java
│     ├── RiskService.java
│     └── ValuationService.java
│
└── main
      └── Main.java
```

---

# Workflow

```text
Market Tick Generated
          │
          ▼
Added to Blocking Queue
          │
          ▼
Dispatcher assigns Worker
          │
          ▼
Worker updates Market Cache
          │
          ▼
Portfolio Manager processes affected portfolios
          │
          ▼
Portfolio Valuation
          │
          ▼
Risk Calculation
          │
          ▼
Alert Generation
          │
          ▼
Audit Event Logged
```

---

# Technologies Used

- Java
- Multithreading
- BlockingQueue
- ConcurrentHashMap
- Collections Framework
- Object-Oriented Programming
- Event-Driven Architecture
- Producer-Consumer Pattern
- Distributed Processing Concepts

---

# Core Concepts Demonstrated

## Java

- Classes & Objects
- Inheritance
- Encapsulation
- Immutable Objects
- Collections
- Enums

## Concurrency

- Threads
- Thread Synchronization
- BlockingQueue
- ConcurrentHashMap
- Worker Threads
- Producer-Consumer Pattern

## Distributed Systems

- Workload Partitioning
- Horizontal Scaling
- Worker Architecture
- Event Processing
- Fault Tolerance Concepts

## Software Engineering

- Layered Architecture
- Separation of Concerns
- SOLID Principles
- Service-Oriented Design
- Immutable DTOs

---

# Financial Concepts

The project demonstrates several financial concepts including:

- Market Data Processing
- Portfolio Management
- Cost Basis
- Market Value
- Unrealized Profit & Loss
- Exposure Calculation
- Concentration Risk
- Risk Scoring
- Real-Time Risk Monitoring

---

# Sample Output

```text
========================================
DRACO Risk Engine Started
========================================

Market Tick:
TSLA 610 -> 615

Worker 2 processing TSLA

Portfolio PORT-002 updated

Exposure: 1,245,000

Concentration Risk: 0.74

Alert Generated:
HIGH_EXPOSURE

Audit Event Logged
```

---

# Future Improvements

- Kafka-based Event Streaming
- Redis Market Data Cache
- PostgreSQL Audit Storage
- Spring Boot REST APIs
- Docker Support
- Kubernetes Deployment
- Prometheus Metrics
- Grafana Dashboard
- Portfolio Symbol Index
- Incremental Portfolio Valuation
- WebSocket Live Dashboard
- Distributed Cache Synchronization

---

# Learning Outcomes

This project demonstrates understanding of:

- Concurrent Programming
- Distributed Backend Systems
- Financial Risk Engines
- Event-Driven Architecture
- Thread Safety
- Java Collections
- Backend Software Design
- Real-Time Processing

---

# Resume Description

**DRACO** is a distributed event-driven financial risk engine implemented in Java that simulates real-time market data ingestion, portfolio valuation, risk scoring, alert generation, and audit logging using multithreading, concurrent data structures, and worker-based partitioning.

---

# How to Run

```bash
git clone <repository-url>

cd DRACO

javac src/main/Main.java

java src.main.Main
```

---

# Author

**Harish Raghav**

Developed as a backend systems project to explore distributed systems, concurrent programming, and financial risk processing in Java.