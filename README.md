![logo](https://cdn.modrinth.com/data/cached_images/55730dca078c5f5d993037782ca33c6c534df966.png)

## License

This project's code is licensed under [ARR](LICENSE).
You can use it in your Modpack,
but if you would like clarification or more explicit permission join
the [discord server](https://discord.gg/tvqjhCxkwg).

![Divider](https://cdn.modrinth.com/data/cached_images/1ccd731a59f2b97cc9189ceb9b073d0b61476424.png)

## Depending on Dye Depot

Artifacts are published in a maven repository for all versions

```kotlin
repositories {
    maven {
        url = uri("https://registry.somethingcatchy.net/repository/maven-public/")
        content {
            includeGroup("com.possible-triangle")
        }
    }
}
```

<details>

<summary>Using Fabric Loom</summary>

```kotlin
dependencies {
    modImplementation("com.possible-triangle:dye_depot-$minecraft_version-fabric:$dye_depot_version")
}
```

</details>

<details>

<summary>Using NeoForge ModDevGradle</summary>

```kotlin
dependencies {
    implementation("com.possible-triangle:dye_depot-$minecraft_version-neoforge:$dye_depot_version")
}
```

</details>

<details>
<summary>Using ForgeGradle</summary>

```kotlin
dependencies {
    implementation(fg.deobf("com.possible-triangle:dye_depot-$minecraft_version-forge:$minecraft_version-$dye_depot_version"))
}
```

</details>
