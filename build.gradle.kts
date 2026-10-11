plugins {
    id("com.possible-triangle.fabric")
}

fabric {
    accessWidener()
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
            includeGroup("io.github.fabricators_of_create.Porting-Lib")
            includeGroup("com.possible-triangle")
        }
    }
}

dependencies {
    modCompileOnly(libs.jei.common.api)
    modRuntimeOnly(libs.jei)

    modImplementation(pack.modrinth.moonlight)
    modImplementation(pack.modrinth.supplementaries)
    modImplementation(pack.modrinth.supplementaries.squared)

    // for data generation
    modImplementation(libs.porting.lib.models)
    modImplementation(libs.multikulti.datagen)
}

enableSpotless()
