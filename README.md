# Compose mini

A WIP set of helpers for building compose UI libraries from scratch.

While the Compose runtime itself is fairly simple to fit into new environments, Jetpack compose UI is a large project, with many decisions made specifically for Android that make it hard to reuse in other environments.

Our goal is to provide modules and guides to let custom UI implementations share code and choose what fits their needs best.

## Usage

### Dependencies

```kotlin
implementation("me.dvyy.compose.mini:<module>:x.y.z")
```

```toml
[versions]
compose-mini = "x.y.z"
[libraries]
compose-mini-runtime = { module = "me.dvyy.compose.mini:runtime", version.ref = "compose-mini" }
```

