# DRACO

I use DRACO to explore how a market-price update flows through a queue into portfolio valuation and threshold alerts. It is a Java simulation running in **one JVM**, with local worker threads partitioned by symbol.

## Run the example

Use JDK 21 (including `javac`), from this repository's root. On Linux/macOS or Git Bash:

```sh
mkdir -p out
find src tests -name '*.java' > sources.txt
javac -d out @sources.txt
java -cp out src.Main
java -cp out tests.RegressionTest
```

The finite example submits three ticks, drains both workers, and exits. Its final market value is **32500.00**, with unrealized P&L **2500.00**. No accounts, external market feeds, or paid services are needed.

## What happens to a tick

`Dispatcher` chooses a partition using the symbol; the partition owner receives the tick in a bounded FIFO queue. Workers call `PortfolioManager`, which updates the shared price cache and values affected portfolios under one lock. This makes each update and its resulting valuation consistent with other worker updates. It also deliberately serializes valuation; extra workers do not imply more valuation throughput.

I keep cost basis separate from current value. Exposure is total long-position market value; concentration is the largest position divided by exposure, or zero for an empty portfolio. The weighted risk score and alert thresholds are illustrative rules, not a calibrated financial risk model.

## Checks and boundaries

The regression runner checks queue draining, FIFO updates, P&L, empty portfolios, missing prices, and rejection after shutdown. Audit output includes the event type and portfolio instead of Java object addresses.

Positions are configured before processing starts. Prices and positions must be finite/nonnegative as appropriate; short positions are outside this model. This example uses floating-point values, not accounting-grade decimal money.

There is no network distribution, durable event log, replay, or automatic failover. Audit records live in memory. `HeartBeatManager` and `RecoveryManager` are early sketches and are not wired into the demo; changing a partition owner does not recover queued messages. A failed valuation is reported during worker shutdown. Cross-symbol processing order is unspecified.

## Where I would extend it

My next step is an explicit event sequence and replay test before adding a message broker. A console recording is sufficient for this repository; hosting a permanent service would not demonstrate the queue's correctness better.
