description = "DevOps Boot Schedule Common"

dependencies {
    api(project(":devops-boot-project:devops-boot-core:devops-webflux"))
    api(project(":devops-boot-project:devops-boot-core:devops-api"))
    api("com.squareup.okhttp3:okhttp")
}
