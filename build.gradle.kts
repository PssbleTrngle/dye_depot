plugins {
    id("com.possible-triangle.neoforge")
}

neoforge {
    accessTransformer()

    dataGen {
        splitSourceSet()

        existing("supplementaries")
        existing("suppsquared")
    }
}

repositories {
    maven {
        url = uri("https://maven.blamejared.com/")
        content {
            includeGroup("mezz.jei")
        }
    }

    nexus {
        content {
            includeGroup("com.possible-triangle")
        }
    }
}

dependencies {
    modCompileOnly(libs.jei.common.api)
    modCompileOnly(libs.jei.api)
    modRuntimeOnly(libs.jei)

    modImplementation(pack.modrinth.moonlight)
    modImplementation(pack.modrinth.supplementaries)
    modImplementation(pack.modrinth.supplementaries.squared)

    modImplementation(libs.multikulti.datagen)
}

enableSpotless()
